package com.netease.vrlib.model;

/* loaded from: classes5.dex */
public class BarrelDistortionConfig {
    private double paramA = -0.068d;
    private double paramB = 0.32d;
    private double paramC = -0.2d;
    private float scale = 0.95f;
    private boolean defaultEnabled = false;

    public BarrelDistortionConfig setParamA(double d) {
        this.paramA = d;
        return this;
    }

    public BarrelDistortionConfig setParamB(double d) {
        this.paramB = d;
        return this;
    }

    public BarrelDistortionConfig setParamC(double d) {
        this.paramC = d;
        return this;
    }

    public BarrelDistortionConfig setScale(float f) {
        this.scale = f;
        return this;
    }

    public BarrelDistortionConfig setDefaultEnabled(boolean z) {
        this.defaultEnabled = z;
        return this;
    }

    public double getParamA() {
        return this.paramA;
    }

    public double getParamB() {
        return this.paramB;
    }

    public double getParamC() {
        return this.paramC;
    }

    public float getScale() {
        return this.scale;
    }

    public boolean isDefaultEnabled() {
        return this.defaultEnabled;
    }
}