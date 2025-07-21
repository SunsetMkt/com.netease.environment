package com.netease.vrlib.strategy.projection;

import com.netease.vrlib.common.MDDirection;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDMultiFishEyePlugin;

/* loaded from: classes5.dex */
public class MultiFishEyeProjection extends SphereProjection {
    private MDDirection direction;
    private float radius;

    public MultiFishEyeProjection(float f, MDDirection mDDirection) {
        this.radius = f;
        this.direction = mDDirection;
    }

    @Override // com.netease.vrlib.strategy.projection.SphereProjection, com.netease.vrlib.strategy.projection.AbsProjectionStrategy
    public MDAbsPlugin buildMainPlugin(MDMainPluginBuilder mDMainPluginBuilder) {
        return new MDMultiFishEyePlugin(mDMainPluginBuilder, this.radius, this.direction);
    }
}