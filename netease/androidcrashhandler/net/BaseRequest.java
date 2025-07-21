package com.netease.androidcrashhandler.net;

import com.netease.androidcrashhandler.thirdparty.okhttp.OkHttpProxy;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.okio.Buffer;
import java.util.concurrent.Callable;

/* loaded from: classes5.dex */
public abstract class BaseRequest implements Callable<Integer> {
    public RequestCallback requestCallback = null;

    public abstract Request createRequest(RequestBody requestBody) throws Exception;

    public abstract RequestBody createRequestBody() throws Exception;

    public abstract void handleResponse(Response response) throws Exception;

    public void registerRequestCallback(RequestCallback requestCallback) {
        this.requestCallback = requestCallback;
    }

    public void exec() {
        try {
            RequestBody requestBodyCreateRequestBody = createRequestBody();
            printRequestBody(requestBodyCreateRequestBody);
            handleResponse(OkHttpProxy.exec(createRequest(requestBodyCreateRequestBody)));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w(LogUtils.TAG, "RequestBase net [exec] Exception=" + e.toString());
        }
    }

    public void callback(int i, String str) {
        RequestCallback requestCallback = this.requestCallback;
        if (requestCallback != null) {
            requestCallback.onResponse(i, str);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        exec();
        return 1;
    }

    protected void printRequestBody(RequestBody requestBody) throws Exception {
        if (requestBody == null) {
            LogUtils.i(LogUtils.TAG, "RequestBase net [showRequestBody] requestBody is null");
            return;
        }
        if (LogUtils.isDebug()) {
            try {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String utf8 = buffer.readUtf8();
                LogUtils.i(LogUtils.TAG, "RequestBase net [showRequestBody] RequestBody content=" + utf8.substring(0, utf8.lastIndexOf("Content-Length: ") + 16 + 10));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}