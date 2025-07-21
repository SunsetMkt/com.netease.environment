package com.netease.vrlib.objects;

import android.content.Context;
import android.graphics.RectF;
import com.netease.vrlib.MD360Program;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDDome3D extends MDAbsObject3D {
    float mDegree;
    boolean mIsUpper;
    float mPrevRatio = 1.0f;
    RectF mTextureSize;
    float[] texCoordinates;

    public MDDome3D(RectF rectF, float f, boolean z) {
        this.mTextureSize = rectF;
        this.mDegree = f;
        this.mIsUpper = z;
    }

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    public void uploadTexCoordinateBufferIfNeed(MD360Program mD360Program, int i) {
        if (super.getTexCoordinateBuffer(i) == null) {
            return;
        }
        if (i == 0) {
            float fWidth = this.mTextureSize.width() / this.mTextureSize.height();
            if (fWidth != this.mPrevRatio) {
                int length = this.texCoordinates.length;
                float[] fArr = new float[length];
                for (int i2 = 0; i2 < length; i2 += 2) {
                    float[] fArr2 = this.texCoordinates;
                    fArr[i2] = ((fArr2[i2] - 0.5f) / fWidth) + 0.5f;
                    int i3 = i2 + 1;
                    fArr[i3] = fArr2[i3];
                }
                ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(length * 4);
                byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
                FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
                floatBufferAsFloatBuffer.put(fArr);
                floatBufferAsFloatBuffer.position(0);
                setTexCoordinateBuffer(0, floatBufferAsFloatBuffer);
                setTexCoordinateBuffer(1, floatBufferAsFloatBuffer);
                this.mPrevRatio = fWidth;
            }
        }
        super.uploadTexCoordinateBufferIfNeed(mD360Program, i);
    }

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    protected void executeLoad(Context context) {
        generateDome(this.mDegree, this.mIsUpper, this);
    }

    private static void generateDome(float f, boolean z, MDDome3D mDDome3D) {
        generateDome(18.0f, SDefine.fK, f, z, mDDome3D);
    }

    public static void generateDome(float f, int i, float f2, boolean z, MDDome3D mDDome3D) {
        float f3 = f2 / 360.0f;
        float f4 = i >> 1;
        float f5 = 1.0f / f4;
        float f6 = 1.0f / i;
        boolean z2 = true;
        int i2 = ((int) (f4 * f3)) + 1;
        int i3 = i + 1;
        int i4 = i2 * i3;
        int i5 = i4 * 3;
        float[] fArr = new float[i5];
        int i6 = i4 * 2;
        float[] fArr2 = new float[i6];
        int i7 = i4 * 6;
        short[] sArr = new short[i7];
        int i8 = z ? 1 : -1;
        short s = 0;
        int i9 = 0;
        int i10 = 0;
        while (s < i2) {
            short s2 = 0;
            while (s2 < i3) {
                double d = s2 * 6.2831855f * f6;
                float f7 = f6;
                int i11 = i3;
                double d2 = s * 3.1415927f * f5;
                int i12 = i5;
                float[] fArr3 = fArr;
                float fCos = ((float) (Math.cos(d) * Math.sin(d2))) * i8;
                float fSin = ((float) Math.sin(r4 - 1.5707964f)) * (-i8);
                float fSin2 = (float) (Math.sin(d) * Math.sin(d2));
                short[] sArr2 = sArr;
                double d3 = s;
                short s3 = s2;
                double d4 = f5;
                double d5 = f3;
                float fCos2 = (((float) (((Math.cos(d) * d3) * d4) / d5)) / 2.0f) + 0.5f;
                int i13 = i9 + 1;
                fArr2[i9] = (((float) (((Math.sin(d) * d3) * d4) / d5)) / 2.0f) + 0.5f;
                i9 = i13 + 1;
                fArr2[i13] = fCos2;
                int i14 = i10 + 1;
                fArr3[i10] = fCos * f;
                int i15 = i14 + 1;
                fArr3[i14] = fSin * f;
                i10 = i15 + 1;
                fArr3[i15] = fSin2 * f;
                s2 = (short) (s3 + 1);
                i7 = i7;
                i6 = i6;
                i8 = i8;
                i3 = i11;
                f6 = f7;
                s = s;
                i5 = i12;
                fArr = fArr3;
                sArr = sArr2;
            }
            s = (short) (s + 1);
            z2 = true;
        }
        int i16 = i3;
        int i17 = i7;
        int i18 = i5;
        float[] fArr4 = fArr;
        int i19 = i6;
        short[] sArr3 = sArr;
        int i20 = 0;
        for (short s4 = 0; s4 < i2 - 1; s4 = (short) (s4 + 1)) {
            short s5 = 0;
            while (s5 < i16 - 1) {
                int i21 = i20 + 1;
                int i22 = s4 * i16;
                sArr3[i20] = (short) (i22 + s5);
                int i23 = i21 + 1;
                int i24 = (s4 + 1) * i16;
                short s6 = (short) (i24 + s5);
                sArr3[i21] = s6;
                int i25 = i23 + 1;
                int i26 = s5 + 1;
                short s7 = (short) (i22 + i26);
                sArr3[i23] = s7;
                int i27 = i25 + 1;
                sArr3[i25] = s7;
                int i28 = i27 + 1;
                sArr3[i27] = s6;
                i20 = i28 + 1;
                sArr3[i28] = (short) (i24 + i26);
                s5 = (short) i26;
            }
        }
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i18 * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArr4);
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
        mDDome3D.setIndicesBuffer(shortBufferAsShortBuffer);
        mDDome3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer2);
        mDDome3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer2);
        mDDome3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
        mDDome3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
        mDDome3D.setNumIndices(i17);
        mDDome3D.texCoordinates = fArr2;
    }
}