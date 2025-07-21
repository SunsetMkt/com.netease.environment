package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnPushListener {
    void onCancelLocalPushFinished(boolean z);

    void onGetUserPushFinished(boolean z);

    void onSendLocalNotificationFinished(int i);

    void onSendPushNotificationFinished(boolean z);

    void onSetUserPushFinished(boolean z);
}