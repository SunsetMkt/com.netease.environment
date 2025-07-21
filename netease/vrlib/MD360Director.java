package com.netease.vrlib;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.netease.vrlib.model.MDPosition;

/* loaded from: classes3.dex */
public class MD360Director {
    private static final String TAG = "MD360Director";
    private static final float sNear = 0.7f;
    private final MDPosition mCameraRotation;
    private float mDeltaX;
    private float mDeltaY;
    private float mEyeX;
    private float mEyeY;
    private float mEyeZ;
    private float mLookX;
    private float mLookY;
    private float mNearScale;
    private float mRatio;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int mViewportWidth = 2;
    private int mViewportHeight = 1;
    private float[] mCurrentRotation = new float[16];
    private float[] mCurrentRotationPost = new float[16];
    private float[] mSensorMatrix = new float[16];
    private float[] mTempMatrix = new float[16];
    private float[] mTempInvertMatrix = new float[16];
    private boolean mViewMatrixInvalidate = true;

    protected MD360Director(Builder builder) {
        this.mEyeX = 0.0f;
        this.mEyeY = 0.0f;
        this.mEyeZ = 0.0f;
        this.mLookX = 0.0f;
        this.mLookY = 0.0f;
        this.mRatio = 0.0f;
        this.mNearScale = 0.0f;
        this.mRatio = builder.mRatio;
        this.mNearScale = builder.mNearScale;
        this.mEyeX = builder.mEyeX;
        this.mEyeY = builder.mEyeY;
        this.mEyeZ = builder.mEyeZ;
        this.mLookX = builder.mLookX;
        this.mLookY = builder.mLookY;
        this.mCameraRotation = builder.mRotation;
        initModel();
    }

    public float getDeltaY() {
        return this.mDeltaY;
    }

    public void setDeltaY(float f) {
        this.mDeltaY = f;
        this.mViewMatrixInvalidate = true;
    }

    public float getDeltaX() {
        return this.mDeltaX;
    }

    public void setDeltaX(float f) {
        this.mDeltaX = f;
        this.mViewMatrixInvalidate = true;
    }

    public float[] getTempInvertMatrix() {
        return this.mTempInvertMatrix;
    }

    private void initModel() {
        Matrix.setIdentityM(this.mSensorMatrix, 0);
    }

    public void shot(MD360Program mD360Program) {
        shot(mD360Program, MDPosition.sOriginalPosition);
    }

    public void shot(MD360Program mD360Program, MDPosition mDPosition) {
        if (this.mViewMatrixInvalidate) {
            updateViewMatrix();
            this.mViewMatrixInvalidate = false;
        }
        Matrix.multiplyMM(this.mMVMatrix, 0, this.mViewMatrix, 0, mDPosition.getMatrix(), 0);
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mProjectionMatrix, 0, this.mMVMatrix, 0);
        GLES20.glUniformMatrix4fv(mD360Program.getMVMatrixHandle(), 1, false, this.mMVMatrix, 0);
        GLES20.glUniformMatrix4fv(mD360Program.getMVPMatrixHandle(), 1, false, this.mMVPMatrix, 0);
    }

    public void updateViewport(int i, int i2) {
        this.mViewportWidth = i;
        this.mViewportHeight = i2;
        this.mRatio = (i * 1.0f) / i2;
        updateProjection();
    }

    public void updateProjectionNearScale(float f) {
        this.mNearScale = f;
        updateProjection();
    }

    protected void updateProjection() {
        float f = this.mRatio;
        Matrix.frustumM(getProjectionMatrix(), 0, (-f) / 2.0f, f / 2.0f, -0.5f, 0.5f, getNear(), 500.0f);
    }

    protected float getNear() {
        return this.mNearScale * sNear;
    }

    protected float getRatio() {
        return this.mRatio;
    }

    public float[] getProjectionMatrix() {
        return this.mProjectionMatrix;
    }

    public int getViewportWidth() {
        return this.mViewportWidth;
    }

    public int getViewportHeight() {
        return this.mViewportHeight;
    }

    public float[] getViewMatrix() {
        return this.mViewMatrix;
    }

    private void updateViewMatrix() {
        float f = this.mEyeX;
        float f2 = this.mEyeY;
        float f3 = this.mEyeZ;
        float f4 = this.mLookX;
        float f5 = this.mLookY;
        Matrix.setIdentityM(this.mViewMatrix, 0);
        Matrix.setLookAtM(this.mViewMatrix, 0, f, f2, f3, f4, f5, -1.0f, 0.0f, 1.0f, 0.0f);
        Matrix.setIdentityM(this.mCurrentRotation, 0);
        if (this.mDeltaY >= 90.0f) {
            this.mDeltaY = 90.0f;
        }
        if (this.mDeltaY < -90.0f) {
            this.mDeltaY = -90.0f;
        }
        Matrix.rotateM(this.mCurrentRotation, 0, -this.mDeltaY, 1.0f, 0.0f, 0.0f);
        Matrix.setIdentityM(this.mCurrentRotationPost, 0);
        Matrix.rotateM(this.mCurrentRotationPost, 0, -this.mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.setIdentityM(this.mTempMatrix, 0);
        Matrix.multiplyMM(this.mTempMatrix, 0, this.mCurrentRotationPost, 0, this.mCameraRotation.getMatrix(), 0);
        Matrix.multiplyMM(this.mCurrentRotationPost, 0, this.mSensorMatrix, 0, this.mTempMatrix, 0);
        Matrix.multiplyMM(this.mTempMatrix, 0, this.mCurrentRotation, 0, this.mCurrentRotationPost, 0);
        System.arraycopy(this.mTempMatrix, 0, this.mCurrentRotation, 0, 16);
        Matrix.multiplyMM(this.mTempMatrix, 0, this.mViewMatrix, 0, this.mCurrentRotation, 0);
        System.arraycopy(this.mTempMatrix, 0, this.mViewMatrix, 0, 16);
    }

    public void updateSensorMatrix(float[] fArr) {
        System.arraycopy(fArr, 0, this.mSensorMatrix, 0, 16);
        this.mViewMatrixInvalidate = true;
    }

    public void reset() {
        this.mDeltaY = 0.0f;
        this.mDeltaX = 0.0f;
        Matrix.setIdentityM(this.mSensorMatrix, 0);
        this.mViewMatrixInvalidate = true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private float mEyeX = 0.0f;
        private float mEyeY = 0.0f;
        private float mEyeZ = 0.0f;
        private float mRatio = 1.5f;
        private float mNearScale = 1.0f;
        private float mLookX = 0.0f;
        private float mLookY = 0.0f;
        private MDPosition mRotation = MDPosition.newInstance();

        public Builder setLookX(float f) {
            this.mLookX = f;
            return this;
        }

        public Builder setLookY(float f) {
            this.mLookY = f;
            return this;
        }

        public Builder setEyeX(float f) {
            this.mEyeX = f;
            return this;
        }

        public Builder setEyeY(float f) {
            this.mEyeY = f;
            return this;
        }

        public Builder setEyeZ(float f) {
            this.mEyeZ = f;
            return this;
        }

        public Builder setRoll(float f) {
            this.mRotation.setRoll(f);
            return this;
        }

        public Builder setPitch(float f) {
            this.mRotation.setPitch(f);
            return this;
        }

        public Builder setYaw(float f) {
            this.mRotation.setYaw(f);
            return this;
        }

        public Builder setRatio(float f) {
            this.mRatio = f;
            return this;
        }

        public Builder setNearScale(float f) {
            this.mNearScale = f;
            return this;
        }

        public MD360Director build() {
            return new MD360Director(this);
        }
    }
}