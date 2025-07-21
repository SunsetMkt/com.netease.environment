package com.netease.unisdk.ngvoice;

/* loaded from: classes5.dex */
public interface NgVoiceCallback {
    public static final String ERROR_CREATE_FILE_ERROR = "create_file_error";
    public static final String ERROR_EXCEPTION = "exception";
    public static final String ERROR_NO_ENOUGH_SPACE = "no_enough_space";

    void onDownloadFinish(boolean z, String str, String str2);

    void onPlaybackFinish(boolean z);

    void onRecordFinish(boolean z, String str, float f, String str2);

    void onRequestPermissions(boolean z);

    void onTranslateFinish(String str, String str2);

    void onUploadFinish(boolean z, String str, String str2);
}