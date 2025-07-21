package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.graphics.Rect;
import android.view.Window;
import com.netease.ntunisdk.zxing.notch.NotchDevice;

/* loaded from: classes.dex */
public class NotchImplOppo extends NotchDevice {
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected boolean checkModel(Context context) {
        try {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Throwable unused) {
            return false;
        }
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected int initNotchHeight(Context context, Window window) {
        int statusBarHeight = Utils.getStatusBarHeight(context, 0);
        if (context.getResources().getConfiguration().orientation == 1) {
            NotchInfo notchInfo = new NotchInfo();
            notchInfo.setNotchPosition(statusBarHeight, 2, new Rect(0, 0, 0, statusBarHeight));
            this.mNotchs = new NotchInfo[]{null, notchInfo, null, null};
        } else {
            NotchInfo notchInfo2 = new NotchInfo();
            notchInfo2.setNotchPosition(statusBarHeight, 2, new Rect(0, 0, statusBarHeight, 0));
            this.mNotchs[0] = notchInfo2;
            this.mNotchs = new NotchInfo[]{notchInfo2, null, null, null};
        }
        return statusBarHeight;
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