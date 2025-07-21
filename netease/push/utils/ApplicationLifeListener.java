package com.netease.push.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/* loaded from: classes3.dex */
public abstract class ApplicationLifeListener {
    private int mFrontNums = 0;
    private int mActiveNums = 0;
    private String frontActivity = "";
    private Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() { // from class: com.netease.push.utils.ApplicationLifeListener.1
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.i("ApplicationLifeListener", "onActivityCreated:" + ApplicationLifeListener.this.mActiveNums);
            ApplicationLifeListener.access$008(ApplicationLifeListener.this);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
            Log.i("ApplicationLifeListener", "activity:" + activity.toString());
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
            Log.i("ApplicationLifeListener", "activity:" + activity.toString());
            ApplicationLifeListener.this.frontActivity = activity.toString();
            Log.i("ApplicationLifeListener", "onEnterFront");
            ApplicationLifeListener.this.onEnterFront();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            Log.i("ApplicationLifeListener", "activity:" + activity.toString());
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
            Log.i("ApplicationLifeListener", "activity:" + activity.toString());
            if (ApplicationLifeListener.this.frontActivity.equals(activity.toString())) {
                Log.i("ApplicationLifeListener", "onEnterBackground");
                ApplicationLifeListener.this.onEnterBackground();
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            Log.i("ApplicationLifeListener", "activity:" + activity.toString());
            Log.i("ApplicationLifeListener", "onActivityDestroyed:" + ApplicationLifeListener.this.mActiveNums);
            ApplicationLifeListener.access$010(ApplicationLifeListener.this);
            if (ApplicationLifeListener.this.mActiveNums == 0) {
                ApplicationLifeListener.this.onExit();
            }
        }
    };

    public abstract void onEnterBackground();

    public abstract void onEnterFront();

    public abstract void onExit();

    static /* synthetic */ int access$008(ApplicationLifeListener applicationLifeListener) {
        int i = applicationLifeListener.mActiveNums;
        applicationLifeListener.mActiveNums = i + 1;
        return i;
    }

    static /* synthetic */ int access$010(ApplicationLifeListener applicationLifeListener) {
        int i = applicationLifeListener.mActiveNums;
        applicationLifeListener.mActiveNums = i - 1;
        return i;
    }

    public void registerLifecycleCallback(Application application) {
        if (application == null) {
            return;
        }
        application.registerActivityLifecycleCallbacks(this.callbacks);
    }

    public void unRegisterLifecycleCallback(Application application) {
        if (application == null) {
            return;
        }
        application.unregisterActivityLifecycleCallbacks(this.callbacks);
    }
}