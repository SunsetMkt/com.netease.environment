package com.netease.environment.OIIO0IO;

import android.content.Context;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.OIIO0OO.OIIO0OI;
import com.netease.environment.regex.Matcher;
import com.netease.environment.regex.Pattern;
import java.util.Map;
import java.util.concurrent.Callable;

/* compiled from: ReviewNicknameCallable.java */
/* loaded from: classes5.dex */
public class OIIO0OO implements Callable<String> {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private final String f1564OIIO00I = "OIIO0OO";
    private Context OIIO0O0;
    private String OIIO0OO;

    public OIIO0OO(Context context, String str) {
        this.OIIO0O0 = context;
        this.OIIO0OO = str;
    }

    @Override // java.util.concurrent.Callable
    public String call() throws Exception {
        String key;
        Matcher matcher;
        String str = this.OIIO0OO;
        if (str == null || str.isEmpty()) {
            return OIIO0II.OIIO00I(100, "param is null or empty", "-1", "N_7");
        }
        try {
            OIIO.OIIO0O0(this.f1564OIIO00I, "fast mode");
            for (Map.Entry<String, Pattern> entry : com.netease.environment.OIIO0I.OIIO0OO.OIIO0O0(this.OIIO0O0).entrySet()) {
                String str2 = "";
                try {
                    key = entry.getKey();
                    try {
                        matcher = entry.getValue().matcher(this.OIIO0OO);
                    } catch (Exception e) {
                        e = e;
                        str2 = key;
                        OIIO.OIIO00I(this.f1564OIIO00I, "exception when run in nickName, pattern key: " + str2 + " content: " + this.OIIO0OO + " and exception:" + e);
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                if (OIIO0OI.OIIOOO() ? matcher != null : matcher.find()) {
                    return OIIO0II.OIIO00I(202, "shield", key, "N_8", matcher.group());
                }
                if (Thread.interrupted()) {
                    return OIIO0II.OIIO00I(100, "time out", "-1", "N_9");
                }
            }
            return OIIO0II.OIIO00I(200, "pass", "-1", "N_10");
        } catch (Exception e3) {
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(e3, "fast");
            OIIO.OIIO00I(this.f1564OIIO00I, "exception when run in fast mode");
            throw e3;
        }
    }
}