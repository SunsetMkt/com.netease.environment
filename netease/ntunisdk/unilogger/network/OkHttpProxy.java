package com.netease.ntunisdk.unilogger.network;

import com.netease.ntunisdk.okhttp.SharedClient;
import com.netease.ntunisdk.okhttp3.OkHttpClient;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.unilogger.utils.LogUtils;

/* loaded from: classes5.dex */
public class OkHttpProxy {
    public static OkHttpClient okHttpClient;

    public static Response exec(Request request) {
        if (okHttpClient == null) {
            okHttpClient = SharedClient.get();
        }
        try {
            LogUtils.i(LogUtils.TAG, "OkHttpProxy net [exec] url:" + request.url().toString());
            return okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "net OkHttpProxy net [exec] Exception = " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
}