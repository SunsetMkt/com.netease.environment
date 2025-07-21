package com.netease.ntunisdk.modules.deviceinfo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AppChannel {
    private static final String TAG = "AppChannel";

    public static String getAppChannel(Context context) throws Throwable {
        String commonFileChannel = readCommonFileChannel(context);
        if (TextUtils.isEmpty(commonFileChannel)) {
            commonFileChannel = readChannelFile(context);
        }
        if (TextUtils.isEmpty(commonFileChannel)) {
            String distroId = getDistroId(context);
            if (!TextUtils.isEmpty(distroId)) {
                commonFileChannel = distroId;
            }
        }
        String kwaiCpsChannel = readKwaiCpsChannel(context);
        String tencentCpsChannel = readTencentCpsChannel(context);
        String toutiaoCpsChannel = readToutiaoCpsChannel(context);
        if (!TextUtils.isEmpty(kwaiCpsChannel)) {
            commonFileChannel = kwaiCpsChannel;
        } else if (!TextUtils.isEmpty(tencentCpsChannel)) {
            commonFileChannel = tencentCpsChannel;
        } else if (!TextUtils.isEmpty(toutiaoCpsChannel)) {
            commonFileChannel = toutiaoCpsChannel;
        }
        String cpsChannelImpl = getCpsChannelImpl(context);
        return !TextUtils.isEmpty(cpsChannelImpl) ? cpsChannelImpl : commonFileChannel;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String readCommonFileChannel(android.content.Context r9) throws java.io.IOException {
        /*
            java.lang.String r0 = "APP_CHANNEL"
            java.lang.String r1 = ""
            java.lang.String r2 = "AppChannel"
            java.lang.String r3 = "ntunisdk_common_data"
            r4 = 0
            android.content.res.AssetManager r5 = r9.getAssets()     // Catch: java.lang.Throwable -> L3e
            r6 = 3
            java.io.InputStream r5 = r5.open(r3, r6)     // Catch: java.lang.Throwable -> L3e
            if (r5 != 0) goto L1f
            java.lang.String r6 = "ntunisdk_common_data null"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r2, r6)     // Catch: java.lang.Throwable -> L3f
            if (r5 == 0) goto L1e
            r5.close()     // Catch: java.io.IOException -> L1e
        L1e:
            return r1
        L1f:
            int r6 = r5.available()     // Catch: java.lang.Throwable -> L3f
            if (r6 != 0) goto L2b
            if (r5 == 0) goto L2a
            r5.close()     // Catch: java.io.IOException -> L2a
        L2a:
            return r1
        L2b:
            byte[] r6 = new byte[r6]     // Catch: java.lang.Throwable -> L3f
            r5.read(r6)     // Catch: java.lang.Throwable -> L3f
            java.lang.String r7 = new java.lang.String     // Catch: java.lang.Throwable -> L3f
            java.lang.String r8 = "UTF-8"
            r7.<init>(r6, r8)     // Catch: java.lang.Throwable -> L3f
            if (r5 == 0) goto L3c
            r5.close()     // Catch: java.io.IOException -> L3c
        L3c:
            r4 = r7
            goto L4b
        L3e:
            r5 = r4
        L3f:
            java.lang.String r6 = "ntunisdk_common_data config not found"
            com.netease.ntunisdk.modules.base.utils.LogModule.i(r2, r6)     // Catch: java.lang.Throwable -> La1
            if (r5 == 0) goto L4b
            r5.close()     // Catch: java.io.IOException -> L4a
            goto L4b
        L4a:
        L4b:
            if (r4 != 0) goto L62
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r3)
            java.lang.String r0 = " is null"
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r2, r9)
            return r1
        L62:
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r2, r4)
            org.json.JSONTokener r3 = new org.json.JSONTokener
            r3.<init>(r4)
            java.lang.Object r3 = r3.nextValue()     // Catch: java.lang.Throwable -> L87
            org.json.JSONObject r3 = (org.json.JSONObject) r3     // Catch: java.lang.Throwable -> L87
            java.lang.String r9 = getCpsChannelReader(r9)     // Catch: java.lang.Throwable -> L87
            boolean r4 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Throwable -> L87
            if (r4 != 0) goto L7b
            return r9
        L7b:
            boolean r9 = r3.has(r0)     // Catch: java.lang.Throwable -> L87
            if (r9 != 0) goto L82
            return r1
        L82:
            java.lang.String r9 = r3.getString(r0)     // Catch: java.lang.Throwable -> L87
            return r9
        L87:
            r9 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "[readCommonFileChannel] Exception="
            r0.append(r3)
            java.lang.String r9 = r9.getMessage()
            r0.append(r9)
            java.lang.String r9 = r0.toString()
            com.netease.ntunisdk.modules.base.utils.LogModule.e(r2, r9)
            return r1
        La1:
            r9 = move-exception
            if (r5 == 0) goto La7
            r5.close()     // Catch: java.io.IOException -> La7
        La7:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.deviceinfo.AppChannel.readCommonFileChannel(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00b5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String readChannelFile(android.content.Context r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 269
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.deviceinfo.AppChannel.readChannelFile(android.content.Context):java.lang.String");
    }

    private static String readKwaiCpsChannel(Context context) {
        try {
            Method declaredMethod = Class.forName("com.kwai.monitor.payload.TurboHelper").getDeclaredMethod("getChannel", Context.class);
            declaredMethod.setAccessible(true);
            String str = (String) declaredMethod.invoke(null, context);
            LogModule.d(TAG, "readKwaiCpsChannel: " + str);
            return str;
        } catch (Throwable th) {
            LogModule.e(TAG, "[readKwaiCpsChannel] Exception=" + th.getMessage());
            return "";
        }
    }

    private static String readTencentCpsChannel(Context context) {
        try {
            File file = new File(context.getPackageCodePath());
            if (!file.exists()) {
                return "";
            }
            Method declaredMethod = Class.forName("com.txgdt.channel.reader.ChannelReader").getDeclaredMethod("getChannelByV2", File.class);
            declaredMethod.setAccessible(true);
            String str = (String) declaredMethod.invoke(null, file);
            LogModule.i(TAG, "readTencentCpsChannel: " + str);
            return str;
        } catch (Throwable th) {
            LogModule.e(TAG, "[readTencentCpsChannel] Exception=" + th.getMessage());
            return "";
        }
    }

    private static String readToutiaoCpsChannel(Context context) {
        try {
            File file = new File(context.getPackageCodePath());
            if (!file.exists()) {
                return "";
            }
            String str = null;
            try {
                Method declaredMethod = Class.forName("com.netease.ntunisdk.toutiao.utils.cps.ApkChanneling").getDeclaredMethod("getChannel", File.class);
                declaredMethod.setAccessible(true);
                str = (String) declaredMethod.invoke(null, file);
            } catch (Throwable th) {
                LogModule.e(TAG, "[readToutiaoCpsChannel] Exception=" + th.getMessage());
            }
            LogModule.d(TAG, "[readToutiaoCpsChannel] init -> tag: " + str);
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i : new int[]{110, 101, 116, 101, 97, 115, 101}) {
                sb.append((char) i);
            }
            String string = sb.toString();
            if (!str.startsWith(string)) {
                str = string + "." + str;
            }
            LogModule.d(TAG, "[readToutiaoCpsChannel] init -> channel: " + str);
            return str;
        } catch (Throwable th2) {
            LogModule.e(TAG, "[readToutiaoCpsChannel] Exception=" + th2.getMessage());
            return "";
        }
    }

    private static String getDistroId(Context context) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("com.netease.apk_distro/config.json")));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    return new JSONObject(sb.toString()).getString("distro_id");
                }
            }
        } catch (Exception unused) {
            return null;
        }
    }

    private static String getCpsChannelReader(Context context) {
        File file = new File(context.getPackageCodePath());
        try {
            Method declaredMethod = Class.forName("com.netease.ntunisdk.base.utils.cps.ApkChanneling").getDeclaredMethod("getChannel", File.class);
            declaredMethod.setAccessible(true);
            return (String) declaredMethod.invoke(null, file);
        } catch (Throwable th) {
            LogModule.e(TAG, "[getCpsChannelReader] Exception=" + th.getMessage());
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00b6 A[EXC_TOP_SPLITTER, PHI: r1 r2
  0x00b6: PHI (r1v3 java.io.InputStream) = (r1v2 java.io.InputStream), (r1v4 java.io.InputStream) binds: [B:33:0x00d6, B:25:0x00b4] A[DONT_GENERATE, DONT_INLINE]
  0x00b6: PHI (r2v2 java.lang.String) = (r2v1 java.lang.String), (r2v5 java.lang.String) binds: [B:33:0x00d6, B:25:0x00b4] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String getCpsChannelImpl(android.content.Context r13) throws java.io.IOException {
        /*
            java.lang.String r0 = "AppChannel"
            java.lang.String r1 = "ntunisdk_data"
            r2 = 0
            android.content.res.AssetManager r3 = r13.getAssets()     // Catch: java.lang.Throwable -> Lbc
            r4 = 3
            java.io.InputStream r1 = r3.open(r1, r4)     // Catch: java.lang.Throwable -> Lbc
            int r3 = r1.available()     // Catch: java.lang.Throwable -> Lba
            if (r3 != 0) goto L1f
            java.lang.String r13 = "ntunisdk_data empty"
            android.util.Log.d(r0, r13)     // Catch: java.lang.Throwable -> Lba
            if (r1 == 0) goto L1e
            r1.close()     // Catch: java.io.IOException -> L1e
        L1e:
            return r2
        L1f:
            byte[] r3 = new byte[r3]     // Catch: java.lang.Throwable -> Lba
            r1.read(r3)     // Catch: java.lang.Throwable -> Lba
            java.lang.String r4 = new java.lang.String     // Catch: java.lang.Throwable -> Lba
            java.lang.String r5 = "UTF-8"
            r4.<init>(r3, r5)     // Catch: java.lang.Throwable -> Lba
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lba
            r3.<init>()     // Catch: java.lang.Throwable -> Lba
            java.lang.String r5 = "[getCpsChannelImpl] ntunisdk_data:"
            r3.append(r5)     // Catch: java.lang.Throwable -> Lba
            r3.append(r4)     // Catch: java.lang.Throwable -> Lba
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> Lba
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r3)     // Catch: java.lang.Throwable -> Lba
            java.lang.String r3 = ";"
            java.lang.String[] r3 = r4.split(r3)     // Catch: java.lang.Throwable -> Lba
            int r4 = r3.length     // Catch: java.lang.Throwable -> Lba
            r5 = 0
            r7 = r2
            r6 = 0
        L49:
            if (r6 >= r4) goto L9f
            r8 = r3[r6]     // Catch: java.lang.Throwable -> L9c
            java.lang.String r9 = r8.trim()     // Catch: java.lang.Throwable -> L9c
            java.lang.String r10 = "Sdk"
            boolean r9 = r9.startsWith(r10)     // Catch: java.lang.Throwable -> L9c
            if (r9 == 0) goto L99
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L99
            r9.<init>()     // Catch: java.lang.Throwable -> L99
            java.lang.String r10 = "com.netease.ntunisdk."
            r9.append(r10)     // Catch: java.lang.Throwable -> L99
            java.lang.String r8 = r8.trim()     // Catch: java.lang.Throwable -> L99
            r9.append(r8)     // Catch: java.lang.Throwable -> L99
            java.lang.String r8 = r9.toString()     // Catch: java.lang.Throwable -> L99
            java.lang.Class r8 = java.lang.Class.forName(r8)     // Catch: java.lang.Throwable -> L99
            java.lang.String r9 = "getCustomCpsChannel"
            r10 = 1
            java.lang.Class[] r11 = new java.lang.Class[r10]     // Catch: java.lang.Throwable -> L99
            java.lang.Class<java.lang.Object[]> r12 = java.lang.Object[].class
            r11[r5] = r12     // Catch: java.lang.Throwable -> L99
            java.lang.reflect.Method r8 = r8.getDeclaredMethod(r9, r11)     // Catch: java.lang.Throwable -> L99
            r8.setAccessible(r10)     // Catch: java.lang.Throwable -> L99
            java.lang.Object[] r9 = new java.lang.Object[r10]     // Catch: java.lang.Throwable -> L99
            r9[r5] = r13     // Catch: java.lang.Throwable -> L99
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch: java.lang.Throwable -> L99
            r10[r5] = r9     // Catch: java.lang.Throwable -> L99
            java.lang.Object r8 = r8.invoke(r2, r10)     // Catch: java.lang.Throwable -> L99
            java.lang.String r8 = (java.lang.String) r8     // Catch: java.lang.Throwable -> L99
            boolean r7 = android.text.TextUtils.isEmpty(r8)     // Catch: java.lang.Throwable -> L98
            if (r7 != 0) goto L98
            r2 = r8
            goto La0
        L98:
            r7 = r8
        L99:
            int r6 = r6 + 1
            goto L49
        L9c:
            r13 = move-exception
            r2 = r7
            goto Lbe
        L9f:
            r2 = r7
        La0:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lba
            r13.<init>()     // Catch: java.lang.Throwable -> Lba
            java.lang.String r3 = "[getCpsChannelImpl] channel="
            r13.append(r3)     // Catch: java.lang.Throwable -> Lba
            r13.append(r2)     // Catch: java.lang.Throwable -> Lba
            java.lang.String r13 = r13.toString()     // Catch: java.lang.Throwable -> Lba
            com.netease.ntunisdk.modules.base.utils.LogModule.e(r0, r13)     // Catch: java.lang.Throwable -> Lba
            if (r1 == 0) goto Ld9
        Lb6:
            r1.close()     // Catch: java.io.IOException -> Ld9
            goto Ld9
        Lba:
            r13 = move-exception
            goto Lbe
        Lbc:
            r13 = move-exception
            r1 = r2
        Lbe:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lda
            r3.<init>()     // Catch: java.lang.Throwable -> Lda
            java.lang.String r4 = "[getCpsChannelImpl] Exception="
            r3.append(r4)     // Catch: java.lang.Throwable -> Lda
            java.lang.String r13 = r13.getMessage()     // Catch: java.lang.Throwable -> Lda
            r3.append(r13)     // Catch: java.lang.Throwable -> Lda
            java.lang.String r13 = r3.toString()     // Catch: java.lang.Throwable -> Lda
            com.netease.ntunisdk.modules.base.utils.LogModule.e(r0, r13)     // Catch: java.lang.Throwable -> Lda
            if (r1 == 0) goto Ld9
            goto Lb6
        Ld9:
            return r2
        Lda:
            r13 = move-exception
            if (r1 == 0) goto Le0
            r1.close()     // Catch: java.io.IOException -> Le0
        Le0:
            goto Le2
        Le1:
            throw r13
        Le2:
            goto Le1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.deviceinfo.AppChannel.getCpsChannelImpl(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v7, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v10 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v9 */
    private static JSONObject getChannelObjcet(Context context) throws Throwable {
        ?? r9;
        InputStream inputStreamOpen;
        int iAvailable;
        JSONObject jSONObjectOptJSONObject;
        InputStream inputStream = null;
        inputStream = null;
        inputStream = null;
        inputStream = null;
        inputStream = null;
        InputStream inputStream2 = null;
        InputStream inputStream3 = null;
        try {
            try {
                inputStreamOpen = context.getAssets().open("channel_infos_data", 3);
            } catch (Exception e) {
                e = e;
                r9 = null;
            }
            if (inputStreamOpen == null) {
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException unused) {
                    }
                }
                return null;
            }
            try {
                iAvailable = inputStreamOpen.available();
            } catch (Exception e2) {
                e = e2;
                inputStream3 = inputStreamOpen;
                r9 = null;
                LogModule.i(TAG, "[getChannelInfos] Exception=" + e.toString());
                inputStream = inputStream3;
                if (inputStream3 != null) {
                    try {
                        inputStream3.close();
                        inputStream = inputStream3;
                    } catch (IOException unused2) {
                    }
                }
                return r9;
            } catch (Throwable th) {
                th = th;
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
            if (iAvailable == 0) {
                LogModule.w(TAG, "channel_infos_data is empty");
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException unused4) {
                    }
                }
                return null;
            }
            byte[] bArr = new byte[iAvailable];
            inputStreamOpen.read(bArr);
            String str = new String(bArr, "UTF-8");
            inputStreamOpen.close();
            if (TextUtils.isEmpty(str)) {
                LogModule.d(TAG, "channel_infos_data is empty");
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException unused5) {
                    }
                }
                return null;
            }
            LogModule.i(TAG, "[getChannelInfos] channelInfos =" + str);
            if (!TextUtils.isEmpty(str)) {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("main_channel") && (jSONObjectOptJSONObject = jSONObject.optJSONObject("main_channel")) != null && jSONObjectOptJSONObject.length() > 0) {
                    Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
                    while (itKeys.hasNext()) {
                        ?? OptJSONObject = jSONObjectOptJSONObject.optJSONObject(itKeys.next());
                        LogModule.i(TAG, "[getChannelInfos] mChannelInfosJson=" + OptJSONObject.toString());
                        inputStream2 = OptJSONObject;
                    }
                }
            }
            if (inputStreamOpen != null) {
                try {
                    inputStreamOpen.close();
                } catch (IOException unused6) {
                }
            }
            r9 = inputStream2;
            inputStream = inputStream2;
            return r9;
        } catch (Throwable th2) {
            th = th2;
            inputStreamOpen = inputStream;
        }
    }

    public static String getChannel(Context context) throws Throwable {
        LogModule.i(TAG, "[getChannelInfos] start");
        JSONObject channelObjcet = getChannelObjcet(context);
        return (channelObjcet == null || !channelObjcet.has("channel_id")) ? "unknown" : channelObjcet.optString("channel_id", "unknown");
    }

    public static boolean isBase64(String str) {
        try {
            return str.replaceAll("[\n]", "").equals(Base64.encodeToString(Base64.decode(str, 0), 0).replaceAll("[\n]", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}