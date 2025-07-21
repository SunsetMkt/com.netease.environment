package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.view.Window;
import com.netease.ntunisdk.zxing.notch.NotchDevice;

/* loaded from: classes.dex */
public class NotchImplHuaWei extends NotchDevice {
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected boolean checkModel(Context context) {
        try {
            Boolean bool = (Boolean) context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil").getMethod("hasNotchInScreen", new Class[0]).invoke(null, new Object[0]);
            if (bool != null) {
                return bool.booleanValue();
            }
        } catch (Throwable unused) {
        }
        return false;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:44:0x006d
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
            java.lang.ClassLoader r0 = r9.getClassLoader()     // Catch: java.lang.Throwable -> L6f
            java.lang.String r1 = "com.huawei.android.util.HwNotchSizeUtil"
            java.lang.Class r0 = r0.loadClass(r1)     // Catch: java.lang.Throwable -> L6f
            java.lang.String r1 = "getNotchSize"
            java.lang.Class[] r2 = new java.lang.Class[r10]     // Catch: java.lang.Throwable -> L6f
            java.lang.reflect.Method r1 = r0.getMethod(r1, r2)     // Catch: java.lang.Throwable -> L6f
            java.lang.Object[] r2 = new java.lang.Object[r10]     // Catch: java.lang.Throwable -> L6f
            java.lang.Object r0 = r1.invoke(r0, r2)     // Catch: java.lang.Throwable -> L6f
            int[] r0 = (int[]) r0     // Catch: java.lang.Throwable -> L6f
            r1 = 1
            if (r0 == 0) goto L24
            int r2 = r0.length     // Catch: java.lang.Throwable -> L6f
            if (r2 <= r1) goto L24
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L6f
            goto L25
        L24:
            r0 = 0
        L25:
            android.content.res.Resources r2 = r9.getResources()     // Catch: java.lang.Throwable -> L6d
            android.content.res.Configuration r2 = r2.getConfiguration()     // Catch: java.lang.Throwable -> L6d
            int r2 = r2.orientation     // Catch: java.lang.Throwable -> L6d
            r3 = 3
            r4 = 4
            r5 = 2
            r6 = 0
            if (r2 != r1) goto L4f
            com.netease.ntunisdk.zxing.notch.NotchInfo r2 = new com.netease.ntunisdk.zxing.notch.NotchInfo     // Catch: java.lang.Throwable -> L6d
            r2.<init>()     // Catch: java.lang.Throwable -> L6d
            android.graphics.Rect r7 = new android.graphics.Rect     // Catch: java.lang.Throwable -> L6d
            r7.<init>(r10, r10, r10, r0)     // Catch: java.lang.Throwable -> L6d
            r2.setNotchPosition(r0, r5, r7)     // Catch: java.lang.Throwable -> L6d
            com.netease.ntunisdk.zxing.notch.NotchInfo[] r4 = new com.netease.ntunisdk.zxing.notch.NotchInfo[r4]     // Catch: java.lang.Throwable -> L6d
            r4[r10] = r6     // Catch: java.lang.Throwable -> L6d
            r4[r1] = r2     // Catch: java.lang.Throwable -> L6d
            r4[r5] = r6     // Catch: java.lang.Throwable -> L6d
            r4[r3] = r6     // Catch: java.lang.Throwable -> L6d
            r8.mNotchs = r4     // Catch: java.lang.Throwable -> L6d
            goto L70
        L4f:
            com.netease.ntunisdk.zxing.notch.NotchInfo r2 = new com.netease.ntunisdk.zxing.notch.NotchInfo     // Catch: java.lang.Throwable -> L6d
            r2.<init>()     // Catch: java.lang.Throwable -> L6d
            android.graphics.Rect r7 = new android.graphics.Rect     // Catch: java.lang.Throwable -> L6d
            r7.<init>(r10, r10, r0, r10)     // Catch: java.lang.Throwable -> L6d
            r2.setNotchPosition(r0, r5, r7)     // Catch: java.lang.Throwable -> L6d
            com.netease.ntunisdk.zxing.notch.NotchInfo[] r7 = r8.mNotchs     // Catch: java.lang.Throwable -> L6d
            r7[r10] = r2     // Catch: java.lang.Throwable -> L6d
            com.netease.ntunisdk.zxing.notch.NotchInfo[] r4 = new com.netease.ntunisdk.zxing.notch.NotchInfo[r4]     // Catch: java.lang.Throwable -> L6d
            r4[r10] = r2     // Catch: java.lang.Throwable -> L6d
            r4[r1] = r6     // Catch: java.lang.Throwable -> L6d
            r4[r5] = r6     // Catch: java.lang.Throwable -> L6d
            r4[r3] = r6     // Catch: java.lang.Throwable -> L6d
            r8.mNotchs = r4     // Catch: java.lang.Throwable -> L6d
            goto L70
        L6d:
            goto L70
        L6f:
            r0 = 0
        L70:
            if (r0 <= 0) goto L73
            goto L77
        L73:
            int r0 = com.netease.ntunisdk.zxing.notch.Utils.getStatusBarHeight(r9, r10)
        L77:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.zxing.notch.NotchImplHuaWei.initNotchHeight(android.content.Context, android.view.Window):int");
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