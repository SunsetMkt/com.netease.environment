package com.netease.environment;

import android.content.Context;
import com.netease.environment.OIIO0I.OIIO0OO;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0OO.OIIO00I;
import com.netease.environment.OIIO0OO.OIIO0O0;
import com.netease.environment.OIIO0OO.OIIO0OI;
import com.netease.environment.regex.Pattern;
import java.util.Map;

/* loaded from: classes3.dex */
public class B {
    public static void c(int i, Map<String, Pattern> map) {
        OIIO0OO.OIIO00I(i, map);
    }

    public static void d(Context context, long j, long j2, boolean z) {
        OIIO0O0.OIIO0OO(context, z);
        OIIO0O0.OIIO0OI(context, j);
        OIIO0O0.OIIO0O0(context, j2);
        OIIO.OIIO0O0("EnvSdkManager", "SdkConfig set updateIntervalMSecs:" + j + " reviewTimeoutMSecs:" + j2 + " enableState:" + z);
    }

    public static void e(String str, String str2) {
        try {
            OIIO.OIIO0O0("EnvSdkManager", "LogConfig saveCompileFailedLog:" + str + " :" + str2);
            OIIO00I.OIIO00I(str, str2, "400");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean f() {
        return OIIO.OIIO00I();
    }

    public static void t(long j) {
        OIIO00I.OIIO00I("compile_time", j);
        OIIO0OI.OIIO00I(j);
    }
}