package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes.dex */
public abstract class NotchDevice {
    protected static final int NOTCH_HEIGHT_NONE = 0;
    protected static final int NOTCH_UNINITIALIZED = -1;
    public static NotchDevice sInstance;
    protected NotchInfo[] mNotchs = {null, null, null, null};
    private int mNotchHeight = -1;
    private int lastOrientation = -1;

    public abstract void addStatusBar(Context context, Window window);

    protected abstract boolean checkModel(Context context);

    protected abstract int initNotchHeight(Context context, Window window);

    public abstract void justify(Context context, Window window, NotchAffectView[] notchAffectViewArr);

    public static synchronized NotchDevice getInstance(Context context) {
        if (sInstance == null) {
            if (Build.VERSION.SDK_INT >= 26) {
                int i = 0;
                NotchDevice[] notchDeviceArr = {new NotchImplAndroidP(), new NotchImplHuaWei(), new NotchImplMiui(), new NotchImplOppo(), new NotchImplVivo(), new NotchImplSamsung(), new NotchImplNone()};
                while (true) {
                    if (i >= 7) {
                        break;
                    }
                    NotchDevice notchDevice = notchDeviceArr[i];
                    if (notchDevice.checkModel(context)) {
                        sInstance = notchDevice;
                        break;
                    }
                    i++;
                }
            } else {
                sInstance = new NotchImplNone();
            }
        }
        return sInstance;
    }

    public static int[] getScreenSize(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
        } catch (Exception unused) {
            return new int[]{0, 0};
        }
    }

    protected final int getNotchHeightImpl(Context context, Window window) {
        if (!isNotchHeightValidImpl(context)) {
            this.lastOrientation = context.getResources().getConfiguration().orientation;
            this.mNotchs = new NotchInfo[]{null, null, null, null};
            this.mNotchHeight = initNotchHeight(context, window);
        }
        return this.mNotchHeight;
    }

    protected final void addStatusBarImpl(Context context, Window window) {
        if (getNotchHeightImpl(context, window) > 0) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags &= -1025;
            window.setAttributes(attributes);
        }
    }

    protected final void justifyImpl(Context context, Window window, NotchAffectView[] notchAffectViewArr) {
        if (getNotchHeightImpl(context, window) > 0) {
            for (NotchAffectView notchAffectView : notchAffectViewArr) {
                if (notchAffectView != null && notchAffectView.affectView != null) {
                    notchAffectView.justNotch(this.mNotchs);
                }
            }
        }
    }

    protected final boolean isNotchHeightValidImpl(Context context) {
        return this.mNotchHeight != -1 && this.lastOrientation == context.getResources().getConfiguration().orientation;
    }

    public static class NotchAffectView {
        public static final int LAYOUT_CENTER = 1;
        public static final int LAYOUT_LEFT = 2;
        public static final int LAYOUT_RIGHT = 3;
        public static final int NOT_MIN_HEIGHT = -1;
        View affectView;
        boolean inPaddingMode;
        boolean isLandscape;
        boolean isTopView;
        int layout;
        int minHeight;

        public NotchAffectView(View view, boolean z, boolean z2, boolean z3, int i) {
            this.affectView = view;
            this.inPaddingMode = z2;
            this.isLandscape = z3;
            this.layout = i;
            this.isTopView = z;
            this.minHeight = -1;
        }

        public NotchAffectView(View view, boolean z, boolean z2, boolean z3, int i, int i2) {
            this.affectView = view;
            this.inPaddingMode = z2;
            this.isLandscape = z3;
            this.layout = i;
            this.isTopView = z;
            this.minHeight = i2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0057  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void justNotch(com.netease.ntunisdk.zxing.notch.NotchInfo[] r10) {
            /*
                Method dump skipped, instructions count: 320
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.zxing.notch.NotchDevice.NotchAffectView.justNotch(com.netease.ntunisdk.zxing.notch.NotchInfo[]):void");
        }

        private void changeHeight(int i) {
            ViewGroup.LayoutParams layoutParams = this.affectView.getLayoutParams();
            int height = this.affectView.getHeight();
            int i2 = this.minHeight;
            if (i2 == -1 || height >= i + i2) {
                return;
            }
            layoutParams.height = i + i2;
            this.affectView.setLayoutParams(layoutParams);
        }

        private void changeSpace(int i, int i2, int i3, int i4) {
            UniSdkUtils.d("notch", "inPaddingMode:" + this.inPaddingMode + ",left:" + i + ", right:" + i2 + ", top:" + i3 + ",bottom:" + i4);
            if (i == 0 && i2 == 0 && i3 == 0 && i4 == 0) {
                return;
            }
            if (i > 500 || i3 > 500) {
                i = 0;
                i3 = 100;
            }
            if (this.inPaddingMode) {
                setPadding(i, i3, i2, i4);
            } else {
                setMargin(i, i3, i2, i4);
            }
        }

        private void setPadding(int i, int i2, int i3, int i4) {
            if (this.affectView.getPaddingLeft() < i || this.affectView.getPaddingBottom() < i4 || this.affectView.getPaddingTop() < i2 || this.affectView.getPaddingRight() < i3) {
                View view = this.affectView;
                view.setPadding(i + (view.getPaddingLeft() / 2), i2 + this.affectView.getPaddingTop(), i3 + (this.affectView.getPaddingRight() / 2), i4 + this.affectView.getPaddingBottom());
            }
        }

        private void setMargin(int i, int i2, int i3, int i4) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.affectView.getLayoutParams();
            marginLayoutParams.setMargins(i + marginLayoutParams.leftMargin, i2 + marginLayoutParams.topMargin, i3 + marginLayoutParams.rightMargin, i4 + marginLayoutParams.bottomMargin);
        }
    }
}