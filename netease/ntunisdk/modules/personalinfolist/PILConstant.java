package com.netease.ntunisdk.modules.personalinfolist;

import com.netease.ntunisdk.cutout.RespUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PILConstant {
    public static final String DISABLE_PIL_REPORT = "DISABLE_PIL_REPORT";
    public static final String EB = "EB";
    public static String INNER_CLIENT_LOG_URL = "https://applog.matrix.netease.com/client/sdk/clientlog";
    public static String JF_LOG_KEY_CONTENT = "";
    public static final String MODEL_NAME = "personalInfoListModule";
    public static final String VERSION = "1.1.0";
    public static boolean isOversea = false;
    public static boolean isReport = true;

    public enum PILExtendCode {
        SUCCESS("0", "success"),
        NO_EXIST_METHOD("1", "\u65b9\u6cd5\u4e0d\u5b58\u5728 (channel\u4e0bmethodId\u4e0d\u5b58\u5728)"),
        PARAM_ERROR("2", "\u53c2\u6570\u9519\u8bef\uff08\u662f\u5426\u5fc5\u586b\u3001\u7c7b\u578b\u9519\u8bef\uff09"),
        UNKNOWN_ERROR("10000", "\u672a\u77e5\u9519\u8bef");

        public String code;
        public String describe;

        PILExtendCode(String str, String str2) {
            this.code = str;
            this.describe = str2;
        }

        public String getCode() {
            return this.code;
        }

        public String getDescribe() {
            return this.describe;
        }

        public static void formatResult(JSONObject jSONObject, PILExtendCode pILExtendCode) throws JSONException {
            try {
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, pILExtendCode.getCode());
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, pILExtendCode.getDescribe());
            } catch (Exception unused) {
            }
        }
    }
}