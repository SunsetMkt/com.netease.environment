package com.netease.mpay.ps.codescanner.task;

import com.netease.mpay.ps.codescanner.server.api.GetQRCodeInfoResp;

/* loaded from: classes5.dex */
public interface GetLoginInfoListener {
    void onFailure(String str);

    void onSuccess(GetQRCodeInfoResp getQRCodeInfoResp);
}