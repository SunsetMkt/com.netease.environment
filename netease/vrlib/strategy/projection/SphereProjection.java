package com.netease.vrlib.strategy.projection;

import android.app.Activity;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.objects.MDSphere3D;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDPanoramaPlugin;

/* loaded from: classes5.dex */
public class SphereProjection extends AbsProjectionStrategy {
    private MDAbsObject3D object3D;

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDAbsObject3D getObject3D() {
        return this.object3D;
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDPosition getModelPosition() {
        return MDPosition.sOriginalPosition;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
        MDSphere3D mDSphere3D = new MDSphere3D();
        this.object3D = mDSphere3D;
        MDObject3DHelper.loadObj(activity, mDSphere3D);
    }

    @Override // com.netease.vrlib.strategy.projection.AbsProjectionStrategy
    public MDAbsPlugin buildMainPlugin(MDMainPluginBuilder mDMainPluginBuilder) {
        return new MDPanoramaPlugin(mDMainPluginBuilder);
    }
}