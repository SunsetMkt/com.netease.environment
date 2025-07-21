package com.netease.vrlib.plugins;

import android.content.Context;

/* loaded from: classes5.dex */
public abstract class MDAbsLinePipe {
    private boolean mIsInit;

    public abstract void commit(int i, int i2, int i3);

    protected abstract void init(Context context);

    public abstract void takeOver(int i, int i2, int i3);

    public final void setup(Context context) {
        if (this.mIsInit) {
            return;
        }
        init(context);
        this.mIsInit = true;
    }
}