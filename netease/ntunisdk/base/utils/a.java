package com.netease.ntunisdk.base.utils;

import android.content.Context;
import com.netease.ntunisdk.base.utils.cps.ApkChanneling;
import java.io.File;
import java.io.IOException;

/* compiled from: CpsChannelReader.java */
/* loaded from: classes3.dex */
public final class a {
    public static String a(Context context) {
        try {
            return ApkChanneling.getChannel(new File(context.getPackageCodePath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            return null;
        }
    }
}