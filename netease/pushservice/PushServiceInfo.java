package com.netease.pushservice;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import com.netease.pushclient.UnisdkDeviceUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import org.json.JSONException;

/* loaded from: classes4.dex */
public class PushServiceInfo {
    private static final String TAG = "NGPush_" + PushServiceInfo.class.getSimpleName();
    public static String EB = "";
    private static String EBconnectUrl = "sdkgate.pushv3.easebar.com:25003";
    public String mPushSrv = "sdkgate.pushv3.netease.com:25003";
    public String mDevId = "";
    private boolean mbReset = false;
    private char[] charArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public void resetUUID() {
        this.mbReset = true;
        this.mDevId = "";
    }

    public void setPushSrv(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mPushSrv = str;
    }

    public String getPushSrv() {
        return this.mPushSrv;
    }

    public String getConnectUrl(Context context) throws IllegalAccessException, JSONException, IOException, IllegalArgumentException, InvocationTargetException {
        readLibraryConfig(context);
        PushLog.d(TAG, "getConnectUrl EB--->" + EB);
        if (!TextUtils.isEmpty(PushSetting.getVaule(context, "niepushAddr"))) {
            return PushSetting.getVaule(context, "niepushAddr");
        }
        if (!"".equals(EB) && "1".equals(EB)) {
            return EBconnectUrl;
        }
        return this.mPushSrv;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0025 A[Catch: all -> 0x0020, IOException -> 0x0052, TRY_LEAVE, TryCatch #6 {IOException -> 0x0052, blocks: (B:7:0x0019, B:11:0x0025, B:15:0x0032, B:20:0x003e), top: B:69:0x0019, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0032 A[Catch: all -> 0x0020, IOException -> 0x0052, TRY_ENTER, TRY_LEAVE, TryCatch #6 {IOException -> 0x0052, blocks: (B:7:0x0019, B:11:0x0025, B:15:0x0032, B:20:0x003e), top: B:69:0x0019, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void readLibraryConfig(android.content.Context r6) throws java.lang.IllegalAccessException, org.json.JSONException, java.io.IOException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 242
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushservice.PushServiceInfo.readLibraryConfig(android.content.Context):void");
    }

    public String createUUID(Context context) {
        return createSpecialUUID(context);
    }

    private String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String createSpecialUUID(Context context) throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        int length = this.charArray.length;
        StringBuilder sb2 = new StringBuilder();
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            long j = length;
            sb2.append(this.charArray[(int) (jCurrentTimeMillis % j)]);
            jCurrentTimeMillis /= j;
        }
        String string = sb2.reverse().toString();
        if (TextUtils.isEmpty(string)) {
            string = createRandom(8, this.charArray);
        }
        appendString(sb, string, 8);
        String strCreateRandom = Build.MODEL;
        if (TextUtils.isEmpty(strCreateRandom)) {
            strCreateRandom = createRandom(6, this.charArray);
        }
        appendString(sb, strCreateRandom, 6);
        String unisdkAndroidId = UnisdkDeviceUtil.getUnisdkAndroidId(context);
        if (TextUtils.isEmpty(unisdkAndroidId)) {
            unisdkAndroidId = createRandom(6, this.charArray);
        }
        PushLog.i(TAG, "createSpecialUUID1:" + unisdkAndroidId);
        appendString(sb, unisdkAndroidId, 6);
        appendString(sb, createRandom(12, this.charArray), 12);
        PushLog.i(TAG, "createSpecialUUID2:" + ((Object) sb));
        return sb.toString();
    }

    private void appendString(StringBuilder sb, String str, int i) {
        int iAbs = Math.abs(i);
        String strReplaceAll = str.toLowerCase().replaceAll("\\W+", "");
        if (strReplaceAll.length() > iAbs) {
            if (i > 0) {
                strReplaceAll = strReplaceAll.substring(0, iAbs);
            } else {
                strReplaceAll = strReplaceAll.substring(strReplaceAll.length() - iAbs);
            }
        }
        StringBuilder sb2 = new StringBuilder(strReplaceAll);
        Random random = new Random();
        for (int i2 = 0; i2 < iAbs; i2++) {
            if (i2 == strReplaceAll.length()) {
                sb2.append('_');
            } else if (i2 > strReplaceAll.length()) {
                char[] cArr = this.charArray;
                sb2.append(cArr[random.nextInt(cArr.length)]);
            } else if (Arrays.binarySearch(this.charArray, strReplaceAll.charAt(i2)) < 0) {
                char[] cArr2 = this.charArray;
                sb2.setCharAt(i2, cArr2[random.nextInt(cArr2.length)]);
            }
        }
        sb.append(sb2.subSequence(0, iAbs));
    }

    private String createRandom(int i, char[] cArr) {
        int length = cArr.length;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(cArr[random.nextInt(length)]);
        }
        return sb.toString();
    }
}