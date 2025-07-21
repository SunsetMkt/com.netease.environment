package com.netease.mpay.ps.codescanner.module;

import com.netease.mpay.ps.codescanner.CodeScannerCallback;
import com.netease.mpay.ps.codescanner.CodeScannerExtCallback;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class QRCodeScannerData {
    public CodeScannerCallback callback;
    public CodeScannerExtCallback extCallback;
    public String extra;
    public String extraUniData;
    public String sdkJsonData;
    public String token;
    public String uid;

    public QRCodeScannerData(String str, String str2, HashMap<String, String> map, String str3, String str4, CodeScannerCallback codeScannerCallback, CodeScannerExtCallback codeScannerExtCallback) {
        this.uid = str;
        this.token = str2;
        this.extra = str3;
        this.extraUniData = str4;
        this.callback = codeScannerCallback;
        this.extCallback = codeScannerExtCallback;
        this.sdkJsonData = "";
        try {
            this.sdkJsonData = new JSONObject(map).toString();
        } catch (Exception unused) {
        }
    }
}