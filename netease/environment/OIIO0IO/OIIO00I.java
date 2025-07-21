package com.netease.environment.OIIO0IO;

import android.content.Context;
import android.os.AsyncTask;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0IO;
import com.netease.environment.OIIO0OO.OIIO0OI;
import org.jose4j.jwk.Use;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: InitialTask.java */
/* loaded from: classes5.dex */
public class OIIO00I extends AsyncTask<Void, Void, String> {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private final String f1562OIIO00I = "OIIO00I";
    private Context OIIO0O0;

    public OIIO00I(Context context) {
        this.OIIO0O0 = context;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: OIIO00I, reason: merged with bridge method [inline-methods] */
    public String doInBackground(Void... voidArr) throws JSONException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String strOIIO00I = com.netease.environment.OIIO0OO.OIIO0OO.OIIO00I();
        OIIO.OIIO0O0(this.f1562OIIO00I, "init url:" + strOIIO00I);
        String strOIIO0OO = com.netease.environment.OIIO0OO.OIIO00I.OIIO0OO(this.OIIO0O0);
        if (strOIIO0OO != null && !strOIIO0OO.isEmpty()) {
            OIIO.OIIO0O0(this.f1562OIIO00I, "before encode init param:" + strOIIO0OO);
            strOIIO0OO = com.netease.environment.OIIO0II.OIIO0O0.OIIO0O0(strOIIO0OO.getBytes());
        }
        if (strOIIO0OO != null && strOIIO0OO.length() > 102400) {
            com.netease.environment.OIIO0OO.OIIO00I.OIIO0IO(this.OIIO0O0);
            strOIIO0OO = "";
        }
        OIIO.OIIO0O0(this.f1562OIIO00I, "after encode init param:" + strOIIO0OO);
        String strOIIO00I2 = com.netease.environment.OIIO0OI.OIIO0O0.OIIO00I(strOIIO00I, strOIIO0OO);
        OIIO.OIIO0O0(this.f1562OIIO00I, "init result:" + strOIIO00I2);
        try {
            JSONObject jSONObject = new JSONObject(strOIIO00I2);
            String strOptString = jSONObject.optString("url_one_z_a");
            OIIO.OIIO0O0(this.f1562OIIO00I, "the regex url is " + strOptString);
            if (OIIO0IO.OIIO00I(strOptString)) {
                if (strOptString.startsWith("http://")) {
                    strOptString = strOptString.replaceFirst("http://", "https://");
                }
                String strOIIO00I3 = com.netease.environment.OIIO0OO.OIIO0O0.OIIO00I(this.OIIO0O0, OIIO0OI.OIIO0I(), (String) null);
                OIIO.OIIO0O0(this.f1562OIIO00I, "the native regex url is " + strOIIO00I3);
                if (strOptString.equals(strOIIO00I3)) {
                    OIIO.OIIO0O0(this.f1562OIIO00I, "the regex file is latest");
                } else {
                    com.netease.environment.OIIO0OI.OIIO00I.OIIO00I(this.OIIO0O0, strOptString);
                    OIIO.OIIO0O0(this.f1562OIIO00I, "the regex file is out of date");
                }
            }
            OIIO0OI.OIIO0IO(jSONObject.optString("rstr", "***"));
            OIIO0OI.OIIO0I0(jSONObject.optBoolean("t2s", true));
            OIIO0OI.OIIO0I(jSONObject.optBoolean("f2h", true));
            OIIO0OI.OIIO0O0(jSONObject.optBoolean("un", true));
            OIIO0OI.OIIO0OI(jSONObject.optBoolean(Use.SIGNATURE, true));
            OIIO0OI.OIIO0IO(jSONObject.optBoolean("lu", false));
            OIIO0OI.OIIO0OO(jSONObject.optString("drpf", "drpf-dep87.proxima.nie.netease.com"));
        } catch (Exception unused) {
            OIIO.OIIO0O0(this.f1562OIIO00I, "fail to parse init result:" + strOIIO00I2);
        }
        com.netease.environment.OIIO0OO.OIIO00I.OIIO0OO();
        com.netease.environment.OIIO0OO.OIIO00I.OIIO0IO(this.OIIO0O0);
        long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
        OIIO.OIIO0O0(this.f1562OIIO00I, "get regex url cost time: " + jCurrentTimeMillis2 + "ms");
        com.netease.environment.OIIO0OO.OIIO00I.OIIO00I("getUrl", jCurrentTimeMillis2);
        return null;
    }
}