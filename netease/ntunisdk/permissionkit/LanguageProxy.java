package com.netease.ntunisdk.permissionkit;

import android.content.Context;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.permission.utils.PermissionTextUtils;

/* loaded from: classes.dex */
public class LanguageProxy {
    public static void setLanguageCode(String str) {
        if (str == null) {
            return;
        }
        boolean zEqualsIgnoreCase = ConstProp.LANGUAGE_CODE_ZH_HANS.equalsIgnoreCase(str);
        String str2 = ConstProp.LANGUAGE_CODE_FR;
        String str3 = null;
        if (zEqualsIgnoreCase || ConstProp.LANGUAGE_CODE_ZH_CN.equalsIgnoreCase(str)) {
            str2 = ConstProp.GAME_REGION_CN;
        } else if (ConstProp.LANGUAGE_CODE_ZH_HANT.equalsIgnoreCase(str) || ConstProp.LANGUAGE_CODE_ZH_HK.equalsIgnoreCase(str)) {
            str2 = "HK";
        } else {
            if (!ConstProp.LANGUAGE_CODE_ZH_TW.equalsIgnoreCase(str)) {
                if (ConstProp.LANGUAGE_CODE_EN.equalsIgnoreCase(str)) {
                    str3 = "en";
                    str2 = ConstProp.GAME_REGION_US;
                } else if (ConstProp.LANGUAGE_CODE_JA.equalsIgnoreCase(str)) {
                    str3 = "ja";
                    str2 = ConstProp.GAME_REGION_JP;
                } else if (ConstProp.LANGUAGE_CODE_KO.equalsIgnoreCase(str)) {
                    str3 = "ko";
                    str2 = "KR";
                } else if (ConstProp.LANGUAGE_CODE_PT.equalsIgnoreCase(str)) {
                    str3 = "pt";
                    str2 = ConstProp.LANGUAGE_CODE_PT;
                } else if (ConstProp.LANGUAGE_CODE_RU.equalsIgnoreCase(str)) {
                    str3 = "ru";
                    str2 = ConstProp.LANGUAGE_CODE_RU;
                } else if (ConstProp.LANGUAGE_CODE_DE.equalsIgnoreCase(str)) {
                    str3 = "de";
                    str2 = ConstProp.LANGUAGE_CODE_DE;
                } else if (ConstProp.LANGUAGE_CODE_ES.equalsIgnoreCase(str)) {
                    str3 = "es";
                    str2 = ConstProp.LANGUAGE_CODE_ES;
                } else if (ConstProp.LANGUAGE_CODE_TH.equalsIgnoreCase(str)) {
                    str3 = "th";
                    str2 = ConstProp.LANGUAGE_CODE_TH;
                } else if (ConstProp.LANGUAGE_CODE_VI.equalsIgnoreCase(str)) {
                    str3 = "vi";
                    str2 = "VN";
                } else if (ConstProp.LANGUAGE_CODE_TR.equalsIgnoreCase(str)) {
                    str3 = "tr";
                    str2 = ConstProp.LANGUAGE_CODE_TR;
                } else if (ConstProp.LANGUAGE_CODE_HI.equalsIgnoreCase(str)) {
                    str3 = "hi";
                    str2 = ConstProp.LANGUAGE_CODE_IN;
                } else if (ConstProp.LANGUAGE_CODE_IN.equalsIgnoreCase(str)) {
                    str3 = "in";
                    str2 = ClientLogConstant.ID;
                } else if (ConstProp.LANGUAGE_CODE_FR.equalsIgnoreCase(str)) {
                    str3 = "fr";
                } else {
                    str2 = null;
                }
                PermissionTextUtils.setLanguageAndRegion(str3, str2);
            }
            str2 = "TW";
        }
        str3 = "zh";
        PermissionTextUtils.setLanguageAndRegion(str3, str2);
    }

    public static String getString(Context context, int i) {
        return PermissionTextUtils.getString(context, i);
    }
}