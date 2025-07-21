package com.netease.ntunisdk.zxing.client.android;

import android.content.Intent;
import android.os.Bundle;
import com.google.zxing.DecodeHintType;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.EnumMap;
import java.util.Map;

/* loaded from: classes.dex */
final class DecodeHintManager {
    private static final String TAG = "UniQR hint";

    private DecodeHintManager() {
    }

    static Map<DecodeHintType, Object> parseDecodeHints(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null || extras.isEmpty()) {
            return null;
        }
        EnumMap enumMap = new EnumMap(DecodeHintType.class);
        for (DecodeHintType decodeHintType : DecodeHintType.values()) {
            if (decodeHintType != DecodeHintType.CHARACTER_SET && decodeHintType != DecodeHintType.NEED_RESULT_POINT_CALLBACK && decodeHintType != DecodeHintType.POSSIBLE_FORMATS) {
                String strName = decodeHintType.name();
                if (extras.containsKey(strName)) {
                    if (decodeHintType.getValueType().equals(Void.class)) {
                        enumMap.put((EnumMap) decodeHintType, (DecodeHintType) Boolean.TRUE);
                    } else {
                        Object obj = extras.get(strName);
                        if (decodeHintType.getValueType().isInstance(obj)) {
                            enumMap.put((EnumMap) decodeHintType, (DecodeHintType) obj);
                        } else {
                            UniSdkUtils.w(TAG, "Ignoring hint " + decodeHintType + " because it is not assignable from " + obj);
                        }
                    }
                }
            }
        }
        UniSdkUtils.i(TAG, "Hints from the Intent: " + enumMap);
        return enumMap;
    }
}