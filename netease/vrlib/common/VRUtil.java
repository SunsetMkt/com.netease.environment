package com.netease.vrlib.common;

import android.graphics.PointF;
import android.opengl.Matrix;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.model.MDRay;
import com.netease.vrlib.model.MDVector3D;

/* loaded from: classes5.dex */
public class VRUtil {
    private static final String TAG = "VRUtil";
    private static float[] mTmp = new float[16];
    public static final float sNotHit = Float.MAX_VALUE;

    /* JADX WARN: Removed duplicated region for block: B:12:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void sensorRotationVector2Matrix(android.hardware.SensorEvent r7, int r8, float[] r9) {
        /*
            float[] r7 = r7.values
            if (r8 == 0) goto L28
            r0 = 2
            r1 = 1
            if (r8 == r1) goto L1b
            if (r8 == r0) goto L28
            r0 = 3
            if (r8 == r0) goto Le
            goto L2b
        Le:
            float[] r8 = com.netease.vrlib.common.VRUtil.mTmp
            android.hardware.SensorManager.getRotationMatrixFromVector(r8, r7)
            float[] r7 = com.netease.vrlib.common.VRUtil.mTmp
            r8 = 130(0x82, float:1.82E-43)
            android.hardware.SensorManager.remapCoordinateSystem(r7, r8, r1, r9)
            goto L2b
        L1b:
            float[] r8 = com.netease.vrlib.common.VRUtil.mTmp
            android.hardware.SensorManager.getRotationMatrixFromVector(r8, r7)
            float[] r7 = com.netease.vrlib.common.VRUtil.mTmp
            r8 = 129(0x81, float:1.81E-43)
            android.hardware.SensorManager.remapCoordinateSystem(r7, r0, r8, r9)
            goto L2b
        L28:
            android.hardware.SensorManager.getRotationMatrixFromVector(r9, r7)
        L2b:
            r2 = 0
            r3 = 1119092736(0x42b40000, float:90.0)
            r4 = 1065353216(0x3f800000, float:1.0)
            r5 = 0
            r6 = 0
            r1 = r9
            android.opengl.Matrix.rotateM(r1, r2, r3, r4, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.vrlib.common.VRUtil.sensorRotationVector2Matrix(android.hardware.SensorEvent, int, float[]):void");
    }

    public static void notNull(Object obj, String str) {
        if (obj == null) {
            throw new RuntimeException(str);
        }
    }

    public static void barrelDistortion(double d, double d2, double d3, PointF pointF) {
        double d4 = ((1.0d - d) - d2) - d3;
        if (pointF.x == 0.0d && pointF.y == 0.0d) {
            return;
        }
        double d5 = 1.0f;
        double d6 = (pointF.x - 0.0d) / d5;
        double d7 = (pointF.y - 0.0d) / d5;
        double dSqrt = Math.sqrt((d6 * d6) + (d7 * d7));
        double dAbs = Math.abs(dSqrt / (((((((d * dSqrt) * dSqrt) * dSqrt) + ((d2 * dSqrt) * dSqrt)) + (d3 * dSqrt)) + d4) * dSqrt));
        pointF.set((float) ((d6 * dAbs * d5) + 0.0d), (float) (0.0d + (d7 * dAbs * d5)));
    }

    public static MDVector3D vec3Sub(MDVector3D mDVector3D, MDVector3D mDVector3D2) {
        return new MDVector3D().setX(mDVector3D.getX() - mDVector3D2.getX()).setY(mDVector3D.getY() - mDVector3D2.getY()).setZ(mDVector3D.getZ() - mDVector3D2.getZ());
    }

    public static MDVector3D vec3Cross(MDVector3D mDVector3D, MDVector3D mDVector3D2) {
        return new MDVector3D().setX((mDVector3D.getY() * mDVector3D2.getZ()) - (mDVector3D2.getY() * mDVector3D.getZ())).setY((mDVector3D.getZ() * mDVector3D2.getX()) - (mDVector3D2.getZ() * mDVector3D.getX())).setZ((mDVector3D.getX() * mDVector3D2.getY()) - (mDVector3D2.getX() * mDVector3D.getY()));
    }

    public static float vec3Dot(MDVector3D mDVector3D, MDVector3D mDVector3D2) {
        return (mDVector3D.getX() * mDVector3D2.getX()) + (mDVector3D.getY() * mDVector3D2.getY()) + (mDVector3D.getZ() * mDVector3D2.getZ());
    }

    public static MDRay point2Ray(float f, float f2, MD360Director mD360Director) {
        MDVector3D mDVector3D = new MDVector3D();
        float[] projectionMatrix = mD360Director.getProjectionMatrix();
        mDVector3D.setX((-(((f * 2.0f) / mD360Director.getViewportWidth()) - 1.0f)) / projectionMatrix[0]);
        mDVector3D.setY((((f2 * 2.0f) / mD360Director.getViewportHeight()) - 1.0f) / projectionMatrix[5]);
        mDVector3D.setZ(1.0f);
        float[] viewMatrix = mD360Director.getViewMatrix();
        float[] tempInvertMatrix = mD360Director.getTempInvertMatrix();
        if (!Matrix.invertM(tempInvertMatrix, 0, viewMatrix, 0)) {
            return null;
        }
        MDVector3D mDVector3D2 = new MDVector3D();
        MDVector3D mDVector3D3 = new MDVector3D();
        mDVector3D2.setX((mDVector3D.getX() * tempInvertMatrix[0]) + (mDVector3D.getY() * tempInvertMatrix[4]) + (mDVector3D.getZ() * tempInvertMatrix[8]));
        mDVector3D2.setY((mDVector3D.getX() * tempInvertMatrix[1]) + (mDVector3D.getY() * tempInvertMatrix[5]) + (mDVector3D.getZ() * tempInvertMatrix[9]));
        mDVector3D2.setZ((mDVector3D.getX() * tempInvertMatrix[2]) + (mDVector3D.getY() * tempInvertMatrix[6]) + (mDVector3D.getZ() * tempInvertMatrix[10]));
        mDVector3D3.setX(tempInvertMatrix[12]);
        mDVector3D3.setY(tempInvertMatrix[13]);
        mDVector3D3.setZ(tempInvertMatrix[14]);
        return new MDRay(mDVector3D3, mDVector3D2);
    }

    public static float intersectTriangle(MDRay mDRay, MDVector3D mDVector3D, MDVector3D mDVector3D2, MDVector3D mDVector3D3) {
        MDVector3D mDVector3DVec3Sub;
        MDVector3D mDVector3DVec3Sub2 = vec3Sub(mDVector3D2, mDVector3D);
        MDVector3D mDVector3DVec3Sub3 = vec3Sub(mDVector3D3, mDVector3D);
        MDVector3D mDVector3DVec3Cross = vec3Cross(mDRay.getDir(), mDVector3DVec3Sub3);
        float fVec3Dot = vec3Dot(mDVector3DVec3Sub2, mDVector3DVec3Cross);
        if (fVec3Dot > 0.0f) {
            mDVector3DVec3Sub = vec3Sub(mDRay.getOrig(), mDVector3D);
        } else {
            mDVector3DVec3Sub = vec3Sub(mDVector3D, mDRay.getOrig());
            fVec3Dot = -fVec3Dot;
        }
        if (fVec3Dot < 1.0E-4f) {
            return Float.MAX_VALUE;
        }
        float fVec3Dot2 = vec3Dot(mDVector3DVec3Sub, mDVector3DVec3Cross);
        if (fVec3Dot2 >= 0.0f && fVec3Dot2 <= fVec3Dot) {
            MDVector3D mDVector3DVec3Cross2 = vec3Cross(mDVector3DVec3Sub, mDVector3DVec3Sub2);
            float fVec3Dot3 = vec3Dot(mDRay.getDir(), mDVector3DVec3Cross2);
            if (fVec3Dot3 >= 0.0f && fVec3Dot2 + fVec3Dot3 <= fVec3Dot) {
                float fVec3Dot4 = vec3Dot(mDVector3DVec3Sub3, mDVector3DVec3Cross2) * (1.0f / fVec3Dot);
                if (fVec3Dot4 > 0.0f) {
                    return Float.MAX_VALUE;
                }
                return Math.abs(fVec3Dot4);
            }
        }
        return Float.MAX_VALUE;
    }
}