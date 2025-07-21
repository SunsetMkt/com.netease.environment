package com.netease.ntunisdk;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.modules.permission.PermissionContext;
import com.netease.ntunisdk.modules.permission.common.PermissionCode;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import com.netease.ntunisdk.modules.permission.core.PermissionHandler;
import com.netease.ntunisdk.modules.permission.core.PermissionListener;
import com.netease.ntunisdk.modules.permission.core.PermissionTask;
import com.netease.ntunisdk.modules.permission.ui.PermissionFragment;
import com.netease.ntunisdk.modules.permission.ui.PermissionToast;
import com.netease.ntunisdk.modules.permission.utils.PermissionUtils;
import com.netease.ntunisdk.permissionkit.LanguageProxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkPermissionKit extends SdkBase implements PermissionContext {
    private static final String CHANNEL = "netease_permission_kit";
    private static final String FEATURE_HAS_INIT_UISDK = "FEATURE_HAS_INIT_UISDK";
    private static final String TAG = "UniSDK PermissionKit";
    private static final String TAG_PERMISSION = "TAG_PERMISSION";
    private static final String UNISDK_PERMISSION_SETTING_TIP = "UNISDK_PERMISSION_SETTING_TIP";
    private static final String UNISDK_PERMISSION_TOAST_TIP = "UNISDK_PERMISSION_TOAST_TIP";
    private static final String VER = "1.3.0";
    private Handler handler;
    private List<PermissionTask> permissionHandlerList;
    private Map<String, PermissionHandler> permissionHandlerMap;

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return CHANNEL;
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

    public SdkPermissionKit(Context context) {
        super(context);
        setFeature(ConstProp.INNER_MODE_SECOND_CHANNEL, true);
        setFeature(ConstProp.INNER_MODE_NO_PAY, true);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        UniSdkUtils.d(TAG, "netease_permission_kit version: 1.3.0");
        UniSdkUtils.d(TAG, "init netease_permission_kit");
        SdkMgr.getInst().setPropInt(FEATURE_HAS_INIT_UISDK, 1);
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.LANGUAGE_CODE, null);
        UniSdkUtils.d(TAG, "init netease_permission_kit language: " + propStr);
        LanguageProxy.setLanguageCode(propStr);
        this.permissionHandlerList = new ArrayList();
        this.permissionHandlerMap = new ConcurrentHashMap();
        this.handler = new Handler(Looper.getMainLooper()) { // from class: com.netease.ntunisdk.SdkPermissionKit.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message != null && message.what == 1336 && (message.obj instanceof String)) {
                    UniSdkUtils.d(SdkPermissionKit.TAG, "get handleMessage");
                    SdkPermissionKit.this.permissionHandlerMap.remove((String) message.obj);
                    Iterator it = SdkPermissionKit.this.permissionHandlerList.iterator();
                    while (it.hasNext()) {
                        PermissionTask permissionTask = (PermissionTask) it.next();
                        String key = permissionTask.getKey();
                        PermissionHandler value = permissionTask.getValue();
                        if (!SdkPermissionKit.this.permissionHandlerMap.containsKey(key)) {
                            SdkPermissionKit.this.permissionHandlerMap.put(key, value);
                            it.remove();
                        }
                    }
                    Iterator it2 = SdkPermissionKit.this.permissionHandlerMap.entrySet().iterator();
                    if (it2.hasNext()) {
                        Map.Entry entry = (Map.Entry) it2.next();
                        PermissionHandler permissionHandler = (PermissionHandler) entry.getValue();
                        UniSdkUtils.d(SdkPermissionKit.TAG, "handleMessage: " + ((String) entry.getKey()));
                        SdkPermissionKit.this.showFragment(permissionHandler);
                    }
                }
            }
        };
        onFinishInitListener.finishInit(2);
        UniSdkUtils.d(TAG, "PermissionKit.onInitFinish: OK");
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) {
        handleExtendFunc(str, null);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str, Object... objArr) {
        if (objArr == null || objArr.length != 1) {
            return;
        }
        try {
            Object obj = objArr[0];
            if (obj instanceof Activity) {
                handleExtendFunc(str, (Activity) obj);
            }
        } catch (Exception unused) {
        }
    }

    private void requestPermission(final String str, final JSONObject jSONObject, Activity activity) throws JSONException {
        final PermissionHandler permissionHandler;
        UniSdkUtils.d(TAG, "requestPermission-onStart");
        if (!TextUtils.isEmpty(PermissionUtils.checkParam(jSONObject))) {
            extendCallHandOut(str, PermissionCode.PARAM_ERROR, jSONObject, null);
            UniSdkUtils.d(TAG, "requestPermission check permissionString failed");
            return;
        }
        final String strOptString = jSONObject.optString("permissionName");
        PermissionListener permissionListener = new PermissionListener() { // from class: com.netease.ntunisdk.SdkPermissionKit.2
            @Override // com.netease.ntunisdk.modules.permission.core.PermissionListener
            public void onPermissionResult(String str2, int[] iArr, PermissionHandler permissionHandler2, PermissionFragment permissionFragment) throws JSONException {
                UniSdkUtils.d(SdkPermissionKit.TAG, "permission onPermissionResult callback:" + jSONObject);
                int length = iArr.length;
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    if (iArr[i] < -1) {
                        z = true;
                        break;
                    }
                    i++;
                }
                if (!z) {
                    SdkPermissionKit.this.extendCallHandOut(str, PermissionCode.SUCCESS, jSONObject, iArr);
                } else {
                    permissionFragment.goToSetting(permissionHandler2);
                }
            }
        };
        if (activity == null) {
            permissionHandler = new PermissionHandler(this, permissionListener, jSONObject);
        } else {
            permissionHandler = new PermissionHandler(new OtherPermissionContext(activity), permissionListener, jSONObject);
        }
        permissionHandler.setShowDialog(SdkMgr.getInst().getPropInt(SdkQRCode.ENABLE_UNISDK_PERMISSION_TIPS, 0) == 1);
        permissionHandler.setTipSetting(SdkMgr.getInst().getPropStr(UNISDK_PERMISSION_SETTING_TIP, ""));
        if (permissionHandler.getPermissionRequestProxy() != null) {
            if (this.permissionHandlerMap.size() > 0) {
                if (!this.permissionHandlerMap.containsKey(strOptString)) {
                    this.permissionHandlerMap.put(strOptString, permissionHandler);
                    UniSdkUtils.d(TAG, "put Permission Success");
                } else {
                    this.permissionHandlerList.add(new PermissionTask(strOptString, permissionHandler));
                    UniSdkUtils.d(TAG, "add Permission Success");
                }
            } else {
                Handler handler = this.handler;
                if (handler != null) {
                    handler.post(new Runnable() { // from class: com.netease.ntunisdk.SdkPermissionKit.3
                        @Override // java.lang.Runnable
                        public void run() {
                            SdkPermissionKit.this.permissionHandlerMap.put(strOptString, permissionHandler);
                            UniSdkUtils.d(SdkPermissionKit.TAG, "runOnUiThread: " + strOptString);
                            SdkPermissionKit.this.showFragment(permissionHandler);
                        }
                    });
                }
            }
            UniSdkUtils.d(TAG, "Requesting Permission Success");
            return;
        }
        UniSdkUtils.d(TAG, "Requesting Permission Failed");
    }

    private void handleExtendFunc(String str, Activity activity) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("channel");
            String strOptString2 = jSONObject.optString("requestChannel");
            if (!TextUtils.isEmpty(jSONObject.optString("result"))) {
                UniSdkUtils.d(TAG, "extendFunc ignore json");
                return;
            }
            String strOptString3 = jSONObject.optString("methodId");
            UniSdkUtils.d(TAG, "extendFunc:" + strOptString3);
            if ("setLanguage".equalsIgnoreCase(strOptString3)) {
                LanguageProxy.setLanguageCode(jSONObject.optString("language", SdkMgr.getInst().getPropStr(ConstProp.LANGUAGE_CODE, null)));
            }
            if (CHANNEL.equals(strOptString)) {
                try {
                    if ("requestPermission".equalsIgnoreCase(strOptString3)) {
                        requestPermission(strOptString2, jSONObject, activity);
                    } else {
                        extendCallHandOut(strOptString2, PermissionCode.NO_EXIST_METHOD, jSONObject, null);
                        UniSdkUtils.d(TAG, "extendFunc No exist method");
                    }
                } catch (JSONException e) {
                    extendCallHandOut(strOptString2, PermissionCode.JSON_ERROR, jSONObject, null);
                    UniSdkUtils.d(TAG, "extendFunc JSONException:" + e.getMessage());
                } catch (Exception e2) {
                    extendCallHandOut(strOptString2, PermissionCode.UNKNOWN_ERROR, jSONObject, null);
                    UniSdkUtils.d(TAG, "extendFunc UnknownException:" + e2.getMessage());
                }
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extendCallHandOut(String str, PermissionCode permissionCode, JSONObject jSONObject, int[] iArr) throws JSONException {
        boolean zOptBoolean = jSONObject.optBoolean("isCallGame", true);
        extendCall(str, permissionCode, jSONObject, iArr);
        if (zOptBoolean) {
            extendCallCompatible(str, jSONObject, iArr);
        }
    }

    private void extendCallCompatible(String str, JSONObject jSONObject, int[] iArr) throws JSONException {
        if (iArr == null) {
            return;
        }
        try {
            String[] strArrSplit = jSONObject.optString("permissionName").split(",");
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            boolean z = false;
            for (int i = 0; i < strArrSplit.length; i++) {
                boolean z2 = PermissionUtils.getBoolean(this.myCtx, PermissionConstant.PERMISSION_NEVER_AGAIN + strArrSplit[i], false);
                if (iArr[i] != 0) {
                    arrayList.add(strArrSplit[i]);
                    arrayList2.add(Boolean.valueOf(z2));
                    z = true;
                }
            }
            String str2 = z ? "uniSDKPermissionDenied" : null;
            JSONObject jSONObject2 = new JSONObject();
            JSONArray jSONArray = new JSONArray((Collection) arrayList2);
            JSONArray jSONArray2 = new JSONArray((Collection) arrayList);
            jSONObject2.put("methodId", str2);
            jSONObject2.put("channel", str);
            jSONObject2.put("permissions", jSONArray2);
            jSONObject2.put("dontAskAgain", jSONArray);
            if (z) {
                extendFuncCall(jSONObject2.toString());
                UniSdkUtils.d(TAG, " extendFuncCall Game");
            }
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "jsonException: " + e);
        }
    }

    private void extendCall(String str, PermissionCode permissionCode, JSONObject jSONObject, int[] iArr) throws JSONException {
        JSONObject jSONObject2;
        String strOptString;
        try {
            String strOptString2 = null;
            if (permissionCode == PermissionCode.SUCCESS) {
                Message messageObtain = Message.obtain();
                messageObtain.obj = jSONObject != null ? jSONObject.optString("permissionName") : null;
                messageObtain.what = PermissionConstant.REQUEST_CODE;
                this.handler.sendMessage(messageObtain);
            }
            if (jSONObject != null) {
                jSONObject2 = new JSONObject(jSONObject.toString());
                strOptString2 = jSONObject.optString("permissionName");
                strOptString = jSONObject.optString("refuseTip");
            } else {
                jSONObject2 = new JSONObject();
                strOptString = null;
            }
            jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, permissionCode.getCode());
            jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, permissionCode.getMsg());
            if (!TextUtils.isEmpty(strOptString2) && iArr != null) {
                strOptString2.getClass();
                String[] strArrSplit = strOptString2.split(",");
                for (int i = 0; i < strArrSplit.length; i++) {
                    UniSdkUtils.d(TAG, "permissionName " + strArrSplit[i]);
                    if ((this.myCtx instanceof Activity) && Build.VERSION.SDK_INT >= 23 && this.myCtx.checkSelfPermission(strOptString2) == 0) {
                        PermissionUtils.putBoolean(this.myCtx, PermissionConstant.PERMISSION_NEVER_AGAIN + strOptString2, false);
                    } else {
                        if (PermissionUtils.getBoolean(this.myCtx, PermissionConstant.PERMISSION_NEVER_AGAIN + strArrSplit[i], false)) {
                            iArr[i] = -2;
                        }
                    }
                }
                if (permissionCode == PermissionCode.SUCCESS) {
                    jSONObject2.put("result", PermissionUtils.joinString(iArr));
                }
            } else {
                UniSdkUtils.d(TAG, "permissionName is null or grantResults is null");
            }
            if (iArr != null) {
                showToast(iArr, strOptString);
            }
            UniSdkUtils.d(TAG, "extendCall:" + jSONObject2);
            if (TextUtils.isEmpty(str)) {
                extendFuncCall(jSONObject2.toString());
                UniSdkUtils.d(TAG, " extendFuncCall Game: " + jSONObject2);
                return;
            }
            jSONObject2.put("channel", str);
            SdkMgr.getInst().ntExtendFunc(jSONObject2.toString());
            UniSdkUtils.d(TAG, " extendFuncCall SDK: " + jSONObject2);
        } catch (JSONException e) {
            UniSdkUtils.d(TAG, "jsonException: " + e);
        }
    }

    private void showToast(int[] iArr, String str) {
        for (int i : iArr) {
            if (i < 0) {
                String propStr = SdkMgr.getInst().getPropStr(UNISDK_PERMISSION_TOAST_TIP);
                if (TextUtils.isEmpty(str)) {
                    if (TextUtils.isEmpty(propStr)) {
                        PermissionToast.show(this.myCtx, LanguageProxy.getString(this.myCtx, com.netease.mc.mi.R.string.netease_permissionkit_sdk__refuse_tip), 0);
                        return;
                    } else {
                        PermissionToast.show(this.myCtx, propStr, 0);
                        return;
                    }
                }
                PermissionToast.show(this.myCtx, str, 0);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFragment(PermissionHandler permissionHandler) {
        try {
            PermissionFragment permissionFragment = new PermissionFragment();
            Activity activity = permissionHandler.getPermissionRequestProxy().getActivity();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().add(permissionFragment, TAG_PERMISSION).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            permissionFragment.setContext(activity);
            permissionFragment.setPermissionHandlerMap(this.permissionHandlerMap);
            permissionFragment.classifyPermission(permissionHandler);
        } catch (Exception e) {
            UniSdkUtils.d(TAG, " showFragment: " + e.getMessage());
        }
    }

    @Override // com.netease.ntunisdk.modules.permission.PermissionContext
    public Context getContext() {
        return this.myCtx;
    }

    public static class OtherPermissionContext implements PermissionContext {
        Context context;

        public OtherPermissionContext(Context context) {
            this.context = context;
        }

        @Override // com.netease.ntunisdk.modules.permission.PermissionContext
        public Context getContext() {
            return this.context;
        }
    }
}