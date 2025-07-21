package com.netease.vrlib.model;

import android.opengl.Matrix;

/* loaded from: classes5.dex */
public class MDVector3D {
    private float[] values;

    public MDVector3D() {
        float[] fArr = new float[4];
        this.values = fArr;
        fArr[3] = 1.0f;
    }

    public MDVector3D setX(float f) {
        this.values[0] = f;
        return this;
    }

    public MDVector3D setY(float f) {
        this.values[1] = f;
        return this;
    }

    public MDVector3D setZ(float f) {
        this.values[2] = f;
        return this;
    }

    public float getX() {
        return this.values[0];
    }

    public float getY() {
        return this.values[1];
    }

    public float getZ() {
        return this.values[2];
    }

    public void multiplyMV(float[] fArr) {
        float[] fArr2 = this.values;
        Matrix.multiplyMV(fArr2, 0, fArr, 0, fArr2, 0);
    }

    public String toString() {
        return "MDVector3D{x=" + getX() + ", y=" + getY() + ", z=" + getZ() + '}';
    }
}