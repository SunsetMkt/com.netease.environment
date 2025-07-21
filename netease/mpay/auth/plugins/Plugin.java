package com.netease.mpay.auth.plugins;

/* loaded from: classes.dex */
public interface Plugin {
    Plugin addCallback(PluginCallback pluginCallback);

    Plugin addOnNextListener(OnNextListener onNextListener);

    void addPreTaskResult(PluginResult pluginResult);

    void execute();

    PluginCallback getCallback();

    String getName();

    boolean isEndNode();

    boolean isNeedSuccessBeforeExecute();
}