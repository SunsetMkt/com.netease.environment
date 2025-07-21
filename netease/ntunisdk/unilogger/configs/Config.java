package com.netease.ntunisdk.unilogger.configs;

import android.text.TextUtils;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.network.NetCallback;
import com.netease.ntunisdk.unilogger.network.NetworkProxy;
import com.netease.ntunisdk.unilogger.network.WhoamiRequest;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Config {
    private boolean isRemote = false;
    public boolean wifiOnly = false;
    public int expire = 30;
    public long fileLimit = 10485760;
    public long carrierLimit = 10485760;
    private ConfigCallBack configCallBack = null;
    private HashMap<String, Integer> keysMap = new HashMap<>();
    private HashMap<String, UnitResult> resultMap = new HashMap<>();
    private JSONObject configJson = null;
    private boolean hadRequestRegion = false;

    public class UnitResult {
        public boolean isCallback = false;
        public boolean isRemote = false;
        public boolean writeEnable = true;
        public boolean uploadEnable = false;
        public HashMap<String, Integer> orMap = new HashMap<>();
        public HashMap<String, Integer> andMap = new HashMap<>();

        public UnitResult() {
        }
    }

    public synchronized void parseConfig(JSONObject jSONObject, ConfigCallBack configCallBack, boolean z) {
        this.isRemote = z;
        init(jSONObject, configCallBack);
        start();
    }

    private void init(JSONObject jSONObject, ConfigCallBack configCallBack) {
        this.configJson = jSONObject;
        this.configCallBack = configCallBack;
    }

    private void start() throws JSONException, ClassNotFoundException {
        parseActionParamsFromConfig();
        initResultMap();
        fillResultMap();
    }

    private void setDefaultInfoToResultMapByUnitname(String str, JSONObject jSONObject) {
        if (TextUtils.isEmpty(str) || jSONObject == null || jSONObject.length() == 0 || !jSONObject.has(str)) {
            return;
        }
        try {
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(str);
            if (jSONObjectOptJSONObject == null || jSONObjectOptJSONObject.length() == 0) {
                return;
            }
            if (jSONObjectOptJSONObject.has("or")) {
                Iterator<String> itKeys = jSONObjectOptJSONObject.optJSONObject("or").keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    this.keysMap.put(next, 1);
                    this.resultMap.get(str).orMap.put(next, 0);
                }
            }
            if (jSONObjectOptJSONObject.has("and")) {
                Iterator<String> itKeys2 = jSONObjectOptJSONObject.optJSONObject("and").keys();
                while (itKeys2.hasNext()) {
                    String next2 = itKeys2.next();
                    this.keysMap.put(next2, 1);
                    this.resultMap.get(str).andMap.put(next2, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Config [parseConfigByKey] Exception=" + e.toString() + ", isRemote=" + this.isRemote);
        }
    }

    private void parseActionParamsFromConfig() {
        JSONObject jSONObject = this.configJson;
        if (jSONObject == null || jSONObject.length() == 0) {
            return;
        }
        if (this.configJson.has(Const.CONFIG_KEY.WIFI_ONLY)) {
            this.wifiOnly = this.configJson.optBoolean(Const.CONFIG_KEY.WIFI_ONLY);
        }
        if (this.configJson.has(Const.CONFIG_KEY.EXPIRE)) {
            this.expire = this.configJson.optInt(Const.CONFIG_KEY.EXPIRE);
        }
        if (this.configJson.has(Const.CONFIG_KEY.FILE_LIMIT)) {
            this.fileLimit = this.configJson.optLong(Const.CONFIG_KEY.FILE_LIMIT);
        }
        if (this.configJson.has(Const.CONFIG_KEY.CARRIER_LIMIT)) {
            this.carrierLimit = this.configJson.optLong(Const.CONFIG_KEY.CARRIER_LIMIT);
        }
        LogUtils.i_inner("Config [parseActionParamsFromConfig] wifiOnly=" + this.wifiOnly + ", expire=" + this.expire + ", fileLimit=" + this.fileLimit + ", carrierLimit=" + this.carrierLimit + ", isRemote=" + this.isRemote);
    }

    private void initResultMap() {
        JSONObject jSONObject = this.configJson;
        if (jSONObject == null || jSONObject.length() == 0) {
            return;
        }
        if (this.configJson.has(Const.CONFIG_KEY.ALL)) {
            this.resultMap.clear();
            addUnitToResultMapByUnitname(Const.CONFIG_KEY.ALL);
            setDefaultInfoToResultMapByUnitname(Const.CONFIG_KEY.ALL, this.configJson);
            return;
        }
        Iterator<String> itKeys = this.configJson.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (!Const.CONFIG_KEY.WIFI_ONLY.equals(next) && !Const.CONFIG_KEY.EXPIRE.equals(next) && !Const.CONFIG_KEY.FILE_LIMIT.equals(next) && !Const.CONFIG_KEY.CARRIER_LIMIT.equals(next) && !Const.CONFIG_KEY.QUEUE_SIZE.equals(next) && !Const.CONFIG_KEY.UPLOAD_URL.equals(next)) {
                addUnitToResultMapByUnitname(next);
                setDefaultInfoToResultMapByUnitname(next, this.configJson);
            }
        }
    }

    private void addUnitToResultMapByUnitname(String str) {
        if (TextUtils.isEmpty(str) || this.resultMap == null) {
            return;
        }
        UnitResult unitResult = new UnitResult();
        unitResult.isRemote = this.isRemote;
        this.resultMap.put(str, unitResult);
    }

    private void fillResultMap() throws JSONException, ClassNotFoundException {
        JSONObject jSONObject = this.configJson;
        if (jSONObject == null || jSONObject.length() == 0) {
            return;
        }
        if (this.keysMap.containsKey(Const.CONFIG_KEY.LOCAL_IP)) {
            handleLocalIp();
        }
        if (this.keysMap.containsKey("udid") && !TextUtils.isEmpty(GlobalPrarm.udid)) {
            containValueFromConfigKey(GlobalPrarm.udid, "udid");
        }
        if (this.keysMap.containsKey("uid") && !TextUtils.isEmpty(GlobalPrarm.uid)) {
            containValueFromConfigKey(GlobalPrarm.uid, "uid");
        }
        if (this.keysMap.containsKey(Const.CONFIG_KEY.ROLEID) && !TextUtils.isEmpty(GlobalPrarm.roleId)) {
            containValueFromConfigKey(GlobalPrarm.roleId, Const.CONFIG_KEY.ROLEID);
        }
        if (this.keysMap.containsKey("gameid") && !TextUtils.isEmpty(GlobalPrarm.gameId)) {
            containValueFromConfigKey(GlobalPrarm.gameId, "gameid");
        }
        if (this.keysMap.containsKey("package")) {
            containValueFromConfigKey(GlobalPrarm.packageName, "package", this.configJson);
        }
        if (this.keysMap.containsKey("model")) {
            containValueFromConfigKey(GlobalPrarm.model, "model", this.configJson);
        }
        if (this.keysMap.containsKey("os_ver")) {
            containValueFromConfigKey(GlobalPrarm.osVer, "os_ver", this.configJson);
        }
        if (this.keysMap.containsKey("sdk_version")) {
            containValueFromConfigKey(GlobalPrarm.sdkVersion, "sdk_version", this.configJson);
        }
        if (this.keysMap.containsKey("region")) {
            handleRegion();
        }
        if (this.keysMap.containsKey(Const.CONFIG_KEY.UNISDK_VERSION)) {
            handleUnisdkVersion();
        }
        if (this.keysMap.containsKey("channel_id")) {
            handleChannelId();
        }
        if (this.keysMap.containsKey(Const.CONFIG_KEY.CHANNEL_VERSION)) {
            handleChannelVersion();
        }
    }

    private void handleLocalIp() throws JSONException {
        if (!TextUtils.isEmpty(GlobalPrarm.localIp)) {
            containValueFromConfigKey(GlobalPrarm.localIp, Const.CONFIG_KEY.LOCAL_IP, this.configJson);
            return;
        }
        String localIpFromModuleDeviceinfo = Utils.getLocalIpFromModuleDeviceinfo();
        LogUtils.i_inner("Config [startEveryModuleConfigKey] localIp=" + localIpFromModuleDeviceinfo + ", isRemote=" + this.isRemote);
        GlobalPrarm.localIp = localIpFromModuleDeviceinfo;
        containValueFromConfigKey(localIpFromModuleDeviceinfo, Const.CONFIG_KEY.LOCAL_IP, this.configJson);
    }

    private void handleRegion() {
        if (!TextUtils.isEmpty(GlobalPrarm.region)) {
            containValueFromConfigKey(GlobalPrarm.region, "region", this.configJson);
            return;
        }
        if (this.hadRequestRegion) {
            return;
        }
        this.hadRequestRegion = true;
        LogUtils.i_inner("Config [startEveryModuleConfigKey] WhoamiRequest \u8bf7\u6c42\u56fd\u5bb6\u4fe1\u606f, isRemote=" + this.isRemote);
        WhoamiRequest whoamiRequest = new WhoamiRequest();
        whoamiRequest.registerNetCallback(new NetCallback() { // from class: com.netease.ntunisdk.unilogger.configs.Config.1
            @Override // com.netease.ntunisdk.unilogger.network.NetCallback
            public void onNetCallback(int i, String str) {
                LogUtils.i_inner("Config [startEveryModuleConfigKey] WhoamiRequest [onNetCallback] country=" + str + ", isRemote=" + Config.this.isRemote);
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                GlobalPrarm.region = str;
                Config config = Config.this;
                config.containValueFromConfigKey(str, "region", config.configJson);
            }
        });
        NetworkProxy.addToUploadQueue(whoamiRequest);
    }

    private void handleUnisdkVersion() throws ClassNotFoundException {
        if (!TextUtils.isEmpty(GlobalPrarm.unisdkVersion)) {
            containValueFromConfigKey(GlobalPrarm.unisdkVersion, Const.CONFIG_KEY.UNISDK_VERSION, this.configJson);
            return;
        }
        String uniSDKBaseVersionReflexUniSDK = Utils.getUniSDKBaseVersionReflexUniSDK();
        GlobalPrarm.unisdkVersion = uniSDKBaseVersionReflexUniSDK;
        containValueFromConfigKey(uniSDKBaseVersionReflexUniSDK, Const.CONFIG_KEY.UNISDK_VERSION, this.configJson);
    }

    private void handleChannelId() {
        if (!TextUtils.isEmpty(GlobalPrarm.channelId)) {
            containValueFromConfigKey(GlobalPrarm.channelId, "channel_id", this.configJson);
            return;
        }
        String channelIdFromAssetsFile = Utils.getChannelIdFromAssetsFile();
        GlobalPrarm.channelId = channelIdFromAssetsFile;
        containValueFromConfigKey(channelIdFromAssetsFile, "channel_id", this.configJson);
    }

    private void handleChannelVersion() {
        if (!TextUtils.isEmpty(GlobalPrarm.channelVersion)) {
            containValueFromConfigKey(GlobalPrarm.channelVersion, Const.CONFIG_KEY.CHANNEL_VERSION, this.configJson);
            return;
        }
        String channelVersionFromAssetsFile = Utils.getChannelVersionFromAssetsFile();
        GlobalPrarm.channelVersion = channelVersionFromAssetsFile;
        containValueFromConfigKey(channelVersionFromAssetsFile, Const.CONFIG_KEY.CHANNEL_VERSION, this.configJson);
    }

    public synchronized void containValueFromConfigKey(String str, String str2, JSONObject jSONObject) {
        try {
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && jSONObject != null && jSONObject.length() > 0) {
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    if (!Const.CONFIG_KEY.WIFI_ONLY.equals(next) && !Const.CONFIG_KEY.EXPIRE.equals(next) && !Const.CONFIG_KEY.FILE_LIMIT.equals(next) && !Const.CONFIG_KEY.CARRIER_LIMIT.equals(next) && !Const.CONFIG_KEY.QUEUE_SIZE.equals(next) && !Const.CONFIG_KEY.UPLOAD_URL.equals(next)) {
                        containValueFromUnitConfigKey(next, str, str2, jSONObject.optJSONObject(next));
                    }
                }
                checkAllFromMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Config [containValueFromConfigKey] Exception=" + e.toString() + ", isRemote=" + this.isRemote);
        }
    }

    private void containValueFromUnitConfigKey(String str, String str2, String str3, JSONObject jSONObject) throws Exception {
        JSONArray jSONArrayOptJSONArray;
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(itKeys.next());
            if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.has(str3) && (jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray(str3)) != null && jSONArrayOptJSONArray.length() > 0) {
                int iContainValueFromJSONArray = containValueFromJSONArray(str2, jSONArrayOptJSONArray);
                LogUtils.i_inner("Config [containValueFromUnitConfigKey] unitName=" + str + " desValue=" + str2 + ", srcKey=" + str3 + ", result=" + iContainValueFromJSONArray + ", isRemote=" + this.isRemote);
                if (this.resultMap.containsKey(str)) {
                    updataResultToMap(str, str3, iContainValueFromJSONArray);
                    return;
                }
            }
        }
    }

    private int containValueFromJSONArray(String str, JSONArray jSONArray) throws Exception {
        for (int i = 0; i < jSONArray.length(); i++) {
            String strOptString = jSONArray.optString(i);
            if (!TextUtils.isEmpty(strOptString) && str.equals(strOptString)) {
                return 1;
            }
        }
        return 2;
    }

    public void containValueFromConfigKey(String str, String str2) {
        containValueFromConfigKey(str, str2, this.configJson);
    }

    private void updataResultToMap(String str, String str2, int i) {
        try {
            if (Const.CONFIG_KEY.LOCAL_IP.equals(str2) || "udid".equals(str2) || "uid".equals(str2) || Const.CONFIG_KEY.ROLEID.equals(str2) || "gameid".equals(str2)) {
                if (this.resultMap.containsKey(str) && this.resultMap.get(str).orMap != null) {
                    this.resultMap.get(str).orMap.put(str2, Integer.valueOf(i));
                }
            } else if (("package".equals(str2) || "model".equals(str2) || "os_ver".equals(str2) || "region".equals(str2) || "sdk_version".equals(str2) || Const.CONFIG_KEY.UNISDK_VERSION.equals(str2) || "channel_id".equals(str2) || Const.CONFIG_KEY.CHANNEL_VERSION.equals(str2)) && this.resultMap.containsKey(str) && this.resultMap.get(str).andMap != null) {
                this.resultMap.get(str).andMap.put(str2, Integer.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Config [updataResultToMap] Exception=" + e.toString() + ", isRemote=" + this.isRemote);
        }
    }

    private synchronized void checkAllFromMap() {
        try {
            for (String str : this.resultMap.keySet()) {
                HashMap<String, Integer> map = this.resultMap.get(str).orMap;
                HashMap<String, Integer> map2 = this.resultMap.get(str).andMap;
                if (!checkIsEmpty(str, map, map2)) {
                    checkFinalHit(str, checkOrMapHit(map), checkAndMapHit(map2));
                }
            }
            ConfigUtil.writeUnitResultToFile(this.resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Config [checkAllFromMap] Exception=" + e.toString() + ", isRemote=" + this.isRemote);
        }
    }

    private boolean checkIsEmpty(String str, HashMap<String, Integer> map, HashMap<String, Integer> map2) throws Exception {
        if (map.size() != 0 || map2.size() != 0) {
            return false;
        }
        boolean z = this.resultMap.get(str).writeEnable || this.resultMap.get(str).uploadEnable;
        this.resultMap.get(str).writeEnable = false;
        this.resultMap.get(str).uploadEnable = false;
        if (!this.resultMap.get(str).isCallback || z) {
            callback(str);
        }
        return true;
    }

    private int checkOrMapHit(HashMap<String, Integer> map) throws Exception {
        if (map.size() == 0) {
            return 1;
        }
        int i = 2;
        for (String str : map.keySet()) {
            if (1 == map.get(str).intValue()) {
                return 1;
            }
            if (map.get(str).intValue() == 0) {
                i = 0;
            }
        }
        return i;
    }

    private int checkAndMapHit(HashMap<String, Integer> map) throws Exception {
        int i = 1;
        if (map.size() == 0) {
            return 1;
        }
        for (String str : map.keySet()) {
            if (2 == map.get(str).intValue()) {
                return 2;
            }
            if (map.get(str).intValue() == 0) {
                i = 0;
            }
        }
        return i;
    }

    private void checkFinalHit(String str, int i, int i2) throws Exception {
        boolean z = true;
        if (2 == i || 2 == i2) {
            if (!this.resultMap.get(str).writeEnable && !this.resultMap.get(str).uploadEnable) {
                z = false;
            }
            this.resultMap.get(str).writeEnable = false;
            this.resultMap.get(str).uploadEnable = false;
            if (!this.resultMap.get(str).isCallback || z) {
                callback(str);
                return;
            }
            return;
        }
        if (1 == i && 1 == i2) {
            boolean z2 = (this.resultMap.get(str).writeEnable && this.resultMap.get(str).uploadEnable) ? false : true;
            this.resultMap.get(str).writeEnable = true;
            this.resultMap.get(str).uploadEnable = true;
            if (!this.resultMap.get(str).isCallback || z2) {
                callback(str);
            }
        }
    }

    private void callback(String str) throws Exception {
        if (this.configCallBack == null || TextUtils.isEmpty(str)) {
            return;
        }
        this.resultMap.get(str).isCallback = true;
        this.configCallBack.onResult(str, this.resultMap.get(str), this.isRemote);
    }

    public UnitResult getUnitResult(String str) {
        if (TextUtils.isEmpty(str) || !this.resultMap.containsKey(str)) {
            return null;
        }
        return this.resultMap.get(str);
    }

    private void showMsg() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : this.resultMap.keySet()) {
                StringBuffer stringBufferAppend = stringBuffer.append(str);
                StringBuilder sb = new StringBuilder();
                sb.append(", orMap=");
                Object obj = "null";
                sb.append(this.resultMap.get(str).orMap != null ? this.resultMap.get(str).orMap.toString() : "null");
                StringBuffer stringBufferAppend2 = stringBufferAppend.append(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(", andMap=");
                if (this.resultMap.get(str).andMap != null) {
                    obj = this.resultMap.get(str).andMap;
                }
                sb2.append(obj);
                stringBufferAppend2.append(sb2.toString()).append(", writeEnable=" + this.resultMap.get(str).writeEnable).append(", uploadEnable=" + this.resultMap.get(str).uploadEnable).append(" ");
            }
            LogUtils.i_inner("Config [showMsg] resultMap=" + stringBuffer.toString() + ", isRemote=" + this.isRemote);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("Config [showMsg] Exception=" + e.toString() + ", isRemote=" + this.isRemote);
        }
    }
}