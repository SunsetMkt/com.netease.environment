package com.netease.vrlib.plugins;

import android.content.Context;
import android.opengl.GLES20;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360DirectorFactory;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.common.MDDirection;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.strategy.projection.ProjectionModeManager;
import com.netease.vrlib.texture.MD360Texture;
import com.xiaomi.gamecenter.sdk.pay.ReportCode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public class MDMultiFishEyePlugin extends MDAbsPlugin {
    private MD360Program mBitmapProgram;
    private MDMesh mConverterObject3D;
    private MDDrawingCache mDrawingCache;
    private MD360Director mFixedDirector;
    private MD360Program mProgram;
    private ProjectionModeManager mProjectionModeManager;
    private MD360Texture mTexture;

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    protected boolean removable() {
        return false;
    }

    public MDMultiFishEyePlugin(MDMainPluginBuilder mDMainPluginBuilder, float f, MDDirection mDDirection) {
        if (mDMainPluginBuilder != null) {
            this.mTexture = mDMainPluginBuilder.getTexture();
            this.mProgram = new MD360Program(mDMainPluginBuilder.getContentType());
            this.mBitmapProgram = new MD360Program(1);
            this.mProjectionModeManager = mDMainPluginBuilder.getProjectionModeManager();
            this.mFixedDirector = new MD360DirectorFactory.OrthogonalImpl().createDirector(0);
            this.mConverterObject3D = new MDMesh(f, mDDirection);
            this.mDrawingCache = new MDDrawingCache();
        }
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void init(Context context) {
        this.mProgram.build(context);
        this.mBitmapProgram.build(context);
        this.mTexture.create();
        MDObject3DHelper.loadObj(context, this.mConverterObject3D);
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void beforeRenderer(int i, int i2) {
        this.mFixedDirector.updateViewport(i, i2);
        this.mDrawingCache.bind(i, i2);
        drawConverter(i, i2);
        this.mDrawingCache.unbind();
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void renderer(int i, int i2, int i3, MD360Director mD360Director) {
        MDAbsObject3D object3D = this.mProjectionModeManager.getObject3D();
        if (object3D == null) {
            return;
        }
        mD360Director.updateViewport(i2, i3);
        this.mBitmapProgram.use();
        GLUtil.glCheck("MDPanoramaPlugin mProgram use");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.mDrawingCache.getTextureOutput());
        object3D.uploadVerticesBufferIfNeed(this.mBitmapProgram, i);
        object3D.uploadTexCoordinateBufferIfNeed(this.mBitmapProgram, i);
        mD360Director.shot(this.mBitmapProgram, getModelPosition());
        object3D.draw();
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void destroy() {
        this.mTexture = null;
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    protected MDPosition getModelPosition() {
        return this.mProjectionModeManager.getModelPosition();
    }

    private void drawConverter(int i, int i2) {
        GLES20.glClear(16640);
        GLUtil.glCheck("MDMultiFisheyeConvertLinePipe glClear");
        int i3 = i / 2;
        for (int i4 = 0; i4 < 2; i4++) {
            int i5 = i3 * i4;
            GLES20.glViewport(i5, 0, i3, i2);
            GLES20.glEnable(ReportCode.H);
            GLES20.glScissor(i5, 0, i3, i2);
            this.mProgram.use();
            this.mTexture.texture(this.mProgram);
            this.mFixedDirector.updateViewport(i3, i2);
            this.mConverterObject3D.uploadVerticesBufferIfNeed(this.mProgram, i4);
            this.mConverterObject3D.uploadTexCoordinateBufferIfNeed(this.mProgram, i4);
            this.mFixedDirector.shot(this.mProgram);
            this.mConverterObject3D.draw();
            GLES20.glDisable(ReportCode.H);
        }
    }

    private class MDMesh extends MDAbsObject3D {
        private static final String TAG = "MDMesh";
        private final MDDirection direction;
        private final float radius;

        public MDMesh(float f, MDDirection mDDirection) {
            this.radius = f;
            this.direction = mDDirection;
        }

        @Override // com.netease.vrlib.objects.MDAbsObject3D
        protected void executeLoad(Context context) {
            generateMesh(this);
        }

        private void generateMesh(MDAbsObject3D mDAbsObject3D) {
            short s = 16;
            float f = 1.0f;
            float f2 = 1.0f / 16;
            float[] fArr = new float[867];
            float[] fArr2 = new float[578];
            float[] fArr3 = new float[578];
            short[] sArr = new short[1734];
            short s2 = 0;
            int i = 0;
            int i2 = 0;
            while (true) {
                if (s2 >= 17) {
                    break;
                }
                short s3 = 0;
                for (short s4 = 17; s3 < s4; s4 = 17) {
                    int i3 = i2 + 1;
                    float f3 = s3 * f2;
                    fArr[i2] = (f3 * 2.0f) - f;
                    int i4 = i3 + 1;
                    float f4 = s2 * f2;
                    fArr[i3] = (2.0f * f4) - f;
                    int i5 = i4 + 1;
                    fArr[i4] = -8.0f;
                    float[] fArr4 = fArr;
                    double d = (f4 - 0.5f) * 3.1415927f;
                    float f5 = f2;
                    double d2 = (f3 - 0.5f) * 3.1415927f;
                    short s5 = s2;
                    float fCos = (float) (Math.cos(d) * Math.sin(d2));
                    float fCos2 = (float) (Math.cos(d2) * Math.cos(d));
                    short s6 = s3;
                    float fAtan2 = (float) Math.atan2((float) Math.sin(d), fCos);
                    double d3 = 0.5f;
                    double dAtan2 = (this.radius * ((float) Math.atan2(Math.sqrt((fCos * fCos) + (r3 * r3)), fCos2))) / 3.1415927f;
                    double d4 = fAtan2;
                    int i6 = i;
                    float fCos3 = (float) (d3 + (Math.cos(d4) * dAtan2));
                    float fSin = (float) (d3 + (dAtan2 * Math.sin(d4)));
                    if (this.direction == MDDirection.HORIZONTAL) {
                        int i7 = i6 * 2;
                        float f6 = fCos3 * 0.5f;
                        fArr2[i7] = f6;
                        int i8 = i7 + 1;
                        fArr2[i8] = fSin;
                        fArr3[i7] = f6 + 0.5f;
                        fArr3[i8] = fSin;
                    } else {
                        int i9 = i6 * 2;
                        fArr2[i9] = fCos3;
                        int i10 = i9 + 1;
                        float f7 = fSin * 0.5f;
                        fArr2[i10] = f7;
                        fArr3[i9] = fCos3;
                        fArr3[i10] = f7 + 0.5f;
                    }
                    i = i6 + 1;
                    s3 = (short) (s6 + 1);
                    s2 = s5;
                    i2 = i5;
                    fArr = fArr4;
                    f2 = f5;
                    f = 1.0f;
                }
                s2 = (short) (s2 + 1);
                s = 16;
                f = 1.0f;
            }
            float[] fArr5 = fArr;
            short s7 = s;
            int i11 = 0;
            for (short s8 = 0; s8 < s7; s8 = (short) (s8 + 1)) {
                short s9 = 0;
                while (s9 < s7) {
                    int i12 = s8 * 17;
                    int i13 = s9 + 1;
                    short s10 = (short) (i12 + i13);
                    int i14 = (s8 + 1) * 17;
                    short s11 = (short) (i14 + s9);
                    short s12 = (short) (i12 + s9);
                    short s13 = (short) (i14 + i13);
                    int i15 = i11 + 1;
                    sArr[i11] = s10;
                    int i16 = i15 + 1;
                    sArr[i15] = s11;
                    int i17 = i16 + 1;
                    sArr[i16] = s12;
                    int i18 = i17 + 1;
                    sArr[i17] = s10;
                    int i19 = i18 + 1;
                    sArr[i18] = s13;
                    i11 = i19 + 1;
                    sArr[i19] = s11;
                    s9 = (short) i13;
                }
            }
            ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(3468);
            byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
            floatBufferAsFloatBuffer.put(fArr5);
            floatBufferAsFloatBuffer.position(0);
            ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(2312);
            byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer2 = byteBufferAllocateDirect2.asFloatBuffer();
            floatBufferAsFloatBuffer2.put(fArr2);
            floatBufferAsFloatBuffer2.position(0);
            ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(2312);
            byteBufferAllocateDirect3.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer3 = byteBufferAllocateDirect3.asFloatBuffer();
            floatBufferAsFloatBuffer3.put(fArr3);
            floatBufferAsFloatBuffer3.position(0);
            ByteBuffer byteBufferAllocateDirect4 = ByteBuffer.allocateDirect(3468);
            byteBufferAllocateDirect4.order(ByteOrder.nativeOrder());
            ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect4.asShortBuffer();
            shortBufferAsShortBuffer.put(sArr);
            shortBufferAsShortBuffer.position(0);
            mDAbsObject3D.setIndicesBuffer(shortBufferAsShortBuffer);
            mDAbsObject3D.setTexCoordinateBuffer(0, floatBufferAsFloatBuffer2);
            mDAbsObject3D.setTexCoordinateBuffer(1, floatBufferAsFloatBuffer3);
            mDAbsObject3D.setVerticesBuffer(0, floatBufferAsFloatBuffer);
            mDAbsObject3D.setVerticesBuffer(1, floatBufferAsFloatBuffer);
            mDAbsObject3D.setNumIndices(1734);
        }
    }
}