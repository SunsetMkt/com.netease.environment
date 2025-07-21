package com.netease.mpay.auth.plugins;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import com.netease.mpay.auth.SDKContext;
import com.netease.ntunisdk.base.SdkMgr;
import java.io.IOException;

/* loaded from: classes.dex */
public class UniSDKPlugin extends LifeCyclePlugin {
    public static final String CURRENT_QUICK_QR_MODE = "CURRENT_QUICK_QR_MODE";
    public static final String CURRENT_SHORTCUT_MAIN_RUNNING = "CURRENT_SHORTCUT_MAIN_RUNNING";
    private static final String TAG = "UniSDK Plugin";

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onCreate(Bundle bundle) throws IOException, SecurityException {
        super.onCreate(bundle);
        init(this.mActivity);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnCreate(bundle);
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onStart() {
        super.onStart();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnStart();
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onResume() {
        super.onResume();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnResume();
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onPause() {
        super.onPause();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnPause();
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onStop() {
        super.onStop();
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnStop();
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onDestroy() {
        super.onDestroy();
        if (SdkMgr.getInst() != null) {
            SdkMgr.getInst().setPropInt("CURRENT_SHORTCUT_MAIN_RUNNING", 0);
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnNewIntent(intent);
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnConfigurationChanged(configuration);
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnRequestPermissionsResult(i, strArr, iArr);
        }
    }

    @Override // com.netease.mpay.auth.plugins.LifeCyclePlugin, com.netease.mpay.auth.plugins.Lifecycle
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (shouldHandleLifecycle()) {
            SdkMgr.getInst().handleOnActivityResult(i, i2, intent);
        }
    }

    private boolean shouldHandleLifecycle() {
        return SDKContext.getInstance().isColdLaunch() && SdkMgr.getInst() != null;
    }

    private void init(Activity activity) throws IOException, SecurityException {
        Log.d(TAG, "Enter init");
        if (SDKContext.getInstance().isColdLaunch()) {
            Log.d(TAG, "getInst:" + SdkMgr.getInst());
            SdkMgr.init(activity);
            Log.d(TAG, "jf_gameId:" + SdkMgr.getInst().getPropStr("JF_GAMEID"));
            SdkMgr.getInst().setPropInt("CURRENT_SHORTCUT_MAIN_RUNNING", 1);
        }
    }

    private boolean isNeteaseChannel() {
        return "netease".equals(SdkMgr.getInst().getChannel());
    }
}