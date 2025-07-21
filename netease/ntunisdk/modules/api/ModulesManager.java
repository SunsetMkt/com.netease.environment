package com.netease.ntunisdk.modules.api;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.InnerModulesCallback;
import com.netease.ntunisdk.modules.base.InnerModulesManager;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ModulesManager extends LifecycleManager implements InnerModulesCallback {
    private static ModulesManager ModulesManagerInst = new ModulesManager();
    private static final String TAG = "UniSDK ModulesManager";
    public static final String VERSION = "1.3.2";
    private HashMap<String, HashMap<String, ModulesCallbackEntiy>> callbackMap = new HashMap<>();
    private HashMap<String, ModulesCallbackEntiy> sourceCallbackMap = new HashMap<>();
    private boolean isInit = false;

    public String getVersion() {
        return "1.3.2";
    }

    public static ModulesManager getInst() {
        return ModulesManagerInst;
    }

    private ModulesManager() {
        InnerModulesManager.getInst().setCallback(this);
    }

    public void init(Context context) {
        InnerModulesManager.getInst().init(context);
        this.isInit = true;
    }

    public void unInit() {
        this.isInit = false;
        InnerModulesManager.getInst().unInit();
    }

    @Override // com.netease.ntunisdk.modules.api.LifecycleManager
    public void onCreate(Bundle bundle) {
        if (!this.isInit) {
            throw new IllegalStateException("please call init method first!!!");
        }
        super.onCreate(bundle);
    }

    public void reInit(Context context) {
        InnerModulesManager.getInst().reInit(context);
    }

    public int addModuleCallback(String str, ModulesCallback modulesCallback) {
        return addModuleCallback(str, false, modulesCallback);
    }

    public synchronized int addModuleCallback(String str, boolean z, ModulesCallback modulesCallback) {
        ModulesCallbackEntiy modulesCallbackEntiy;
        LogModule.d(TAG, String.format("addModuleCallback,source:%s, recPush:%s", str, Boolean.valueOf(z)));
        modulesCallbackEntiy = new ModulesCallbackEntiy(str, z, modulesCallback);
        this.sourceCallbackMap.put(str, modulesCallbackEntiy);
        return modulesCallbackEntiy.cbId;
    }

    public int addModuleCallback(String str, String str2, ModulesCallback modulesCallback) {
        return addModuleCallback(str, false, str2, modulesCallback);
    }

    public synchronized int addModuleCallback(String str, boolean z, String str2, ModulesCallback modulesCallback) {
        ModulesCallbackEntiy modulesCallbackEntiy;
        LogModule.d(TAG, String.format("addModuleCallback,source:%s, recPush:%s, module:%s", str, Boolean.valueOf(z), str2));
        modulesCallbackEntiy = new ModulesCallbackEntiy(str, z, modulesCallback);
        if (this.callbackMap.containsKey(str)) {
            HashMap<String, ModulesCallbackEntiy> map = this.callbackMap.get(str);
            if (map.containsKey(str2)) {
                LogModule.e(TAG, String.format("addModuleCallback, module:%s has exist, and reset", str2));
            }
            map.put(str2, modulesCallbackEntiy);
        } else {
            HashMap<String, ModulesCallbackEntiy> map2 = new HashMap<>();
            map2.put(str2, modulesCallbackEntiy);
            this.callbackMap.put(str, map2);
        }
        return modulesCallbackEntiy.cbId;
    }

    public synchronized void removeModuleCallback(int i) {
        for (String str : this.sourceCallbackMap.keySet()) {
            if (i == this.sourceCallbackMap.get(str).cbId) {
                this.sourceCallbackMap.remove(str);
                return;
            }
        }
        Iterator<String> it = this.callbackMap.keySet().iterator();
        while (it.hasNext()) {
            HashMap<String, ModulesCallbackEntiy> map = this.callbackMap.get(it.next());
            for (String str2 : map.keySet()) {
                if (i == map.get(str2).cbId) {
                    map.remove(str2);
                    return;
                }
            }
        }
    }

    public String extendFunc(String str, String str2, String str3) {
        return extendFunc(str, str2, str3, new Object[0]);
    }

    public String extendFunc(String str, String str2, String str3, Object... objArr) {
        return InnerModulesManager.getInst().extendFunc(str, str2, str3, objArr);
    }

    public <T> T extendFuncGen(String str, String str2, String str3, Object... objArr) {
        return (T) InnerModulesManager.getInst().extendFuncGen(str, str2, str3, objArr);
    }

    @Override // com.netease.ntunisdk.modules.base.InnerModulesCallback
    public synchronized void callback(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str)) {
            if (this.sourceCallbackMap.containsKey(str)) {
                ModulesCallbackEntiy modulesCallbackEntiy = this.sourceCallbackMap.get(str);
                if (modulesCallbackEntiy != null && modulesCallbackEntiy.cb != null) {
                    LogModule.d(TAG, "source callback");
                    modulesCallbackEntiy.cb.extendFuncCallback(modulesCallbackEntiy.source, str2, str3);
                }
            } else {
                HashMap<String, ModulesCallbackEntiy> map = this.callbackMap.get(str);
                if (map != null) {
                    ModulesCallbackEntiy modulesCallbackEntiy2 = map.get(str2);
                    if (modulesCallbackEntiy2 != null && modulesCallbackEntiy2.cb != null) {
                        LogModule.d(TAG, "source and module callback");
                        modulesCallbackEntiy2.cb.extendFuncCallback(modulesCallbackEntiy2.source, str2, str3);
                    }
                } else {
                    LogModule.e(TAG, String.format("callbackMap have not contain source\uff1a%s", str));
                }
            }
        } else {
            for (String str4 : this.sourceCallbackMap.keySet()) {
                ModulesCallbackEntiy modulesCallbackEntiy3 = this.sourceCallbackMap.get(str2);
                if (modulesCallbackEntiy3 != null && modulesCallbackEntiy3.recPush && modulesCallbackEntiy3.cb != null) {
                    modulesCallbackEntiy3.cb.extendFuncCallback(modulesCallbackEntiy3.source, str2, str3);
                }
            }
            for (String str5 : this.callbackMap.keySet()) {
                if (!this.sourceCallbackMap.containsKey(str5)) {
                    HashMap<String, ModulesCallbackEntiy> map2 = this.callbackMap.get(str5);
                    if (map2.containsKey(str2)) {
                        ModulesCallbackEntiy modulesCallbackEntiy4 = map2.get(str2);
                        if (modulesCallbackEntiy4 != null && modulesCallbackEntiy4.recPush) {
                            if (modulesCallbackEntiy4.cb != null) {
                                modulesCallbackEntiy4.cb.extendFuncCallback(modulesCallbackEntiy4.source, str2, str3);
                            }
                        } else {
                            LogModule.d(TAG, String.format("source %s not receive %s push", str5, str2));
                        }
                    } else {
                        LogModule.e(TAG, String.format("modulesCallbackEntityMap have not contain module\uff1a%s", str2));
                    }
                }
            }
        }
    }

    public synchronized boolean hasModule(String str) {
        return InnerModulesManager.getInst().hasModule(str);
    }
}