package com.netease.unisdk.ngvoice;

/* loaded from: classes5.dex */
public interface NgVoiceInterface {
    boolean hasPermissions();

    void ntCancelRecord();

    void ntClearVoiceCache(long j);

    void ntDownloadVoiceFile(String str, String str2);

    void ntGetTranslation(String str);

    float ntGetVoiceAmplitude();

    void ntStartPlayback(String str);

    void ntStartRecord(String str);

    void ntStopPlayback();

    void ntStopRecord();

    void ntUploadVoiceFile(String str);

    void onRequestPermissionsResult(int i, String[] strArr, int[] iArr);

    void requestPermissions();
}