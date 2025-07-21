package com.netease.ntunisdk.base.deeplink;

import android.content.Intent;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.HashMap;
import java.util.Map;

/* compiled from: UniDeepLinkPref.java */
/* loaded from: classes6.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static Map<String, String> f1829a = new HashMap();

    static void a(String str, String str2) {
        UniSdkUtils.i("UniDeepLink", "key=" + str + ", value=" + str2);
        f1829a.put(str, str2);
    }

    static void a() {
        UniSdkUtils.i("UniDeepLink", "clearKeyValue");
        f1829a.clear();
    }

    public static Map<String, String> b() {
        return f1829a;
    }

    static void a(Intent intent) {
        if (intent != null) {
            a(ConstProp.START_INTENT_URI, intent.toUri(0));
        }
    }
}