package com.netease.vrlib.strategy.interactive;

import com.netease.vrlib.MD360Director;
import com.netease.vrlib.strategy.IModeStrategy;
import com.netease.vrlib.strategy.interactive.InteractiveModeManager;
import java.util.List;

/* loaded from: classes5.dex */
public abstract class AbsInteractiveStrategy implements IModeStrategy, IInteractiveMode {
    private InteractiveModeManager.Params params;

    public AbsInteractiveStrategy(InteractiveModeManager.Params params) {
        this.params = params;
    }

    public InteractiveModeManager.Params getParams() {
        return this.params;
    }

    protected List<MD360Director> getDirectorList() {
        return this.params.projectionModeManager.getDirectors();
    }
}