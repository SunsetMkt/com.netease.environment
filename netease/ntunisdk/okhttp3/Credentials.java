package com.netease.ntunisdk.okhttp3;

import com.netease.ntunisdk.okhttp3.internal.Util;
import com.netease.ntunisdk.okio.ByteString;
import java.nio.charset.Charset;

/* loaded from: classes2.dex */
public final class Credentials {
    private Credentials() {
    }

    public static String basic(String str, String str2) {
        return basic(str, str2, Util.ISO_8859_1);
    }

    public static String basic(String str, String str2, Charset charset) {
        return "Basic " + ByteString.encodeString(str + ":" + str2, charset).base64();
    }
}