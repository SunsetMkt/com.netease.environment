package com.netease.ntunisdk.zxing.client.android;

import android.hardware.Camera;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class ZoomHandler {
    private static ZoomHandler sInstance;
    private Camera mCamera;
    private float mOldDist = 1.0f;
    private boolean mZoomSupported;

    private ZoomHandler(Camera camera) {
        this.mCamera = camera;
        this.mZoomSupported = camera.getParameters().isZoomSupported();
    }

    public static void init(Camera camera) {
        if (sInstance == null) {
            sInstance = new ZoomHandler(camera);
        }
    }

    public static void destroy() {
        ZoomHandler zoomHandler = sInstance;
        if (zoomHandler != null) {
            zoomHandler.mCamera = null;
        }
        sInstance = null;
    }

    public static boolean handleTouchEvent(MotionEvent motionEvent) {
        ZoomHandler zoomHandler = sInstance;
        if (zoomHandler == null) {
            return false;
        }
        return zoomHandler.onTouchEvent(motionEvent);
    }

    private boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mZoomSupported) {
            return false;
        }
        if (motionEvent.getPointerCount() == 1) {
            return true;
        }
        int action = motionEvent.getAction() & 255;
        if (action == 2) {
            float fingerSpacing = getFingerSpacing(motionEvent);
            float f = this.mOldDist;
            if (fingerSpacing > f) {
                handleZoom(true);
            } else if (fingerSpacing < f) {
                handleZoom(false);
            }
            this.mOldDist = fingerSpacing;
        } else if (action == 5) {
            this.mOldDist = getFingerSpacing(motionEvent);
        }
        return true;
    }

    private float getFingerSpacing(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((x * x) + (y * y));
    }

    private void handleZoom(boolean z) {
        Camera.Parameters parameters = this.mCamera.getParameters();
        int maxZoom = parameters.getMaxZoom();
        int zoom = parameters.getZoom();
        if (z && zoom < maxZoom) {
            zoom++;
        } else if (zoom > 0) {
            zoom--;
        }
        parameters.setZoom(zoom);
        this.mCamera.setParameters(parameters);
    }
}