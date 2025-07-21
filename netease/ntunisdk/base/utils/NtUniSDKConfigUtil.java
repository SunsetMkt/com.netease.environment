package com.netease.ntunisdk.base.utils;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class NtUniSDKConfigUtil {
    private static final String TAG = "NtUniSDKConfigUtil: ";

    public interface SdkBaseHandle {
        void doConfigVal(JSONObject jSONObject, String str, boolean z);

        void setPropStr(String str, String str2);
    }

    private static InputStream getAssetStream(Context context, String str) {
        try {
            return context.getAssets().open(str, 3);
        } catch (Exception unused) {
            return null;
        }
    }

    private static String readString(InputStream inputStream) throws IOException {
        try {
            try {
                int iAvailable = inputStream.available();
                if (iAvailable == 0) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException unused) {
                        }
                    }
                    return null;
                }
                byte[] bArr = new byte[iAvailable];
                inputStream.read(bArr);
                String str = new String(bArr, "UTF-8");
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused2) {
                    }
                }
                return str;
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            UniSdkUtils.i(TAG, e.getMessage());
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException unused4) {
                }
            }
            return null;
        }
    }

    private static void setPropStrAll(JSONObject jSONObject, SdkBaseHandle sdkBaseHandle) {
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            String strOptString = jSONObject.optString(next);
            if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(strOptString)) {
                sdkBaseHandle.setPropStr(next, strOptString);
            }
        }
    }

    private static void doConfigValAll(JSONObject jSONObject, SdkBaseHandle sdkBaseHandle) {
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (!TextUtils.isEmpty(next)) {
                sdkBaseHandle.doConfigVal(jSONObject, next, false);
            }
        }
    }

    public static void readLibraryConfig(Context context, SdkBaseHandle sdkBaseHandle, String str) {
        readLibraryConfigImpl(context, sdkBaseHandle, str);
        readNtUniSDKGameConfigImpl(context, sdkBaseHandle, str);
    }

    private static void readLibraryConfigImpl(Context context, SdkBaseHandle sdkBaseHandle, String str) throws IOException {
        String str2 = "ntunisdk_config";
        InputStream assetStream = getAssetStream(context, "ntunisdk_config");
        if (assetStream == null) {
            UniSdkUtils.i(str, "NtUniSDKConfigUtil: fail to read ntunisdk_config, try ntunisdk.cfg");
            str2 = "ntunisdk.cfg";
            assetStream = getAssetStream(context, "ntunisdk.cfg");
        }
        if (assetStream == null) {
            UniSdkUtils.d(str, "NtUniSDKConfigUtil: ntunisdk_config/ntunisdk.cfg null");
            return;
        }
        String string = readString(assetStream);
        if (string == null) {
            UniSdkUtils.d(str, TAG + str2 + " is null");
            return;
        }
        UniSdkUtils.d(str, "NtUniSDKConfigUtil: readLibraryConfigImpl: ".concat(String.valueOf(string)));
        if (string.contains("\uff1a") || string.contains("\u201c") || string.contains("\u201d")) {
            UniSdkUtils.e(str, TAG + str2 + "\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26");
        }
        try {
            doConfigValAll(new JSONObject(string), sdkBaseHandle);
        } catch (JSONException unused) {
            UniSdkUtils.i(str, "NtUniSDKConfigUtil: ntunisdk_config/ntunisdk.cfg config parse to json error");
        }
    }

    private static void readNtUniSDKGameConfigImpl(Context context, SdkBaseHandle sdkBaseHandle, String str) throws IOException {
        FileInputStream fileInputStreamOpenFileInput;
        try {
            fileInputStreamOpenFileInput = context.openFileInput("ntunisdk_game_config");
        } catch (Exception unused) {
            fileInputStreamOpenFileInput = null;
        }
        if (fileInputStreamOpenFileInput == null) {
            UniSdkUtils.d(str, "NtUniSDKConfigUtil: ntunisdk_game_config is null");
            return;
        }
        String string = readString(fileInputStreamOpenFileInput);
        if (string == null) {
            UniSdkUtils.d(str, "NtUniSDKConfigUtil: ntunisdk_game_config is null");
            return;
        }
        UniSdkUtils.d(str, "NtUniSDKConfigUtil: readNtUniSDKGameConfigImpl: ".concat(String.valueOf(string)));
        if (string.contains("\uff1a") || string.contains("\u201c") || string.contains("\u201d")) {
            UniSdkUtils.e(str, "NtUniSDKConfigUtil: ntunisdk_game_config\u5305\u542b\u4e2d\u6587\u7279\u6b8a\u5b57\u7b26");
        }
        try {
            setPropStrAll(new JSONObject(string), sdkBaseHandle);
        } catch (JSONException unused2) {
            UniSdkUtils.i(str, "NtUniSDKConfigUtil: ntunisdk_game_config config parse to json error");
        }
    }
}