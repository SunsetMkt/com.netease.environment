package com.netease.mpay.ps.codescanner.net;

import com.alipay.sdk.m.s.a;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/* loaded from: classes5.dex */
public class Utils {
    static String encodeQs(ArrayList<NameValuePair> arrayList) {
        try {
            return encodeQs(arrayList, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    static String encodeQs(ArrayList<NameValuePair> arrayList, String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            NameValuePair nameValuePair = arrayList.get(i);
            if (nameValuePair.getValue() != null) {
                if (i != 0) {
                    sb.append(a.l);
                }
                sb.append(URLEncoder.encode(nameValuePair.getName(), str));
                sb.append("=");
                sb.append(URLEncoder.encode(nameValuePair.getValue(), str));
            }
        }
        return sb.toString();
    }

    static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            int i = inputStream.read(bArr, 0, bArr.length);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }
}