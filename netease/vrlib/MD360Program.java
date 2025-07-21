package com.netease.vrlib;

import android.content.Context;
import android.opengl.GLES20;
import com.netease.vrlib.common.GLUtil;
import tv.danmaku.ijk.media.lib.R;

/* loaded from: classes3.dex */
public class MD360Program {
    private int mContentType;
    private int mMVMatrixHandle;
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mProgramHandle;
    private int mTextureCoordinateHandle;
    private int mTextureUniformHandle;

    public MD360Program(int i) {
        this.mContentType = i;
    }

    public void build(Context context) {
        int iCreateAndLinkProgram = GLUtil.createAndLinkProgram(GLUtil.compileShader(35633, getVertexShader(context)), GLUtil.compileShader(35632, getFragmentShader(context)), new String[]{"a_Position", "a_TexCoordinate"});
        this.mProgramHandle = iCreateAndLinkProgram;
        this.mMVPMatrixHandle = GLES20.glGetUniformLocation(iCreateAndLinkProgram, "u_MVPMatrix");
        this.mMVMatrixHandle = GLES20.glGetUniformLocation(this.mProgramHandle, "u_MVMatrix");
        this.mTextureUniformHandle = GLES20.glGetUniformLocation(this.mProgramHandle, "u_Texture");
        this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgramHandle, "a_Position");
        this.mTextureCoordinateHandle = GLES20.glGetAttribLocation(this.mProgramHandle, "a_TexCoordinate");
    }

    protected String getVertexShader(Context context) {
        return GLUtil.readTextFileFromRaw(context, R.raw.per_pixel_vertex_shader);
    }

    protected String getFragmentShader(Context context) {
        return FragmentShaderFactory.fs(context, this.mContentType);
    }

    public void use() {
        GLES20.glUseProgram(this.mProgramHandle);
    }

    public int getMVPMatrixHandle() {
        return this.mMVPMatrixHandle;
    }

    public int getMVMatrixHandle() {
        return this.mMVMatrixHandle;
    }

    public int getTextureUniformHandle() {
        return this.mTextureUniformHandle;
    }

    public int getPositionHandle() {
        return this.mPositionHandle;
    }

    public int getTextureCoordinateHandle() {
        return this.mTextureCoordinateHandle;
    }

    private static class FragmentShaderFactory {
        private FragmentShaderFactory() {
        }

        static String fs(Context context, int i) {
            int i2;
            if (i == 1) {
                i2 = R.raw.per_pixel_fragment_shader_bitmap;
            } else {
                i2 = R.raw.per_pixel_fragment_shader;
            }
            return GLUtil.readTextFileFromRaw(context, i2);
        }
    }
}