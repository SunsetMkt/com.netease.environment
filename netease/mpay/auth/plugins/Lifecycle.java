package com.netease.mpay.auth.plugins;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

/* loaded from: classes.dex */
public interface Lifecycle {
    void onActivityResult(int i, int i2, Intent intent);

    void onConfigurationChanged(Configuration configuration);

    void onCreate(Bundle bundle);

    void onDestroy();

    void onNewIntent(Intent intent);

    void onPause();

    void onRequestPermissionsResult(int i, String[] strArr, int[] iArr);

    void onRestart();

    void onResume();

    void onStart();

    void onStop();
}