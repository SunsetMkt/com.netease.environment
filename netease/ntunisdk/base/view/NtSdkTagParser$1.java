package com.netease.ntunisdk.base.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/* loaded from: classes5.dex */
class NtSdkTagParser$1 extends ClickableSpan {
    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public final void updateDrawState(TextPaint textPaint) {
    }

    NtSdkTagParser$1() {
    }

    @Override // android.text.style.ClickableSpan
    public final void onClick(View view) {
        if (ntSdkTagParser$OnSpanClickListener != null) {
            NtSdkTagParser$OnSpanClickListener.b();
        }
    }
}