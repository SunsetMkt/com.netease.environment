package com.netease.ntunisdk.modules.ngwebviewgeneral;

import android.app.Application;
import android.os.Build;
import com.netease.ntunisdk.modules.ngwebviewgeneral.log.NgWebviewLog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;

/* loaded from: classes.dex */
public class ApplicationNGWebview extends Application {
    private static final String TAG = "UniSDK ApplicationNGWebview";

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        NgWebviewLog.i(TAG, "onCreate...");
        if (Build.VERSION.SDK_INT < 28 || getPackageName().equals(Application.getProcessName())) {
            return;
        }
        UniWebView.setDataDirectorySuffix(Application.getProcessName());
    }
}