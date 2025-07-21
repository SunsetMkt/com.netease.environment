package com.netease.cloud.nos.android.ssl;

import android.content.Context;
import com.netease.cloud.nos.android.utils.LogUtil;
import java.io.InputStream;
import java.security.KeyStore;
import org.apache.http.conn.ssl.SSLSocketFactory;

/* loaded from: classes6.dex */
public class SSLCustomSocketFactory extends SSLSocketFactory {
    private static final String KEY_PASS = "";
    private static final String LOGTAG = LogUtil.makeLogTag(SSLCustomSocketFactory.class);

    public SSLCustomSocketFactory(KeyStore keyStore) throws Throwable {
        super(keyStore);
    }

    public static SSLSocketFactory getSocketFactory(Context context) {
        try {
            InputStream inputStreamOpenRawResource = context.getResources().openRawResource(0);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try {
                keyStore.load(inputStreamOpenRawResource, "".toCharArray());
                inputStreamOpenRawResource.close();
                return new SSLCustomSocketFactory(keyStore);
            } catch (Throwable th) {
                inputStreamOpenRawResource.close();
                throw th;
            }
        } catch (Throwable th2) {
            LogUtil.d(LOGTAG, "ssl socket factory exception", th2);
            return null;
        }
    }
}