package com.netease.androidcrashhandler.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class CUtil {
    private static final String TAG = "CUtil";

    public interface ThreadTask {
        void run();
    }

    public static boolean str2File(String str, String str2, String str3, boolean z) throws Throwable {
        LogUtils.i(LogUtils.TAG, "CUtil [str2File] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str2)) {
            LogUtils.i(LogUtils.TAG, "CUtil [str2File] param error");
            return false;
        }
        LogUtils.i(LogUtils.TAG, "CUtil [str2File] start fileDirPath=" + str2 + ", fileName=" + str3);
        try {
            byte[] bytes = str.getBytes("UTF-8");
            BufferedOutputStream bufferedOutputStream = null;
            try {
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (file.exists()) {
                    File file2 = new File(file.getAbsolutePath(), str3);
                    if (z) {
                        file2.delete();
                        file2.createNewFile();
                    } else if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    if (file2.exists()) {
                        BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(file2, !z));
                        try {
                            bufferedOutputStream2.write(bytes);
                            bufferedOutputStream = bufferedOutputStream2;
                        } catch (Throwable th) {
                            th = th;
                            bufferedOutputStream = bufferedOutputStream2;
                            if (bufferedOutputStream != null) {
                                bufferedOutputStream.close();
                            }
                            throw th;
                        }
                    } else {
                        LogUtils.i(LogUtils.TAG, "CUtil [str2File] file does not exist");
                    }
                } else {
                    LogUtils.i(LogUtils.TAG, "CUtil [str2File] directory does not exist");
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                return true;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "CUtil [str2File] Exception=" + e);
            return false;
        }
    }

    public static boolean str2File(String str, String str2, String str3) {
        LogUtils.i(LogUtils.TAG, "CUtil [str2File] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str2)) {
            LogUtils.i(LogUtils.TAG, "CUtil [str2File] param error");
            return false;
        }
        LogUtils.i(LogUtils.TAG, "CUtil [str2File] start fileDirPath=" + str2 + ", fileName=" + str3);
        try {
            byte[] bytes = str.getBytes("UTF-8");
            BufferedOutputStream bufferedOutputStream = null;
            try {
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (file.exists()) {
                    File file2 = new File(file.getAbsolutePath(), str3);
                    if (!file2.exists()) {
                        file2.delete();
                        file2.createNewFile();
                    }
                    if (file2.exists()) {
                        BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(file2));
                        try {
                            bufferedOutputStream2.write(bytes);
                            bufferedOutputStream = bufferedOutputStream2;
                        } catch (Throwable th) {
                            th = th;
                            bufferedOutputStream = bufferedOutputStream2;
                            if (bufferedOutputStream != null) {
                                bufferedOutputStream.close();
                            }
                            throw th;
                        }
                    } else {
                        LogUtils.i(LogUtils.TAG, "CUtil [str2File] file does not exist");
                    }
                } else {
                    LogUtils.i(LogUtils.TAG, "CUtil [str2File] directory does not exist");
                }
                if (bufferedOutputStream == null) {
                    return true;
                }
                bufferedOutputStream.close();
                return true;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "CUtil [str2File] Exception=" + e);
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.lang.CharSequence, java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v5, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r7v8 */
    public static String file2Str(String str) throws Throwable {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        if (TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "CUtil [file2Str] param error");
            return "";
        }
        if (!new File((String) str).exists()) {
            LogUtils.i(LogUtils.TAG, "CUtil [file2Str] file is not exists");
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(new File((String) str));
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                        while (true) {
                            try {
                                String line = bufferedReader.readLine();
                                if (line == null) {
                                    break;
                                }
                                sb.append(line);
                            } catch (Exception e) {
                                e = e;
                                e.printStackTrace();
                                LogUtils.w(LogUtils.TAG, "CUtil [file2Str] Exception =" + e.toString());
                                if (bufferedReader != null) {
                                    bufferedReader.close();
                                }
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                                return sb.toString();
                            }
                        }
                        bufferedReader.close();
                    } catch (Exception e2) {
                        e = e2;
                        bufferedReader = null;
                    } catch (Throwable th) {
                        th = th;
                        str = 0;
                        if (str != 0) {
                            str.close();
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    bufferedReader = null;
                    fileInputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    str = 0;
                    fileInputStream = null;
                }
                fileInputStream.close();
                return sb.toString();
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Exception e4) {
            LogUtils.i(LogUtils.TAG, "CUtil [file2Str] Exception=" + e4);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x009d A[Catch: Exception -> 0x00a1, TRY_ENTER, TryCatch #6 {Exception -> 0x00a1, blocks: (B:18:0x0047, B:19:0x004a, B:33:0x007a, B:40:0x009d, B:44:0x00a5, B:45:0x00a8), top: B:53:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a5 A[Catch: Exception -> 0x00a1, TryCatch #6 {Exception -> 0x00a1, blocks: (B:18:0x0047, B:19:0x004a, B:33:0x007a, B:40:0x009d, B:44:0x00a5, B:45:0x00a8), top: B:53:0x0029 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String file2Str(java.lang.String r6, java.lang.String r7) throws java.lang.Throwable {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            java.lang.String r1 = ""
            java.lang.String r2 = "trace"
            if (r0 != 0) goto Lbb
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L12
            goto Lbb
        L12:
            java.io.File r0 = new java.io.File
            r0.<init>(r6, r7)
            boolean r0 = r0.exists()
            if (r0 != 0) goto L23
            java.lang.String r6 = "CUtil [file2Str] file is not exists"
            com.netease.androidcrashhandler.util.LogUtils.i(r2, r6)
            return r1
        L23:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L5a
            java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L5a
            r4.<init>(r6, r7)     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L5a
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L5a
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L53
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L53
            r7.<init>(r3)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L53
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L53
        L3d:
            java.lang.String r7 = r6.readLine()     // Catch: java.lang.Exception -> L4e java.lang.Throwable -> L9a
            if (r7 == 0) goto L47
            r0.append(r7)     // Catch: java.lang.Exception -> L4e java.lang.Throwable -> L9a
            goto L3d
        L47:
            r6.close()     // Catch: java.lang.Exception -> La1
        L4a:
            r3.close()     // Catch: java.lang.Exception -> La1
            goto L80
        L4e:
            r7 = move-exception
            goto L5d
        L50:
            r7 = move-exception
            r6 = r1
            goto L9b
        L53:
            r7 = move-exception
            r6 = r1
            goto L5d
        L56:
            r7 = move-exception
            r6 = r1
            r3 = r6
            goto L9b
        L5a:
            r7 = move-exception
            r6 = r1
            r3 = r6
        L5d:
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L9a
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L9a
            r4.<init>()     // Catch: java.lang.Throwable -> L9a
            java.lang.String r5 = "CUtil [file2Str] Exception ="
            r4.append(r5)     // Catch: java.lang.Throwable -> L9a
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L9a
            r4.append(r7)     // Catch: java.lang.Throwable -> L9a
            java.lang.String r7 = r4.toString()     // Catch: java.lang.Throwable -> L9a
            com.netease.androidcrashhandler.util.LogUtils.w(r2, r7)     // Catch: java.lang.Throwable -> L9a
            if (r6 == 0) goto L7d
            r6.close()     // Catch: java.lang.Exception -> La1
        L7d:
            if (r3 == 0) goto L80
            goto L4a
        L80:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "CUtil [file2Str] sb.toString() ="
            r6.<init>(r7)
            java.lang.String r7 = r0.toString()
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            com.netease.androidcrashhandler.util.LogUtils.w(r2, r6)
            java.lang.String r6 = r0.toString()
            return r6
        L9a:
            r7 = move-exception
        L9b:
            if (r6 == 0) goto La3
            r6.close()     // Catch: java.lang.Exception -> La1
            goto La3
        La1:
            r6 = move-exception
            goto La9
        La3:
            if (r3 == 0) goto La8
            r3.close()     // Catch: java.lang.Exception -> La1
        La8:
            throw r7     // Catch: java.lang.Exception -> La1
        La9:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r0 = "CUtil [file2Str] Exception="
            r7.<init>(r0)
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            com.netease.androidcrashhandler.util.LogUtils.i(r2, r6)
            return r1
        Lbb:
            java.lang.String r6 = "CUtil [file2Str] param error"
            com.netease.androidcrashhandler.util.LogUtils.i(r2, r6)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.util.CUtil.file2Str(java.lang.String, java.lang.String):java.lang.String");
    }

    public static void addInfoToDiFile(String str, String str2, String str3, String str4) throws Throwable {
        LogUtils.w(LogUtils.TAG, "CUtil [addInfoToDiFile] start");
        LogUtils.w(LogUtils.TAG, "CUtil [addInfoToDiFile] dirPath=" + str + ", fileName=" + str2 + ", key=" + str3 + ", value=" + str4);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4) || "{}".equals(str4)) {
            LogUtils.w(LogUtils.TAG, "CUtil [addInfoToDiFile] params error");
            return;
        }
        String strFile2Str = file2Str(str, str2);
        if (TextUtils.isEmpty(strFile2Str)) {
            LogUtils.w(LogUtils.TAG, "CUtil [addInfoToDiFile] fileContent error");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(strFile2Str);
            try {
                jSONObject.put(str3, new JSONObject(str4));
            } catch (Exception unused) {
                jSONObject.put(str3, str4);
            }
            str2File(jSONObject.toString(), str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w(LogUtils.TAG, "CUtil [addInfoToDiFile] Exception=" + e.toString());
        }
        LogUtils.w(LogUtils.TAG, "CUtil [addInfoToDiFile] end");
    }

    public static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
        LogUtils.i(LogUtils.TAG, "CUtil [getFileMD5] start");
        if (!file.exists() || !file.isFile()) {
            LogUtils.i(LogUtils.TAG, "CUtil [getFileMD5] param error");
            return null;
        }
        byte[] bArr = new byte[1024];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int i = fileInputStream.read(bArr, 0, 1024);
                if (i != -1) {
                    messageDigest.update(bArr, 0, i);
                } else {
                    fileInputStream.close();
                    BigInteger bigInteger = new BigInteger(1, messageDigest.digest());
                    LogUtils.i(LogUtils.TAG, "CUtil [getFileMD5] file MD5 = " + bigInteger.toString(16));
                    return bigInteger.toString(16);
                }
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "CUtil [getFileMD5] Exception=" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0249 A[Catch: Exception -> 0x0295, TRY_ENTER, TryCatch #5 {Exception -> 0x0295, blocks: (B:118:0x027f, B:120:0x0284, B:122:0x0289, B:123:0x028c, B:108:0x0249, B:110:0x024e, B:112:0x0253, B:124:0x028d), top: B:134:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x024e A[Catch: Exception -> 0x0295, TryCatch #5 {Exception -> 0x0295, blocks: (B:118:0x027f, B:120:0x0284, B:122:0x0289, B:123:0x028c, B:108:0x0249, B:110:0x024e, B:112:0x0253, B:124:0x028d), top: B:134:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0253 A[Catch: Exception -> 0x0295, TRY_LEAVE, TryCatch #5 {Exception -> 0x0295, blocks: (B:118:0x027f, B:120:0x0284, B:122:0x0289, B:123:0x028c, B:108:0x0249, B:110:0x024e, B:112:0x0253, B:124:0x028d), top: B:134:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x027f A[Catch: Exception -> 0x0295, TRY_ENTER, TryCatch #5 {Exception -> 0x0295, blocks: (B:118:0x027f, B:120:0x0284, B:122:0x0289, B:123:0x028c, B:108:0x0249, B:110:0x024e, B:112:0x0253, B:124:0x028d), top: B:134:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0284 A[Catch: Exception -> 0x0295, TryCatch #5 {Exception -> 0x0295, blocks: (B:118:0x027f, B:120:0x0284, B:122:0x0289, B:123:0x028c, B:108:0x0249, B:110:0x024e, B:112:0x0253, B:124:0x028d), top: B:134:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0289 A[Catch: Exception -> 0x0295, TryCatch #5 {Exception -> 0x0295, blocks: (B:118:0x027f, B:120:0x0284, B:122:0x0289, B:123:0x028c, B:108:0x0249, B:110:0x024e, B:112:0x0253, B:124:0x028d), top: B:134:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01ff A[Catch: Exception -> 0x003f, TRY_ENTER, TryCatch #10 {Exception -> 0x003f, blocks: (B:5:0x0039, B:11:0x0048, B:27:0x00b5, B:75:0x01ff, B:76:0x0202, B:78:0x0207, B:114:0x0257), top: B:135:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0207 A[Catch: Exception -> 0x003f, TRY_LEAVE, TryCatch #10 {Exception -> 0x003f, blocks: (B:5:0x0039, B:11:0x0048, B:27:0x00b5, B:75:0x01ff, B:76:0x0202, B:78:0x0207, B:114:0x0257), top: B:135:0x0037 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean copyFile(java.lang.String r19, java.lang.String r20) {
        /*
            Method dump skipped, instructions count: 695
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.util.CUtil.copyFile(java.lang.String, java.lang.String):boolean");
    }

    public static String getAssetFileContent(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "CUtil [getAssetFile] param is error");
            return null;
        }
        try {
            InputStream inputStreamOpen = context.getAssets().open(str);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            inputStreamOpen.close();
            return new String(bArr);
        } catch (IOException e) {
            LogUtils.i(LogUtils.TAG, "CUtil [getAssetFile] IOException=" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static void checkAndReset(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!str.contains("(") || str.contains(")")) {
            if (str.contains("(") || !str.contains(")")) {
                if (str.contains("(") && str.contains(")")) {
                    String[] strArrSplit = str.split("\\(|\\)");
                    if (strArrSplit != null) {
                        LogUtils.i(LogUtils.TAG, "CUtil [checkAndReset] versions length=" + strArrSplit.length);
                        int length = strArrSplit.length;
                        for (int i = 0; i < length; i++) {
                            LogUtils.i(LogUtils.TAG, "CUtil [checkAndReset] versions string=" + strArrSplit[i]);
                        }
                        if (strArrSplit.length >= 2) {
                            if (!TextUtils.isEmpty(strArrSplit[0])) {
                                LogUtils.i(LogUtils.TAG, "CUtil [checkAndReset] engineVersion=" + strArrSplit[0]);
                                InitProxy.getInstance().setEngineVersion(strArrSplit[0]);
                            }
                            if (TextUtils.isEmpty(strArrSplit[1])) {
                                return;
                            }
                            LogUtils.i(LogUtils.TAG, "CUtil [checkAndReset] resVersion=" + strArrSplit[1]);
                            InitProxy.getInstance().setResVersion(strArrSplit[1]);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                LogUtils.i(LogUtils.TAG, "CUtil [checkAndReset] client_v=" + str);
                InitProxy.getInstance().setEngineVersion(str);
            }
        }
    }

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        if (context == null) {
            return "unknown";
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            return (packageInfo == null || TextUtils.isEmpty(packageInfo.versionName)) ? "unknown" : packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(LogUtils.TAG, "CUtil [getVersionName] NameNotFoundException=" + e.toString());
            e.printStackTrace();
            return "unknown";
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "CUtil [getVersionName] Exception=" + e2.toString());
            e2.printStackTrace();
            return "unknown";
        }
    }

    public static String getEB(Context context) throws JSONException {
        String string = "-1";
        if (context == null) {
            return "-1";
        }
        String assetFileContent = getAssetFileContent(context, "ntunisdk_config");
        LogUtils.i(LogUtils.TAG, "CUtil [getEB] [read ntunisdk_config] ebInfo=" + assetFileContent);
        if (TextUtils.isEmpty(assetFileContent)) {
            assetFileContent = getAssetFileContent(context, "ntunisdk.cfg");
            LogUtils.i(LogUtils.TAG, "CUtil [getEB] [read ntunisdk.cfg] ebInfo=" + assetFileContent);
        }
        if (!TextUtils.isEmpty(assetFileContent)) {
            try {
                JSONObject jSONObject = new JSONObject(assetFileContent);
                if (jSONObject.has("EB")) {
                    string = jSONObject.getString("EB");
                }
            } catch (Exception e) {
                LogUtils.i(LogUtils.TAG, "CUtil [getEB] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
        LogUtils.i(LogUtils.TAG, "CUtil [getEB] final result=" + assetFileContent);
        return string;
    }

    public static void runOnMainThread(Runnable runnable) {
        Log.i(LogUtils.TAG, "CUtil [runOnMainThread] start");
        if (runnable == null) {
            Log.i(LogUtils.TAG, "CUtil [runOnMainThread] param error");
        } else if (isMainThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runOnNewChildThread(final ThreadTask threadTask, String str) {
        Thread thread = new Thread(new Runnable() { // from class: com.netease.androidcrashhandler.util.CUtil.1
            @Override // java.lang.Runnable
            public void run() {
                threadTask.run();
            }
        });
        if (!TextUtils.isEmpty(str)) {
            thread.setName(str);
        }
        thread.start();
    }

    public static String getCertificateSHA1Fingerprint(Context context) throws PackageManager.NameNotFoundException, CertificateException {
        PackageInfo packageInfo;
        CertificateFactory certificateFactory;
        X509Certificate x509Certificate;
        LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] start");
        if (context == null) {
            LogUtils.w(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] context is null");
            return "unknown";
        }
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        if (packageManager == null) {
            LogUtils.w(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] pm is null");
            return "unknown";
        }
        String strByte2HexFormatted = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 64);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] NameNotFoundException =" + e.toString());
            e.printStackTrace();
            packageInfo = null;
        }
        if (packageInfo == null) {
            LogUtils.w(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] packageInfo is null");
            return "unknown";
        }
        Signature[] signatureArr = packageInfo.signatures;
        if (signatureArr == null || signatureArr.length <= 0) {
            LogUtils.w(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] signatures is null");
            return "unknown";
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(signatureArr[0].toByteArray());
        try {
            certificateFactory = CertificateFactory.getInstance("X509");
        } catch (Exception e2) {
            LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] Exception1 =" + e2.toString());
            e2.printStackTrace();
            certificateFactory = null;
        }
        if (certificateFactory == null) {
            LogUtils.w(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] cf is null");
            return "unknown";
        }
        try {
            x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
        } catch (Exception e3) {
            LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] Exception2 =" + e3.toString());
            e3.printStackTrace();
            x509Certificate = null;
        }
        if (x509Certificate == null) {
            LogUtils.w(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] X509Certificate is null");
            return "unknown";
        }
        try {
            strByte2HexFormatted = byte2HexFormatted(MessageDigest.getInstance("SHA1").digest(x509Certificate.getEncoded()));
        } catch (NoSuchAlgorithmException e4) {
            LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] NoSuchAlgorithmException =" + e4.toString());
            e4.printStackTrace();
        } catch (CertificateEncodingException e5) {
            LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] CertificateEncodingException =" + e5.toString());
            e5.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "CUtil [getCertificateSHA1Fingerprint] result =" + strByte2HexFormatted);
        return strByte2HexFormatted;
    }

    private static String byte2HexFormatted(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "unknown";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            String hexString = Integer.toHexString(bArr[i]);
            int length = hexString.length();
            if (length == 1) {
                hexString = "0" + hexString;
            }
            if (length > 2) {
                hexString = hexString.substring(length - 2, length);
            }
            sb.append(hexString.toUpperCase());
            if (i < bArr.length - 1) {
                sb.append(':');
            }
        }
        return sb.toString();
    }

    public static String getCPUType() {
        return getSystemProperty("ro.product.cpu.abi", "ARM").contains("x86") ? "x86" : "ARM";
    }

    public static String getSystemProperty(String str, String str2) throws ClassNotFoundException {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str2 = (String) cls.getMethod(h.c, String.class, String.class).invoke(cls, str, "");
        } catch (Exception e) {
            LogUtils.w(LogUtils.TAG, "CUtil [getSystemProperty] Exception=" + e.toString());
            e.printStackTrace();
        }
        LogUtils.w(LogUtils.TAG, "CUtil [getSystemProperty] " + str + " = " + str2);
        return str2;
    }

    public static String getSuitableUrl(String str) {
        String eb = InitProxy.getInstance().getEB();
        LogUtils.i(LogUtils.TAG, "CUtil [getSuitableUrl] , eb=" + eb);
        return "1".equals(eb) ? str.replaceAll("\\.netease\\.", ".easebar.") : str;
    }

    public static JSONObject addSrcJsonToDesJson(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        if (jSONObject != null && jSONObject2 != null) {
            Iterator<String> itKeys = jSONObject2.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                String strOptString = jSONObject2.optString(next);
                if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(strOptString)) {
                    try {
                        jSONObject.put(next, strOptString);
                    } catch (Exception e) {
                        LogUtils.i(LogUtils.TAG, "CUtil [addSrcJsonToDesJson] Exception =" + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        return jSONObject;
    }

    public static void deleteDir(String str) {
        LogUtils.i(LogUtils.TAG, "CUtil [deleteDir] target:" + str);
        try {
            if (TextUtils.isEmpty(str)) {
                LogUtils.w(LogUtils.TAG, "CUtil [deleteDir] param error");
                return;
            }
            File file = new File(str);
            if (file.isFile()) {
                file.delete();
                return;
            }
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles == null) {
                file.delete();
                return;
            }
            for (File file2 : fileArrListFiles) {
                deleteDir(file2.getAbsolutePath());
            }
            file.delete();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static ArrayList<String> getFileNames(String str) {
        File[] fileArrListFiles = new File(str).listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        if (fileArrListFiles != null) {
            for (File file : fileArrListFiles) {
                if (file.isFile()) {
                    arrayList.add(file.getName());
                }
            }
        }
        return arrayList;
    }

    public static String timestampToFormat(long j) {
        try {
            return new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date(j)) + " " + new SimpleDateFormat("Z", Locale.ENGLISH).format(new Date());
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String transIdBackup() {
        LogUtils.d(LogUtils.TAG, "CUtil [transIdBackup] start");
        return "9999999999999999_" + System.currentTimeMillis() + "_" + String.format(Locale.US, "%09d", Integer.valueOf(new Random().nextInt(1000000000)));
    }

    public static File findSuffixInDir(String str, String str2) {
        File[] fileArrListFiles;
        try {
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
            File file = new File(str2);
            if (!file.exists() || !file.isDirectory() || (fileArrListFiles = file.listFiles()) == null) {
                return null;
            }
            for (File file2 : fileArrListFiles) {
                if (file2.getName().endsWith(str)) {
                    return file2;
                }
            }
            return null;
        }
        return null;
    }

    public static String archSwitch(String str) {
        str.hashCode();
        switch (str) {
            case "x86_64":
                return "x86_64";
            case "x86":
                return "x86";
            case "armeabi-v7a":
                return "arm";
            case "arm64-v8a":
                return "arm64";
            default:
                return str;
        }
    }

    public static boolean isContainSpecialFile(final String[] strArr, String str) {
        LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] start");
        if (strArr == null || strArr.length == 0) {
            LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] No crashes occurred last time");
            return false;
        }
        LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile]  path=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] sUploadFilePath is error");
            return false;
        }
        try {
            String[] list = new File(str).list(new FilenameFilter() { // from class: com.netease.androidcrashhandler.util.CUtil.2
                @Override // java.io.FilenameFilter
                public boolean accept(File file, String str2) {
                    LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] file=" + str2);
                    for (String str3 : strArr) {
                        LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] file name=" + str2 + ", tag=" + str3);
                        if (str2.endsWith(str3)) {
                            LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] has crash file=" + str2);
                            return true;
                        }
                    }
                    return false;
                }
            });
            if (list != null) {
                return list.length > 0;
            }
            return false;
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "CUtil [isContainSpecialFile] Exception=" + e.toString());
            e.printStackTrace();
            return false;
        }
    }
}