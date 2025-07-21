package com.netease.vrlib.texture;

import com.netease.vrlib.MD360Program;

/* loaded from: classes5.dex */
public abstract class MD360Texture {
    private static final String TAG = "MD360Texture";
    private static final int TEXTURE_EMPTY = 0;
    private int mTextureId = 0;

    protected abstract int createTextureId();

    public abstract void destroy();

    protected final boolean isEmpty(int i) {
        return i == 0;
    }

    public abstract boolean isReady();

    public abstract void notifyChanged();

    public abstract void release();

    public abstract boolean texture(MD360Program mD360Program);

    public void create() {
        int iCreateTextureId = createTextureId();
        if (iCreateTextureId != 0) {
            this.mTextureId = iCreateTextureId;
        }
    }

    public int getCurrentTextureId() {
        return this.mTextureId;
    }
}