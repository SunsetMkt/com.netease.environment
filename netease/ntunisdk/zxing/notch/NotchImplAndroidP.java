package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.zxing.notch.NotchDevice;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class NotchImplAndroidP extends NotchDevice {
    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected boolean checkModel(Context context) {
        return Build.VERSION.SDK_INT >= 28;
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    protected int initNotchHeight(Context context, Window window) {
        int i;
        int i2;
        View decorView = getDecorView(window);
        int[] screenSize = getScreenSize(context);
        if (decorView != null && Build.VERSION.SDK_INT >= 29) {
            WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
            DisplayCutout displayCutout = rootWindowInsets != null ? rootWindowInsets.getDisplayCutout() : null;
            if (displayCutout == null) {
                return 0;
            }
            UniSdkUtils.d("notch", "NotchImplAndroidP:" + displayCutout.toString());
            try {
                if (!displayCutout.getBoundingRectLeft().isEmpty()) {
                    NotchInfo notchInfo = new NotchInfo();
                    notchInfo.setScreenInfo(screenSize[0], screenSize[1]);
                    i = displayCutout.getBoundingRectLeft().right;
                    try {
                        notchInfo.setNotchPosition(i, 1, displayCutout.getBoundingRectLeft());
                        this.mNotchs[0] = notchInfo;
                    } catch (Throwable th) {
                        th = th;
                        th.printStackTrace();
                        try {
                            ArrayList arrayList = (ArrayList) displayCutout.getBoundingRects();
                            if (arrayList.size() == 1) {
                                Rect rect = (Rect) arrayList.get(0);
                                if (displayCutout.getSafeInsetTop() > 0) {
                                    NotchInfo notchInfo2 = new NotchInfo();
                                    notchInfo2.setScreenInfo(screenSize[0], screenSize[1]);
                                    int i3 = rect.bottom;
                                    notchInfo2.setNotchPosition(i3, 2, rect);
                                    this.mNotchs[1] = notchInfo2;
                                    return i3;
                                }
                                if (displayCutout.getSafeInsetLeft() > 0) {
                                    NotchInfo notchInfo3 = new NotchInfo();
                                    notchInfo3.setScreenInfo(screenSize[0], screenSize[1]);
                                    int i4 = rect.right;
                                    notchInfo3.setNotchPosition(i4, 1, rect);
                                    this.mNotchs[0] = notchInfo3;
                                    return i4;
                                }
                                if (displayCutout.getSafeInsetRight() > 0) {
                                    NotchInfo notchInfo4 = new NotchInfo();
                                    notchInfo4.setScreenInfo(screenSize[0], screenSize[1]);
                                    int i5 = rect.right - rect.left;
                                    notchInfo4.setNotchPosition(i5, 4, rect);
                                    this.mNotchs[2] = notchInfo4;
                                    return i5;
                                }
                                NotchInfo notchInfo5 = new NotchInfo();
                                notchInfo5.setScreenInfo(screenSize[0], screenSize[1]);
                                int i6 = rect.bottom - rect.top;
                                notchInfo5.setNotchPosition(i6, 8, rect);
                                this.mNotchs[3] = notchInfo5;
                                return i6;
                            }
                            Iterator it = arrayList.iterator();
                            int i7 = 0;
                            while (it.hasNext()) {
                                Rect rect2 = (Rect) it.next();
                                if (i7 == 0) {
                                    NotchInfo notchInfo6 = new NotchInfo();
                                    notchInfo6.setScreenInfo(screenSize[0], screenSize[1]);
                                    i2 = rect2.right;
                                    notchInfo6.setNotchPosition(i2, 1, rect2);
                                    this.mNotchs[0] = notchInfo6;
                                } else if (i7 == 1) {
                                    NotchInfo notchInfo7 = new NotchInfo();
                                    notchInfo7.setScreenInfo(screenSize[0], screenSize[1]);
                                    i2 = rect2.bottom;
                                    notchInfo7.setNotchPosition(i2, 2, rect2);
                                    this.mNotchs[1] = notchInfo7;
                                } else if (i7 == 2) {
                                    NotchInfo notchInfo8 = new NotchInfo();
                                    notchInfo8.setScreenInfo(screenSize[0], screenSize[1]);
                                    i2 = rect2.right - rect2.left;
                                    notchInfo8.setNotchPosition(i2, 4, rect2);
                                    this.mNotchs[2] = notchInfo8;
                                } else if (i7 != 3) {
                                    i7++;
                                } else {
                                    NotchInfo notchInfo9 = new NotchInfo();
                                    notchInfo9.setScreenInfo(screenSize[0], screenSize[1]);
                                    i2 = rect2.bottom - rect2.top;
                                    notchInfo9.setNotchPosition(i2, 8, rect2);
                                    this.mNotchs[3] = notchInfo9;
                                }
                                i = i2;
                                i7++;
                            }
                            return i;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    this.mNotchs[0] = null;
                    i = 0;
                }
                if (!displayCutout.getBoundingRectTop().isEmpty()) {
                    NotchInfo notchInfo10 = new NotchInfo();
                    notchInfo10.setScreenInfo(screenSize[0], screenSize[1]);
                    i = displayCutout.getBoundingRectTop().bottom;
                    notchInfo10.setNotchPosition(i, 2, displayCutout.getBoundingRectTop());
                    this.mNotchs[1] = notchInfo10;
                } else {
                    this.mNotchs[1] = null;
                }
                if (!displayCutout.getBoundingRectRight().isEmpty()) {
                    NotchInfo notchInfo11 = new NotchInfo();
                    notchInfo11.setScreenInfo(screenSize[0], screenSize[1]);
                    i = displayCutout.getBoundingRectRight().right - displayCutout.getBoundingRectRight().left;
                    notchInfo11.setNotchPosition(i, 4, displayCutout.getBoundingRectRight());
                    this.mNotchs[2] = notchInfo11;
                } else {
                    this.mNotchs[2] = null;
                }
                if (!displayCutout.getBoundingRectBottom().isEmpty()) {
                    NotchInfo notchInfo12 = new NotchInfo();
                    notchInfo12.setScreenInfo(screenSize[0], screenSize[1]);
                    int i8 = displayCutout.getBoundingRectBottom().bottom - displayCutout.getBoundingRectBottom().top;
                    notchInfo12.setNotchPosition(i8, 8, displayCutout.getBoundingRectBottom());
                    this.mNotchs[3] = notchInfo12;
                    return i8;
                }
                this.mNotchs[3] = null;
                return i;
            } catch (Throwable th2) {
                th = th2;
                i = 0;
            }
        }
        return 0;
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void addStatusBar(Context context, Window window) {
        if (isNotchHeightValidImpl(context)) {
            new AddStatusBarRunnable(context, window).run();
            return;
        }
        View decorView = getDecorView(window);
        if (decorView != null) {
            decorView.post(new AddStatusBarRunnable(context, window));
        }
    }

    @Override // com.netease.ntunisdk.zxing.notch.NotchDevice
    public void justify(Context context, Window window, NotchDevice.NotchAffectView[] notchAffectViewArr) {
        if (isNotchHeightValidImpl(context)) {
            new SetMarginRunnable(context, window, notchAffectViewArr).run();
            return;
        }
        View decorView = getDecorView(window);
        if (decorView != null) {
            decorView.post(new SetMarginRunnable(context, window, notchAffectViewArr));
        }
    }

    private View getDecorView(Window window) {
        if (window != null) {
            return window.getDecorView();
        }
        return null;
    }

    private class AddStatusBarRunnable implements Runnable {
        private Context context;
        private Window window;

        AddStatusBarRunnable(Context context, Window window) {
            this.context = context;
            this.window = window;
        }

        @Override // java.lang.Runnable
        public void run() {
            NotchImplAndroidP.this.addStatusBarImpl(this.context, this.window);
        }
    }

    private class SetMarginRunnable implements Runnable {
        private Context context;
        private NotchDevice.NotchAffectView[] views;
        private Window window;

        SetMarginRunnable(Context context, Window window, NotchDevice.NotchAffectView[] notchAffectViewArr) {
            this.context = context;
            this.window = window;
            this.views = notchAffectViewArr;
        }

        @Override // java.lang.Runnable
        public void run() {
            NotchImplAndroidP.this.justifyImpl(this.context, this.window, this.views);
        }
    }
}