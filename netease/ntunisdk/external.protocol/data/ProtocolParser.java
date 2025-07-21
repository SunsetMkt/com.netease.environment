package com.netease.ntunisdk.external.protocol.data;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.data.Config;
import com.netease.ntunisdk.external.protocol.utils.FileUtil;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.jose4j.jwx.HeaderParameterNames;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ProtocolParser {
    private static final String TAG = "ProtocolParser";

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00db  */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.netease.ntunisdk.external.protocol.data.ProtocolInfo parseByJson(java.lang.String r18, org.json.JSONObject r19, boolean r20) {
        /*
            Method dump skipped, instructions count: 423
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.external.protocol.data.ProtocolParser.parseByJson(java.lang.String, org.json.JSONObject, boolean):com.netease.ntunisdk.external.protocol.data.ProtocolInfo");
    }

    public static ProtocolInfo readLocalProtocol(ProtocolFile protocolFile, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (SDKRuntime.getInstance().hasInMemory(str)) {
            return SDKRuntime.getInstance().getProtocol(str);
        }
        String strLoadRawContent = loadRawContent(protocolFile, str, TextCompat.md5(str));
        boolean z = !TextUtils.isEmpty(strLoadRawContent);
        if (TextUtils.isEmpty(strLoadRawContent)) {
            return null;
        }
        L.d(TAG, "local-->url: " + str + "\n" + strLoadRawContent);
        try {
            ProtocolInfo byJson = parseByJson(str, new JSONObject(strLoadRawContent), z);
            byJson.setProtocolFile(protocolFile);
            return loadProtocolContent(protocolFile, byJson, z);
        } catch (Exception unused) {
            return null;
        }
    }

    private static ProtocolInfo loadProtocolContent(ProtocolFile protocolFile, ProtocolInfo protocolInfo, boolean z) throws Throwable {
        File file = protocolFile.cacheDir;
        String strLoadBase64Content = loadBase64Content(file, protocolInfo.textUrl);
        ProtocolProp prop = ProtocolManager.getInstance().getProp();
        if (!TextUtils.isEmpty(strLoadBase64Content)) {
            protocolInfo.text = ProtocolTextHandler.handle(strLoadBase64Content, prop.getIssuer(), prop.getGameName());
        } else {
            protocolInfo.text = "";
        }
        String strLoadBase64Content2 = loadBase64Content(file, protocolInfo.updateTextUrl);
        if (!TextUtils.isEmpty(strLoadBase64Content2)) {
            protocolInfo.updateText = ProtocolTextHandler.handle(strLoadBase64Content2, prop.getIssuer(), prop.getGameName());
        }
        protocolInfo.reviewText = strLoadBase64Content;
        protocolInfo.isLocal = !TextUtils.isEmpty(protocolInfo.text);
        return protocolInfo;
    }

    private static String loadBase64Content(File file, String str) throws Throwable {
        String str2;
        String file2 = FileUtil.readFile(new File(file, TextCompat.md5(str)).getPath(), "UTF-8");
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                str2 = new String(Base64.decode(file2, 0), StandardCharsets.UTF_8);
            } else {
                str2 = new String(Base64.decode(file2, 0), "UTF-8");
            }
            return str2;
        } catch (Exception unused) {
            return null;
        }
    }

    private static String loadRawContent(ProtocolFile protocolFile, String str, String str2) {
        File file = new File(protocolFile.cacheDir, str2);
        L.d(TAG, "start to load cache protocol file:" + str + ", file:" + file);
        return FileUtil.readFile(file.getPath(), "UTF-8");
    }

    public static Config parseConfig(JSONObject jSONObject) {
        JSONObject jSONObjectOptJSONObject;
        if (jSONObject == null || (jSONObjectOptJSONObject = jSONObject.optJSONObject("data")) == null) {
            return null;
        }
        Config config = new Config();
        config.defaultLanguage = jSONObjectOptJSONObject.optString("default_language", "");
        JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("url_list");
        if (jSONArrayOptJSONArray != null) {
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject2 != null) {
                    Config.ProtocolConfig protocolConfig = new Config.ProtocolConfig();
                    String strOptString = jSONObjectOptJSONObject2.optString("url");
                    if (!TextUtils.isEmpty(strOptString)) {
                        protocolConfig.url = strOptString;
                        protocolConfig.id = jSONObjectOptJSONObject2.optInt(ResIdReader.RES_TYPE_ID);
                        protocolConfig.tag = jSONObjectOptJSONObject2.optString(HeaderParameterNames.AUTHENTICATION_TAG);
                        protocolConfig.language = jSONObjectOptJSONObject2.optString("language");
                        protocolConfig.isLauncherShow = jSONObjectOptJSONObject2.optBoolean("launch_show", false);
                        JSONArray jSONArrayOptJSONArray2 = jSONObjectOptJSONObject2.optJSONArray(Const.COUNTRY);
                        if (jSONArrayOptJSONArray2 != null) {
                            for (int i2 = 0; i2 < jSONArrayOptJSONArray2.length(); i2++) {
                                String strOptString2 = jSONArrayOptJSONArray2.optString(i2);
                                if (!TextUtils.isEmpty(strOptString2)) {
                                    protocolConfig.countries.add(strOptString2);
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(config.defaultLanguage) && config.defaultLanguage.equals(protocolConfig.language)) {
                            config.defaultProtocolConfig = protocolConfig;
                        }
                        config.protocolConfigs.add(protocolConfig);
                    }
                }
            }
        }
        return config;
    }
}