package com.netease.vrlib;

import android.opengl.Matrix;
import com.netease.vrlib.MD360Director;

/* loaded from: classes3.dex */
public abstract class MD360DirectorFactory {
    public abstract MD360Director createDirector(int i);

    public static class DefaultImpl extends MD360DirectorFactory {
        @Override // com.netease.vrlib.MD360DirectorFactory
        public MD360Director createDirector(int i) {
            return MD360Director.builder().build();
        }
    }

    public static class OrthogonalImpl extends MD360DirectorFactory {
        @Override // com.netease.vrlib.MD360DirectorFactory
        public MD360Director createDirector(int i) {
            return new OrthogonalDirector(new MD360Director.Builder());
        }
    }

    private static class OrthogonalDirector extends MD360Director {
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
            Matrix.orthoM(getProjectionMatrix(), 0, -1.0f, 1.0f, -1.0f, 1.0f, getNear(), 500.0f);
        }
    }
}