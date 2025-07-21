package com.netease.vrlib.objects;

import android.content.Context;
import com.netease.vrlib.common.MDDirection;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDStereoSphere3D extends MDAbsObject3D {
    private MDDirection direction;

    public MDStereoSphere3D(MDDirection mDDirection) {
        MDDirection mDDirection2 = MDDirection.HORIZONTAL;
        this.direction = mDDirection;
    }

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    protected void executeLoad(Context context) {
        generateSphere(this, this.direction);
    }

    private static void generateSphere(MDAbsObject3D mDAbsObject3D, MDDirection mDDirection) {
        generateSphere(18.0f, 75, SDefine.fK, mDAbsObject3D, mDDirection);
    }

    private static void generateSphere(float f, int i, int i2, MDAbsObject3D mDAbsObject3D, MDDirection mDDirection) {
        int i3;
        float f2 = 1.0f / i;
        float f3 = 1.0f / i2;
        int i4 = i + 1;
        int i5 = i2 + 1;
        int i6 = i4 * i5;
        int i7 = i6 * 3;
        float[] fArr = new float[i7];
        int i8 = i6 * 2;
        float[] fArr2 = new float[i8];
        float[] fArr3 = new float[i8];
        int i9 = i6 * 6;
        short[] sArr = new short[i9];
        short s = 0;
        int i10 = 0;
        int i11 = 0;
        while (s < i4) {
            short s2 = 0;
            while (s2 < i5) {
                int i12 = i4;
                float f4 = s2;
                int i13 = i9;
                int i14 = i7;
                double d = 6.2831855f * f4 * f3;
                float f5 = s;
                int i15 = i8;
                double d2 = 3.1415927f * f5 * f2;
                short[] sArr2 = sArr;
                short s3 = s2;
                float fCos = (float) (Math.cos(d) * Math.sin(d2));
                short s4 = s;
                float f6 = -((float) Math.sin(r10 - 1.5707964f));
                float fSin = (float) (Math.sin(d) * Math.sin(d2));
                if (MDDirection.VERTICAL == mDDirection) {
                    float f7 = f4 * f3;
                    fArr2[i10] = f7;
                    fArr3[i10] = f7;
                    i3 = i10 + 1;
                    float f8 = (f5 * f2) / 2.0f;
                    fArr2[i3] = f8;
                    fArr3[i3] = f8 + 0.5f;
                } else {
                    float f9 = (f4 * f3) / 2.0f;
                    fArr2[i10] = f9;
                    fArr3[i10] = f9 + 0.5f;
                    i3 = i10 + 1;
                    float f10 = f5 * f2;
                    fArr2[i3] = f10;
                    fArr3[i3] = f10;
                }
                i10 = i3 + 1;
                int i16 = i11 + 1;
                fArr[i11] = fCos * f;
                int i17 = i16 + 1;
                fArr[i16] = f6 * f;
                i11 = i17 + 1;
                fArr[i17] = fSin * f;
                s2 = (short) (s3 + 1);
                i4 = i12;
                i7 = i14;
                i9 = i13;
                s = s4;
                sArr = sArr2;
                i8 = i15;
            }
            s = (short) (s + 1);
        }
        int i18 = i9;
        int i19 = i7;
        int i20 = i8;
        short[] sArr3 = sArr;
        int i21 = 0;
        for (short s5 = 0; s5 < i; s5 = (short) (s5 + 1)) {
            short s6 = 0;
            while (s6 < i2) {
                int i22 = i21 + 1;
                int i23 = s5 * i5;
                sArr3[i21] = (short) (i23 + s6);
                int i24 = i22 + 1;
                int i25 = (s5 + 1) * i5;
                short s7 = (short) (i25 + s6);
                sArr3[i22] = s7;
                int i26 = i24 + 1;
                int i27 = s6 + 1;
                short s8 = (short) (i23 + i27);
                sArr3[i24] = s8;
                int i28 = i26 + 1;
                sArr3[i26] = s8;
                int i29 = i28 + 1;
                sArr3[i28] = s7;
                i21 = i29 + 1;
                sArr3[i29] = (short) (i25 + i27);
                s6 = (short) i27;
            }
        }
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i19 * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr);
        floatBufferAsFloatBuffer.position(0);
        int i30 = i20 * 4;
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(i30);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
        floatBufferAsFloatBuffer2.put(fArr2);
        floatBufferAsFloatBuffer2.position(0);
        ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(i30);
        byteBufferAllocateDirect3.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer3 = byteBufferAllocateDirect3.asFloatBuffer();
        floatBufferAsFloatBuffer3.put(fArr3);
        floatBufferAsFloatBuffer3.position(0);
        ByteBuffer byteBufferAllocateDirect4 = ByteBuffer.allocateDirect(i18 * 2);
        byteBufferAllocateDirect4.order(ByteOrder.nativeOrder());
        ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect4.asShortBuffer();
        shortBufferAsShortBuffer.put(sArr3);
        shortBufferAsShortBuffer.position(0);
        mDAbsObject3D.setIndicesBuffer(shortBufferAsShortBuffer);
        mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer3);
        mDAbsObject3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
        mDAbsObject3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
        mDAbsObject3D.setNumIndices(i18);
    }
}