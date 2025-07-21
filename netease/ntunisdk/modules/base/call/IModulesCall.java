package com.netease.ntunisdk.modules.base.call;

import com.netease.ntunisdk.modules.base.BaseModules;

/* loaded from: classes.dex */
public interface IModulesCall {
    String callSDKOthersModules(String str, String str2, String str3, Object... objArr);

    void callback(String str, String str2, String str3, String str4);

    void notiSDKOthersModules(String str, String str2, String str3);

    void registerModulesListener(String str, boolean z, BaseModules baseModules);

    void unRegisterModulesListener(String str, BaseModules baseModules);
}