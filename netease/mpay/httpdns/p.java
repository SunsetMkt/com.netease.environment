package com.netease.mpay.httpdns;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

/* loaded from: classes5.dex */
public class p {

    /* renamed from: a, reason: collision with root package name */
    public static o f1598a = new o(1, 0, "[%p-%l-%c]-#%t:%m");

    public static void a(String str) {
        if (f1598a.f1597a < 2) {
            return;
        }
        a(2, str);
    }

    public static void a(Throwable th) {
        if (th == null) {
            return;
        }
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        stringWriter.flush();
        String string = stringWriter.toString();
        if (f1598a.f1597a < 0) {
            return;
        }
        a(0, string);
    }

    public static synchronized void a(boolean z) {
        f1598a = new o(z ? 2 : -1, 0, "[%p-%l-%c]-#%t:%m");
    }

    public static void a(int i, String str) {
        String strA = f1598a.a(i, str);
        if (i == -1) {
            Log.e("HttpDns", strA);
            return;
        }
        if (i == 0) {
            Log.w("HttpDns", strA);
        } else if (i != 2) {
            Log.i("HttpDns", strA);
        } else {
            Log.d("HttpDns", strA);
        }
    }
}