package com.netease.vrlib;

import android.content.Context;
import android.view.MotionEvent;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.common.MDMainHandler;
import com.netease.vrlib.common.VRUtil;
import com.netease.vrlib.model.MDRay;
import com.netease.vrlib.plugins.IMDHotspot;
import com.netease.vrlib.plugins.MDAbsPlugin;
import com.netease.vrlib.plugins.MDPluginManager;
import com.netease.vrlib.strategy.display.DisplayModeManager;
import com.netease.vrlib.strategy.projection.ProjectionModeManager;

/* loaded from: classes3.dex */
public class MDPickerManager {
    private static final int HIT_FROM_EYE = 1;
    private static final int HIT_FROM_TOUCH = 2;
    private static final String TAG = "MDPickerManager";
    private DisplayModeManager mDisplayModeManager;
    private MDVRLibrary.IEyePickListener mEyePickChangedListener;
    private boolean mEyePickEnable;
    private EyePickPoster mEyePickPoster;
    private MDAbsPlugin mEyePicker;
    private MDGLHandler mGLHandler;
    private MDPluginManager mPluginManager;
    private ProjectionModeManager mProjectionModeManager;
    private RayPickAsTouchTask mRayPickAsTouchRunnable;
    private MDVRLibrary.ITouchPickListener mTouchPickListener;
    private TouchPickPoster mTouchPickPoster;
    private MDVRLibrary.IGestureListener mTouchPicker;

    private MDPickerManager(Builder builder) {
        this.mEyePickPoster = new EyePickPoster();
        this.mTouchPickPoster = new TouchPickPoster();
        this.mRayPickAsTouchRunnable = new RayPickAsTouchTask();
        this.mTouchPicker = new MDVRLibrary.IGestureListener() { // from class: com.netease.vrlib.MDPickerManager.1
            @Override // com.netease.vrlib.MDVRLibrary.IGestureListener
            public void onClick(MotionEvent motionEvent) {
                MDPickerManager.this.mRayPickAsTouchRunnable.setEvent(motionEvent.getX(), motionEvent.getY());
                MDPickerManager.this.mGLHandler.post(MDPickerManager.this.mRayPickAsTouchRunnable);
            }
        };
        this.mEyePicker = new MDAbsPlugin() { // from class: com.netease.vrlib.MDPickerManager.2
            @Override // com.netease.vrlib.plugins.MDAbsPlugin
            public void beforeRenderer(int i, int i2) {
            }

            @Override // com.netease.vrlib.plugins.MDAbsPlugin
            public void destroy() {
            }

            @Override // com.netease.vrlib.plugins.MDAbsPlugin
            protected void init(Context context) {
            }

            @Override // com.netease.vrlib.plugins.MDAbsPlugin
            protected boolean removable() {
                return false;
            }

            @Override // com.netease.vrlib.plugins.MDAbsPlugin
            public void renderer(int i, int i2, int i3, MD360Director mD360Director) {
                if (i == 0 && MDPickerManager.this.isEyePickEnable()) {
                    MDPickerManager.this.rayPickAsEye(i2 >> 1, i3 >> 1, mD360Director);
                }
            }
        };
        this.mDisplayModeManager = builder.displayModeManager;
        this.mProjectionModeManager = builder.projectionModeManager;
        this.mPluginManager = builder.pluginManager;
        this.mGLHandler = builder.glHandler;
    }

    public boolean isEyePickEnable() {
        return this.mEyePickEnable;
    }

    public void setEyePickEnable(boolean z) {
        this.mEyePickEnable = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rayPickAsTouch(float f, float f2) {
        MDVRLibrary.ITouchPickListener iTouchPickListener;
        int visibleSize = this.mDisplayModeManager.getVisibleSize();
        if (visibleSize == 0) {
            return;
        }
        int viewportWidth = (int) (f / this.mProjectionModeManager.getDirectors().get(0).getViewportWidth());
        if (viewportWidth >= visibleSize) {
            return;
        }
        MDRay mDRayPoint2Ray = VRUtil.point2Ray(f - (r1 * viewportWidth), f2, this.mProjectionModeManager.getDirectors().get(viewportWidth));
        IMDHotspot iMDHotspotPick = pick(mDRayPoint2Ray, 2);
        if (mDRayPoint2Ray == null || (iTouchPickListener = this.mTouchPickListener) == null) {
            return;
        }
        iTouchPickListener.onHotspotHit(iMDHotspotPick, mDRayPoint2Ray);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rayPickAsEye(float f, float f2, MD360Director mD360Director) {
        pick(VRUtil.point2Ray(f, f2, mD360Director), 1);
    }

    private IMDHotspot pick(MDRay mDRay, int i) {
        if (mDRay == null) {
            return null;
        }
        return hitTest(mDRay, i);
    }

    private IMDHotspot hitTest(MDRay mDRay, int i) {
        IMDHotspot iMDHotspot = null;
        float f = Float.MAX_VALUE;
        for (Object obj : this.mPluginManager.getPlugins()) {
            if (obj instanceof IMDHotspot) {
                IMDHotspot iMDHotspot2 = (IMDHotspot) obj;
                float fHit = iMDHotspot2.hit(mDRay);
                if (fHit != Float.MAX_VALUE && fHit <= f) {
                    iMDHotspot = iMDHotspot2;
                    f = fHit;
                }
            }
        }
        if (i == 1) {
            this.mEyePickPoster.setHit(iMDHotspot);
            MDMainHandler.sharedHandler().postDelayed(this.mEyePickPoster, 100L);
        } else if (i == 2 && f != Float.MAX_VALUE) {
            this.mTouchPickPoster.setRay(mDRay);
            this.mTouchPickPoster.setHit(iMDHotspot);
            MDMainHandler.sharedHandler().post(this.mTouchPickPoster);
        }
        return iMDHotspot;
    }

    public MDVRLibrary.IGestureListener getTouchPicker() {
        return this.mTouchPicker;
    }

    public MDAbsPlugin getEyePicker() {
        return this.mEyePicker;
    }

    public static Builder with() {
        return new Builder();
    }

    public void setEyePickChangedListener(MDVRLibrary.IEyePickListener iEyePickListener) {
        this.mEyePickChangedListener = iEyePickListener;
    }

    public void setTouchPickListener(MDVRLibrary.ITouchPickListener iTouchPickListener) {
        this.mTouchPickListener = iTouchPickListener;
    }

    private class EyePickPoster implements Runnable {
        private IMDHotspot hit;
        private long timestamp;

        private EyePickPoster() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MDMainHandler.sharedHandler().removeCallbacks(this);
            if (MDPickerManager.this.mEyePickChangedListener != null) {
                MDPickerManager.this.mEyePickChangedListener.onHotspotHit(this.hit, this.timestamp);
            }
        }

        public void setHit(IMDHotspot iMDHotspot) {
            if (this.hit != iMDHotspot) {
                this.timestamp = System.currentTimeMillis();
                IMDHotspot iMDHotspot2 = this.hit;
                if (iMDHotspot2 != null) {
                    iMDHotspot2.onEyeHitOut();
                }
            }
            this.hit = iMDHotspot;
            if (iMDHotspot != null) {
                iMDHotspot.onEyeHitIn(this.timestamp);
            }
        }
    }

    private static class TouchPickPoster implements Runnable {
        private IMDHotspot hit;
        private MDRay ray;

        private TouchPickPoster() {
        }

        @Override // java.lang.Runnable
        public void run() {
            IMDHotspot iMDHotspot = this.hit;
            if (iMDHotspot != null) {
                iMDHotspot.onTouchHit(this.ray);
            }
        }

        public void setRay(MDRay mDRay) {
            this.ray = mDRay;
        }

        public void setHit(IMDHotspot iMDHotspot) {
            this.hit = iMDHotspot;
        }
    }

    public void resetEyePick() {
        EyePickPoster eyePickPoster = this.mEyePickPoster;
        if (eyePickPoster != null) {
            eyePickPoster.setHit(null);
        }
    }

    public static class Builder {
        private DisplayModeManager displayModeManager;
        private MDGLHandler glHandler;
        private MDPluginManager pluginManager;
        private ProjectionModeManager projectionModeManager;

        private Builder() {
        }

        public MDPickerManager build() {
            return new MDPickerManager(this);
        }

        public Builder setPluginManager(MDPluginManager mDPluginManager) {
            this.pluginManager = mDPluginManager;
            return this;
        }

        public Builder setDisplayModeManager(DisplayModeManager displayModeManager) {
            this.displayModeManager = displayModeManager;
            return this;
        }

        public Builder setProjectionModeManager(ProjectionModeManager projectionModeManager) {
            this.projectionModeManager = projectionModeManager;
            return this;
        }

        public Builder setGLHandler(MDGLHandler mDGLHandler) {
            this.glHandler = mDGLHandler;
            return this;
        }
    }

    private class RayPickAsTouchTask implements Runnable {
        float x;
        float y;

        private RayPickAsTouchTask() {
        }

        public void setEvent(float f, float f2) {
            this.x = f;
            this.y = f2;
        }

        @Override // java.lang.Runnable
        public void run() {
            MDPickerManager.this.rayPickAsTouch(this.x, this.y);
        }
    }
}