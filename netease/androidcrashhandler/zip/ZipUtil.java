package com.netease.androidcrashhandler.zip;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.WifiUtil;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ZipUtil {
    private static void compress(File file, ZipOutputStream zipOutputStream, String str, boolean z) throws Exception {
        byte[] bArr = new byte[2048];
        if (file.isFile()) {
            zipOutputStream.putNextEntry(new ZipEntry(str));
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i != -1) {
                    zipOutputStream.write(bArr, 0, i);
                } else {
                    zipOutputStream.closeEntry();
                    fileInputStream.close();
                    return;
                }
            }
        } else {
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles == null || fileArrListFiles.length == 0) {
                if (z) {
                    zipOutputStream.putNextEntry(new ZipEntry(str + "/"));
                    zipOutputStream.closeEntry();
                    return;
                }
                return;
            }
            for (File file2 : fileArrListFiles) {
                if (z) {
                    compress(file2, zipOutputStream, str + "/" + file2.getName(), z);
                } else {
                    compress(file2, zipOutputStream, file2.getName(), z);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:165:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x01c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int zip(java.lang.String r18, java.util.ArrayList<java.lang.String> r19, java.lang.String r20, java.lang.String r21) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 528
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.zip.ZipUtil.zip(java.lang.String, java.util.ArrayList, java.lang.String, java.lang.String):int");
    }

    public static void updateCrashTimeToDi(long j, String str, String str2) {
        LogUtils.i(LogUtils.TAG, "ZipUtil [updateCrashTimeToDi] start, crashTime=" + j + ", actDiFileName=" + str2);
        if (0 == j || TextUtils.isEmpty(str2)) {
            LogUtils.i(LogUtils.TAG, "ZipUtil [updateCrashTimeToDi] params error");
            return;
        }
        try {
            String str3 = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.ENGLISH).format(new Date(j)) + " " + new SimpleDateFormat("Z", Locale.ENGLISH).format(new Date());
            CUtil.addInfoToDiFile(str, str2, "time", str3);
            LogUtils.i(LogUtils.TAG, "ZipUtil [updateCrashTimeToDi] update crash time: " + str3);
        } catch (Throwable th) {
            th.printStackTrace();
            LogUtils.i(LogUtils.TAG, "ZipUtil [updateCrashTimeToDi] throwable: " + th.toString());
        }
    }

    public static boolean checkFileTimeValid(File file) {
        if (file == null) {
            return false;
        }
        long jLastModified = file.lastModified();
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = ((((jCurrentTimeMillis - jLastModified) / 1000) / 60) / 60) / 24;
        LogUtils.i(LogUtils.TAG, "ZipUtil [checkFileTimeValid] lastModified = " + jLastModified + ", currentTimeMillis=" + jCurrentTimeMillis + ", day=" + j + ", mExpire=" + ConfigCore.getInstance().getmExpire());
        return j < ((long) ConfigCore.getInstance().getmExpire());
    }

    public static boolean checkFileSizeValid(File file) {
        if (file == null) {
            return false;
        }
        long j = ConfigCore.getInstance().getmCarrierLimit();
        long length = file.length() / 1024;
        boolean zIsConnectNet = WifiUtil.isConnectNet();
        boolean zIsConnectedMobile = WifiUtil.isConnectedMobile();
        LogUtils.i(LogUtils.TAG, "ZipUtil [checkFileSizeValid] file length = " + length + ", limitSize=" + j + ", isConnectNet=" + zIsConnectNet + ", isConnectedMobile=" + zIsConnectedMobile);
        return (zIsConnectNet && zIsConnectedMobile && length > j) ? false : true;
    }

    public static void deleteOldFileUploadPathFile(ArrayList<String> arrayList, boolean z) {
        LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] start");
        if (arrayList == null || arrayList.size() <= 0 || TextUtils.isEmpty(InitProxy.sOldUploadFilePath)) {
            LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] param error");
            return;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            File file = new File(InitProxy.sOldUploadFilePath, next);
            if (file.exists()) {
                LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] fileName=" + next);
                if (z) {
                    if (file.isDirectory()) {
                        LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] pass dir=" + next);
                    } else {
                        LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] delete file=" + next);
                        file.delete();
                    }
                } else if (file.isDirectory()) {
                    LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] pass dir=" + next);
                } else if (checkIsShareFile(next)) {
                    LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] file=" + next + ", \u5176\u4e3a\u5171\u4eab\u6587\u4ef6\u6682\u4e0d\u5220\u9664");
                } else {
                    LogUtils.i(LogUtils.TAG, "ZipUtil [deleteFile] delete file=" + next);
                    file.delete();
                }
            }
        }
    }

    private static boolean checkIsShareFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.endsWith(Const.FileNameTag.DI_FILE) || str.equals("ntunisdk_so_uuids") || str.equals(Const.FileNameTag.CFG_FILE);
    }

    public static void deleteOldFileUploadPathFile(File file) {
        file.delete();
        String name = file.getName();
        if (name.endsWith(Const.FileNameTag.CFG_FILE)) {
            return;
        }
        InitProxy.getInstance();
        File file2 = new File(InitProxy.sUploadFilePath, name + Const.FileNameTag.CFG_FILE);
        if (file2.exists()) {
            file2.delete();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x00ea A[Catch: Exception -> 0x00e6, TRY_LEAVE, TryCatch #7 {Exception -> 0x00e6, blocks: (B:121:0x00e2, B:125:0x00ea), top: B:134:0x00e2 }] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x00e2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.io.File gZip(java.lang.String r8, java.lang.String r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 262
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.zip.ZipUtil.gZip(java.lang.String, java.lang.String):java.io.File");
    }

    public static boolean checkNetAndAgreement() {
        if (!NTCrashHunterKit.mIsThroughUserAgreement) {
            LogUtils.i(LogUtils.TAG, "ZipUtil [checkNetAndAgreement] \u6ca1\u6709\u540c\u610f\u7528\u6237\u534f\u8bae\uff0c\u4e0d\u89e6\u53d1\u5206\u53d1\u4e0a\u4f20\u673a\u5236");
            return false;
        }
        if (WifiUtil.isConnectNet() && (!WifiUtil.isConnectedMobile() || !ConfigCore.getInstance().ismWifiOnly())) {
            return true;
        }
        LogUtils.i(LogUtils.TAG, "ZipUtil [checkNetAndAgreement] \u7f51\u7edc\u72b6\u6001\u4e0d\u6b63\u786e");
        return false;
    }

    public static JSONObject getCfgFileContent(String str) throws Throwable {
        LogUtils.i(LogUtils.TAG, "ZipCore [getCfgFileContent] start:" + str);
        String strFile2Str = CUtil.file2Str(str + Const.FileNameTag.CFG_FILE);
        if (!TextUtils.isEmpty(strFile2Str)) {
            try {
                return new JSONObject(strFile2Str);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [getCfgFileContent] finish");
        return null;
    }

    /* renamed from: com.netease.androidcrashhandler.zip.ZipUtil$1 */
    class AnonymousClass1 implements Comparator<File> {
        AnonymousClass1() {
        }

        @Override // java.util.Comparator
        public int compare(File file, File file2) {
            long jLastModified = file.lastModified() - file2.lastModified();
            if (jLastModified > 0) {
                return -1;
            }
            return jLastModified == 0 ? 0 : 1;
        }
    }

    private static void sortFiles(File[] fileArr) {
        if (fileArr != null) {
            try {
                if (fileArr.length > 0) {
                    Arrays.sort(fileArr, new Comparator<File>() { // from class: com.netease.androidcrashhandler.zip.ZipUtil.1
                        AnonymousClass1() {
                        }

                        @Override // java.util.Comparator
                        public int compare(File file, File file2) {
                            long jLastModified = file.lastModified() - file2.lastModified();
                            if (jLastModified > 0) {
                                return -1;
                            }
                            return jLastModified == 0 ? 0 : 1;
                        }
                    });
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getValidZipFileList(String str) {
        LogUtils.i(LogUtils.TAG, "ZipCore [getValidZipFileList] start");
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File[] fileArrListFiles = new File(str).listFiles();
            if (fileArrListFiles != null) {
                sortFiles(fileArrListFiles);
                for (File file : fileArrListFiles) {
                    String name = file.getName();
                    if (file.isFile() && name.endsWith(".zip")) {
                        if (!checkFileTimeValid(file) || !checkFileSizeValid(file)) {
                            LogUtils.i(LogUtils.TAG, "ZipCore [getSuitableZipFileList] deleteZipAndCfgFile=" + file.toString());
                            deleteZipAndCfgFile(file);
                        } else {
                            arrayList.add(file.getAbsolutePath());
                        }
                    } else if (file.isFile() && name.endsWith(Const.FileNameTag.ZIP_TEMP_FILE) && (!checkFileTimeValid(file) || !checkFileSizeValid(file))) {
                        LogUtils.i(LogUtils.TAG, "ZipCore [getSuitableZipFileList] deleteZipAndCfgFile=" + file.toString());
                        deleteZipAndCfgFile(file);
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [getSuitableZipFileList] zipFileList=" + arrayList.toString());
        return arrayList;
    }

    public static void deleteZipAndCfgFile(File file) {
        file.delete();
        String name = file.getName();
        if (name.endsWith(".zip")) {
            File file2 = new File(InitProxy.sUploadFilePath, name + Const.FileNameTag.CFG_FILE);
            if (file2.exists()) {
                file2.delete();
            }
        }
    }

    public static void checkAndcopyUuidFile(List<String> list, String str) {
        if (list.contains("ntunisdk_so_uuids")) {
            LogUtils.i(LogUtils.TAG, "ZipCore [handleUuidFile]  so_uuid file exit");
            return;
        }
        LogUtils.i(LogUtils.TAG, "ZipCore [handleUuidFile] copy so_uuid file");
        if (CUtil.copyFile(InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids", str + File.separator + "ntunisdk_so_uuids")) {
            list.add("ntunisdk_so_uuids");
            LogUtils.i(LogUtils.TAG, "ZipCore [handleUuidFile] arrayList: " + list.toString());
            return;
        }
        String assetFileContent = CUtil.getAssetFileContent(NTCrashHunterKit.sharedKit().getContext(), "ntunisdk_so_uuids");
        if (TextUtils.isEmpty(assetFileContent)) {
            return;
        }
        CUtil.str2File(assetFileContent, InitProxy.sFilesDir, "ntunisdk_so_uuids");
        if (CUtil.str2File(assetFileContent, str, "ntunisdk_so_uuids")) {
            list.add("ntunisdk_so_uuids");
            LogUtils.i(LogUtils.TAG, "ZipCore [handleUuidFile] arrayList: " + list.toString());
        }
    }

    public static String mergeLogFile(File file, File file2) throws IOException {
        if (!file.exists() || !file2.exists()) {
            return null;
        }
        fileMerge(file, file2);
        LogUtils.i(LogUtils.TAG, "ZipCore [mergeLogFile] fileMerge");
        file.delete();
        return file.getName();
    }

    private static void fileMerge(File file, File file2) throws IOException {
        BufferedWriter bufferedWriter;
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(file2, true));
            } catch (Throwable th) {
                th = th;
                bufferedWriter = null;
            }
        } catch (Throwable th2) {
            th = th2;
            bufferedWriter = null;
        }
        try {
            bufferedWriter.write("---------------------------------------------------------------------\n");
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                } else {
                    bufferedReader.close();
                    bufferedWriter.close();
                    try {
                        bufferedReader.close();
                        try {
                            bufferedWriter.close();
                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (IOException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedReader2 = bufferedReader;
            try {
                th.printStackTrace();
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e3) {
                        throw new RuntimeException(e3);
                    }
                }
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e4) {
                        throw new RuntimeException(e4);
                    }
                }
            } catch (Throwable th4) {
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e5) {
                        throw new RuntimeException(e5);
                    }
                }
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e6) {
                        throw new RuntimeException(e6);
                    }
                }
                throw th4;
            }
        }
    }
}