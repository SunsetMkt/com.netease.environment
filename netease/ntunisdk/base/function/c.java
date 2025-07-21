package com.netease.ntunisdk.base.function;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.util.Locale;
import org.json.JSONObject;

/* compiled from: CountryCode.java */
/* loaded from: classes4.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    private static String f1831a;
    private static String b;

    public static String a() {
        Locale locale;
        if (TextUtils.isEmpty(f1831a)) {
            f1831a = b();
        }
        if (TextUtils.isEmpty(f1831a)) {
            if (Build.VERSION.SDK_INT >= 24) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
            f1831a = locale != null ? locale.getCountry() : null;
        }
        return f1831a;
    }

    private static String b() {
        try {
            return new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.JF_AIM_INFO)).optString(Const.COUNTRY);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String a(Context context) throws Resources.NotFoundException {
        String strA = a();
        if (TextUtils.isEmpty(strA)) {
            return null;
        }
        if (TextUtils.isEmpty(b)) {
            int identifier = context.getResources().getIdentifier("unisdk_country_codes", ResIdReader.RES_TYPE_ARRAY, context.getPackageName());
            if (identifier > 0) {
                String[] stringArray = context.getResources().getStringArray(identifier);
                int length = stringArray.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String[] strArrSplit = stringArray[i].split(",");
                    if (strArrSplit[1].trim().equals(strA)) {
                        b = strArrSplit[0];
                        break;
                    }
                    i++;
                }
            } else {
                return null;
            }
        }
        return b;
    }
}