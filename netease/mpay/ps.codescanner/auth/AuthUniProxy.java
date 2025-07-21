package com.netease.mpay.ps.codescanner.auth;

import com.netease.ntunisdk.external.protocol.UniSDKProxy;

/* loaded from: classes5.dex */
public class AuthUniProxy extends UniSDKProxy {
    private boolean isRunning = false;

    @Override // com.netease.ntunisdk.external.protocol.UniSDKProxy
    public boolean isSupportShortCut() {
        return true;
    }

    public synchronized void setRunning(boolean z) {
        this.isRunning = z;
    }

    @Override // com.netease.ntunisdk.external.protocol.UniSDKProxy
    public boolean hasAppRunning() {
        return this.isRunning;
    }
}