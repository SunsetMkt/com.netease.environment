package com.netease.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.google.vrtoolkit.cardboard.sensors.internal.OrientationEKF;
import com.google.vrtoolkit.cardboard.sensors.internal.Vector3d;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.common.MDMainHandler;
import com.netease.vrlib.common.VRUtil;
import com.netease.vrlib.strategy.interactive.InteractiveModeManager;
import java.util.Iterator;

/* loaded from: classes5.dex */
public class CardboardMotionStrategy extends AbsInteractiveStrategy implements SensorEventListener {
    private static final String TAG = "CardboardMotionStrategy";
    private boolean bFlag;
    private boolean bHasGyroscope;
    private int mDeviceRotation;
    private float[] mEkfToHeadTracker;
    private final Vector3d mGyroBias;
    private Boolean mIsSupport;
    private Vector3d mLatestAcc;
    private final Vector3d mLatestGyro;
    private long mLatestGyroEventClockTimeNs;
    private boolean mRegistered;
    private float[] mResultMatrix;
    private float[] mRotateMatrix;
    private float[] mSensorMatrix;
    private float[] mTmpMatrix;
    private final OrientationEKF mTracker;
    private Runnable updateSensorRunnable;
    private Runnable updateTempSensorRunnable;

    public boolean handleDrag(int i, int i2) {
        return false;
    }

    public CardboardMotionStrategy(InteractiveModeManager.Params params) {
        super(params);
        this.mResultMatrix = new float[16];
        this.mRotateMatrix = new float[16];
        this.mEkfToHeadTracker = new float[16];
        this.mRegistered = false;
        this.mIsSupport = null;
        this.mTracker = new OrientationEKF();
        this.mLatestAcc = new Vector3d();
        this.mGyroBias = new Vector3d();
        this.mLatestGyro = new Vector3d();
        this.mSensorMatrix = new float[16];
        this.mTmpMatrix = new float[16];
        this.bFlag = false;
        this.bHasGyroscope = false;
        this.updateTempSensorRunnable = new Runnable() { // from class: com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.1
            @Override // java.lang.Runnable
            public void run() {
                if (!CardboardMotionStrategy.this.mRegistered || CardboardMotionStrategy.this.bFlag) {
                    return;
                }
                synchronized (CardboardMotionStrategy.this.mTracker) {
                    Iterator<MD360Director> it = CardboardMotionStrategy.this.getDirectorList().iterator();
                    while (it.hasNext()) {
                        it.next().updateSensorMatrix(CardboardMotionStrategy.this.mTmpMatrix);
                    }
                }
            }
        };
        this.updateSensorRunnable = new Runnable() { // from class: com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.2
            /* JADX WARN: Removed duplicated region for block: B:22:0x0061  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    r7 = this;
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    boolean r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$000(r0)
                    if (r0 == 0) goto Ld1
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    boolean r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$400(r0)
                    if (r0 != 0) goto L12
                    goto Ld1
                L12:
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    com.google.vrtoolkit.cardboard.sensors.internal.OrientationEKF r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$200(r0)
                    monitor-enter(r0)
                    java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch: java.lang.Throwable -> Lce
                    long r2 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> Lce
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r4 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this     // Catch: java.lang.Throwable -> Lce
                    long r4 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$500(r4)     // Catch: java.lang.Throwable -> Lce
                    long r2 = r2 - r4
                    long r1 = r1.toSeconds(r2)     // Catch: java.lang.Throwable -> Lce
                    double r1 = (double) r1     // Catch: java.lang.Throwable -> Lce
                    r3 = 4580461061010952465(0x3f91111111111111, double:0.016666666666666666)
                    double r1 = r1 + r3
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this     // Catch: java.lang.Throwable -> Lce
                    com.google.vrtoolkit.cardboard.sensors.internal.OrientationEKF r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$200(r3)     // Catch: java.lang.Throwable -> Lce
                    double[] r1 = r3.getPredictedGLMatrix(r1)     // Catch: java.lang.Throwable -> Lce
                    r2 = 0
                    r3 = r2
                L3d:
                    int r4 = r1.length     // Catch: java.lang.Throwable -> Lce
                    if (r3 >= r4) goto L4e
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r4 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this     // Catch: java.lang.Throwable -> Lce
                    float[] r4 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$300(r4)     // Catch: java.lang.Throwable -> Lce
                    r5 = r1[r3]     // Catch: java.lang.Throwable -> Lce
                    float r5 = (float) r5     // Catch: java.lang.Throwable -> Lce
                    r4[r3] = r5     // Catch: java.lang.Throwable -> Lce
                    int r3 = r3 + 1
                    goto L3d
                L4e:
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lce
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    int r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$600(r0)
                    r1 = 0
                    if (r0 == 0) goto L61
                    r3 = 1
                    if (r0 == r3) goto L69
                    r3 = 2
                    if (r0 == r3) goto L66
                    r3 = 3
                    if (r0 == r3) goto L63
                L61:
                    r0 = r1
                    goto L6b
                L63:
                    r0 = 1132920832(0x43870000, float:270.0)
                    goto L6b
                L66:
                    r0 = 1127481344(0x43340000, float:180.0)
                    goto L6b
                L69:
                    r0 = 1119092736(0x42b40000, float:90.0)
                L6b:
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$700(r3)
                    float r4 = -r0
                    android.opengl.Matrix.setRotateEulerM(r3, r2, r1, r1, r4)
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$800(r3)
                    r4 = -1028390912(0xffffffffc2b40000, float:-90.0)
                    android.opengl.Matrix.setRotateEulerM(r3, r2, r4, r1, r0)
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r1 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$900(r0)
                    r2 = 0
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$700(r0)
                    r4 = 0
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r5 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$300(r0)
                    r6 = 0
                    android.opengl.Matrix.multiplyMM(r1, r2, r3, r4, r5, r6)
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r1 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$300(r0)
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r3 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$900(r0)
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r5 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$800(r0)
                    android.opengl.Matrix.multiplyMM(r1, r2, r3, r4, r5, r6)
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r0 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    java.util.List r0 = r0.getDirectorList()
                    java.util.Iterator r0 = r0.iterator()
                Lb7:
                    boolean r1 = r0.hasNext()
                    if (r1 == 0) goto Lcd
                    java.lang.Object r1 = r0.next()
                    com.netease.vrlib.MD360Director r1 = (com.netease.vrlib.MD360Director) r1
                    com.netease.vrlib.strategy.interactive.CardboardMotionStrategy r2 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.this
                    float[] r2 = com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.access$300(r2)
                    r1.updateSensorMatrix(r2)
                    goto Lb7
                Lcd:
                    return
                Lce:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Lce
                    throw r1
                Ld1:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.vrlib.strategy.interactive.CardboardMotionStrategy.AnonymousClass2.run():void");
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
            SensorManager sensorManager = (SensorManager) activity.getSystemService("sensor");
            this.mIsSupport = Boolean.valueOf((sensorManager.getDefaultSensor(1) == null || sensorManager.getDefaultSensor(4) == null || sensorManager.getDefaultSensor(11) == null) ? false : true);
        }
        return this.mIsSupport.booleanValue();
    }

    protected void registerSensor(Context context) {
        if (this.mRegistered) {
            return;
        }
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(1);
        Sensor defaultSensor2 = sensorManager.getDefaultSensor(4);
        Sensor defaultSensor3 = sensorManager.getDefaultSensor(11);
        if (defaultSensor == null || defaultSensor2 == null) {
            Log.e(TAG, "TYPE_ACCELEROMETER TYPE_GYROSCOPE sensor not support!");
            return;
        }
        sensorManager.registerListener(this, defaultSensor, getParams().mMotionDelay, MDMainHandler.sharedHandler());
        sensorManager.registerListener(this, defaultSensor2, getParams().mMotionDelay, MDMainHandler.sharedHandler());
        sensorManager.registerListener(this, defaultSensor3, getParams().mMotionDelay, MDMainHandler.sharedHandler());
        this.mRegistered = true;
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
            int type = sensorEvent.sensor.getType();
            if (type == 1) {
                synchronized (this.mTracker) {
                    this.mLatestAcc.set(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                    this.mTracker.processAcc(this.mLatestAcc, sensorEvent.timestamp);
                    getParams().glHandler.post(this.updateSensorRunnable);
                }
            } else {
                if (type == 4) {
                    synchronized (this.mTracker) {
                        this.mLatestGyroEventClockTimeNs = System.nanoTime();
                        this.mLatestGyro.set(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                        Vector3d vector3d = this.mLatestGyro;
                        Vector3d.sub(vector3d, this.mGyroBias, vector3d);
                        this.mTracker.processGyro(this.mLatestGyro, sensorEvent.timestamp);
                        this.bFlag = true;
                        this.bHasGyroscope = true;
                        getParams().glHandler.post(this.updateSensorRunnable);
                    }
                    return;
                }
                if (type == 11 && !this.bFlag) {
                    VRUtil.sensorRotationVector2Matrix(sensorEvent, this.mDeviceRotation, this.mSensorMatrix);
                    synchronized (this.mTracker) {
                        System.arraycopy(this.mSensorMatrix, 0, this.mTmpMatrix, 0, 16);
                    }
                }
            }
            getParams().glHandler.post(this.updateTempSensorRunnable);
            return;
        }
        if (sensorEvent.sensor.getType() != 11 || this.bFlag) {
            return;
        }
        VRUtil.sensorRotationVector2Matrix(sensorEvent, this.mDeviceRotation, this.mSensorMatrix);
        synchronized (this.mTracker) {
            System.arraycopy(this.mSensorMatrix, 0, this.mTmpMatrix, 0, 16);
        }
        getParams().glHandler.post(this.updateTempSensorRunnable);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (getParams().mSensorListener != null) {
            getParams().mSensorListener.onAccuracyChanged(sensor, i);
        }
    }
}