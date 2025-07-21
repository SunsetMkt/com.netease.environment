package com.netease.mpay.ps.codescanner;

/* loaded from: classes3.dex */
public enum CodeScannerRetCode {
    SUCCESS(0),
    PARAM_INVALID(101),
    UID_MISMATCH(102),
    RETURN_GAME(103),
    QR_CODE_INVALID(104);

    private int code;

    CodeScannerRetCode(int i) {
        this.code = i;
    }
}