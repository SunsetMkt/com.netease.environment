package com.netease.vrlib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;
import com.google.android.apps.muzei.render.GLTextureView;

/* loaded from: classes3.dex */
public abstract class MDGLScreenWrapper {
    public abstract View getView();

    public abstract void init(Context context);

    public abstract void onPause();

    public abstract void onResume();

    public abstract void setRenderer(GLSurfaceView.Renderer renderer);

    public static MDGLScreenWrapper wrap(GLSurfaceView gLSurfaceView) {
        return new MDGLSurfaceViewImpl(gLSurfaceView);
    }

    public static MDGLScreenWrapper wrap(GLTextureView gLTextureView) {
        return new MDGLTextureViewImpl(gLTextureView);
    }

    private static class MDGLTextureViewImpl extends MDGLScreenWrapper {
        GLTextureView glTextureView;

        public MDGLTextureViewImpl(GLTextureView gLTextureView) {
            this.glTextureView = gLTextureView;
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public View getView() {
            return this.glTextureView;
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void setRenderer(GLSurfaceView.Renderer renderer) {
            this.glTextureView.setRenderer(renderer);
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void init(Context context) {
            this.glTextureView.setEGLContextClientVersion(2);
            this.glTextureView.setPreserveEGLContextOnPause(true);
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void onResume() {
            this.glTextureView.onResume();
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void onPause() {
            this.glTextureView.onPause();
        }
    }

    private static class MDGLSurfaceViewImpl extends MDGLScreenWrapper {
        GLSurfaceView glSurfaceView;

        private MDGLSurfaceViewImpl(GLSurfaceView gLSurfaceView) {
            this.glSurfaceView = gLSurfaceView;
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public View getView() {
            return this.glSurfaceView;
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void setRenderer(GLSurfaceView.Renderer renderer) {
            this.glSurfaceView.setRenderer(renderer);
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void init(Context context) {
            this.glSurfaceView.setEGLContextClientVersion(2);
            this.glSurfaceView.setPreserveEGLContextOnPause(true);
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void onResume() {
            this.glSurfaceView.onResume();
        }

        @Override // com.netease.vrlib.MDGLScreenWrapper
        public void onPause() {
            this.glSurfaceView.onPause();
        }
    }
}