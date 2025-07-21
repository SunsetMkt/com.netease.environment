package com.netease.ntunisdk.modules.base.call;

import com.netease.ntunisdk.modules.base.InnerModulesManager;

/* loaded from: classes.dex */
public abstract class DefaultModulesCall implements IModulesCall {
    protected InnerModulesManager innerModulesManager;

    @Override // com.netease.ntunisdk.modules.base.call.IModulesCall
    public void callback(String str, String str2, String str3, String str4) {
        this.innerModulesManager.extendFuncCallback(str, str2, str3, str4);
    }
}