package com.netease.ntunisdk;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.netease.ntunisdk.base.SdkApplication;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;

/* loaded from: classes.dex */
public class ApplicationNGWebview extends SdkApplication {
    private static final String TAG = "UniSDK ApplicationNGWebview";

    @Override // com.netease.ntunisdk.base.SdkApplication
    public String getChannel() {
        return "ngwebview";
    }

    public ApplicationNGWebview(Context context) {
        super(context);
    }

    @Override // com.netease.ntunisdk.base.SdkApplication
    public void handleOnApplicationOnCreate(Context context, Application application) {
        Log.i(TAG, "onCreate...");
        if (Build.VERSION.SDK_INT < 28 || context.getPackageName().equals(Application.getProcessName())) {
            return;
        }
        UniWebView.setDataDirectorySuffix(Application.getProcessName());
    }
}