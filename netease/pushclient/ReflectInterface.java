package com.netease.pushclient;

import android.content.Context;
import android.util.Log;

/* loaded from: classes3.dex */
public final class ReflectInterface {
    private static final String TAG = "NGPush_ReflectInterface";
    private static Class<?> s_clazzImpl;

    public static Object refectCall(Context context, String str) {
        try {
            if (s_clazzImpl == null) {
                s_clazzImpl = Class.forName("com.netease.pushclient.Reflect");
            }
            return s_clazzImpl.getMethod("refectCall", Context.class, String.class).invoke(null, context, str);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "refectCall exception:" + e.getMessage());
            return null;
        }
    }
}