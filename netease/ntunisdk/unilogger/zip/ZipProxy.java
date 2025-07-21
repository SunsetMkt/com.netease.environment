package com.netease.ntunisdk.unilogger.zip;

import android.text.TextUtils;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.uploader.UploadCallBack;
import com.netease.ntunisdk.unilogger.uploader.UploadProxy;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ZipProxy {
    private static ZipProxy zipProxy;
    private int expire = 30;
    private long fileLimit = 10485760;

    public class ZipResult {
        public boolean zipSucess = true;
        public ArrayList<File> filePathList = new ArrayList<>();
        public String zipFileName = null;

        public ZipResult() {
        }
    }

    private ZipProxy() {
    }

    public static ZipProxy getInstance() {
        if (zipProxy == null) {
            zipProxy = new ZipProxy();
        }
        return zipProxy;
    }

    public synchronized void init(int i, long j) {
        ZipCore.getInstance().start();
        this.expire = i;
        this.fileLimit = j * 1024;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x01a8  */
    /* JADX WARN: Type inference failed for: r10v12, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r11v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v14 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.netease.ntunisdk.unilogger.zip.ZipProxy.ZipResult zipFileListInSameList(java.util.ArrayList<java.io.File> r24, boolean r25, boolean r26, java.lang.String r27) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 439
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.unilogger.zip.ZipProxy.zipFileListInSameList(java.util.ArrayList, boolean, boolean, java.lang.String):com.netease.ntunisdk.unilogger.zip.ZipProxy$ZipResult");
    }

    public ZipResult zipSingleFileInSameThread(File file, boolean z, boolean z2, String str) {
        ArrayList<File> arrayList = new ArrayList<>();
        arrayList.add(file);
        return zipFileListInSameList(arrayList, z, z2, str);
    }

    public synchronized void zipFileListInOtherThread(ArrayList<File> arrayList, boolean z, boolean z2, String str, ZipCallBack zipCallBack) {
        ZipUnit zipUnit = new ZipUnit(arrayList, z, z2);
        zipUnit.setZipCallBack(zipCallBack);
        zipUnit.setZipDirPath(str);
        ZipCore.getInstance().addZipUnitToQueue(zipUnit);
    }

    public synchronized void zipSingleFileInOtherThread(File file, boolean z, boolean z2, String str, ZipCallBack zipCallBack) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(file);
        ZipUnit zipUnit = new ZipUnit(arrayList, z, z2);
        zipUnit.setZipCallBack(zipCallBack);
        zipUnit.setZipDirPath(str);
        ZipCore.getInstance().addZipUnitToQueue(zipUnit);
    }

    public void zipHistoryFileInOtherThread(JSONObject jSONObject) {
        ArrayList<String> arrayListSearchFilesBySuffix = Utils.searchFilesBySuffix(GlobalPrarm.uniLoggerLogDirPath, null);
        LogUtils.i_inner("ZipProxy [zipHistoryFile] all fileList=" + arrayListSearchFilesBySuffix.toString() + ", preUnitResult=" + jSONObject.toString());
        ArrayList<String> needZipFileList = getNeedZipFileList(arrayListSearchFilesBySuffix, jSONObject);
        StringBuilder sb = new StringBuilder("ZipProxy [zipHistoryFile] need upload fileList=");
        sb.append(needZipFileList.toString());
        LogUtils.i_inner(sb.toString());
        AnonymousClass1 anonymousClass1 = new ZipCallBack() { // from class: com.netease.ntunisdk.unilogger.zip.ZipProxy.1
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.unilogger.zip.ZipCallBack
            public void onZipResult(boolean z, ArrayList<File> arrayList, String str) {
                LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] suc=" + z + ", zipPath=" + str);
                if (z) {
                    Iterator<File> it = arrayList.iterator();
                    while (it.hasNext()) {
                        File next = it.next();
                        if (next != null && next.exists()) {
                            next.delete();
                        }
                    }
                    UploadProxy.getInstance().uploadFile(str, new UploadCallBack() { // from class: com.netease.ntunisdk.unilogger.zip.ZipProxy.1.1
                        C00841() {
                        }

                        @Override // com.netease.ntunisdk.unilogger.uploader.UploadCallBack
                        public void onUploadResult(int i, String str2) {
                            LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] [logger-trace] code=" + i + ", filePath=" + str2);
                            if (i == 1) {
                                File file = new File(str2);
                                if (file.exists()) {
                                    LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] delete file, filePath=" + str2);
                                    file.delete();
                                }
                            }
                        }
                    });
                }
            }

            /* renamed from: com.netease.ntunisdk.unilogger.zip.ZipProxy$1$1 */
            class C00841 implements UploadCallBack {
                C00841() {
                }

                @Override // com.netease.ntunisdk.unilogger.uploader.UploadCallBack
                public void onUploadResult(int i, String str2) {
                    LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] [logger-trace] code=" + i + ", filePath=" + str2);
                    if (i == 1) {
                        File file = new File(str2);
                        if (file.exists()) {
                            LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] delete file, filePath=" + str2);
                            file.delete();
                        }
                    }
                }
            }
        };
        if (needZipFileList == null || needZipFileList.size() <= 0) {
            return;
        }
        Iterator<String> it = needZipFileList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            File file = new File(next);
            if (file.length() == 0) {
                LogUtils.i_inner("ZipProxy [zipHistoryFile] file size is 0, delete file, filePath=" + next);
                file.delete();
            } else {
                zipSingleFileInOtherThread(file, true, true, null, anonymousClass1);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.unilogger.zip.ZipProxy$1 */
    class AnonymousClass1 implements ZipCallBack {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.unilogger.zip.ZipCallBack
        public void onZipResult(boolean z, ArrayList<File> arrayList, String str) {
            LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] suc=" + z + ", zipPath=" + str);
            if (z) {
                Iterator<File> it = arrayList.iterator();
                while (it.hasNext()) {
                    File next = it.next();
                    if (next != null && next.exists()) {
                        next.delete();
                    }
                }
                UploadProxy.getInstance().uploadFile(str, new UploadCallBack() { // from class: com.netease.ntunisdk.unilogger.zip.ZipProxy.1.1
                    C00841() {
                    }

                    @Override // com.netease.ntunisdk.unilogger.uploader.UploadCallBack
                    public void onUploadResult(int i, String str2) {
                        LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] [logger-trace] code=" + i + ", filePath=" + str2);
                        if (i == 1) {
                            File file = new File(str2);
                            if (file.exists()) {
                                LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] delete file, filePath=" + str2);
                                file.delete();
                            }
                        }
                    }
                });
            }
        }

        /* renamed from: com.netease.ntunisdk.unilogger.zip.ZipProxy$1$1 */
        class C00841 implements UploadCallBack {
            C00841() {
            }

            @Override // com.netease.ntunisdk.unilogger.uploader.UploadCallBack
            public void onUploadResult(int i, String str2) {
                LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] [logger-trace] code=" + i + ", filePath=" + str2);
                if (i == 1) {
                    File file = new File(str2);
                    if (file.exists()) {
                        LogUtils.i_inner("ZipProxy [zipHistoryFile] [onZipResult] [onUploadResult] delete file, filePath=" + str2);
                        file.delete();
                    }
                }
            }
        }
    }

    private ArrayList<String> getNeedZipFileList(ArrayList<String> arrayList, JSONObject jSONObject) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        if (arrayList != null && arrayList.size() != 0) {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!next.endsWith(".zip")) {
                    String fileCreatorByFilePath = Utils.getFileCreatorByFilePath(next);
                    LogUtils.i_inner("ZipProxy [getNeedZipFileList] creator=" + fileCreatorByFilePath);
                    File file = new File(next);
                    if (file.exists()) {
                        if (!TextUtils.isEmpty(fileCreatorByFilePath) && jSONObject.has(fileCreatorByFilePath) && jSONObject.optBoolean(fileCreatorByFilePath) && fileMatch(file)) {
                            arrayList2.add(next);
                        } else {
                            LogUtils.i_inner("ZipProxy [getNeedZipFileList] delete filepath=" + next);
                            file.delete();
                        }
                    }
                }
            }
        }
        return arrayList2;
    }

    private boolean fileMatch(File file) {
        boolean z = Utils.fileDateMatch(file, this.expire) && Utils.fileSizeMatch(file, this.fileLimit);
        LogUtils.i_inner("UploadProxy [fileMatch] result=" + z + ", fileName=" + file.getName());
        return z;
    }
}