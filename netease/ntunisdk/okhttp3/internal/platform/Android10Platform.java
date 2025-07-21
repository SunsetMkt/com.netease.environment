package com.netease.ntunisdk.okhttp3.internal.platform;

import android.net.ssl.SSLSockets;
import com.netease.ntunisdk.okhttp3.Protocol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;

/* loaded from: classes5.dex */
class Android10Platform extends AndroidPlatform {
    Android10Platform(Class<?> cls) {
        super(cls, null, null, null, null);
    }

    @Nullable
    public static Platform buildIfSupported() {
        System.out.println("wuln--okhttp Android10Platform [buildIfSupported] start");
        if (!Platform.isAndroid()) {
            return null;
        }
        try {
            if (getSdkInt() >= 29) {
                return new Android10Platform(Class.forName("com.android.org.conscrypt.SSLParametersImpl"));
            }
        } catch (ClassNotFoundException unused) {
        }
        return null;
    }

    private void enableSessionTickets(SSLSocket sSLSocket) {
        if (SSLSockets.isSupportedSocket(sSLSocket)) {
            SSLSockets.setUseSessionTickets(sSLSocket, true);
        }
    }

    @Override // com.netease.ntunisdk.okhttp3.internal.platform.AndroidPlatform, com.netease.ntunisdk.okhttp3.internal.platform.Platform
    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) throws IOException {
        System.out.println("wuln--okhttp Android10Platform [configureTlsExtensions] start");
        try {
            enableSessionTickets(sSLSocket);
            SSLParameters sSLParameters = sSLSocket.getSSLParameters();
            if (sSLParameters.getServerNames() != null) {
                System.out.println("wuln--okhttp Android10Platform [configureTlsExtensions] sslParameters22=" + sSLParameters.getServerNames().toString());
            } else {
                System.out.println("wuln--okhttp Android10Platform [configureTlsExtensions] sslParameters33");
            }
            System.out.println("wuln--okhttp Android10Platform [configureTlsExtensions] hostname=" + str);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new SNIHostName(str));
            sSLParameters.setServerNames(arrayList);
            if (sSLParameters.getServerNames() != null) {
                System.out.println("wuln--okhttp Android10Platform [configureTlsExtensions] sslParameters22=" + sSLParameters.getServerNames().toString());
            } else {
                System.out.println("wuln--okhttp Android10Platform [configureTlsExtensions] sslParameters33");
            }
            sSLParameters.setApplicationProtocols((String[]) Platform.alpnProtocolNames(list).toArray(new String[0]));
            sSLSocket.setSSLParameters(sSLParameters);
        } catch (IllegalArgumentException e) {
            throw new IOException("Android internal error", e);
        }
    }

    @Override // com.netease.ntunisdk.okhttp3.internal.platform.AndroidPlatform, com.netease.ntunisdk.okhttp3.internal.platform.Platform
    @Nullable
    public String getSelectedProtocol(SSLSocket sSLSocket) {
        System.out.println("wuln--okhttp Android10Platform [getSelectedProtocol] start");
        String applicationProtocol = sSLSocket.getApplicationProtocol();
        if (applicationProtocol == null || applicationProtocol.isEmpty()) {
            return null;
        }
        return applicationProtocol;
    }
}