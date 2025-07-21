package com.netease.ntunisdk.unilogger.network;

import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import java.util.concurrent.Callable;

/* loaded from: classes5.dex */
public abstract class BaseRequest implements Callable<Integer> {
    public NetCallback netCallback = null;

    public abstract Request createRequest(RequestBody requestBody) throws Exception;

    public abstract RequestBody createRequestBody() throws Exception;

    public abstract void handleResponse(Response response) throws Exception;

    public void registerNetCallback(NetCallback netCallback) {
        this.netCallback = netCallback;
    }

    public void exec() {
        try {
            handleResponse(OkHttpProxy.exec(createRequest(createRequestBody())));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w(LogUtils.TAG, "RequestBase net  [exec] Exception=" + e.toString());
        }
    }

    public void callback(int i, String str) {
        NetCallback netCallback = this.netCallback;
        if (netCallback != null) {
            netCallback.onNetCallback(i, str);
        }
    }

    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        exec();
        return 1;
    }
}