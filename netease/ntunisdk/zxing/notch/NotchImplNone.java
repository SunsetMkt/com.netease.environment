package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.view.Window;
import com.netease.ntunisdk.zxing.notch.NotchDevice;

/* loaded from: classes.dex */
public class NotchImplNone extends NotchDevice {
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void addStatusBar(Context context, Window window) {
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected boolean checkModel(Context context) {
        return true;
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected int initNotchHeight(Context context, Window window) {
        return 0;
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void justify(Context context, Window window, NotchDevice.NotchAffectView[] notchAffectViewArr) {
    }
}