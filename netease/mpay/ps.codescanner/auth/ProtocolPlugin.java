package com.netease.mpay.ps.codescanner.auth;

import android.app.Activity;
import android.util.Log;
import com.netease.mpay.auth.plugins.DefaultPlugin;
import com.netease.mpay.auth.plugins.OnNextListener;
import com.netease.mpay.auth.plugins.PluginCallback;
import com.netease.mpay.auth.plugins.PluginResult;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.external.protocol.ProtocolCallback;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;

/* loaded from: classes5.dex */
public class ProtocolPlugin extends DefaultPlugin implements OnNextListener {
    private static final String TAG = "ProtocolPlugin";
    private boolean mWaitingForProtocolCallback;

    private void onProtocolFinish() {
    }

    @Override // com.netease.mpay.auth.plugins.DefaultPlugin, com.netease.mpay.auth.plugins.Plugin
    public boolean isNeedSuccessBeforeExecute() {
        return true;
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public void execute() {
        if (Utils.isSupportProtocol() && Utils.isProtocolLauncher(this.mActivity)) {
            if (isProtocolShowing()) {
                UniSdkUtils.d(TAG, "Protocol is showing, finish self");
                this.mPluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, this));
                return;
            } else {
                showProtocol(this.mActivity, this.mPluginCallback);
                return;
            }
        }
        this.mPluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, this.mPreTaskResult.data, this));
    }

    private boolean isProtocolShowing() {
        try {
            return ProtocolManager.getInstance().isProtocolShowing();
        } catch (Throwable unused) {
            UniSdkUtils.d(TAG, "not find protocol sdk");
            try {
                return SDKRuntime.getInstance().isProtocolShowing();
            } catch (Throwable unused2) {
                return false;
            }
        }
    }

    private void showProtocol(Activity activity, PluginCallback pluginCallback) {
        ProtocolManager protocolManager = ProtocolManager.getInstance();
        protocolManager.setProp(new ProtocolProp());
        protocolManager.setActivity(activity);
        protocolManager.setCallback(new ProtocolCallback() { // from class: com.netease.mpay.ps.codescanner.auth.ProtocolPlugin.1
            final /* synthetic */ PluginCallback val$callback;
            final /* synthetic */ ProtocolManager val$instance;

            AnonymousClass1(ProtocolManager protocolManager2, PluginCallback pluginCallback2) {
                protocolManager = protocolManager2;
                pluginCallback = pluginCallback2;
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onFinish(int i) {
                Log.d(ProtocolPlugin.TAG, "Protocol: onFinish " + i);
                try {
                    protocolManager.removeCallback(this);
                } catch (Throwable unused) {
                    protocolManager.setCallback(null);
                }
                ProtocolPlugin.this.mWaitingForProtocolCallback = false;
                if (i != 2) {
                    pluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, ProtocolPlugin.this.mPreTaskResult.data, ProtocolPlugin.this));
                } else {
                    pluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, ProtocolPlugin.this));
                }
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onOpen() {
                Log.d(ProtocolPlugin.TAG, "Protocol: onOpen=>onClose");
            }
        });
        if (protocolManager2.hasAcceptLaunchProtocol()) {
            Log.d(TAG, "Protocol: has accepted ");
            protocolManager2.getCallback().onFinish(3);
        } else {
            protocolManager2.showProtocolWhenLaunch();
        }
    }

    /* renamed from: com.netease.mpay.ps.codescanner.auth.ProtocolPlugin$1 */
    class AnonymousClass1 implements ProtocolCallback {
        final /* synthetic */ PluginCallback val$callback;
        final /* synthetic */ ProtocolManager val$instance;

        AnonymousClass1(ProtocolManager protocolManager2, PluginCallback pluginCallback2) {
            protocolManager = protocolManager2;
            pluginCallback = pluginCallback2;
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
        public void onFinish(int i) {
            Log.d(ProtocolPlugin.TAG, "Protocol: onFinish " + i);
            try {
                protocolManager.removeCallback(this);
            } catch (Throwable unused) {
                protocolManager.setCallback(null);
            }
            ProtocolPlugin.this.mWaitingForProtocolCallback = false;
            if (i != 2) {
                pluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, ProtocolPlugin.this.mPreTaskResult.data, ProtocolPlugin.this));
            } else {
                pluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, ProtocolPlugin.this));
            }
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
        public void onOpen() {
            Log.d(ProtocolPlugin.TAG, "Protocol: onOpen=>onClose");
        }
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public String getName() {
        return ProtocolPlugin.class.getName();
    }

    @Override // com.netease.mpay.auth.plugins.OnNextListener
    public void onNext(PluginResult pluginResult) {
        this.mOnNextListener.onNext(pluginResult);
    }
}