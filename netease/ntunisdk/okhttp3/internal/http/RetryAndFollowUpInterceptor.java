package com.netease.ntunisdk.okhttp3.internal.http;

import com.netease.ntunisdk.okhttp3.Address;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.CertificatePinner;
import com.netease.ntunisdk.okhttp3.EventListener;
import com.netease.ntunisdk.okhttp3.HttpUrl;
import com.netease.ntunisdk.okhttp3.Interceptor;
import com.netease.ntunisdk.okhttp3.OkHttpClient;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.okhttp3.Route;
import com.netease.ntunisdk.okhttp3.internal.Util;
import com.netease.ntunisdk.okhttp3.internal.connection.RouteException;
import com.netease.ntunisdk.okhttp3.internal.connection.StreamAllocation;
import com.netease.ntunisdk.okhttp3.internal.http2.ConnectionShutdownException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: classes5.dex */
public final class RetryAndFollowUpInterceptor implements Interceptor {
    private static final int MAX_FOLLOW_UPS = 20;
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private final boolean forWebSocket;
    private volatile StreamAllocation streamAllocation;

    public RetryAndFollowUpInterceptor(OkHttpClient okHttpClient, boolean z) {
        this.client = okHttpClient;
        this.forWebSocket = z;
    }

    private Address createAddress(HttpUrl httpUrl) {
        SSLSocketFactory sslSocketFactory;
        HostnameVerifier hostnameVerifier;
        CertificatePinner certificatePinner;
        if (httpUrl.isHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        } else {
            sslSocketFactory = null;
            hostnameVerifier = null;
            certificatePinner = null;
        }
        return new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }

    private Address createAddress(HttpUrl httpUrl, Request request) {
        SSLSocketFactory sslSocketFactory;
        HostnameVerifier hostnameVerifier;
        CertificatePinner certificatePinner;
        if (httpUrl.isHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        } else {
            sslSocketFactory = null;
            hostnameVerifier = null;
            certificatePinner = null;
        }
        return new Address(httpUrl.host(), request.header("host"), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }

    private Request followUpRequest(Response response, Route route) throws IOException {
        String strHeader;
        HttpUrl httpUrlResolve;
        if (response == null) {
            throw new IllegalStateException();
        }
        int iCode = response.code();
        String strMethod = response.request().method();
        if (iCode == 307 || iCode == 308) {
            if (!strMethod.equals("GET") && !strMethod.equals("HEAD")) {
                return null;
            }
        } else {
            if (iCode == 401) {
                return this.client.authenticator().authenticate(route, response);
            }
            if (iCode == 503) {
                if ((response.priorResponse() == null || response.priorResponse().code() != 503) && retryAfter(response, Integer.MAX_VALUE) == 0) {
                    return response.request();
                }
                return null;
            }
            if (iCode == 407) {
                if (route.proxy().type() == Proxy.Type.HTTP) {
                    return this.client.proxyAuthenticator().authenticate(route, response);
                }
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            }
            if (iCode == 408) {
                if (!this.client.retryOnConnectionFailure() || (response.request().body() instanceof UnrepeatableRequestBody)) {
                    return null;
                }
                if ((response.priorResponse() == null || response.priorResponse().code() != 408) && retryAfter(response, 0) <= 0) {
                    return response.request();
                }
                return null;
            }
            switch (iCode) {
                case 300:
                case 301:
                case 302:
                case 303:
                    break;
                default:
                    return null;
            }
        }
        if (!this.client.followRedirects() || (strHeader = response.header("Location")) == null || (httpUrlResolve = response.request().url().resolve(strHeader)) == null) {
            return null;
        }
        if (!httpUrlResolve.scheme().equals(response.request().url().scheme()) && !this.client.followSslRedirects()) {
            return null;
        }
        Request.Builder builderNewBuilder = response.request().newBuilder();
        if (HttpMethod.permitsRequestBody(strMethod)) {
            boolean zRedirectsWithBody = HttpMethod.redirectsWithBody(strMethod);
            if (HttpMethod.redirectsToGet(strMethod)) {
                builderNewBuilder.method("GET", null);
            } else {
                builderNewBuilder.method(strMethod, zRedirectsWithBody ? response.request().body() : null);
            }
            if (!zRedirectsWithBody) {
                builderNewBuilder.removeHeader("Transfer-Encoding");
                builderNewBuilder.removeHeader("Content-Length");
                builderNewBuilder.removeHeader("Content-Type");
            }
        }
        if (!sameConnection(response, httpUrlResolve)) {
            builderNewBuilder.removeHeader("Authorization");
        }
        return builderNewBuilder.url(httpUrlResolve).build();
    }

    private boolean isRecoverable(IOException iOException, boolean z) {
        if (iOException instanceof ProtocolException) {
            return false;
        }
        return iOException instanceof InterruptedIOException ? (iOException instanceof SocketTimeoutException) && !z : (((iOException instanceof SSLHandshakeException) && (iOException.getCause() instanceof CertificateException)) || (iOException instanceof SSLPeerUnverifiedException)) ? false : true;
    }

    private boolean recover(IOException iOException, StreamAllocation streamAllocation, boolean z, Request request) throws IOException {
        streamAllocation.streamFailed(iOException);
        if (this.client.retryOnConnectionFailure()) {
            return !(z && requestIsUnrepeatable(iOException, request)) && isRecoverable(iOException, z) && streamAllocation.hasMoreRoutes();
        }
        return false;
    }

    private boolean requestIsUnrepeatable(IOException iOException, Request request) {
        return (request.body() instanceof UnrepeatableRequestBody) || (iOException instanceof FileNotFoundException);
    }

    private int retryAfter(Response response, int i) {
        String strHeader = response.header("Retry-After");
        if (strHeader == null) {
            return i;
        }
        if (strHeader.matches("\\d+")) {
            return Integer.valueOf(strHeader).intValue();
        }
        return Integer.MAX_VALUE;
    }

    private boolean sameConnection(Response response, HttpUrl httpUrl) {
        HttpUrl httpUrlUrl = response.request().url();
        return httpUrlUrl.host().equals(httpUrl.host()) && httpUrlUrl.port() == httpUrl.port() && httpUrlUrl.scheme().equals(httpUrl.scheme());
    }

    public void cancel() throws IOException {
        this.canceled = true;
        StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation != null) {
            streamAllocation.cancel();
        }
    }

    @Override // com.netease.ntunisdk.okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response responseProceed;
        Request requestFollowUpRequest;
        System.out.println("wuln--okhttp RetryAndFollowUpInterceptor [intercept] start");
        Request request = chain.request();
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        Call call = realInterceptorChain.call();
        EventListener eventListener = realInterceptorChain.eventListener();
        StreamAllocation streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(request.url(), request), call, eventListener, this.callStackTrace);
        this.streamAllocation = streamAllocation;
        Request request2 = request;
        int i = 0;
        Response response = null;
        while (!this.canceled) {
            try {
                try {
                    try {
                        responseProceed = realInterceptorChain.proceed(request2, streamAllocation, null, null);
                        if (response != null) {
                            responseProceed = responseProceed.newBuilder().priorResponse(response.newBuilder().body(null).build()).build();
                        }
                        try {
                            requestFollowUpRequest = followUpRequest(responseProceed, streamAllocation.route());
                        } catch (IOException e) {
                            streamAllocation.release();
                            throw e;
                        }
                    } catch (RouteException e2) {
                        if (!recover(e2.getLastConnectException(), streamAllocation, false, request2)) {
                            throw e2.getFirstConnectException();
                        }
                    }
                } catch (IOException e3) {
                    if (!recover(e3, streamAllocation, !(e3 instanceof ConnectionShutdownException), request2)) {
                        throw e3;
                    }
                }
                if (requestFollowUpRequest == null) {
                    streamAllocation.release();
                    return responseProceed;
                }
                Util.closeQuietly(responseProceed.body());
                int i2 = i + 1;
                if (i2 > 20) {
                    streamAllocation.release();
                    throw new ProtocolException("Too many follow-up requests: " + i2);
                }
                if (requestFollowUpRequest.body() instanceof UnrepeatableRequestBody) {
                    streamAllocation.release();
                    throw new HttpRetryException("Cannot retry streamed HTTP body", responseProceed.code());
                }
                if (!sameConnection(responseProceed, requestFollowUpRequest.url())) {
                    streamAllocation.release();
                    streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(requestFollowUpRequest.url(), request2), call, eventListener, this.callStackTrace);
                    this.streamAllocation = streamAllocation;
                } else if (streamAllocation.codec() != null) {
                    throw new IllegalStateException("Closing the body of " + responseProceed + " didn't close its backing stream. Bad interceptor?");
                }
                response = responseProceed;
                request2 = requestFollowUpRequest;
                i = i2;
            } catch (Throwable th) {
                streamAllocation.streamFailed(null);
                streamAllocation.release();
                throw th;
            }
        }
        streamAllocation.release();
        throw new IOException("Canceled");
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCallStackTrace(Object obj) {
        this.callStackTrace = obj;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
}