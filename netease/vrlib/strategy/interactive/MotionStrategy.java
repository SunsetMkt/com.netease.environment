package com.netease.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.common.MDMainHandler;
import com.netease.vrlib.common.VRUtil;
import com.netease.vrlib.strategy.interactive.InteractiveModeManager;
import java.util.Iterator;

/* loaded from: classes5.dex */
public class MotionStrategy extends AbsInteractiveStrategy implements SensorEventListener {
    private static final String TAG = "MotionStrategy";
    private int mDeviceRotation;
    private Boolean mIsSupport;
    private final Object mMatrixLock;
    private boolean mRegistered;
    private float[] mSensorMatrix;
    private float[] mTmpMatrix;
    private Runnable updateSensorRunnable;

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public boolean handleDrag(int i, int i2) {
        return false;
    }

    public MotionStrategy(InteractiveModeManager.Params params) {
        super(params);
        this.mSensorMatrix = new float[16];
        this.mTmpMatrix = new float[16];
        this.mRegistered = false;
        this.mIsSupport = null;
        this.mMatrixLock = new Object();
        this.updateSensorRunnable = new Runnable() { // from class: com.netease.vrlib.strategy.interactive.MotionStrategy.1
            @Override // java.lang.Runnable
            public void run() {
                if (MotionStrategy.this.mRegistered) {
                    synchronized (MotionStrategy.this.mMatrixLock) {
                        Iterator<MD360Director> it = MotionStrategy.this.getDirectorList().iterator();
                        while (it.hasNext()) {
                            it.next().updateSensorMatrix(MotionStrategy.this.mTmpMatrix);
                        }
                    }
                }
            }
        };
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void onResume(Context context) {
        registerSensor(context);
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void onPause(Context context) {
        unregisterSensor(context);
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public void onOrientationChanged(Activity activity) {
        this.mDeviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
        this.mDeviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Iterator<MD360Director> it = getDirectorList().iterator();
        while (it.hasNext()) {
            it.next().reset();
        }
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
        unregisterSensor(activity);
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        if (this.mIsSupport == null) {
            this.mIsSupport = Boolean.valueOf(((SensorManager) activity.getSystemService("sensor")).getDefaultSensor(11) != null);
        }
        return this.mIsSupport.booleanValue();
    }

    protected void registerSensor(Context context) {
        if (this.mRegistered) {
            return;
        }
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(11);
        if (defaultSensor == null) {
            Log.e(TAG, "TYPE_ROTATION_VECTOR sensor not support!");
        } else {
            sensorManager.registerListener(this, defaultSensor, getParams().mMotionDelay, MDMainHandler.sharedHandler());
            this.mRegistered = true;
        }
    }

    protected void unregisterSensor(Context context) {
        if (this.mRegistered) {
            ((SensorManager) context.getSystemService("sensor")).unregisterListener(this);
            this.mRegistered = false;
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.accuracy != 0) {
            if (getParams().mSensorListener != null) {
                getParams().mSensorListener.onSensorChanged(sensorEvent);
            }
            if (sensorEvent.sensor.getType() != 11) {
                return;
            }
            VRUtil.sensorRotationVector2Matrix(sensorEvent, this.mDeviceRotation, this.mSensorMatrix);
            synchronized (this.mMatrixLock) {
                System.arraycopy(this.mSensorMatrix, 0, this.mTmpMatrix, 0, 16);
            }
            getParams().glHandler.post(this.updateSensorRunnable);
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (getParams().mSensorListener != null) {
            getParams().mSensorListener.onAccuracyChanged(sensor, i);
        }
    }
}