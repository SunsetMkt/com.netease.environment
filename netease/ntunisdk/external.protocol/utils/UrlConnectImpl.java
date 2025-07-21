package com.netease.ntunisdk.external.protocol.utils;

import com.alipay.sdk.app.OpenAuthTask;
import java.util.HashMap;

/* loaded from: classes.dex */
public class UrlConnectImpl {
    private static final String CHARSET = "utf-8";
    private static final int CONNECT_TIMEOUT = 4000;
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final int READ_TIMEOUT = 4000;
    private static final String TAG = "P-NetWork";

    public static Response fetch(String str, HashMap<String, String> map) {
        return fetch(str, map, OpenAuthTask.SYS_ERR, OpenAuthTask.SYS_ERR);
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x015d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.netease.ntunisdk.external.protocol.utils.Response fetch(java.lang.String r10, java.util.HashMap<java.lang.String, java.lang.String> r11, int r12, int r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 364
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.utils.UrlConnectImpl.fetch(java.lang.String, java.util.HashMap, int, int):com.netease.ntunisdk.external.protocol.utils.Response");
    }

    public static String post(String str, byte[] bArr) {
        return post(str, null, bArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0118 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:32:0x00db -> B:63:0x010e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String post(java.lang.String r8, java.util.HashMap<java.lang.String, java.lang.String> r9, byte[] r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 295
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.utils.UrlConnectImpl.post(java.lang.String, java.util.HashMap, byte[]):java.lang.String");
    }
}