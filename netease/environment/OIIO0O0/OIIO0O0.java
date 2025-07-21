package com.netease.environment.OIIO0O0;

import android.content.Context;
import android.text.TextUtils;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.regex.Matcher;
import com.netease.environment.regex.Pattern;
import java.util.Map;

/* compiled from: RemindAction.java */
/* loaded from: classes5.dex */
public class OIIO0O0 {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1566OIIO00I = "OIIO0O0";

    public static String OIIO00I(Context context, String str) throws NumberFormatException {
        String key;
        Pattern value;
        Matcher matcher;
        String comment;
        try {
            OIIO.OIIO0O0(f1566OIIO00I, "remind words fast mode");
            if (Thread.interrupted()) {
                return OIIO0II.OIIO00I(100, "time out", "-1", "M_5");
            }
            for (Map.Entry<String, Pattern> entry : com.netease.environment.OIIO0I.OIIO0OO.OIIO0OI(context).entrySet()) {
                String str2 = "";
                try {
                    key = entry.getKey();
                    try {
                        value = entry.getValue();
                        matcher = value.matcher(str);
                    } catch (Exception e) {
                        e = e;
                        str2 = key;
                        OIIO.OIIO00I(f1566OIIO00I, "exception when run in intercept words, pattern key: " + str2 + " content: " + str + " and exception:" + e);
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                if (com.netease.environment.OIIO0OO.OIIO0OI.OIIOOO() ? matcher != null : matcher.find()) {
                    try {
                        comment = value.getComment();
                    } catch (Exception e3) {
                        com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(e3, "fast");
                        OIIO.OIIO00I(f1566OIIO00I, "exception when get remind tips");
                    }
                    if (TextUtils.isEmpty(comment)) {
                        comment = "0";
                        return OIIO0II.OIIO00I(207, comment, key, "M_1");
                    }
                    Integer.parseInt(comment);
                    return OIIO0II.OIIO00I(207, comment, key, "M_1");
                }
                if (Thread.interrupted()) {
                    return OIIO0II.OIIO00I(100, "time out", "-1", "M_2");
                }
            }
            return OIIO0II.OIIO00I(200, "pass", "-1", "M_3");
        } catch (Exception e4) {
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(e4, "fast");
            String str3 = f1566OIIO00I;
            OIIO.OIIO00I(str3, "exception when run in remind words fast mode");
            OIIO.OIIO0O0(str3, e4.toString());
            return OIIO0II.OIIO00I(e4, "M_4");
        }
    }
}