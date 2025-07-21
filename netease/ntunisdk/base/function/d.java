package com.netease.ntunisdk.base.function;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.utils.LifeCycleChecker;
import com.netease.ntunisdk.base.utils.clientlog.ClientLogUtils;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: LoginPlugin.java */
/* loaded from: classes4.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    private static List<SdkBase> f1832a;
    private static Iterator<SdkBase> b;
    private static SdkBase c;

    public static boolean a(SdkBase sdkBase, int i) throws JSONException {
        if (f1832a == null) {
            f1832a = new LinkedList();
            for (SdkBase sdkBase2 : sdkBase.getLoginSdkInstMap().values()) {
                if (sdkBase2.getPropInt(ConstProp.LOGIN_PLUGIN_PRIORITY, 0) > 0) {
                    f1832a.add(sdkBase2);
                }
            }
            for (SdkBase sdkBase3 : sdkBase.getSdkInstMap().values()) {
                if (sdkBase3.getPropInt(ConstProp.LOGIN_PLUGIN_PRIORITY, 0) > 0) {
                    f1832a.add(sdkBase3);
                }
            }
            if (f1832a.size() > 1) {
                Collections.sort(f1832a, new Comparator<SdkBase>() { // from class: com.netease.ntunisdk.base.function.d.1
                    @Override // java.util.Comparator
                    public final /* synthetic */ int compare(SdkBase sdkBase4, SdkBase sdkBase5) {
                        return Integer.compare(sdkBase5.getPropInt(ConstProp.LOGIN_PLUGIN_PRIORITY, 0), sdkBase4.getPropInt(ConstProp.LOGIN_PLUGIN_PRIORITY, 0));
                    }
                });
            }
        }
        if (b == null) {
            b = f1832a.iterator();
        }
        if (!b.hasNext()) {
            b = null;
            return false;
        }
        SdkBase next = b.next();
        try {
            c = next;
            String strA = a(next.getChannel());
            if (!TextUtils.isEmpty(strA)) {
                sdkBase.saveClientLog(null, ClientLogUtils.objArgs2Json(new String[]{OneTrackParams.XMSdkParams.STEP, strA + "_start"}).toString());
            }
            LifeCycleChecker.getInst().start(c.getChannel());
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "loginPlugin");
            jSONObject.putOpt("loginDoneCode", Integer.valueOf(i));
            next.extendFunc(jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void a(int i) throws JSONException {
        if (c == null) {
            return;
        }
        LifeCycleChecker.getInst().stop(c.getChannel());
        String strA = a(c.getChannel());
        c = null;
        if (TextUtils.isEmpty(strA)) {
            return;
        }
        JSONObject jSONObjectObjArgs2Json = ClientLogUtils.objArgs2Json(new String[]{"code", (i == 0 || i == 13 || i == 130) ? "0" : "1"});
        ((SdkBase) SdkMgr.getInst()).saveClientLog(null, ClientLogUtils.objArgs2Json(new Object[]{OneTrackParams.XMSdkParams.STEP, strA + "_done", "raw_info", jSONObjectObjArgs2Json}).toString());
    }

    private static String a(String str) {
        return (!TextUtils.isEmpty(str) && str.equals("child_protection")) ? LifeCycleChecker.LibTag.CHILD_PROTECT : "";
    }
}