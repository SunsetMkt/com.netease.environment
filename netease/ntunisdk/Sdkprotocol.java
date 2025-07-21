package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import com.netease.mpay.httpdns.HttpDns;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.external.protocol.DeepLinkPref;
import com.netease.ntunisdk.external.protocol.ExtendProtocolCallback;
import com.netease.ntunisdk.external.protocol.ProtocolCallback;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.Tracker;
import com.netease.ntunisdk.external.protocol.UniSDKProxy;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.external.protocol.plugins.Callback;
import com.netease.ntunisdk.external.protocol.plugins.PluginManager;
import com.netease.ntunisdk.external.protocol.plugins.Result;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.rnccplayer.VideoViewManager;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Sdkprotocol extends SdkBase {
    private static final String APP_KEY = "B91xFDWsRsEu41otp8JR7Clt03QCpjhI";
    private static final String CHANNEL = "protocol";
    private static final String SDK_VERSION = "4.11.0";
    private static final String TAG = "UniSDK protocol";
    String appChannel;
    private String currentUid;
    private boolean isOverseaDistribution;
    String jfGameId;
    private boolean launcherOpen;
    String loginChannel;
    private Activity mShowProtocolActivity;
    private ExtendProtocolCallback protocolCallback;
    private ProtocolCallback protocolCallbackForLogin;
    private ProtocolManager protocolManager;
    String udid;

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return "protocol";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return "4.11.0";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return "4.11.0";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    public Sdkprotocol(Context context) {
        super(context);
        this.isOverseaDistribution = false;
        this.protocolCallback = new ExtendProtocolCallback() { // from class: com.netease.ntunisdk.Sdkprotocol.1
            @Override // com.netease.ntunisdk.external.protocol.ExtendProtocolCallback
            public void onFinish(int i, JSONObject jSONObject) throws JSONException {
                StringBuilder sb = new StringBuilder();
                sb.append("onFinish, code = ");
                sb.append(i);
                sb.append(", result:");
                sb.append(jSONObject != null ? jSONObject.toString() : "null");
                UniSdkUtils.d(Sdkprotocol.TAG, sb.toString());
                String propStr = SdkMgr.getInst().getPropStr("COMPACT_SCENE");
                if (!TextUtils.isEmpty(propStr) && !propStr.equals(Const.CONFIG_KEY.ALL)) {
                    SdkMgr.getInst().setPropStr("COMPACT_SCENE", Const.CONFIG_KEY.ALL);
                }
                if (Sdkprotocol.this.isOverseaDistribution && SDKRuntime.getInstance().isProtocolLauncher() && ProtocolManager.getInstance().isDisagreedAliasChanged() && !User.isLogoutUser(Sdkprotocol.this.currentUid)) {
                    SdkMgr.getInst().setPropStr("DISAGREED_PROTOCOLS", ProtocolManager.getInstance().getDisagreedAliasStr());
                    JSONObject jSONObjectOnProtocolListChangedCallback = ProtocolManager.getInstance().onProtocolListChangedCallback();
                    if (jSONObjectOnProtocolListChangedCallback != null) {
                        SdkMgr.getInst().ntExtendFunc(jSONObjectOnProtocolListChangedCallback.toString());
                        Sdkprotocol.this.extendFuncCall(jSONObjectOnProtocolListChangedCallback.toString());
                    }
                }
                if (Sdkprotocol.this.protocolCallbackForLogin != null) {
                    Sdkprotocol.this.protocolCallbackForLogin.onFinish(i);
                } else if (i != 2) {
                    try {
                        Sdkprotocol sdkprotocol = Sdkprotocol.this;
                        String[] strArr = new String[18];
                        strArr[0] = "func";
                        strArr[1] = "showCompactView";
                        strArr[2] = OneTrackParams.XMSdkParams.STEP;
                        strArr[3] = "show_protocol_done";
                        strArr[4] = "accept";
                        strArr[5] = String.valueOf(i);
                        strArr[6] = "channel_id";
                        strArr[7] = Sdkprotocol.this.loginChannel;
                        strArr[8] = "uid";
                        strArr[9] = TextUtils.isEmpty(Sdkprotocol.this.currentUid) ? "" : Sdkprotocol.this.currentUid;
                        strArr[10] = "jfGameId";
                        strArr[11] = Sdkprotocol.this.jfGameId;
                        strArr[12] = "udid";
                        strArr[13] = Sdkprotocol.this.udid;
                        strArr[14] = "protocol_id";
                        strArr[15] = String.valueOf(Sdkprotocol.this.protocolManager.getCurrentProtocolId());
                        strArr[16] = "protocol_version";
                        strArr[17] = String.valueOf(Sdkprotocol.this.protocolManager.getCurrentProtocolVersion());
                        sdkprotocol.saveClientLog(strArr);
                    } catch (Throwable unused) {
                    }
                }
                ((SdkBase) SdkMgr.getInst()).protocolFinish(i);
                if (i == 2) {
                    PluginManager.getInstance().exit(Sdkprotocol.this.myCtx, new Callback() { // from class: com.netease.ntunisdk.Sdkprotocol.1.1
                        @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                        public void onFinish(Result result) {
                            SysHelper.killProcess(Sdkprotocol.this.myCtx);
                        }
                    });
                }
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onFinish(int i) {
                UniSdkUtils.d(Sdkprotocol.TAG, "onFinish, code = " + i);
                String propStr = SdkMgr.getInst().getPropStr("COMPACT_SCENE");
                if (!TextUtils.isEmpty(propStr) && !propStr.equals(Const.CONFIG_KEY.ALL)) {
                    SdkMgr.getInst().setPropStr("COMPACT_SCENE", Const.CONFIG_KEY.ALL);
                }
                if (Sdkprotocol.this.protocolCallbackForLogin != null) {
                    Sdkprotocol.this.protocolCallbackForLogin.onFinish(i);
                } else if (i != 2) {
                    try {
                        Sdkprotocol sdkprotocol = Sdkprotocol.this;
                        String[] strArr = new String[18];
                        strArr[0] = "func";
                        strArr[1] = "showCompactView";
                        strArr[2] = OneTrackParams.XMSdkParams.STEP;
                        strArr[3] = "show_protocol_done";
                        strArr[4] = "accept";
                        strArr[5] = String.valueOf(i);
                        strArr[6] = "channel_id";
                        strArr[7] = Sdkprotocol.this.loginChannel;
                        strArr[8] = "uid";
                        strArr[9] = TextUtils.isEmpty(Sdkprotocol.this.currentUid) ? "" : Sdkprotocol.this.currentUid;
                        strArr[10] = "jfGameId";
                        strArr[11] = Sdkprotocol.this.jfGameId;
                        strArr[12] = "udid";
                        strArr[13] = Sdkprotocol.this.udid;
                        strArr[14] = "protocol_id";
                        strArr[15] = String.valueOf(Sdkprotocol.this.protocolManager.getCurrentProtocolId());
                        strArr[16] = "protocol_version";
                        strArr[17] = String.valueOf(Sdkprotocol.this.protocolManager.getCurrentProtocolVersion());
                        sdkprotocol.saveClientLog(strArr);
                    } catch (Throwable unused) {
                    }
                }
                ((SdkBase) SdkMgr.getInst()).protocolFinish(i);
                if (i == 2) {
                    PluginManager.getInstance().exit(Sdkprotocol.this.myCtx, new Callback() { // from class: com.netease.ntunisdk.Sdkprotocol.1.2
                        @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                        public void onFinish(Result result) throws JSONException {
                            ((SdkBase) SdkMgr.getInst()).resetCommonProp();
                            SysHelper.killProcess(Sdkprotocol.this.myCtx);
                        }
                    });
                }
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onOpen() {
                UniSdkUtils.d(Sdkprotocol.TAG, "onOpen");
                if (Sdkprotocol.this.protocolCallbackForLogin != null) {
                    Sdkprotocol.this.protocolCallbackForLogin.onOpen();
                }
                SdkMgr.getInst().setPropInt("USER_COMPACT_OPEN", 1);
                Sdkprotocol.this.extendFuncCall("{\"methodId\":\"onProtocolOpen\", \"channel\":\"protocol\"}");
            }
        };
        setPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL, 1);
        setPropInt(ConstProp.INNER_MODE_NO_PAY, 1);
        setFeature(ConstProp.SHOW_PROTOCOL_IN_LOGIN, true);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) throws JSONException {
        initProtocolManager();
        for (Map.Entry<String, String> entry : DeepLinkPref.getAllKeyValues().entrySet()) {
            SdkMgr.getInst().setPropStr(entry.getKey(), entry.getValue());
        }
        this.jfGameId = SdkMgr.getInst().getPropStr("JF_GAMEID");
        this.loginChannel = SdkMgr.getInst().getChannel();
        this.appChannel = SdkMgr.getInst().getPropStr(ConstProp.APP_CHANNEL);
        this.udid = super.getUdid();
        int publishArea = SDKRuntime.getInstance().getPublishArea();
        this.isOverseaDistribution = SDKRuntime.getInstance().getPublishArea() != 0;
        UniSdkUtils.d(TAG, " >> init : channelID:" + this.loginChannel + ", appChannel:" + this.appChannel + ", jfGameId:" + this.jfGameId + ", udid:" + this.udid + ", publishArea:" + publishArea + "\uff0cHasShowProtocolLauncher:" + this.protocolManager.hasAcceptProtocol() + ", noKillProcess:" + SDKRuntime.getInstance().isNotExitProcess());
        try {
            SDKRuntime.getInstance().setDelayShow("1".equals(ResUtils.getString(this.myCtx, "protocol_delay_show", "0")));
        } catch (Exception unused) {
        }
        Tracker.getInstance().setPublishArea(publishArea);
        if (TextUtils.isEmpty(SDKRuntime.getInstance().getAppChannel())) {
            SDKRuntime.getInstance().setAppChannel(this.appChannel);
        }
        if (TextUtils.isEmpty(SDKRuntime.getInstance().getJFGameId())) {
            SDKRuntime.getInstance().setJFGameId(this.jfGameId);
        }
        if (SysHelper.isSupportHttpDNS()) {
            HttpDns.getInstance().init(this.myCtx, this.jfGameId, SDKRuntime.getInstance().getPublishArea(), UniSdkUtils.isDebug);
        }
        try {
            L.setDebug(UniSdkUtils.isDebug);
        } catch (Exception unused2) {
        }
        SdkMgr.getInst().setPropStr("PROTOCOL_DATA_STR", SDKRuntime.getInstance().getDataStrForceRefresh());
        ProtocolManager.getInstance().setUniSdkRunning(true);
        if (this.protocolManager.hasAcceptProtocol()) {
            try {
                String[] strArr = new String[18];
                strArr[0] = "func";
                strArr[1] = "showProtocolInLaunch";
                strArr[2] = OneTrackParams.XMSdkParams.STEP;
                strArr[3] = "show_protocol_done";
                strArr[4] = "accept";
                strArr[5] = "1";
                strArr[6] = "uid";
                strArr[7] = TextUtils.isEmpty(this.currentUid) ? "" : this.currentUid;
                strArr[8] = "channel_id";
                strArr[9] = this.loginChannel;
                strArr[10] = "jfGameId";
                strArr[11] = this.jfGameId;
                strArr[12] = "udid";
                strArr[13] = this.udid;
                strArr[14] = "protocol_id";
                strArr[15] = String.valueOf(this.protocolManager.getLaunchProtocolId());
                strArr[16] = "protocol_version";
                strArr[17] = String.valueOf(this.protocolManager.getLaunchProtocolVersion());
                saveClientLog(strArr);
            } catch (Throwable unused3) {
            }
        }
        UniSdkUtils.d(TAG, " >> init : protocolManager checkLatestProtocol");
        this.protocolManager.checkLatestProtocol();
        onFinishInitListener.finishInit(0);
        if (this.isOverseaDistribution && SDKRuntime.getInstance().isProtocolLauncher()) {
            SdkMgr.getInst().setPropStr("DISAGREED_PROTOCOLS", ProtocolManager.getInstance().getDisagreedAliasStr());
            JSONObject jSONObjectOnProtocolListChangedCallback = ProtocolManager.getInstance().onProtocolListChangedCallback();
            if (jSONObjectOnProtocolListChangedCallback != null) {
                SdkMgr.getInst().ntExtendFunc(jSONObjectOnProtocolListChangedCallback.toString());
            }
            ProtocolManager.getInstance().consumeDisagreedAliasChanged();
        }
        try {
            Tracker.getInstance().setEventCallback(new com.netease.ntunisdk.external.protocol.Callback<JSONObject>() { // from class: com.netease.ntunisdk.Sdkprotocol.2
                @Override // com.netease.ntunisdk.external.protocol.Callback
                public void onFinish(JSONObject jSONObject) {
                    if (jSONObject == null) {
                        return;
                    }
                    try {
                        jSONObject.put("func", "showProtocol");
                        jSONObject.put("uid", TextUtils.isEmpty(Sdkprotocol.this.currentUid) ? "" : Sdkprotocol.this.currentUid);
                        jSONObject.put("channel_id", Sdkprotocol.this.loginChannel);
                        jSONObject.put("jfGameId", Sdkprotocol.this.jfGameId);
                        jSONObject.put("udid", Sdkprotocol.this.udid);
                        Sdkprotocol.this.saveClientLog(jSONObject);
                    } catch (Throwable unused4) {
                    }
                }
            });
        } catch (Exception unused4) {
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void exit() {
        ProtocolManager protocolManager = this.protocolManager;
        if (protocolManager != null) {
            protocolManager.onDestroy(this.myCtx);
            this.protocolManager.onExit();
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void doSepcialConfigVal(JSONObject jSONObject) {
        String strOptString = jSONObject.optString("PROTOCOL_LAUNCHER");
        doConfigVal(jSONObject, "ReleaseArea");
        Log.d(TAG, "protocol_launcher : " + strOptString);
        if ("1".equals(strOptString)) {
            this.launcherOpen = true;
        }
    }

    public void setShowProtocolActivity(Activity activity) {
        this.mShowProtocolActivity = activity;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void showCompactView(boolean z) {
        UniSdkUtils.d(TAG, " >> showCompactView : " + z);
        this.currentUid = SdkMgr.getInst().getPropStr(ConstProp.UID, "");
        initProtocolManager();
        UniSdkUtils.d(TAG, " >> showCompactView : " + z + ",uid:" + this.currentUid);
        if (!z) {
            UniSdkUtils.d(TAG, " >> launcherOpen : " + this.launcherOpen + ",isOverseaDistribution:" + this.isOverseaDistribution);
            if (this.launcherOpen && !this.isOverseaDistribution) {
                ((SdkBase) SdkMgr.getInst()).protocolFinish(3);
                return;
            }
        }
        if (this.mShowProtocolActivity == null) {
            this.mShowProtocolActivity = (Activity) this.myCtx;
        }
        int propInt = SdkMgr.getInst().getPropInt(ConstProp.ENABLE_RTL, 0);
        UniSdkUtils.d(TAG, " >> ENABLE_RTL : " + propInt);
        SDKRuntime.getInstance().setRTLLayout(propInt == 1);
        if (z) {
            this.protocolManager.showProtocol(this.mShowProtocolActivity, this.currentUid);
        } else {
            this.protocolManager.showProtocolIfNeed(this.mShowProtocolActivity, this.currentUid);
        }
    }

    private ProtocolProp getProtocolProp() {
        ProtocolProp prop = this.protocolManager.getProp();
        if (prop == null) {
            prop = new ProtocolProp();
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.NT_COMPACT_URL);
        if (!TextUtils.isEmpty(propStr)) {
            prop.setUrl(propStr);
        }
        String propStr2 = SdkMgr.getInst().getPropStr("NT_COMPACT_URL_TYPE");
        if (!TextUtils.isEmpty(propStr2)) {
            prop.setUrlType(propStr2);
        }
        String propStr3 = SdkMgr.getInst().getPropStr("NT_COMPACT_OFFLINE_GAME");
        if (!TextUtils.isEmpty(propStr3)) {
            prop.setOfflineGameFlag(propStr3);
        }
        String propStr4 = SdkMgr.getInst().getPropStr("NT_COMPACT_NEW_OFFLINE");
        if (!TextUtils.isEmpty(propStr4)) {
            prop.setNewOfflineFlag(propStr4);
        }
        String propStr5 = SdkMgr.getInst().getPropStr("NT_COMPACT_SHOW_TITLE");
        if (!TextUtils.isEmpty(propStr5)) {
            prop.setShowTitleFlag(propStr5);
        }
        String propStr6 = SdkMgr.getInst().getPropStr(ConstProp.NT_COMPACT_SHOW_AGREE_LINE);
        if (!TextUtils.isEmpty(propStr6)) {
            prop.setShowAgreeLineFlag(propStr6);
        }
        String propStr7 = SdkMgr.getInst().getPropStr(ConstProp.NT_COMPACT_AGREE_LINE_TEXT);
        if (!TextUtils.isEmpty(propStr7)) {
            prop.setAgreeLineText(propStr7);
        }
        String propStr8 = SdkMgr.getInst().getPropStr("NT_COMPACT_LOCALE");
        if (!TextUtils.isEmpty(propStr8)) {
            prop.setLocale(propStr8);
        }
        String string = ResUtils.getString(this.myCtx, "protocol_issuer_name");
        if (!TextUtils.isEmpty(string)) {
            prop.setIssuer(string);
        }
        prop.setLanguage(SdkMgr.getInst().getPropStr(ConstProp.LANGUAGE_CODE));
        prop.setGameId(SdkMgr.getInst().getPropStr("JF_GAMEID"));
        String propStr9 = SdkMgr.getInst().getPropStr("COMPACT_SCENE");
        if (!TextUtils.isEmpty(propStr9)) {
            prop.setScene(propStr9);
        }
        return prop;
    }

    private String getAppName(Context context) {
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) {
        try {
            UniSdkUtils.d(TAG, "extendFunc:" + str);
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            if ("showProtocolInLogin".equals(strOptString)) {
                showProtocolInLogin(jSONObject);
                return;
            }
            boolean z = true;
            if ("acceptProtocol".equals(strOptString)) {
                String strOptString2 = jSONObject.optString("uid", "");
                this.protocolManager.acceptProtocol(strOptString2);
                this.currentUid = strOptString2;
                try {
                    saveClientLog("func", "acceptProtocol", OneTrackParams.XMSdkParams.STEP, "show_protocol_done", "accept", "1", "row", str, "uid", strOptString2, "channel_id", this.loginChannel, "jfGameId", this.jfGameId, "udid", this.udid, "protocol_id", String.valueOf(this.protocolManager.getCurrentProtocolId()), "protocol_version", String.valueOf(this.protocolManager.getCurrentProtocolVersion()));
                    return;
                } catch (Throwable unused) {
                    return;
                }
            }
            if ("uploadClassesDirectly".equals(strOptString)) {
                uploadClassesDirectly();
                return;
            }
            if ("updateProtocolConfig".equals(strOptString)) {
                initProtocolManager();
                int propInt = SdkMgr.getInst().getPropInt(ConstProp.ENABLE_RTL, 0);
                UniSdkUtils.d(TAG, " >> ENABLE_RTL : " + propInt);
                SDKRuntime sDKRuntime = SDKRuntime.getInstance();
                if (propInt != 1) {
                    z = false;
                }
                sDKRuntime.setRTLLayout(z);
                return;
            }
            if ("showProtocol".equals(strOptString)) {
                String strOptString3 = jSONObject.optString("alias", "");
                String strOptString4 = jSONObject.optString("uid", "");
                if (TextUtils.isEmpty(strOptString4)) {
                    strOptString4 = SdkMgr.getInst().getPropStr(ConstProp.UID, "");
                }
                initProtocolManager();
                ProtocolManager.getInstance().showProtocol((Activity) this.myCtx, strOptString4, strOptString3);
                return;
            }
            if ("queryAgreedProtocols".equals(strOptString)) {
                String strOptString5 = jSONObject.optString("uid", "");
                if (TextUtils.isEmpty(strOptString5)) {
                    strOptString5 = SdkMgr.getInst().getPropStr(ConstProp.UID, "");
                }
                JSONObject jSONObjectQueryAgreedProtocols = this.protocolManager.queryAgreedProtocols(strOptString5);
                if (jSONObjectQueryAgreedProtocols != null) {
                    extendFuncCall(jSONObjectQueryAgreedProtocols.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnConfigurationChanged(Configuration configuration) {
        UniSdkUtils.d(TAG, "sdkOnConfigurationChanged");
        initProtocolManager();
        this.protocolManager.handleOnConfigurationChanged((Activity) this.myCtx, configuration);
    }

    public void showProtocolInLogin(final JSONObject jSONObject) {
        UniSdkUtils.d(TAG, "showProtocolInLogin>>");
        initProtocolManager();
        UniSdkUtils.d(TAG, "isDelayShow:" + SDKRuntime.getInstance().isDelayShow());
        final int iOptInt = -1;
        try {
            iOptInt = jSONObject.optInt("loginDoneCode");
            String strOptString = jSONObject.optString(VideoViewManager.PROP_SRC, "");
            if (SDKRuntime.getInstance().isDelayShow() && "login".equalsIgnoreCase(strOptString)) {
                UniSdkUtils.d(TAG, "showProtocolInLogin>> DelayShow");
                showProtocolInLoginCallback(1, iOptInt);
                return;
            }
        } catch (Exception unused) {
        }
        int propInt = SdkMgr.getInst().getPropInt(ConstProp.ENABLE_RTL, 0);
        UniSdkUtils.d(TAG, "showProtocolInLogin>> ENABLE_RTL : " + propInt);
        SDKRuntime.getInstance().setRTLLayout(propInt == 1);
        this.currentUid = jSONObject.optString("uid", "");
        this.protocolCallbackForLogin = new ProtocolCallback() { // from class: com.netease.ntunisdk.Sdkprotocol.3
            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onOpen() {
            }

            @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
            public void onFinish(int i) throws JSONException {
                int i2 = (i == 3 || i == 0 || i == 4) ? 1 : i;
                UniSdkUtils.d(Sdkprotocol.TAG, "onFinish[protocolCallbackForLogin], code = " + i2 + ", loginDoneCode:" + iOptInt);
                Sdkprotocol.this.showProtocolInLoginCallback(i2, iOptInt);
                if (i != 2) {
                    try {
                        Sdkprotocol sdkprotocol = Sdkprotocol.this;
                        String[] strArr = new String[20];
                        strArr[0] = "func";
                        strArr[1] = "showProtocolInLogin";
                        strArr[2] = OneTrackParams.XMSdkParams.STEP;
                        strArr[3] = "show_protocol_done";
                        strArr[4] = "accept";
                        strArr[5] = String.valueOf(i);
                        strArr[6] = "row";
                        strArr[7] = jSONObject.toString();
                        strArr[8] = "uid";
                        strArr[9] = TextUtils.isEmpty(Sdkprotocol.this.currentUid) ? "" : Sdkprotocol.this.currentUid;
                        strArr[10] = "channel_id";
                        strArr[11] = Sdkprotocol.this.loginChannel;
                        strArr[12] = "jfGameId";
                        strArr[13] = Sdkprotocol.this.jfGameId;
                        strArr[14] = "udid";
                        strArr[15] = Sdkprotocol.this.udid;
                        strArr[16] = "protocol_id";
                        strArr[17] = String.valueOf(Sdkprotocol.this.protocolManager.getCurrentProtocolId());
                        strArr[18] = "protocol_version";
                        strArr[19] = String.valueOf(Sdkprotocol.this.protocolManager.getCurrentProtocolVersion());
                        sdkprotocol.saveClientLog(strArr);
                    } catch (Throwable unused2) {
                    }
                }
            }
        };
        this.protocolManager.showProtocolIfNeed((Activity) this.myCtx, this.currentUid);
    }

    public void showProtocolInLoginCallback(int i, int i2) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "showProtocolInLoginCallback");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("code", i);
            if (i2 != -1) {
                jSONObject2.put("loginDoneCode", i2);
            }
            jSONObject.put("result", jSONObject2);
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
            this.protocolCallbackForLogin = null;
        } catch (Exception unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void gameLoginSuccess() {
        uploadClasses();
    }

    private void uploadClasses() {
        if (this.isOverseaDistribution) {
            return;
        }
        UniSdkUtils.d(TAG, "upload classes");
        this.jfGameId = SdkMgr.getInst().getPropStr("JF_GAMEID");
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.APP_CHANNEL);
        this.appChannel = propStr;
        this.protocolManager.syncClasses(this.jfGameId, propStr, APP_KEY);
    }

    private void uploadClassesDirectly() {
        if (this.isOverseaDistribution) {
            return;
        }
        UniSdkUtils.d(TAG, "upload classes");
        this.jfGameId = SdkMgr.getInst().getPropStr("JF_GAMEID");
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.APP_CHANNEL);
        this.appChannel = propStr;
        this.protocolManager.syncClasses(this.jfGameId, propStr, APP_KEY);
    }

    private void initProtocolManager() {
        ProtocolManager protocolManager = ProtocolManager.getInstance();
        this.protocolManager = protocolManager;
        protocolManager.setCallback(this.protocolCallback);
        this.protocolManager.init((Activity) this.myCtx);
        this.protocolManager.setHasCustomProtocol(SdkMgr.getInst().hasFeature("FEATURE_USE_CUSTOM_PROTOCOL"));
        this.protocolManager.setSilentMode(SdkMgr.getInst().getPropInt("PROTOCOL_SILENT_MODE", 0) == 1);
        this.protocolManager.setProp(getProtocolProp());
        this.protocolManager.setUniSDKProxy(new ConcreteUniSDKProxy());
        int propInt = SdkMgr.getInst().getPropInt("PROTOCOL_DELAY_SHOW", -1);
        UniSdkUtils.d(TAG, "PROTOCOL_DELAY_SHOW:" + propInt);
        if (propInt != -1) {
            this.protocolManager.setDelayShow(propInt == 1);
        }
        int propInt2 = SdkMgr.getInst().getPropInt("PROTOCOL_NOT_EXIT", -1);
        UniSdkUtils.d(TAG, "PROTOCOL_NOT_EXIT:" + propInt2);
        if (propInt2 != -1) {
            this.protocolManager.setNoKillProcess(propInt2 == 1);
        }
    }

    public static class ConcreteUniSDKProxy extends UniSDKProxy {
        @Override // com.netease.ntunisdk.external.protocol.UniSDKProxy
        public boolean isSupportShortCut() {
            try {
                if (SdkMgr.getInst() != null) {
                    return SdkMgr.getInst().getPropInt("CURRENT_SHORTCUT_MAIN_RUNNING", 0) == 1;
                }
                return false;
            } catch (Throwable unused) {
                return false;
            }
        }

        @Override // com.netease.ntunisdk.external.protocol.UniSDKProxy
        public boolean hasAppRunning() {
            return SdkMgr.getInst() != null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveClientLog(String... strArr) {
        JSONObject jSONObject = new JSONObject();
        if (strArr != null) {
            try {
                if (1 < strArr.length) {
                    for (int i = 0; i < strArr.length - 1; i += 2) {
                        jSONObject.putOpt(strArr[i], strArr[i + 1]);
                    }
                }
            } catch (Throwable th) {
                UniSdkUtils.w(TAG, th.getMessage());
                return;
            }
        }
        super.saveClientLog(null, jSONObject.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveClientLog(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        try {
            super.saveClientLog(null, jSONObject.toString());
        } catch (Throwable th) {
            UniSdkUtils.w(TAG, th.getMessage());
        }
    }
}