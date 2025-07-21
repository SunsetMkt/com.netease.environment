package com.netease.ntunisdk.okio;

/* loaded from: classes3.dex */
public final class Utf8 {
    private Utf8() {
    }

    public static long size(String str) {
        return size(str, 0, str.length());
    }

    public static long size(String str, int i, int i2) {
        long j;
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("beginIndex < 0: " + i);
        }
        if (i2 < i) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + i2 + " < " + i);
        }
        if (i2 > str.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + i2 + " > " + str.length());
        }
        long j2 = 0;
        while (i < i2) {
            char cCharAt = str.charAt(i);
            if (cCharAt < '\u0080') {
                j2++;
            } else {
                if (cCharAt < '\u0800') {
                    j = 2;
                } else if (cCharAt < '\ud800' || cCharAt > '\udfff') {
                    j = 3;
                } else {
                    int i3 = i + 1;
                    char cCharAt2 = i3 < i2 ? str.charAt(i3) : (char) 0;
                    if (cCharAt > '\udbff' || cCharAt2 < '\udc00' || cCharAt2 > '\udfff') {
                        j2++;
                        i = i3;
                    } else {
                        j2 += 4;
                        i += 2;
                    }
                }
                j2 += j;
            }
            i++;
        }
        return j2;
    }
}