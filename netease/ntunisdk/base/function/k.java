package com.netease.ntunisdk.base.function;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import com.netease.ntunisdk.base.view.Alerter;
import org.json.JSONObject;

/* compiled from: OrderProtocol.java */
/* loaded from: classes4.dex */
public final class k {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f1839a;
    private static boolean b;
    private static String c;
    private static String d;
    private static String e;
    private static String f;
    private static String g;
    private static SharedPreferences h;

    public static void a() {
        String string;
        f1839a = SdkMgr.getInst().getPropInt(ConstProp.DEBUG_MODE, 0) != 0;
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_UNI_UPDATE_URL);
        if (TextUtils.isEmpty(propStr)) {
            string = SdkMgr.getInst().hasFeature("EB") ? ConstProp.SDK_SWITCHER_PROJECT_OVERSEA_URL : ConstProp.SDK_SWITCHER_PROJECT_URL;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(propStr);
            sb.append(propStr.endsWith("/") ? "" : "/");
            sb.append("feature2/");
            string = sb.toString();
        }
        if (TextUtils.isEmpty(string)) {
            UniSdkUtils.i("ProtocolFeature2", "null or empty url, request protocol info will not go on");
            return;
        }
        String str = string + SdkMgr.getInst().getPropStr("JF_GAMEID") + ".common_config.json?gameid=" + SdkMgr.getInst().getPropStr("JF_GAMEID");
        UniSdkUtils.i("ProtocolFeature2", "url: ".concat(String.valueOf(str)));
        NetUtil.wget(str, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.function.k.1
            @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
            public final void ProcessResult(String str2) {
                UniSdkUtils.i("ProtocolFeature2", "result: ".concat(String.valueOf(str2)));
                k.a(str2);
            }
        });
    }

    public static boolean a(Context context, final OrderInfo orderInfo, final boolean z) {
        if (orderInfo == null) {
            UniSdkUtils.e("ProtocolFeature2", "invalid order info instance");
            return false;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UID, orderInfo.getUid());
        UniSdkUtils.i("ProtocolFeature2", "uid=".concat(String.valueOf(propStr)));
        boolean z2 = ((!f1839a && !b) || TextUtils.isEmpty(d) || TextUtils.isEmpty(e) || TextUtils.isEmpty(propStr)) ? false : true;
        if (h == null) {
            h = context.getSharedPreferences("ProtocolFeature2", 0);
        }
        final String str = propStr + "_" + e;
        boolean z3 = h.getBoolean(str, false);
        UniSdkUtils.i("ProtocolFeature2", "enable:" + z2 + ", agreed:" + z3);
        if (!z2 || z3) {
            UniSdkUtils.i("ProtocolFeature2", "order confirmation trigger checked to be off");
            return false;
        }
        new Alerter(context).showDialog(c, d, f, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.function.k.2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) throws Throwable {
                if (k.h != null) {
                    k.h.edit().putBoolean(str, true).commit();
                } else {
                    UniSdkUtils.e("ProtocolFeature2", "no sp ready");
                }
                k.a(orderInfo, z, true);
            }
        }, g, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.base.function.k.3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) throws Throwable {
                k.a(orderInfo, z, false);
            }
        }, false, true, null);
        return true;
    }

    public static void a(OrderInfo orderInfo, boolean z, boolean z2) throws Throwable {
        SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
        if (z2) {
            if (!sdkBase.hasFeature(ConstProp.INNER_MODE_AI_DETECT)) {
                sdkBase.continueOrderSetting(orderInfo, z);
                return;
            } else {
                a.a(orderInfo, z);
                return;
            }
        }
        orderInfo.setOrderStatus(3);
        orderInfo.setOrderErrReason("confirmation denied, order cancel");
        sdkBase.checkOrderDone(orderInfo);
    }

    static /* synthetic */ void a(String str) {
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.w("ProtocolFeature2", "json invalid");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("order_privacy_hint");
            if (jSONObjectOptJSONObject != null) {
                jSONObject = jSONObjectOptJSONObject;
            }
            b = jSONObject.optBoolean("enable");
            e = jSONObject.optString("version");
            d = jSONObject.optString("text");
            c = jSONObject.optString("title");
            f = jSONObject.optString("agree");
            g = jSONObject.optString("reject");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}