package com.netease.mpay.auth;

import android.app.Activity;

/* loaded from: classes.dex */
public interface Rule {
    Rule attachActivity(Activity activity);

    void start();
}