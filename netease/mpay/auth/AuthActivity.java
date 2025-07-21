package com.netease.mpay.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.netease.mc.mi.R;
import com.netease.mpay.auth.plugins.PluginExecutor;
import com.netease.mpay.ps.codescanner.auth.DefaultAuthRules;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes.dex */
public class AuthActivity extends Activity {
    public static final String CURRENT_QUICK_QR_MODE = "CURRENT_QUICK_QR_MODE";
    public static final String CURRENT_SHORTCUT_MAIN_RUNNING = "CURRENT_SHORTCUT_MAIN_RUNNING";
    public static final String DOT = ".";
    public static final String GAME_MOBILE = "game.mobile";
    public static final String NTES = "ntes";
    protected static final String TAG = "AuthActivity";

    private void loadGameBackground() {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.netease_mpay_ps_codescanner__auth);
        Log.d(TAG, "hasRunning\uff1a" + DefaultAuthRules.getInstance().hasRunning());
        if ((getIntent().getFlags() & 1048576) != 0) {
            Log.d(TAG, "open From History");
            try {
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getPackageName());
                launchIntentForPackage.addFlags(603979776);
                startActivity(launchIntentForPackage);
                finish();
                return;
            } catch (Throwable unused) {
                return;
            }
        }
        SDKContext.getInstance().setColdLaunch(SdkMgr.getInst() == null);
        Log.d(TAG, SDKContext.getInstance().isColdLaunch() ? "cold launch" : "hot launch");
        PluginExecutor.getInstance().onCreate(bundle);
        DefaultAuthRules.getInstance().init(getIntent());
        setIntent(new Intent());
        if (SDKContext.getInstance().isColdLaunch()) {
            DefaultAuthRules.getInstance().attachActivity(this);
            DefaultAuthRules.getInstance().start();
        } else {
            DefaultAuthRules.getInstance().start();
            finish();
        }
    }

    public String getFlag(String str, int i) {
        boolean z = (i & getIntent().getFlags()) == 0;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(":[add]=");
        sb.append(!z);
        return sb.toString();
    }

    private void updateOrientation() {
        if (isLandscape()) {
            setRequestedOrientation(6);
        } else {
            setRequestedOrientation(1);
        }
    }

    private boolean returnToGame() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        Uri uriBuild = new Uri.Builder().scheme("ntes").authority("game.mobile").path(getUnisdkDeepLinkPath()).build();
        intent.setData(uriBuild);
        if (getPackageManager().resolveActivity(intent, 65536) == null) {
            UniSdkUtils.d(TAG, "no UniSDK Deep Link found: " + uriBuild);
            return false;
        }
        startActivity(intent);
        return true;
    }

    private void killProcess() {
        UniSdkUtils.d(TAG, "killProcess");
        finish();
        System.exit(0);
    }

    private boolean isLandscape() {
        int propInt = SdkMgr.getInst().getPropInt(ConstProp.SCR_ORIENTATION, 5);
        Log.d(TAG, "SCR_ORIENTATION: " + propInt);
        return 5 == propInt || 2 == propInt || 3 == propInt;
    }

    private String getUnisdkDeepLinkPath() {
        return SdkMgr.getInst().getPropStr("JF_GAMEID") + ".";
    }

    @Override // android.app.Activity
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
        PluginExecutor.getInstance().onRestart();
    }

    @Override // android.app.Activity
    protected void onStart() {
        Log.d(TAG, "onStart " + getTaskId() + " " + hashCode());
        super.onStart();
        PluginExecutor.getInstance().onStart();
    }

    @Override // android.app.Activity
    protected void onResume() {
        Log.d(TAG, "onResume " + getTaskId() + " " + hashCode());
        super.onResume();
        PluginExecutor.getInstance().onResume();
    }

    @Override // android.app.Activity
    protected void onPause() {
        Log.d(TAG, "onPause " + getTaskId() + " " + hashCode());
        super.onPause();
        PluginExecutor.getInstance().onPause();
    }

    @Override // android.app.Activity
    protected void onStop() {
        Log.d(TAG, "onStop " + getTaskId() + " " + hashCode());
        super.onStop();
        PluginExecutor.getInstance().onStop();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        PluginExecutor.getInstance().onDestroy();
        Log.d(TAG, "onDestroy " + getTaskId() + " " + hashCode());
        if (SdkMgr.getInst() != null) {
            SdkMgr.getInst().setPropInt("CURRENT_SHORTCUT_MAIN_RUNNING", 0);
        }
        if (shouldHandleLifecycle()) {
            SdkMgr.destroyInst();
            System.exit(0);
        }
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        PluginExecutor.getInstance().onNewIntent(intent);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        PluginExecutor.getInstance().onConfigurationChanged(configuration);
        Log.d(TAG, "onConfigurationChanged");
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        PluginExecutor.getInstance().onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        UniSdkUtils.d(TAG, "onActivityResult...requestCode=" + i + ",resultCode=" + i2);
        PluginExecutor.getInstance().onActivityResult(i, i2, intent);
    }

    private boolean shouldHandleLifecycle() {
        return SDKContext.getInstance().isColdLaunch() && SdkMgr.getInst() != null;
    }
}