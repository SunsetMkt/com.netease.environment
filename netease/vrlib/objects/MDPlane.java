package com.netease.vrlib.objects;

import android.content.Context;
import android.graphics.RectF;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.strategy.projection.PlaneProjection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDPlane extends MDAbsObject3D {
    private static final String TAG = "MDPlane";
    private PlaneProjection.PlaneScaleCalculator mCalculator;
    private float mPrevRatio;
    private RectF mSize;

    private int getNumColumn() {
        return 1;
    }

    private int getNumRow() {
        return 1;
    }

    private MDPlane(PlaneProjection.PlaneScaleCalculator planeScaleCalculator, RectF rectF) {
        this.mCalculator = planeScaleCalculator;
        this.mSize = rectF;
    }

    public MDPlane(PlaneProjection.PlaneScaleCalculator planeScaleCalculator) {
        this(planeScaleCalculator, new RectF(0.0f, 0.0f, 1.0f, 1.0f));
    }

    public MDPlane(RectF rectF) {
        this(new PlaneProjection.PlaneScaleCalculator(209, new RectF(0.0f, 0.0f, 100.0f, 100.0f)), rectF);
    }

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    protected void executeLoad(Context context) {
        generateMesh(this);
    }

    @Override // com.netease.vrlib.objects.MDAbsObject3D
    public void uploadVerticesBufferIfNeed(MD360Program mD360Program, int i) {
        if (super.getVerticesBuffer(i) == null) {
            return;
        }
        if (i == 0) {
            float textureRatio = this.mCalculator.getTextureRatio();
            if (textureRatio != this.mPrevRatio) {
                float[] fArrGenerateVertex = generateVertex();
                ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(fArrGenerateVertex.length * 4);
                byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
                FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
                floatBufferAsFloatBuffer.put(fArrGenerateVertex);
                floatBufferAsFloatBuffer.position(0);
                setVerticesBuffer(0, floatBufferAsFloatBuffer);
                setVerticesBuffer(1, floatBufferAsFloatBuffer);
                this.mPrevRatio = textureRatio;
            }
        }
        super.uploadVerticesBufferIfNeed(mD360Program, i);
    }

    private float[] generateVertex() {
        this.mCalculator.calculate();
        this.mPrevRatio = this.mCalculator.getTextureRatio();
        float textureWidth = this.mCalculator.getTextureWidth() * this.mSize.width();
        float textureHeight = this.mCalculator.getTextureHeight() * this.mSize.height();
        float[] fArr = new float[getNumPoint() * 3];
        int numRow = getNumRow();
        int numColumn = getNumColumn();
        float f = 1.0f / numRow;
        float f2 = 1.0f / numColumn;
        int i = 0;
        for (short s = 0; s < numRow + 1; s = (short) (s + 1)) {
            short s2 = 0;
            while (s2 < numColumn + 1) {
                int i2 = i + 1;
                fArr[i] = ((s2 * f2) - 0.5f) * textureWidth;
                int i3 = i2 + 1;
                fArr[i2] = ((s * f) - 0.5f) * textureHeight;
                fArr[i3] = 0;
                s2 = (short) (s2 + 1);
                i = i3 + 1;
            }
        }
        return fArr;
    }

    private float[] generateTexcoords() {
        float[] fArr = new float[getNumPoint() * 2];
        int numRow = getNumRow();
        int numColumn = getNumColumn();
        float f = 1.0f / numRow;
        float f2 = 1.0f / numColumn;
        int i = 0;
        for (short s = 0; s < numRow + 1; s = (short) (s + 1)) {
            for (short s2 = 0; s2 < numColumn + 1; s2 = (short) (s2 + 1)) {
                int i2 = i + 1;
                fArr[i] = s2 * f2;
                i = i2 + 1;
                fArr[i2] = 1.0f - (s * f);
            }
        }
        return fArr;
    }

    private void generateMesh(MDAbsObject3D mDAbsObject3D) {
        int numRow = getNumRow();
        int numColumn = getNumColumn();
        float[] fArrGenerateVertex = generateVertex();
        float[] fArrGenerateTexcoords = generateTexcoords();
        int numPoint = getNumPoint() * 6;
        short[] sArr = new short[numPoint];
        int i = numColumn + 1;
        int i2 = 0;
        for (short s = 0; s < numRow; s = (short) (s + 1)) {
            short s2 = 0;
            while (s2 < numColumn) {
                int i3 = s * i;
                int i4 = s2 + 1;
                short s3 = (short) (i3 + i4);
                int i5 = (s + 1) * i;
                short s4 = (short) (i5 + s2);
                short s5 = (short) (i3 + s2);
                short s6 = (short) (i5 + i4);
                int i6 = i2 + 1;
                sArr[i2] = s3;
                int i7 = i6 + 1;
                sArr[i6] = s4;
                int i8 = i7 + 1;
                sArr[i7] = s5;
                int i9 = i8 + 1;
                sArr[i8] = s3;
                int i10 = i9 + 1;
                sArr[i9] = s6;
                i2 = i10 + 1;
                sArr[i10] = s4;
                s2 = (short) i4;
            }
        }
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(fArrGenerateVertex.length * 4);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
        floatBufferAsFloatBuffer.put(fArrGenerateVertex);
        floatBufferAsFloatBuffer.position(0);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(fArrGenerateTexcoords.length * 4);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
        floatBufferAsFloatBuffer2.put(fArrGenerateTexcoords);
        floatBufferAsFloatBuffer2.position(0);
        ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(numPoint * 2);
        byteBufferAllocateDirect3.order(ByteOrder.nativeOrder());
        ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect3.asShortBuffer();
        shortBufferAsShortBuffer.put(sArr);
        shortBufferAsShortBuffer.position(0);
        mDAbsObject3D.setIndicesBuffer(shortBufferAsShortBuffer);
        mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer2);
        mDAbsObject3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
        mDAbsObject3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
        mDAbsObject3D.setNumIndices(numPoint);
    }

    private int getNumPoint() {
        return (getNumRow() + 1) * (getNumColumn() + 1);
    }
}