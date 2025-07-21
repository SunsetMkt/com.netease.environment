package com.netease.environment.OIIO0II;

import java.text.Normalizer;
import java.util.Arrays;

/* compiled from: UnicodeUtils.java */
/* loaded from: classes5.dex */
public class OIIOO0O {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1561OIIO00I = "OIIOO0O";

    public static boolean OIIO00I(String str) {
        return str.equals(new String(str.getBytes("UTF-8"), "UTF-8"));
    }

    public static String OIIO0O0(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        try {
            char[] charArray = str.toCharArray();
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < charArray.length) {
                int iCharCount = Character.charCount(Character.codePointAt(charArray, i)) + i;
                String strValueOf = String.valueOf(Arrays.copyOfRange(charArray, i, iCharCount));
                String strNormalize = Normalizer.normalize(strValueOf, Normalizer.Form.NFKC);
                if (strValueOf.codePointCount(0, strValueOf.length()) >= strNormalize.codePointCount(0, strNormalize.length())) {
                    sb.append(strNormalize);
                } else {
                    sb.append(strValueOf);
                }
                i = iCharCount;
            }
            return sb.toString();
        } catch (Exception e) {
            OIIO.OIIO0O0(f1561OIIO00I, "fail to normalize : " + e.getMessage());
            return str;
        }
    }
}