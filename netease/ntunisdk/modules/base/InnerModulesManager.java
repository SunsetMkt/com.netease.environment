package com.netease.ntunisdk.modules.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.base.call.SDKModulesCall;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.tencent.open.SocialConstants;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class InnerModulesManager extends InnerLifecycleManager {
    private static InnerModulesManager ModulesManagerInst = new InnerModulesManager();
    private static final String SUBMODULES_PATH = "com.netease.ntunisdk.modules";
    private static final String TAG = "UniSDK InnerModuManager";
    private static boolean isSoLoadDone;
    private InnerModulesCallback callback;
    private boolean isInit = false;
    private IModulesCall modulesCall = new SDKModulesCall(this);

    private static native void jniCallback(String str, String str2, String str3);

    private InnerModulesManager() {
    }

    public static InnerModulesManager getInst() {
        return ModulesManagerInst;
    }

    public void init(Context context) throws PackageManager.NameNotFoundException {
        if (!this.isInit) {
            this.isInit = true;
            this.context = context;
            LogModule.checkIsDebug(context);
            LogModule.d(TAG, "init...");
            preLoad();
            return;
        }
        if (this.context != null && (this.context instanceof Application) && (context instanceof Activity)) {
            LogModule.d(TAG, "init by activity context");
            reInit(context);
        }
    }

    public void unInit() {
        this.isInit = false;
        if (this.modulesMap != null) {
            this.modulesMap.clear();
        }
        this.context = null;
    }

    public void reInit(Context context) {
        this.context = context;
        Iterator<String> it = this.modulesMap.keySet().iterator();
        while (it.hasNext()) {
            this.modulesMap.get(it.next()).setContext(context);
        }
    }

    private void preLoad() throws PackageManager.NameNotFoundException {
        try {
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return;
            }
            for (String str : applicationInfo.metaData.keySet()) {
                if (str.startsWith(Constant.METADATA_MODULE_PREFIX)) {
                    genModuleObj(applicationInfo.metaData.getString(str));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized BaseModules genModuleObj(String str) {
        BaseModules baseModules;
        LogModule.d(TAG, String.format("reflect %s newInstance", str));
        try {
            baseModules = (BaseModules) Class.forName("com.netease.ntunisdk.modules." + str.toLowerCase(Locale.ROOT) + "." + str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1) + "Module").getConstructor(Context.class, IModulesCall.class).newInstance(this.context, this.modulesCall);
            if (baseModules != null) {
                try {
                    registerModules(baseModules);
                } catch (Exception unused) {
                }
            }
        } catch (Exception unused2) {
            baseModules = null;
        }
        return baseModules;
    }

    public void registerModules(BaseModules baseModules) {
        this.modulesMap.put(baseModules.getModuleName(), baseModules);
    }

    public void setCallback(InnerModulesCallback innerModulesCallback) {
        this.callback = innerModulesCallback;
    }

    public String extendFunc(String str, String str2, String str3, Object... objArr) {
        return extendFuncAll("native", str, str2, str3, objArr);
    }

    public String extendFuncByInner(String str, String str2, String str3, Object... objArr) {
        return extendFuncAll(Constant.INNER_CALL, str, str2, str3, objArr);
    }

    public String extendFuncAll(String str, String str2, String str3, String str4, Object... objArr) throws JSONException {
        BaseModules baseModulesGenModuleObj;
        LogModule.d(TAG, String.format("extendFuncAll callType:%s, source:%s, moudule:%s, data:%s", str, str2, str3, str4));
        synchronized (this) {
            if (!this.modulesMap.containsKey(str3) || (baseModulesGenModuleObj = this.modulesMap.get(str3)) == null) {
                baseModulesGenModuleObj = genModuleObj(str3);
            }
        }
        if (baseModulesGenModuleObj != null) {
            String strExtendFunc = baseModulesGenModuleObj.extendFunc(str, str2, str4, objArr);
            return strExtendFunc != null ? strExtendFunc : "";
        }
        LogModule.e(TAG, str3 + " not exist");
        if (ConstProp.UNISDKBASE.equals(str2)) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", 20000);
            jSONObject.put("msg", "module is not exist!");
            jSONObject.put(SocialConstants.PARAM_SOURCE, str2);
            jSONObject.put("module", str3);
            jSONObject.put(SocialConstants.TYPE_REQUEST, str4);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public <T> T extendFuncGen(String str, String str2, String str3, Object... objArr) {
        BaseModules baseModulesGenModuleObj;
        LogModule.d(TAG, String.format("extendFuncGen source:%s, moudule:%s, data:%s", str, str2, str3));
        synchronized (this) {
            if (!this.modulesMap.containsKey(str2) || (baseModulesGenModuleObj = this.modulesMap.get(str2)) == null) {
                baseModulesGenModuleObj = genModuleObj(str2);
            }
        }
        if (baseModulesGenModuleObj != null) {
            return (T) baseModulesGenModuleObj.extendFuncGen("native", str, str3, objArr);
        }
        return null;
    }

    public void extendFuncCallback(String str, String str2, String str3, String str4) {
        if ("native".equals(str)) {
            LogModule.d(TAG, "call nativeCallback");
            this.callback.callback(str2, str3, str4);
        } else if (Constant.JNI_CALL.equals(str)) {
            LogModule.d(TAG, "call jniCallback");
            handleJniCallback(str2, str3, str4);
        } else {
            if (Constant.INNER_CALL.equals(str)) {
                return;
            }
            LogModule.d(TAG, "call allCallback");
            this.callback.callback(str2, str3, str4);
            handleJniCallback(str2, str3, str4);
        }
    }

    public synchronized boolean hasModule(String str) {
        if (this.modulesMap.containsKey(str)) {
            return true;
        }
        return genModuleObj(str) != null;
    }

    public void handleJniCallback(String str, String str2, String str3) {
        if (isSoLoadDone) {
            try {
                jniCallback(str, str2, str3);
            } catch (Throwable unused) {
            }
        }
    }

    public static void jniSoLoadDone(boolean z) {
        isSoLoadDone = z;
        Log.i(TAG, "isSoLoadDone = " + isSoLoadDone);
    }

    public static String jniCallExtendFunc(String str, String str2, String str3) {
        return ModulesManagerInst.extendFuncAll(Constant.JNI_CALL, str, str2, str3, new Object[0]);
    }
}