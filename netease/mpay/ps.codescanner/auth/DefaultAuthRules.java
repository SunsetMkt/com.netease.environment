package com.netease.mpay.ps.codescanner.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.netease.mpay.auth.Rule;
import com.netease.mpay.auth.SDKContext;
import com.netease.mpay.auth.plugins.PluginCallback;
import com.netease.mpay.auth.plugins.PluginExecutor;
import com.netease.mpay.auth.plugins.PluginResult;
import com.netease.mpay.auth.plugins.UniSDKPlugin;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DefaultAuthRules implements Rule, PluginCallback {
    private static final String TAG = "ScannerAuth";
    private static DefaultAuthRules sInstance;
    private boolean isStartLoginByScanner;
    private String lastUid;
    private Activity mActivity;
    public Callback mAuthCallback;
    private Intent mIntent;
    public Callback mLoginCallback;
    private AuthUniProxy mUniSDKProxy;
    private boolean hasLogin = false;
    public boolean isGameActivityPaused = false;
    public ConcurrentHashMap<String, PluginCallback> mExecuteCallbacks = new ConcurrentHashMap<>();

    public interface Callback {
        void onFinish(int i, JSONObject jSONObject);
    }

    public String getLastUid() {
        return this.lastUid;
    }

    public void setLastUid(String str) {
        this.lastUid = str;
    }

    public synchronized boolean isStartLoginByScanner() {
        return this.isStartLoginByScanner;
    }

    public synchronized void setStartLoginByScanner(boolean z) {
        this.isStartLoginByScanner = z;
    }

    private DefaultAuthRules() {
        if (Utils.isSupportProtocol()) {
            this.mUniSDKProxy = new AuthUniProxy();
            ProtocolManager.getInstance().setUniSDKProxy(this.mUniSDKProxy);
        }
    }

    public void init(Intent intent) {
        this.mIntent = intent;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public static DefaultAuthRules getInstance() {
        if (sInstance == null) {
            synchronized (DefaultAuthRules.class) {
                sInstance = new DefaultAuthRules();
            }
        }
        return sInstance;
    }

    @Override // com.netease.mpay.auth.Rule
    public void start() {
        Activity activity = this.mActivity;
        if (activity == null || activity.isFinishing() || this.mIntent == null) {
            onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, null, null));
            return;
        }
        PluginExecutor.getInstance().reset();
        this.isStartLoginByScanner = false;
        AuthUniProxy authUniProxy = this.mUniSDKProxy;
        if (authUniProxy != null) {
            authUniProxy.setRunning(true);
        }
        PluginExecutor.getInstance().execute(new UniSDKPlugin().setActivity(this.mActivity).addCallback(this));
        PluginExecutor.getInstance().execute(new DecisionPlugin().setActivity(this.mActivity).addCallback(this));
        PluginExecutor.getInstance().execute(new ProtocolPlugin().setActivity(this.mActivity).addCallback(this));
        PluginExecutor.getInstance().execute(new UniSDKLoginPlugin().setActivity(this.mActivity).addCallback(this));
        if (DoulinkAuthPlugin.isAuthToDy(this.mIntent.getData())) {
            if (!DoulinkAuthPlugin.isDouyinLinkModuleExist()) {
                Log.d(TAG, "Doulink MODULE not exist");
                onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, null, null));
                return;
            } else {
                PluginExecutor.getInstance().execute(new DoulinkAuthPlugin().setActivity(this.mActivity).addCallback(this));
                return;
            }
        }
        PluginExecutor.getInstance().execute(new AuthPlugin().setActivity(this.mActivity).addCallback(this));
    }

    @Override // com.netease.mpay.auth.Rule
    public Rule attachActivity(Activity activity) {
        if (this.mActivity == null) {
            this.mActivity = activity;
        }
        return this;
    }

    public boolean hasRunning() {
        AuthUniProxy authUniProxy = this.mUniSDKProxy;
        return authUniProxy != null && authUniProxy.hasAppRunning();
    }

    @Override // com.netease.mpay.auth.plugins.PluginCallback
    public void onFinish(PluginResult pluginResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("onFinish:");
        sb.append(pluginResult.status);
        sb.append(", data:");
        sb.append(pluginResult.data != null ? pluginResult.data.toString() : "");
        sb.append(",isEnd:");
        sb.append(pluginResult.isEnd);
        Log.d(TAG, sb.toString());
        if (pluginResult.isSuccess()) {
            if (pluginResult.isEnd && SDKContext.getInstance().isColdLaunch()) {
                killProcess();
                return;
            }
            return;
        }
        if (SDKContext.getInstance().isColdLaunch()) {
            killProcess();
        } else {
            UniSdkUtils.d(TAG, "return Game");
        }
    }

    private void killProcess() {
        UniSdkUtils.d(TAG, "kill Game");
        this.mActivity.finish();
        System.exit(0);
    }

    public synchronized void add(String str, PluginCallback pluginCallback) {
        this.mExecuteCallbacks.put(str, pluginCallback);
    }

    public synchronized void remove(String str) {
        this.mExecuteCallbacks.remove(str);
    }

    public void notifyLoginDone(int i) {
        Callback callback = this.mLoginCallback;
        if (callback != null) {
            callback.onFinish(i, null);
        }
    }

    public void setLoginCallback(Callback callback) {
        this.mLoginCallback = callback;
    }

    public void notifyAuthDone(int i) {
        Callback callback = this.mAuthCallback;
        if (callback != null) {
            callback.onFinish(i, null);
        }
    }

    public void setAuthCallback(Callback callback) {
        this.mAuthCallback = callback;
    }

    public boolean isGameActivityPaused() {
        return this.isGameActivityPaused;
    }

    public void setGameActivityPaused(boolean z) {
        this.isGameActivityPaused = z;
    }

    public synchronized boolean isHasLogin() {
        return this.hasLogin;
    }

    public synchronized void setHasLogin(boolean z) {
        this.hasLogin = z;
    }
}