package com.netease.androidcrashhandler.thirdparty.okhttp;

import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.okhttp.SharedClient;
import com.netease.ntunisdk.okhttp3.OkHttpClient;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import java.util.concurrent.TimeUnit;

/* loaded from: classes6.dex */
public class OkHttpProxy {
    private static OkHttpClient sOkHttpClient;

    public static Response exec(Request request) {
        if (sOkHttpClient == null) {
            OkHttpClient.Builder builderNewBuilder = SharedClient.get().newBuilder();
            builderNewBuilder.connectTimeout(30000L, TimeUnit.MILLISECONDS);
            builderNewBuilder.readTimeout(30000L, TimeUnit.MILLISECONDS);
            builderNewBuilder.writeTimeout(30000L, TimeUnit.MILLISECONDS);
            sOkHttpClient = builderNewBuilder.build();
        }
        try {
            LogUtils.i(LogUtils.TAG, "OkHttpProxy net [exec] url:" + request.url().toString());
            return sOkHttpClient.newCall(request).execute();
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "OkHttpProxy net [exec] Exception = " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}