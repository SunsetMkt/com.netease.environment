package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.google.zxing.WriterException;
import com.netease.ntsharesdk.ShareArgs;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OnCodeScannerListener;
import com.netease.ntunisdk.base.OnFinishInitListener;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.ShareInfo;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.Crypto;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.langutil.LanguageUtil;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.qrcode.QrCodeDownLoadUtil;
import com.netease.ntunisdk.zxing.client.android.CaptureActivity;
import com.netease.ntunisdk.zxing.encoding.EncodingHandler;
import com.netease.ntunisdk.zxing.utils.Util;
import com.tencent.open.SocialConstants;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SdkQRCode extends SdkBase {
    public static final String DISABLE_QUICK_QR_PAY = "DISABLE_QUICK_QR_PAY";
    public static final String ENABLE_POPUP_QR_PIC = "ENABLE_POPUP_QR_PIC";
    public static final String ENABLE_UNISDK_PERMISSION_TIPS = "ENABLE_UNISDK_PERMISSION_TIPS";
    public static final String ENABLE_UNISDK_PERMISSION_UI = "ENABLE_UNISDK_PERMISSION_UI";
    public static final String HIDE_QRCODE_ALBUM_BTN = "HIDE_QRCODE_ALBUM_BTN";
    public static final String HIDE_QRCODE_FLASH_BTN = "HIDE_QRCODE_FLASH_BTN";
    public static final boolean IS_TEST = false;
    private static final int PERMISSIONS_REQUEST_CAMERA = 31125;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 31126;
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    public static final String QRCODE_SCAN_TIPS = "QRCODE_SCAN_TIPS";
    private static final String QR_SDK_VERSION = "1.5.3";
    private static final String QR_UNISDK_VERSION = "1.5.3";
    public static final String QUICK_QR_CONFIG = "QUICK_QR_CONFIG";
    public static final String REMOVE_JUMP_ALBUM = "REMOVE_JUMP_ALBUM";
    private static final int REQUEST_CODE_CAPTURE_ACTIVITY;
    private static final int REQUEST_CODE_START_PICK_IMAGE;
    public static final String TAG = "UniQR sdk";
    public static final String TURN_OFF_SENSOR = "TURN_OFF_SENSOR";
    private static final String UNISDK_QRCODE = "unisdk_qrcode";
    public static final String UNISDK_QRCODE_CAMERA_PERMISSION_TIPS = "UNISDK_QRCODE_CAMERA_PERMISSION_TIPS";
    public static final String UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS = "UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS";
    public static final String UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS = "UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS";
    public static final String UNI_QRCODE_BACKGROUND_COLOR = "UNI_QRCODE_BACKGROUND_COLOR";
    public static final String UNI_QRCODE_FOREGROUND_COLOR = "UNI_QRCODE_FOREGROUND_COLOR";
    public static final String UNI_QRCODE_LEVEL = "UNI_QRCODE_LEVEL";
    private OnCodeScannerListener unisdkCodeScannerListener;

    @Override // com.netease.ntunisdk.base.SdkBase
    public void checkOrder(OrderInfo orderInfo) {
    }

    @Override // com.netease.ntunisdk.base.SdkBase, com.netease.ntunisdk.base.GamerInterface
    public String getChannel() {
        return UNISDK_QRCODE;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginSession() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getLoginUid() {
        return null;
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public String getSDKVersion() {
        return "1.5.3";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected String getUniSDKVersion() {
        return "1.5.3";
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void login() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void logout() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void openManager() {
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void upLoadUserInfo() {
    }

    static {
        int iAbs = Math.abs(515164811) & 65535;
        REQUEST_CODE_CAPTURE_ACTIVITY = iAbs;
        REQUEST_CODE_START_PICK_IMAGE = Math.abs(iAbs + 1) & 65535;
    }

    public SdkQRCode(Context context) {
        super(context);
        setPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL, 1);
        setPropInt(ConstProp.INNER_MODE_NO_PAY, 1);
    }

    public void setUniSDKCodeScannerListener(OnCodeScannerListener onCodeScannerListener) {
        this.unisdkCodeScannerListener = onCodeScannerListener;
    }

    private void unisdkCodeScannerDone(final int i, final String str) {
        if (this.unisdkCodeScannerListener == null) {
            UniSdkUtils.e(TAG, "unisdkCodeScannerListener not set");
        } else {
            UniSdkUtils.i(TAG, "unisdkCodeScannerDone");
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.SdkQRCode.1
                @Override // java.lang.Runnable
                public void run() {
                    UniSdkUtils.i(SdkQRCode.TAG, "runOnUiThread unisdkCodeScannerDone");
                    if (SdkQRCode.this.unisdkCodeScannerListener != null) {
                        SdkQRCode.this.unisdkCodeScannerListener.codeScannerFinish(i, str);
                        SdkQRCode.this.unisdkCodeScannerListener = null;
                    }
                }
            });
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void codeScannerDone(int i, String str) throws JSONException {
        if (!TextUtils.isEmpty(str) && str.startsWith("unisdkqrcode:")) {
            UniSdkUtils.d(TAG, "unisdkqrcode\uff1a" + str);
            restoreIndex(str.substring(13));
            return;
        }
        if (this.unisdkCodeScannerListener == null) {
            UniSdkUtils.d(TAG, "codeScannerDone");
            super.codeScannerDone(i, str);
        } else {
            UniSdkUtils.d(TAG, "channel codeScannerDone");
            unisdkCodeScannerDone(i, str);
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void init(OnFinishInitListener onFinishInitListener) {
        UniSdkUtils.i(TAG, "init SdkQRCode");
        initConfig();
        onFinishInitListener.finishInit(0);
        doQrCodeConfigVal2();
    }

    private void initConfig() {
        LanguageUtil.setLanguageCode(SdkMgr.getInst().getPropStr(ConstProp.LANGUAGE_CODE, null));
        if (getPropInt(ConstProp.SCR_ORIENTATION, 0) == 0) {
            setPropInt(ConstProp.SCR_ORIENTATION, SdkMgr.getInst().getPropInt(ConstProp.SCR_ORIENTATION, 0));
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void createQRCode(final String str, final int i, final int i2, final String str2) {
        if (TextUtils.isEmpty(str2)) {
            createQRCodeDone("");
        } else {
            final String propStr = SdkMgr.getInst().getPropStr(UNI_QRCODE_LEVEL, "Q");
            new Thread(new Runnable() { // from class: com.netease.ntunisdk.SdkQRCode.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode, content:" + str + ", width:" + i + ",height:" + i2 + ",fileName:" + str2);
                        String strCreateQRCode = EncodingHandler.createQRCode(SdkQRCode.this.myCtx, str, null, i, i2, str2, propStr);
                        StringBuilder sb = new StringBuilder();
                        sb.append("createQRCodeDone:");
                        sb.append(strCreateQRCode);
                        UniSdkUtils.d(SdkQRCode.TAG, sb.toString());
                        SdkQRCode.this.createQRCodeDone(strCreateQRCode);
                    } catch (WriterException e) {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode exception:" + e.getMessage());
                        SdkQRCode.this.createQRCodeDone("");
                    } catch (FileNotFoundException e2) {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode exception:" + e2.getMessage());
                        SdkQRCode.this.createQRCodeDone("");
                    } catch (Exception e3) {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode exception:" + e3.getMessage());
                        SdkQRCode.this.createQRCodeDone("");
                    }
                }
            }).start();
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void createQRCode(final String str, final int i, final int i2, final String str2, final String str3) {
        if (TextUtils.isEmpty(str2)) {
            createQRCodeDone("");
        } else {
            final String propStr = SdkMgr.getInst().getPropStr(UNI_QRCODE_LEVEL, "Q");
            new Thread(new Runnable() { // from class: com.netease.ntunisdk.SdkQRCode.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode, content:" + str + ", width:" + i + ",height:" + i2 + ",fileName:" + str2 + ",logo:" + str3);
                        String strCreateQRCode = EncodingHandler.createQRCode(SdkQRCode.this.myCtx, str, TextUtils.isEmpty(str3) ? null : BitmapFactory.decodeFile(str3), i, i2, str2, propStr);
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCodeDone:" + strCreateQRCode);
                        SdkQRCode.this.createQRCodeDone(strCreateQRCode);
                    } catch (WriterException e) {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode exception:" + e.getMessage());
                        SdkQRCode.this.createQRCodeDone("");
                    } catch (FileNotFoundException e2) {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode exception:" + e2.getMessage());
                        SdkQRCode.this.createQRCodeDone("");
                    } catch (Exception e3) {
                        UniSdkUtils.d(SdkQRCode.TAG, "createQRCode exception:" + e3.getMessage());
                        SdkQRCode.this.createQRCodeDone("");
                    }
                }
            }).start();
        }
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void scannerQRCode(final String str) {
        UniSdkUtils.d(TAG, "scannnerQRCode:" + str);
        if (TextUtils.isEmpty(str)) {
            str = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_QRCODE_FILE, "");
            UniSdkUtils.d(TAG, "getPropStr UNISDK_QRCODE_FILE:" + str);
        }
        new Handler().postDelayed(new Runnable() { // from class: com.netease.ntunisdk.SdkQRCode.4
            @Override // java.lang.Runnable
            public void run() throws JSONException {
                if (TextUtils.isEmpty(str)) {
                    UniSdkUtils.d(SdkQRCode.TAG, "open camera scanner...");
                    SdkQRCode.this.requestCameraThenExecute();
                } else {
                    UniSdkUtils.d(SdkQRCode.TAG, "read pic scanner...");
                    if (!str.contains("unisdk:") || !"recognize".equalsIgnoreCase(str.substring(7))) {
                        SdkQRCode.this.startScanTheImage(str);
                    } else {
                        SdkQRCode.this.requestAlbumThenExecute();
                    }
                }
                SdkMgr.getInst().setPropStr(ConstProp.UNISDK_QRCODE_FILE, "");
            }
        }, SdkMgr.getInst().getPropInt("QRCODE_DELAY_MILLIS", 0));
    }

    public void startCaptureActivity() throws JSONException {
        UniSdkUtils.i(TAG, "startCaptureActivity");
        Intent intent = new Intent(this.myCtx, (Class<?>) CaptureActivity.class);
        boolean zEquals = "1".equals(SdkMgr.getInst().getPropStr(TURN_OFF_SENSOR));
        UniSdkUtils.i(TAG, "startCaptureActivity" + zEquals);
        intent.putExtra(TURN_OFF_SENSOR, zEquals);
        if (zEquals && getPropInt(ConstProp.SCR_ORIENTATION, 0) == 5) {
            intent.putExtra(ConstProp.SCR_ORIENTATION, 2);
        } else {
            intent.putExtra(ConstProp.SCR_ORIENTATION, getPropInt(ConstProp.SCR_ORIENTATION, 0));
        }
        intent.putExtra(REMOVE_JUMP_ALBUM, getPropInt(REMOVE_JUMP_ALBUM, 0));
        try {
            HookManager.startActivityForResult((Activity) this.myCtx, intent, REQUEST_CODE_CAPTURE_ACTIVITY);
        } catch (Exception e) {
            UniSdkUtils.d(TAG, "startCaptureActivity exception:" + e.getMessage());
            codeScannerDone(10, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestAlbumThenExecute() {
        if (Util.isMediaPermissionGranted(this.myCtx)) {
            startPickLocalImage();
            return;
        }
        if (SdkMgr.getInst().getPropInt(ENABLE_UNISDK_PERMISSION_TIPS, 0) != 0) {
            String propStr = SdkMgr.getInst().getPropStr(UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS);
            UniSdkUtils.d(TAG, "requestPermission,UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS:" + propStr);
            if (TextUtils.isEmpty(propStr)) {
                propStr = Util.getLocalString(this.myCtx, "ntunisdk_scan_request_external_storage");
            }
            new UniAlertDialog((Activity) this.myCtx).alert(propStr, Util.getLocalString(this.myCtx, "ntunisdk_scan_camera_permission_sure"), Util.getLocalString(this.myCtx, "ntunisdk_scan_camera_permission_cancel"), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkQRCode.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Util.requestMediaPermission((Activity) SdkQRCode.this.myCtx, SdkQRCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }, null, false);
            return;
        }
        Util.requestMediaPermission((Activity) this.myCtx, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void startPickLocalImage() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
        } else {
            intent = new Intent("android.intent.action.GET_CONTENT", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        }
        HookManager.startActivityForResult((Activity) this.myCtx, intent, REQUEST_CODE_START_PICK_IMAGE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startScanTheImage(String str) {
        Util.scanningImage(str, new Util.ScanImageCallback() { // from class: com.netease.ntunisdk.SdkQRCode.6
            @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
            public void onSuccess(String str2) throws JSONException {
                SdkQRCode.this.codeScannerDone(0, str2);
            }

            @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
            public void onFailed() throws JSONException {
                SdkQRCode.this.codeScannerDone(10, "");
            }
        });
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnActivityResult(int i, int i2, Intent intent) throws JSONException {
        UniSdkUtils.d(TAG, "sdkOnActivityResult...");
        if (REQUEST_CODE_CAPTURE_ACTIVITY != i) {
            if (REQUEST_CODE_START_PICK_IMAGE == i) {
                UniSdkUtils.i(TAG, "pick_image_result");
                String path = null;
                if (i2 == -1 && intent != null && intent.getData() != null) {
                    path = Util.getPath(this.myCtx, intent.getData());
                }
                if (!TextUtils.isEmpty(path)) {
                    UniSdkUtils.d(TAG, "startScanTheImage:" + path);
                    startScanTheImage(path);
                    return;
                }
                UniSdkUtils.d(TAG, "not choose pic");
                codeScannerDone(10, "");
                return;
            }
            return;
        }
        if (i2 == 161) {
            UniSdkUtils.i(TAG, CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String string = extras.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                if (!TextUtils.isEmpty(string)) {
                    codeScannerDone(0, string);
                    return;
                } else {
                    codeScannerDone(1 == extras.getInt(CaptureActivity.INTENT_EXTRA_NO_PERMISSION, 0) ? 4 : 10, string);
                    return;
                }
            }
            return;
        }
        if (i2 == 162) {
            UniSdkUtils.i(TAG, CaptureActivity.INTENT_EXTRA_KEY_QR_ALBUM);
            Bundle extras2 = intent.getExtras();
            String string2 = extras2 != null ? extras2.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_ALBUM) : "";
            if (!TextUtils.isEmpty(string2)) {
                codeScannerDone(0, string2);
                return;
            } else {
                codeScannerDone(10, "");
                return;
            }
        }
        codeScannerDone(10, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestCameraThenExecute() throws JSONException {
        if (Util.selfPermissionGranted(this.myCtx, PERMISSION_CAMERA)) {
            UniSdkUtils.d(TAG, "have granted permissions");
            startCaptureActivity();
            return;
        }
        if (SdkMgr.getInst().getPropInt(ENABLE_UNISDK_PERMISSION_TIPS, 0) != 0) {
            String propStr = SdkMgr.getInst().getPropStr(UNISDK_QRCODE_CAMERA_PERMISSION_TIPS);
            UniSdkUtils.d(TAG, "requestPermission, UNISDK_QRCODE_CAMERA_PERMISSION_TIPS:" + propStr);
            if (TextUtils.isEmpty(propStr)) {
                propStr = Util.getLocalString(this.myCtx, "ntunisdk_scan_need_camera_permission");
            }
            new UniAlertDialog((Activity) this.myCtx).alert(propStr, Util.getLocalString(this.myCtx, "ntunisdk_scan_camera_permission_sure"), Util.getLocalString(this.myCtx, "ntunisdk_scan_camera_permission_cancel"), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkQRCode.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions((Activity) SdkQRCode.this.myCtx, new String[]{SdkQRCode.PERMISSION_CAMERA}, SdkQRCode.PERMISSIONS_REQUEST_CAMERA);
                }
            }, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.SdkQRCode.8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) throws JSONException {
                    SdkQRCode.this.handleCameraPermissionNotGranted();
                }
            }, false);
            return;
        }
        ActivityCompat.requestPermissions((Activity) this.myCtx, new String[]{PERMISSION_CAMERA}, PERMISSIONS_REQUEST_CAMERA);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void sdkOnRequestPermissionsResult(int i, String[] strArr, int[] iArr) throws JSONException {
        UniSdkUtils.d(TAG, "sdkOnRequestPermissionsResult, requestCode = " + i);
        if (i == PERMISSIONS_REQUEST_CAMERA) {
            if (iArr.length > 0 && iArr[0] == 0) {
                UniSdkUtils.d(TAG, "camera permission was granted");
                startCaptureActivity();
                return;
            } else {
                handleCameraPermissionNotGranted();
                return;
            }
        }
        if (i != PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            startPickLocalImage();
        } else {
            Toast.makeText(this.myCtx, Util.getLocalString(this.myCtx, "ntunisdk_scan_storage_tips"), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCameraPermissionNotGranted() throws JSONException {
        boolean z = "1".equals(SdkMgr.getInst().getPropStr("ENABLE_UNISDK_PERMISSION_UI")) || "1".equals(getPropStr("ENABLE_UNISDK_PERMISSION_UI"));
        UniSdkUtils.d(TAG, "needToast : " + z);
        if (z) {
            Toast.makeText(this.myCtx, Util.getLocalString(this.myCtx, "ntunisdk_scan_camera_tips"), 0).show();
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "uniSDKPermissionDenied");
            jSONObject.putOpt("permissionName", "Manifest.permission.CAMERA");
            jSONObject.putOpt("channel", UNISDK_QRCODE);
            extendFuncCall(jSONObject.toString());
        } catch (Exception e) {
            UniSdkUtils.d(TAG, "handleCameraPermissionNotGranted exception:" + e.getMessage());
        }
        codeScannerDone(4, "");
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    protected void doSepcialConfigVal(JSONObject jSONObject) {
        doQrCodeConfigVal1(jSONObject);
    }

    private void doQrCodeConfigVal1(JSONObject jSONObject) {
        doConfigVal(jSONObject, DISABLE_QUICK_QR_PAY);
        doConfigVal(jSONObject, ENABLE_UNISDK_PERMISSION_TIPS);
        doConfigVal(jSONObject, UNISDK_QRCODE_CAMERA_PERMISSION_TIPS);
        doConfigVal(jSONObject, UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS);
        doConfigVal(jSONObject, UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS);
        doConfigVal(jSONObject, HIDE_QRCODE_FLASH_BTN);
        doConfigVal(jSONObject, HIDE_QRCODE_ALBUM_BTN);
        doConfigVal(jSONObject, REMOVE_JUMP_ALBUM);
        doConfigVal(jSONObject, QRCODE_SCAN_TIPS);
        doConfigVal(jSONObject, ENABLE_POPUP_QR_PIC);
        doConfigVal(jSONObject, "ENABLE_UNISDK_PERMISSION_UI");
    }

    private void doQrCodeConfigVal2() {
        if (SdkMgr.getInst() == null) {
            return;
        }
        doQrCodeConfigVal2Inner(DISABLE_QUICK_QR_PAY);
        doQrCodeConfigVal2Inner(ENABLE_UNISDK_PERMISSION_TIPS);
        doQrCodeConfigVal2Inner(UNISDK_QRCODE_CAMERA_PERMISSION_TIPS);
        doQrCodeConfigVal2Inner(UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS);
        doQrCodeConfigVal2Inner(UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS);
        doQrCodeConfigVal2Inner(HIDE_QRCODE_FLASH_BTN);
        doQrCodeConfigVal2Inner(HIDE_QRCODE_ALBUM_BTN);
        doQrCodeConfigVal2Inner(QRCODE_SCAN_TIPS);
        doQrCodeConfigVal2Inner(ENABLE_POPUP_QR_PIC);
        doQrCodeConfigVal2Inner("ENABLE_UNISDK_PERMISSION_UI");
    }

    private void doQrCodeConfigVal2Inner(String str) {
        String propStr = getPropStr(str);
        if (TextUtils.isEmpty(propStr)) {
            return;
        }
        SdkMgr.getInst().setPropStr(str, propStr);
    }

    private void restoreIndex(String str) throws JSONException {
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
        if (!TextUtils.isEmpty(propStr)) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("index", str);
            } catch (JSONException e) {
                UniSdkUtils.e(TAG, "indexJson JSONException:" + e.getMessage());
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder(propStr);
            if (propStr.endsWith("/")) {
                sb.append("query_index");
            } else {
                sb.append("/query_index");
            }
            UniSdkUtils.d(TAG, "post json index, queryIndexUrl:" + sb.toString());
            HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
            queueItemNewQueueItem.method = "POST";
            queueItemNewQueueItem.url = sb.toString();
            queueItemNewQueueItem.bSync = true;
            queueItemNewQueueItem.leftRetry = 0;
            queueItemNewQueueItem.setBody(jSONObject.toString());
            queueItemNewQueueItem.transParam = "UNISD_JF_GAS3_QUERY_INDEX";
            queueItemNewQueueItem.callback = new IndexCallback(str);
            String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
            if (!TextUtils.isEmpty(propStr2)) {
                HashMap map = new HashMap();
                try {
                    if (SdkMgr.getInst() != null && (SdkMgr.getInst().hasFeature(ConstProp.MODE_HAS_INTERFACE_PROTECTION) || "1.8.5".equalsIgnoreCase(SdkMgr.getBaseVersion()))) {
                        ApiRequestUtil.addSecureHeader(map, propStr2, queueItemNewQueueItem.method, queueItemNewQueueItem.url, jSONObject.toString(), true);
                    } else {
                        map.put("X-Client-Sign", Crypto.hmacSHA256Signature(propStr2, getSignSrc(queueItemNewQueueItem.method, queueItemNewQueueItem.url, jSONObject.toString())));
                    }
                } catch (Exception e2) {
                    UniSdkUtils.d(TAG, "hmacSHA256Signature exception:" + e2.getMessage());
                }
                queueItemNewQueueItem.setHeaders(map);
            } else {
                UniSdkUtils.d(TAG, "JF_CLIENT_KEY empty");
            }
            HTTPQueue.getInstance("UniSDK").addItem(queueItemNewQueueItem);
            return;
        }
        UniSdkUtils.e(TAG, "UNISDK_JF_GAS3_URL null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProcessResult(String str, String str2) {
        UniSdkUtils.d(TAG, "queryIndexUrl res:" + str2);
        if (TextUtils.isEmpty(str2)) {
            UniSdkUtils.d(TAG, "\u83b7\u53d6index\u5931\u8d25");
            OnCodeScannerListener onCodeScannerListener = this.unisdkCodeScannerListener;
            if (onCodeScannerListener != null) {
                onCodeScannerListener.codeScannerFinish(10, "");
                return;
            }
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str2);
            if (200 == jSONObject.optInt("code")) {
                String strOptString = jSONObject.optString("data", "");
                if (stringMD5(strOptString.getBytes()).equalsIgnoreCase(str)) {
                    String str3 = new String(Base64.decode(strOptString, 0), "UTF-8");
                    UniSdkUtils.d(TAG, "queryIndexUrl data:" + str3);
                    if (this.unisdkCodeScannerListener != null) {
                        this.unisdkCodeScannerListener.codeScannerFinish(0, "");
                    }
                    handlerUniQrcode(new JSONObject(str3));
                    return;
                }
                UniSdkUtils.e(TAG, "index\u6821\u9a8c\u4e0d\u901a\u8fc7");
                if (this.unisdkCodeScannerListener != null) {
                    this.unisdkCodeScannerListener.codeScannerFinish(10, "");
                    return;
                }
                return;
            }
            UniSdkUtils.d(TAG, "err:" + jSONObject.optString(NotificationCompat.CATEGORY_ERROR));
            if (this.unisdkCodeScannerListener != null) {
                this.unisdkCodeScannerListener.codeScannerFinish(12, str2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UniSdkUtils.e(TAG, "handle query index exception:" + e.getMessage());
        }
    }

    private void handlerUniQrcode(JSONObject jSONObject) {
        if (jSONObject != null) {
            if ("share".equalsIgnoreCase(jSONObject.optString("action"))) {
                ShareInfo shareInfo = new ShareInfo();
                shareInfo.setShareChannel(jSONObject.optInt("shareChannel", 102));
                shareInfo.setTitle(jSONObject.optString("title"));
                String strOptString = jSONObject.optString("text");
                shareInfo.setText(strOptString);
                shareInfo.setDesc(jSONObject.optString(SocialConstants.PARAM_APP_DESC, strOptString));
                shareInfo.setLink(jSONObject.optString("link"));
                String strOptString2 = jSONObject.optString("type");
                shareInfo.setType(strOptString2);
                String strOptString3 = jSONObject.optString("image_url");
                if (!TextUtils.isEmpty(strOptString3)) {
                    if ("TYPE_LINK".equals(strOptString2)) {
                        shareInfo.setU3dShareBitmap(strOptString3);
                    } else {
                        shareInfo.setImage(strOptString3);
                    }
                }
                String strOptString4 = jSONObject.optString(ShareArgs.VIDEO_URL);
                if (!TextUtils.isEmpty(strOptString4)) {
                    shareInfo.setVideoUrl(strOptString4);
                }
                QrCodeDownLoadUtil.webShare(this.myCtx, shareInfo);
                return;
            }
            return;
        }
        UniSdkUtils.e(TAG, "handlerUniQrcode params null");
    }

    class IndexCallback implements HTTPCallback {
        private String dataId;

        public IndexCallback(String str) {
            this.dataId = str;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public boolean processResult(String str, String str2) {
            SdkQRCode.this.handleProcessResult(this.dataId, str);
            return false;
        }
    }

    private static String getSignSrc(String str, String str2, String str3) {
        String strReplace = str2.replace("://", "");
        String strSubstring = strReplace.contains("/") ? strReplace.substring(strReplace.indexOf("/")) : "";
        UniSdkUtils.d(TAG, "pathQs:" + strSubstring);
        return str.toUpperCase() + strSubstring + str3;
    }

    private static String stringMD5(byte[] bArr) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    private static String byteArrayToHex(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] cArr2 = new char[bArr.length * 2];
        int i = 0;
        for (byte b : bArr) {
            int i2 = i + 1;
            cArr2[i] = cArr[(b >>> 4) & 15];
            i = i2 + 1;
            cArr2[i2] = cArr[b & 15];
        }
        return new String(cArr2);
    }

    @Override // com.netease.ntunisdk.base.SdkBase
    public void extendFunc(String str) {
        UniSdkUtils.i(TAG, "extendFunc: " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            if ("setLanguage".equalsIgnoreCase(jSONObject.optString("methodId"))) {
                LanguageUtil.setLanguageCode(jSONObject.optString("language", SdkMgr.getInst().getPropStr(ConstProp.LANGUAGE_CODE, null)));
            }
        } catch (Exception unused) {
        }
    }
}