package com.netease.ntunisdk.zxing.notch;

import android.graphics.Rect;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public class NotchInfo {
    public static final int BOTTOM = 8;
    public static final int CENTER = 48;
    public static final int CENTER_H = 16;
    public static final int CENTER_V = 32;
    public static final int LEFT = 1;
    protected static final int NOTCH_HEIGHT_NONE = 0;
    protected static final int NOTCH_UNINITIALIZED = -1;
    public static final int RIGHT = 4;
    public static final int TOP = 2;
    public Rect mBoundingRect = new Rect(0, 0, 0, 0);
    public int mNotchHeight = -1;
    private int location = 0;
    private int centerX = -1;
    private int centerY = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Location {
    }

    public void setScreenInfo(int i, int i2) {
        this.centerX = i / 2;
        this.centerY = i2 / 2;
    }

    public void setNotchPosition(int i, int i2, Rect rect) {
        this.mBoundingRect = rect;
        this.mNotchHeight = i;
        setPosition(rect, i2);
    }

    private void setPosition(Rect rect, int i) {
        if (rect.isEmpty()) {
            return;
        }
        this.location = getRelativePosition(rect) | i | this.location;
    }

    private int getRelativePosition(Rect rect) {
        int i;
        int i2 = (rect.right + rect.left) / 2;
        int i3 = (rect.bottom + rect.top) / 2;
        if (i2 <= 0) {
            i = 0;
        } else if (Math.abs(this.centerX - i2) < 100) {
            i = 16;
        } else {
            i = i2 < this.centerX ? 1 : 4;
        }
        if (i3 > 0) {
            if (Math.abs(this.centerY - i3) < 100) {
                i |= 32;
            } else {
                i = i3 < this.centerY ? i | 2 : i | 8;
            }
        }
        if (i == 0) {
            return 48;
        }
        return i;
    }

    public boolean isAtTop() {
        return (this.location & 2) == 2;
    }

    public boolean isAtLeft() {
        return (this.location & 1) == 1;
    }

    public boolean isAtBottom() {
        return (this.location & 8) == 8;
    }

    public boolean isAtCenter() {
        return (this.location & 48) == 48;
    }

    public boolean isAtCenterHorizontal() {
        return (this.location & 16) == 16;
    }

    public boolean isAtCenterVertical() {
        return (this.location & 32) == 32;
    }
}