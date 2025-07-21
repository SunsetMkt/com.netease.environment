package com.netease.environment.OIIO0IO;

import android.content.Context;
import android.text.TextUtils;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.OIIO0O0.OIIO0OI;
import com.netease.environment.regex.Pattern;
import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ReviewCallable.java */
/* loaded from: classes5.dex */
public class OIIO0O0 implements Callable<String> {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private final String f1563OIIO00I = "OIIO0O0";
    private String OIIO0I0;
    private Context OIIO0O0;
    private String OIIO0OI;
    private String OIIO0OO;

    public OIIO0O0(Context context, String str, String str2, String str3) {
        this.OIIO0O0 = context;
        this.OIIO0OO = str;
        this.OIIO0OI = str2;
        this.OIIO0I0 = str3;
    }

    public boolean OIIO00I(String str, int i) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            int iOptInt = jSONObject.optInt("code");
            OIIO.OIIO0O0(this.f1563OIIO00I, "check break code : " + iOptInt);
            if (iOptInt == i) {
                return true;
            }
            return TextUtils.equals(jSONObject.optString("message"), "time out");
        } catch (Exception unused) {
            OIIO.OIIO0O0(this.f1563OIIO00I, "fail to parse the result returned by check break");
            return false;
        }
    }

    public boolean OIIO0O0(String str, int i) {
        try {
            return new JSONObject(str).optInt("code") == i;
        } catch (Exception unused) {
            OIIO.OIIO0O0(this.f1563OIIO00I, "fail to parse the result returned by check code");
            return false;
        }
    }

    @Override // java.util.concurrent.Callable
    public String call() {
        String strOIIO00I = com.netease.environment.OIIO0I.OIIO00I.OIIO00I(this.OIIO0OO);
        String str = this.OIIO0OO;
        if (str == null || str.isEmpty() || strOIIO00I == null || strOIIO00I.isEmpty()) {
            return OIIO0II.OIIO00I(100, "param is null or empty", "-1", "V_7");
        }
        String str2 = "level=" + this.OIIO0OI + "_channel=" + this.OIIO0I0 + "_content=" + strOIIO00I;
        String strOIIO00I2 = OIIO0OI.OIIO00I(this.OIIO0O0, str2);
        if (OIIO00I(strOIIO00I2, 202)) {
            return strOIIO00I2;
        }
        Map<String, Pattern> mapOIIO0I0 = com.netease.environment.OIIO0I.OIIO0OO.OIIO0I0(this.OIIO0O0);
        if (mapOIIO0I0 != null && mapOIIO0I0.size() > 0) {
            strOIIO00I2 = com.netease.environment.OIIO0O0.OIIO0OO.OIIO00I(this.OIIO0O0, this.OIIO0OO, strOIIO00I);
            if (OIIO00I(strOIIO00I2, 202)) {
                return strOIIO00I2;
            }
        }
        Map<String, Pattern> mapOIIO00I = com.netease.environment.OIIO0I.OIIO0OO.OIIO00I(this.OIIO0O0);
        if (mapOIIO00I != null && mapOIIO00I.size() > 0) {
            String strOIIO00I3 = com.netease.environment.OIIO0O0.OIIO00I.OIIO00I(this.OIIO0O0, str2);
            boolean zOIIO0O0 = OIIO0O0(strOIIO00I3, 201);
            boolean zOIIO0O02 = OIIO0O0(strOIIO00I2, 206);
            if (zOIIO0O02 && zOIIO0O0) {
                return OIIO00I(strOIIO00I2);
            }
            if (zOIIO0O02) {
                return strOIIO00I2;
            }
            if (OIIO00I(strOIIO00I3, 201)) {
                return strOIIO00I3;
            }
            strOIIO00I2 = strOIIO00I3;
        } else if (OIIO00I(strOIIO00I2, 206)) {
            return strOIIO00I2;
        }
        Map<String, Pattern> mapOIIO0OI = com.netease.environment.OIIO0I.OIIO0OO.OIIO0OI(this.OIIO0O0);
        return (mapOIIO0OI == null || mapOIIO0OI.size() == 0) ? strOIIO00I2 : com.netease.environment.OIIO0O0.OIIO0O0.OIIO00I(this.OIIO0O0, str2);
    }

    public String OIIO00I(String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("message");
            jSONObject.put("message", com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0O());
            OIIO.OIIO0O0(this.f1563OIIO00I, "replace message from: " + strOptString);
            OIIO.OIIO0O0(this.f1563OIIO00I, "replace message to: " + com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0O());
            return jSONObject.toString();
        } catch (Exception unused) {
            OIIO.OIIO0O0(this.f1563OIIO00I, "fail to parse the result returned by replace message");
            return str;
        }
    }
}