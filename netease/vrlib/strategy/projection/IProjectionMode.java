package com.netease.vrlib.strategy.projection;

import com.netease.vrlib.model.MDPosition;
import com.netease.vrlib.objects.MDAbsObject3D;

/* loaded from: classes5.dex */
public interface IProjectionMode {
    MDPosition getModelPosition();

    MDAbsObject3D getObject3D();
}