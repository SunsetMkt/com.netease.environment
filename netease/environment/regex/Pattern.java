package com.netease.environment.regex;

import android.content.Context;
import java.util.List;

/* loaded from: classes6.dex */
public abstract class Pattern {
    public static final int CASE_INSENSITIVE = 2;
    public static final int EXTRACT_COMMENT = 512;
    public static final int UNICODE_CASE = 64;

    public static Pattern compile(String str, int i) {
        return (Pattern) Class.forName("com.netease.environment.A").getDeclaredMethod("b", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(i));
    }

    public static boolean initLocalMaps(Context context, String str, String str2) {
        try {
            return ((Boolean) Class.forName("com.netease.environment.A").getDeclaredMethod("c", Context.class, String.class, String.class).invoke(null, context, str, str2)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean initMapsMemory(Context context, String str, String str2) {
        try {
            return ((Boolean) Class.forName("com.netease.environment.A").getDeclaredMethod("d", Context.class, String.class, String.class).invoke(null, context, str, str2)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isPCRE() {
        try {
            return ((Boolean) Class.forName("com.netease.environment.A").getDeclaredMethod("a", new Class[0]).invoke(null, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract String getComment();

    public abstract Matcher matcher(CharSequence charSequence);

    public abstract List<Matcher> matchers(String str);
}