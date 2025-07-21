package com.netease.vrlib.model;

/* loaded from: classes5.dex */
public class MDPinchConfig {
    private float max = 5.0f;
    private float min = 1.0f;
    private float defaultValue = 1.0f;
    private float mSensitivity = 3.0f;

    public MDPinchConfig setMax(float f) {
        this.max = f;
        return this;
    }

    public MDPinchConfig setMin(float f) {
        this.min = f;
        return this;
    }

    public MDPinchConfig setDefaultValue(float f) {
        this.defaultValue = f;
        return this;
    }

    public MDPinchConfig setSensitivity(float f) {
        this.mSensitivity = f;
        return this;
    }

    public float getSensitivity() {
        return this.mSensitivity;
    }

    public float getMax() {
        return this.max;
    }

    public float getMin() {
        return this.min;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }
}