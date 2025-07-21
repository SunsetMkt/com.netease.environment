package com.netease.ntunisdk.modules.base.entity;

import com.netease.ntunisdk.modules.base.BaseModules;

/* loaded from: classes.dex */
public class RegisterModuleEntity {
    public boolean isOnce;
    public BaseModules moduleObj;

    public RegisterModuleEntity(boolean z, BaseModules baseModules) {
        this.isOnce = true;
        this.isOnce = z;
        this.moduleObj = baseModules;
    }
}