package com.netease.mpay.ps.codescanner.auth;

import android.content.Context;
import com.netease.mpay.auth.AuthUtils;

/* loaded from: classes5.dex */
public class Utils {
    public static final String PROTOCOL_LAUNCHER = "com.netease.ntunisdk.external.protocol.ProtocolLauncher";

    public static boolean isSupportProtocol() {
        return AuthUtils.isClassInstalled("com.netease.ntunisdk.external.protocol.UniSDKProxy") && AuthUtils.isClassInstalled("com.netease.ntunisdk.external.protocol.ProtocolManager");
    }

    public static boolean isProtocolLauncher(Context context) {
        return "com.netease.ntunisdk.external.protocol.ProtocolLauncher".equals(AuthUtils.getLauncherName(context));
    }
}