package com.netease.vrlib.plugins;

import android.content.Context;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.model.MDMainPluginBuilder;
import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;
import com.netease.vrlib.strategy.projection.ProjectionModeManager;
import com.netease.vrlib.texture.MD360Texture;

/* loaded from: classes5.dex */
public class MDPanoramaPlugin extends MDAbsPlugin {
    private MD360Program mProgram;
    private ProjectionModeManager mProjectionModeManager;
    private MD360Texture mTexture;

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void beforeRenderer(int i, int i2) {
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    protected boolean removable() {
        return false;
    }

    public MDPanoramaPlugin(MDMainPluginBuilder mDMainPluginBuilder) {
        if (mDMainPluginBuilder != null) {
            this.mTexture = mDMainPluginBuilder.getTexture();
            this.mProgram = new MD360Program(mDMainPluginBuilder.getContentType());
            this.mProjectionModeManager = mDMainPluginBuilder.getProjectionModeManager();
        }
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void init(Context context) {
        this.mProgram.build(context);
        this.mTexture.create();
    }

    @Override // com.netease.vrlib.plugins.MDAbsPlugin
    public void renderer(int i, int i2, int i3, MD360Director mD360Director) {
        MDAbsObject3D object3D = this.mProjectionModeManager.getObject3D();
        if (object3D == null || this.mTexture == null) {
            return;
        }
        mD360Director.updateViewport(i2, i3);
        this.mProgram.use();
        GLUtil.glCheck("MDPanoramaPlugin mProgram use");
        this.mTexture.texture(this.mProgram);
        object3D.uploadVerticesBufferIfNeed(this.mProgram, i);
        object3D.uploadTexCoordinateBufferIfNeed(this.mProgram, i);
        mD360Director.shot(this.mProgram, getModelPosition());
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
}