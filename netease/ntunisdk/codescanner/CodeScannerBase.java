package com.netease.ntunisdk.codescanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.netease.mpay.auth.plugins.PluginExecutor;
import com.netease.mpay.ps.codescanner.auth.DefaultAuthRules;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes.dex */
public class CodeScannerBase extends SdkBase {
    private static final String TAG = "UniSDK netease_codescanner";
    protected volatile boolean mHasPaused;
    protected volatile OnPauseStateChangeListener mOnPauseStateChangeListener;

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    public CodeScannerBase(Context context) {
        super(context);
        this.mHasPaused = true;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnCreate(Bundle bundle) {
        super.sdkOnCreate(bundle);
        PluginExecutor.getInstance().onCreate(bundle);
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void handleOnRestart() {
        super.handleOnRestart();
        PluginExecutor.getInstance().onRestart();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnStart() {
        super.sdkOnStart();
        PluginExecutor.getInstance().onStart();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnResume() {
        UniSdkUtils.d(TAG, "sdkOnResume");
        super.sdkOnResume();
        PluginExecutor.getInstance().onResume();
        this.mHasPaused = false;
        DefaultAuthRules.getInstance().setGameActivityPaused(this.mHasPaused);
        UniSdkUtils.d(TAG, "mHasPaused=" + this.mHasPaused);
        if (this.mOnPauseStateChangeListener != null) {
            this.mOnPauseStateChangeListener.onPauseStateChanged(false);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnNewIntent(Intent intent) {
        super.sdkOnNewIntent(intent);
        UniSdkUtils.d(TAG, "sdkOnNewIntent");
        PluginExecutor.getInstance().onNewIntent(intent);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnPause() {
        super.sdkOnPause();
        UniSdkUtils.d(TAG, "sdkOnPause");
        PluginExecutor.getInstance().onPause();
        this.mHasPaused = true;
        DefaultAuthRules.getInstance().setGameActivityPaused(this.mHasPaused);
        UniSdkUtils.d(TAG, "mHasPaused=" + this.mHasPaused);
        if (this.mOnPauseStateChangeListener != null) {
            this.mOnPauseStateChangeListener.onPauseStateChanged(true);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnStop() {
        super.sdkOnStop();
        UniSdkUtils.d(TAG, "sdkOnStop");
        PluginExecutor.getInstance().onStop();
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void handleOnWindowFocusChanged(boolean z) {
        super.handleOnWindowFocusChanged(z);
        UniSdkUtils.d(TAG, "handleOnWindowFocusChanged:" + z);
        DefaultAuthRules.getInstance().setGameActivityPaused(z);
    }
}