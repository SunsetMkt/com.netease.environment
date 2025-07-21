package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.os.Build;
import android.view.Window;
import com.netease.ntunisdk.zxing.notch.NotchDevice;

/* loaded from: classes.dex */
public class NotchImplMiui extends NotchDevice {
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected boolean checkModel(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 28) {
                return 1 == Integer.parseInt(SystemPropProxy.get("ro.miui.notch", String.valueOf(0)));
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:40:0x0064
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected int initNotchHeight(android.content.Context r9, android.view.Window r10) {
        /*
            r8 = this;
            r10 = 0
            android.content.res.Resources r0 = r9.getResources()     // Catch: java.lang.Throwable -> L66
            java.lang.String r1 = "notch_height"
            java.lang.String r2 = "dimen"
            java.lang.String r3 = "android"
            int r0 = r0.getIdentifier(r1, r2, r3)     // Catch: java.lang.Throwable -> L66
            if (r0 <= 0) goto L1a
            android.content.res.Resources r1 = r9.getResources()     // Catch: java.lang.Throwable -> L66
            int r0 = r1.getDimensionPixelSize(r0)     // Catch: java.lang.Throwable -> L66
            goto L1b
        L1a:
            r0 = 0
        L1b:
            android.content.res.Resources r1 = r9.getResources()     // Catch: java.lang.Throwable -> L64
            android.content.res.Configuration r1 = r1.getConfiguration()     // Catch: java.lang.Throwable -> L64
            int r1 = r1.orientation     // Catch: java.lang.Throwable -> L64
            r2 = 3
            r3 = 4
            r4 = 1
            r5 = 2
            r6 = 0
            if (r1 != r4) goto L46
            com.netease.ntunisdk.zxing.notch.NotchInfo r1 = new com.netease.ntunisdk.zxing.notch.NotchInfo     // Catch: java.lang.Throwable -> L64
            r1.<init>()     // Catch: java.lang.Throwable -> L64
            android.graphics.Rect r7 = new android.graphics.Rect     // Catch: java.lang.Throwable -> L64
            r7.<init>(r10, r10, r10, r0)     // Catch: java.lang.Throwable -> L64
            r1.setNotchPosition(r0, r5, r7)     // Catch: java.lang.Throwable -> L64
            com.netease.ntunisdk.zxing.notch.NotchInfo[] r3 = new com.netease.ntunisdk.zxing.notch.NotchInfo[r3]     // Catch: java.lang.Throwable -> L64
            r3[r10] = r6     // Catch: java.lang.Throwable -> L64
            r3[r4] = r1     // Catch: java.lang.Throwable -> L64
            r3[r5] = r6     // Catch: java.lang.Throwable -> L64
            r3[r2] = r6     // Catch: java.lang.Throwable -> L64
            r8.mNotchs = r3     // Catch: java.lang.Throwable -> L64
            goto L67
        L46:
            com.netease.ntunisdk.zxing.notch.NotchInfo r1 = new com.netease.ntunisdk.zxing.notch.NotchInfo     // Catch: java.lang.Throwable -> L64
            r1.<init>()     // Catch: java.lang.Throwable -> L64
            android.graphics.Rect r7 = new android.graphics.Rect     // Catch: java.lang.Throwable -> L64
            r7.<init>(r10, r10, r0, r10)     // Catch: java.lang.Throwable -> L64
            r1.setNotchPosition(r0, r5, r7)     // Catch: java.lang.Throwable -> L64
            com.netease.ntunisdk.zxing.notch.NotchInfo[] r7 = r8.mNotchs     // Catch: java.lang.Throwable -> L64
            r7[r10] = r1     // Catch: java.lang.Throwable -> L64
            com.netease.ntunisdk.zxing.notch.NotchInfo[] r3 = new com.netease.ntunisdk.zxing.notch.NotchInfo[r3]     // Catch: java.lang.Throwable -> L64
            r3[r10] = r1     // Catch: java.lang.Throwable -> L64
            r3[r4] = r6     // Catch: java.lang.Throwable -> L64
            r3[r5] = r6     // Catch: java.lang.Throwable -> L64
            r3[r2] = r6     // Catch: java.lang.Throwable -> L64
            r8.mNotchs = r3     // Catch: java.lang.Throwable -> L64
            goto L67
        L64:
            goto L67
        L66:
            r0 = 0
        L67:
            if (r0 <= 0) goto L6a
            goto L6e
        L6a:
            int r0 = com.netease.ntunisdk.zxing.notch.Utils.getStatusBarHeight(r9, r10)
        L6e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.zxing.notch.NotchImplMiui.initNotchHeight(android.content.Context, android.view.Window):int");
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void addStatusBar(Context context, Window window) {
        addStatusBarImpl(context, window);
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void justify(Context context, Window window, NotchDevice.NotchAffectView[] notchAffectViewArr) {
        justifyImpl(context, window, notchAffectViewArr);
    }
}