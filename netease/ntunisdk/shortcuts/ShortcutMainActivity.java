package com.netease.ntunisdk.shortcuts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.netease.mc.mi.R;
import com.netease.ntunisdk.UniAlertDialog;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnCodeScannerListener;
import com.netease.ntunisdk.base.OnExtendFuncListener;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OnLoginDoneListener;
import com.netease.ntunisdk.base.OnLogoutDoneListener;
import com.netease.ntunisdk.base.OnOrderCheckListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.ProtocolCallback;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.shortcuts.ShortcutUtils;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ShortcutMainActivity extends MockMainActivity {
    public static final String CURRENT_QUICK_QR_MODE = "CURRENT_QUICK_QR_MODE";
    public static final String CURRENT_SHORTCUT_MAIN_RUNNING = "CURRENT_SHORTCUT_MAIN_RUNNING";
    public static final String DOT = ".";
    public static final String GAME_MOBILE = "game.mobile";
    public static final String NTES = "ntes";
    private boolean mWaitingForProtocolCallback;

    @Override // com.netease.ntunisdk.shortcuts.MockMainActivity, android.app.Activity, android.content.ComponentCallbacks
    public /* bridge */ /* synthetic */ void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // com.netease.ntunisdk.shortcuts.MockMainActivity, android.app.Activity
    public /* bridge */ /* synthetic */ void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // com.netease.ntunisdk.shortcuts.MockMainActivity, android.app.Activity
    protected void onCreate(Bundle bundle) throws IOException, SecurityException {
        super.onCreate(bundle);
        Log.d("QR quickqr_main", this.mIsColdLaunch ? "cold launch" : "hot launch");
        if (ShortcutUtils.isSupportProtocol() && ShortcutUtils.isProtocolLauncher(this)) {
            if (isProtocolShowing()) {
                UniSdkUtils.d("QR quickqr_main", "Protocol is showing, finish self");
                finish();
                return;
            } else {
                init();
                showProtocol();
                return;
            }
        }
        init();
        onProtocolFinish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProtocolFinish() {
        if (this.mIsColdLaunch) {
            initUniSDK();
            updateOrientation();
        } else {
            UniSdkUtils.d("QR quickqr_main", "UniSDK has already init, ignore init");
            finish();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    ShortcutMainActivity.this.presentQRCodeScanner();
                }
            }, 500L);
        }
    }

    private void updateOrientation() {
        if (isLandscape()) {
            setRequestedOrientation(6);
        } else {
            setRequestedOrientation(1);
        }
    }

    private void loadGameBackground() {
        try {
            int quickQrBgResId = BgUtils.getQuickQrBgResId(this);
            if (quickQrBgResId > 0) {
                setContentView(R.layout.ntunisdk_scanner_shortcuts);
                ImageView imageView = (ImageView) findViewById(R.id.netease_quickqr__assistant_background);
                imageView.setImageResource(quickQrBgResId);
                imageView.setVisibility(0);
            } else {
                UniSdkUtils.d("QR quickqr_main", "loadGameBackground background not exist, ignore");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean returnToGame() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        Uri uriBuild = new Uri.Builder().scheme("ntes").authority("game.mobile").path(getUnisdkDeepLinkPath()).build();
        intent.setData(uriBuild);
        if (getPackageManager().resolveActivity(intent, 65536) == null) {
            UniSdkUtils.d("QR quickqr_main", "no UniSDK Deep Link found: " + uriBuild);
            return false;
        }
        startActivity(intent);
        return true;
    }

    private void showProtocol() {
        final ProtocolManager protocolManager = ProtocolManager.getInstance();
        protocolManager.init(this);
        if (protocolManager.hasAcceptLaunchProtocol()) {
            UniSdkUtils.d("QR quickqr_main", "hasAcceptLaunchProtocol = true");
            onProtocolFinish();
            return;
        }
        protocolManager.setProp(new ProtocolProp());
        protocolManager.setActivity(this);
        protocolManager.setCallback(new ProtocolCallback() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.2
            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onFinish(int i) {
                Log.d("QR quickqr_main", "Protocol: onFinish " + i);
                try {
                    protocolManager.removeCallback(this);
                } catch (Throwable unused) {
                    protocolManager.setCallback(null);
                }
                ShortcutMainActivity.this.mWaitingForProtocolCallback = false;
                ShortcutMainActivity.this.onProtocolFinish();
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onOpen() {
                Log.d("QR quickqr_main", "Protocol: onOpen=>onClose");
                if (ShortcutMainActivity.this.mWaitingForProtocolCallback) {
                    protocolManager.showProtocolWhenLaunch();
                }
            }
        });
        this.mWaitingForProtocolCallback = true;
        protocolManager.showProtocolWhenLaunch();
    }

    private boolean isProtocolShowing() {
        try {
            return ProtocolManager.getInstance().isProtocolShowing();
        } catch (Exception e) {
            throw new RuntimeException("\u8bf7UniPack\u4f7f\u7528protocol_12\u53ca\u4ee5\u4e0a\u7248\u672c", e);
        }
    }

    private void init() throws IOException, SecurityException {
        Log.d("QR quickqr_main", "Enter init");
        if (this.mIsColdLaunch) {
            SdkMgr.init(this);
            SdkMgr.getInst().setPropInt("CURRENT_SHORTCUT_MAIN_RUNNING", 1);
        }
    }

    private void initUniSDK() {
        Log.d("QR quickqr_main", "Enter initUniSDK");
        if (this.mIsColdLaunch) {
            SdkMgr.getInst().setLoginListener(new OnLoginDoneListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.3
                @Override // com.netease.ntunisdk.base.OnLoginDoneListener
                public void loginDone(int i) {
                    UniSdkUtils.d("QR quickqr_main", String.format("loginDone, code = %s", Integer.valueOf(i)));
                    ShortcutMainActivity.this.onLoginDone(i);
                }
            }, 1);
            SdkMgr.getInst().setLogoutListener(new OnLogoutDoneListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.4
                @Override // com.netease.ntunisdk.base.OnLogoutDoneListener
                public void logoutDone(int i) {
                    UniSdkUtils.d("QR quickqr_main", String.format("logoutDone, code = %s", Integer.valueOf(i)));
                }
            }, 1);
            SdkMgr.getInst().setOrderListener(new OnOrderCheckListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.5
                @Override // com.netease.ntunisdk.base.OnOrderCheckListener
                public void orderConsumeDone(OrderInfo orderInfo) {
                }

                @Override // com.netease.ntunisdk.base.OnOrderCheckListener
                public void orderCheckDone(OrderInfo orderInfo) {
                    Object[] objArr = new Object[1];
                    Object obj = orderInfo;
                    if (orderInfo == null) {
                        obj = "NULL";
                    }
                    objArr[0] = obj;
                    UniSdkUtils.d("QR quickqr_main", String.format("orderCheckDone, orderInfo = %s", objArr));
                }
            }, 1);
            SdkMgr.getInst().setExtendFuncListener(new OnExtendFuncListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.6
                /* JADX WARN: Removed duplicated region for block: B:19:0x0051  */
                @Override // com.netease.ntunisdk.base.OnExtendFuncListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void onExtendFuncCall(java.lang.String r8) {
                    /*
                        r7 = this;
                        r0 = 1
                        java.lang.Object[] r1 = new java.lang.Object[r0]
                        r2 = 0
                        r1[r2] = r8
                        java.lang.String r3 = "onExtendFuncCall, code = %s"
                        java.lang.String r1 = java.lang.String.format(r3, r1)
                        java.lang.String r3 = "QR quickqr_main"
                        com.netease.ntunisdk.base.UniSdkUtils.d(r3, r1)
                        org.json.JSONObject r1 = new org.json.JSONObject     // Catch: org.json.JSONException -> L6b
                        r1.<init>(r8)     // Catch: org.json.JSONException -> L6b
                        java.lang.String r8 = "methodId"
                        java.lang.String r3 = ""
                        java.lang.String r8 = r1.optString(r8, r3)     // Catch: org.json.JSONException -> L6b
                        int r3 = r8.hashCode()     // Catch: org.json.JSONException -> L6b
                        r4 = 57256915(0x369abd3, float:6.866979E-37)
                        r5 = 2
                        r6 = -1
                        if (r3 == r4) goto L47
                        r4 = 911577092(0x36559004, float:3.1823301E-6)
                        if (r3 == r4) goto L3e
                        r2 = 1324341463(0x4eefd8d7, float:2.0119827E9)
                        if (r3 == r2) goto L34
                        goto L51
                    L34:
                        java.lang.String r2 = "onQrCodeScanOrderPayFinish"
                        boolean r8 = r8.equals(r2)     // Catch: org.json.JSONException -> L6b
                        if (r8 == 0) goto L51
                        r2 = 2
                        goto L52
                    L3e:
                        java.lang.String r3 = "onQrCodeScanLoginSuccess"
                        boolean r8 = r8.equals(r3)     // Catch: org.json.JSONException -> L6b
                        if (r8 == 0) goto L51
                        goto L52
                    L47:
                        java.lang.String r2 = "onQrCodeScanIndexPayFinish"
                        boolean r8 = r8.equals(r2)     // Catch: org.json.JSONException -> L6b
                        if (r8 == 0) goto L51
                        r2 = 1
                        goto L52
                    L51:
                        r2 = -1
                    L52:
                        if (r2 == 0) goto L65
                        if (r2 == r0) goto L59
                        if (r2 == r5) goto L59
                        return
                    L59:
                        com.netease.ntunisdk.shortcuts.ShortcutMainActivity r8 = com.netease.ntunisdk.shortcuts.ShortcutMainActivity.this     // Catch: org.json.JSONException -> L6b
                        java.lang.String r0 = "orderStatus"
                        int r0 = r1.optInt(r0, r6)     // Catch: org.json.JSONException -> L6b
                        com.netease.ntunisdk.shortcuts.ShortcutMainActivity.access$500(r8, r0)     // Catch: org.json.JSONException -> L6b
                        return
                    L65:
                        com.netease.ntunisdk.shortcuts.ShortcutMainActivity r8 = com.netease.ntunisdk.shortcuts.ShortcutMainActivity.this     // Catch: org.json.JSONException -> L6b
                        com.netease.ntunisdk.shortcuts.ShortcutMainActivity.access$400(r8)     // Catch: org.json.JSONException -> L6b
                        return
                    L6b:
                        r8 = move-exception
                        r8.printStackTrace()
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.AnonymousClass6.onExtendFuncCall(java.lang.String):void");
                }
            }, 1);
            SdkMgr.getInst().ntInit(new OnFinishInitListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.7
                @Override // com.netease.ntunisdk.base.OnFinishInitListener
                public void finishInit(int i) {
                    UniSdkUtils.d("QR quickqr_main", String.format("finishInit, code = %s", Integer.valueOf(i)));
                    if (i == 0 || i == 2 || i == 3) {
                        UniSdkUtils.d("QR quickqr_main", "UniSDK ntInit success");
                        ShortcutMainActivity.this.onInitUniSDKSuccess();
                    } else {
                        UniSdkUtils.d("QR quickqr_main", "UniSDK ntInit failed, scan qrcode ignore");
                        ShortcutMainActivity.this.onInitUniSDKFailed();
                    }
                }
            });
        }
    }

    public void notifyQuickQrSetProps(String str) {
        Log.d("QR quickqr_main", "quickQrConfig = " + str);
        if (!this.mIsColdLaunch) {
            Log.d("QR quickqr_main", "not cold launch, notifyQuickQrSetProps ignore");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                SdkMgr.getInst().setPropStr(next, jSONObject.getString(next));
            }
        } catch (JSONException e) {
            Log.d("QR quickqr_main", "notifyQuickQrSetProps JSONException:" + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitUniSDKSuccess() {
        ShortcutUtils.CheckResult checkResultCheckUniSDKParams = ShortcutUtils.checkUniSDKParams();
        if (!checkResultCheckUniSDKParams.success) {
            new UniAlertDialog(this).alert(String.format("\u53c2\u6570\u9519\u8bef(-%s)", Integer.valueOf(checkResultCheckUniSDKParams.code)), "\u9000\u51fa\u5e94\u7528", new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ShortcutMainActivity.this.killProcess();
                }
            }, false);
            return;
        }
        SdkMgr.getInst().setPropInt(ConstProp.UNISDK_JF_GAS3, 1);
        if (isNeteaseChannel()) {
            presentQRCodeScanner();
        } else {
            SdkMgr.getInst().ntLogin();
        }
        SdkMgr.getInst().setCodeScannerListener(new OnCodeScannerListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.9
            @Override // com.netease.ntunisdk.base.OnCodeScannerListener
            public void codeScannerFinish(int i, String str) {
                UniSdkUtils.d("QR quickqr_main", String.format("codeScannerFinish, code = %s, extra = %s", Integer.valueOf(i), str));
                ShortcutMainActivity.this.onCodeScannerFinish(i, str);
            }
        }, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitUniSDKFailed() {
        new UniAlertDialog(this).alert("\u672a\u77e5\u5f02\u5e38\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002", getString(ResUtils.getResId(this, "ntunisdk_scan_quit_app", ResIdReader.RES_TYPE_STRING)), getString(ResUtils.getResId(this, "ntunisdk_scan_start_game", ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ShortcutMainActivity.this.killProcess();
            }
        }, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ShortcutUtils.relaunchApp(ShortcutMainActivity.this, true);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLoginDone(int i) {
        if (i == 0) {
            presentQRCodeScanner();
        } else {
            new UniAlertDialog(this).alert("\u767b\u5f55\u5931\u8d25\uff0c\u662f\u5426\u91cd\u8bd5\uff1f", "\u9000\u51fa", "\u91cd\u8bd5", new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.12
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    ShortcutMainActivity.this.killProcess();
                }
            }, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.13
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SdkMgr.getInst().ntLogin();
                }
            }, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void presentQRCodeScanner() {
        UniSdkUtils.d("QR quickqr_main", "start presentQRCodeScanner");
        SdkMgr.getInst().setPropInt("CURRENT_QUICK_QR_MODE", 1);
        SdkMgr.getInst().ntPresentQRCodeScanner("", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCodeScannerFinish(int i, String str) {
        UniSdkUtils.d("QR quickqr_main", String.format("Enter onCodeScannerFinish code=%s, extra=%s", Integer.valueOf(i), str));
        if (i == 0) {
            return;
        }
        if (i == 21) {
            killProcess();
        } else {
            killProcess();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void qrCodeScanLoginSuccessAlert() {
        new UniAlertDialog(this).alert(getString(ResUtils.getResId(this, "ntunisdk_scan_qrcode_quit_app_warn", ResIdReader.RES_TYPE_STRING)), getString(ResUtils.getResId(this, "ntunisdk_scan_quit_app", ResIdReader.RES_TYPE_STRING)), getString(ResUtils.getResId(this, "ntunisdk_scan_start_game", ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ShortcutMainActivity.this.killProcess();
            }
        }, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ShortcutUtils.relaunchApp(ShortcutMainActivity.this, true);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void qrCodeScanIndexPayFinishAlert(int i) {
        UniSdkUtils.d("QR quickqr_main", String.format("Enter qrCodeScanIndexPayFinishAlert orderStatus=%s", Integer.valueOf(i)));
        new UniAlertDialog(this).alert("\u652f\u4ed8\u7ed3\u675f\uff0c\u662f\u5426\u9000\u51fa\u5e94\u7528\uff1f", getString(ResUtils.getResId(this, "ntunisdk_scan_quit_app", ResIdReader.RES_TYPE_STRING)), getString(ResUtils.getResId(this, "ntunisdk_scan_start_game", ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                ShortcutMainActivity.this.killProcess();
            }
        }, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.shortcuts.ShortcutMainActivity.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                ShortcutUtils.relaunchApp(ShortcutMainActivity.this, true);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void killProcess() {
        UniSdkUtils.d("QR quickqr_main", "killProcess");
        finish();
        System.exit(0);
    }

    private boolean isNeteaseChannel() {
        return "netease".equals(SdkMgr.getInst().getChannel());
    }

    private boolean isLandscape() {
        int propInt = SdkMgr.getInst().getPropInt(ConstProp.SCR_ORIENTATION, 5);
        Log.d("QR quickqr_main", "SCR_ORIENTATION: " + propInt);
        return 5 == propInt || 2 == propInt || 3 == propInt;
    }

    private String getUnisdkDeepLinkPath() {
        return SdkMgr.getInst().getPropStr("JF_GAMEID") + ".";
    }

    @Override // com.netease.ntunisdk.shortcuts.MockMainActivity, android.app.Activity
    protected void onDestroy() {
        if (SdkMgr.getInst() != null) {
            SdkMgr.getInst().setPropInt("CURRENT_SHORTCUT_MAIN_RUNNING", 0);
        }
        super.onDestroy();
    }
}