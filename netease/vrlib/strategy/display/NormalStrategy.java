package com.netease.vrlib.strategy.display;

import android.app.Activity;
import android.content.Context;

/* loaded from: classes6.dex */
public class NormalStrategy extends AbsDisplayStrategy {
    @Override // com.netease.vrlib.strategy.display.IDisplayMode
    public int getVisibleSize() {
        return 1;
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

    @Override // com.netease.vrlib.strategy.display.AbsDisplayStrategy, com.netease.vrlib.strategy.IModeStrategy
    public void onPause(Context context) {
    }

    @Override // com.netease.vrlib.strategy.display.AbsDisplayStrategy, com.netease.vrlib.strategy.IModeStrategy
    public void onResume(Context context) {
    }
}