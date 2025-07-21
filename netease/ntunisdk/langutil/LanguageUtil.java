package com.netease.ntunisdk.langutil;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class LanguageUtil {
    private static HashMap<String, Resources> sCachedRes = new HashMap<>();
    public static String language = null;
    private static String region = null;

    public static void setLanguageCode(String str) {
        if (str == null) {
            return;
        }
        if (ConstProp.LANGUAGE_CODE_ZH_HANS.equalsIgnoreCase(str) || ConstProp.LANGUAGE_CODE_ZH_CN.equalsIgnoreCase(str)) {
            language = "zh";
            region = ConstProp.GAME_REGION_CN;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_ZH_HANT.equalsIgnoreCase(str) || ConstProp.LANGUAGE_CODE_ZH_HK.equalsIgnoreCase(str)) {
            language = "zh";
            region = "HK";
            return;
        }
        if (ConstProp.LANGUAGE_CODE_ZH_TW.equalsIgnoreCase(str)) {
            language = "zh";
            region = "TW";
            return;
        }
        if (ConstProp.LANGUAGE_CODE_EN.equalsIgnoreCase(str)) {
            language = "en";
            region = ConstProp.GAME_REGION_US;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_JA.equalsIgnoreCase(str)) {
            language = "ja";
            region = ConstProp.GAME_REGION_JP;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_KO.equalsIgnoreCase(str)) {
            language = "ko";
            region = "KR";
            return;
        }
        if (ConstProp.LANGUAGE_CODE_PT.equalsIgnoreCase(str)) {
            language = "pt";
            region = ConstProp.LANGUAGE_CODE_PT;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_RU.equalsIgnoreCase(str)) {
            language = "ru";
            region = ConstProp.LANGUAGE_CODE_RU;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_DE.equalsIgnoreCase(str)) {
            language = "de";
            region = ConstProp.LANGUAGE_CODE_DE;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_ES.equalsIgnoreCase(str)) {
            language = "es";
            region = ConstProp.LANGUAGE_CODE_ES;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_TH.equalsIgnoreCase(str)) {
            language = "th";
            region = ConstProp.LANGUAGE_CODE_TH;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_VI.equalsIgnoreCase(str)) {
            language = "vi";
            region = "VN";
            return;
        }
        if (ConstProp.LANGUAGE_CODE_TR.equalsIgnoreCase(str)) {
            language = "tr";
            region = ConstProp.LANGUAGE_CODE_TR;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_HI.equalsIgnoreCase(str)) {
            language = "hi";
            region = ConstProp.LANGUAGE_CODE_IN;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_IN.equalsIgnoreCase(str)) {
            language = "in";
            region = ClientLogConstant.ID;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_FR.equalsIgnoreCase(str)) {
            language = "fr";
            region = ConstProp.LANGUAGE_CODE_FR;
            return;
        }
        if (ConstProp.LANGUAGE_CODE_MS.equalsIgnoreCase(str)) {
            language = "ms";
            region = "MY";
        } else if (ConstProp.LANGUAGE_CODE_IT.equalsIgnoreCase(str)) {
            language = "it";
            region = ConstProp.LANGUAGE_CODE_IT;
        } else if (ConstProp.LANGUAGE_CODE_AR.equalsIgnoreCase(str)) {
            language = "ar";
            region = ConstProp.GAME_REGION_SA;
        }
    }

    private static Locale getLocale() {
        if (language == null) {
            return Locale.getDefault();
        }
        return new Locale(language, region);
    }

    private static Locale changeLocale() {
        Locale locale = getLocale();
        return locale == null ? Locale.getDefault() : locale;
    }

    private static String getKey() {
        return language + "_" + region;
    }

    public static String getStringByLocale(Context context, int i) {
        Resources resources;
        String key = getKey();
        if (language == null) {
            resources = context.getResources();
        } else if (sCachedRes.containsKey(key)) {
            resources = sCachedRes.get(key);
        } else {
            Resources resources2 = context.getResources();
            Configuration configuration = new Configuration(resources2.getConfiguration());
            Locale localeChangeLocale = changeLocale();
            if (Build.VERSION.SDK_INT <= 17) {
                configuration.locale = localeChangeLocale;
                resources = new Resources(resources2.getAssets(), resources2.getDisplayMetrics(), configuration);
            } else {
                configuration.setLocale(localeChangeLocale);
                Resources resources3 = context.createConfigurationContext(configuration).getResources();
                resources = resources3 == null ? context.getResources() : resources3;
            }
            sCachedRes.put(key, resources);
        }
        return resources != null ? resources.getString(i) : "";
    }

    public static String getString(Context context, int i) {
        if (language == null) {
            return context.getResources().getString(i);
        }
        return getStringByLocale(context, i);
    }
}