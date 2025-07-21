package com.netease.cloud.nos.android.utils;

import android.content.Context;
import com.JavaWebsocket.WebSocket;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.ssl.SSLTrustAllSocketFactory;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

/* loaded from: classes5.dex */
public class Http {
    private static HttpClient httpClient;
    private static HttpClient lbsHttpClient;

    private static HttpClient buildHttpClient(Context context, int i, int i2) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 10);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(3));
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLTrustAllSocketFactory.getSocketFactory(), WebSocket.DEFAULT_WSS_PORT));
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(i2));
        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(i));
        return defaultHttpClient;
    }

    public static HttpClient getHttpClient(Context context) {
        HttpClient httpClient2 = WanAccelerator.getConf().getHttpClient();
        if (httpClient2 != null) {
            return httpClient2;
        }
        if (httpClient == null) {
            httpClient = buildHttpClient(context, WanAccelerator.getConf().getConnectionTimeout(), WanAccelerator.getConf().getSoTimeout());
        }
        return httpClient;
    }

    public static HttpClient getLbsHttpClient(Context context) {
        HttpClient httpClient2 = WanAccelerator.getConf().getHttpClient();
        if (httpClient2 != null) {
            return httpClient2;
        }
        if (lbsHttpClient == null) {
            lbsHttpClient = buildHttpClient(context, WanAccelerator.getConf().getLbsConnectionTimeout(), WanAccelerator.getConf().getLbsSoTimeout());
        }
        return lbsHttpClient;
    }
}