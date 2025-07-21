package com.netease.ntunisdk.base.view;

import android.widget.TextView;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes5.dex */
public class NtSdkTagParser$RichTextView {

    /* renamed from: a, reason: collision with root package name */
    TextView f1851a;
    NtSdkTagParser$OnSpanClickListener b;
    NtSdkStringClickableSpan c;

    public NtSdkTagParser$RichTextView(TextView textView, NtSdkStringClickableSpan ntSdkStringClickableSpan) {
        this.f1851a = textView;
        if (ntSdkStringClickableSpan != null) {
            this.c = ntSdkStringClickableSpan;
            this.b = ntSdkStringClickableSpan.getOnSpanClickListener();
        }
        if (this.b == null) {
            this.b = new NtSdkTagParser$OnSpanClickListener() { // from class: com.netease.ntunisdk.base.view.NtSdkTagParser$OnSpanClickWithOutLinkListener
                @Override // com.netease.ntunisdk.base.view.NtSdkTagParser$OnSpanClickListener
                protected final void c() {
                    UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickWithOutLinkListener: onUrsRealnameClicked");
                }
            };
        }
    }
}