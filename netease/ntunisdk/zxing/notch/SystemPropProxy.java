package com.netease.ntunisdk.zxing.notch;

import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class SystemPropProxy {
    private static Method systemPropGet;

    static {
        try {
            for (Method method : Class.forName("android.os.SystemProperties").getMethods()) {
                if (h.c.equals(method.getName())) {
                    systemPropGet = method;
                    return;
                }
            }
        } catch (ClassNotFoundException unused) {
        }
    }

    private SystemPropProxy() {
    }

    public static String get(String str, String str2) {
        try {
            return (String) systemPropGet.invoke(null, str, str2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return str2;
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return str2;
        }
    }
}