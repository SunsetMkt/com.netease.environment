package com.netease.cloud.nos.android.constants;

/* loaded from: classes6.dex */
public class Code {
    public static final int BAD_REQUEST = 400;
    public static final int CACHE_EXPIRED = 404;
    public static final int CALLBACK_ERROR = 520;
    public static final int CONNECTION_REFUSED = 901;
    public static final int CONNECTION_RESET = 902;
    public static final int CONNECTION_TIMEOUT = 900;
    public static final int HTTP_EXCEPTION = 799;
    public static final int HTTP_NO_RESPONSE = 899;
    public static final int HTTP_SUCCESS = 200;
    public static final int INVALID_LBS_DATA = 700;
    public static final int INVALID_OFFSET = 699;
    public static final int INVALID_RESPONSE_DATA = 701;
    public static final int INVALID_TOKEN = 403;
    public static final int LBS_ERROR = 400;
    public static final int MONITOR_CANCELED = 2;
    public static final int MONITOR_FAIL = 1;
    public static final int MONITOR_SUCCESS = 0;
    public static final int SERVER_ERROR = 500;
    public static final int SOCKET_TIMEOUT = 903;
    public static final int SSL_FAILED = 904;
    public static final int UNKNOWN_REASON = 999;
    public static final int UPLOADING_CANCEL = 600;

    public static String getDes(int i) {
        return i != 200 ? i != 400 ? i != 403 ? i != 500 ? i != 520 ? i != 699 ? i != 799 ? i != 899 ? "could not upload file with unknown reason, please contact with us" : "could not upload file with no http response, please contact with us" : "could not upload file with http exception, please wait for network recover" : "could not upload file with invalid break point offset." : "could not upload file with callback error." : "could not upload file with server inner error, please contact with us" : "could not upload file with invalid token, please change your token before uploading" : "bad request, please confirm the sdk usage" : "file upload success";
    }

    public static boolean isOK(int i) {
        return i == 200;
    }
}