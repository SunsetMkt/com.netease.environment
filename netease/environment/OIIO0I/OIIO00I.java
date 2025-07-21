package com.netease.environment.OIIO0I;

import android.text.TextUtils;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIOO0O;
import com.netease.environment.OIIO0OO.OIIO0OI;

/* compiled from: ContentFormatter.java */
/* loaded from: classes4.dex */
public class OIIO00I {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1555OIIO00I = "OIIO00I";

    public static String OIIO00I(String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str2 = f1555OIIO00I;
        OIIO.OIIO0O0(str2, "the content to format is :" + str);
        if (!TextUtils.isEmpty(str)) {
            try {
                if (OIIO0OI.OIIO()) {
                    str = OIIOO0O.OIIO0O0(str);
                    OIIO.OIIO0O0(str2, "after normalize:" + str);
                }
                if (OIIO0OI.OIIOO0I()) {
                    str = com.netease.environment.OIIO0II.OIIO0OO.OIIO0O0(str);
                    OIIO.OIIO0O0(str2, "after simplify:" + str);
                }
                if (OIIO0OI.OIIOOO0()) {
                    str = com.netease.environment.OIIO0II.OIIO0OO.OIIO0OO(str);
                    OIIO.OIIO0O0(str2, "after toDBC:" + str);
                }
                str = com.netease.environment.OIIO0II.OIIO0OO.OIIO00I(str);
                OIIO.OIIO0O0(str2, "after enc:" + str);
            } catch (Exception unused) {
                OIIO.OIIO00I(f1555OIIO00I, "fail to format content");
            }
        }
        String str3 = f1555OIIO00I;
        OIIO.OIIO0O0(str3, "the content after format is :" + str);
        OIIO.OIIO0O0(str3, "it costs " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms to format data");
        return str;
    }
}