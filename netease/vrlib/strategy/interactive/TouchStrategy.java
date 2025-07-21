package com.netease.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import com.netease.vrlib.MD360Director;
import com.netease.vrlib.strategy.interactive.InteractiveModeManager;
import java.util.Iterator;

/* loaded from: classes5.dex */
public class TouchStrategy extends AbsInteractiveStrategy {
    private static final String TAG = "TouchStrategy";
    private static final float sDamping = 0.2f;
    private static final float sDensity = Resources.getSystem().getDisplayMetrics().density;

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public boolean isSupport(Activity activity) {
        return true;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void off(Activity activity) {
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public void onOrientationChanged(Activity activity) {
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void onPause(Context context) {
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void onResume(Context context) {
    }

    public TouchStrategy(InteractiveModeManager.Params params) {
        super(params);
    }

    @Override // com.netease.vrlib.strategy.interactive.IInteractiveMode
    public boolean handleDrag(int i, int i2) {
        for (MD360Director mD360Director : getDirectorList()) {
            float deltaX = mD360Director.getDeltaX();
            float f = sDensity;
            mD360Director.setDeltaX(deltaX - ((i / f) * sDamping));
            mD360Director.setDeltaY(mD360Director.getDeltaY() - ((i2 / f) * sDamping));
        }
        return false;
    }

    @Override // com.netease.vrlib.strategy.IModeStrategy
    public void on(Activity activity) {
        Iterator<MD360Director> it = getDirectorList().iterator();
        while (it.hasNext()) {
            it.next().reset();
        }
    }
}