package com.netease.androidcrashhandler.unknownCrash;

/* loaded from: classes5.dex */
public interface AppLifeCallback {
    void isAppForeground(boolean z);

    void onActivityCreate();

    void onActivityDestroy();

    void onActivityStart();

    void onActivityStop();
}