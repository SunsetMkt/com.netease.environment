package com.netease.vrlib.strategy.projection;

import android.app.Activity;
import android.graphics.RectF;
import android.opengl.Matrix;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.objects.MDPlane;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDPanoramaPlugin;

/* loaded from: classes5.dex */
public class PlaneProjection extends AbsProjectionStrategy {
    private static final MDPosition position = MDPosition.newInstance().setZ(-2.0f);
    private MDPlane object3D;
    private PlaneScaleCalculator planeScaleCalculator;

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
    }

    private PlaneProjection(PlaneScaleCalculator planeScaleCalculator) {
        this.planeScaleCalculator = planeScaleCalculator;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
        MDPlane mDPlane = new MDPlane(this.planeScaleCalculator);
        this.object3D = mDPlane;
        MDObject3DHelper.loadObj(activity, mDPlane);
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDAbsObject3D getObject3D() {
        return this.object3D;
    }

    @Override // com.netease.vrlib.strategy.projection.IProjectionMode
    public MDPosition getModelPosition() {
        return position;
    }

    @Override // com.netease.vrlib.strategy.projection.AbsProjectionStrategy
    public MDAbsPlugin buildMainPlugin(MDMainPluginBuilder mDMainPluginBuilder) {
        return new MDPanoramaPlugin(mDMainPluginBuilder);
    }

    @Override // com.netease.vrlib.strategy.projection.AbsProjectionStrategy
    protected MD360DirectorFactory hijackDirectorFactory() {
        return new OrthogonalDirectorFactory();
    }

    public static PlaneProjection create(int i, RectF rectF) {
        return new PlaneProjection(new PlaneScaleCalculator(i, rectF));
    }

    public static class PlaneScaleCalculator {
        private static final float sBaseValue = 1.0f;
        private int mScaleType;
        private RectF mTextureSize;
        private float mViewportRatio;
        private float mViewportWidth = 1.0f;
        private float mViewportHeight = 1.0f;
        private float mTextureWidth = 1.0f;
        private float mTextureHeight = 1.0f;

        public PlaneScaleCalculator(int i, RectF rectF) {
            this.mScaleType = i;
            this.mTextureSize = rectF;
        }

        public float getTextureRatio() {
            return this.mTextureSize.width() / this.mTextureSize.height();
        }

        public void setViewportRatio(float f) {
            this.mViewportRatio = f;
        }

        public void calculate() {
            float f = this.mViewportRatio;
            float textureRatio = getTextureRatio();
            int i = this.mScaleType;
            if (i == 208) {
                if (textureRatio > f) {
                    this.mViewportWidth = f * 1.0f;
                    this.mViewportHeight = 1.0f;
                    this.mTextureWidth = textureRatio * 1.0f;
                    this.mTextureHeight = 1.0f;
                    return;
                }
                this.mViewportWidth = 1.0f;
                this.mViewportHeight = 1.0f / f;
                this.mTextureWidth = 1.0f;
                this.mTextureHeight = 1.0f / textureRatio;
                return;
            }
            if (i == 209) {
                this.mTextureHeight = 1.0f;
                this.mTextureWidth = 1.0f;
                this.mViewportHeight = 1.0f;
                this.mViewportWidth = 1.0f;
                return;
            }
            if (f > textureRatio) {
                this.mViewportWidth = f * 1.0f;
                this.mViewportHeight = 1.0f;
                this.mTextureWidth = textureRatio * 1.0f;
                this.mTextureHeight = 1.0f;
                return;
            }
            this.mViewportWidth = 1.0f;
            this.mViewportHeight = 1.0f / f;
            this.mTextureWidth = 1.0f;
            this.mTextureHeight = 1.0f / textureRatio;
        }

        public float getViewportWidth() {
            return this.mViewportWidth;
        }

        public float getViewportHeight() {
            return this.mViewportHeight;
        }

        public float getTextureWidth() {
            return this.mTextureWidth;
        }

        public float getTextureHeight() {
            return this.mTextureHeight;
        }
    }

    private class OrthogonalDirectorFactory extends MD360DirectorFactory {
        private OrthogonalDirectorFactory() {
        }

        @Override // com.netease.vrlib.MD360DirectorFactory
        public MD360Director createDirector(int i) {
            return new OrthogonalDirector(new MD360Director.Builder());
        }
    }

    private class OrthogonalDirector extends MD360Director {
        @Override // com.netease.vrlib.MD360Director
        public void setDeltaX(float f) {
        }

        @Override // com.netease.vrlib.MD360Director
        public void setDeltaY(float f) {
        }

        @Override // com.netease.vrlib.MD360Director
        public void updateSensorMatrix(float[] fArr) {
        }

        private OrthogonalDirector(MD360Director.Builder builder) {
            super(builder);
        }

        @Override // com.netease.vrlib.MD360Director
        protected void updateProjection() {
            PlaneProjection.this.planeScaleCalculator.setViewportRatio(getRatio());
            PlaneProjection.this.planeScaleCalculator.calculate();
            Matrix.orthoM(getProjectionMatrix(), 0, (-PlaneProjection.this.planeScaleCalculator.getViewportWidth()) / 2.0f, PlaneProjection.this.planeScaleCalculator.getViewportWidth() / 2.0f, (-PlaneProjection.this.planeScaleCalculator.getViewportHeight()) / 2.0f, PlaneProjection.this.planeScaleCalculator.getViewportHeight() / 2.0f, getNear(), 500.0f);
        }
    }
}