package com.netease.ntunisdk.shortcuts;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes.dex */
class MockMainActivity extends Activity {
    protected static final String TAG = "QR quickqr_main";
    protected boolean mIsColdLaunch;

    MockMainActivity() {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate " + getTaskId() + " " + hashCode());
        this.mIsColdLaunch = SdkMgr.getInst() == null;
        super.onCreate(bundle);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnCreate(bundle);
        }
    }

    @Override // android.app.Activity
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnRestart();
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        Log.d(TAG, "onStart " + getTaskId() + " " + hashCode());
        super.onStart();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnStart();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        Log.d(TAG, "onResume " + getTaskId() + " " + hashCode());
        super.onResume();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnResume();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        Log.d(TAG, "onPause " + getTaskId() + " " + hashCode());
        super.onPause();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnPause();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        Log.d(TAG, "onStop " + getTaskId() + " " + hashCode());
        super.onStop();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnStop();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        Log.d(TAG, "onDestroy " + getTaskId() + " " + hashCode());
        super.onDestroy();
        if (shouldHandleLifecycle()) {
            SdkMgr.destroyInst();
            System.exit(0);
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnNewIntent(intent);
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(configuration);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnConfigurationChanged(configuration);
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnRequestPermissionsResult(i, strArr, iArr);
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        UniSdkUtils.d(TAG, "onActivityResult...requestCode=" + i + ",resultCode=" + i2);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    private boolean shouldHandleLifecycle() {
        return this.mIsColdLaunch && SdkMgr.getInst() != null;
    }
}