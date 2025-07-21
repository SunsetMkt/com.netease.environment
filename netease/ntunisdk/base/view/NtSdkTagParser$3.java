package com.netease.ntunisdk.base.view;

import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/* loaded from: classes5.dex */
class NtSdkTagParser$3 extends ClickableSpan {
    final /* synthetic */ SpannableString b;

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public final void updateDrawState(TextPaint textPaint) {
    }

    NtSdkTagParser$3(SpannableString spannableString) {
        spannableString = spannableString;
    }

    @Override // android.text.style.ClickableSpan
    public final void onClick(View view) {
        NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener = ntSdkTagParser$OnSpanClickListener;
        if (ntSdkTagParser$OnSpanClickListener != null) {
            ntSdkTagParser$OnSpanClickListener.c();
        }
    }
}