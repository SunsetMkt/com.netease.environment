package com.netease.ntunisdk.application;

import android.app.Application;
import android.content.Context;
import com.netease.ntunisdk.base.ApplicationHandler;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes6.dex */
public class NtSdkApplication extends Application {
    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) throws IOException, SecurityException {
        super.attachBaseContext(context);
        ApplicationHandler.handleOnApplicationAttachBaseContext(context, this);
    }

    @Override // android.app.Application
    public void onCreate() throws IllegalAccessException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        super.onCreate();
        ApplicationHandler.handleOnApplicationOnCreate(getApplicationContext(), this);
    }
}