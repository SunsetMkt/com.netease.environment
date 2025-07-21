package com.netease.ntunisdk.modules.base.call;

import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.InnerModulesManager;
import com.netease.ntunisdk.modules.base.entity.RegisterModuleEntity;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class SDKModulesCall extends DefaultModulesCall {
    private static final String TAG = "UniSDK SDKModulesCall";
    private Map<String, HashMap<String, RegisterModuleEntity>> regModulesMap = new HashMap();

    public SDKModulesCall(InnerModulesManager innerModulesManager) {
        this.innerModulesManager = innerModulesManager;
    }

    @Override // com.netease.ntunisdk.modules.base.call.IModulesCall
    public synchronized void registerModulesListener(String str, boolean z, BaseModules baseModules) {
        if (this.regModulesMap.containsKey(str)) {
            LogModule.d(TAG, "regModulesMap contain:" + str);
            this.regModulesMap.get(str).put(baseModules.getModuleName(), new RegisterModuleEntity(z, baseModules));
        } else {
            HashMap<String, RegisterModuleEntity> map = new HashMap<>();
            map.put(baseModules.getModuleName(), new RegisterModuleEntity(z, baseModules));
            LogModule.d(TAG, "regModulesMap put:" + str);
            this.regModulesMap.put(str, map);
        }
    }

    @Override // com.netease.ntunisdk.modules.base.call.IModulesCall
    public synchronized void unRegisterModulesListener(String str, BaseModules baseModules) {
        if (this.regModulesMap.containsKey(str)) {
            HashMap<String, RegisterModuleEntity> map = this.regModulesMap.get(str);
            for (String str2 : map.keySet()) {
                if (str2.equalsIgnoreCase(baseModules.getModuleName())) {
                    map.remove(str2);
                    LogModule.d(TAG, str2 + " unRegisterModulesListener " + str);
                    return;
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.modules.base.call.IModulesCall
    public synchronized void notiSDKOthersModules(String str, String str2, String str3) {
        if (!this.regModulesMap.isEmpty() && this.regModulesMap.containsKey(str2)) {
            HashMap<String, RegisterModuleEntity> map = this.regModulesMap.get(str2);
            if (map.containsKey(str)) {
                LogModule.d(TAG, str + " receiveOthersModulesMsg");
                map.get(str).moduleObj.receiveOthersModulesMsg(str2, str3);
            } else if (TextUtils.isEmpty(str)) {
                LogModule.d(TAG, "not specify source, all register module can receive msg");
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    map.get(it.next()).moduleObj.receiveOthersModulesMsg(str2, str3);
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.modules.base.call.IModulesCall
    public String callSDKOthersModules(String str, String str2, String str3, Object... objArr) {
        return this.innerModulesManager.extendFuncByInner(str, str2, str3, objArr);
    }
}