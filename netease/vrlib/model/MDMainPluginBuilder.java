package com.netease.vrlib.model;

import com.netease.vrlib.strategy.projection.ProjectionModeManager;
import com.netease.vrlib.texture.MD360Texture;

/* loaded from: classes5.dex */
public class MDMainPluginBuilder {
    private int contentType = 0;
    private ProjectionModeManager projectionModeManager;
    private MD360Texture texture;

    public MD360Texture getTexture() {
        return this.texture;
    }

    public int getContentType() {
        return this.contentType;
    }

    public ProjectionModeManager getProjectionModeManager() {
        return this.projectionModeManager;
    }

    public MDMainPluginBuilder setContentType(int i) {
        this.contentType = i;
        return this;
    }

    public MDMainPluginBuilder setTexture(MD360Texture mD360Texture) {
        this.texture = mD360Texture;
        return this;
    }

    public MDMainPluginBuilder setProjectionModeManager(ProjectionModeManager projectionModeManager) {
        this.projectionModeManager = projectionModeManager;
        return this;
    }
}