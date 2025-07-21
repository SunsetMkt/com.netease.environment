package com.netease.mpay.ps.codescanner.auth;

import android.net.Uri;
import com.netease.mpay.auth.AuthUtils;
import com.netease.mpay.auth.plugins.DefaultPlugin;
import com.netease.mpay.auth.plugins.PluginResult;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.netease_douyinlink.ttgame.Doulink;

/* loaded from: classes5.dex */
public class DoulinkAuthPlugin extends DefaultPlugin {
    private static final String DOULINK_PATTERN = "dygame.*//.+/author_to_dy.*";
    private static final String TAG = "DoulinkAuthPlugin";

    @Override // com.netease.mpay.auth.plugins.DefaultPlugin, com.netease.mpay.auth.plugins.Plugin
    public boolean isEndNode() {
        return true;
    }

    @Override // com.netease.mpay.auth.plugins.DefaultPlugin, com.netease.mpay.auth.plugins.Plugin
    public boolean isNeedSuccessBeforeExecute() {
        return true;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public void execute() {
        Uri data = DefaultAuthRules.getInstance().getIntent().getData();
        if (data == null) {
            retFinish(PluginResult.RESULT_FAILED);
        } else {
            Doulink.getInst().execute(data.toString(), new Doulink.DoulinkAuthCallback() { // from class: com.netease.mpay.ps.codescanner.auth.DoulinkAuthPlugin.1
                AnonymousClass1() {
                }

                public void onSuccess(String str) {
                    UniSdkUtils.d(DoulinkAuthPlugin.TAG, "onSuccess: rawResult: " + str);
                    DoulinkAuthPlugin.this.retFinish(PluginResult.RESULT_SUCCESS);
                }

                public void onFailure(int i, String str) {
                    UniSdkUtils.d(DoulinkAuthPlugin.TAG, "onFailure: code: " + i + ", msg: " + str);
                    DoulinkAuthPlugin.this.retFinish(PluginResult.RESULT_FAILED);
                }
            });
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.DoulinkAuthPlugin$1 */
    class AnonymousClass1 implements Doulink.DoulinkAuthCallback {
        AnonymousClass1() {
        }

        public void onSuccess(String str) {
            UniSdkUtils.d(DoulinkAuthPlugin.TAG, "onSuccess: rawResult: " + str);
            DoulinkAuthPlugin.this.retFinish(PluginResult.RESULT_SUCCESS);
        }

        public void onFailure(int i, String str) {
            UniSdkUtils.d(DoulinkAuthPlugin.TAG, "onFailure: code: " + i + ", msg: " + str);
            DoulinkAuthPlugin.this.retFinish(PluginResult.RESULT_FAILED);
        }
    }

    public void retFinish(int i) {
        getCallback().onFinish(PluginResult.newInstance(i, this.mPreTaskResult.data, this));
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public String getName() {
        return DoulinkAuthPlugin.class.getName();
    }

    public static boolean isAuthToDy(Uri uri) {
        return String.valueOf(uri).matches(DOULINK_PATTERN);
    }

    public static boolean isDouyinLinkModuleExist() {
        return AuthUtils.isClassInstalled("com.netease.ntunisdk.SdkDouyinLink") && AuthUtils.isClassInstalled("com.netease.ntunisdk.netease_douyinlink.ttgame.Doulink");
    }
}