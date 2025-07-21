package com.netease.vrlib.strategy.projection;

import android.app.Activity;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.common.MDDirection;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.objects.MDStereoSphere3D;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDPanoramaPlugin;

/* loaded from: classes5.dex */
public class StereoSphereProjection extends AbsProjectionStrategy {
    private MDDirection direction;
    private MDAbsObject3D object3D;

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
    }

    private static class FixedDirectorFactory extends MD360DirectorFactory {
        private FixedDirectorFactory() {
        }

        /* synthetic */ FixedDirectorFactory(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.netease.vrlib.MD360DirectorFactory
        public MD360Director createDirector(int i) {
            return MD360Director.builder().build();
        }
    }

    public StereoSphereProjection(MDDirection mDDirection) {
        this.direction = mDDirection;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
        MDStereoSphere3D mDStereoSphere3D = new MDStereoSphere3D(this.direction);
        this.object3D = mDStereoSphere3D;
        MDObject3DHelper.loadObj(activity, mDStereoSphere3D);
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDAbsObject3D getObject3D() {
        return this.object3D;
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDPosition getModelPosition() {
        return MDPosition.sOriginalPosition;
    }

    @Override // com.netease.vrlib.strategy.projection.AbsProjectionStrategy
    protected MD360DirectorFactory hijackDirectorFactory() {
        return new FixedDirectorFactory();
    }

    @Override // com.netease.vrlib.strategy.projection.AbsProjectionStrategy
    public MDAbsPlugin buildMainPlugin(MDMainPluginBuilder mDMainPluginBuilder) {
        return new MDPanoramaPlugin(mDMainPluginBuilder);
    }
}