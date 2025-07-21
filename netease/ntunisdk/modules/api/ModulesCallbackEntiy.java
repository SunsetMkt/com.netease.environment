package com.netease.ntunisdk.modules.api;

import com.netease.ntunisdk.modules.base.utils.IdGenerator;

/* loaded from: classes.dex */
public class ModulesCallbackEntiy {
    public ModulesCallback cb;
    public int cbId = IdGenerator.getUniqueId();
    public boolean recPush;
    public String source;

    public ModulesCallbackEntiy(String str, boolean z, ModulesCallback modulesCallback) {
        this.recPush = false;
        this.source = str;
        this.recPush = z;
        this.cb = modulesCallback;
    }
}