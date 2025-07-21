package com.netease.mpay.auth.plugins;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
/* loaded from: classes.dex */
public @interface LifeCycleState {
    public static final int ON_CREATE = 1;
    public static final int ON_DESTROY = 6;
    public static final int ON_PAUSE = 4;
    public static final int ON_RESUME = 3;
    public static final int ON_START = 2;
    public static final int ON_STOP = 5;
}