package com.netease.androidcrashhandler.util;

import android.text.TextUtils;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.init.InitProxy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class TraceFileUtil {
    private static String loading_arch = null;
    private static final int version_json = 1;

    public static String serializationMapping(int i) throws JSONException, IOException {
        String assetFileContent;
        File file = new File(InitProxy.sFilesDir + File.separator + "ntunisdk_so_uuids");
        if (file.exists()) {
            assetFileContent = readFile(file);
        } else {
            assetFileContent = CUtil.getAssetFileContent(NTCrashHunterKit.sharedKit().getContext(), "ntunisdk_so_uuids");
            if (TextUtils.isEmpty(assetFileContent)) {
                assetFileContent = "";
            } else {
                CUtil.str2File(assetFileContent, InitProxy.sFilesDir, "ntunisdk_so_uuids");
            }
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("version", 1);
            jSONObject.putOpt("targetThreadId", Integer.valueOf(i));
        } catch (Throwable th) {
            th.printStackTrace();
            try {
                jSONObject.putOpt("msg", th.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(assetFileContent)) {
            jSONObject.putOpt("msg", "uuid_so file is lost");
            return jSONObject.toString();
        }
        String[] strArrSplit = assetFileContent.split("\n");
        loading_arch = AndroidCrashHandler.getInstance().getSoLoadingType();
        HashMap map = new HashMap();
        for (String str : strArrSplit) {
            Matcher matcher = Pattern.compile("[^\\s]+$").matcher(str);
            String strGroup = matcher.find() ? matcher.group() : "";
            Matcher matcher2 = Pattern.compile(".*.so").matcher(str);
            String strGroup2 = matcher2.find() ? matcher2.group() : "";
            if (matchArch(strGroup2)) {
                Matcher matcher3 = Pattern.compile("[^\\\\|/]*.so").matcher(str);
                if (matcher3.find()) {
                    strGroup2 = matcher3.group().trim();
                }
                map.put(strGroup2, strGroup);
            }
        }
        jSONObject.putOpt("msg", "success");
        jSONObject.putOpt("modules", new JSONObject(map));
        return jSONObject.toString();
    }

    private static String readFile(File file) throws IOException {
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            char[] cArr = new char[1024];
            while (true) {
                int i = bufferedReader.read(cArr, 0, 1024);
                if (i == -1) {
                    break;
                }
                sb.append(cArr, 0, i);
            }
            bufferedReader.close();
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            try {
                th.printStackTrace();
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                return sb.toString();
            } catch (Throwable th3) {
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th3;
            }
        }
        return sb.toString();
    }

    private static boolean matchArch(String str) {
        String str2;
        if (str.contains(loading_arch)) {
            return true;
        }
        str2 = loading_arch;
        str2.hashCode();
        switch (str2) {
            case "x86_64":
                str2 = "x86_64";
                break;
            case "x86":
                str2 = "x86";
                break;
            case "armeabi-v7a":
                str2 = "arm";
                break;
            case "arm64-v8a":
                str2 = "arm64";
                break;
        }
        return str.contains(str2);
    }

    private static class MappingModule {
        String mapping_name;
        String uuid;

        public MappingModule(String str, String str2) {
            this.mapping_name = str;
            this.uuid = str2;
        }
    }
}