package com.netease.ntunisdk.base.view;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes5.dex */
public class NtSdkTagParser$OnSpanClickListener {
    protected static void b() {
        UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickListener: onRealnameClicked");
    }

    protected void a() {
        UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickListener: onFFRulesClicked");
    }

    protected void c() {
        UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickListener: onUrsRealnameClicked");
    }

    public boolean onOutLinkClicked(Context context, String str, String str2, String str3) {
        UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickListener: onOutLinkClicked: " + str + " " + str3 + " " + str2);
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return ViewUtils.openBrowser(context, str);
    }

    public boolean onOutlinkHref_2Clicked(String str, String str2) {
        UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickListener: onOutlinkHref_2Clicked: " + str + " " + str2);
        return false;
    }

    public boolean onOpenLinkClicked(String str, int i, String str2) {
        UniSdkUtils.d("UniSDK NtSdkTagParser", "OnSpanClickListener: onOpenLinkClicked: " + str + ", openType: " + i + ", " + str2);
        return false;
    }
}