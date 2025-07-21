package com.netease.ngrtc;

/* loaded from: classes4.dex */
public interface NgRTCObserver {
    void OnConnectSuccess(boolean z);

    void OnDisconnect();

    void OnError(int i, String str);

    void OnMemberJoined(String str);

    void OnMemberLeaved(String str);

    void OnMemberMuted(String str);

    void OnParticipantListRefreshed(String str, int i, int i2, int i3);

    void OnReconnect(int i);

    void OnSpeakBegin(String str);

    void OnSpeakEnd(String str);
}