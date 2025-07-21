package com.netease.ntunisdk.base;

import android.text.TextUtils;
import com.alipay.sdk.m.s.a;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class SDKEchoes {
    private static SDKEchoes x;

    /* renamed from: a, reason: collision with root package name */
    private String f1617a = null;
    private String b = null;
    private String c = null;
    private String d = null;
    private String e = null;
    private String f = null;
    private String g = null;
    private String h = null;
    private String i = null;
    private String j = null;
    private String k = null;
    private String l = null;
    private String m = null;
    private String n = null;
    private int o = 0;
    private String p = null;
    private String q = null;
    private String r = null;
    private String s = null;
    private String t = null;
    private String u = null;
    private String v = null;
    private int w = 0;

    private SDKEchoes() {
    }

    public static SDKEchoes getInstance() {
        if (x == null) {
            x = new SDKEchoes();
        }
        return x;
    }

    public String getmFeedbackMsg() {
        return this.p;
    }

    public void setmFeedbackMsg(String str) {
        this.p = str;
    }

    public void init() {
        this.u = SdkMgr.getInst().getPropStr(ConstProp.ECHOES_TID, "");
        this.v = SdkMgr.getInst().getPropStr(ConstProp.ECHOES_USR_AVATARID, "");
        this.f1617a = "";
        this.b = SdkMgr.getInst().getChannel();
        this.c = "ad";
        this.d = SdkMgr.getInst().getPropStr(ConstProp.SDC_LOG_OS_VER, "");
        this.e = SdkMgr.getInst().getPropStr(ConstProp.SDC_LOG_APP_VER, "");
        this.g = UniSdkUtils.getMobileModel();
        this.h = SdkMgr.getInst().getPropStr(ConstProp.ECHOES_SERVERLIST, "");
        this.i = getTimeZone();
        this.j = getAreaZone();
        this.k = SdkMgr.getInst().getPropStr(ConstProp.UDID, "");
        this.l = SdkMgr.getInst().getPropStr(ConstProp.USERINFO_UID, "");
        this.m = SdkMgr.getInst().getPropStr(ConstProp.USERINFO_NAME, "");
        this.n = SdkMgr.getInst().getPropStr(ConstProp.USERINFO_HOSTNAME, "");
        this.o = SdkMgr.getInst().getPropInt(ConstProp.ECHOES_USR_STATUS, -1);
        this.q = SDKPharos.getInstance().getPharosid();
        this.r = SdkMgr.getDLInst().getOrbitSessionId();
        this.s = SdkMgr.getInst().getPropStr(ConstProp.ECHOES_CUSTOM_LOG, "");
        this.f = SdkMgr.getInst().getPropStr("JF_GAMEID", "");
    }

    public String getTimeZone() {
        String[] strArrSplit;
        int iIntValue;
        String displayName = TimeZone.getDefault().getDisplayName(false, 0);
        UniSdkUtils.i("SDKEchoes", "\u7f51\u7edc\u76d1\u63a7\u6a21\u5757---\u65f6\u5dee=".concat(String.valueOf(displayName)));
        if (displayName == null || !displayName.contains(Marker.ANY_NON_NULL_MARKER) || !displayName.contains(":") || (strArrSplit = displayName.split("\\+|\\:")) == null || strArrSplit.length <= 2) {
            return displayName;
        }
        try {
            iIntValue = Integer.valueOf(strArrSplit[1]).intValue();
        } catch (Exception unused) {
            iIntValue = 100;
        }
        return Marker.ANY_NON_NULL_MARKER.concat(String.valueOf(iIntValue));
    }

    public String getAreaZone() {
        String id = TimeZone.getDefault().getID();
        UniSdkUtils.i("SDKEchoes", "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u5730\u533a=".concat(String.valueOf(id)));
        return id;
    }

    public void ntOpenEchoes() {
        String str;
        init();
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.ECHOES_URL, "");
        UniSdkUtils.i("SDKEchoes", "echo2Flatform url=".concat(String.valueOf(propStr)));
        if (TextUtils.isEmpty(propStr)) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer("tid=");
        stringBuffer.append(this.u).append("&usr_nickname=").append(this.m).append("&usr_id=").append(this.l).append("&usr_server=").append(this.n).append("&usr_avatarid=").append(this.v);
        if (propStr.endsWith("?")) {
            str = propStr + stringBuffer.toString();
        } else if (propStr.contains("?")) {
            str = propStr + a.l + stringBuffer.toString();
        } else {
            str = propStr + "?" + stringBuffer.toString();
        }
        UniSdkUtils.i("SDKEchoes", "echo2Flatform url=".concat(String.valueOf(str)));
        SdkMgr.getInst().setPropStr(ConstProp.WEBVIEW_CLBTN, "1");
        SdkMgr.getInst().ntOpenWebView(str);
    }

    public void echo2SA() {
        if (TextUtils.isEmpty(this.f)) {
            init();
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_ECHOES_URL, ConstProp.ECHOES_DEFAULT_URL);
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.i("SDKEchoes", "null or empty url, echo service will not go on");
            return;
        }
        if (SdkMgr.getInst().hasFeature("EB")) {
            propStr = propStr.replaceAll("netease.com", "easebar.com");
        }
        UniSdkUtils.i("SDKEchoes", "echo2SA url=".concat(String.valueOf(propStr)));
        if (TextUtils.isEmpty(propStr)) {
            return;
        }
        NetUtil.wpost_http_https(propStr, createEhco2SAParams().toString(), new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.SDKEchoes.1
            @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
            public void ProcessResult(String str) {
                UniSdkUtils.i("SDKEchoes", "echo2Flatform [ProcessResult]=" + str + ", mDumpid=" + SDKEchoes.this.t);
            }
        });
    }

    public JSONObject createEhco2SAParams() throws JSONException {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("catagory", this.f1617a);
            jSONObject2.put("channel", this.b);
            jSONObject2.put("os", this.c);
            jSONObject2.put("os_ver", this.d);
            jSONObject2.put("app_ver", this.e);
            jSONObject2.put("mobile_type", this.g);
            jSONObject2.put("server_list", this.h);
            jSONObject2.put("time_zone", this.i);
            jSONObject2.put("area_zone", this.j);
            jSONObject2.put("udid", this.k);
            jSONObject2.put("usr_id", this.l);
            jSONObject2.put("usr_nickname", this.m);
            jSONObject2.put("usr_server", this.n);
            jSONObject2.put("usr_status", this.o);
            jSONObject2.put("feedback_msg", this.p);
            jSONObject2.put("pharos_id", this.q);
            jSONObject2.put("orbit_sessionid", this.r);
            jSONObject2.put("dumpid", getDumpId());
            jSONObject2.put("project", this.f);
            try {
                jSONObject = new JSONObject(this.s);
            } catch (Exception unused) {
                jSONObject = new JSONObject();
            }
            jSONObject2.put("custom_log", jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UniSdkUtils.i("SDKEchoes", "createEhco2SAParams =" + jSONObject2.toString());
        return jSONObject2;
    }

    public String getDumpId() {
        this.w++;
        String transid = UniSdkUtils.getTransid(((SdkBase) SdkMgr.getInst()).myCtx);
        UniSdkUtils.i("SDKEchoes", "echo2Flatform [ProcessResult] transid=".concat(String.valueOf(transid)));
        if (!TextUtils.isEmpty(transid)) {
            this.t = transid + "_" + this.w;
            StringBuilder sb = new StringBuilder("echo2Flatform [ProcessResult] mDumpid=");
            sb.append(this.t);
            UniSdkUtils.i("SDKEchoes", sb.toString());
            SdkMgr.getInst().setPropStr(ConstProp.ECHOES_DUMPID, this.t);
        }
        return this.t;
    }
}