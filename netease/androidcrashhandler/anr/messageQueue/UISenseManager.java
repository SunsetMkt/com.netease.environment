package com.netease.androidcrashhandler.anr.messageQueue;

import android.os.Looper;

/* loaded from: classes5.dex */
public class UISenseManager implements MessageCallback {
    private static final long DEFAULT_FRAME_DURATION = 16666667;
    private UISenseManager MANAGER = new UISenseManager();
    private boolean mInit;

    @Override // com.netease.androidcrashhandler.anr.messageQueue.MessageCallback
    public void messageEnd(long j, long j2) {
    }

    @Override // com.netease.androidcrashhandler.anr.messageQueue.MessageCallback
    public void messageStart(long j, long j2) {
    }

    public UISenseManager getInstance() {
        return this.MANAGER;
    }

    private UISenseManager() {
        this.mInit = false;
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            this.mInit = false;
        } else {
            LooperMessageLoggingManager.getInstance().registerCallback(this);
        }
    }
}