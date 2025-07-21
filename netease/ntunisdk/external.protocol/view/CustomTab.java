package com.netease.ntunisdk.external.protocol.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import androidx.browser.customtabs.CustomTabsIntent;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.utils.L;

/* loaded from: classes.dex */
public class CustomTab {
    private final Uri uri;

    public CustomTab(String str) {
        this.uri = Uri.parse(str);
    }

    public boolean openCustomTab(Activity activity) {
        String chromePackage = SDKRuntime.getInstance().getChromePackage(activity);
        CustomTabPrefetchHelper.mayLaunchUrl(this.uri);
        CustomTabsIntent customTabsIntentBuild = new CustomTabsIntent.Builder(CustomTabPrefetchHelper.getPreparedSessionOnce()).setUrlBarHidingEnabled(true).setInstantAppsEnabled(true).setShowTitle(false).build();
        if (!TextUtils.isEmpty(chromePackage)) {
            L.d("open browser:" + chromePackage);
            customTabsIntentBuild.intent.setPackage(chromePackage);
        }
        try {
            customTabsIntentBuild.launchUrl(activity, this.uri);
            return true;
        } catch (ActivityNotFoundException unused) {
            return false;
        }
    }
}