package com.netease.mpay.ps.codescanner.task;

import com.netease.mpay.ps.codescanner.server.api.ConfirmLoginResp;

/* loaded from: classes5.dex */
public interface LoginWebListener {
    void onLoginFailure(String str);

    void onLoginSuccess(ConfirmLoginResp confirmLoginResp);
}