package com.netease.pushclient;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.util.Locale;

/* loaded from: classes3.dex */
public class LanguageUtil {
    public static Locale createLocal(String str) {
        if ("zh-Hans".equalsIgnoreCase(str) || ConstProp.LANGUAGE_CODE_ZH_CN.equalsIgnoreCase(str)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if ("zh-Hant".equalsIgnoreCase(str) || ConstProp.LANGUAGE_CODE_ZH_HK.equalsIgnoreCase(str) || ConstProp.LANGUAGE_CODE_ZH_TW.equalsIgnoreCase(str)) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return new Locale(str);
    }

    public static String languageMap(String str) {
        if (TextUtils.isEmpty(str) || ConstProp.LANGUAGE_CODE_AUTO.equalsIgnoreCase(str)) {
            return null;
        }
        return ResIdReader.RES_TYPE_ID.equalsIgnoreCase(str) ? "in" : ConstProp.LANGUAGE_CODE_ES_EUR.equalsIgnoreCase(str) ? ConstProp.LANGUAGE_CODE_ES : ConstProp.LANGUAGE_CODE_PT_EUR.equalsIgnoreCase(str) ? ConstProp.LANGUAGE_CODE_PT : str;
    }
}