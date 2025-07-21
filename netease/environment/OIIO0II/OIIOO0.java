package com.netease.environment.OIIO0II;

import android.content.Context;
import com.facebook.hermes.intl.Constants;
import yangchaoyue.yangchaoyue.yangchaoyue.yangchaoyue.yangchaoyue.p006do.b;

/* compiled from: NetWorkUtils.java */
/* loaded from: classes5.dex */
public class OIIOO0 {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004f A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int OIIO00I(android.content.Context r2) {
        /*
            java.lang.String r0 = "connectivity"
            java.lang.Object r2 = r2.getSystemService(r0)
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2
            android.net.NetworkInfo r2 = r2.getActiveNetworkInfo()
            if (r2 == 0) goto L53
            boolean r0 = r2.isConnected()
            if (r0 == 0) goto L53
            int r0 = r2.getType()
            r1 = 1
            if (r0 != r1) goto L1d
            r2 = 0
            goto L54
        L1d:
            int r0 = r2.getType()
            if (r0 != 0) goto L53
            java.lang.String r0 = r2.getSubtypeName()
            int r2 = r2.getSubtype()
            r1 = 20
            if (r2 == r1) goto L51
            switch(r2) {
                case 1: goto L4d;
                case 2: goto L4d;
                case 3: goto L4f;
                case 4: goto L4d;
                case 5: goto L4f;
                case 6: goto L4f;
                case 7: goto L4d;
                case 8: goto L4f;
                case 9: goto L4f;
                case 10: goto L4f;
                case 11: goto L4d;
                case 12: goto L4f;
                case 13: goto L4b;
                case 14: goto L4f;
                case 15: goto L4f;
                default: goto L32;
            }
        L32:
            java.lang.String r2 = "TD-SCDMA"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 != 0) goto L4f
            java.lang.String r2 = "WCDMA"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 != 0) goto L4f
            java.lang.String r2 = "CDMA2000"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L53
            goto L4f
        L4b:
            r2 = 4
            goto L54
        L4d:
            r2 = 2
            goto L54
        L4f:
            r2 = 3
            goto L54
        L51:
            r2 = 5
            goto L54
        L53:
            r2 = -1
        L54:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.environment.OIIO0II.OIIOO0.OIIO00I(android.content.Context):int");
    }

    public static String OIIO0O0(Context context) {
        int iOIIO00I = OIIO00I(context);
        return iOIIO00I != -1 ? iOIIO00I != 0 ? iOIIO00I != 2 ? iOIIO00I != 3 ? iOIIO00I != 4 ? iOIIO00I != 5 ? Constants.COLLATION_INVALID : "5G" : b.b : b.c : b.d : "wifi" : Constants.COLLATION_INVALID;
    }
}