package com.netease.ntunisdk.modules.deviceinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeviceInfoModule extends BaseModules {
    public static final String DISABLE_ACCESS_DATA = "DISABLE_ACCESS_DATA";
    public static final String EB = "EB";
    public static final String ENABLE_FAKE_ABOUT_ID = "ENABLE_FAKE_ABOUT_ID";
    public static final String ISSUER_DOMAIN = "ISSUER_DOMAIN";
    public static final String ISSUER_ID = "ISSUER_ID";
    private static final String MODEL_NAME = "deviceInfo";
    public static final String NO_ANDROIDID = "NO_ANDROIDID";
    private static final long REFRESH_CACHE_INTERVAL = 900000;
    private static final String TAG = "UNISDK DeviceInfoModule";
    private static final String VERSION = "1.5.7";
    public static boolean disableAccessData;
    private static long refreshCacheStartTime = System.currentTimeMillis();
    private ConnectivityManager connectivityManager;
    private CustomNetworkCallback customNetworkCallback;
    private boolean enableFake;
    private boolean hasRefreshData;
    private boolean hasRequestGaid;
    public boolean isOversea;
    public String issuerDomain;
    public String issuerId;
    private NetworkStateReceiver networkStateReceiver;
    private boolean noAndroidId;
    private Hashtable<String, String> propDict;

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getModuleName() {
        return MODEL_NAME;
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getVersion() {
        return VERSION;
    }

    @Override // com.netease.ntunisdk.modules.base.Lifecycle
    protected void onPause() {
    }

    @Override // com.netease.ntunisdk.modules.base.Lifecycle
    protected void onStart() {
    }

    public DeviceInfoModule(Context context, IModulesCall iModulesCall) throws IOException {
        super(context, iModulesCall);
        this.hasRequestGaid = false;
        this.hasRefreshData = false;
        this.enableFake = false;
        this.noAndroidId = false;
        this.isOversea = false;
        this.issuerId = "";
        this.issuerDomain = "";
        this.networkStateReceiver = null;
        this.customNetworkCallback = null;
        this.connectivityManager = null;
        this.propDict = new Hashtable<>();
        readLibraryConfig();
        readNtUniSDKGameConfig();
        if (!this.hasRequestGaid && !disableAccessData) {
            GaidUtils.requestGaid(context, this);
            this.hasRequestGaid = true;
        }
        registerNetworkReceiver();
        judgeReqLocation(context);
    }

    private void judgeReqLocation(Context context) {
        if (Build.VERSION.SDK_INT >= 23 && (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0 || context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0)) {
            DeviceUtils.isReqLocation = true;
        }
        LogModule.d(TAG, "judgeReqLocation isReqLocation = " + DeviceUtils.isReqLocation);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002a A[Catch: all -> 0x0025, Exception -> 0x0055, TRY_LEAVE, TryCatch #8 {Exception -> 0x0055, blocks: (B:10:0x001e, B:14:0x002a, B:18:0x0035, B:23:0x0041), top: B:68:0x001e, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0035 A[Catch: all -> 0x0025, Exception -> 0x0055, TRY_ENTER, TRY_LEAVE, TryCatch #8 {Exception -> 0x0055, blocks: (B:10:0x001e, B:14:0x002a, B:18:0x0035, B:23:0x0041), top: B:68:0x001e, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readLibraryConfig() throws java.io.IOException {
        /*
            r7 = this;
            java.lang.String r0 = "UNISDK DeviceInfoModule"
            android.content.Context r1 = r7.context
            if (r1 != 0) goto L7
            return
        L7:
            java.lang.String r1 = "ntunisdk_config"
            android.content.Context r2 = r7.context
            android.content.res.AssetManager r2 = r2.getAssets()
            r3 = 3
            r4 = 0
            java.io.InputStream r5 = r2.open(r1, r3)     // Catch: java.lang.Exception -> L16
            goto L1c
        L16:
            java.lang.String r5 = "fail to read ntunisdk_config, try ntunisdk.cfg"
            com.netease.ntunisdk.modules.base.utils.LogModule.i(r0, r5)
            r5 = r4
        L1c:
            if (r5 != 0) goto L28
            java.lang.String r1 = "ntunisdk.cfg"
            java.io.InputStream r5 = r2.open(r1, r3)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            goto L28
        L25:
            r0 = move-exception
            goto Lbb
        L28:
            if (r5 != 0) goto L35
            java.lang.String r2 = "ntunisdk_config/ntunisdk.cfg null"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r2)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            if (r5 == 0) goto L34
            r5.close()     // Catch: java.io.IOException -> L34
        L34:
            return
        L35:
            int r2 = r5.available()     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            if (r2 != 0) goto L41
            if (r5 == 0) goto L40
            r5.close()     // Catch: java.io.IOException -> L40
        L40:
            return
        L41:
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            r5.read(r2)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            java.lang.String r3 = new java.lang.String     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            java.lang.String r6 = "UTF-8"
            r3.<init>(r2, r6)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L55
            if (r5 == 0) goto L60
            r5.close()     // Catch: java.io.IOException -> L53
            goto L60
        L53:
            goto L60
        L55:
            java.lang.String r2 = "ntunisdk_config/ntunisdk.cfg config not found"
            com.netease.ntunisdk.modules.base.utils.LogModule.i(r0, r2)     // Catch: java.lang.Throwable -> L25
            if (r5 == 0) goto L5f
            r5.close()     // Catch: java.io.IOException -> L5f
        L5f:
            r3 = r4
        L60:
            if (r3 != 0) goto L77
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            java.lang.String r1 = " is null"
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r1)
            return
        L77:
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r0, r3)
            java.lang.String r2 = "\uff1a"
            boolean r2 = r3.contains(r2)
            if (r2 != 0) goto L92
            java.lang.String r2 = "\u201c"
            boolean r2 = r3.contains(r2)
            if (r2 != 0) goto L92
            java.lang.String r2 = "\u201d"
            boolean r2 = r3.contains(r2)
            if (r2 == 0) goto La6
        L92:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            java.lang.String r1 = "\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26"
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            com.netease.ntunisdk.modules.base.utils.LogModule.e(r0, r1)
        La6:
            org.json.JSONTokener r1 = new org.json.JSONTokener     // Catch: java.lang.Exception -> Lb5
            r1.<init>(r3)     // Catch: java.lang.Exception -> Lb5
            java.lang.Object r1 = r1.nextValue()     // Catch: java.lang.Exception -> Lb5
            org.json.JSONObject r1 = (org.json.JSONObject) r1     // Catch: java.lang.Exception -> Lb5
            r7.doConfigSet(r1)     // Catch: java.lang.Exception -> Lb5
            goto Lba
        Lb5:
            java.lang.String r1 = "ntunisdk_config/ntunisdk.cfg config parse to json error"
            com.netease.ntunisdk.modules.base.utils.LogModule.i(r0, r1)
        Lba:
            return
        Lbb:
            if (r5 == 0) goto Lc0
            r5.close()     // Catch: java.io.IOException -> Lc0
        Lc0:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.deviceinfo.DeviceInfoModule.readLibraryConfig():void");
    }

    private void readNtUniSDKGameConfig() throws IOException {
        FileInputStream fileInputStreamOpenFileInput;
        int iAvailable;
        if (this.context == null) {
            return;
        }
        String str = null;
        try {
            fileInputStreamOpenFileInput = this.context.openFileInput("ntunisdk_game_config");
        } catch (Exception unused) {
            fileInputStreamOpenFileInput = null;
        }
        if (fileInputStreamOpenFileInput == null) {
            LogModule.d(TAG, "ntunisdk_game_config is null");
            return;
        }
        try {
            try {
                iAvailable = fileInputStreamOpenFileInput.available();
            } catch (Throwable th) {
                if (fileInputStreamOpenFileInput != null) {
                    try {
                        fileInputStreamOpenFileInput.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Exception unused3) {
            LogModule.i(TAG, "ntunisdk_config/ntunisdk.cfg config not found");
            if (fileInputStreamOpenFileInput != null) {
                try {
                    fileInputStreamOpenFileInput.close();
                } catch (IOException unused4) {
                }
            }
        }
        if (iAvailable == 0) {
            if (fileInputStreamOpenFileInput != null) {
                try {
                    fileInputStreamOpenFileInput.close();
                    return;
                } catch (IOException unused5) {
                    return;
                }
            }
            return;
        }
        byte[] bArr = new byte[iAvailable];
        fileInputStreamOpenFileInput.read(bArr);
        String str2 = new String(bArr, "UTF-8");
        if (fileInputStreamOpenFileInput != null) {
            try {
                fileInputStreamOpenFileInput.close();
            } catch (IOException unused6) {
            }
        }
        str = str2;
        if (str == null) {
            LogModule.d(TAG, "ntunisdk_game_config is null");
            return;
        }
        LogModule.d(TAG, str);
        LogModule.d(TAG, "readNtUniSDKGameConfigImpl: " + str);
        if (str.contains("\uff1a") || str.contains("\u201c") || str.contains("\u201d")) {
            LogModule.e(TAG, "ntunisdk_game_config\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26");
        }
        try {
            doConfigSet(new JSONObject(str));
        } catch (Exception unused7) {
            LogModule.i(TAG, "ntunisdk_game_config config parse to json error");
        }
    }

    private void doConfigSet(JSONObject jSONObject) throws JSONException {
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (!TextUtils.isEmpty(next)) {
                String string = jSONObject.getString(next);
                LogModule.d(TAG, "key: " + next + " value:" + string);
                this.propDict.put(next, string);
            }
        }
        if (this.propDict.containsKey("NO_ANDROIDID")) {
            if ("1".equals(this.propDict.get("NO_ANDROIDID"))) {
                this.noAndroidId = true;
            } else {
                this.noAndroidId = false;
            }
            LogModule.d(TAG, "noAndroidId:" + this.noAndroidId);
        }
        if (this.propDict.containsKey("ENABLE_FAKE_ABOUT_ID")) {
            if ("1".equals(this.propDict.get("ENABLE_FAKE_ABOUT_ID"))) {
                this.enableFake = true;
            } else {
                this.enableFake = false;
            }
            LogModule.d(TAG, "enableFake:" + this.enableFake);
        }
        if (this.propDict.containsKey("EB")) {
            if ("1".equals(this.propDict.get("EB"))) {
                this.isOversea = true;
                DeviceUtils.isOversea = true;
            } else {
                this.isOversea = false;
                DeviceUtils.isOversea = false;
            }
            LogModule.d(TAG, "isOversea:" + this.isOversea);
        }
        if (this.propDict.containsKey(DISABLE_ACCESS_DATA)) {
            if ("1".equals(this.propDict.get(DISABLE_ACCESS_DATA))) {
                disableAccessData = true;
            } else {
                disableAccessData = false;
            }
            LogModule.d(TAG, "disableAccessData:" + disableAccessData);
        }
        if (this.propDict.containsKey(ISSUER_ID)) {
            this.issuerId = this.propDict.get(ISSUER_ID);
        }
        if (this.propDict.containsKey(ISSUER_DOMAIN)) {
            this.issuerDomain = this.propDict.get(ISSUER_DOMAIN);
        }
        LogModule.d(TAG, "issuerId:" + this.issuerId + " issuerDomain:" + this.issuerDomain);
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String extendFunc(String str, String str2, String str3, Object... objArr) {
        try {
            JSONObject jSONObject = new JSONObject(str3);
            String strOptString = jSONObject.optString("methodId");
            if ("setImei".equalsIgnoreCase(strOptString)) {
                DeviceUtils.setImei(jSONObject.optString("imei"));
                return "";
            }
            if ("setImsi".equalsIgnoreCase(strOptString)) {
                DeviceUtils.setImsi(jSONObject.optString("imsi"));
                return "";
            }
            if ("setAndroidId".equalsIgnoreCase(strOptString)) {
                DeviceUtils.setAndroidId(jSONObject.optString("androidId"));
                return "";
            }
            if ("setMacAddress".equalsIgnoreCase(strOptString)) {
                DeviceUtils.setMacAddress(jSONObject.optString("macAddress"));
                return "";
            }
            if ("setTimeZone".equalsIgnoreCase(strOptString)) {
                DeviceUtils.setTimeZone(jSONObject.optString("timeZone"));
                return "";
            }
            if ("setTimeArea".equalsIgnoreCase(strOptString)) {
                DeviceUtils.setTimeArea(jSONObject.optString("timeArea"));
                return "";
            }
            if ("setMinorStatus".equalsIgnoreCase(strOptString)) {
                String strOptString2 = jSONObject.optString("status");
                if (SDefine.iW.equalsIgnoreCase(strOptString2)) {
                    disableAccessData = true;
                    DeviceUtils.hasRefreshWifiList = false;
                } else if (SDefine.iX.equalsIgnoreCase(strOptString2)) {
                    disableAccessData = false;
                    if (!this.hasRequestGaid) {
                        GaidUtils.requestGaid(this.context, this);
                        this.hasRequestGaid = true;
                    }
                    if (!this.hasRefreshData) {
                        DeviceUtils.hasRefreshWifiList = true;
                        this.hasRefreshData = true;
                    }
                }
                return "";
            }
            if ("getSerial".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getSerial(this.context);
            }
            if ("getImei".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getMobileIMEI(this.context);
            }
            if ("getImsi".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getMobileIMSI(this.context);
            }
            if ("getTransId".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getTransid(this.context, jSONObject.optBoolean("isNoAndroidId", this.noAndroidId), jSONObject.optBoolean("isFake", this.enableFake));
            }
            if ("getUDID".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getDeviceUDID(this.context, jSONObject.optBoolean("isNoAndroidId", this.noAndroidId), jSONObject.optBoolean("isFake", this.enableFake));
            }
            if ("getUnisdkDeviceId".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getUnisdkDeviceId(this.context, jSONObject.optBoolean("isNoAndroidId", this.noAndroidId), jSONObject.optBoolean("isFake", this.enableFake));
            }
            if ("getAndroidId".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getAndroidId(this.context);
            }
            if ("getMacAddress".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getMacAddress(this.context);
            }
            if ("getLocalIpAddress".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getLocalIpAddress(this.context);
            }
            if ("getDeviceBaseInfo".equalsIgnoreCase(strOptString)) {
                return getDeviceBaseInfo(jSONObject);
            }
            if ("getTimeZone".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getTimeZone();
            }
            if ("getAreaZone".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getAreaZone();
            }
            if ("getCellId".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getCellId(this.context);
            }
            if ("getWifiSignal".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getWifiSignal(this.context);
            }
            if ("getGateWay".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getGateWay(this.context);
            }
            if (UniWebView.ACTION_GETNETWORKTYPE.equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getNetworkType(this.context);
            }
            if ("getNetworkType2".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getNetworkType2(this.context, jSONObject.optBoolean("reacquire"));
            }
            if ("getNetworkType4Downloader".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getNetworkType4Downloader(this.context, jSONObject.optBoolean("reacquire"));
            }
            if ("ntGetNetworktype".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.ntGetNetworktype(this.context);
            }
            if ("getNetworkInfoJson".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getNetworkInfoJson(this.context);
            }
            if ("getSystemLanguage".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getSystemLanguage(jSONObject.optBoolean("reacquire"));
            }
            if ("getSimCountryIso".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getSimCountryIso(this.context, jSONObject.optBoolean("reacquire"));
            }
            if ("setFake".equalsIgnoreCase(strOptString)) {
                setEnableFake(jSONObject);
                return "";
            }
            if ("getWifiListJson".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getWifiListJson(this.context, jSONObject.optBoolean("reacquire"));
            }
            if ("getAppVersionCode".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getAppVersionCode(this.context);
            }
            if ("getAppVersionName".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getAppVersionName(this.context);
            }
            if ("getOsVer".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getOsVer(this.context);
            }
            if ("getMobileType".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getMobileType(this.context);
            }
            if ("getAppFilesCacheDir".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getAppFilesCacheDir(this.context);
            }
            if ("getFirstDeviceId".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getFirstDeviceId(this.context);
            }
            if ("getAppSetID".equalsIgnoreCase(strOptString)) {
                DeviceUtils.getAppSetID(this.context, jSONObject, this);
                return "";
            }
            if ("getAppOccupiedStorage".equalsIgnoreCase(strOptString)) {
                DeviceUtils.getAppOccupiedStorage(this.context, jSONObject, this);
                return "";
            }
            if ("BaseSupportDeviceInfo".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.baseSupportDeviceInfo(this.context, jSONObject);
            }
            if ("getIssuerId".equalsIgnoreCase(strOptString)) {
                return this.issuerId;
            }
            if ("getIssuerDomain".equalsIgnoreCase(strOptString)) {
                return this.issuerDomain;
            }
            if ("getIssuerUrl".equalsIgnoreCase(strOptString)) {
                return DeviceUtils.getIssuerUrl(jSONObject, this.issuerDomain);
            }
            return "getAppChannel".equalsIgnoreCase(strOptString) ? DeviceUtils.getAppChannel(this.context) : "unknow";
        } catch (JSONException e) {
            e.printStackTrace();
            return "unknow";
        }
    }

    private String getDeviceBaseInfo(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("imei", DeviceUtils.getMobileIMEI(this.context));
            boolean zOptBoolean = jSONObject.optBoolean("isNoAndroidId", this.noAndroidId);
            boolean zOptBoolean2 = jSONObject.optBoolean("isFake", this.enableFake);
            jSONObject2.put("udid", DeviceUtils.getDeviceUDID(this.context, zOptBoolean, zOptBoolean2));
            jSONObject2.put("transId", DeviceUtils.getTransid(this.context, zOptBoolean, zOptBoolean2));
            jSONObject2.put("unisdkDeviceId", DeviceUtils.getUnisdkDeviceId(this.context, zOptBoolean, zOptBoolean2));
            jSONObject2.put("localIpAddress", DeviceUtils.getLocalIpAddress(this.context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject2.toString();
    }

    public void gaidCallback(String str) {
        LogModule.d(TAG, "gaidCallback:" + str);
        callback("native", ConstProp.UNISDKBASE, str);
    }

    private void setEnableFake(JSONObject jSONObject) {
        this.enableFake = jSONObject.optBoolean("isFake", this.enableFake);
        LogModule.i(TAG, "enableFake:" + this.enableFake);
    }

    @Override // com.netease.ntunisdk.modules.base.Lifecycle
    protected void onResume() {
        registerNetworkReceiver();
        checkIfRefreshCache();
    }

    private void checkIfRefreshCache() {
        try {
            if (System.currentTimeMillis() - refreshCacheStartTime > REFRESH_CACHE_INTERVAL) {
                refreshNetworkCache(this.context);
                refreshCacheStartTime = System.currentTimeMillis();
            }
        } catch (Exception e) {
            LogModule.e(TAG, "checkIfRefreshCache error, message = " + e.getMessage());
        }
    }

    @Override // com.netease.ntunisdk.modules.base.Lifecycle
    protected void onStop() {
        refreshCacheStartTime = System.currentTimeMillis();
    }

    @Override // com.netease.ntunisdk.modules.base.Lifecycle
    public void onDestroy() {
        try {
            if (this.customNetworkCallback != null) {
                if (this.connectivityManager == null) {
                    this.connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    this.connectivityManager.unregisterNetworkCallback(this.customNetworkCallback);
                }
                this.customNetworkCallback = null;
                LogModule.i(TAG, "unregister customNetworkCallback...");
                DeviceUtils.commonNetworkInfo = null;
                DeviceUtils.commonNetworkCapabilities = null;
            }
            if (this.networkStateReceiver != null) {
                this.context.unregisterReceiver(this.networkStateReceiver);
                this.networkStateReceiver = null;
                LogModule.i(TAG, "unregister NetworkStateReceiver...");
                DeviceUtils.commonNetworkInfo = null;
                DeviceUtils.commonNetworkCapabilities = null;
            }
        } catch (Exception e) {
            LogModule.e(TAG, "unregister NetworkStateReceiver error, message = " + e.getMessage());
        }
    }

    private void registerNetworkReceiver() {
        try {
            if (this.connectivityManager == null) {
                this.connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
            }
            if (Build.VERSION.SDK_INT >= 24) {
                if (this.customNetworkCallback == null) {
                    LogModule.i(TAG, "register DefaultNetworkCallback...");
                    this.customNetworkCallback = new CustomNetworkCallback();
                    this.connectivityManager.registerDefaultNetworkCallback(this.customNetworkCallback);
                    DeviceUtils.isFallbackNetReceiver = false;
                    if (this.networkStateReceiver != null) {
                        this.context.unregisterReceiver(this.networkStateReceiver);
                        this.networkStateReceiver = null;
                        LogModule.i(TAG, "unregister NetworkStateReceiver...");
                        return;
                    }
                    return;
                }
                return;
            }
            if (this.networkStateReceiver == null) {
                LogModule.i(TAG, "register NetworkStateReceiver...");
                this.networkStateReceiver = new NetworkStateReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                this.context.registerReceiver(this.networkStateReceiver, intentFilter);
                DeviceUtils.isFallbackNetReceiver = true;
            }
        } catch (Exception e) {
            LogModule.e(TAG, "registerNetworkReceiver error, message = " + e.getMessage());
            try {
                if (this.networkStateReceiver == null) {
                    LogModule.i(TAG, "register NetworkStateReceiver...");
                    this.networkStateReceiver = new NetworkStateReceiver();
                    IntentFilter intentFilter2 = new IntentFilter();
                    intentFilter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    this.context.registerReceiver(this.networkStateReceiver, intentFilter2);
                    DeviceUtils.isFallbackNetReceiver = true;
                }
            } catch (Exception unused) {
                LogModule.e(TAG, "register NetworkStateReceiver error, message = " + e.getMessage());
            }
        }
    }

    class NetworkStateReceiver extends BroadcastReceiver {
        private boolean isFirst = true;

        NetworkStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                LogModule.i(DeviceInfoModule.TAG, "NetworkStateReceiver onReceive...");
                if (this.isFirst) {
                    this.isFirst = false;
                } else {
                    LogModule.i(DeviceInfoModule.TAG, "Network state changed...");
                    DeviceInfoModule.this.refreshNetworkCache(context);
                }
            } catch (Exception e) {
                LogModule.e(DeviceInfoModule.TAG, "onReceive error, message = " + e.getMessage());
            }
        }
    }

    class CustomNetworkCallback extends ConnectivityManager.NetworkCallback {
        private boolean isFirst = true;
        private NetworkCapabilities lastNetworkCapabilities = null;

        CustomNetworkCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            LogModule.i(DeviceInfoModule.TAG, "CustomNetworkCallback: " + networkCapabilities);
            if (networkCapabilities == null || !filterCapabilitiesChanged(networkCapabilities)) {
                return;
            }
            try {
                LogModule.i(DeviceInfoModule.TAG, "CustomNetworkCallback onCapabilitiesChanged...");
                if (this.isFirst) {
                    this.isFirst = false;
                    return;
                }
                LogModule.i(DeviceInfoModule.TAG, "CustomNetworkCallback Network state changed...");
                DeviceInfoModule.this.refreshNetworkCache(DeviceInfoModule.this.context);
                this.lastNetworkCapabilities = networkCapabilities;
            } catch (Exception e) {
                LogModule.e(DeviceInfoModule.TAG, "onCapabilitiesChanged error, message = " + e.getMessage());
            }
        }

        private boolean filterCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.lastNetworkCapabilities == null) {
                return true;
            }
            if ((this.lastNetworkCapabilities.hasTransport(1) && networkCapabilities.hasTransport(1)) || (this.lastNetworkCapabilities.hasTransport(0) && networkCapabilities.hasTransport(0))) {
                String string = this.lastNetworkCapabilities.toString();
                String string2 = networkCapabilities.toString();
                if (string.substring(0, string.indexOf("LinkUpBandwidth")).equals(string2.substring(0, string2.indexOf("LinkUpBandwidth")))) {
                    return false;
                }
            }
            return true;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            super.onLost(network);
            try {
                LogModule.i(DeviceInfoModule.TAG, "CustomNetworkCallback onLost...");
                LogModule.i(DeviceInfoModule.TAG, "Network state changed...");
                DeviceInfoModule.this.refreshNetworkCache(DeviceInfoModule.this.context);
                this.lastNetworkCapabilities = null;
            } catch (Exception e) {
                LogModule.e(DeviceInfoModule.TAG, "onLost error, message = " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshNetworkCache(Context context) {
        LogModule.i(TAG, "refreshNetworkCache...");
        if (this.connectivityManager == null) {
            this.connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        }
        ConnectivityManager connectivityManager = this.connectivityManager;
        if (connectivityManager != null) {
            DeviceUtils.commonNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (Build.VERSION.SDK_INT >= 24) {
                DeviceUtils.commonNetworkCapabilities = this.connectivityManager.getNetworkCapabilities(this.connectivityManager.getActiveNetwork());
            }
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager != null) {
            DeviceUtils.commonWifiInfo = HookManager.getConnectionInfo(wifiManager);
        }
        callback("native", "", DeviceUtils.getNetworkInfoJson(context));
        callback("native", "", DeviceUtils.getWifiListJson(context, true));
        try {
            callback(com.netease.ntunisdk.modules.base.Constant.JNI_CALL, "", DeviceUtils.getNetworkInfoJson(context));
        } catch (Throwable unused) {
            LogModule.e(TAG, "libNgModules.so is not exists");
        }
    }

    public void onCallback(String str, String str2, String str3) {
        callback(str, str2, str3);
    }
}