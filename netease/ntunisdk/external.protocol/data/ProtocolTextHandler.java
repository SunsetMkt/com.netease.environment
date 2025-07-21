package com.netease.ntunisdk.external.protocol.data;

import com.netease.ntunisdk.external.protocol.utils.L;

/* loaded from: classes.dex */
public class ProtocolTextHandler {
    private static final String AD_CLASS_END_TAG = " ios_class=";
    private static final String AGREEMENT_END_TAG = "</user_agreement>";
    private static final int AGREEMENT_END_TAG_LEN = 17;
    private static final String AGREEMENT_START_TAG = "<user_agreement ad_class=";
    private static final int AGREEMENT_START_TAG_LEN = 25;
    private static final String DIV_END_TAG = "</div>";
    private static final String DIV_START_TAG = "<div>";
    private static final String TAG = "TH";

    public static String handle(String str, String str2, String str3) {
        L.d("TH", "handle start ======= ");
        try {
            L.d("TH", "Launch issuer : " + str2 + ", gameName:" + str3);
            return findAgreementAndHandle(str);
        } catch (Exception e) {
            e.printStackTrace();
            L.e("TH", "handle >> " + e.getMessage());
            return str;
        }
    }

    private static String findAgreementAndHandle(String str) {
        int[] iArrFindAgreementTag = findAgreementTag(str);
        while (iArrFindAgreementTag != null) {
            str = handleOnce(str, iArrFindAgreementTag[0], iArrFindAgreementTag[1]);
            iArrFindAgreementTag = findAgreementTag(str);
        }
        return str;
    }

    private static String handleOnce(String str, int i, int i2) {
        int i3 = AGREEMENT_END_TAG_LEN;
        String sdkItem = parseSdkItem(str.substring(i, i2 + i3));
        if (sdkItem == null) {
            return str.substring(0, i) + str.substring(i2 + i3);
        }
        return str.substring(0, i) + sdkItem + str.substring(i2 + i3);
    }

    private static int[] findAgreementTag(String str) {
        int iIndexOf = str.indexOf(AGREEMENT_START_TAG);
        int iIndexOf2 = str.indexOf(AGREEMENT_END_TAG);
        if (iIndexOf < 0 || iIndexOf2 <= 0) {
            return null;
        }
        return new int[]{iIndexOf, iIndexOf2};
    }

    private static String parseSdkItem(String str) {
        try {
            int iIndexOf = str.indexOf(AD_CLASS_END_TAG);
            String strSubstring = str.substring(AGREEMENT_START_TAG_LEN + 1, iIndexOf - 1);
            L.d("TH", "ad_class_name : " + strSubstring);
            if (!isClassFound(strSubstring)) {
                return null;
            }
            return DIV_START_TAG + str.substring(str.indexOf(">", iIndexOf) + 1, str.length() - AGREEMENT_END_TAG_LEN) + DIV_END_TAG;
        } catch (Exception e) {
            L.e("TH", "parseSdkItem >> " + e.getMessage());
            return str;
        }
    }

    private static boolean isClassFound(String str) throws ClassNotFoundException {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}