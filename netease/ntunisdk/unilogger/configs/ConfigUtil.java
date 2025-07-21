package com.netease.ntunisdk.unilogger.configs;

import android.text.TextUtils;
import com.netease.ntunisdk.unilogger.configs.Config;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigUtil {
    public static void writeUnitResultToFile(HashMap<String, Config.UnitResult> map) throws JSONException {
        if (map != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                for (String str : map.keySet()) {
                    jSONObject.put(str, map.get(str).uploadEnable);
                }
                Utils.str2File(jSONObject.toString(), GlobalPrarm.uniLoggerDirPath, Const.FILE.UNIT_RESULT_FILE_NAME);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.w_inner("ConfigLocal [writeConfigToLocalConfigFile] Exception=" + e.toString());
            }
        }
    }

    public static JSONObject readUnitResultFromFile() {
        JSONObject jSONObject = new JSONObject();
        try {
            String strFile2Str = Utils.file2Str(GlobalPrarm.uniLoggerDirPath, Const.FILE.UNIT_RESULT_FILE_NAME);
            return !TextUtils.isEmpty(strFile2Str) ? new JSONObject(strFile2Str) : jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w_inner("ConfigLocal [readConfigUnitResultFromFile] Exception=" + e.toString());
            return jSONObject;
        }
    }

    public static boolean deletePreUnitResultFile() {
        return Utils.deleteFile(GlobalPrarm.uniLoggerDirPath, Const.FILE.UNIT_RESULT_FILE_NAME);
    }
}