package com.netease.vrlib.strategy.display;

import android.app.Activity;

/* loaded from: classes6.dex */
public class GlassStrategy extends AbsDisplayStrategy {
    @Override // com.netease.vrlib.strategy.display.IDisplayMode
    public int getVisibleSize() {
        return 2;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
    }
}