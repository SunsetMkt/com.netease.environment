package com.netease.pharos.network;

import android.net.SSLCertificateSocketFactory;
import com.netease.pharos.util.LogUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: classes5.dex */
public class TlsSniSocketFactory extends SSLSocketFactory {
    private final String TAG = "TlsSniSocketFactory";
    private final HttpsURLConnection mConn;

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i) throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return null;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    public TlsSniSocketFactory(HttpsURLConnection httpsURLConnection) {
        this.mConn = httpsURLConnection;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        String requestProperty = this.mConn.getRequestProperty("Host");
        if (requestProperty != null) {
            str = requestProperty;
        }
        if (z) {
            socket.close();
        }
        SSLCertificateSocketFactory sSLCertificateSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(3000);
        LogUtil.i(this.TAG, "TlsSniSocketFactory [createSocket] host = " + str + ", port = " + i);
        SSLSocket sSLSocket = (SSLSocket) sSLCertificateSocketFactory.createSocket(str, i);
        sSLSocket.setEnabledProtocols(sSLSocket.getSupportedProtocols());
        LogUtil.i(this.TAG, "TlsSniSocketFactory [createSocket] Setting SNI hostname");
        sSLCertificateSocketFactory.setHostname(sSLSocket, str);
        SSLSession session = sSLSocket.getSession();
        if (!this.mConn.getHostnameVerifier().verify(str, session)) {
            throw new SSLPeerUnverifiedException("Cannot verify hostname: " + str);
        }
        LogUtil.i(this.TAG, "TlsSniSocketFactory [createSocket] Established " + session.getProtocol() + " connection with " + session.getPeerHost() + " using " + session.getCipherSuite());
        return sSLSocket;
    }
}