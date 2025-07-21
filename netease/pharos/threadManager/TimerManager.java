package com.netease.pharos.threadManager;

import com.netease.pharos.util.LogUtil;
import java.util.Timer;

/* loaded from: classes6.dex */
public class TimerManager {
    public static final String TAG = "TimerManager";
    private static TimerManager sTimerManager;
    private Timer mTimer = null;

    private TimerManager() {
    }

    public static TimerManager getInstance() {
        if (sTimerManager == null) {
            synchronized (TimerManager.class) {
                if (sTimerManager == null) {
                    LogUtil.i(TAG, "TimerManager [getInstance] new TimerManager()");
                    sTimerManager = new TimerManager();
                }
            }
        }
        return sTimerManager;
    }

    public Timer getTimer() {
        if (this.mTimer == null) {
            LogUtil.i(TAG, "ThreadPoolManager [getTimer] new Timer");
            this.mTimer = new Timer();
        }
        return this.mTimer;
    }
}