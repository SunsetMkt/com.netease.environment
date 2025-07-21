package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import com.alipay.sdk.m.s.a;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.lbs.LegacyPermissionUtil;
import com.netease.ntunisdk.lbs.LocationReporter;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.unilogger.global.Const;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.jose4j.jwx.HeaderParameterNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkAndroidLocation extends SdkBase {
    private static final String CHANNEL = "android_location";
    private static final int CODE_AFTER_LOCATION_RETURN = 5;
    private static final int CODE_DIRECT_GET_NEARBY = 4;
    private static final int CODE_EXTEND_FUNC = 0;
    private static final int CODE_GET_NEIGHBOUR = 6;
    private static final int CODE_GUESS_LOCATION = 9;
    private static final int CODE_PERFORM_OTHER_METHOD = 8;
    private static final int CODE_REPORT_LOC = 7;
    private static final int CODE_REQ_PERMISSION_AT_EXTEND;
    private static final int CODE_REQ_PERMISSION_AT_INIT;
    private static final int CODE_STOP_GPS_DELAY = 10;
    private static final String[] DEVICE_BLACK_LIST;
    private static final int MAX_VALID_ACCURACY = Integer.MAX_VALUE;
    private static final String METHOD_GET_LOCATION = "getLocation";
    private static final String METHOD_GET_NEARBY = "getNearby";
    private static final String METHOD_GET_NEARBY_REQ = "getNearbyReqInfo";
    private static final String METHOD_GET_PROVIDER_ENABLE = "isProviderEnable";
    private static final String METHOD_GET_REQ = "getReqInfo";
    private static final String METHOD_GOTO_LOCATION_SETTING = "gotoLocationSetting";
    private static final String METHOD_HAS_PERMISSION = "lbsAuthorization";
    private static final String METHOD_WEATHER = "weather";
    private static final String SDK_VER = "2.7";
    private static final String SERV_API_VERSION = "v1";
    private static final String TAG = "UniSDK AndroidLocation";
    private static final String TAG_LBS = "lbs";
    private static final String URL_TEST = "https://nglbs.nie.netease.com:8000/";
    private static String mCachedRequestMethod;
    private static final String[] needPermissions;
    private static final Set<String> sMethodSet = new HashSet();
    private LocationManager androidLocationManager;
    private Boolean canGotoLocationSetting;
    private String mCachedGameId;
    private String mCachedMethodId;
    private String mCachedServerName;
    private int mCursor;
    private int mDistance;
    private LocationListener mGpsLocUpdateListener;
    private boolean mHasPermission;
    private boolean mInBackground;
    private boolean mInBlackList;
    private boolean mLbsOpened;
    private int mLimit;
    private LocationListener mLocUpdateListener;
    private Handler mMainHandler;
    private final Map<String, Object> mOtherParams;
    private final Map<String, Object> mOtherQueryParams;
    private int mPeriod;
    private Location mPreNearbyLocation;
    private String mServerId;
    private int mTargetSdkVersion;
    private boolean mUnlimitedTime;
    private boolean mUpdateGoing;
    private Handler mWorkerHandler;
    private String[] runtimePermissions;

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return CHANNEL;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return ConstProp.S_NOT_LOGIN_SESSION;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return "";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return SDK_VER;
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
    public void setFloatBtnVisible(boolean z) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    static {
        sMethodSet.add(METHOD_GET_LOCATION);
        sMethodSet.add(METHOD_GET_NEARBY);
        sMethodSet.add(METHOD_GET_NEARBY_REQ);
        sMethodSet.add(METHOD_GET_REQ);
        sMethodSet.add(METHOD_GET_PROVIDER_ENABLE);
        sMethodSet.add(METHOD_HAS_PERMISSION);
        sMethodSet.add(METHOD_GOTO_LOCATION_SETTING);
        sMethodSet.add(METHOD_WEATHER);
        needPermissions = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
        DEVICE_BLACK_LIST = new String[]{"GIONEE", "A31#OPPO"};
        CODE_REQ_PERMISSION_AT_INIT = Math.abs(TAG.hashCode()) % 65535;
        CODE_REQ_PERMISSION_AT_EXTEND = CODE_REQ_PERMISSION_AT_INIT + 1;
        mCachedRequestMethod = null;
    }

    public SdkAndroidLocation(Context context) {
        super(context);
        this.runtimePermissions = null;
        this.mUpdateGoing = false;
        this.mCachedMethodId = "";
        this.mCachedGameId = null;
        this.mCachedServerName = null;
        this.mHasPermission = false;
        this.mLbsOpened = false;
        this.mOtherParams = new HashMap();
        this.mOtherQueryParams = new HashMap();
        this.mInBlackList = false;
        this.mInBackground = false;
        this.mTargetSdkVersion = 0;
        this.mUnlimitedTime = false;
        setFeature(ConstProp.INNER_MODE_SECOND_CHANNEL, true);
        setFeature(ConstProp.INNER_MODE_NO_PAY, true);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        UniSdkUtils.i(TAG, "init");
        SharedPreferences sharedPreferences = this.myCtx.getSharedPreferences(getChannel(), 0);
        boolean z = sharedPreferences.getBoolean("special", false);
        boolean z2 = sharedPreferences.getBoolean("first", true);
        if (z2) {
            sharedPreferences.edit().putBoolean("first", false).apply();
        }
        if (!z && z2 && verifyPermissions(getRuntimePermissions()) && Build.VERSION.SDK_INT >= 23) {
            sharedPreferences.edit().putBoolean("special", true).apply();
        }
        this.mWorkerHandler = new WorkerHandler(HandlerThreadUtil.getWorkerThread().getLooper(), this);
        this.mMainHandler = new MainHandler(Looper.getMainLooper(), this);
        this.mInBlackList = isBlackListDeivces();
        UniSdkUtils.i(TAG, "this devices is in black-list? - " + this.mInBlackList);
        this.androidLocationManager = (LocationManager) this.myCtx.getSystemService("location");
        this.mLocUpdateListener = new LocationListener() { // from class: com.netease.ntunisdk.SdkAndroidLocation.1
            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                UniSdkUtils.i(SdkAndroidLocation.TAG, "onLocationChanged: " + location);
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
                UniSdkUtils.i(SdkAndroidLocation.TAG, "onStatusChanged: " + str + "/" + i + "/" + bundle);
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
                UniSdkUtils.i(SdkAndroidLocation.TAG, "onProviderEnabled: " + str);
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
                UniSdkUtils.i(SdkAndroidLocation.TAG, "onProviderDisabled: " + str);
            }
        };
        if (onFinishInitListener != null) {
            onFinishInitListener.finishInit(0);
        }
    }

    private boolean hasProvider(LocationManager locationManager, String str) {
        List<String> providers;
        if (locationManager != null && (providers = locationManager.getProviders(false)) != null && !providers.isEmpty()) {
            for (String str2 : providers) {
                if (!TextUtils.isEmpty(str2) && str2.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startLocReqUpdate(boolean z, int i, long j) {
        UniSdkUtils.i(TAG, "startLocReqUpdate: " + z + "/" + i);
        if (this.myCtx == null) {
            UniSdkUtils.e(TAG, "invalid context");
            return false;
        }
        this.mHasPermission = hasOneLbsPermission();
        this.mLbsOpened = isLocationEnabled();
        UniSdkUtils.i(TAG, "hasPermission=" + this.mHasPermission + ",lbsOpened=" + this.mLbsOpened);
        if (this.mHasPermission) {
            UniSdkUtils.i(TAG, "mUpdateGoing=" + this.mUpdateGoing);
            if (!this.mUpdateGoing && hasProvider(this.androidLocationManager, "network")) {
                try {
                    this.androidLocationManager.requestLocationUpdates("network", j, 0.0f, this.mLocUpdateListener, HandlerThreadUtil.getWorkerThread().getLooper());
                    this.mUpdateGoing = true;
                } catch (Throwable unused) {
                }
            }
        } else if (z) {
            checkPermissions(i, getRuntimePermissions());
        }
        return this.mHasPermission;
    }

    private void stopLocation() {
        LocationListener locationListener;
        UniSdkUtils.i(TAG, "destroyLocation");
        Handler handler = this.mWorkerHandler;
        if (handler != null) {
            handler.removeMessages(5);
            this.mWorkerHandler.removeMessages(10);
        }
        stopGps();
        LocationManager locationManager = this.androidLocationManager;
        if (locationManager == null || (locationListener = this.mLocUpdateListener) == null) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        this.mUpdateGoing = false;
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public void exit() {
        stopLocation();
        super.exit();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return getSDKVersion();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnStop() {
        UniSdkUtils.i(TAG, "onStop, all lbs function call will stop");
        this.mInBackground = true;
        stopLocation();
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnResume() {
        this.mInBackground = false;
    }

    private void readOtherParams(JSONObject jSONObject) {
        this.mOtherParams.clear();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String strValueOf = String.valueOf(itKeys.next());
            if (!TextUtils.equals("query", strValueOf)) {
                this.mOtherParams.put(strValueOf, jSONObject.opt(strValueOf));
            }
        }
        this.mOtherQueryParams.clear();
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("query");
        if (jSONObjectOptJSONObject != null) {
            Iterator<String> itKeys2 = jSONObjectOptJSONObject.keys();
            while (itKeys2.hasNext()) {
                String strValueOf2 = String.valueOf(itKeys2.next());
                this.mOtherQueryParams.put(strValueOf2, jSONObjectOptJSONObject.opt(strValueOf2));
            }
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFuncCall(final String str) {
        if (this.myCtx == null || !(this.myCtx instanceof Activity)) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkAndroidLocation.2
            @Override // java.lang.Runnable
            public void run() throws JSONException {
                String string = str;
                try {
                    JSONObject jSONObject = new JSONObject(string);
                    jSONObject.putOpt("channel", SdkAndroidLocation.this.getChannel());
                    string = jSONObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SdkAndroidLocation.super.extendFuncCall(string);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) {
        UniSdkUtils.i(TAG, "extendFunc: " + str);
        Handler handler = this.mWorkerHandler;
        if (handler != null) {
            this.mWorkerHandler.sendMessage(Message.obtain(handler, 0, str));
        }
    }

    private boolean handlePermissionExtendFunc(String str, String str2, final JSONObject jSONObject) {
        if (!CHANNEL.equals(str) || !"requestPermission".equals(str2)) {
            return false;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkAndroidLocation.3
            @Override // java.lang.Runnable
            public void run() {
                int iOptInt = jSONObject.optInt("requestCode");
                if (SdkAndroidLocation.CODE_REQ_PERMISSION_AT_INIT == iOptInt) {
                    SdkAndroidLocation.this.startLocReqUpdate(false, 0, 10000L);
                } else if (SdkAndroidLocation.CODE_REQ_PERMISSION_AT_EXTEND == iOptInt) {
                    SdkAndroidLocation.this.startLocReqUpdate(false, 0, 200L);
                    SdkAndroidLocation.this.doAfterLocationShouldReturn();
                }
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void workerExtendFunc(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            if (handlePermissionExtendFunc(jSONObject.optString("channel"), strOptString, jSONObject)) {
                return;
            }
            String strOptString2 = jSONObject.optString(HeaderParameterNames.AUTHENTICATION_TAG);
            if (TextUtils.isEmpty(strOptString)) {
                UniSdkUtils.e(TAG, "invalid methodId");
                return;
            }
            if ((TextUtils.isEmpty(strOptString2) || !TAG_LBS.equalsIgnoreCase(strOptString2)) && !sMethodSet.contains(strOptString)) {
                UniSdkUtils.w(TAG, "invalid method tag");
                return;
            }
            if (METHOD_GET_PROVIDER_ENABLE.equalsIgnoreCase(strOptString)) {
                returnProviderEnable(jSONObject);
                return;
            }
            if (METHOD_HAS_PERMISSION.equalsIgnoreCase(strOptString)) {
                returnPermissionEnable(jSONObject);
                return;
            }
            if (METHOD_GOTO_LOCATION_SETTING.equalsIgnoreCase(strOptString)) {
                gotoLocationSetting(jSONObject);
                return;
            }
            if (this.mInBackground) {
                UniSdkUtils.w(TAG, "app not in foreground, lbs function will not work");
                return;
            }
            String strOptString3 = jSONObject.optString("token");
            if (!TextUtils.isEmpty(strOptString3)) {
                SdkMgr.getInst().setPropStr(ConstProp.X_LBS_TOKEN, strOptString3);
            }
            String strOptString4 = jSONObject.optString("tips");
            if (!TextUtils.isEmpty(strOptString4)) {
                SdkMgr.getInst().setPropInt(SdkQRCode.ENABLE_UNISDK_PERMISSION_TIPS, 1);
                SdkMgr.getInst().setPropStr("UNISDK_LBS_PERMISSION_TIPS", strOptString4);
            }
            this.mUnlimitedTime = jSONObject.optBoolean("unlimitTime");
            this.mDistance = jSONObject.optInt("distance");
            this.mPeriod = jSONObject.optInt("period");
            this.mLimit = jSONObject.optInt("limit");
            this.mCursor = jSONObject.optInt("cursor");
            this.mServerId = jSONObject.optString("serverid", "0");
            this.mCachedServerName = jSONObject.optString("serverName");
            this.mCachedGameId = jSONObject.optString("gameid", null);
            mCachedRequestMethod = jSONObject.optString("requestMethod");
            readOtherParams(jSONObject);
            if (this.mCursor != 0 && this.mPreNearbyLocation != null && METHOD_GET_NEARBY.equalsIgnoreCase(this.mCachedMethodId)) {
                UniSdkUtils.i(TAG, "directly GET_NEARBY");
                Message messageObtain = Message.obtain();
                messageObtain.what = 4;
                messageObtain.obj = strOptString;
                this.mWorkerHandler.sendMessage(messageObtain);
                return;
            }
            this.mCachedMethodId = strOptString;
            startLocReqUpdate(true, CODE_REQ_PERMISSION_AT_EXTEND, 200L);
            if (this.mHasPermission) {
                doAfterLocationShouldReturn();
            }
        } catch (JSONException e) {
            UniSdkUtils.w(TAG, "" + e.getMessage());
        }
    }

    private int getTimeDelay() {
        return this.mUnlimitedTime ? Integer.MAX_VALUE : 2500;
    }

    private String[] getRuntimePermissions() throws PackageManager.NameNotFoundException {
        if (this.runtimePermissions == null) {
            try {
                PackageInfo packageInfo = this.myCtx.getPackageManager().getPackageInfo(this.myCtx.getPackageName(), 4096);
                if (packageInfo.requestedPermissions == null) {
                    this.runtimePermissions = needPermissions;
                } else {
                    HashSet hashSet = new HashSet(Arrays.asList(packageInfo.requestedPermissions));
                    LinkedList linkedList = new LinkedList();
                    for (String str : needPermissions) {
                        if (hashSet.contains(str)) {
                            linkedList.add(str);
                        }
                    }
                    if (linkedList.size() == 0) {
                        this.runtimePermissions = needPermissions;
                    } else {
                        this.runtimePermissions = (String[]) linkedList.toArray(new String[0]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.runtimePermissions == null) {
            this.runtimePermissions = needPermissions;
        }
        return this.runtimePermissions;
    }

    private boolean verifyPermissions(String[] strArr) {
        UniSdkUtils.i(TAG, "verifyPermissions: " + Arrays.toString(strArr));
        for (String str : strArr) {
            if (!checkPermission(str)) {
                return false;
            }
        }
        return true;
    }

    private boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return Settings.Secure.getInt(this.myCtx.getContentResolver(), "location_mode") != 0;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return !TextUtils.isEmpty(Settings.Secure.getString(this.myCtx.getContentResolver(), "location_providers_allowed"));
    }

    private boolean checkPermission(String str) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (getTargetSdkVersion() >= 23) {
            return (this.myCtx == null || LegacyPermissionUtil.checkSelfPermission(this.myCtx, str) != 0 || LegacyPermissionUtil.shouldShowRequestPermissionRationale((Activity) this.myCtx, str)) ? false : true;
        }
        int iCheckSelfPermission = -1;
        try {
            iCheckSelfPermission = LegacyPermissionUtil.checkSelfPermission(this.myCtx, str);
        } catch (Throwable unused) {
        }
        return iCheckSelfPermission == 0;
    }

    private int getTargetSdkVersion() {
        if (this.mTargetSdkVersion == 0) {
            try {
                this.mTargetSdkVersion = this.myCtx.getPackageManager().getPackageInfo(this.myCtx.getPackageName(), 0).applicationInfo.targetSdkVersion;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this.mTargetSdkVersion;
    }

    private List<String> findDeniedPermissions(String[] strArr) {
        UniSdkUtils.i(TAG, "findDeniedPermissions");
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (LegacyPermissionUtil.checkSelfPermission(this.myCtx, str) != 0 || LegacyPermissionUtil.shouldShowRequestPermissionRationale((Activity) this.myCtx, str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    private void checkPermissions(int i, String... strArr) {
        UniSdkUtils.i(TAG, "checkPermissions");
        List<String> listFindDeniedPermissions = findDeniedPermissions(strArr);
        UniSdkUtils.i(TAG, "checkPermissions: " + listFindDeniedPermissions);
        if (!this.mInBlackList || Build.VERSION.SDK_INT >= 23) {
            if (getTargetSdkVersion() < 23) {
                doAfterLocationShouldReturn();
                return;
            }
            if (listFindDeniedPermissions.size() > 0) {
                try {
                    LbsPermissionUtil.toReqPermissionWithPermissionKit(CHANNEL, (Activity) this.myCtx, i, (String[]) listFindDeniedPermissions.toArray(new String[0]));
                    return;
                } catch (JSONException e) {
                    UniSdkUtils.e(TAG, "checkPermissions -> e: " + e.getMessage());
                    return;
                }
            }
            return;
        }
        if (CODE_REQ_PERMISSION_AT_INIT != i) {
            doAfterLocationShouldReturn();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doItDirectly(Location location, String str) throws JSONException {
        UniSdkUtils.i(TAG, "doItDirectly: " + location + ", " + str);
        returnLocation(location, str);
        getNearby(location, str);
        getNearbyReqInfo(location, str);
        performOtherExtendMethod(location, str);
        LocationReporter.reportLocation("LocationManager.getLastKnownLocation", str, location == null ? 0.0d : location.getLongitude(), location != null ? location.getLatitude() : 0.0d, checkValid(location));
    }

    private void doAfterLocationReturnAsync() throws JSONException {
        UniSdkUtils.i(TAG, "doAfterLocationReturnAsync");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("location", new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        appendWifiList(jSONObject, true);
        appendOtherInfo(jSONObject);
        this.mMainHandler.sendMessage(Message.obtain(this.mMainHandler, 9, jSONObject));
    }

    private void startGps() {
        UniSdkUtils.i(TAG, "startGps-like");
        if (this.mGpsLocUpdateListener == null) {
            this.mGpsLocUpdateListener = new LocationListener() { // from class: com.netease.ntunisdk.SdkAndroidLocation.4
                @Override // android.location.LocationListener
                public void onLocationChanged(Location location) {
                    UniSdkUtils.i(SdkAndroidLocation.TAG, "other-onLocationChanged: " + location);
                }

                @Override // android.location.LocationListener
                public void onStatusChanged(String str, int i, Bundle bundle) {
                    UniSdkUtils.i(SdkAndroidLocation.TAG, "other-onStatusChanged: " + str + "/" + i + "/" + bundle);
                }

                @Override // android.location.LocationListener
                public void onProviderEnabled(String str) {
                    UniSdkUtils.i(SdkAndroidLocation.TAG, "other-onProviderEnabled: " + str);
                }

                @Override // android.location.LocationListener
                public void onProviderDisabled(String str) {
                    UniSdkUtils.i(SdkAndroidLocation.TAG, "other-onProviderDisabled: " + str);
                }
            };
        }
        List<String> providers = this.androidLocationManager.getProviders(true);
        if (providers == null || providers.isEmpty()) {
            return;
        }
        for (String str : providers) {
            if (!"network".equals(str)) {
                UniSdkUtils.i(TAG, "startGps: tag=" + str);
                try {
                    this.androidLocationManager.requestLocationUpdates(str, 1L, 1.0f, this.mGpsLocUpdateListener, HandlerThreadUtil.getWorkerThread().getLooper());
                } catch (Throwable unused) {
                }
            }
        }
    }

    private void stopGps() {
        LocationListener locationListener;
        UniSdkUtils.i(TAG, "stopGps-like");
        LocationManager locationManager = this.androidLocationManager;
        if (locationManager == null || (locationListener = this.mGpsLocUpdateListener) == null || this.mInBlackList) {
            return;
        }
        try {
            locationManager.removeUpdates(locationListener);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAfterLocationShouldReturn() {
        UniSdkUtils.i(TAG, "doAfterLocationShouldReturn");
        if (this.mInBlackList) {
            Message.obtain(this.mWorkerHandler, 5, 0, 0).sendToTarget();
            return;
        }
        startGps();
        Handler handler = this.mWorkerHandler;
        handler.sendMessageDelayed(Message.obtain(handler, 10, 0, 0), getTimeDelay());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAfterLocationReturn() throws JSONException {
        UniSdkUtils.i(TAG, "doAfterLocationReturn");
        Location androidLocation = getAndroidLocation();
        stopLocation();
        doAfterLocationReturnSync(androidLocation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAfterLocationReturnSync(Location location) throws JSONException {
        UniSdkUtils.i(TAG, "doAfterLocationReturnSync: " + location);
        if (TextUtils.isEmpty(this.mCachedMethodId)) {
            return;
        }
        UniSdkUtils.i(TAG, "this is method task");
        doItDirectly(location, this.mCachedMethodId);
        this.mCachedMethodId = "";
    }

    private void appendWifiList(JSONObject jSONObject, boolean z) throws JSONException {
        try {
            if (z) {
                jSONObject.putOpt("wifi", WifiListUtil.acquireSingleWifiList2(this.myCtx));
            } else {
                jSONObject.putOpt("wifi", WifiListUtil.getArray());
            }
        } catch (JSONException e) {
            UniSdkUtils.w(TAG, "" + e.getMessage());
        }
        UniSdkUtils.i(TAG, "after appendWifiList: " + jSONObject);
    }

    private Location getAndroidLocation() {
        Location location = null;
        try {
            if (!this.mInBlackList) {
                List<String> providers = this.androidLocationManager.getProviders(false);
                if (providers == null || providers.isEmpty()) {
                    return null;
                }
                Iterator<String> it = providers.iterator();
                while (it.hasNext()) {
                    Location lastKnownLocation = this.androidLocationManager.getLastKnownLocation(it.next());
                    UniSdkUtils.i(TAG, "getAndroidLocation: " + lastKnownLocation);
                    if (checkValid(lastKnownLocation) && (location == null || location.getTime() < lastKnownLocation.getTime())) {
                        location = lastKnownLocation;
                    }
                }
                return location;
            }
            return this.androidLocationManager.getLastKnownLocation("network");
        } catch (Exception e) {
            UniSdkUtils.e(TAG, "getAndroidLocation fail: " + e.getMessage());
            return null;
        }
    }

    private void appendOtherInfo(JSONObject jSONObject) throws JSONException {
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putOpt("username", SdkMgr.getInst().getPropStr(ConstProp.FULL_UID));
            jSONObject2.putOpt("udid", SdkMgr.getInst().getUdid());
            jSONObject2.putOpt("uid", SdkMgr.getInst().getPropStr(ConstProp.UID));
            jSONObject2.putOpt(Const.CONFIG_KEY.ROLEID, SdkMgr.getInst().getPropStr(ConstProp.USERINFO_UID));
            jSONObject2.putOpt("rolename", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_NAME));
            jSONObject2.putOpt("rolelevel", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_GRADE));
            jSONObject.putOpt("user", jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.putOpt("platform", "android");
            jSONObject.putOpt("system", jSONObject3);
            jSONObject.putOpt("model", Build.MANUFACTURER + "#" + Build.BRAND + "#" + Build.MODEL);
        } catch (JSONException e) {
            UniSdkUtils.w(TAG, "" + e.getMessage());
        }
        UniSdkUtils.i(TAG, "after appendOtherInfo: " + jSONObject);
    }

    private void appendReqUrl(JSONObject jSONObject, String str) throws JSONException {
        try {
            String str2 = getReqUrl() + "/neighbor" + getNearbyQueryParam();
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("location");
            String locTag = getLocTag(jSONObjectOptJSONObject);
            if (!TextUtils.isEmpty(locTag)) {
                JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject(locTag);
                str2 = str2 + "&lat=" + jSONObjectOptJSONObject2.optDouble("lat") + "&log=" + jSONObjectOptJSONObject2.optDouble(ClientLogConstant.LOG);
            }
            String strReplace = str2 + "&gameid=" + getMyInnerGameId(this.mCachedGameId);
            if (METHOD_GET_REQ.equalsIgnoreCase(str)) {
                strReplace = strReplace.replace("/neighbor", "/xxxxxx");
            }
            jSONObject.putOpt("reqUrl", strReplace);
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "" + e.getMessage());
        }
        UniSdkUtils.i(TAG, "after appendReqUrl: " + jSONObject);
    }

    private JSONObject acquireBaseLocationObject(Location location, boolean z) throws JSONException {
        boolean z2;
        UniSdkUtils.i(TAG, "acquireBaseLocationObject");
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        if (checkValid(location)) {
            z2 = true;
            try {
                String addressWithTimeLimit = AddressUtil.getAddressWithTimeLimit(this.myCtx, location.getLatitude(), location.getLongitude(), 1000L);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.putOpt("lat", Double.valueOf(location.getLatitude()));
                jSONObject3.putOpt(ClientLogConstant.LOG, Double.valueOf(location.getLongitude()));
                jSONObject3.putOpt("addr", addressWithTimeLimit);
                jSONObject3.putOpt("accuracy", Float.valueOf(location.getAccuracy()));
                jSONObject3.putOpt("provider", location.getProvider());
                jSONObject2.putOpt("android", jSONObject3);
                if (z) {
                    jSONObject2.putOpt("lat", Double.valueOf(location.getLatitude()));
                    jSONObject2.putOpt(ClientLogConstant.LOG, Double.valueOf(location.getLongitude()));
                    jSONObject2.putOpt("addr", addressWithTimeLimit);
                    jSONObject2.putOpt("accuracy", Float.valueOf(location.getAccuracy()));
                }
            } catch (Exception e) {
                UniSdkUtils.w(TAG, "" + e.getMessage());
            }
        } else {
            z2 = false;
        }
        try {
            if (z2) {
                jSONObject.putOpt("location", jSONObject2);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 0);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "suc");
            } else {
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 1000);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "\u672a\u83b7\u53d6\u5230\u6709\u6548\u5b9a\u4f4d\u4fe1\u606f");
            }
        } catch (Exception e2) {
            UniSdkUtils.w(TAG, "" + e2.getMessage());
        }
        UniSdkUtils.i(TAG, "base loc: " + jSONObject);
        return jSONObject;
    }

    private void returnLocation(Location location, String str) throws JSONException {
        if (METHOD_GET_LOCATION.equalsIgnoreCase(str)) {
            UniSdkUtils.i(TAG, "returnLocation: " + location);
            JSONObject jSONObjectAcquireBaseLocationObject = acquireBaseLocationObject(location, true);
            try {
                jSONObjectAcquireBaseLocationObject.putOpt("methodId", METHOD_GET_LOCATION);
            } catch (JSONException e) {
                UniSdkUtils.w(TAG, "" + e.getMessage());
            }
            UniSdkUtils.i(TAG, "extendFuncCall: " + jSONObjectAcquireBaseLocationObject);
            extendFuncCall(jSONObjectAcquireBaseLocationObject.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getNearbyQueryParam() {
        String[] strArr = {"distance", "" + this.mDistance, "period", "" + this.mPeriod, "limit", "" + this.mLimit, "cursor", "" + this.mCursor, "serverid", "" + this.mServerId};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArr.length; i += 2) {
            int i2 = i + 1;
            if (!"0".equals(strArr[i2])) {
                if (sb.length() > 0) {
                    sb.append(a.l);
                }
                sb.append(strArr[i]);
                sb.append("=");
                sb.append(strArr[i2]);
            }
        }
        String string = sb.toString();
        if (sb.length() <= 0) {
            return string;
        }
        return "?" + string;
    }

    private void getNearby(Location location, String str) throws JSONException {
        Location location2;
        if (METHOD_GET_NEARBY.equalsIgnoreCase(str)) {
            if (this.mCursor != 0 && (location2 = this.mPreNearbyLocation) != null) {
                location = location2;
            }
            UniSdkUtils.i(TAG, "getNearby: " + location);
            JSONObject jSONObjectAcquireBaseLocationObject = acquireBaseLocationObject(location, false);
            appendWifiList(jSONObjectAcquireBaseLocationObject, true);
            appendOtherInfo(jSONObjectAcquireBaseLocationObject);
            this.mPreNearbyLocation = location;
            this.mMainHandler.sendMessage(Message.obtain(this.mMainHandler, 6, jSONObjectAcquireBaseLocationObject));
        }
    }

    private void getNearbyReqInfo(Location location, String str) throws JSONException {
        Location location2;
        if (METHOD_GET_NEARBY_REQ.equalsIgnoreCase(str) || METHOD_GET_REQ.equalsIgnoreCase(str)) {
            if (this.mCursor != 0 && (location2 = this.mPreNearbyLocation) != null) {
                location = location2;
            }
            UniSdkUtils.i(TAG, "getReqInfo: " + location);
            JSONObject jSONObjectAcquireBaseLocationObject = acquireBaseLocationObject(location, false);
            if (jSONObjectAcquireBaseLocationObject.has("location")) {
                appendWifiList(jSONObjectAcquireBaseLocationObject, true);
                appendOtherInfo(jSONObjectAcquireBaseLocationObject);
                appendReqUrl(jSONObjectAcquireBaseLocationObject, str);
                this.mPreNearbyLocation = location;
            }
            try {
                jSONObjectAcquireBaseLocationObject.putOpt("methodId", str);
            } catch (JSONException e) {
                UniSdkUtils.w(TAG, "" + e.getMessage());
            }
            UniSdkUtils.i(TAG, "extendFuncCall: " + jSONObjectAcquireBaseLocationObject);
            extendFuncCall(jSONObjectAcquireBaseLocationObject.toString());
        }
    }

    private void performOtherExtendMethod(Location location, String str) throws JSONException {
        if (TextUtils.isEmpty(str) || sMethodSet.contains(str)) {
            return;
        }
        UniSdkUtils.i(TAG, "performOtherExtendMethod: " + location);
        JSONObject jSONObjectAcquireBaseLocationObject = acquireBaseLocationObject(location, false);
        appendWifiList(jSONObjectAcquireBaseLocationObject, true);
        appendOtherInfo(jSONObjectAcquireBaseLocationObject);
        appendReqUrl(jSONObjectAcquireBaseLocationObject, str);
        try {
            if ("lbsCommonReq".equals(str)) {
                str = this.mCachedServerName;
            }
            jSONObjectAcquireBaseLocationObject.putOpt("methodId", str);
            UniSdkUtils.d(TAG, "performOtherExtendMethod -> jsonObject: " + jSONObjectAcquireBaseLocationObject);
        } catch (JSONException e) {
            UniSdkUtils.w(TAG, "" + e.getMessage());
        }
        this.mMainHandler.sendMessage(Message.obtain(this.mMainHandler, 8, jSONObjectAcquireBaseLocationObject));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getReqUrl() {
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_LBS_URL, getPropStr(ConstProp.UNISDK_LBS_URL, URL_TEST));
        if (propStr.endsWith("/")) {
            propStr = propStr.substring(0, propStr.length() - 1);
        }
        return propStr + "/v1";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void doSepcialConfigVal(JSONObject jSONObject) {
        doConfigVal(jSONObject, ConstProp.UNISDK_LBS_URL);
    }

    private static boolean checkValid(Location location) {
        return (location == null || 0.0f == location.getAccuracy() || 2.1474836E9f < location.getAccuracy() || 0.0d == location.getLatitude() || 0.0d == location.getLongitude()) ? false : true;
    }

    private static boolean isBlackListDeivces() {
        String upperCase = (Build.DEVICE + "#" + Build.MANUFACTURER + "#" + Build.BRAND + "#" + Build.BOARD + "#" + Build.MODEL).toUpperCase(Locale.ROOT);
        for (String str : DEVICE_BLACK_LIST) {
            if (upperCase.contains(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMyInnerGameId(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID);
        return TextUtils.isEmpty(propStr) ? SdkMgr.getInst().getPropStr("JF_GAMEID") : propStr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getLocTag(JSONObject jSONObject) {
        if (jSONObject != null && jSONObject.has("android")) {
            return "android";
        }
        return null;
    }

    static class ConnectServTask extends AsyncTask<JSONObject, Integer, JSONObject> {
        private final String mMethodId;
        protected WeakReference<SdkAndroidLocation> mRef;
        private final String mToken;
        private String mUrl;

        ConnectServTask(SdkAndroidLocation sdkAndroidLocation, String str, String str2) {
            UniSdkUtils.i(SdkAndroidLocation.TAG, "ConnectServTask init");
            this.mRef = new WeakReference<>(sdkAndroidLocation);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str.contains("?") ? a.l : "?");
            sb.append("gameid=");
            sb.append(SdkAndroidLocation.getMyInnerGameId(sdkAndroidLocation.mCachedGameId));
            this.mUrl = sb.toString();
            this.mToken = SdkMgr.getInst().getPropStr(ConstProp.X_LBS_TOKEN);
            this.mMethodId = str2;
        }

        private void appendOtherParams(JSONObject jSONObject) throws JSONException {
            SdkAndroidLocation sdkAndroidLocation = this.mRef.get();
            if (sdkAndroidLocation != null) {
                for (Map.Entry entry : sdkAndroidLocation.mOtherParams.entrySet()) {
                    if (!jSONObject.has((String) entry.getKey())) {
                        jSONObject.putOpt((String) entry.getKey(), entry.getValue());
                    }
                }
                sdkAndroidLocation.mOtherParams.clear();
            }
        }

        private String getOtherQueryParams() {
            StringBuilder sb = new StringBuilder();
            SdkAndroidLocation sdkAndroidLocation = this.mRef.get();
            if (sdkAndroidLocation != null) {
                for (Map.Entry entry : sdkAndroidLocation.mOtherQueryParams.entrySet()) {
                    sb.append(a.l);
                    sb.append((String) entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                }
                sdkAndroidLocation.mOtherQueryParams.clear();
            }
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(JSONObject... jSONObjectArr) throws JSONException {
            UniSdkUtils.i(SdkAndroidLocation.TAG, "ConnectServTask doInBackground");
            JSONObject jSONObject = new JSONObject();
            if (jSONObjectArr == null || jSONObjectArr.length == 0) {
                UniSdkUtils.e(SdkAndroidLocation.TAG, "no valid json object passed.");
                return jSONObject;
            }
            if (TextUtils.isEmpty(this.mToken) && SdkAndroidLocation.METHOD_GET_NEARBY.equalsIgnoreCase(this.mMethodId)) {
                UniSdkUtils.e(SdkAndroidLocation.TAG, "no valid X_LBS_TOKEN.");
                return jSONObject;
            }
            JSONObject jSONObject2 = jSONObjectArr[0];
            String str = SdkAndroidLocation.METHOD_WEATHER.equals(this.mMethodId) ? "GET" : null;
            UniSdkUtils.d(SdkAndroidLocation.TAG, "mCachedRequestMethod: " + SdkAndroidLocation.mCachedRequestMethod);
            if (!TextUtils.isEmpty(SdkAndroidLocation.mCachedRequestMethod)) {
                str = SdkAndroidLocation.mCachedRequestMethod;
            }
            SdkAndroidLocation sdkAndroidLocation = this.mRef.get();
            if (jSONObject2.has("location") && sdkAndroidLocation != null && UniSdkUtils.isNetworkAvailable(sdkAndroidLocation.myCtx)) {
                try {
                    appendOtherParams(jSONObject2);
                    JSONObject jSONObjectOptJSONObject = jSONObject2.optJSONObject("location");
                    String locTag = SdkAndroidLocation.getLocTag(jSONObjectOptJSONObject);
                    if (!TextUtils.isEmpty(locTag)) {
                        JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject(locTag);
                        this.mUrl += "&lat=" + jSONObjectOptJSONObject2.optDouble("lat") + "&log=" + jSONObjectOptJSONObject2.optDouble(ClientLogConstant.LOG) + getOtherQueryParams();
                    }
                    JSONObject jSONObject3 = new JSONObject(HttpReqUtil.doHttpReq(this.mUrl, this.mToken, jSONObject2.toString(), str));
                    try {
                        calculateDistances(jSONObject2, jSONObject3);
                        return jSONObject3;
                    } catch (Exception e) {
                        e = e;
                        jSONObject = jSONObject3;
                        UniSdkUtils.w(SdkAndroidLocation.TAG, "" + e.getMessage());
                        return jSONObject;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            } else {
                try {
                    jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 1000);
                    jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "\u672a\u83b7\u53d6\u5230\u6709\u6548\u5b9a\u4f4d\u4fe1\u606f");
                    return jSONObject;
                } catch (Exception e3) {
                    UniSdkUtils.w(SdkAndroidLocation.TAG, "" + e3.getMessage());
                    return jSONObject;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(JSONObject jSONObject) throws JSONException {
            UniSdkUtils.i(SdkAndroidLocation.TAG, "post, config=" + jSONObject);
            if (TextUtils.isEmpty(this.mMethodId)) {
                return;
            }
            try {
                jSONObject.putOpt("methodId", this.mMethodId);
            } catch (JSONException e) {
                UniSdkUtils.w(SdkAndroidLocation.TAG, "" + e.getMessage());
            }
            SdkAndroidLocation sdkAndroidLocation = this.mRef.get();
            if (sdkAndroidLocation != null) {
                sdkAndroidLocation.extendFuncCall(jSONObject.toString());
            }
        }

        private void calculateDistances(JSONObject jSONObject, JSONObject jSONObject2) throws Exception {
            if (jSONObject2 == null || !jSONObject2.has("results")) {
                UniSdkUtils.w(SdkAndroidLocation.TAG, "no calculate dist");
                return;
            }
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("location");
            String locTag = SdkAndroidLocation.getLocTag(jSONObjectOptJSONObject);
            if (TextUtils.isEmpty(locTag)) {
                UniSdkUtils.w(SdkAndroidLocation.TAG, "not found src location tag");
                return;
            }
            JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject(locTag);
            Location location = new Location(locTag);
            location.setLatitude(jSONObjectOptJSONObject2.optDouble("lat"));
            location.setLongitude(jSONObjectOptJSONObject2.optDouble(ClientLogConstant.LOG));
            JSONArray jSONArrayOptJSONArray = jSONObject2.optJSONArray("results");
            for (int i = 0; i != jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject3 = jSONArrayOptJSONArray.optJSONObject(i);
                JSONObject jSONObjectOptJSONObject4 = jSONObjectOptJSONObject3.optJSONObject("location");
                Location location2 = new Location("server");
                location2.setLatitude(jSONObjectOptJSONObject4.optDouble("lat"));
                location2.setLongitude(jSONObjectOptJSONObject4.optDouble(ClientLogConstant.LOG));
                jSONObjectOptJSONObject3.putOpt("distance", Float.valueOf(location.distanceTo(location2)));
            }
        }
    }

    static class GuessLocationTask extends ConnectServTask {
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.netease.ntunisdk.SdkAndroidLocation.ConnectServTask, android.os.AsyncTask
        public void onPostExecute(JSONObject jSONObject) {
        }

        GuessLocationTask(SdkAndroidLocation sdkAndroidLocation, String str, String str2) {
            super(sdkAndroidLocation, str, str2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Removed duplicated region for block: B:13:0x005b  */
        @Override // com.netease.ntunisdk.SdkAndroidLocation.ConnectServTask, android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public org.json.JSONObject doInBackground(org.json.JSONObject... r6) throws org.json.JSONException {
            /*
                r5 = this;
                org.json.JSONObject r6 = super.doInBackground(r6)
                r0 = 0
                java.lang.String r1 = "result"
                org.json.JSONObject r6 = r6.optJSONObject(r1)     // Catch: java.lang.Exception -> L35
                java.lang.String r1 = "provider"
                java.lang.String r1 = r6.optString(r1)     // Catch: java.lang.Exception -> L35
                android.location.Location r2 = new android.location.Location     // Catch: java.lang.Exception -> L35
                r2.<init>(r1)     // Catch: java.lang.Exception -> L35
                java.lang.String r1 = "accuracy"
                double r3 = r6.optDouble(r1)     // Catch: java.lang.Exception -> L33
                float r1 = (float) r3     // Catch: java.lang.Exception -> L33
                r2.setAccuracy(r1)     // Catch: java.lang.Exception -> L33
                java.lang.String r1 = "lat"
                double r3 = r6.optDouble(r1)     // Catch: java.lang.Exception -> L33
                r2.setLatitude(r3)     // Catch: java.lang.Exception -> L33
                java.lang.String r1 = "log"
                double r3 = r6.optDouble(r1)     // Catch: java.lang.Exception -> L33
                r2.setLongitude(r3)     // Catch: java.lang.Exception -> L33
                goto L51
            L33:
                r6 = move-exception
                goto L37
            L35:
                r6 = move-exception
                r2 = r0
            L37:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r3 = ""
                r1.append(r3)
                java.lang.String r6 = r6.getMessage()
                r1.append(r6)
                java.lang.String r6 = r1.toString()
                java.lang.String r1 = "UniSDK AndroidLocation"
                com.netease.ntunisdk.base.UniSdkUtils.e(r1, r6)
            L51:
                java.lang.ref.WeakReference<com.netease.ntunisdk.SdkAndroidLocation> r6 = r5.mRef
                java.lang.Object r6 = r6.get()
                com.netease.ntunisdk.SdkAndroidLocation r6 = (com.netease.ntunisdk.SdkAndroidLocation) r6
                if (r6 == 0) goto L5e
                com.netease.ntunisdk.SdkAndroidLocation.access$1200(r6, r2)
            L5e:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.SdkAndroidLocation.GuessLocationTask.doInBackground(org.json.JSONObject[]):org.json.JSONObject");
        }
    }

    static class WorkerHandler extends Handler {
        private final WeakReference<SdkAndroidLocation> mRef;

        WorkerHandler(Looper looper, SdkAndroidLocation sdkAndroidLocation) {
            super(looper);
            this.mRef = new WeakReference<>(sdkAndroidLocation);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws JSONException {
            SdkAndroidLocation sdkAndroidLocation = this.mRef.get();
            if (sdkAndroidLocation == null) {
                UniSdkUtils.e(SdkAndroidLocation.TAG, "null inst");
                return;
            }
            int i = message.what;
            if (i != 0) {
                if (i != 10) {
                    if (i == 4) {
                        sdkAndroidLocation.doItDirectly(sdkAndroidLocation.mPreNearbyLocation, (String) message.obj);
                        return;
                    } else if (i != 5) {
                        return;
                    }
                }
                sdkAndroidLocation.doAfterLocationReturn();
                return;
            }
            sdkAndroidLocation.workerExtendFunc((String) message.obj);
        }
    }

    static class MainHandler extends Handler {
        private final WeakReference<SdkAndroidLocation> mRef;

        MainHandler(Looper looper, SdkAndroidLocation sdkAndroidLocation) {
            super(looper);
            this.mRef = new WeakReference<>(sdkAndroidLocation);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SdkAndroidLocation sdkAndroidLocation = this.mRef.get();
            if (sdkAndroidLocation != null) {
                switch (message.what) {
                    case 6:
                        new ConnectServTask(sdkAndroidLocation, sdkAndroidLocation.getReqUrl() + "/neighbor" + sdkAndroidLocation.getNearbyQueryParam(), SdkAndroidLocation.METHOD_GET_NEARBY).execute((JSONObject) message.obj);
                        break;
                    case 7:
                        new ConnectServTask(sdkAndroidLocation, sdkAndroidLocation.getReqUrl() + "/location", null).execute((JSONObject) message.obj);
                        break;
                    case 8:
                        JSONObject jSONObject = (JSONObject) message.obj;
                        String strOptString = jSONObject.optString("methodId");
                        new ConnectServTask(sdkAndroidLocation, sdkAndroidLocation.getReqUrl() + "/" + strOptString, strOptString).execute(jSONObject);
                        break;
                    case 9:
                        new GuessLocationTask(sdkAndroidLocation, sdkAndroidLocation.getReqUrl() + "/guess" + sdkAndroidLocation.getNearbyQueryParam(), "guess").execute(new JSONObject[]{(JSONObject) message.obj});
                        break;
                }
            }
            UniSdkUtils.e(SdkAndroidLocation.TAG, "null inst");
        }
    }

    private void returnProviderEnable(JSONObject jSONObject) throws JSONException {
        try {
            String strOptString = jSONObject.optString("provider");
            if (!TextUtils.isEmpty(strOptString)) {
                jSONObject.putOpt("result", Boolean.valueOf(ProviderUtil.isProviderEnable(this.myCtx, strOptString)));
            } else {
                jSONObject.putOpt("gps", Boolean.valueOf(ProviderUtil.isGpsEnable(this.myCtx)));
                jSONObject.putOpt("network", Boolean.valueOf(ProviderUtil.isNetworkEnable(this.myCtx)));
            }
            jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 0);
            jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "suc");
            extendFuncCall(jSONObject.toString());
        } catch (Exception unused) {
        }
    }

    private void returnPermissionEnable(JSONObject jSONObject) throws JSONException {
        try {
            boolean zHasOneLbsPermission = hasOneLbsPermission();
            int i = zHasOneLbsPermission ? 1 : 0;
            jSONObject.putOpt("result", Boolean.valueOf(zHasOneLbsPermission));
            jSONObject.putOpt("isLBSAuthorization", Integer.valueOf(i));
            jSONObject.putOpt("isLBSAuthorizationResult", Boolean.valueOf(zHasOneLbsPermission));
            jSONObject.putOpt("isSystemLocationEnabled", Boolean.valueOf(checkSystemLocationEnable(this.myCtx)));
            jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 0);
            jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "suc");
            extendFuncCall(jSONObject.toString());
        } catch (Exception unused) {
        }
    }

    private boolean hasOneLbsPermission() throws PackageManager.NameNotFoundException {
        for (String str : getRuntimePermissions()) {
            if (LegacyPermissionUtil.checkSelfPermission(this.myCtx, str) == 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkSystemLocationEnable(Context context) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            if (!locationManager.isProviderEnabled("gps")) {
                if (!locationManager.isProviderEnabled("network")) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            if (UniSdkUtils.isDebug) {
                e.printStackTrace();
            }
            UniSdkUtils.e(TAG, "checkSystemLocationEnable -> e: " + e);
            return false;
        }
    }

    private void gotoLocationSetting(JSONObject jSONObject) throws JSONException {
        try {
            Intent intent = new Intent();
            intent.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
            UniSdkUtils.d(TAG, "gotoLocationSetting -> canGotoLocationSetting: " + this.canGotoLocationSetting);
            if (this.canGotoLocationSetting == null) {
                List<ResolveInfo> listQueryIntentActivities = this.myCtx.getPackageManager().queryIntentActivities(intent, 65536);
                if (listQueryIntentActivities != null && listQueryIntentActivities.size() > 0) {
                    this.canGotoLocationSetting = true;
                } else {
                    this.canGotoLocationSetting = false;
                }
            }
            if (this.canGotoLocationSetting != null && this.canGotoLocationSetting.booleanValue()) {
                HookManager.startActivity((Activity) this.myCtx, intent);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 0);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "suc");
                extendFuncCall(jSONObject.toString());
                return;
            }
            jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 999);
            jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, "no location setting");
            extendFuncCall(jSONObject.toString());
        } catch (Exception e) {
            if (UniSdkUtils.isDebug) {
                e.printStackTrace();
            }
            UniSdkUtils.e(TAG, "gotoLocationSetting -> e: " + e);
            try {
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_CODE, 999);
                jSONObject.putOpt(RespUtil.UniSdkField.RESP_MSG, e.getMessage());
                extendFuncCall(jSONObject.toString());
            } catch (Exception unused) {
                if (UniSdkUtils.isDebug) {
                    e.printStackTrace();
                }
            }
        }
    }
}