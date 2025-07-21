package com.netease.ntunisdk.modules.applicationlifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ApplicationLifecycleModule extends BaseModules {
    public static final String ACTION_APP_FOREGROUND = "app_foreground";
    public static final String ACTION_IS_REGISTER = "is_register";
    public static final String ACTION_LIFE_MODEL = "life_model";
    public static final String ACTION_REGISTER_SYSTEM = "register_system";
    public static final String ACTION_START_LISTEN = "start_listen";
    public static final int ACTIVITY_LIFE_CREATE = 1;
    public static final int ACTIVITY_LIFE_DESTROY = 4;
    public static final int ACTIVITY_LIFE_START = 2;
    public static final int ACTIVITY_LIFE_STOP = 3;
    public static final String MODULE_NAME = "applicationLifecycle";
    private static final String TAG = "UNISDK ApplicationLifecycleModule";
    public static final String TAG_FOREGROUND_BOOL = "foreground";
    public static final String TAG_LIFE_ACT_STR = "life_act";
    public static final String TAG_LIFE_MODEL_INT = "life_model_int";
    private static final String VERSION = "1.0.2";
    private final List<String> callbackList;
    private boolean isRegister;
    private String mActivityName;
    private boolean mIsFront;
    private int mLifeModel;

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getVersion() {
        return "1.0.2";
    }

    public ApplicationLifecycleModule(Context context, IModulesCall iModulesCall) {
        super(context, iModulesCall);
        this.isRegister = false;
        this.mIsFront = false;
        this.mLifeModel = 0;
        this.mActivityName = "";
        this.callbackList = new ArrayList();
        if (context instanceof Application) {
            registerActivityLifecycleCallbacks((Application) context);
        } else if (context instanceof Activity) {
            registerActivityLifecycleCallbacks(((Activity) context).getApplication());
        } else {
            LogModule.e(TAG, "startListenAppLifecycle params error");
        }
    }

    private synchronized void registerActivityLifecycleCallbacks(Application application) {
        LogModule.d(TAG, "isRegister:" + this.isRegister);
        if (!this.isRegister) {
            LogModule.d(TAG, "registerActivityLifecycleCallbacks");
            application.registerActivityLifecycleCallbacks(new LifecycleCallback(this));
            this.isRegister = true;
        }
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String extendFunc(String str, String str2, String str3, Object... objArr) {
        String strOptString;
        try {
            strOptString = new JSONObject(str3).optString("methodId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ACTION_START_LISTEN.equals(strOptString)) {
            synchronized (this.callbackList) {
                if (!this.callbackList.contains(str2)) {
                    this.callbackList.add(str2);
                    callbackRes(this.mIsFront);
                    callActivityLife(this.mLifeModel, this.mActivityName);
                    return String.valueOf(true);
                }
                return String.valueOf(false);
            }
        }
        if (ACTION_IS_REGISTER.equals(strOptString)) {
            return String.valueOf(this.isRegister);
        }
        if (ACTION_REGISTER_SYSTEM.equals(strOptString) && objArr != null && objArr.length > 0) {
            Object obj = objArr[0];
            if (obj instanceof Application) {
                registerActivityLifecycleCallbacks((Application) obj);
            } else if (obj instanceof Activity) {
                registerActivityLifecycleCallbacks(((Activity) obj).getApplication());
            } else {
                LogModule.e(TAG, "startListenAppLifecycle params error");
                return String.valueOf(false);
            }
        }
        return String.valueOf(this.isRegister);
    }

    public void callbackRes(boolean z) {
        this.mIsFront = z;
        LogModule.d(TAG, "callbackRes isFront:" + z);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", ACTION_APP_FOREGROUND);
            jSONObject.put(TAG_FOREGROUND_BOOL, z);
            Iterator<String> it = this.callbackList.iterator();
            while (it.hasNext()) {
                callback("native", it.next(), jSONObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callActivityLife(int i, String str) {
        LogModule.d(TAG, "callbackRes life:" + i);
        this.mActivityName = str;
        this.mLifeModel = i;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", ACTION_LIFE_MODEL);
            jSONObject.put(TAG_LIFE_MODEL_INT, i);
            jSONObject.put(TAG_LIFE_ACT_STR, str);
            Iterator<String> it = this.callbackList.iterator();
            while (it.hasNext()) {
                callback("native", it.next(), jSONObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}