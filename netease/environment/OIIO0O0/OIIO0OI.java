package com.netease.environment.OIIO0O0;

import android.content.Context;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.regex.Matcher;
import com.netease.environment.regex.Pattern;
import java.util.Map;

/* compiled from: ShieldAction.java */
/* loaded from: classes5.dex */
public class OIIO0OI {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1567OIIO00I = "OIIO0OI";

    public static String OIIO00I(Context context, String str) {
        String key;
        Matcher matcher;
        try {
            OIIO.OIIO0O0(f1567OIIO00I, "shield words fast mode");
            if (Thread.interrupted()) {
                return OIIO0II.OIIO00I(100, "time out", "-1", "S_5");
            }
            for (Map.Entry<String, Pattern> entry : com.netease.environment.OIIO0I.OIIO0OO.OIIO0I(context).entrySet()) {
                String str2 = "";
                try {
                    key = entry.getKey();
                    try {
                        matcher = entry.getValue().matcher(str);
                    } catch (Exception e) {
                        e = e;
                        str2 = key;
                        OIIO.OIIO00I(f1567OIIO00I, "exception when run in shield words, pattern key: " + str2 + " content: " + str + " and exception:" + e);
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                if (com.netease.environment.OIIO0OO.OIIO0OI.OIIOOO() ? matcher != null : matcher.find()) {
                    return OIIO0II.OIIO00I(202, "shield", key, "S_1", matcher.group());
                }
                if (Thread.interrupted()) {
                    return OIIO0II.OIIO00I(100, "time out", "-1", "S_2");
                }
            }
            return OIIO0II.OIIO00I(200, "pass", "-1", "S_3");
        } catch (Exception e3) {
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(e3, "fast");
            String str3 = f1567OIIO00I;
            OIIO.OIIO00I(str3, "exception when run in shield words fast mode");
            OIIO.OIIO0O0(str3, e3.toString());
            return OIIO0II.OIIO00I(e3, "S_4");
        }
    }
}