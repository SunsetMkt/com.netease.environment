package com.netease.ntunisdk.modules.applicationlifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/* loaded from: classes5.dex */
public class LifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private ApplicationLifecycleModule applicationLifecycleModule;
    private int mFrontNums = 0;

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public LifecycleCallback(ApplicationLifecycleModule applicationLifecycleModule) {
        this.applicationLifecycleModule = applicationLifecycleModule;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.applicationLifecycleModule.callActivityLife(1, activity.getClass().getSimpleName());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        int i = this.mFrontNums + 1;
        this.mFrontNums = i;
        if (i == 1) {
            this.applicationLifecycleModule.callbackRes(true);
        }
        this.applicationLifecycleModule.callActivityLife(2, activity.getClass().getSimpleName());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        int i = this.mFrontNums - 1;
        this.mFrontNums = i;
        if (i == 0) {
            this.applicationLifecycleModule.callbackRes(false);
        }
        this.applicationLifecycleModule.callActivityLife(3, activity.getClass().getSimpleName());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        this.applicationLifecycleModule.callActivityLife(4, activity.getClass().getSimpleName());
    }
}