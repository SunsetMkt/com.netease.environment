package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnCodeScannerListener {
    public static final int CHANNEL_NOT_SUPPORT = 31;
    public static final int EXT_QRCODE_SUEESS = 21;
    public static final int FAIL = 10;
    public static final int PARAM_INVALID = 1;
    public static final int PERMISSIONS_DENY = 4;
    public static final int PICTURE_EMPTY = 11;
    public static final int PICTURE_OUT_OF_DATE = 12;
    public static final int QR_CODE_INVALID = 5;
    public static final int RETURN_GAME = 3;
    public static final int SUCCESS = 0;
    public static final int UID_MISMATCH = 2;

    void codeScannerFinish(int i, String str);
}