package com.netease.ntunisdk.base.function;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.onetrack.OneTrack;
import java.lang.ref.WeakReference;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PxLogInfo.java */
/* loaded from: classes4.dex */
public final class g {

    /* renamed from: a, reason: collision with root package name */
    private static a f1834a;

    private static JSONObject f() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("udid", SdkMgr.getInst().getUdid());
            jSONObject.putOpt("app_ver", SdkMgr.getInst().getPropStr(ConstProp.SDC_LOG_APP_VER));
            jSONObject.putOpt("caid", SdkMgr.getInst().getPropStr("neid", ""));
            jSONObject.put("appid", SdkMgr.getInst().getPropStr(ConstProp.NOAH_APPID, ""));
            jSONObject.put("login_channel", SdkMgr.getInst().getChannel());
            jSONObject.putOpt("platform", "ad");
            jSONObject.putOpt("sdkuid", SdkMgr.getInst().getPropStr(ConstProp.UID));
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    public static JSONObject a() {
        return f();
    }

    public static JSONObject b() throws JSONException {
        JSONObject jSONObjectF = f();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis() / 1000);
            jSONObjectF.putOpt("active_time", sb.toString());
        } catch (Exception unused) {
        }
        return jSONObjectF;
    }

    public static JSONObject a(Context context) throws JSONException {
        JSONObject jSONObjectF = f();
        try {
            jSONObjectF.putOpt("server", Integer.valueOf(SdkMgr.getInst().getPropInt(ConstProp.USERINFO_HOSTID, 0)));
            jSONObjectF.putOpt("nation", c.a(context));
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.FULL_UID);
            String strReplace = null;
            if (propStr != null) {
                int iIndexOf = propStr.indexOf(".win.");
                if (iIndexOf > 0 && iIndexOf < propStr.length()) {
                    propStr = propStr.substring(0, iIndexOf);
                }
                String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.ORIGIN_GUEST_UID);
                String propStr3 = SdkMgr.getInst().getPropStr(ConstProp.UID);
                if (propStr3 != null && propStr2 != null && propStr.contains(propStr3)) {
                    strReplace = propStr.replace(propStr3, propStr2);
                }
            }
            jSONObjectF.putOpt("account_id", propStr);
            jSONObjectF.putOpt("old_accountid", strReplace);
            jSONObjectF.putOpt("role_id", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_UID));
            jSONObjectF.putOpt("role_name", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_NAME));
            jSONObjectF.putOpt("country_code", c.a());
        } catch (Exception unused) {
        }
        return jSONObjectF;
    }

    public static JSONObject b(Context context) throws JSONException {
        JSONObject jSONObjectA = a(context);
        try {
            int[] displayPixels = UniSdkUtils.getDisplayPixels(context);
            jSONObjectA.putOpt("device_width", Integer.valueOf(displayPixels[0]));
            jSONObjectA.putOpt("device_height", Integer.valueOf(displayPixels[1]));
            jSONObjectA.putOpt("role_level", Integer.valueOf(SdkMgr.getInst().getPropInt(ConstProp.USERINFO_GRADE, 0)));
            jSONObjectA.putOpt(OneTrack.Param.VIP_LEVEL, Integer.valueOf(SdkMgr.getInst().getPropInt(ConstProp.USERINFO_VIP, 0)));
            jSONObjectA.putOpt("sauth_login_type", SdkMgr.getInst().getPropStr(ConstProp.SAUTH_LOGIN_TYPE));
            jSONObjectA.putOpt("aid", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_AID));
            jSONObjectA.putOpt("realname_msg", SdkMgr.getInst().getPropStr(ConstProp.SAUTH_RESPONSE_REALNAME_MSG));
            jSONObjectA.putOpt("aas_msg", SdkMgr.getInst().getPropStr(ConstProp.SAUTH_RESPONSE_AAS_MSG));
        } catch (Exception unused) {
        }
        return jSONObjectA;
    }

    public static JSONObject a(Context context, long j) throws JSONException {
        JSONObject jSONObjectA = a(context);
        try {
            int[] displayPixels = UniSdkUtils.getDisplayPixels(context);
            jSONObjectA.putOpt("device_width", Integer.valueOf(displayPixels[0]));
            jSONObjectA.putOpt("device_height", Integer.valueOf(displayPixels[1]));
            jSONObjectA.putOpt("role_level", Integer.valueOf(SdkMgr.getInst().getPropInt(ConstProp.USERINFO_GRADE, 0)));
            jSONObjectA.putOpt(OneTrack.Param.VIP_LEVEL, Integer.valueOf(SdkMgr.getInst().getPropInt(ConstProp.USERINFO_VIP, 0)));
            jSONObjectA.putOpt("aid", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_AID));
            long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
            jSONObjectA.putOpt("logout_time", Long.valueOf(jCurrentTimeMillis));
            if (0 < j && j < jCurrentTimeMillis) {
                jSONObjectA.putOpt("online_time", Long.valueOf(jCurrentTimeMillis - j));
            }
        } catch (Exception unused) {
        }
        return jSONObjectA;
    }

    public static void a(Context context, SdkBase sdkBase) {
        if (f1834a == null) {
            f1834a = new a(context, sdkBase);
        }
        f1834a.b();
    }

    public static void c() {
        a aVar = f1834a;
        if (aVar != null) {
            aVar.d();
        }
    }

    public static void d() {
        a aVar = f1834a;
        if (aVar != null) {
            aVar.e();
        }
    }

    public static void e() {
        a aVar = f1834a;
        if (aVar != null) {
            aVar.c();
            f1834a.a();
        }
    }

    /* compiled from: PxLogInfo.java */
    private static class a extends Handler {

        /* renamed from: a, reason: collision with root package name */
        private static final int f1835a;
        private static final int b;
        private final WeakReference<Context> c;
        private final WeakReference<SdkBase> d;
        private long e;
        private long f;
        private long g;
        private long h;
        private boolean i;

        static {
            int iAbs = Math.abs(1520147008) & 65535;
            f1835a = iAbs;
            b = Math.abs(iAbs + 1) & 65535;
        }

        public a(Context context, SdkBase sdkBase) {
            super(Looper.getMainLooper());
            this.i = false;
            this.c = new WeakReference<>(context);
            this.d = new WeakReference<>(sdkBase);
        }

        public final void a() {
            removeMessages(f1835a);
            this.e = 0L;
            this.f = 0L;
            this.i = false;
        }

        public final void b() {
            a();
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.g = jCurrentTimeMillis;
            this.h = Constants.ASSEMBLE_PUSH_NETWORK_INTERVAL;
            this.e = jCurrentTimeMillis / 1000;
            sendEmptyMessageDelayed(f1835a, Constants.ASSEMBLE_PUSH_NETWORK_INTERVAL);
            this.i = true;
        }

        public final void c() {
            Message.obtain(this, b, Long.valueOf(this.e)).sendToTarget();
        }

        public final void d() {
            if (this.i && this.f == 0 && this.e > 0) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                this.f = jCurrentTimeMillis;
                this.h -= jCurrentTimeMillis - this.g;
                this.f = jCurrentTimeMillis / 1000;
                removeMessages(f1835a);
            }
        }

        public final void e() {
            if (!this.i || this.f <= 0 || this.e <= 0) {
                return;
            }
            this.e += (System.currentTimeMillis() / 1000) - this.f;
            this.f = 0L;
            sendEmptyMessageDelayed(f1835a, this.h);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) throws Throwable {
            if (this.c.get() == null || this.d.get() == null) {
                UniSdkUtils.e("LogoutHandler", "invalid record, invalid context or sdk instance");
                return;
            }
            int i = f1835a;
            if (i == message.what) {
                if (0 == this.e) {
                    UniSdkUtils.e("LogoutHandler", "invalid record, call PxLogInfo.registerLogoutLog");
                    return;
                }
                this.d.get().dispatchDrpf(g.a(this.c.get(), this.e), "LogoutRole");
                sendEmptyMessageDelayed(i, Constants.ASSEMBLE_PUSH_NETWORK_INTERVAL);
                this.g = System.currentTimeMillis();
                this.h = Constants.ASSEMBLE_PUSH_NETWORK_INTERVAL;
                return;
            }
            if (b == message.what) {
                long jLongValue = ((Long) message.obj).longValue();
                if (0 == jLongValue) {
                    UniSdkUtils.w("LogoutHandler", "invalid record, call PxLogInfo.registerLogoutLog");
                } else {
                    this.d.get().dispatchDrpf(g.a(this.c.get(), jLongValue), "LogoutRole");
                }
            }
        }
    }
}