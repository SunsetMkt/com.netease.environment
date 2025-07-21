package com.netease.vrlib.plugins;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.common.VRUtil;
import com.netease.vrlib.model.BarrelDistortionConfig;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.strategy.display.DisplayModeManager;
import com.xiaomi.gamecenter.sdk.pay.ReportCode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDBarrelDistortionLinePipe extends MDAbsLinePipe {
    private BarrelDistortionConfig mConfiguration;
    private DisplayModeManager mDisplayModeManager;
    private boolean mEnabled;
    private MD360Program mProgram = new MD360Program(1);
    private MD360Director mDirector = new MD360DirectorFactory.OrthogonalImpl().createDirector(0);
    private MDBarrelDistortionMesh object3D = new MDBarrelDistortionMesh();
    private MDDrawingCache mDrawingCache = new MDDrawingCache();

    public MDBarrelDistortionLinePipe(DisplayModeManager displayModeManager) {
        this.mDisplayModeManager = displayModeManager;
        this.mConfiguration = displayModeManager.getBarrelDistortionConfig();
    }

    @Override // com.netease.vrlib.plugins.MDAbsLinePipe
    public void init(Context context) {
        this.mProgram.build(context);
        MDObject3DHelper.loadObj(context, this.object3D);
    }

    @Override // com.netease.vrlib.plugins.MDAbsLinePipe
    public void takeOver(int i, int i2, int i3) {
        boolean zIsAntiDistortionEnabled = this.mDisplayModeManager.isAntiDistortionEnabled();
        this.mEnabled = zIsAntiDistortionEnabled;
        if (zIsAntiDistortionEnabled) {
            this.mDrawingCache.bind(i, i2);
            this.mDirector.updateViewport(i, i2);
            this.object3D.setMode(i3);
            GLES20.glClear(16640);
            GLUtil.glCheck("MDBarrelDistortionLinePipe glClear");
        }
    }

    @Override // com.netease.vrlib.plugins.MDAbsLinePipe
    public void commit(int i, int i2, int i3) {
        if (this.mEnabled) {
            this.mDrawingCache.unbind();
            int i4 = i / i3;
            for (int i5 = 0; i5 < i3; i5++) {
                int i6 = i4 * i5;
                GLES20.glViewport(i6, 0, i4, i2);
                GLES20.glEnable(ReportCode.H);
                GLES20.glScissor(i6, 0, i4, i2);
                draw(i5);
                GLES20.glDisable(ReportCode.H);
            }
        }
    }

    private void draw(int i) {
        this.mProgram.use();
        GLUtil.glCheck("MDBarrelDistortionLinePipe mProgram use");
        this.object3D.uploadVerticesBufferIfNeed(this.mProgram, i);
        this.object3D.uploadTexCoordinateBufferIfNeed(this.mProgram, i);
        this.mDirector.shot(this.mProgram);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.mDrawingCache.getTextureOutput());
        this.object3D.draw();
    }

    private class MDBarrelDistortionMesh extends MDAbsObject3D {
        private static final String TAG = "MDBarrelDistortionMesh";
        private int mode;
        private FloatBuffer singleTexCoordinateBuffer;

        public MDBarrelDistortionMesh() {
        }

        @Override // com.netease.vrlib.objects.MDAbsObject3D
        public FloatBuffer getTexCoordinateBuffer(int i) {
            int i2 = this.mode;
            if (i2 == 1) {
                return this.singleTexCoordinateBuffer;
            }
            if (i2 == 2) {
                return super.getTexCoordinateBuffer(i);
            }
            return null;
        }

        @Override // com.netease.vrlib.objects.MDAbsObject3D
        protected void executeLoad(Context context) {
            generateMesh(this);
        }

        private void generateMesh(MDAbsObject3D mDAbsObject3D) {
            short s = 10;
            float f = 1.0f / 10;
            float[] fArr = new float[363];
            float[] fArr2 = new float[242];
            float[] fArr3 = new float[242];
            float[] fArr4 = new float[242];
            short[] sArr = new short[726];
            short s2 = 0;
            int i = 0;
            int i2 = 0;
            while (true) {
                if (s2 >= 11) {
                    break;
                }
                short s3 = 0;
                for (short s4 = 11; s3 < s4; s4 = 11) {
                    int i3 = i + 1;
                    float f2 = s3 * f;
                    fArr2[i] = f2;
                    float f3 = s2 * f;
                    fArr2[i3] = f3;
                    float f4 = f2 * 0.5f;
                    fArr3[i] = f4;
                    fArr3[i3] = f3;
                    fArr4[i] = f4 + 0.5f;
                    fArr4[i3] = f3;
                    int i4 = i2 + 1;
                    fArr[i2] = (f2 * 2.0f) - 1.0f;
                    int i5 = i4 + 1;
                    fArr[i4] = (f3 * 2.0f) - 1.0f;
                    i2 = i5 + 1;
                    fArr[i5] = -8.0f;
                    s3 = (short) (s3 + 1);
                    i = i3 + 1;
                }
                s2 = (short) (s2 + 1);
            }
            applyBarrelDistortion(121, fArr);
            short s5 = 0;
            int i6 = 0;
            while (s5 < s) {
                short s6 = 0;
                while (s6 < s) {
                    int i7 = s5 * 11;
                    int i8 = s6 + 1;
                    short s7 = (short) (i7 + i8);
                    int i9 = (s5 + 1) * 11;
                    short s8 = (short) (i9 + s6);
                    short s9 = (short) (i7 + s6);
                    short s10 = (short) (i9 + i8);
                    int i10 = i6 + 1;
                    sArr[i6] = s7;
                    int i11 = i10 + 1;
                    sArr[i10] = s8;
                    int i12 = i11 + 1;
                    sArr[i11] = s9;
                    int i13 = i12 + 1;
                    sArr[i12] = s7;
                    int i14 = i13 + 1;
                    sArr[i13] = s10;
                    i6 = i14 + 1;
                    sArr[i14] = s8;
                    s6 = (short) i8;
                    s = 10;
                }
                s5 = (short) (s5 + 1);
                s = 10;
            }
            ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(1452);
            byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
            floatBufferAsFloatBuffer.put(fArr);
            floatBufferAsFloatBuffer.position(0);
            ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(968);
            byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
            floatBufferAsFloatBuffer2.put(fArr2);
            floatBufferAsFloatBuffer2.position(0);
            ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(968);
            byteBufferAllocateDirect3.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer3 = byteBufferAllocateDirect3.asFloatBuffer();
            floatBufferAsFloatBuffer3.put(fArr3);
            floatBufferAsFloatBuffer3.position(0);
            ByteBuffer byteBufferAllocateDirect4 = ByteBuffer.allocateDirect(968);
            byteBufferAllocateDirect4.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer4 = byteBufferAllocateDirect4.asFloatBuffer();
            floatBufferAsFloatBuffer4.put(fArr4);
            floatBufferAsFloatBuffer4.position(0);
            ByteBuffer byteBufferAllocateDirect5 = ByteBuffer.allocateDirect(1452);
            byteBufferAllocateDirect5.order(ByteOrder.nativeOrder());
            ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect5.asShortBuffer();
            shortBufferAsShortBuffer.put(sArr);
            shortBufferAsShortBuffer.position(0);
            mDAbsObject3D.setIndicesBuffer(shortBufferAsShortBuffer);
            mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer3);
            mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer4);
            mDAbsObject3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
            mDAbsObject3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
            mDAbsObject3D.setNumIndices(726);
            this.singleTexCoordinateBuffer = floatBufferAsFloatBuffer2;
        }

        private void applyBarrelDistortion(int i, float[] fArr) {
            PointF pointF = new PointF();
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = i2 * 3;
                int i4 = i3 + 1;
                pointF.set(fArr[i3], fArr[i4]);
                VRUtil.barrelDistortion(MDBarrelDistortionLinePipe.this.mConfiguration.getParamA(), MDBarrelDistortionLinePipe.this.mConfiguration.getParamB(), MDBarrelDistortionLinePipe.this.mConfiguration.getParamC(), pointF);
                fArr[i3] = pointF.x * MDBarrelDistortionLinePipe.this.mConfiguration.getScale();
                fArr[i4] = pointF.y * MDBarrelDistortionLinePipe.this.mConfiguration.getScale();
            }
        }

        public void setMode(int i) {
            this.mode = i;
        }
    }
}