package com.netease.ntunisdk.unilogger.configs;

import android.text.TextUtils;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.network.ConfigRequest;
import com.netease.ntunisdk.unilogger.network.NetCallback;
import com.netease.ntunisdk.unilogger.network.NetworkProxy;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.io.File;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigProxy {
    private static ConfigProxy configProxy;
    private JSONObject preUnitResult = new JSONObject();
    private ConfigCallBack configCallBack = null;
    private HashMap<String, Boolean> callBackMap = new HashMap<>();
    private Config localConfig = null;
    private Config remoteConfig = null;
    private volatile boolean hasInit = false;
    ConfigCallBack tConfigCallBack = new ConfigCallBack() { // from class: com.netease.ntunisdk.unilogger.configs.ConfigProxy.1
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.unilogger.configs.ConfigCallBack
        public void onResult(String str, Config.UnitResult unitResult, boolean z) {
            LogUtils.i_inner("ConfigProxy ConfigCallBack [onResult] unitName=" + str + ", callBackMap=" + ConfigProxy.this.callBackMap.toString() + ", isRemote=" + z);
            if (!ConfigProxy.this.callBackMap.containsKey(str)) {
                ConfigProxy.this.callBackMap.put(str, Boolean.valueOf(z));
                if (ConfigProxy.this.configCallBack != null) {
                    ConfigProxy.this.configCallBack.onResult(str, unitResult, z);
                    return;
                }
                return;
            }
            if (((Boolean) ConfigProxy.this.callBackMap.get(str)).booleanValue() || !z || ConfigProxy.this.configCallBack == null) {
                return;
            }
            ConfigProxy.this.configCallBack.onResult(str, unitResult, z);
        }
    };
    public long requestLocalConfigDelay = 0;
    public long requestRemoteConfigDelay = 0;
    public JSONObject localConfigJson = null;
    public JSONObject remoteConfigJson = null;

    public static ConfigProxy getInstance() {
        if (configProxy == null) {
            configProxy = new ConfigProxy();
        }
        return configProxy;
    }

    /* renamed from: com.netease.ntunisdk.unilogger.configs.ConfigProxy$1 */
    class AnonymousClass1 implements ConfigCallBack {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.unilogger.configs.ConfigCallBack
        public void onResult(String str, Config.UnitResult unitResult, boolean z) {
            LogUtils.i_inner("ConfigProxy ConfigCallBack [onResult] unitName=" + str + ", callBackMap=" + ConfigProxy.this.callBackMap.toString() + ", isRemote=" + z);
            if (!ConfigProxy.this.callBackMap.containsKey(str)) {
                ConfigProxy.this.callBackMap.put(str, Boolean.valueOf(z));
                if (ConfigProxy.this.configCallBack != null) {
                    ConfigProxy.this.configCallBack.onResult(str, unitResult, z);
                    return;
                }
                return;
            }
            if (((Boolean) ConfigProxy.this.callBackMap.get(str)).booleanValue() || !z || ConfigProxy.this.configCallBack == null) {
                return;
            }
            ConfigProxy.this.configCallBack.onResult(str, unitResult, z);
        }
    }

    public synchronized boolean init(ConfigCallBack configCallBack) {
        if (!this.hasInit) {
            this.hasInit = true;
            LogUtils.v_inner("ConfigProxy [init] start");
            this.configCallBack = configCallBack;
            handlePreUnitResult();
            readLocalConfig();
            requestRemoteConfig();
        }
        return true;
    }

    private void handlePreUnitResult() {
        this.preUnitResult = ConfigUtil.readUnitResultFromFile();
        ConfigUtil.deletePreUnitResultFile();
        LogUtils.i_inner("ConfigProxy [handlePreUnitResult] preUnitResult=" + this.preUnitResult);
    }

    public JSONObject getPreUnitResult() {
        return this.preUnitResult;
    }

    public void containValueFromConfigKey(String str, String str2) {
        Config config = this.localConfig;
        if (config != null) {
            config.containValueFromConfigKey(str, str2);
        }
        Config config2 = this.remoteConfig;
        if (config2 != null) {
            config2.containValueFromConfigKey(str, str2);
        }
    }

    public int getExpire() {
        Config config = this.localConfig;
        if (config != null) {
            return config.expire;
        }
        return 30;
    }

    public long getCarrierLimit() {
        Config config = this.localConfig;
        if (config != null) {
            return config.carrierLimit;
        }
        return 10485760L;
    }

    public long getFileLimit() {
        Config config = this.localConfig;
        if (config != null) {
            return config.fileLimit;
        }
        return 10485760L;
    }

    public boolean getWifiOnly() {
        Config config = this.localConfig;
        if (config != null) {
            return config.wifiOnly;
        }
        return false;
    }

    /* renamed from: com.netease.ntunisdk.unilogger.configs.ConfigProxy$2 */
    class AnonymousClass2 implements NetCallback {
        AnonymousClass2() {
        }

        @Override // com.netease.ntunisdk.unilogger.network.NetCallback
        public void onNetCallback(int i, String str) {
            synchronized (this) {
                LogUtils.i_inner("ConfigProxy [requestRemoteConfig] ConfigRequest [onNetCallback] code=" + i + ", info=" + str);
                if (200 == i) {
                    try {
                        if (!TextUtils.isEmpty(str) && !"{}".equals(str)) {
                            LogUtils.w_inner("ConfigProxy [requestRemoteConfig] ConfigRequest [onNetCallback] remoteConfig write to file, result=" + Utils.str2File(str, GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME) + ", filePath=" + GlobalPrarm.uniLoggerDirPath + File.separator + Const.FILE.LOCAL_CONFIG_FILE_NAME);
                            JSONObject jSONObject = new JSONObject(str);
                            if (ConfigProxy.this.remoteConfig == null) {
                                ConfigProxy.this.remoteConfig = new Config();
                            }
                            ConfigProxy.this.remoteConfig.parseConfig(jSONObject, ConfigProxy.this.tConfigCallBack, true);
                        } else {
                            Utils.deleteFile(GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.w_inner("ConfigProxy [requestRemoteConfig] ConfigRequest [onNetCallback] Exception=" + e.toString());
                    }
                } else if (-2 == i) {
                    LogUtils.i_inner("ConfigProxy [requestRemoteConfig] delete file:unilogger_local_config_file");
                    Utils.deleteFile(GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME);
                    if (ConfigProxy.this.remoteConfig == null) {
                        ConfigProxy.this.remoteConfig = new Config();
                    }
                }
            }
        }
    }

    private void requestRemoteConfig() {
        ConfigRequest configRequest = new ConfigRequest();
        configRequest.registerNetCallback(new NetCallback() { // from class: com.netease.ntunisdk.unilogger.configs.ConfigProxy.2
            AnonymousClass2() {
            }

            @Override // com.netease.ntunisdk.unilogger.network.NetCallback
            public void onNetCallback(int i, String str) {
                synchronized (this) {
                    LogUtils.i_inner("ConfigProxy [requestRemoteConfig] ConfigRequest [onNetCallback] code=" + i + ", info=" + str);
                    if (200 == i) {
                        try {
                            if (!TextUtils.isEmpty(str) && !"{}".equals(str)) {
                                LogUtils.w_inner("ConfigProxy [requestRemoteConfig] ConfigRequest [onNetCallback] remoteConfig write to file, result=" + Utils.str2File(str, GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME) + ", filePath=" + GlobalPrarm.uniLoggerDirPath + File.separator + Const.FILE.LOCAL_CONFIG_FILE_NAME);
                                JSONObject jSONObject = new JSONObject(str);
                                if (ConfigProxy.this.remoteConfig == null) {
                                    ConfigProxy.this.remoteConfig = new Config();
                                }
                                ConfigProxy.this.remoteConfig.parseConfig(jSONObject, ConfigProxy.this.tConfigCallBack, true);
                            } else {
                                Utils.deleteFile(GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.w_inner("ConfigProxy [requestRemoteConfig] ConfigRequest [onNetCallback] Exception=" + e.toString());
                        }
                    } else if (-2 == i) {
                        LogUtils.i_inner("ConfigProxy [requestRemoteConfig] delete file:unilogger_local_config_file");
                        Utils.deleteFile(GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME);
                        if (ConfigProxy.this.remoteConfig == null) {
                            ConfigProxy.this.remoteConfig = new Config();
                        }
                    }
                }
            }
        });
        NetworkProxy.addToUploadQueue(configRequest);
    }

    private synchronized void readLocalConfig() {
        String strFile2Str = Utils.file2Str(GlobalPrarm.uniLoggerDirPath, Const.FILE.LOCAL_CONFIG_FILE_NAME);
        LogUtils.i_inner("Config [readLocalConfig] read localConfig file, localConfigFileInfo=" + strFile2Str);
        try {
            if (!TextUtils.isEmpty(strFile2Str)) {
                JSONObject jSONObject = new JSONObject(strFile2Str);
                if (this.localConfig == null) {
                    this.localConfig = new Config();
                }
                this.localConfig.parseConfig(jSONObject, this.tConfigCallBack, false);
            } else {
                LogUtils.i_inner("Config [readLocalConfig] read localConfig file, file content is error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Config [readLocalConfig] Exception=" + e.toString());
        }
    }

    public Config.UnitResult getUnitResult(String str) {
        Config config = this.remoteConfig;
        if (config != null) {
            return config.getUnitResult(str);
        }
        Config config2 = this.localConfig;
        if (config2 != null) {
            return config2.getUnitResult(str);
        }
        return null;
    }

    public void setRequestLocalConfigDelay(long j) {
        this.requestLocalConfigDelay = j;
    }

    public void setRequestRemoteConfigDelay(long j) {
        this.requestRemoteConfigDelay = j;
    }

    public void setLocalConfigJson(JSONObject jSONObject) {
        this.localConfigJson = jSONObject;
    }

    public void setRemoteConfigJson(JSONObject jSONObject) {
        this.remoteConfigJson = jSONObject;
    }
}