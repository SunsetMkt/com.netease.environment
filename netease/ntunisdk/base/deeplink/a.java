package com.netease.ntunisdk.base.deeplink;

/* compiled from: ToAppUtil.java */
/* loaded from: classes6.dex */
public final class a {
    /* JADX WARN: Removed duplicated region for block: B:21:0x0074 A[Catch: Exception -> 0x00a2, TryCatch #0 {Exception -> 0x00a2, blocks: (B:3:0x0010, B:5:0x0022, B:6:0x0028, B:8:0x002e, B:9:0x0034, B:11:0x004d, B:12:0x0050, B:14:0x0059, B:21:0x0074, B:27:0x0092, B:29:0x0098, B:15:0x0061, B:17:0x0067, B:19:0x006c), top: B:34:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0098 A[Catch: Exception -> 0x00a2, TRY_LEAVE, TryCatch #0 {Exception -> 0x00a2, blocks: (B:3:0x0010, B:5:0x0022, B:6:0x0028, B:8:0x002e, B:9:0x0034, B:11:0x004d, B:12:0x0050, B:14:0x0059, B:21:0x0074, B:27:0x0092, B:29:0x0098, B:15:0x0061, B:17:0x0067, B:19:0x006c), top: B:34:0x0010 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(android.content.Context r7, org.json.JSONObject r8) {
        /*
            java.lang.String r0 = java.lang.String.valueOf(r8)
            java.lang.String r1 = "toApp: "
            java.lang.String r0 = r1.concat(r0)
            java.lang.String r2 = "ToAppUtil"
            com.netease.ntunisdk.base.UniSdkUtils.i(r2, r0)
            r0 = 0
            java.lang.String r3 = "package"
            java.lang.String r3 = r8.optString(r3)     // Catch: java.lang.Exception -> La2
            java.lang.String r4 = "android"
            java.lang.String r4 = r8.optString(r4)     // Catch: java.lang.Exception -> La2
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Exception -> La2
            if (r5 == 0) goto L28
            java.lang.String r4 = "scheme"
            java.lang.String r4 = r8.optString(r4)     // Catch: java.lang.Exception -> La2
        L28:
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Exception -> La2
            if (r5 == 0) goto L34
            java.lang.String r4 = "ios"
            java.lang.String r4 = r8.optString(r4)     // Catch: java.lang.Exception -> La2
        L34:
            java.lang.String r5 = "action"
            java.lang.String r6 = "android.intent.action.VIEW"
            java.lang.String r8 = r8.optString(r5, r6)     // Catch: java.lang.Exception -> La2
            android.content.Intent r5 = new android.content.Intent     // Catch: java.lang.Exception -> La2
            r5.<init>(r8)     // Catch: java.lang.Exception -> La2
            r8 = 268436480(0x10000400, float:2.524663E-29)
            r5.setFlags(r8)     // Catch: java.lang.Exception -> La2
            boolean r8 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Exception -> La2
            if (r8 != 0) goto L50
            r5.setPackage(r3)     // Catch: java.lang.Exception -> La2
        L50:
            java.lang.String r8 = "://"
            boolean r8 = r4.contains(r8)     // Catch: java.lang.Exception -> La2
            r6 = 1
            if (r8 == 0) goto L61
            android.net.Uri r8 = android.net.Uri.parse(r4)     // Catch: java.lang.Exception -> La2
            r5.setData(r8)     // Catch: java.lang.Exception -> La2
            goto L6a
        L61:
            boolean r8 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Exception -> La2
            if (r8 != 0) goto L6c
            r5.setClassName(r3, r4)     // Catch: java.lang.Exception -> La2
        L6a:
            r8 = r6
            goto L72
        L6c:
            java.lang.String r8 = "can not handle it"
            com.netease.ntunisdk.base.UniSdkUtils.w(r2, r8)     // Catch: java.lang.Exception -> La2
            r8 = r0
        L72:
            if (r8 == 0) goto L90
            android.content.pm.PackageManager r8 = r7.getPackageManager()     // Catch: java.lang.Exception -> La2
            java.util.List r8 = r8.queryIntentActivities(r5, r0)     // Catch: java.lang.Exception -> La2
            java.lang.String r3 = java.lang.String.valueOf(r8)     // Catch: java.lang.Exception -> La2
            java.lang.String r1 = r1.concat(r3)     // Catch: java.lang.Exception -> La2
            com.netease.ntunisdk.base.UniSdkUtils.i(r2, r1)     // Catch: java.lang.Exception -> La2
            int r8 = r8.size()     // Catch: java.lang.Exception -> La2
            if (r8 <= 0) goto L8e
            goto L8f
        L8e:
            r6 = r0
        L8f:
            r8 = r6
        L90:
            if (r8 != 0) goto L98
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Exception -> La2
            r2 = 30
            if (r1 < r2) goto La0
        L98:
            r1 = 0
            android.content.Intent r1 = android.content.Intent.createChooser(r5, r1)     // Catch: java.lang.Exception -> La2
            com.netease.ntunisdk.modules.personalinfolist.HookManager.startActivity(r7, r1)     // Catch: java.lang.Exception -> La2
        La0:
            r0 = r8
            goto La6
        La2:
            r7 = move-exception
            r7.printStackTrace()
        La6:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.deeplink.a.a(android.content.Context, org.json.JSONObject):boolean");
    }
}