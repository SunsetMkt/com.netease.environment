package com.netease.mpay.auth;

/* loaded from: classes.dex */
public class SDKContext {
    private static SDKContext sInstance;
    private boolean mIsColdLaunch;

    private SDKContext() {
    }

    public static SDKContext getInstance() {
        if (sInstance == null) {
            synchronized (SDKContext.class) {
                if (sInstance == null) {
                    sInstance = new SDKContext();
                }
            }
        }
        return sInstance;
    }

    public boolean isColdLaunch() {
        return this.mIsColdLaunch;
    }

    public void setColdLaunch(boolean z) {
        this.mIsColdLaunch = z;
    }
}