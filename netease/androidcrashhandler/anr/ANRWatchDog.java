package com.netease.androidcrashhandler.anr;

import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.netease.androidcrashhandler.util.LogUtils;

/* loaded from: classes4.dex */
public class ANRWatchDog extends Thread {
    private static final int DEFAULT_ANR_TIMEOUT = 5000;
    private ANRInterceptor _anrInterceptor;
    private ANRListener _anrListener;
    private boolean _ignoreDebugger;
    private InterruptionListener _interruptionListener;
    private boolean _logThreadsWithoutStackTrace;
    private String _namePrefix;
    private volatile boolean _reported;
    private volatile long _tick;
    private final Runnable _ticker;
    private final int _timeoutInterval;
    private final Handler _uiHandler;
    private static final ANRListener DEFAULT_ANR_LISTENER = new ANRListener() { // from class: com.netease.androidcrashhandler.anr.ANRWatchDog.1
        @Override // com.netease.androidcrashhandler.anr.ANRWatchDog.ANRListener
        public void onAppNotResponding(ANRError aNRError) {
            throw aNRError;
        }
    };
    private static final ANRInterceptor DEFAULT_ANR_INTERCEPTOR = new ANRInterceptor() { // from class: com.netease.androidcrashhandler.anr.ANRWatchDog.2
        @Override // com.netease.androidcrashhandler.anr.ANRWatchDog.ANRInterceptor
        public long intercept(long j) {
            return 0L;
        }
    };
    private static final InterruptionListener DEFAULT_INTERRUPTION_LISTENER = new InterruptionListener() { // from class: com.netease.androidcrashhandler.anr.ANRWatchDog.3
        @Override // com.netease.androidcrashhandler.anr.ANRWatchDog.InterruptionListener
        public void onInterrupted(InterruptedException interruptedException) {
            Log.w("ANRWatchdog", "Interrupted: " + interruptedException.getMessage());
        }
    };

    public interface ANRInterceptor {
        long intercept(long j);
    }

    public interface ANRListener {
        void onAppNotResponding(ANRError aNRError);
    }

    public interface InterruptionListener {
        void onInterrupted(InterruptedException interruptedException);
    }

    public ANRWatchDog() {
        this(5000);
    }

    public ANRWatchDog(int i) {
        this._anrListener = DEFAULT_ANR_LISTENER;
        this._anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
        this._interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
        this._uiHandler = new Handler(Looper.getMainLooper());
        this._namePrefix = "";
        this._logThreadsWithoutStackTrace = false;
        this._ignoreDebugger = false;
        this._tick = 0L;
        this._reported = false;
        this._ticker = new Runnable() { // from class: com.netease.androidcrashhandler.anr.ANRWatchDog.4
            @Override // java.lang.Runnable
            public void run() {
                ANRWatchDog.this._tick = 0L;
                ANRWatchDog.this._reported = false;
            }
        };
        this._timeoutInterval = i;
    }

    public int getTimeoutInterval() {
        return this._timeoutInterval;
    }

    public ANRWatchDog setANRListener(ANRListener aNRListener) {
        if (aNRListener == null) {
            this._anrListener = DEFAULT_ANR_LISTENER;
        } else {
            this._anrListener = aNRListener;
        }
        return this;
    }

    public ANRWatchDog setANRInterceptor(ANRInterceptor aNRInterceptor) {
        if (aNRInterceptor == null) {
            this._anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
        } else {
            this._anrInterceptor = aNRInterceptor;
        }
        return this;
    }

    public ANRWatchDog setInterruptionListener(InterruptionListener interruptionListener) {
        if (interruptionListener == null) {
            this._interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
        } else {
            this._interruptionListener = interruptionListener;
        }
        return this;
    }

    public ANRWatchDog setReportThreadNamePrefix(String str) {
        if (str == null) {
            str = "";
        }
        this._namePrefix = str;
        return this;
    }

    public ANRWatchDog setReportMainThreadOnly() {
        this._namePrefix = null;
        return this;
    }

    public ANRWatchDog setReportAllThreads() {
        this._namePrefix = "";
        return this;
    }

    public ANRWatchDog setLogThreadsWithoutStackTrace(boolean z) {
        this._logThreadsWithoutStackTrace = z;
        return this;
    }

    public ANRWatchDog setIgnoreDebugger(boolean z) {
        this._ignoreDebugger = z;
        return this;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws InterruptedException {
        ANRError aNRErrorNewMainOnly;
        LogUtils.i(LogUtils.TAG, "ANRWatchDog [run] start");
        setName("|ANR-WatchDog|");
        long jIntercept = this._timeoutInterval;
        while (!isInterrupted()) {
            boolean z = this._tick == 0;
            this._tick += jIntercept;
            if (z) {
                this._uiHandler.post(this._ticker);
            }
            try {
                Thread.sleep(jIntercept);
                if (this._tick != 0 && !this._reported) {
                    long j = this._tick;
                    if (!this._ignoreDebugger && (Debug.isDebuggerConnected() || Debug.waitingForDebugger())) {
                        Log.w("ANRWatchdog", "An ANR was detected but ignored because the debugger is connected (you can prevent this with setIgnoreDebugger(true))");
                        this._reported = true;
                    } else {
                        jIntercept = this._anrInterceptor.intercept(j);
                        if (jIntercept <= 0) {
                            String str = this._namePrefix;
                            if (str != null) {
                                aNRErrorNewMainOnly = ANRError.New(j, str, this._logThreadsWithoutStackTrace);
                            } else {
                                aNRErrorNewMainOnly = ANRError.NewMainOnly(j);
                            }
                            if (this._tick != 0 && !this._reported) {
                                try {
                                    this._anrListener.onAppNotResponding(aNRErrorNewMainOnly);
                                } catch (Exception unused) {
                                }
                                jIntercept = this._timeoutInterval;
                                this._reported = true;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                LogUtils.w(LogUtils.TAG, "ANR-WatchDog InterruptedException=" + e.toString());
                this._interruptionListener.onInterrupted(e);
                return;
            }
        }
    }
}