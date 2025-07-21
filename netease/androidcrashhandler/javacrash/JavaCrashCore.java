package com.netease.androidcrashhandler.javacrash;

import android.os.Process;
import android.util.Log;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.entity.di.DiInfo;
import com.netease.androidcrashhandler.entity.di.DiProxy;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.thirdparty.clientLogModule.ClientLog;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.StorageToFileProxy;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class JavaCrashCore implements Thread.UncaughtExceptionHandler {
    private static JavaCrashCore sJavaCrashCore;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private File mJavaCrashRootDir;
    public JavaCrashCallBack mJavaCrashCallBack = null;
    private String mLastTimeCrashDir = "";

    private JavaCrashCore() {
    }

    public static JavaCrashCore getInstance() {
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [getInstance] start");
        if (sJavaCrashCore == null) {
            sJavaCrashCore = new JavaCrashCore();
        }
        return sJavaCrashCore;
    }

    public void createJavaCrashRootDir() {
        File file = new File(InitProxy.sUploadFilePath, Const.FileNameTag.DIR_JAVA_CRASH + Process.myPid() + "_" + System.currentTimeMillis());
        this.mJavaCrashRootDir = file;
        if (file.exists()) {
            return;
        }
        this.mJavaCrashRootDir.mkdirs();
    }

    public void start() {
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [start] start");
        if (InitProxy.getInstance().isDetectJavaCrash()) {
            LogUtils.i(LogUtils.TAG, "JavaCrashCore [start] \u542f\u52a8Java crash\u68c0\u6d4b\u673a\u5236");
            this.mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
            try {
                Thread.setDefaultUncaughtExceptionHandler(this);
            } catch (Exception e) {
                LogUtils.i(LogUtils.TAG, "JavaCrashCore [start] Exception=" + e.toString());
                e.printStackTrace();
            }
            createJavaCrashRootDir();
            return;
        }
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [start] \u4e0d\u542f\u52a8Java crash\u68c0\u6d4b\u673a\u5236");
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [uncaughtException] start, ");
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [uncaughtException] Thread id:" + thread.getId() + ", Name=" + thread.getName());
        if (th == null) {
            LogUtils.i(LogUtils.TAG, "JavaCrashCore [uncaughtException] param error");
        } else {
            handleException(thread, th);
        }
    }

    public void handleException(Thread thread, Throwable th) {
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [handleException] start");
        try {
            if (th == null) {
                LogUtils.i(LogUtils.TAG, "JavaCrashCore [handleException] param error");
                return;
            }
            ClientLog.getInstence().sendClientLog("OCCUR JAVA CRASH");
            LogUtils.i(LogUtils.TAG, "JavaCrashCore [handleException] getExceptionInfo");
            StringBuffer stringBuffer = new StringBuffer("thread name:");
            stringBuffer.append(thread.getName());
            stringBuffer.append("\n\n");
            stringBuffer.append(getExceptionInfo(th));
            CUtil.str2File(stringBuffer.toString(), this.mJavaCrashRootDir.getAbsolutePath(), System.currentTimeMillis() + Const.FileNameTag.ACI_FILE);
            if (this.mJavaCrashCallBack != null) {
                LogUtils.i(LogUtils.TAG, "JavaCrashCore [handleException] callback");
                this.mJavaCrashCallBack.crashCallBack(th);
            }
            DiProxy.getInstance().updateDiInfoToLocalFile();
            CUtil.copyFile(InitProxy.sUploadFilePath + "/" + DiInfo.sCurFileName, this.mJavaCrashRootDir.getAbsolutePath() + "/" + DiInfo.sCurFileName);
            AndroidCrashHandler.getInstance().writeFdInfoToLocalFile(this.mJavaCrashRootDir.getAbsolutePath());
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("event_info", "OCCUR JAVA CRASH");
            } catch (Exception e) {
                e.printStackTrace();
            }
            AndroidCrashHandler.callbackToUser(4, jSONObject);
            AndroidCrashHandler.callbackToUser(11, jSONObject);
            StorageToFileProxy.getInstances().finish();
            if (ConfigCore.getInstance().isAppCrashThrowable()) {
                this.mDefaultCrashHandler.uncaughtException(thread, th);
                return;
            }
            Log.e(LogUtils.TAG, "java Crash not Throwable:============================");
            th.printStackTrace();
            System.exit(0);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static String getExceptionInfo(Throwable th) throws IOException {
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [getExceptionInfo] start");
        String string = null;
        if (th == null) {
            LogUtils.i(LogUtils.TAG, "JavaCrashCore [getExceptionInfo] param error");
            return null;
        }
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                cause.printStackTrace(printWriter);
            }
            string = stringWriter.toString();
            stringWriter.close();
            printWriter.close();
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "JavaCrashCore [getExceptionInfo] Exception =" + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(LogUtils.TAG, "=======================================================================");
        LogUtils.i(LogUtils.TAG, "JavaCrashCore [getExceptionInfo] java crash info");
        LogUtils.i(LogUtils.TAG, string);
        LogUtils.i(LogUtils.TAG, "=======================================================================");
        return string;
    }

    public void setJavaCrashCallBack(JavaCrashCallBack javaCrashCallBack) {
        this.mJavaCrashCallBack = javaCrashCallBack;
    }

    public String getLastTimeCrashDir() {
        return this.mLastTimeCrashDir;
    }

    public void setLastTimeCrashDir(String str) {
        this.mLastTimeCrashDir = str;
    }
}