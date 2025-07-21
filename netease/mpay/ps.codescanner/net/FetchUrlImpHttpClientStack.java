package com.netease.mpay.ps.codescanner.net;

import com.netease.mpay.ps.codescanner.net.FetchUrl;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLException;

/* loaded from: classes5.dex */
public class FetchUrlImpHttpClientStack extends FetchUrlStack {
    @Override // com.netease.mpay.ps.codescanner.net.FetchUrlStack
    protected FetchUrl.FetchUrlResponse fetchUrlRaw(int i, String str, HashMap<String, String> map, byte[] bArr, int i2, int i3) throws FetchUrl.FetchUrlException, IOException {
        InputStream errorStream;
        try {
            try {
                HttpURLConnection httpURLConnectionOpenConnection = openConnection(str, i2, i3);
                if (i == 0) {
                    httpURLConnectionOpenConnection.setRequestMethod("GET");
                } else if (1 == i) {
                    httpURLConnectionOpenConnection.setRequestMethod("POST");
                }
                if (map != null && map.size() > 0) {
                    for (String str2 : map.keySet()) {
                        httpURLConnectionOpenConnection.addRequestProperty(str2, map.get(str2));
                    }
                }
                if (bArr != null) {
                    httpURLConnectionOpenConnection.setDoOutput(true);
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnectionOpenConnection.getOutputStream());
                    dataOutputStream.write(bArr);
                    dataOutputStream.close();
                }
                FetchUrl.FetchUrlResponse fetchUrlResponse = new FetchUrl.FetchUrlResponse();
                fetchUrlResponse.code = httpURLConnectionOpenConnection.getResponseCode();
                if (fetchUrlResponse.code == -1) {
                    throw new IOException("Could not retrieve response code from HttpUrlConnection.");
                }
                try {
                    errorStream = httpURLConnectionOpenConnection.getInputStream();
                } catch (IOException unused) {
                    errorStream = httpURLConnectionOpenConnection.getErrorStream();
                }
                if (errorStream != null) {
                    fetchUrlResponse.content = inputStreamToByteArray(errorStream);
                }
                fetchUrlResponse.headers = new HashMap<>();
                for (Map.Entry<String, List<String>> entry : httpURLConnectionOpenConnection.getHeaderFields().entrySet()) {
                    fetchUrlResponse.headers.put(entry.getKey(), httpURLConnectionOpenConnection.getHeaderField(entry.getKey()));
                }
                return fetchUrlResponse;
            } catch (UnsupportedEncodingException e) {
                throw new FetchUrl.FetchUrlException(1, e.getMessage());
            } catch (IllegalAccessError e2) {
                throw new FetchUrl.FetchUrlException(3, e2.getMessage());
            } catch (IllegalStateException e3) {
                throw new FetchUrl.FetchUrlException(2, e3.getMessage());
            } catch (NullPointerException e4) {
                throw new FetchUrl.FetchUrlException(9, e4.getMessage());
            } catch (ProtocolException e5) {
                throw new FetchUrl.FetchUrlException(4, e5.getMessage());
            } catch (SSLException e6) {
                throw handleSSLException(e6);
            }
        } catch (IOException e7) {
            throw new FetchUrl.FetchUrlException(3, e7.getMessage());
        }
    }

    private HttpURLConnection openConnection(String str, int i, int i2) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        httpURLConnection.setConnectTimeout(i);
        httpURLConnection.setReadTimeout(i2);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
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