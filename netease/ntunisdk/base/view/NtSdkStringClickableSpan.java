package com.netease.ntunisdk.base.view;

import android.text.style.ClickableSpan;
import android.view.View;

/* loaded from: classes5.dex */
public class NtSdkStringClickableSpan extends ClickableSpan {

    /* renamed from: a, reason: collision with root package name */
    private String f1845a;
    private NtSdkTagParser$OnSpanClickListener b;

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
    }

    public NtSdkStringClickableSpan() {
    }

    public NtSdkStringClickableSpan(NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener) {
        this.b = ntSdkTagParser$OnSpanClickListener;
    }

    public String getAction() {
        return this.f1845a;
    }

    public void setAction(String str) {
        this.f1845a = str;
    }

    public NtSdkTagParser$OnSpanClickListener getOnSpanClickListener() {
        return this.b;
    }
}