package com.netease.androidcrashhandler.so;

import android.content.Context;
import android.text.TextUtils;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes5.dex */
public class SoUuidCore {
    public static final String NTUNISDK_SO_UUIDS = "ntunisdk_so_uuids";
    private static final String TAG = "SoHandleCore";
    private static SoUuidCore sSoUuidCore;

    private SoUuidCore() {
    }

    public static SoUuidCore getInstance() {
        if (sSoUuidCore == null) {
            sSoUuidCore = new SoUuidCore();
        }
        return sSoUuidCore;
    }

    public void storageSoUuidInfosToSdkDir(final Context context) {
        LogUtils.i(LogUtils.TAG, "SoUuidCore [storageSoUuidInfosToSdkDir] start");
        if (context == null) {
            return;
        }
        CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.so.SoUuidCore.1
            @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
            public void run() throws InterruptedException {
                boolean zStorageAssetSoUuidFileToSdkDir = SoUuidCore.this.storageAssetSoUuidFileToSdkDir(context);
                try {
                    Thread.sleep(5000L);
                    if (!zStorageAssetSoUuidFileToSdkDir) {
                        zStorageAssetSoUuidFileToSdkDir = SoUuidCore.this.createSoUuidFileOnRuntime(context);
                    }
                    if (zStorageAssetSoUuidFileToSdkDir) {
                        return;
                    }
                    LogUtils.i(LogUtils.TAG, "SoUuidCore [storageSoUuidInfosToSdkDir] error");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "storageSoUuidInfosToSdkDir");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean storageAssetSoUuidFileToSdkDir(Context context) {
        LogUtils.i(LogUtils.TAG, "SoUuidCore [storageAssetSoUuidFileToSdkDir] start");
        boolean zStr2File = false;
        try {
            if (!new File(InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids").exists()) {
                String assetFileContent = CUtil.getAssetFileContent(context, "ntunisdk_so_uuids");
                if (!TextUtils.isEmpty(assetFileContent)) {
                    zStr2File = CUtil.str2File(assetFileContent, InitProxy.sFilesDir, "ntunisdk_so_uuids");
                }
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "SoUuidCore [storageAssetSoUuidFileToSdkDir] Exception=" + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "SoUuidCore [storageAssetSoUuidFileToSdkDir] result=" + zStr2File);
        return zStr2File;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createSoUuidFileOnRuntime(Context context) {
        LogUtils.i(LogUtils.TAG, "SoUuidCore [createSoUuidFileOnRuntime] start");
        boolean zStr2File = false;
        if (context == null) {
            return false;
        }
        try {
            if (new File(InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids").exists()) {
                zStr2File = true;
            } else {
                String soUuidInfos = getSoUuidInfos(getSoFilesFromAPK(context));
                if (!TextUtils.isEmpty(soUuidInfos)) {
                    zStr2File = CUtil.str2File(soUuidInfos, InitProxy.sFilesDir, "ntunisdk_so_uuids");
                }
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "SoUuidCore [createSoUuidFileOnRuntime] Exception=" + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "SoUuidCore [createSoUuidFileOnRuntime] result=" + zStr2File);
        return zStr2File;
    }

    private File[] getSoFilesFromAPK(Context context) throws NullPointerException {
        LogUtils.i(LogUtils.TAG, "SoUuidCore [getSoFilesFromAPK] start");
        return new File(context.getApplicationInfo().nativeLibraryDir).listFiles(new FilenameFilter() { // from class: com.netease.androidcrashhandler.so.SoUuidCore.2
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str) {
                return true;
            }
        });
    }

    private String getSoUuidInfos(File[] fileArr) {
        LogUtils.i(LogUtils.TAG, "SoUuidCore [getSoUuidInfos] start");
        StringBuffer stringBuffer = new StringBuffer();
        if (fileArr != null && fileArr.length > 0) {
            for (int i = 0; i < fileArr.length; i++) {
                String soBuildId = AndroidCrashHandler.getInstance().getSoBuildId(fileArr[i].getAbsolutePath());
                fileArr[i].getAbsolutePath();
                String soLoadingType = AndroidCrashHandler.getInstance().getSoLoadingType();
                stringBuffer.append("lib/");
                stringBuffer.append(soLoadingType);
                stringBuffer.append("/");
                stringBuffer.append(fileArr[i].getName());
                stringBuffer.append("\t");
                stringBuffer.append(CUtil.archSwitch(soLoadingType));
                stringBuffer.append("\t");
                stringBuffer.append(soBuildId);
                stringBuffer.append("\n");
                LogUtils.i(LogUtils.TAG, "SoUuidCore [getSoUuidInfos] line:" + stringBuffer.toString());
            }
        }
        return stringBuffer.toString();
    }
}