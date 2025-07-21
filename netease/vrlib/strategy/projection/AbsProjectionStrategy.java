package com.netease.vrlib.strategy.projection;

import android.content.Context;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.strategy.IModeStrategy;

/* loaded from: classes5.dex */
public abstract class AbsProjectionStrategy implements IModeStrategy, IProjectionMode {
    abstract MDAbsPlugin buildMainPlugin(MDMainPluginBuilder mDMainPluginBuilder);

    protected MD360DirectorFactory hijackDirectorFactory() {
        return null;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void onPause(Context context) {
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void onResume(Context context) {
    }
}