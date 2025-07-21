package com.netease.mpay.ps.codescanner.auth;

import com.netease.mpay.auth.plugins.DefaultPlugin;
import com.netease.mpay.auth.plugins.PluginResult;
import com.netease.mpay.ps.codescanner.auth.DefaultAuthRules;
import com.netease.ntunisdk.base.SdkMgr;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class AuthPlugin extends DefaultPlugin {
    @Override // com.netease.mpay.auth.plugins.DefaultPlugin, com.netease.mpay.auth.plugins.Plugin
    public boolean isEndNode() {
        return true;
    }

    @Override // com.netease.mpay.auth.plugins.DefaultPlugin, com.netease.mpay.auth.plugins.Plugin
    public boolean isNeedSuccessBeforeExecute() {
        return true;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public void execute() throws JSONException {
        String[] strArrSplit = DefaultAuthRules.getInstance().getIntent().getData().getQuery().split("=");
        if (strArrSplit == null || strArrSplit.length <= 1) {
            getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, this.mPreTaskResult.data, this));
            return;
        }
        try {
            String str = strArrSplit[1];
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "openAuthLogin");
            jSONObject.put("data", str);
            DefaultAuthRules.getInstance().setAuthCallback(new DefaultAuthRules.Callback() { // from class: com.netease.mpay.ps.codescanner.auth.AuthPlugin.1
                AnonymousClass1() {
                }

                @Override // com.netease.mpay.ps.codescanner.auth.DefaultAuthRules.Callback
                public void onFinish(int i, JSONObject jSONObject2) {
                    if (i == 0) {
                        AuthPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, AuthPlugin.this.mPreTaskResult.data, AuthPlugin.this));
                    } else {
                        AuthPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, AuthPlugin.this.mPreTaskResult.data, AuthPlugin.this));
                    }
                }
            });
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
        } catch (Exception unused) {
            getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, this.mPreTaskResult.data, this));
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.AuthPlugin$1 */
    class AnonymousClass1 implements DefaultAuthRules.Callback {
        AnonymousClass1() {
        }

        @Override // com.netease.mpay.ps.codescanner.auth.DefaultAuthRules.Callback
        public void onFinish(int i, JSONObject jSONObject2) {
            if (i == 0) {
                AuthPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, AuthPlugin.this.mPreTaskResult.data, AuthPlugin.this));
            } else {
                AuthPlugin.this.getCallback().onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, AuthPlugin.this.mPreTaskResult.data, AuthPlugin.this));
            }
        }
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public String getName() {
        return AuthPlugin.class.getName();
    }
}