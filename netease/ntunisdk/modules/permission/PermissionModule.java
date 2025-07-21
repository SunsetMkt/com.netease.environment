package com.netease.ntunisdk.modules.permission;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.permission.common.PermissionCode;
import com.netease.ntunisdk.modules.permission.common.PermissionConstant;
import com.netease.ntunisdk.modules.permission.core.PermissionHandler;
import com.netease.ntunisdk.modules.permission.core.PermissionListener;
import com.netease.ntunisdk.modules.permission.core.PermissionTask;
import com.netease.ntunisdk.modules.permission.ui.PermissionFragment;
import com.netease.ntunisdk.modules.permission.ui.PermissionToast;
import com.netease.ntunisdk.modules.permission.utils.PermissionTextUtils;
import com.netease.ntunisdk.modules.permission.utils.PermissionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PermissionModule extends BaseModules implements PermissionContext {
    private static final String TAG = "PermissionModule";
    private static final String TAG_PERMISSION = "TAG_PERMISSION";
    private final Handler handler;
    private final List<PermissionTask> permissionHandlerList;
    private final Map<String, PermissionHandler> permissionHandlerMap;

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getModuleName() {
        return PermissionConstant.PERMISSION_KEY;
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getVersion() {
        return "1.3.4";
    }

    public PermissionModule(Context context, IModulesCall iModulesCall) {
        super(context, iModulesCall);
        LogModule.isDebug = true;
        this.permissionHandlerList = new ArrayList();
        this.permissionHandlerMap = new ConcurrentHashMap();
        this.handler = new Handler(Looper.getMainLooper()) { // from class: com.netease.ntunisdk.modules.permission.PermissionModule.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message != null && message.what == 1336 && (message.obj instanceof String)) {
                    LogModule.d(PermissionModule.TAG, "get handleMessage");
                    PermissionModule.this.permissionHandlerMap.remove((String) message.obj);
                    Iterator it = PermissionModule.this.permissionHandlerList.iterator();
                    while (it.hasNext()) {
                        PermissionTask permissionTask = (PermissionTask) it.next();
                        String key = permissionTask.getKey();
                        PermissionHandler value = permissionTask.getValue();
                        if (!PermissionModule.this.permissionHandlerMap.containsKey(key)) {
                            PermissionModule.this.permissionHandlerMap.put(key, value);
                            it.remove();
                        }
                    }
                    Iterator it2 = PermissionModule.this.permissionHandlerMap.entrySet().iterator();
                    if (it2.hasNext()) {
                        Map.Entry entry = (Map.Entry) it2.next();
                        PermissionHandler permissionHandler = (PermissionHandler) entry.getValue();
                        LogModule.d(PermissionModule.TAG, "handleMessage: " + ((String) entry.getKey()));
                        PermissionModule.this.showFragment(permissionHandler);
                    }
                }
            }
        };
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String extendFunc(String str, String str2, String str3, Object... objArr) {
        JSONObject jSONObject;
        try {
            try {
                jSONObject = new JSONObject(str3);
                try {
                    String strOptString = jSONObject.optString("methodId");
                    if ("requestPermission".equalsIgnoreCase(strOptString)) {
                        return requestPermission(str, str2, jSONObject, getOtherActivity(objArr)) == PermissionCode.SUCCESS ? "" : synchronousCall(PermissionCode.FAILURE, jSONObject);
                    }
                    if ("hasPermission".equalsIgnoreCase(strOptString)) {
                        return hasPermission(jSONObject, getOtherActivity(objArr));
                    }
                    if ("setPermissionLanguage".equalsIgnoreCase(strOptString)) {
                        String strOptString2 = jSONObject.optString("language");
                        String strOptString3 = jSONObject.optString("region");
                        if (!TextUtils.isEmpty(strOptString2) && !TextUtils.isEmpty(strOptString3)) {
                            PermissionTextUtils.setLanguageAndRegion(strOptString2, strOptString3);
                            return synchronousCall(PermissionCode.SUCCESS, jSONObject);
                        }
                        return synchronousCall(PermissionCode.PARAM_ERROR, jSONObject);
                    }
                    return synchronousCall(PermissionCode.NO_EXIST_METHOD, jSONObject);
                } catch (Exception e) {
                    e = e;
                    LogModule.d(TAG, "extendFunc Exception:" + e);
                    return synchronousCall(PermissionCode.UNKNOWN_ERROR, jSONObject);
                }
            } catch (Exception e2) {
                e = e2;
                jSONObject = null;
            }
        } catch (JSONException e3) {
            LogModule.d(TAG, "extendFunc JSONException:" + e3);
            return synchronousCall(PermissionCode.JSON_ERROR, null);
        }
    }

    private static Activity getOtherActivity(Object[] objArr) {
        if (objArr == null || objArr.length != 1) {
            return null;
        }
        try {
            Object obj = objArr[0];
            if (obj instanceof Activity) {
                return (Activity) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    private String hasPermission(JSONObject jSONObject, Activity activity) throws JSONException {
        LogModule.d(TAG, "hasPermission-onStart");
        String strOptString = jSONObject.optString("permissionName");
        if (TextUtils.isEmpty(strOptString)) {
            return synchronousCall(PermissionCode.PARAM_ERROR, jSONObject);
        }
        strOptString.getClass();
        String[] strArrSplit = strOptString.split(",");
        JSONArray jSONArray = new JSONArray();
        for (String str : strArrSplit) {
            jSONArray.put(PermissionUtils.hasPermission(activity == null ? this.context : activity, str));
        }
        jSONObject.put("result", jSONArray);
        LogModule.d(TAG, "hasPermission-onEnd");
        return synchronousCall(PermissionCode.SUCCESS, jSONObject);
    }

    private PermissionCode requestPermission(final String str, final String str2, final JSONObject jSONObject, Activity activity) {
        final PermissionHandler permissionHandler;
        LogModule.d(TAG, "requestPermission-onStart");
        if (!TextUtils.isEmpty(PermissionUtils.checkParam(jSONObject))) {
            return PermissionCode.PARAM_ERROR;
        }
        final String strOptString = jSONObject.optString("permissionName");
        PermissionListener permissionListener = new PermissionListener() { // from class: com.netease.ntunisdk.modules.permission.PermissionModule.2
            @Override // com.netease.ntunisdk.modules.permission.core.PermissionListener
            public void onPermissionResult(String str3, int[] iArr, PermissionHandler permissionHandler2, PermissionFragment permissionFragment) throws JSONException {
                LogModule.d(PermissionModule.TAG, "permission onPermissionResult callback:" + jSONObject);
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
                if (z) {
                    permissionFragment.goToSetting(permissionHandler2);
                } else {
                    PermissionModule.this.extendCall(str, str2, PermissionCode.SUCCESS, jSONObject, iArr);
                }
            }
        };
        if (activity == null) {
            permissionHandler = new PermissionHandler(this, permissionListener, jSONObject);
        } else {
            permissionHandler = new PermissionHandler(new OtherPermissionContext(activity), permissionListener, jSONObject);
        }
        permissionHandler.setTipSetting("");
        if (permissionHandler.getPermissionRequestProxy() != null) {
            if (this.permissionHandlerMap.size() > 0) {
                if (!this.permissionHandlerMap.containsKey(strOptString)) {
                    this.permissionHandlerMap.put(strOptString, permissionHandler);
                    LogModule.d(TAG, "put Permission Success");
                } else {
                    this.permissionHandlerList.add(new PermissionTask(strOptString, permissionHandler));
                    LogModule.d(TAG, "add Permission Success");
                }
            } else {
                Handler handler = this.handler;
                if (handler != null) {
                    handler.post(new Runnable() { // from class: com.netease.ntunisdk.modules.permission.PermissionModule.3
                        @Override // java.lang.Runnable
                        public void run() {
                            PermissionModule.this.permissionHandlerMap.put(strOptString, permissionHandler);
                            LogModule.d(PermissionModule.TAG, "runOnUiThread: " + strOptString);
                            PermissionModule.this.showFragment(permissionHandler);
                        }
                    });
                }
            }
            LogModule.d(TAG, "Requesting Permission Success");
            return PermissionCode.SUCCESS;
        }
        LogModule.d(TAG, "Requesting Permission Failed");
        return PermissionCode.FAILURE;
    }

    public void extendCall(String str, String str2, PermissionCode permissionCode, JSONObject jSONObject, int[] iArr) throws JSONException {
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
                    LogModule.d(TAG, "permissionName " + strArrSplit[i]);
                    if ((this.context instanceof Activity) && Build.VERSION.SDK_INT >= 23 && this.context.checkSelfPermission(strOptString2) == 0) {
                        PermissionUtils.putBoolean(this.context, PermissionConstant.PERMISSION_NEVER_AGAIN + strOptString2, false);
                    } else {
                        if (PermissionUtils.getBoolean(this.context, PermissionConstant.PERMISSION_NEVER_AGAIN + strArrSplit[i], false)) {
                            iArr[i] = -2;
                        }
                    }
                }
                if (permissionCode == PermissionCode.SUCCESS) {
                    jSONObject2.put("result", PermissionUtils.joinString(iArr));
                }
            } else {
                LogModule.d(TAG, "permissionName is null or grantResults is null");
            }
            if (iArr != null) {
                showToast(iArr, strOptString);
            }
            callback(str, str2, jSONObject2.toString());
        } catch (JSONException e) {
            LogModule.d(TAG, "jsonException: " + e);
        }
    }

    private String synchronousCall(PermissionCode permissionCode, JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2;
        try {
            if (jSONObject != null) {
                jSONObject2 = new JSONObject(jSONObject.toString());
            } else {
                jSONObject2 = new JSONObject();
            }
            jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, permissionCode.getCode());
            jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, permissionCode.getMsg());
            return jSONObject2.toString();
        } catch (JSONException e) {
            LogModule.d(TAG, "jsonException: " + e);
            return "";
        }
    }

    @Override // com.netease.ntunisdk.modules.permission.PermissionContext
    public Context getContext() {
        return this.context;
    }

    private void showToast(int[] iArr, String str) {
        for (int i : iArr) {
            if (i < 0) {
                if (TextUtils.isEmpty(str)) {
                    PermissionToast.show(this.context, PermissionTextUtils.getString(this.context, R.string.netease_permissionkit_sdk__refuse_tip), 0);
                    return;
                } else {
                    PermissionToast.show(this.context, str, 0);
                    return;
                }
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
            LogModule.d(TAG, " showFragment: " + e.getMessage());
        }
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