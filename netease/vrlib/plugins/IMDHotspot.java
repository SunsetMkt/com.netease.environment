package com.netease.vrlib.plugins;

import com.netease.vrlib.model.MDRay;

/* loaded from: classes5.dex */
public interface IMDHotspot {
    String getTitle();

    float hit(MDRay mDRay);

    void onEyeHitIn(long j);

    void onEyeHitOut();

    void onTouchHit(MDRay mDRay);

    void useTexture(int i);
}