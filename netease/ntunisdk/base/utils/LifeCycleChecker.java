package com.netease.ntunisdk.base.utils;

import android.os.Handler;
import android.os.Looper;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes3.dex */
public class LifeCycleChecker {
    private static final long DELAY_CHECK_MILLIS = 3000;
    private static final String TAG = "LifeCycleChecker: ";
    private static volatile LifeCycleChecker sInstance;
    private volatile Timer mCheckTimer;
    private volatile boolean mGameHasFocus;
    private volatile boolean mIsWaiting;
    private volatile OnTimeoutListener mOnTimeoutListener;

    public static class LibTag {
        public static final String AIDETECT = "aidetect";
        public static final String CHECK_ENTER = "check_enter";
        public static final String CHILD_PROTECT = "child_protect";
        public static final String PROTOCOL = "protocol";
    }

    public interface OnTimeoutListener {
        void onTimeout();
    }

    private LifeCycleChecker() {
    }

    public static LifeCycleChecker getInst() {
        if (sInstance == null) {
            synchronized (LifeCycleChecker.class) {
                if (sInstance == null) {
                    sInstance = new LifeCycleChecker();
                }
            }
        }
        return sInstance;
    }

    public synchronized void setOnTimeoutListener(OnTimeoutListener onTimeoutListener) {
        UniSdkUtils.d(TAG, "setOnTimeoutListener: ".concat(String.valueOf(onTimeoutListener)));
        this.mOnTimeoutListener = onTimeoutListener;
    }

    public synchronized void start(String str) {
        UniSdkUtils.d(TAG, "startCheck: ".concat(String.valueOf(str)));
        if (isWaiting()) {
            cancel();
        }
        setWaiting(true);
        if (this.mGameHasFocus) {
            schedule();
        }
    }

    public synchronized void stop(String str) {
        UniSdkUtils.d(TAG, "stopCheck: ".concat(String.valueOf(str)));
        if (isWaiting()) {
            cancel();
            setWaiting(false);
        }
    }

    public synchronized void onGameResume() {
        UniSdkUtils.d(TAG, "onGameResume");
    }

    public synchronized void onGameFocusChanged(boolean z) {
        UniSdkUtils.d(TAG, "onGameFocusChanged: ".concat(String.valueOf(z)));
        if (this.mGameHasFocus == z) {
            return;
        }
        this.mGameHasFocus = z;
        if (z) {
            if (isWaiting()) {
                schedule();
            }
        } else {
            cancel();
        }
    }

    public synchronized void onGamePause() {
        UniSdkUtils.d(TAG, "onGamePause");
    }

    private synchronized void schedule() {
        UniSdkUtils.d(TAG, "timer scheduled");
        this.mCheckTimer = new Timer();
        this.mCheckTimer.schedule(new TimerTask() { // from class: com.netease.ntunisdk.base.utils.LifeCycleChecker.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                LifeCycleChecker.this.checkTimeout();
            }
        }, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void checkTimeout() {
        this.mCheckTimer = null;
        UniSdkUtils.d(TAG, "timer checking, waiting = " + this.mIsWaiting);
        if (isWaiting()) {
            onTimeout();
            setWaiting(false);
        }
    }

    private synchronized void onTimeout() {
        UniSdkUtils.d(TAG, "onTimeout");
        if (this.mOnTimeoutListener != null) {
            final OnTimeoutListener onTimeoutListener = this.mOnTimeoutListener;
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.netease.ntunisdk.base.utils.LifeCycleChecker.2
                @Override // java.lang.Runnable
                public void run() {
                    onTimeoutListener.onTimeout();
                }
            });
        }
    }

    private synchronized void cancel() {
        if (this.mCheckTimer != null) {
            UniSdkUtils.d(TAG, "timer cancelled");
            this.mCheckTimer.cancel();
            this.mCheckTimer = null;
        }
    }

    private synchronized boolean isWaiting() {
        if (this.mIsWaiting) {
            if (this.mOnTimeoutListener != null) {
                return true;
            }
        }
        return false;
    }

    private synchronized void setWaiting(boolean z) {
        UniSdkUtils.d(TAG, "setWaiting = ".concat(String.valueOf(z)));
        this.mIsWaiting = z;
    }
}