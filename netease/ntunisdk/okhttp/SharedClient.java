package com.netease.ntunisdk.okhttp;

import com.netease.ntunisdk.okhttp3.OkHttpClient;

/* loaded from: classes6.dex */
public class SharedClient {
    private static volatile OkHttpClient defaultClient;

    private SharedClient() {
    }

    public static OkHttpClient get() {
        if (defaultClient == null) {
            synchronized (SharedClient.class) {
                if (defaultClient == null) {
                    defaultClient = new OkHttpClient.Builder().build();
                }
            }
        }
        return defaultClient;
    }
}