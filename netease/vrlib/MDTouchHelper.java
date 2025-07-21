package com.netease.vrlib;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.model.MDPinchConfig;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes3.dex */
public class MDTouchHelper {
    private static final int MODE_INIT = 0;
    private static final int MODE_PINCH = 1;
    private float defaultScale;
    private MDVRLibrary.IAdvanceGestureListener mAdvanceGestureListener;
    private GestureDetector mGestureDetector;
    private float mGlobalScale;
    private boolean mPinchEnabled;
    private float mSensitivity;
    private float maxScale;
    private float minScale;
    private List<MDVRLibrary.IGestureListener> mClickListeners = new LinkedList();
    private int mCurrentMode = 0;
    private PinchInfo mPinchInfo = new PinchInfo();

    public MDTouchHelper(Context context) {
        this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.netease.vrlib.MDTouchHelper.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if (MDTouchHelper.this.mCurrentMode == 1) {
                    return false;
                }
                if (MDTouchHelper.this.mClickListeners != null) {
                    Iterator it = MDTouchHelper.this.mClickListeners.iterator();
                    while (it.hasNext()) {
                        ((MDVRLibrary.IGestureListener) it.next()).onClick(motionEvent);
                    }
                }
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (MDTouchHelper.this.mCurrentMode == 1) {
                    return false;
                }
                if (MDTouchHelper.this.mAdvanceGestureListener != null) {
                    MDTouchHelper.this.mAdvanceGestureListener.onDrag(f / MDTouchHelper.this.mGlobalScale, f2 / MDTouchHelper.this.mGlobalScale);
                }
                return true;
            }
        });
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 1 || action == 3) {
            this.mCurrentMode = 0;
        } else if (action == 6) {
            if (this.mCurrentMode == 1 && motionEvent.getPointerCount() > 2) {
                if ((motionEvent.getAction() >> 8) == 0) {
                    markPinchInfo(motionEvent.getX(1), motionEvent.getY(1), motionEvent.getX(2), motionEvent.getY(2));
                } else if ((motionEvent.getAction() >> 8) == 1) {
                    markPinchInfo(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(2), motionEvent.getY(2));
                }
            }
        } else if (action == 5) {
            this.mCurrentMode = 1;
            markPinchInfo(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
        } else if (action == 2 && this.mCurrentMode == 1 && motionEvent.getPointerCount() > 1) {
            handlePinch(calDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1)));
        }
        this.mGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public void scaleTo(float f) {
        setScaleInner(this.mPinchInfo.setScale(f));
    }

    public void reset() {
        setScaleInner(this.mPinchInfo.reset());
    }

    private void handlePinch(float f) {
        if (this.mPinchEnabled) {
            setScaleInner(this.mPinchInfo.pinch(f));
        }
    }

    private void setScaleInner(float f) {
        MDVRLibrary.IAdvanceGestureListener iAdvanceGestureListener = this.mAdvanceGestureListener;
        if (iAdvanceGestureListener != null) {
            iAdvanceGestureListener.onPinch(f);
        }
        this.mGlobalScale = f;
    }

    private void markPinchInfo(float f, float f2, float f3, float f4) {
        this.mPinchInfo.mark(f, f2, f3, f4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float calDistance(float f, float f2, float f3, float f4) {
        return (float) Math.sqrt(Math.pow(f - f3, 2.0d) + Math.pow(f2 - f4, 2.0d));
    }

    public void addClickListener(MDVRLibrary.IGestureListener iGestureListener) {
        if (iGestureListener != null) {
            this.mClickListeners.add(iGestureListener);
        }
    }

    public void setAdvanceGestureListener(MDVRLibrary.IAdvanceGestureListener iAdvanceGestureListener) {
        this.mAdvanceGestureListener = iAdvanceGestureListener;
    }

    public void setPinchEnabled(boolean z) {
        this.mPinchEnabled = z;
    }

    public void setPinchConfig(MDPinchConfig mDPinchConfig) {
        this.minScale = mDPinchConfig.getMin();
        this.maxScale = mDPinchConfig.getMax();
        this.mSensitivity = mDPinchConfig.getSensitivity();
        float defaultValue = mDPinchConfig.getDefaultValue();
        this.defaultScale = defaultValue;
        float fMax = Math.max(this.minScale, defaultValue);
        this.defaultScale = fMax;
        float fMin = Math.min(this.maxScale, fMax);
        this.defaultScale = fMin;
        setScaleInner(fMin);
    }

    private class PinchInfo {
        private float currentScale;
        private float oDistance;
        private float prevScale;
        private float x1;
        private float x2;
        private float y1;
        private float y2;

        private PinchInfo() {
        }

        public void mark(float f, float f2, float f3, float f4) {
            this.x1 = f;
            this.y1 = f2;
            this.x2 = f3;
            this.y2 = f4;
            this.oDistance = MDTouchHelper.calDistance(f, f2, f3, f4);
            this.prevScale = this.currentScale;
        }

        public float pinch(float f) {
            if (this.oDistance == 0.0f) {
                this.oDistance = f;
            }
            float f2 = this.prevScale + (((f / this.oDistance) - 1.0f) * MDTouchHelper.this.mSensitivity);
            this.currentScale = f2;
            float fMax = Math.max(f2, MDTouchHelper.this.minScale);
            this.currentScale = fMax;
            float fMin = Math.min(fMax, MDTouchHelper.this.maxScale);
            this.currentScale = fMin;
            return fMin;
        }

        public float setScale(float f) {
            this.prevScale = f;
            this.currentScale = f;
            return f;
        }

        public float reset() {
            return setScale(MDTouchHelper.this.defaultScale);
        }
    }

    public void release() {
        this.mGestureDetector = null;
        this.mPinchInfo = null;
        List<MDVRLibrary.IGestureListener> list = this.mClickListeners;
        if (list != null) {
            list.clear();
            this.mClickListeners = null;
        }
        this.mAdvanceGestureListener = null;
    }
}