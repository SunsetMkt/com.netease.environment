package com.netease.androidcrashhandler.entity.di;

import android.content.Context;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class DiProxy {
    private static final String TAG = "DiProxy";
    private static DiProxy sDiProxy;
    private DiInfo mDiInfo = null;
    private boolean mIsStart = true;
    private int mDiFreshTimeInterval = ConfigCore.getInstance().getmDiFreshTime();
    private int mDiFreshTimeAddInterval = 0;

    private DiProxy() {
    }

    public static DiProxy getInstance() {
        if (sDiProxy == null) {
            sDiProxy = new DiProxy();
        }
        return sDiProxy;
    }

    public void init(Context context) {
        LogUtils.i(LogUtils.TAG, "DiProxy [init] start");
        if (this.mDiInfo == null) {
            this.mDiInfo = new DiInfo(context);
            this.mDiFreshTimeInterval = ConfigCore.getInstance().getmDiFreshTime();
            CUtil.copyFile(InitProxy.sUploadFilePath + File.separator + DiInfo.sCurFileName, InitProxy.sUploadFilePath + File.separator + DiInfo.sPreFileName);
        }
    }

    public void freshPreUserAgreement() {
        LogUtils.i(LogUtils.TAG, "DiProxy [freshSecureData] start");
        synchronized (DiProxy.class) {
            this.mDiInfo.freshSecureData();
            this.mDiInfo.writeToLocalFile();
        }
    }

    public void start() {
        LogUtils.i(LogUtils.TAG, "DiProxy [start] start");
        CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.entity.di.DiProxy.1
            @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
            public void run() throws InterruptedException {
                while (DiProxy.this.mIsStart) {
                    LogUtils.i(LogUtils.TAG, "DiProxy [start] call fresh");
                    synchronized (DiProxy.class) {
                        try {
                            DiProxy.this.mDiInfo.fresh();
                            DiProxy.this.mDiInfo.writeToLocalFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DiProxy.this.threadSleep();
                }
            }
        }, "crashhunter_di_thread");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void threadSleep() throws InterruptedException {
        try {
            LogUtils.i(LogUtils.TAG, "DiProxy [threadSleep] \u4f11\u7720 +" + this.mDiFreshTimeInterval + "s");
            Thread.sleep((long) (this.mDiFreshTimeInterval * 1000));
            if (this.mDiFreshTimeAddInterval > 0) {
                LogUtils.i(LogUtils.TAG, "DiProxy [threadSleep] \u589e\u52a0\u4f11\u7720 +" + this.mDiFreshTimeAddInterval + "s");
                Thread.sleep((long) (this.mDiFreshTimeAddInterval * 1000));
                this.mDiFreshTimeAddInterval = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDiInfoToLocalFile() {
        synchronized (DiProxy.class) {
            LogUtils.i(LogUtils.TAG, "DiProxy [updateDiInfoToLocalFile] start");
            if (this.mDiInfo != null) {
                LogUtils.i(LogUtils.TAG, "DiProxy [updateDiInfoToLocalFile] mIsThroughUserAgreement=" + NTCrashHunterKit.mIsThroughUserAgreement);
                if (NTCrashHunterKit.mIsThroughUserAgreement) {
                    this.mDiInfo.fresh();
                } else {
                    this.mDiInfo.freshSecureData();
                }
                this.mDiInfo.writeToLocalFile();
                LogUtils.i(LogUtils.TAG, "DiProxy [updateDiInfoToLocalFile] end");
            } else {
                LogUtils.i(LogUtils.TAG, "DiProxy [updateDiInfoToLocalFile] mDiInfo is null");
            }
        }
    }

    public JSONObject getDiInfoJson() {
        JSONObject diInfoJson;
        synchronized (DiProxy.class) {
            diInfoJson = this.mDiInfo.getDiInfoJson();
        }
        return diInfoJson;
    }

    public void setDiFreshTimeInterval(int i, boolean z) {
        LogUtils.i(LogUtils.TAG, "DiProxy [setDiFreshTimeInterval] start");
        LogUtils.i(LogUtils.TAG, "DiProxy [setDiFreshTimeInterval] diFreshTimeInterval=" + i + ", useConfig=" + z);
        if (z) {
            this.mDiFreshTimeInterval = ConfigCore.getInstance().getmDiFreshTime();
            this.mDiFreshTimeAddInterval = 0;
            LogUtils.i(LogUtils.TAG, "DiProxy [setDiFreshTimeInterval] Config diFreshTime=" + this.mDiFreshTimeInterval);
            return;
        }
        int i2 = i - this.mDiFreshTimeInterval;
        if (i2 > 0) {
            this.mDiFreshTimeAddInterval = i2;
        } else {
            this.mDiFreshTimeAddInterval = 0;
        }
        this.mDiFreshTimeInterval = i;
    }
}