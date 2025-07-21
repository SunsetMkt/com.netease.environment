package com.netease.ntunisdk.cutout;

/* loaded from: classes.dex */
public class RespUtil {

    public static class RespCode {
        public static final int EXCEPTION = 998;
        public static final int SUCCESS = 0;
        public static final int UNDEFINED_METHOD = 1;
        public static final int UNKNOWN_ERROR = 999;
    }

    public static class UniSdkField {
        public static final String RESP_CODE = "respCode";
        public static final String RESP_MSG = "respMsg";
    }

    public static String parseRespCode(int i) {
        return i != 0 ? i != 1 ? i != 999 ? "" : "\u672a\u77e5\u9519\u8bef" : "\u65b9\u6cd5\u4e0d\u5b58\u5728" : "success";
    }
}