package com.netease.vrlib.objects;

import android.content.Context;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDSphere3D extends MDAbsObject3D {
    private static final String TAG = "MDSphere3D";

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    protected void executeLoad(Context context) {
        generateSphere(this);
    }

    private static void generateSphere(MDAbsObject3D mDAbsObject3D) {
        generateSphere(18.0f, 75, SDefine.fK, mDAbsObject3D);
    }

    private static void generateSphere(float f, int i, int i2, MDAbsObject3D mDAbsObject3D) {
        float f2 = 1.0f / i;
        float f3 = 1.0f / i2;
        int i3 = i + 1;
        int i4 = i2 + 1;
        int i5 = i3 * i4;
        int i6 = i5 * 3;
        float[] fArr = new float[i6];
        int i7 = i5 * 2;
        float[] fArr2 = new float[i7];
        int i8 = i5 * 6;
        short[] sArr = new short[i8];
        short s = 0;
        int i9 = 0;
        int i10 = 0;
        while (s < i3) {
            short s2 = 0;
            while (s2 < i4) {
                int i11 = i3;
                float f4 = s2;
                int i12 = i8;
                double d = 6.2831855f * f4 * f3;
                float f5 = s;
                int i13 = i7;
                double d2 = 3.1415927f * f5 * f2;
                short[] sArr2 = sArr;
                float fCos = (float) (Math.cos(d) * Math.sin(d2));
                float f6 = -((float) Math.sin(r10 - 1.5707964f));
                float fSin = (float) (Math.sin(d) * Math.sin(d2));
                int i14 = i9 + 1;
                fArr2[i9] = f4 * f3;
                i9 = i14 + 1;
                fArr2[i14] = f5 * f2;
                int i15 = i10 + 1;
                fArr[i10] = fCos * f;
                int i16 = i15 + 1;
                fArr[i15] = f6 * f;
                i10 = i16 + 1;
                fArr[i16] = fSin * f;
                s2 = (short) (s2 + 1);
                i3 = i11;
                i6 = i6;
                i8 = i12;
                s = s;
                sArr = sArr2;
                i7 = i13;
            }
            s = (short) (s + 1);
        }
        int i17 = i8;
        int i18 = i6;
        int i19 = i7;
        short[] sArr3 = sArr;
        int i20 = 0;
        for (short s3 = 0; s3 < i; s3 = (short) (s3 + 1)) {
            short s4 = 0;
            while (s4 < i2) {
                int i21 = i20 + 1;
                int i22 = s3 * i4;
                sArr3[i20] = (short) (i22 + s4);
                int i23 = i21 + 1;
                int i24 = (s3 + 1) * i4;
                short s5 = (short) (i24 + s4);
                sArr3[i21] = s5;
                int i25 = i23 + 1;
                int i26 = s4 + 1;
                short s6 = (short) (i22 + i26);
                sArr3[i23] = s6;
                int i27 = i25 + 1;
                sArr3[i25] = s6;
                int i28 = i27 + 1;
                sArr3[i27] = s5;
                i20 = i28 + 1;
                sArr3[i28] = (short) (i24 + i26);
                s4 = (short) i26;
            }
        }
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i18 * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr);
        floatBufferAsFloatBuffer.position(0);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(i19 * 4);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
        floatBufferAsFloatBuffer2.put(fArr2);
        floatBufferAsFloatBuffer2.position(0);
        ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(i17 * 2);
        byteBufferAllocateDirect3.order(ByteOrder.nativeOrder());
        ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect3.asShortBuffer();
        shortBufferAsShortBuffer.put(sArr3);
        shortBufferAsShortBuffer.position(0);
        mDAbsObject3D.setIndicesBuffer(shortBufferAsShortBuffer);
        mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
        mDAbsObject3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
        mDAbsObject3D.setNumIndices(i17);
    }
}