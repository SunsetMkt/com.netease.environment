package com.netease.vrlib.strategy.projection;

import android.app.Activity;
import android.graphics.RectF;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDDome3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDPanoramaPlugin;

/* loaded from: classes5.dex */
public class DomeProjection extends AbsProjectionStrategy {
    private float mDegree;
    private boolean mIsUpper;
    private RectF mTextureSize;
    MDAbsObject3D object3D;

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
    }

    public DomeProjection(RectF rectF, float f, boolean z) {
        this.mTextureSize = rectF;
        this.mDegree = f;
        this.mIsUpper = z;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
        MDDome3D mDDome3D = new MDDome3D(this.mTextureSize, this.mDegree, this.mIsUpper);
        this.object3D = mDDome3D;
        MDObject3DHelper.loadObj(activity, mDDome3D);
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
    public MDAbsPlugin buildMainPlugin(MDMainPluginBuilder mDMainPluginBuilder) {
        return new MDPanoramaPlugin(mDMainPluginBuilder);
    }
}