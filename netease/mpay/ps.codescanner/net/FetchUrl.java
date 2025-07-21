package com.netease.mpay.ps.codescanner.net;

import android.os.Build;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes5.dex */
public class FetchUrl {
    private static final String GET_NETWORK_TIME_URL = "http://service.mkey.163.com/mpay/static/date.json";

    public static final class FetchUrlMethod {
        public static final int GET = 0;
        public static final int POST = 1;
    }

    public static final class FetchUrlResponse {
        public int code;
        public byte[] content;
        public HashMap<String, String> headers;
    }

    public static final class FetchUrlException extends Exception {
        private static final long serialVersionUID = -1225796193933530073L;
        private int mCode;
        private String mOrigMsg;

        public static final class CODE {
            public static final int CLIENT_PROTOCOL_ERROR = 4;
            public static final int ILLEGAL_PARAM = 9;
            public static final int INVALID_URL = 7;
            public static final int NO_PEER_CERTIFICATE = 6;
            public static final int NO_PEER_CERTIFICATE_UNCORRECT_DATE = 8;
            public static final int REQUEST_METHOD_NOT_ALLOWED = 5;
            public static final int SERVER_ERROR = 2;
            public static final int SERVER_READ_ERROR = 3;
            public static final int UNSUPPORTED_PARAM_ENCODING = 1;
        }

        public FetchUrlException(int i, String str) {
            super("Error Code: " + i);
            this.mCode = i;
            this.mOrigMsg = str;
        }

        public int getCode() {
            return this.mCode;
        }

        public String getMsg() {
            switch (this.mCode) {
                case 1:
                    return "Unsupported parameter encoding: " + this.mOrigMsg;
                case 2:
                    return "Server error: " + this.mOrigMsg;
                case 3:
                    return "Server read error: " + this.mOrigMsg;
                case 4:
                    return "Client protocol error: " + this.mOrigMsg;
                case 5:
                    return "Request method not allowed: " + this.mOrigMsg;
                case 6:
                case 8:
                    return "No peer certificate: " + this.mOrigMsg;
                case 7:
                default:
                    return "Unknown error: " + this.mOrigMsg;
                case 9:
                    return "Illegal paramters" + this.mOrigMsg;
            }
        }
    }

    public static FetchUrlResponse fetchUrl(int i, String str, HashMap<String, String> map, ArrayList<NameValuePair> arrayList, int i2, int i3) throws FetchUrlException {
        return getStackInstance().fetchUrl(i, str, map, arrayList, i2, i3);
    }

    public static long getNetworkTime() throws IOException {
        try {
            URLConnection uRLConnectionOpenConnection = new URL(GET_NETWORK_TIME_URL).openConnection();
            uRLConnectionOpenConnection.connect();
            return uRLConnectionOpenConnection.getDate();
        } catch (Exception unused) {
            return 0L;
        }
    }

    private static FetchUrlStack getStackInstance() {
        if (isImpHttpURLConnectionClientAvailable()) {
            return new FetchUrlImpHurlStack();
        }
        return new FetchUrlImpHttpClientStack();
    }

    private static boolean isImpHttpURLConnectionClientAvailable() {
        return Build.VERSION.SDK_INT >= 9;
    }

    public static boolean supportSNI() {
        return isImpHttpURLConnectionClientAvailable();
    }
}