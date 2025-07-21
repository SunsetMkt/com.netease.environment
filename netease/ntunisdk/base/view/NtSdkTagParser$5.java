package com.netease.ntunisdk.base.view;

import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/* loaded from: classes5.dex */
class NtSdkTagParser$5 extends ClickableSpan {
    final /* synthetic */ b b;
    final /* synthetic */ SpannableString c;

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public final void updateDrawState(TextPaint textPaint) {
    }

    NtSdkTagParser$5(b bVar, SpannableString spannableString) {
        bVar = bVar;
        spannableString = spannableString;
    }

    @Override // android.text.style.ClickableSpan
    public final void onClick(View view) {
        NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener = ntSdkTagParser$OnSpanClickListener;
        if (ntSdkTagParser$OnSpanClickListener != null) {
            ntSdkTagParser$OnSpanClickListener.onOpenLinkClicked(bVar.h, NtSdkTagParser$OpenLinkType.convert(bVar.j), spannableString.toString());
        }
    }
}