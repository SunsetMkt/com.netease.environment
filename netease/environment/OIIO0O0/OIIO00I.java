package com.netease.environment.OIIO0O0;

import android.content.Context;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.regex.Matcher;
import com.netease.environment.regex.Pattern;
import java.util.Map;

/* compiled from: InterceptAction.java */
/* loaded from: classes5.dex */
public class OIIO00I {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1565OIIO00I = "OIIO00I";

    public static String OIIO00I(Context context, String str) {
        String key;
        Matcher matcher;
        try {
            OIIO.OIIO0O0(f1565OIIO00I, "intercept words fast mode");
            if (Thread.interrupted()) {
                return OIIO0II.OIIO00I(100, "time out", "-1", "I_5");
            }
            for (Map.Entry<String, Pattern> entry : com.netease.environment.OIIO0I.OIIO0OO.OIIO00I(context).entrySet()) {
                String str2 = "";
                try {
                    key = entry.getKey();
                    try {
                        matcher = entry.getValue().matcher(str);
                    } catch (Exception e) {
                        e = e;
                        str2 = key;
                        OIIO.OIIO00I(f1565OIIO00I, "exception when run in intercept words, pattern key: " + str2 + " content: " + str + " and exception:" + e);
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                if (com.netease.environment.OIIO0OO.OIIO0OI.OIIOOO() ? matcher != null : matcher.find()) {
                    return OIIO0II.OIIO00I(201, "intercept", key, "I_1", matcher.group());
                }
                if (Thread.interrupted()) {
                    return OIIO0II.OIIO00I(100, "time out", "-1", "I_2");
                }
            }
            return OIIO0II.OIIO00I(200, "pass", "-1", "I_3");
        } catch (Exception e3) {
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(e3, "fast");
            String str3 = f1565OIIO00I;
            OIIO.OIIO00I(str3, "exception when run in intercept words fast mode");
            OIIO.OIIO0O0(str3, e3.toString());
            return OIIO0II.OIIO00I(e3, "I_4");
        }
    }
}