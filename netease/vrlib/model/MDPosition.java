package com.netease.vrlib.model;

import android.opengl.Matrix;

/* loaded from: classes5.dex */
public class MDPosition {
    public static final MDPosition sOriginalPosition = newInstance();
    private float[] mCurrentRotation = new float[16];
    private float mZ = 0.0f;
    private float mY = 0.0f;
    private float mX = 0.0f;
    private float mAngleZ = 0.0f;
    private float mAngleY = 0.0f;
    private float mAngleX = 0.0f;
    private float mRoll = 0.0f;
    private float mYaw = 0.0f;
    private float mPitch = 0.0f;

    private MDPosition() {
    }

    public float getPitch() {
        return this.mPitch;
    }

    public MDPosition setPitch(float f) {
        this.mPitch = f;
        return this;
    }

    public float getYaw() {
        return this.mYaw;
    }

    public MDPosition setYaw(float f) {
        this.mYaw = f;
        return this;
    }

    public float getRoll() {
        return this.mRoll;
    }

    public MDPosition setRoll(float f) {
        this.mRoll = f;
        return this;
    }

    public float getX() {
        return this.mX;
    }

    public MDPosition setX(float f) {
        this.mX = f;
        return this;
    }

    public float getY() {
        return this.mY;
    }

    public MDPosition setY(float f) {
        this.mY = f;
        return this;
    }

    public float getZ() {
        return this.mZ;
    }

    public MDPosition setZ(float f) {
        this.mZ = f;
        return this;
    }

    public float getAngleX() {
        return this.mAngleX;
    }

    public MDPosition setAngleX(float f) {
        this.mAngleX = f;
        return this;
    }

    public float getAngleY() {
        return this.mAngleY;
    }

    public MDPosition setAngleY(float f) {
        this.mAngleY = f;
        return this;
    }

    public float getAngleZ() {
        return this.mAngleZ;
    }

    public MDPosition setAngleZ(float f) {
        this.mAngleZ = f;
        return this;
    }

    public static MDPosition newInstance() {
        return new MDPosition();
    }

    public String toString() {
        return "MDPosition{mX=" + this.mX + ", mY=" + this.mY + ", mZ=" + this.mZ + ", mAngleX=" + this.mAngleX + ", mAngleY=" + this.mAngleY + ", mAngleZ=" + this.mAngleZ + ", mPitch=" + this.mPitch + ", mYaw=" + this.mYaw + ", mRoll=" + this.mRoll + '}';
    }

    private void update() {
        Matrix.setIdentityM(this.mCurrentRotation, 0);
        Matrix.rotateM(this.mCurrentRotation, 0, getAngleY(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(this.mCurrentRotation, 0, getAngleX(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(this.mCurrentRotation, 0, getAngleZ(), 0.0f, 0.0f, 1.0f);
        Matrix.translateM(this.mCurrentRotation, 0, getX(), getY(), getZ());
        Matrix.rotateM(this.mCurrentRotation, 0, getYaw(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(this.mCurrentRotation, 0, getPitch(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(this.mCurrentRotation, 0, getRoll(), 0.0f, 0.0f, 1.0f);
    }

    public float[] getMatrix() {
        update();
        return this.mCurrentRotation;
    }
}