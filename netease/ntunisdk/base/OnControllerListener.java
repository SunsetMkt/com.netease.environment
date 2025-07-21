package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnControllerListener {
    void onKeyDown(int i, PadEvent padEvent);

    void onKeyPressure(int i, float f, PadEvent padEvent);

    void onKeyUp(int i, PadEvent padEvent);

    void onLeftStick(float f, float f2, PadEvent padEvent);

    void onRightStick(float f, float f2, PadEvent padEvent);

    void onStateEvent(PadEvent padEvent);
}