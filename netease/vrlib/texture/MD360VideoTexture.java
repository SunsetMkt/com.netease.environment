package com.netease.vrlib.texture;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.Surface;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.common.GLUtil;

/* loaded from: classes5.dex */
public class MD360VideoTexture extends MD360Texture {
    private MDVRLibrary.IOnSurfaceReadyCallback mOnSurfaceReadyListener;
    public Surface mSurface;
    private SurfaceTexture mSurfaceTexture;

    @Override // com.netease.vrlib.texture.MD360Texture
    public boolean isReady() {
        return true;
    }

    public MD360VideoTexture(MDVRLibrary.IOnSurfaceReadyCallback iOnSurfaceReadyCallback) {
        this.mOnSurfaceReadyListener = iOnSurfaceReadyCallback;
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void release() {
        this.mOnSurfaceReadyListener = null;
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void create() {
        super.create();
        int currentTextureId = getCurrentTextureId();
        if (isEmpty(currentTextureId)) {
            return;
        }
        onCreateSurface(currentTextureId);
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void destroy() {
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
        this.mSurfaceTexture = null;
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
        }
        this.mSurface = null;
    }

    private void onCreateSurface(int i) {
        if (this.mSurfaceTexture == null) {
            this.mSurfaceTexture = new SurfaceTexture(i);
            Surface surface = new Surface(this.mSurfaceTexture);
            this.mSurface = surface;
            MDVRLibrary.IOnSurfaceReadyCallback iOnSurfaceReadyCallback = this.mOnSurfaceReadyListener;
            if (iOnSurfaceReadyCallback != null) {
                iOnSurfaceReadyCallback.onSurfaceReady(surface);
            }
        }
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    protected int createTextureId() {
        int[] iArr = new int[1];
        GLES20.glActiveTexture(33984);
        GLES20.glGenTextures(1, iArr, 0);
        GLUtil.glCheck("Texture generate");
        GLES20.glBindTexture(36197, iArr[0]);
        GLUtil.glCheck("Texture bind");
        GLES20.glTexParameterf(36197, 10241, 9729.0f);
        GLES20.glTexParameterf(36197, 10240, 9729.0f);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        return iArr[0];
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public boolean texture(MD360Program mD360Program) {
        SurfaceTexture surfaceTexture;
        if (isEmpty(getCurrentTextureId()) || (surfaceTexture = this.mSurfaceTexture) == null) {
            return false;
        }
        surfaceTexture.updateTexImage();
        return true;
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void notifyChanged() {
        MDVRLibrary.IOnSurfaceReadyCallback iOnSurfaceReadyCallback;
        Surface surface = this.mSurface;
        if (surface == null || (iOnSurfaceReadyCallback = this.mOnSurfaceReadyListener) == null) {
            return;
        }
        iOnSurfaceReadyCallback.onSurfaceReady(surface);
    }
}