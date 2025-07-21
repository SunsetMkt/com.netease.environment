package com.netease.mpay.auth.plugins;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

/* loaded from: classes.dex */
public class LifeCyclePlugin extends DefaultPlugin implements Lifecycle {
    int mLifecycleState;

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onNewIntent(Intent intent) {
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onRestart() {
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onCreate(Bundle bundle) {
        this.mLifecycleState = 1;
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onStart() {
        this.mLifecycleState = 2;
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onResume() {
        this.mLifecycleState = 3;
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onPause() {
        this.mLifecycleState = 4;
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onStop() {
        this.mLifecycleState = 5;
    }

    @Override // com.netease.mpay.auth.plugins.Lifecycle
    public void onDestroy() {
        this.mLifecycleState = 6;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public void execute() {
        getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, this.mPreTaskResult != null ? this.mPreTaskResult.data : null, this));
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public String getName() {
        return LifeCyclePlugin.class.getName();
    }

    public int getLifecycleState() {
        return this.mLifecycleState;
    }
}