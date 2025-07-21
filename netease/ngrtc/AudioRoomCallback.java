package com.netease.ngrtc;

import com.netease.ngrtc.ProtoClient;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public interface AudioRoomCallback {
    void onConnectSuccess(boolean z);

    void onDisconnect(ProtoClient.RTCError rTCError, String str);

    void onError(ProtoClient.RTCError rTCError, String str);

    void onHeadsetPlugged(boolean z);

    void onInitFailed(int i, String str);

    void onInitSuccess();

    void onMemberJoined(ParticipantInfo participantInfo);

    void onMemberLeaved(ParticipantInfo participantInfo);

    void onMemberMuted(ParticipantInfo participantInfo);

    void onParticipantListRefreshed(ArrayList<ParticipantInfo> arrayList, int i, int i2, int i3);

    void onReconnect(ProtoClient.RTCError rTCError);

    void onSpeakBegin(String str);

    void onSpeakEnd(String str);
}