package com.netease.vrlib.objects;

import android.content.Context;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDMultiFisheye3D extends MDAbsObject3D {
    private static final String TAG = "MDMultiFisheye3D";

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    protected void executeLoad(Context context) {
        generateSphere(18.0f, 29, 30, this);
    }

    private static void generateSphere(float f, int i, int i2, MDAbsObject3D mDAbsObject3D) {
        int i3;
        float[] fArr;
        short[] sArr;
        float f2 = 1.0f / i;
        float f3 = 1.0f / i2;
        int i4 = i + 1;
        int i5 = i2 + 1;
        int i6 = i4 * i5;
        int i7 = i6 * 3;
        float[] fArr2 = new float[i7];
        int i8 = i6 * 2;
        float[] fArr3 = new float[i8];
        int i9 = i6 * 6;
        short[] sArr2 = new short[i9];
        int i10 = 0;
        int i11 = 0;
        for (short s = 0; s < i4; s = (short) (s + 1)) {
            short s2 = 0;
            while (s2 < i5) {
                int i12 = i4;
                float f4 = f3;
                double d = s2 * 6.2831855f * f3;
                float f5 = s;
                int i13 = i9;
                double d2 = 3.1415927f * f5 * f2;
                int i14 = i8;
                float[] fArr4 = fArr3;
                float fCos = (float) (Math.cos(d) * Math.sin(d2));
                float f6 = -((float) Math.sin(r13 - 1.5707964f));
                float fSin = (float) (Math.sin(d2) * Math.sin(d));
                int i15 = i11 + 1;
                fArr2[i11] = fCos * f;
                int i16 = i15 + 1;
                fArr2[i15] = f6 * f;
                i11 = i16 + 1;
                fArr2[i16] = fSin * f;
                int i17 = i10 * 2;
                if (i17 < i6) {
                    double d3 = s;
                    sArr = sArr2;
                    double d4 = f2;
                    i3 = i7;
                    fArr = fArr2;
                    float fSin2 = (((float) (Math.sin(d) * d3 * d4 * 2.0d * 0.6499999761581421d)) * 0.5f) + 0.5f;
                    float fCos2 = (((float) (Math.cos(d) * d3 * d4 * 2.0d * 0.6499999761581421d)) * 0.5f) + 0.5f;
                    fArr4[i17] = fSin2;
                    fArr4[i17 + 1] = fCos2 * 0.5f;
                } else {
                    i3 = i7;
                    fArr = fArr2;
                    sArr = sArr2;
                    double d5 = 1.0f - (f5 * f2);
                    float fSin3 = (((float) (Math.sin(d) * d5 * 2.0d * 0.6499999761581421d)) * 0.5f) + 0.5f;
                    float fCos3 = (((float) (Math.cos(d) * d5 * 2.0d * 0.6499999761581421d)) * 0.5f) + 0.5f;
                    fArr4[i17] = 1.0f - fSin3;
                    fArr4[i17 + 1] = (fCos3 * 0.5f) + 0.5f;
                }
                i10++;
                s2 = (short) (s2 + 1);
                f3 = f4;
                i4 = i12;
                i8 = i14;
                i9 = i13;
                fArr3 = fArr4;
                sArr2 = sArr;
                i7 = i3;
                fArr2 = fArr;
            }
        }
        int i18 = i7;
        float[] fArr5 = fArr2;
        int i19 = i8;
        float[] fArr6 = fArr3;
        int i20 = i9;
        short[] sArr3 = sArr2;
        for (int i21 = 0; i21 < i6; i21++) {
            Log.e(TAG, String.format("p %d,", Integer.valueOf(i21)));
            int i22 = i21 * 3;
            Log.e(TAG, String.format("v %d, x=%f y=%f z=%f", Integer.valueOf(i21), Float.valueOf(fArr5[i22]), Float.valueOf(fArr5[i22 + 1]), Float.valueOf(fArr5[i22 + 2])));
            int i23 = i21 * 2;
            Log.e(TAG, String.format("t %d, x=%f y=%f", Integer.valueOf(i21), Float.valueOf(fArr6[i23]), Float.valueOf(fArr6[i23 + 1])));
        }
        int i24 = 0;
        for (short s3 = 0; s3 < i; s3 = (short) (s3 + 1)) {
            short s4 = 0;
            while (s4 < i2) {
                int i25 = i24 + 1;
                int i26 = s3 * i5;
                sArr3[i24] = (short) (i26 + s4);
                int i27 = i25 + 1;
                int i28 = (s3 + 1) * i5;
                short s5 = (short) (i28 + s4);
                sArr3[i25] = s5;
                int i29 = i27 + 1;
                int i30 = s4 + 1;
                short s6 = (short) (i26 + i30);
                sArr3[i27] = s6;
                int i31 = i29 + 1;
                sArr3[i29] = s6;
                int i32 = i31 + 1;
                sArr3[i31] = s5;
                i24 = i32 + 1;
                sArr3[i32] = (short) (i28 + i30);
                s4 = (short) i30;
            }
        }
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i18 * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr5);
        floatBufferAsFloatBuffer.position(0);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(i19 * 4);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
        floatBufferAsFloatBuffer2.put(fArr6);
        floatBufferAsFloatBuffer2.position(0);
        ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(i20 * 2);
        byteBufferAllocateDirect3.order(ByteOrder.nativeOrder());
        ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect3.asShortBuffer();
        shortBufferAsShortBuffer.put(sArr3);
        shortBufferAsShortBuffer.position(0);
        mDAbsObject3D.setIndicesBuffer(shortBufferAsShortBuffer);
        mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
        mDAbsObject3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
        mDAbsObject3D.setNumIndices(i20);
    }
}