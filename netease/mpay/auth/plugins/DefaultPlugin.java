package com.netease.mpay.auth.plugins;

import android.app.Activity;

/* loaded from: classes.dex */
public abstract class DefaultPlugin implements Plugin {
    protected Activity mActivity;
    protected OnNextListener mOnNextListener;
    protected PluginCallback mPluginCallback;
    protected PluginResult mPreTaskResult;

    @Override // com.netease.mpay.auth.plugins.Plugin
    public boolean isEndNode() {
        return false;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public boolean isNeedSuccessBeforeExecute() {
        return false;
    }

    public Plugin setActivity(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public Plugin addCallback(PluginCallback pluginCallback) {
        this.mPluginCallback = pluginCallback;
        return this;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public PluginCallback getCallback() {
        return this.mPluginCallback;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public void addPreTaskResult(PluginResult pluginResult) {
        this.mPreTaskResult = pluginResult;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public Plugin addOnNextListener(OnNextListener onNextListener) {
        this.mOnNextListener = onNextListener;
        return this;
    }

    public void postOnResume(Runnable runnable) {
        PluginExecutor.getInstance().postRunOnResume(runnable);
    }
}