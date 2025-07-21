package com.netease.ntunisdk;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.netease.ntunisdk.base.SdkApplication;
import com.netease.ntunisdk.modules.api.ModulesManager;

/* loaded from: classes2.dex */
public class ApplicationLifeCycle extends SdkApplication {
    @Override // com.netease.ntunisdk.base.SdkApplication
    public String getChannel() {
        return "lifeCycle";
    }

    public ApplicationLifeCycle(Context context) {
        super(context);
    }

    @Override // com.netease.ntunisdk.base.SdkApplication
    public void handleOnApplicationOnCreate(Context context, Application application) {
        Log.d("ApplicationLifeCycle", "handleOnApplicationOnCreate");
        ModulesManager.getInst().init(application);
    }
}