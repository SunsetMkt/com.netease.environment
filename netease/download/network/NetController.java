package com.netease.download.network;

/* loaded from: classes5.dex */
public class NetController {
    private static final int NO_INTERRUPTED = 0;
    private static NetController mController;
    private int mInterruptedCode = 0;
    private int mStatus;

    private NetController() {
    }

    public static NetController getInstances() {
        if (mController == null) {
            mController = new NetController();
        }
        return mController;
    }

    public boolean isInterrupted() {
        return this.mInterruptedCode != 0;
    }

    public void setInterruptedCode(int i) {
        this.mInterruptedCode = i;
    }

    public int getInterruptedCode() {
        return this.mInterruptedCode;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int i) {
        this.mStatus = i;
    }

    public void restore() {
        this.mInterruptedCode = 0;
    }
}