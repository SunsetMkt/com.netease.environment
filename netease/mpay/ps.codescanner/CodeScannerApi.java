package com.netease.mpay.ps.codescanner;

import android.app.Activity;
import com.netease.mpay.ps.codescanner.module.QRCodeScannerData;
import com.netease.mpay.ps.codescanner.utils.Logging;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class CodeScannerApi extends CodeScannerApiImpl {
    public CodeScannerApi(Activity activity, String str, String str2, String str3, String str4, String str5) {
        super(activity, str, str2, str3, str4, str5);
        Logging.debug("CodeScannerApi : " + str + ", " + str2);
    }

    public void presentQRCodeScanner(String str, String str2, String str3, HashMap<String, String> map, String str4, String str5, CodeScannerCallback codeScannerCallback, CodeScannerExtCallback codeScannerExtCallback) {
        Logging.debug("presentQRCodeScanner : " + str2 + ", " + str3 + ", " + str4 + ", " + str5 + ", " + str);
        super.presentQRCodeScanner(str, new QRCodeScannerData(str2, str3, map, str4, str5, codeScannerCallback, codeScannerExtCallback));
    }

    @Override // com.netease.mpay.ps.codescanner.CodeScannerApiImpl
    public void notifyOrderFinish(String str, String str2, String str3, int i) {
        Logging.debug("notifyOrderFinish : " + str + ", " + str2 + ", " + str3 + ", " + i);
        super.notifyOrderFinish(str, str2, str3, i);
    }

    @Override // com.netease.mpay.ps.codescanner.CodeScannerApiImpl
    public void setDebugMode(boolean z) {
        Logging.debug("setDebugMode : " + z);
        super.setDebugMode(z);
    }
}