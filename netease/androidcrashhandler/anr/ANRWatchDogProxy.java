package com.netease.androidcrashhandler.anr;

import android.os.Process;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;

/* loaded from: classes4.dex */
public class ANRWatchDogProxy {
    public static String sAnrUploadFilePath;
    private static ANRWatchDogProxy sAnrWatchDogProxy;
    private String anrLastTimeFileName = null;

    private ANRWatchDogProxy() {
    }

    public static ANRWatchDogProxy getInstance() {
        if (sAnrWatchDogProxy == null) {
            sAnrWatchDogProxy = new ANRWatchDogProxy();
        }
        return sAnrWatchDogProxy;
    }

    public void start() {
        LogUtils.i(LogUtils.TAG, "ANRWatchDogProxy [start] start");
        if (ConfigCore.getInstance().isWacthDogEnabled()) {
            LogUtils.i(LogUtils.TAG, "ANRWatchDogProxy [start] \u5f00\u542fAnr\u76d1\u63a7\u673a\u5236");
            updateAnrFile();
            LogUtils.i(LogUtils.TAG, "ANRWatchDogProxy [getInstance] MessageEnabled = " + ConfigCore.getInstance().ismMessageEnabled());
            return;
        }
        LogUtils.i(LogUtils.TAG, "ANRWatchDogProxy [start] \u4e0d\u5f00\u542fAnr\u76d1\u63a7\u673a\u5236");
    }

    public void updateAnrFile() {
        sAnrUploadFilePath = InitProxy.sUploadFilePath + "/ANR_TRACE_" + Process.myPid() + "_" + System.currentTimeMillis();
        File file = new File(sAnrUploadFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        AndroidCrashHandler.getInstance().setAnrTracePath(sAnrUploadFilePath);
    }

    public String getAnrLastTimeFileName() {
        return this.anrLastTimeFileName;
    }

    public void setAnrLastTimeFileName(String str) {
        this.anrLastTimeFileName = str;
    }
}