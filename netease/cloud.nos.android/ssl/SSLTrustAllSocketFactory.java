package com.netease.cloud.nos.android.ssl;

import com.netease.cloud.nos.android.utils.LogUtil;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

/* loaded from: classes6.dex */
public class SSLTrustAllSocketFactory extends SSLSocketFactory {
    private static final String LOGTAG = LogUtil.makeLogTag(SSLTrustAllSocketFactory.class);
    private SSLContext mCtx;

    public class SSLTrustAllManager implements X509TrustManager {
        public SSLTrustAllManager() {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public SSLTrustAllSocketFactory(KeyStore keyStore) throws Throwable {
        super(keyStore);
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            this.mCtx = sSLContext;
            sSLContext.init(null, new TrustManager[]{new SSLTrustAllManager()}, null);
            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "trust all socket factory exception", e);
        }
    }

    public static SSLSocketFactory getSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            return new SSLTrustAllSocketFactory(keyStore);
        } catch (Throwable th) {
            LogUtil.e(LOGTAG, "get socket factory exception", th);
            return null;
        }
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
    public Socket createSocket() throws IOException {
        return this.mCtx.getSocketFactory().createSocket();
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return this.mCtx.getSocketFactory().createSocket(socket, str, i, z);
    }

    public SSLEngine getSslEngine() {
        return this.mCtx.createSSLEngine();
    }
}