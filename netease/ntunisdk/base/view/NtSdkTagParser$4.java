package com.netease.ntunisdk.base.view;

import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes5.dex */
class NtSdkTagParser$4 extends ClickableSpan {
    final /* synthetic */ TextView b;
    final /* synthetic */ b c;
    final /* synthetic */ SpannableString d;

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public final void updateDrawState(TextPaint textPaint) {
    }

    NtSdkTagParser$4(TextView textView, b bVar, SpannableString spannableString) {
        textView = textView;
        bVar = bVar;
        spannableString = spannableString;
    }

    @Override // android.text.style.ClickableSpan
    public final void onClick(View view) {
        NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener = ntSdkTagParser$OnSpanClickListener;
        if (ntSdkTagParser$OnSpanClickListener != null) {
            ntSdkTagParser$OnSpanClickListener.onOutLinkClicked(textView.getContext(), bVar.h, spannableString.toString(), bVar.i);
        }
    }
}