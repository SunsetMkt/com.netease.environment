package com.netease.ntunisdk.unilogger.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.unilogger.UniLoggerProxy;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Utils {
    private static JSONObject mChannelInfosJson;

    public static String getLocalIpFromModuleDeviceinfo() throws JSONException {
        LogUtils.v_inner("Utils [getLocalIpFromModuleDeviceinfo] start");
        ModulesManager.getInst().init(UniLoggerProxy.context);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getLocalIpAddress");
            return ModulesManager.getInst().extendFunc(LogUtils.TAG, "deviceInfo", jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Utils [getLocalIpFromModuleDeviceinfo] Exception=" + e.toString());
            return "unknown";
        }
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean str2File(String str, String str2, String str3) {
        boolean z = false;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str2)) {
            LogUtils.i_inner("Utils [str2File] param error");
            return false;
        }
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
                            z = true;
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
                        LogUtils.i_inner("Utils [str2File] file does not exist");
                    }
                } else {
                    LogUtils.i_inner("Utils [str2File] directory does not exist");
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Utils [str2File] Exception=" + e.toString());
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0092 A[Catch: Exception -> 0x0096, TRY_ENTER, TryCatch #3 {Exception -> 0x0096, blocks: (B:18:0x0051, B:19:0x0054, B:33:0x0084, B:40:0x0092, B:44:0x009a, B:45:0x009d), top: B:52:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x009a A[Catch: Exception -> 0x0096, TryCatch #3 {Exception -> 0x0096, blocks: (B:18:0x0051, B:19:0x0054, B:33:0x0084, B:40:0x0092, B:44:0x009a, B:45:0x009d), top: B:52:0x0033 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String file2Str(java.lang.String r5, java.lang.String r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto Lb7
            boolean r0 = android.text.TextUtils.isEmpty(r5)
            if (r0 == 0) goto L10
            goto Lb7
        L10:
            java.io.File r0 = new java.io.File
            r0.<init>(r5, r6)
            boolean r0 = r0.exists()
            if (r0 != 0) goto L2d
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r0 = "Utils [file2Str] file is not exists, fileName="
            r5.<init>(r0)
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.netease.ntunisdk.unilogger.utils.LogUtils.e_inner(r5)
            return r1
        L2d:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L64
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L64
            r3.<init>(r5, r6)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L64
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L64
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L5a java.lang.Exception -> L5d
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L5a java.lang.Exception -> L5d
            r6.<init>(r2)     // Catch: java.lang.Throwable -> L5a java.lang.Exception -> L5d
            r5.<init>(r6)     // Catch: java.lang.Throwable -> L5a java.lang.Exception -> L5d
        L47:
            java.lang.String r6 = r5.readLine()     // Catch: java.lang.Exception -> L58 java.lang.Throwable -> L8f
            if (r6 == 0) goto L51
            r0.append(r6)     // Catch: java.lang.Exception -> L58 java.lang.Throwable -> L8f
            goto L47
        L51:
            r5.close()     // Catch: java.lang.Exception -> L96
        L54:
            r2.close()     // Catch: java.lang.Exception -> L96
            goto L8a
        L58:
            r6 = move-exception
            goto L67
        L5a:
            r6 = move-exception
            r5 = r1
            goto L90
        L5d:
            r6 = move-exception
            r5 = r1
            goto L67
        L60:
            r6 = move-exception
            r5 = r1
            r2 = r5
            goto L90
        L64:
            r6 = move-exception
            r5 = r1
            r2 = r5
        L67:
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L8f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8f
            r3.<init>()     // Catch: java.lang.Throwable -> L8f
            java.lang.String r4 = "Utils [file2Str] Exception ="
            r3.append(r4)     // Catch: java.lang.Throwable -> L8f
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> L8f
            r3.append(r6)     // Catch: java.lang.Throwable -> L8f
            java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> L8f
            com.netease.ntunisdk.unilogger.utils.LogUtils.w_inner(r6)     // Catch: java.lang.Throwable -> L8f
            if (r5 == 0) goto L87
            r5.close()     // Catch: java.lang.Exception -> L96
        L87:
            if (r2 == 0) goto L8a
            goto L54
        L8a:
            java.lang.String r5 = r0.toString()
            return r5
        L8f:
            r6 = move-exception
        L90:
            if (r5 == 0) goto L98
            r5.close()     // Catch: java.lang.Exception -> L96
            goto L98
        L96:
            r5 = move-exception
            goto L9e
        L98:
            if (r2 == 0) goto L9d
            r2.close()     // Catch: java.lang.Exception -> L96
        L9d:
            throw r6     // Catch: java.lang.Exception -> L96
        L9e:
            r5.printStackTrace()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r0 = "Utils [file2Str] Exception="
            r6.<init>(r0)
            java.lang.String r5 = r5.toString()
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            com.netease.ntunisdk.unilogger.utils.LogUtils.w_inner(r5)
            return r1
        Lb7:
            java.lang.String r5 = "Utils [file2Str] param error"
            com.netease.ntunisdk.unilogger.utils.LogUtils.i_inner(r5)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.unilogger.utils.Utils.file2Str(java.lang.String, java.lang.String):java.lang.String");
    }

    public static boolean deleteFile(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        File file = new File(str, str2);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteFile(String str) {
        LogUtils.i_inner("Utils [deleteFile] filePath=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtils.i_inner("Utils [deleteFile] result1=false");
            return false;
        }
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        boolean zDelete = file.delete();
        LogUtils.i_inner("Utils [deleteFile] result2=" + zDelete);
        return zDelete;
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            LogUtils.e_inner("Utils [deleteFile] file is not exist");
            return false;
        }
        boolean zDelete = file.delete();
        LogUtils.i_inner("Utils [deleteFile] result2=" + zDelete);
        return zDelete;
    }

    public static boolean deleteAllFilesByFileNameList(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return false;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            deleteFile(it.next());
        }
        return true;
    }

    public static boolean deleteAllFilesByFileList(ArrayList<File> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return false;
        }
        Iterator<File> it = arrayList.iterator();
        while (it.hasNext()) {
            deleteFile(it.next().getAbsolutePath());
        }
        return true;
    }

    public static String getPackageName(Context context) {
        LogUtils.v_inner("Utils [getPackageName] start");
        if (context == null) {
            LogUtils.e_inner("Utils [getPackageName] context error");
            return "unknown";
        }
        return context.getPackageName();
    }

    public static String getUniSDKBaseVersionReflexUniSDK() throws ClassNotFoundException {
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.SdkMgr");
            return (String) cls.getDeclaredMethod("getBaseVersion", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException e) {
            LogUtils.w_inner("Utils [getUniSDKBaseVersionReflexUniSDK] ClassNotFoundException=" + e);
            e.printStackTrace();
            return "unknown";
        } catch (IllegalAccessException e2) {
            LogUtils.w_inner("Utils [getUniSDKBaseVersionReflexUniSDK] IllegalAccessException=" + e2);
            e2.printStackTrace();
            return "unknown";
        } catch (IllegalArgumentException e3) {
            LogUtils.w_inner("Utils [getUniSDKBaseVersionReflexUniSDK] IllegalArgumentException=" + e3);
            e3.printStackTrace();
            return "unknown";
        } catch (NoSuchMethodException e4) {
            LogUtils.w_inner("Utils [getUniSDKBaseVersionReflexUniSDK] NoSuchMethodException=" + e4);
            e4.printStackTrace();
            return "unknown";
        } catch (InvocationTargetException e5) {
            LogUtils.w_inner("Utils [getUniSDKBaseVersionReflexUniSDK] InvocationTargetException=" + e5);
            e5.printStackTrace();
            return "unknown";
        } catch (Exception e6) {
            LogUtils.w_inner("Utils [getUniSDKBaseVersionReflexUniSDK] Exception=" + e6);
            e6.printStackTrace();
            return "unknown";
        }
    }

    public static String getAssetFileContent(Context context, String str) throws IOException {
        if (context == null || TextUtils.isEmpty(str)) {
            LogUtils.e_inner("Utils [getAssetFileContent] param is error");
            return null;
        }
        try {
            InputStream inputStreamOpen = context.getAssets().open(str);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            inputStreamOpen.close();
            return new String(bArr);
        } catch (IOException e) {
            LogUtils.w_inner("Utils [getAssetFileContent] IOException=" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject getChannelInfosFromAssetsFile() {
        JSONObject jSONObjectOptJSONObject;
        if (mChannelInfosJson == null) {
            try {
                String assetFileContent = getAssetFileContent(UniLoggerProxy.context, "channel_infos_data");
                if (!TextUtils.isEmpty(assetFileContent)) {
                    JSONObject jSONObject = new JSONObject(assetFileContent);
                    if (jSONObject.has("main_channel") && (jSONObjectOptJSONObject = jSONObject.optJSONObject("main_channel")) != null && jSONObjectOptJSONObject.length() > 0) {
                        Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
                        while (itKeys.hasNext()) {
                            mChannelInfosJson = jSONObjectOptJSONObject.optJSONObject(itKeys.next());
                        }
                    }
                }
            } catch (Exception e) {
                LogUtils.w_inner("Utils [getChannelInfosFromAssetsFile] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
        if (mChannelInfosJson != null) {
            LogUtils.i_inner("Utils [getChannelInfosFromAssetsFile] mChannelInfosJson=" + mChannelInfosJson.toString());
        }
        return mChannelInfosJson;
    }

    public static String getChannelIdFromAssetsFile() {
        JSONObject channelInfosFromAssetsFile = getChannelInfosFromAssetsFile();
        return (channelInfosFromAssetsFile == null || !channelInfosFromAssetsFile.has("channel_id")) ? "unknown" : channelInfosFromAssetsFile.optString("channel_id", "unknown");
    }

    public static String getChannelVersionFromAssetsFile() {
        JSONObject channelInfosFromAssetsFile = getChannelInfosFromAssetsFile();
        return (channelInfosFromAssetsFile == null || !channelInfosFromAssetsFile.has("version")) ? "unknown" : channelInfosFromAssetsFile.optString("version", "unknown");
    }

    public static String getEBFromAssetsFile(Context context) throws IOException {
        String string;
        string = "-1";
        if (context == null) {
            return "-1";
        }
        String assetFileContent = getAssetFileContent(context, "ntunisdk_config");
        LogUtils.i_inner("Utils [getEB] [read ntunisdk_config] ebInfo=" + assetFileContent);
        if (TextUtils.isEmpty(assetFileContent)) {
            assetFileContent = getAssetFileContent(context, "ntunisdk.cfg");
        }
        LogUtils.i_inner("Utils [getEB] [read ntunisdk.cfg] ebInfo=" + assetFileContent);
        if (!TextUtils.isEmpty(assetFileContent)) {
            try {
                JSONObject jSONObject = new JSONObject(assetFileContent);
                string = jSONObject.has("EB") ? jSONObject.getString("EB") : "-1";
                LogUtils.i_inner("Utils [getEB] result=" + assetFileContent);
            } catch (Exception e) {
                LogUtils.w_inner("Utils [getEB] Exception=" + e.toString());
                e.printStackTrace();
            }
        }
        LogUtils.i_inner("Utils [getEB] final result=" + assetFileContent);
        return string;
    }

    public static boolean isOversea() {
        return !TextUtils.isEmpty(GlobalPrarm.EB) && "1".equals(GlobalPrarm.EB);
    }

    public static ArrayList<String> searchFilesBySuffix(String str, final String str2) {
        final ArrayList<String> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return arrayList;
        }
        LogUtils.i_inner("Utils [searchFile] dirPath=" + str + ", suffix=" + str2);
        try {
            new File(str).list(new FilenameFilter() { // from class: com.netease.ntunisdk.unilogger.utils.Utils.1
                @Override // java.io.FilenameFilter
                public boolean accept(File file, String str3) {
                    if (TextUtils.isEmpty(str2)) {
                        arrayList.add(file.getAbsolutePath() + File.separator + str3);
                        return true;
                    }
                    if (!str3.endsWith(str2)) {
                        return true;
                    }
                    arrayList.add(file.getAbsolutePath() + File.separator + str3);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Utils [searchFile] Exception=" + e.toString());
        }
        return arrayList;
    }

    public static String getFileCreatorByFileName(String str) {
        return TextUtils.isEmpty(str) ? "" : str.split("_")[0];
    }

    public static String getFileCreatorByFilePath(String str) {
        String[] strArrSplit;
        return (TextUtils.isEmpty(str) || (strArrSplit = str.split("/")) == null || strArrSplit.length <= 0) ? "" : getFileCreatorByFileName(strArrSplit[strArrSplit.length - 1]);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x004e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean fileDateMatch(java.io.File r8, int r9) {
        /*
            if (r8 == 0) goto L4e
            boolean r0 = r8.exists()
            if (r0 == 0) goto L4e
            if (r9 <= 0) goto L4e
            long r0 = r8.lastModified()
            long r2 = java.lang.System.currentTimeMillis()
            long r4 = r2 - r0
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            r6 = 60
            long r4 = r4 / r6
            long r4 = r4 / r6
            r6 = 24
            long r4 = r4 / r6
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r6 = "Utils [fileDateMatch] day="
            r8.<init>(r6)
            r8.append(r4)
            java.lang.String r6 = ", lastModifiedTime="
            r8.append(r6)
            r8.append(r0)
            java.lang.String r0 = ", currentTime="
            r8.append(r0)
            r8.append(r2)
            java.lang.String r0 = ", expire="
            r8.append(r0)
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            com.netease.ntunisdk.unilogger.utils.LogUtils.i_inner(r8)
            long r8 = (long) r9
            int r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r8 >= 0) goto L4e
            r8 = 1
            goto L4f
        L4e:
            r8 = 0
        L4f:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r0 = "Utils [fileDateMatch] result="
            r9.<init>(r0)
            r9.append(r8)
            java.lang.String r9 = r9.toString()
            com.netease.ntunisdk.unilogger.utils.LogUtils.i_inner(r9)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.unilogger.utils.Utils.fileDateMatch(java.io.File, int):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean fileSizeMatch(java.io.File r4, long r5) {
        /*
            if (r4 == 0) goto L3d
            boolean r0 = r4.exists()
            if (r0 == 0) goto L3d
            r0 = 0
            int r0 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r0 <= 0) goto L3d
            long r0 = r4.length()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Utils [fileSizeMatch] fileLength="
            r2.<init>(r3)
            r2.append(r0)
            java.lang.String r3 = ", carrierLimit="
            r2.append(r3)
            r2.append(r5)
            java.lang.String r3 = ", fileName="
            r2.append(r3)
            java.lang.String r4 = r4.getAbsolutePath()
            r2.append(r4)
            java.lang.String r4 = r2.toString()
            com.netease.ntunisdk.unilogger.utils.LogUtils.i_inner(r4)
            int r4 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r4 >= 0) goto L3d
            r4 = 1
            goto L3e
        L3d:
            r4 = 0
        L3e:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "Utils [fileSizeMatch] result="
            r5.<init>(r6)
            r5.append(r4)
            java.lang.String r5 = r5.toString()
            com.netease.ntunisdk.unilogger.utils.LogUtils.i_inner(r5)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.unilogger.utils.Utils.fileSizeMatch(java.io.File, long):boolean");
    }

    public static File createFile(String str, String str2) throws IOException {
        File file = null;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            File file2 = new File(str, str2);
            try {
                if (file2.exists()) {
                    return file2;
                }
                file2.createNewFile();
                return file2;
            } catch (Exception e) {
                e = e;
                file = file2;
                e.printStackTrace();
                LogUtils.w_inner("Utils [createFile] Exception=" + e.toString());
                return file;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static String showUnitResult(Config.UnitResult unitResult) {
        StringBuilder sb = new StringBuilder();
        if (unitResult != null) {
            sb.append(", unitResult.writeEnable=");
            sb.append(unitResult.writeEnable);
            sb.append(", unitResult.uploadEnable=");
            sb.append(unitResult.uploadEnable);
            sb.append(", unitResult.orMap=");
            if (unitResult.orMap != null) {
                sb.append(unitResult.orMap.toString());
            } else {
                sb.append("null");
            }
            sb.append(", unitResult.andMap=");
            if (unitResult.andMap != null) {
                sb.append(unitResult.andMap.toString());
            } else {
                sb.append("null");
            }
            sb.append(", unitResult.isRemote=");
            sb.append(unitResult.isRemote);
        }
        return sb.toString();
    }

    public static String getRealGameId(Context context) throws IOException {
        String strOptString;
        LogUtils.i_inner("Utils [getRealGameId] start");
        try {
            strOptString = SdkMgr.getInst().getPropStr("JF_GAMEID", "");
        } catch (Throwable th) {
            th.printStackTrace();
            LogUtils.w_inner("Utils [getRealGameId] Throwable=" + th.toString());
            strOptString = null;
        }
        if (TextUtils.isEmpty(strOptString)) {
            String assetFileContent = getAssetFileContent(context, Const.Common.NTUNISDK_COMMON_DATA);
            try {
                if (!TextUtils.isEmpty(assetFileContent)) {
                    strOptString = new JSONObject(assetFileContent).optString("JF_GAMEID");
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.w_inner("Utils [getRealGameId] Exception=" + e.toString());
            }
        }
        if (TextUtils.isEmpty(strOptString)) {
            LogUtils.i_inner("Utils [getRealGameId] Unisdk\uff1aassets\u4e0b\u7f3a\u5c11ntunisdk_common_data\u6587\u4ef6\uff0c \u9ed8\u8ba4\u4f7f\u7528g0\u8bf7\u6c42\u65e5\u5fd7SDK\u914d\u7f6e");
            strOptString = "g0";
        }
        LogUtils.i_inner("Utils [getRealGameId] gameId=" + strOptString);
        return strOptString;
    }
}