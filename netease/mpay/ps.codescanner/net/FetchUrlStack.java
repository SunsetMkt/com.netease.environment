package com.netease.mpay.ps.codescanner.net;

import com.netease.mpay.ps.codescanner.net.FetchUrl;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/* loaded from: classes5.dex */
public abstract class FetchUrlStack {
    protected static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    protected static final String GET_STR = "GET";
    protected static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    protected static final String POST_STR = "POST";

    protected abstract FetchUrl.FetchUrlResponse fetchUrlRaw(int i, String str, HashMap<String, String> map, byte[] bArr, int i2, int i3) throws FetchUrl.FetchUrlException;

    public FetchUrl.FetchUrlResponse fetchUrl(int i, String str, HashMap<String, String> map, ArrayList<NameValuePair> arrayList, int i2, int i3) throws FetchUrl.FetchUrlException {
        byte[] bArrEncodeParameters = null;
        if (i == 1) {
            if (arrayList != null && arrayList.size() > 0) {
                try {
                    bArrEncodeParameters = encodeParameters(arrayList, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new FetchUrl.FetchUrlException(1, e.getMessage());
                }
            }
        } else if (i == 0) {
            if (arrayList != null && arrayList.size() > 0) {
                if (str.contains("?")) {
                    throw new FetchUrl.FetchUrlException(4, "data and query string are exclusive in GET method");
                }
                str = str + "?" + Utils.encodeQs(arrayList);
            }
        } else {
            throw new FetchUrl.FetchUrlException(5, "" + i + " is not a valid request method");
        }
        return fetchUrlRaw(i, str, map, bArrEncodeParameters, i2, i3);
    }

    protected FetchUrl.FetchUrlException handleSSLException(Exception exc) {
        return new FetchUrl.FetchUrlException(isLocalDateCorrect() ? 6 : 8, exc.getMessage());
    }

    private boolean isLocalDateCorrect() {
        try {
            return Math.abs(FetchUrl.getNetworkTime() - Calendar.getInstance().getTimeInMillis()) < 21600000;
        } catch (Exception unused) {
            return true;
        }
    }

    private byte[] encodeParameters(ArrayList<NameValuePair> arrayList, String str) throws UnsupportedEncodingException {
        return Utils.encodeQs(arrayList, str).getBytes(str);
    }
}