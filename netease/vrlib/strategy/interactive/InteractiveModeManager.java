package com.netease.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorEventListener;
import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.strategy.ModeManager;
import com.netease.vrlib.strategy.projection.ProjectionModeManager;

/* loaded from: classes5.dex */
public class InteractiveModeManager extends ModeManager<AbsInteractiveStrategy> implements IInteractiveMode {
    private static int[] sModes = {1, 2, 3, 4};
    private boolean mIsResumed;
    private Params mParams;
    private UpdateDragRunnable updateDragRunnable;

    public static class Params {
        public MDGLHandler glHandler;
        public int mMotionDelay;
        public SensorEventListener mSensorListener;
        public ProjectionModeManager projectionModeManager;
    }

    public InteractiveModeManager(int i, MDGLHandler mDGLHandler, Params params) {
        super(i, mDGLHandler);
        this.updateDragRunnable = new UpdateDragRunnable();
        this.mParams = params;
        params.glHandler = getGLHandler();
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    protected int[] getModes() {
        return sModes;
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    public AbsInteractiveStrategy createStrategy(int i) {
        if (i == 1) {
            return new MotionStrategy(this.mParams);
        }
        if (i == 3) {
            return new MotionWithTouchStrategy(this.mParams);
        }
        if (i == 4) {
            return new CardboardMotionStrategy(this.mParams);
        }
        if (i == 5) {
            return new CardboardMTStrategy(this.mParams);
        }
        return new TouchStrategy(this.mParams);
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public boolean handleDrag(int i, int i2) {
        this.updateDragRunnable.handleDrag(i, i2);
        getGLHandler().post(this.updateDragRunnable);
        return false;
    }

    /* renamed from: com.netease.vrlib.strategy.interactive.InteractiveModeManager$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Activity val$activity;

        AnonymousClass1(Activity activity) {
            activity = activity;
        }

        @Override // java.lang.Runnable
        public void run() {
            ((AbsInteractiveStrategy) InteractiveModeManager.this.getStrategy()).onOrientationChanged(activity);
        }
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public void onOrientationChanged(Activity activity) {
        getGLHandler().post(new Runnable() { // from class: com.netease.vrlib.strategy.interactive.InteractiveModeManager.1
            final /* synthetic */ Activity val$activity;

            AnonymousClass1(Activity activity2) {
                activity = activity2;
            }

            @Override // java.lang.Runnable
            public void run() {
                ((AbsInteractiveStrategy) InteractiveModeManager.this.getStrategy()).onOrientationChanged(activity);
            }
        });
    }

    private class UpdateDragRunnable implements Runnable {
        private int distanceX;
        private int distanceY;

        private UpdateDragRunnable() {
        }

        /* synthetic */ UpdateDragRunnable(InteractiveModeManager interactiveModeManager, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void handleDrag(int i, int i2) {
            this.distanceX = i;
            this.distanceY = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            ((AbsInteractiveStrategy) InteractiveModeManager.this.getStrategy()).handleDrag(this.distanceX, this.distanceY);
        }
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public void onResume(Context context) {
        this.mIsResumed = true;
        if (getStrategy().isSupport((Activity) context)) {
            getStrategy().onResume(context);
        }
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    public void on(Activity activity) {
        super.on(activity);
        if (this.mIsResumed) {
            onResume(activity);
        }
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public void onPause(Context context) {
        this.mIsResumed = false;
        if (getStrategy().isSupport((Activity) context)) {
            getStrategy().onPause(context);
        }
    }
}