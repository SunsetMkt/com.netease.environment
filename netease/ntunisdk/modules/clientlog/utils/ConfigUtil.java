package com.netease.ntunisdk.modules.clientlog.utils;

/* loaded from: classes6.dex */
public class ConfigUtil {
    /* JADX WARN: Removed duplicated region for block: B:11:0x0025 A[Catch: all -> 0x0020, IOException -> 0x004f, TRY_LEAVE, TryCatch #4 {IOException -> 0x004f, blocks: (B:7:0x0019, B:11:0x0025, B:15:0x0030, B:20:0x003c), top: B:57:0x0019, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0030 A[Catch: all -> 0x0020, IOException -> 0x004f, TRY_ENTER, TRY_LEAVE, TryCatch #4 {IOException -> 0x004f, blocks: (B:7:0x0019, B:11:0x0025, B:15:0x0030, B:20:0x003c), top: B:57:0x0019, outer: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void readLibraryConfig(android.content.Context r7) throws java.io.IOException {
        /*
            java.lang.String r0 = "ClientLogModule"
            java.lang.String r1 = "read file success ,ClientLogConstant.EB_TAG : "
            java.lang.String r2 = "ntunisdk_config"
            android.content.res.AssetManager r7 = r7.getAssets()
            r3 = 3
            r4 = 0
            java.io.InputStream r5 = r7.open(r2, r3)     // Catch: java.lang.Exception -> L11
            goto L17
        L11:
            java.lang.String r5 = "fail to read ntunisdk_config, try ntunisdk.cfg"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r5)
            r5 = r4
        L17:
            if (r5 != 0) goto L23
            java.lang.String r2 = "ntunisdk.cfg"
            java.io.InputStream r5 = r7.open(r2, r3)     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            goto L23
        L20:
            r7 = move-exception
            goto Lb4
        L23:
            if (r5 != 0) goto L30
            java.lang.String r7 = "ntunisdk_config/ntunisdk.cfg null"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r7)     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            if (r5 == 0) goto L2f
            r5.close()     // Catch: java.io.IOException -> L2f
        L2f:
            return
        L30:
            int r7 = r5.available()     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            if (r7 != 0) goto L3c
            if (r5 == 0) goto L3b
            r5.close()     // Catch: java.io.IOException -> L3b
        L3b:
            return
        L3c:
            byte[] r7 = new byte[r7]     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            r5.read(r7)     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            java.lang.String r3 = new java.lang.String     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            java.lang.String r6 = "UTF-8"
            r3.<init>(r7, r6)     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L4f
            if (r5 == 0) goto L4d
            r5.close()     // Catch: java.io.IOException -> L4d
        L4d:
            r4 = r3
            goto L59
        L4f:
            java.lang.String r7 = "ntunisdk_config/ntunisdk.cfg config not found"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r7)     // Catch: java.lang.Throwable -> L20
            if (r5 == 0) goto L59
            r5.close()     // Catch: java.io.IOException -> L59
        L59:
            if (r4 != 0) goto L65
            java.lang.String r7 = " is null"
            java.lang.String r7 = r2.concat(r7)
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r7)
            return
        L65:
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r4)
            java.lang.String r7 = "\uff1a"
            boolean r7 = r4.contains(r7)
            if (r7 != 0) goto L80
            java.lang.String r7 = "\u201c"
            boolean r7 = r4.contains(r7)
            if (r7 != 0) goto L80
            java.lang.String r7 = "\u201d"
            boolean r7 = r4.contains(r7)
            if (r7 == 0) goto L89
        L80:
            java.lang.String r7 = "\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26"
            java.lang.String r7 = r2.concat(r7)
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r7)
        L89:
            org.json.JSONTokener r7 = new org.json.JSONTokener
            r7.<init>(r4)
            java.lang.Object r7 = r7.nextValue()     // Catch: org.json.JSONException -> Lae
            org.json.JSONObject r7 = (org.json.JSONObject) r7     // Catch: org.json.JSONException -> Lae
            java.lang.String r2 = "EB"
            int r7 = r7.getInt(r2)     // Catch: org.json.JSONException -> Lae
            com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant.EB_TAG = r7     // Catch: org.json.JSONException -> Lae
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: org.json.JSONException -> Lae
            r7.<init>(r1)     // Catch: org.json.JSONException -> Lae
            int r1 = com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant.EB_TAG     // Catch: org.json.JSONException -> Lae
            r7.append(r1)     // Catch: org.json.JSONException -> Lae
            java.lang.String r7 = r7.toString()     // Catch: org.json.JSONException -> Lae
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r7)     // Catch: org.json.JSONException -> Lae
            goto Lb3
        Lae:
            java.lang.String r7 = "ntunisdk_config/ntunisdk.cfg config parse to json error"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r7)
        Lb3:
            return
        Lb4:
            if (r5 == 0) goto Lb9
            r5.close()     // Catch: java.io.IOException -> Lb9
        Lb9:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.clientlog.utils.ConfigUtil.readLibraryConfig(android.content.Context):void");
    }
}