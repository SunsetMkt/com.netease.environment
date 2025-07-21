package com.netease.ntunisdk.modules.api;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import com.netease.ntunisdk.modules.base.InnerModulesManager;

/* loaded from: classes.dex */
public class LifecycleManager {
    public void onCreate(Bundle bundle) {
        InnerModulesManager.getInst().onCreate(bundle);
    }

    public void onNewIntent(Intent intent) {
        InnerModulesManager.getInst().onNewIntent(intent);
    }

    public void onSaveInstanceState(Bundle bundle) {
        InnerModulesManager.getInst().onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        InnerModulesManager.getInst().onRestoreInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle, PersistableBundle persistableBundle) {
        InnerModulesManager.getInst().onRestoreInstanceState(bundle, persistableBundle);
    }

    public void onPause() {
        InnerModulesManager.getInst().onPause();
    }

    public void onStart() {
        InnerModulesManager.getInst().onStart();
    }

    public void onStop() {
        InnerModulesManager.getInst().onStop();
    }

    public void onResume() {
        InnerModulesManager.getInst().onResume();
    }

    public void onRestart() {
        InnerModulesManager.getInst().onRestart();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        InnerModulesManager.getInst().onActivityResult(i, i2, intent);
    }

    public void onWindowFocusChanged(boolean z) {
        InnerModulesManager.getInst().onWindowFocusChanged(z);
    }

    public void onConfigurationChanged(Configuration configuration) {
        InnerModulesManager.getInst().onConfigurationChanged(configuration);
    }

    public void onBackPressed() {
        InnerModulesManager.getInst().onBackPressed();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        InnerModulesManager.getInst().onRequestPermissionsResult(i, strArr, iArr);
    }

    public void onUserLeaveHint() {
        InnerModulesManager.getInst().onUserLeaveHint();
    }

    public void onLowMemory() {
        InnerModulesManager.getInst().onLowMemory();
    }

    public void onDestroy() {
        InnerModulesManager.getInst().onDestroy();
    }
}