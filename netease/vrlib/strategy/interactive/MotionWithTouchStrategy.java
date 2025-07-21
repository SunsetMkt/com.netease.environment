package com.netease.vrlib.strategy.interactive;

import android.content.res.Resources;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.strategy.interactive.InteractiveModeManager;

/* loaded from: classes5.dex */
public class MotionWithTouchStrategy extends MotionStrategy {
    private static final float sDamping = 0.2f;
    private static final float sDensity = Resources.getSystem().getDisplayMetrics().density;

    public MotionWithTouchStrategy(InteractiveModeManager.Params params) {
        super(params);
    }

    @Override // com.netease.vrlib.strategy.interactive.MotionStrategy, com.netease.vrlib.strategy.interactive.IInteractiveMode
    public boolean handleDrag(int i, int i2) {
        for (MD360Director mD360Director : getDirectorList()) {
            float deltaX = mD360Director.getDeltaX();
            float f = sDensity;
            mD360Director.setDeltaX(deltaX - ((i / f) * sDamping));
            mD360Director.setDeltaY(mD360Director.getDeltaY() - ((i2 / f) * sDamping));
        }
        return false;
    }
}