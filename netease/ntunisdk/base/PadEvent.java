package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public class PadEvent {
    public static final int ACTION_BLUETOOTH_POWEROFF = 10;
    public static final int ACTION_BLUETOOTH_RESETTING = 7;
    public static final int ACTION_BLUETOOTH_UNAUTHORIZED = 9;
    public static final int ACTION_BLUETOOTH_UNKNOWN = 6;
    public static final int ACTION_BLUETOOTH_UNSUPPORTED = 8;
    public static final int ACTION_CONNECTED = 1;
    public static final int ACTION_CONNECTING = 2;
    public static final int ACTION_CONNECT_FAILED = 3;
    public static final int ACTION_DISCONNECTED = 0;
    public static final int ACTION_DOWN = 0;
    public static final int ACTION_GCM_CONNECTED = 101;
    public static final int ACTION_GCM_DISCONNECTED = 102;
    public static final int ACTION_GCM_NO_SUPPORTED = 103;
    public static final int ACTION_GCM_SEARCH_END = 104;
    public static final int ACTION_HID_CONNECTED = 111;
    public static final int ACTION_HID_DISCONNECTED = 112;
    public static final int ACTION_KEYCODE = 11;
    public static final int ACTION_PRESSURE = -1;
    public static final int ACTION_SCANING = 4;
    public static final int ACTION_SCAN_TIMEOUT = 5;
    public static final int ACTION_SPP_CONNECTED = 121;
    public static final int ACTION_SPP_DISCONNECTED = 122;
    public static final int ACTION_UP = 1;
    public static final int KEYCODE_BACK = 4;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_HELP = 198;
    public static final int KEYCODE_BUTTON_KEYBOARD = 199;
    public static final int KEYCODE_BUTTON_L1 = 102;
    public static final int KEYCODE_BUTTON_L2 = 104;
    public static final int KEYCODE_BUTTON_R1 = 103;
    public static final int KEYCODE_BUTTON_R2 = 105;
    public static final int KEYCODE_BUTTON_START = 108;
    public static final int KEYCODE_BUTTON_THUMBL = 106;
    public static final int KEYCODE_BUTTON_THUMBR = 107;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_DPAD_DOWN = 20;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22;
    public static final int KEYCODE_DPAD_UP = 19;
    public static final int KEYCODE_LEFT_STICK = 200;
    public static final int KEYCODE_RIGHT_STICK = 201;

    /* renamed from: a, reason: collision with root package name */
    private long f1610a;
    private int b;
    private int c;
    private int d;
    private float e;
    private int f;
    private float g;
    private float h;
    private int i;
    private int j;

    public long getEventTime() {
        return this.f1610a;
    }

    public int getDeviceId() {
        return this.b;
    }

    public int getKeyCode() {
        return this.c;
    }

    public int getAction() {
        return this.d;
    }

    public float getPressure() {
        return this.e;
    }

    public int getMotionKeyCode() {
        return this.f;
    }

    public float getX() {
        return this.g;
    }

    public float getY() {
        return this.h;
    }

    public int getState() {
        return this.i;
    }

    public int getStateAction() {
        return this.j;
    }

    public PadEvent(long j, int i) {
        this.f1610a = j;
        this.b = i;
    }

    public PadEvent(long j, int i, int i2, int i3, float f) {
        this.f1610a = j;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = f;
    }

    public PadEvent(long j, int i, int i2, float f, float f2) {
        this.f1610a = j;
        this.b = i;
        this.f = i2;
        this.g = f;
        this.h = f2;
    }

    public PadEvent(long j, int i, int i2, int i3) {
        this.f1610a = j;
        this.b = i;
        this.i = i2;
        this.j = i3;
    }
}