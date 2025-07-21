package com.netease.mpay.ps.codescanner.widget;

import android.content.Context;
import com.netease.mpay.ps.codescanner.utils.Logging;

/* loaded from: classes5.dex */
public class RIdentifier {
    private static Context mContext;
    private static RIdentifier mInstance;
    private static String mPackageName;

    public static final class anim {
    }

    public static final class attr {
    }

    public static final class bool {
    }

    public static final class color {
    }

    public static final class dimen {
    }

    public static final class drawable {
    }

    public static final class id {
    }

    public static final class integer {
    }

    public static final class layout {
    }

    public static final class menu {
    }

    public static final class string {
    }

    public static final class style {
    }

    public static final class styleable {
    }

    private RIdentifier(Context context) {
        if (context == null) {
            return;
        }
        mContext = context.getApplicationContext();
        mPackageName = context.getPackageName();
    }

    public static void init(Context context) {
        synchronized (RIdentifier.class) {
            if (mInstance == null) {
                mInstance = new RIdentifier(context);
            }
        }
    }

    private static final int getIdentifier(String str, String str2) {
        try {
            return mContext.getResources().getIdentifier(str, str2, mPackageName);
        } catch (Exception e) {
            Logging.logStackTrace(e);
            return 0;
        }
    }
}