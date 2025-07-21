package com.netease.mpay.ps.codescanner;

/* loaded from: classes3.dex */
public interface CodeScannerCallback {
    void onFinish(CodeScannerRetCode codeScannerRetCode, String str);

    void onScanPaymentSuccess(String str, String str2);
}