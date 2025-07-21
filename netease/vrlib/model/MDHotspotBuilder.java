package com.netease.vrlib.model;

import android.util.SparseArray;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.texture.MD360BitmapTexture;
import com.netease.vrlib.texture.MD360Texture;

/* loaded from: classes5.dex */
public class MDHotspotBuilder {
    public int[] checkedStatusList;
    public MDVRLibrary.ITouchPickListener clickListener;
    public MDPosition position;
    public int[] statusList;
    public String title;
    public float width = 2.0f;
    public float height = 2.0f;
    public SparseArray<MD360Texture> textures = new SparseArray<>(6);

    public static MDHotspotBuilder create() {
        return new MDHotspotBuilder();
    }

    private MDHotspotBuilder status(int i, int i2, int i3) {
        this.statusList = new int[]{i, i2, i3};
        return this;
    }

    public MDHotspotBuilder status(int i, int i2) {
        return status(i, i2, i2);
    }

    public MDHotspotBuilder status(int i) {
        return status(i, i);
    }

    private MDHotspotBuilder checkedStatus(int i, int i2, int i3) {
        this.checkedStatusList = new int[]{i, i2, i3};
        return this;
    }

    public MDHotspotBuilder checkedStatus(int i, int i2) {
        return checkedStatus(i, i2, i2);
    }

    public MDHotspotBuilder checkedStatus(int i) {
        return checkedStatus(i, i);
    }

    public MDHotspotBuilder title(String str) {
        this.title = str;
        return this;
    }

    public MDHotspotBuilder size(float f, float f2) {
        this.width = f;
        this.height = f2;
        return this;
    }

    public MDHotspotBuilder provider(MDVRLibrary.IBitmapProvider iBitmapProvider) {
        provider(0, iBitmapProvider);
        return this;
    }

    public MDHotspotBuilder provider(int i, MDVRLibrary.IBitmapProvider iBitmapProvider) {
        this.textures.append(i, new MD360BitmapTexture(iBitmapProvider));
        return this;
    }

    public MDHotspotBuilder position(MDPosition mDPosition) {
        this.position = mDPosition;
        return this;
    }

    public MDHotspotBuilder listenClick(MDVRLibrary.ITouchPickListener iTouchPickListener) {
        this.clickListener = iTouchPickListener;
        return this;
    }
}