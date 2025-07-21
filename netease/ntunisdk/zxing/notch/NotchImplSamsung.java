package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.zxing.notch.NotchDevice;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class NotchImplSamsung extends NotchDevice {
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected boolean checkModel(Context context) {
        try {
            Resources resources = context.getResources();
            int identifier = resources.getIdentifier("config_mainBuiltInDisplayCutout", ResIdReader.RES_TYPE_STRING, "android");
            String string = identifier > 0 ? resources.getString(identifier) : null;
            if (string != null) {
                return !TextUtils.isEmpty(string);
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected int initNotchHeight(Context context, Window window) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int i;
        try {
            View decorView = getDecorView(window);
            if (decorView != null && Build.VERSION.SDK_INT >= 23) {
                Object objInvoke = WindowInsets.class.getDeclaredMethod("getDisplayCutout", new Class[0]).invoke(decorView.getRootWindowInsets(), new Object[0]);
                Rect rect = new Rect();
                ArrayList arrayList = new ArrayList();
                Class<?> cls = objInvoke.getClass();
                rect.set(((Integer) cls.getDeclaredMethod("getSafeInsetLeft", new Class[0]).invoke(objInvoke, new Object[0])).intValue(), ((Integer) cls.getDeclaredMethod("getSafeInsetTop", new Class[0]).invoke(objInvoke, new Object[0])).intValue(), ((Integer) cls.getDeclaredMethod("getSafeInsetRight", new Class[0]).invoke(objInvoke, new Object[0])).intValue(), ((Integer) cls.getDeclaredMethod("getSafeInsetBottom", new Class[0]).invoke(objInvoke, new Object[0])).intValue());
                arrayList.addAll((List) cls.getDeclaredMethod("getBoundingRects", new Class[0]).invoke(objInvoke, new Object[0]));
                if (arrayList.isEmpty()) {
                    return 0;
                }
                int[] screenSize = getScreenSize(context);
                if (!((Rect) arrayList.get(0)).isEmpty()) {
                    NotchInfo notchInfo = new NotchInfo();
                    notchInfo.setScreenInfo(screenSize[0], screenSize[1]);
                    i = ((Rect) arrayList.get(0)).right;
                    notchInfo.setNotchPosition(i, 1, (Rect) arrayList.get(0));
                    this.mNotchs[0] = notchInfo;
                } else {
                    this.mNotchs[0] = null;
                    i = 0;
                }
                if (!((Rect) arrayList.get(1)).isEmpty()) {
                    NotchInfo notchInfo2 = new NotchInfo();
                    notchInfo2.setScreenInfo(screenSize[0], screenSize[1]);
                    i = ((Rect) arrayList.get(1)).bottom;
                    notchInfo2.setNotchPosition(i, 1, (Rect) arrayList.get(1));
                    this.mNotchs[1] = notchInfo2;
                } else {
                    this.mNotchs[1] = null;
                }
                if (!((Rect) arrayList.get(2)).isEmpty()) {
                    NotchInfo notchInfo3 = new NotchInfo();
                    notchInfo3.setScreenInfo(screenSize[0], screenSize[1]);
                    int i2 = ((Rect) arrayList.get(2)).right - ((Rect) arrayList.get(2)).left;
                    notchInfo3.setNotchPosition(i2, 1, (Rect) arrayList.get(2));
                    this.mNotchs[2] = notchInfo3;
                    i = i2;
                } else {
                    this.mNotchs[2] = null;
                }
                if (!((Rect) arrayList.get(3)).isEmpty()) {
                    NotchInfo notchInfo4 = new NotchInfo();
                    notchInfo4.setScreenInfo(screenSize[0], screenSize[1]);
                    int i3 = ((Rect) arrayList.get(3)).bottom - ((Rect) arrayList.get(3)).top;
                    notchInfo4.setNotchPosition(i3, 1, (Rect) arrayList.get(3));
                    this.mNotchs[3] = notchInfo4;
                    return i3;
                }
                this.mNotchs[3] = null;
                return i;
            }
        } catch (Exception unused) {
        }
        return 0;
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void addStatusBar(Context context, Window window) {
        addStatusBarImpl(context, window);
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void justify(Context context, Window window, NotchDevice.NotchAffectView[] notchAffectViewArr) {
        justifyImpl(context, window, notchAffectViewArr);
    }

    private View getDecorView(Window window) {
        if (window != null) {
            return window.getDecorView();
        }
        return null;
    }
}