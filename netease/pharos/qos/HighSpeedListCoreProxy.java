package com.netease.pharos.qos;

import com.netease.pharos.util.LogUtil;

/* loaded from: classes4.dex */
public class HighSpeedListCoreProxy {
    private static final String TAG = "HighSpeedListCoreProxy";
    private static HighSpeedListCoreProxy sHighSpeedListCoreProxy;
    private boolean mIsInit = false;
    private boolean mIsStart = false;
    private HighSpeedListCore mHighSpeedListCore = null;

    private HighSpeedListCoreProxy() {
    }

    public static HighSpeedListCoreProxy getInstance() {
        if (sHighSpeedListCoreProxy == null) {
            synchronized (HighSpeedListCoreProxy.class) {
                if (sHighSpeedListCoreProxy == null) {
                    sHighSpeedListCoreProxy = new HighSpeedListCoreProxy();
                }
            }
        }
        return sHighSpeedListCoreProxy;
    }

    public synchronized void init() {
        if (!this.mIsInit) {
            LogUtil.i(TAG, "HighSpeedListCoreProxy [init] start");
            if (this.mHighSpeedListCore == null) {
                this.mHighSpeedListCore = new HighSpeedListCore();
            }
            this.mIsInit = true;
        } else {
            LogUtil.i(TAG, "HighSpeedListCoreProxy [init] already init");
        }
    }

    public synchronized void start() {
        LogUtil.i(TAG, "HighSpeedListCoreProxy [start] [harbor\u6a21\u5757] start");
        if (this.mHighSpeedListCore == null) {
            this.mIsInit = false;
            init();
        }
        LogUtil.i(TAG, "HighSpeedListCoreProxy [start] [harbor\u6a21\u5757] \u5f00\u59cb\u83b7\u53d6\u9ad8\u901f\u5217\u8868");
        LogUtil.i(TAG, "HighSpeedListCoreProxy [start] [harbor\u6a21\u5757] \u83b7\u53d6\u9ad8\u901f\u5217\u8868  \u7ed3\u679c = " + this.mHighSpeedListCore.start());
    }

    public synchronized void clean() {
        LogUtil.i(TAG, "HighSpeedListCoreProxy [clean] start");
        HighSpeedListCore highSpeedListCore = this.mHighSpeedListCore;
        if (highSpeedListCore != null) {
            highSpeedListCore.clean();
        }
        HighSpeedListInfo.getInstance().clean();
        CheckHighSpeedResult.getInstance().reset();
        this.mIsInit = false;
        this.mIsStart = false;
    }
}