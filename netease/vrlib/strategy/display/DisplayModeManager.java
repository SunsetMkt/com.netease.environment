package com.netease.vrlib.strategy.display;

import com.netease.vrlib.common.MDGLHandler;
import com.netease.vrlib.model.BarrelDistortionConfig;
import com.netease.vrlib.strategy.ModeManager;

/* loaded from: classes6.dex */
public class DisplayModeManager extends ModeManager<AbsDisplayStrategy> implements IDisplayMode {
    public static int[] sModes = {101, 102};
    private boolean antiDistortionEnabled;
    private BarrelDistortionConfig barrelDistortionConfig;

    public DisplayModeManager(int i, MDGLHandler mDGLHandler) {
        super(i, mDGLHandler);
    }

    @Override // com.netease.vrlib.strategy.ModeManager
    protected int[] getModes() {
        return sModes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.netease.vrlib.strategy.ModeManager
    public AbsDisplayStrategy createStrategy(int i) {
        if (i == 102) {
            return new GlassStrategy();
        }
        return new NormalStrategy();
    }

    @Override // com.netease.vrlib.strategy.display.IDisplayMode
    public int getVisibleSize() {
        return getStrategy().getVisibleSize();
    }

    public void setAntiDistortionEnabled(boolean z) {
        this.antiDistortionEnabled = z;
    }

    public boolean isAntiDistortionEnabled() {
        return this.antiDistortionEnabled;
    }

    public void setBarrelDistortionConfig(BarrelDistortionConfig barrelDistortionConfig) {
        this.barrelDistortionConfig = barrelDistortionConfig;
    }

    public BarrelDistortionConfig getBarrelDistortionConfig() {
        return this.barrelDistortionConfig;
    }
}