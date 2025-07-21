package com.netease.vrlib.strategy;

import android.app.Activity;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.common.MDMainHandler;
import com.netease.vrlib.strategy.IModeStrategy;
import java.util.Arrays;

/* loaded from: classes5.dex */
public abstract class ModeManager<T extends IModeStrategy> {
    private MDVRLibrary.INotSupportCallback mCallback;
    private MDGLHandler mGLHandler;
    private int mMode;
    private T mStrategy;

    protected abstract T createStrategy(int i);

    protected abstract int[] getModes();

    public ModeManager(int i, MDGLHandler mDGLHandler) {
        this.mGLHandler = mDGLHandler;
        this.mMode = i;
    }

    public void prepare(Activity activity, MDVRLibrary.INotSupportCallback iNotSupportCallback) {
        this.mCallback = iNotSupportCallback;
        initMode(activity, this.mMode);
    }

    private void initMode(Activity activity, final int i) {
        if (this.mStrategy != null) {
            off(activity);
        }
        T t = (T) createStrategy(i);
        this.mStrategy = t;
        if (!t.isSupport(activity)) {
            MDMainHandler.sharedHandler().post(new Runnable() { // from class: com.netease.vrlib.strategy.ModeManager.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ModeManager.this.mCallback != null) {
                        ModeManager.this.mCallback.onNotSupport(i);
                    }
                }
            });
        } else {
            on(activity);
        }
    }

    public void switchMode(Activity activity) {
        int[] modes = getModes();
        switchMode(activity, modes[(Arrays.binarySearch(modes, getMode()) + 1) % modes.length]);
    }

    public void switchMode(Activity activity, int i) {
        if (i == getMode()) {
            return;
        }
        this.mMode = i;
        initMode(activity, i);
    }

    public void on(final Activity activity) {
        final T t = this.mStrategy;
        if (t.isSupport(activity)) {
            getGLHandler().post(new Runnable() { // from class: com.netease.vrlib.strategy.ModeManager.2
                @Override // java.lang.Runnable
                public void run() {
                    t.on(activity);
                }
            });
        }
    }

    public void off(final Activity activity) {
        final T t = this.mStrategy;
        if (t.isSupport(activity)) {
            getGLHandler().post(new Runnable() { // from class: com.netease.vrlib.strategy.ModeManager.3
                @Override // java.lang.Runnable
                public void run() {
                    t.off(activity);
                }
            });
        }
    }

    protected T getStrategy() {
        return this.mStrategy;
    }

    public int getMode() {
        return this.mMode;
    }

    public MDGLHandler getGLHandler() {
        return this.mGLHandler;
    }
}