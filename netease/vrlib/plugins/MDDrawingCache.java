package com.netease.vrlib.plugins;

import android.graphics.Rect;
import android.opengl.GLES20;
import com.netease.vrlib.common.GLUtil;

/* loaded from: classes5.dex */
public class MDDrawingCache {
    private int mFrameBufferId;
    private int mRenderBufferId;
    private int mTextureIdOutput;
    private Rect mViewport = new Rect();
    private int[] originalFramebufferId = new int[1];

    private void setup(int i, int i2) {
        if (this.mViewport.width() == i && this.mViewport.height() == i2) {
            return;
        }
        createFrameBuffer(i, i2);
        this.mViewport.set(0, 0, i, i2);
    }

    private void createFrameBuffer(int i, int i2) {
        int i3 = this.mTextureIdOutput;
        if (i3 != 0) {
            GLES20.glDeleteTextures(1, new int[]{i3}, 0);
        }
        int i4 = this.mRenderBufferId;
        if (i4 != 0) {
            GLES20.glDeleteRenderbuffers(1, new int[]{i4}, 0);
        }
        int i5 = this.mFrameBufferId;
        if (i5 != 0) {
            GLES20.glDeleteFramebuffers(1, new int[]{i5}, 0);
        }
        GLES20.glGetIntegerv(36006, this.originalFramebufferId, 0);
        int[] iArr = new int[1];
        GLES20.glGenFramebuffers(1, iArr, 0);
        GLES20.glBindFramebuffer(36160, iArr[0]);
        this.mFrameBufferId = iArr[0];
        GLUtil.glCheck("Multi Fish Eye frame buffer");
        int[] iArr2 = {0};
        GLES20.glGenRenderbuffers(1, iArr2, 0);
        GLES20.glBindRenderbuffer(36161, iArr2[0]);
        GLES20.glRenderbufferStorage(36161, 33189, i, i2);
        this.mRenderBufferId = iArr2[0];
        GLUtil.glCheck("Multi Fish Eye renderer buffer");
        int[] iArr3 = {0};
        GLES20.glGenTextures(1, iArr3, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, iArr3[0]);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
        this.mTextureIdOutput = iArr3[0];
        GLUtil.glCheck("Multi Fish Eye texture");
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mTextureIdOutput, 0);
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, iArr2[0]);
        GLUtil.glCheck("Multi Fish Eye attach");
        int iGlCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
        if (iGlCheckFramebufferStatus != 36053) {
            String strValueOf = String.valueOf(Integer.toHexString(iGlCheckFramebufferStatus));
            throw new RuntimeException(strValueOf.length() != 0 ? "Framebuffer is not complete: ".concat(strValueOf) : "Framebuffer is not complete: ");
        }
        GLES20.glBindFramebuffer(36160, this.originalFramebufferId[0]);
        GLUtil.glCheck("Multi Fish Eye attach");
    }

    public void bind(int i, int i2) {
        setup(i, i2);
        GLES20.glGetIntegerv(36006, this.originalFramebufferId, 0);
        GLES20.glBindFramebuffer(36160, this.mFrameBufferId);
    }

    public int getTextureOutput() {
        return this.mTextureIdOutput;
    }

    public void unbind() {
        GLES20.glBindFramebuffer(36160, this.originalFramebufferId[0]);
    }
}