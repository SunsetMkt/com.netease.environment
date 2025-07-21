package com.netease.ntunisdk.modules.base;

import android.content.Context;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.base.utils.LogModule;

/* loaded from: classes.dex */
public abstract class BaseModules extends Lifecycle {
    private static final String TAG = "UniSDK BaseModules";
    protected Context context;
    protected IModulesCall modulesCall;

    public abstract String extendFunc(String str, String str2, String str3, Object... objArr);

    public <T> T extendFuncGen(String str, String str2, String str3, Object... objArr) {
        return null;
    }

    public abstract String getModuleName();

    public abstract String getVersion();

    public BaseModules(Context context, IModulesCall iModulesCall) {
        this.context = context;
        this.modulesCall = iModulesCall;
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    public void callback(String str, String str2, String str3) {
        this.modulesCall.callback(str, str2, getModuleName(), str3);
        notiSDKOthersModules(str2, str3);
    }

    protected void notiSDKOthersModules(String str, String str2) {
        this.modulesCall.notiSDKOthersModules(str, getModuleName(), str2);
    }

    protected void registerModuleListener(String str) {
        registerModuleListener(str, true);
    }

    protected void registerModuleListener(String str, boolean z) {
        this.modulesCall.registerModulesListener(str, z, this);
    }

    protected void unRegisterModuleListener(String str) {
        this.modulesCall.unRegisterModulesListener(str, this);
    }

    public void receiveOthersModulesMsg(String str, String str2) {
        LogModule.d(TAG, String.format("%s receive msg from %s, data:%s", getModuleName(), str, str2));
    }

    public String callSDKOthersModules(String str, String str2) {
        return callSDKOthersModules(str, str2, new Object[0]);
    }

    public String callSDKOthersModules(String str, String str2, Object... objArr) {
        LogModule.d(TAG, String.format("%s module call %s module, data:%s", getModuleName(), str, str2));
        return this.modulesCall.callSDKOthersModules(getModuleName(), str, str2, objArr);
    }
}