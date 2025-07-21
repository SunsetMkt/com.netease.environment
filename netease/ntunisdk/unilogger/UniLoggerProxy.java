package com.netease.ntunisdk.unilogger;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.configs.ConfigCallBack;
import com.netease.ntunisdk.unilogger.configs.ConfigProxy;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.network.NetworkProxy;
import com.netease.ntunisdk.unilogger.uploader.UploadProxy;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import com.netease.ntunisdk.unilogger.zip.ZipProxy;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class UniLoggerProxy {
    public static Context context;
    private static final Object lock = new Object();
    private static UniLoggerProxy uniLoggerProxy;
    private volatile int initState = 0;
    private HashMap<String, UniLogger> uniLoggerHashMap = new HashMap<>();

    private UniLoggerProxy() {
    }

    public static UniLoggerProxy getInstance() {
        if (uniLoggerProxy == null) {
            uniLoggerProxy = new UniLoggerProxy();
        }
        return uniLoggerProxy;
    }

    private void init(Context context2) {
        synchronized (lock) {
            Log.v(LogUtils.TAG, "UniLoggerProxy [init] initState=" + this.initState);
            if (this.initState == 0) {
                this.initState = 1;
                Log.v(LogUtils.TAG, "UniLoggerProxy [init] start");
                context = context2;
                initModule(context2);
            }
        }
    }

    private void initModule(Context context2) {
        LogUtils.checkDebugEnabled();
        ModulesManager.getInst().init(context2);
        GlobalPrarm.realGameId = Utils.getRealGameId(context2);
        NetworkProxy.init();
        GlobalPrarm.init(context2);
        ConfigProxy.getInstance().init(new ConfigCallBack() { // from class: com.netease.ntunisdk.unilogger.UniLoggerProxy.1
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.unilogger.configs.ConfigCallBack
            public void onResult(String str, Config.UnitResult unitResult, boolean z) {
                if (TextUtils.isEmpty(str) && unitResult == null) {
                    return;
                }
                try {
                    LogUtils.i_inner("UniLoggerProxy [init] [logger-trace] parse config finish, onResult unitName=" + str + Utils.showUnitResult(unitResult) + ", uniLoggerHashMap=" + UniLoggerProxy.this.uniLoggerHashMap.toString());
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    if (Const.CONFIG_KEY.ALL.equals(str)) {
                        Iterator it = UniLoggerProxy.this.uniLoggerHashMap.keySet().iterator();
                        while (it.hasNext()) {
                            ((UniLogger) UniLoggerProxy.this.uniLoggerHashMap.get((String) it.next())).update(unitResult, true);
                        }
                        return;
                    }
                    if (UniLoggerProxy.this.uniLoggerHashMap.containsKey(str)) {
                        ((UniLogger) UniLoggerProxy.this.uniLoggerHashMap.get(str)).update(unitResult, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.w_inner("UniLoggerProxy [init] ConfigProxy onResult Exception=" + e.toString());
                }
            }
        });
        UploadProxy.getInstance().init(ConfigProxy.getInstance().getExpire(), ConfigProxy.getInstance().getCarrierLimit());
        UploadProxy.getInstance().uploadHistoryLogZips();
        ZipProxy.getInstance().init(ConfigProxy.getInstance().getExpire(), ConfigProxy.getInstance().getFileLimit());
        ZipProxy.getInstance().zipHistoryFileInOtherThread(ConfigProxy.getInstance().getPreUnitResult());
    }

    /* renamed from: com.netease.ntunisdk.unilogger.UniLoggerProxy$1 */
    class AnonymousClass1 implements ConfigCallBack {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.unilogger.configs.ConfigCallBack
        public void onResult(String str, Config.UnitResult unitResult, boolean z) {
            if (TextUtils.isEmpty(str) && unitResult == null) {
                return;
            }
            try {
                LogUtils.i_inner("UniLoggerProxy [init] [logger-trace] parse config finish, onResult unitName=" + str + Utils.showUnitResult(unitResult) + ", uniLoggerHashMap=" + UniLoggerProxy.this.uniLoggerHashMap.toString());
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                if (Const.CONFIG_KEY.ALL.equals(str)) {
                    Iterator it = UniLoggerProxy.this.uniLoggerHashMap.keySet().iterator();
                    while (it.hasNext()) {
                        ((UniLogger) UniLoggerProxy.this.uniLoggerHashMap.get((String) it.next())).update(unitResult, true);
                    }
                    return;
                }
                if (UniLoggerProxy.this.uniLoggerHashMap.containsKey(str)) {
                    ((UniLogger) UniLoggerProxy.this.uniLoggerHashMap.get(str)).update(unitResult, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.w_inner("UniLoggerProxy [init] ConfigProxy onResult Exception=" + e.toString());
            }
        }
    }

    private UniLogger createNewUniLoggerInstance(String str) {
        UniLogger uniLogger;
        synchronized (lock) {
            String str2 = Constants.COLLATION_DEFAULT;
            if (!TextUtils.isEmpty(str)) {
                str2 = str;
            }
            if (this.uniLoggerHashMap.containsKey(str2)) {
                LogUtils.i_inner("UniLoggerProxy [createNewUniLoggerInstance] get instance from uniLoggerHashMap, unitName=" + str2);
                uniLogger = this.uniLoggerHashMap.get(str2);
            } else {
                LogUtils.i_inner("UniLoggerProxy [createNewUniLoggerInstance] recreate instance, unitName=" + str2);
                UniLogger uniLogger2 = new UniLogger(str2);
                this.uniLoggerHashMap.put(str, uniLogger2);
                uniLogger = uniLogger2;
            }
            LogUtils.i_inner("UniLoggerProxy [createNewUniLoggerInstance] uniLoggerHashMap=" + this.uniLoggerHashMap.keySet().toString());
        }
        return uniLogger;
    }

    public UniLogger createNewUniLoggerInstance(Context context2, String str) {
        UniLogger uniLoggerCreateNewUniLoggerInstance;
        synchronized (lock) {
            init(context2);
            uniLoggerCreateNewUniLoggerInstance = createNewUniLoggerInstance(str);
        }
        return uniLoggerCreateNewUniLoggerInstance;
    }
}