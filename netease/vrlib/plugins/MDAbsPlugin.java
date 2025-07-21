package com.netease.vrlib.plugins;

import android.content.Context;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.model.MDPosition;

/* loaded from: classes5.dex */
public abstract class MDAbsPlugin {
    private boolean mIsInit;
    private MDPosition mPosition = MDPosition.sOriginalPosition;

    public abstract void beforeRenderer(int i, int i2);

    public abstract void destroy();

    protected abstract void init(Context context);

    protected abstract boolean removable();

    public abstract void renderer(int i, int i2, int i3, MD360Director mD360Director);

    public final void setup(Context context) {
        if (this.mIsInit) {
            return;
        }
        init(context);
        this.mIsInit = true;
    }

    protected MDPosition getModelPosition() {
        return this.mPosition;
    }

    public void setModelPosition(MDPosition mDPosition) {
        this.mPosition = mDPosition;
    }
}