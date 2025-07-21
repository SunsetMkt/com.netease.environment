package com.netease.vrlib.model;

/* loaded from: classes5.dex */
public class MDRay {
    private MDVector3D mDir;
    private MDVector3D mOrig;

    public MDRay(MDVector3D mDVector3D, MDVector3D mDVector3D2) {
        this.mOrig = mDVector3D;
        this.mDir = mDVector3D2;
    }

    public MDVector3D getOrig() {
        return this.mOrig;
    }

    public void setOrig(MDVector3D mDVector3D) {
        this.mOrig = mDVector3D;
    }

    public MDVector3D getDir() {
        return this.mDir;
    }

    public void setDir(MDVector3D mDVector3D) {
        this.mDir = mDVector3D;
    }

    public String toString() {
        return "MDRay{, mDir=" + this.mDir + ", mOrig=" + this.mOrig + '}';
    }
}