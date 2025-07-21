package com.netease.ntunisdk.okhttp3;

import androidx.core.app.NotificationCompat;
import com.netease.ntunisdk.okhttp3.internal.NamedRunnable;
import com.netease.ntunisdk.okhttp3.internal.Util;
import com.netease.ntunisdk.okhttp3.internal.cache.CacheInterceptor;
import com.netease.ntunisdk.okhttp3.internal.connection.ConnectInterceptor;
import com.netease.ntunisdk.okhttp3.internal.connection.StreamAllocation;
import com.netease.ntunisdk.okhttp3.internal.http.BridgeInterceptor;
import com.netease.ntunisdk.okhttp3.internal.http.CallServerInterceptor;
import com.netease.ntunisdk.okhttp3.internal.http.RealInterceptorChain;
import com.netease.ntunisdk.okhttp3.internal.http.RetryAndFollowUpInterceptor;
import com.netease.ntunisdk.okhttp3.internal.platform.Platform;
import com.netease.ntunisdk.okio.AsyncTimeout;
import com.netease.ntunisdk.okio.Timeout;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

/* loaded from: classes2.dex */
final class RealCall implements Call {
    final OkHttpClient client;

    @Nullable
    private EventListener eventListener;
    private boolean executed;
    final boolean forWebSocket;
    final Request originalRequest;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
    final AsyncTimeout timeout;

    final class AsyncCall extends NamedRunnable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final Callback responseCallback;

        AsyncCall(Callback callback) {
            super("OkHttp %s", RealCall.this.redactedUrl());
            this.responseCallback = callback;
        }

        @Override // com.netease.ntunisdk.okhttp3.internal.NamedRunnable
        protected void execute() {
            RealCall.this.timeout.enter();
            boolean z = false;
            try {
                try {
                } catch (IOException e) {
                    e = e;
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    this.responseCallback.onResponse(RealCall.this, RealCall.this.getResponseWithInterceptorChain());
                } catch (IOException e2) {
                    e = e2;
                    z = true;
                    IOException iOExceptionTimeoutExit = RealCall.this.timeoutExit(e);
                    if (z) {
                        Platform.get().log(4, "Callback failure for " + RealCall.this.toLoggableString(), iOExceptionTimeoutExit);
                    } else {
                        RealCall.this.eventListener.callFailed(RealCall.this, iOExceptionTimeoutExit);
                        this.responseCallback.onFailure(RealCall.this, iOExceptionTimeoutExit);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    z = true;
                    RealCall.this.cancel();
                    if (!z) {
                        this.responseCallback.onFailure(RealCall.this, new IOException("canceled due to " + th));
                    }
                    throw th;
                }
            } finally {
                RealCall.this.client.dispatcher().finished(this);
            }
        }

        void executeOn(ExecutorService executorService) {
            try {
                try {
                    executorService.execute(this);
                } catch (RejectedExecutionException e) {
                    InterruptedIOException interruptedIOException = new InterruptedIOException("executor rejected");
                    interruptedIOException.initCause(e);
                    RealCall.this.eventListener.callFailed(RealCall.this, interruptedIOException);
                    this.responseCallback.onFailure(RealCall.this, interruptedIOException);
                    RealCall.this.client.dispatcher().finished(this);
                }
            } catch (Throwable th) {
                RealCall.this.client.dispatcher().finished(this);
                throw th;
            }
        }

        RealCall get() {
            return RealCall.this;
        }

        String host() {
            return RealCall.this.originalRequest.url().host();
        }

        Request request() {
            return RealCall.this.originalRequest;
        }
    }

    private RealCall(OkHttpClient okHttpClient, Request request, boolean z) {
        this.client = okHttpClient;
        this.originalRequest = request;
        this.forWebSocket = z;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(okHttpClient, z);
        AsyncTimeout asyncTimeout = new AsyncTimeout() { // from class: com.netease.ntunisdk.okhttp3.RealCall.1
            @Override // com.netease.ntunisdk.okio.AsyncTimeout
            protected void timedOut() throws IOException {
                RealCall.this.cancel();
            }
        };
        this.timeout = asyncTimeout;
        asyncTimeout.timeout(okHttpClient.callTimeoutMillis(), TimeUnit.MILLISECONDS);
    }

    private void captureCallStackTrace() {
        this.retryAndFollowUpInterceptor.setCallStackTrace(Platform.get().getStackTraceForCloseable("response.body().close()"));
    }

    static RealCall newRealCall(OkHttpClient okHttpClient, Request request, boolean z) {
        RealCall realCall = new RealCall(okHttpClient, request, z);
        realCall.eventListener = okHttpClient.eventListenerFactory().create(realCall);
        return realCall;
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public void cancel() throws IOException {
        this.retryAndFollowUpInterceptor.cancel();
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public RealCall m522clone() {
        return newRealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public void enqueue(Callback callback) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        this.eventListener.callStart(this);
        this.client.dispatcher().enqueue(new AsyncCall(callback));
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        this.timeout.enter();
        this.eventListener.callStart(this);
        try {
            try {
                this.client.dispatcher().executed(this);
                Response responseWithInterceptorChain = getResponseWithInterceptorChain();
                if (responseWithInterceptorChain != null) {
                    return responseWithInterceptorChain;
                }
                throw new IOException("Canceled");
            } catch (IOException e) {
                IOException iOExceptionTimeoutExit = timeoutExit(e);
                this.eventListener.callFailed(this, iOExceptionTimeoutExit);
                throw iOExceptionTimeoutExit;
            }
        } finally {
            this.client.dispatcher().finished(this);
        }
    }

    Response getResponseWithInterceptorChain() throws IOException {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.client.interceptors());
        arrayList.add(this.retryAndFollowUpInterceptor);
        arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
        arrayList.add(new CacheInterceptor(this.client.internalCache()));
        arrayList.add(new ConnectInterceptor(this.client));
        if (!this.forWebSocket) {
            arrayList.addAll(this.client.networkInterceptors());
        }
        arrayList.add(new CallServerInterceptor(this.forWebSocket));
        Response responseProceed = new RealInterceptorChain(arrayList, null, null, null, 0, this.originalRequest, this, this.eventListener, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis()).proceed(this.originalRequest);
        if (!this.retryAndFollowUpInterceptor.isCanceled()) {
            return responseProceed;
        }
        Util.closeQuietly(responseProceed);
        throw new IOException("Canceled");
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public synchronized boolean isExecuted() {
        return this.executed;
    }

    String redactedUrl() {
        return this.originalRequest.url().redact();
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public Request request() {
        return this.originalRequest;
    }

    StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }

    @Override // com.netease.ntunisdk.okhttp3.Call
    public Timeout timeout() {
        return this.timeout;
    }

    @Nullable
    IOException timeoutExit(@Nullable IOException iOException) {
        if (!this.timeout.exit()) {
            return iOException;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    String toLoggableString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isCanceled() ? "canceled " : "");
        sb.append(this.forWebSocket ? "web socket" : NotificationCompat.CATEGORY_CALL);
        sb.append(" to ");
        sb.append(redactedUrl());
        return sb.toString();
    }
}