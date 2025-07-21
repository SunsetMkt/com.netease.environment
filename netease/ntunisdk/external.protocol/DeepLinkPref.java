package com.netease.ntunisdk.external.protocol;

import android.content.Intent;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.external.protocol.utils.L;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class DeepLinkPref {
    private static final String TAG = "UniDeepLink";
    private static Map<String, String> sPref = new HashMap();

    static void appendKeyValue(String str, String str2) {
        sPref.put(str, str2);
    }

    static void clearKeyValues() {
        L.i(TAG, "clearKeyValue");
        sPref.clear();
    }

    public static Map<String, String> getAllKeyValues() {
        return sPref;
    }

    static void setStartIntent(Intent intent) {
        if (intent != null) {
            appendKeyValue(ConstProp.START_INTENT_URI, intent.toUri(0));
        }
    }

    public static Intent getStartIntent() {
        try {
            return Intent.parseUri(sPref.get(ConstProp.START_INTENT_URI), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}