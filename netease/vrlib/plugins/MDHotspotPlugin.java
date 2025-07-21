package com.netease.vrlib.plugins;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.util.SparseArray;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.common.VRUtil;
import com.netease.vrlib.model.MDHotspotBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.model.MDRay;
import com.netease.vrlib.model.MDVector3D;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.objects.MDObject3DHelper;
import com.netease.vrlib.objects.MDPlane;
import com.netease.vrlib.texture.MD360Texture;
import java.nio.FloatBuffer;
import java.util.LinkedList;

/* loaded from: classes5.dex */
public class MDHotspotPlugin extends MDAbsPlugin implements IMDHotspot {
    private static final String TAG = "MDSimplePlugin";
    private MDVRLibrary.ITouchPickListener clickListener;
    private int mCurrentTextureKey = 0;
    private MDAbsObject3D object3D;
    private MD360Program program;
    private RectF size;
    private SparseArray<MD360Texture> textures;
    private String title;

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void beforeRenderer(int i, int i2) {
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void destroy() {
    }

    @Override // com.netease.vrlib.plugins.IMDHotspot
    public void onEyeHitIn(long j) {
    }

    @Override // com.netease.vrlib.plugins.IMDHotspot
    public void onEyeHitOut() {
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    protected boolean removable() {
        return true;
    }

    public MDHotspotPlugin(MDHotspotBuilder mDHotspotBuilder) {
        this.textures = mDHotspotBuilder.textures;
        this.size = new RectF(0.0f, 0.0f, mDHotspotBuilder.width, mDHotspotBuilder.height);
        this.clickListener = mDHotspotBuilder.clickListener;
        setTitle(mDHotspotBuilder.title);
        setModelPosition(mDHotspotBuilder.position == null ? MDPosition.sOriginalPosition : mDHotspotBuilder.position);
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void init(Context context) {
        MD360Program mD360Program = new MD360Program(1);
        this.program = mD360Program;
        mD360Program.build(context);
        for (int i = 0; i < this.textures.size(); i++) {
            this.textures.valueAt(i).create();
        }
        MDPlane mDPlane = new MDPlane(this.size);
        this.object3D = mDPlane;
        MDObject3DHelper.loadObj(context, mDPlane);
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void renderer(int i, int i2, int i3, MD360Director mD360Director) {
        MD360Texture mD360Texture = this.textures.get(this.mCurrentTextureKey);
        if (mD360Texture == null) {
            return;
        }
        mD360Texture.texture(this.program);
        if (mD360Texture.isReady()) {
            mD360Director.updateViewport(i2, i3);
            this.program.use();
            GLUtil.glCheck("MDSimplePlugin mProgram use");
            this.object3D.uploadVerticesBufferIfNeed(this.program, i);
            this.object3D.uploadTexCoordinateBufferIfNeed(this.program, i);
            mD360Director.shot(this.program, getModelPosition());
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 771);
            this.object3D.draw();
            GLES20.glDisable(3042);
        }
    }

    @Override // com.netease.vrlib.plugins.IMDHotspot
    public float hit(MDRay mDRay) {
        float fIntersectTriangle;
        MDAbsObject3D mDAbsObject3D = this.object3D;
        float f = Float.MAX_VALUE;
        if (mDAbsObject3D == null || mDAbsObject3D.getVerticesBuffer(0) == null) {
            return Float.MAX_VALUE;
        }
        float[] matrix = getModelPosition().getMatrix();
        LinkedList linkedList = new LinkedList();
        FloatBuffer verticesBuffer = this.object3D.getVerticesBuffer(0);
        int iCapacity = verticesBuffer.capacity() / 3;
        for (int i = 0; i < iCapacity; i++) {
            MDVector3D mDVector3D = new MDVector3D();
            int i2 = i * 3;
            mDVector3D.setX(verticesBuffer.get(i2)).setY(verticesBuffer.get(i2 + 1)).setZ(verticesBuffer.get(i2 + 2));
            mDVector3D.multiplyMV(matrix);
            linkedList.add(mDVector3D);
        }
        if (linkedList.size() == 4) {
            float fIntersectTriangle2 = VRUtil.intersectTriangle(mDRay, (MDVector3D) linkedList.get(0), (MDVector3D) linkedList.get(1), (MDVector3D) linkedList.get(2));
            fIntersectTriangle = VRUtil.intersectTriangle(mDRay, (MDVector3D) linkedList.get(1), (MDVector3D) linkedList.get(2), (MDVector3D) linkedList.get(3));
            f = fIntersectTriangle2;
        } else {
            fIntersectTriangle = Float.MAX_VALUE;
        }
        return Math.min(f, fIntersectTriangle);
    }

    @Override // com.netease.vrlib.plugins.IMDHotspot
    public void onTouchHit(MDRay mDRay) {
        MDVRLibrary.ITouchPickListener iTouchPickListener = this.clickListener;
        if (iTouchPickListener != null) {
            iTouchPickListener.onHotspotHit(this, mDRay);
        }
    }

    @Override // com.netease.vrlib.plugins.IMDHotspot
    public String getTitle() {
        return this.title;
    }

    @Override // com.netease.vrlib.plugins.IMDHotspot
    public void useTexture(int i) {
        this.mCurrentTextureKey = i;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}