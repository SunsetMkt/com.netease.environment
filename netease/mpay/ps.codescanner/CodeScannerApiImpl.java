package com.netease.mpay.ps.codescanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.netease.mpay.ps.codescanner.module.AppExtra;
import com.netease.mpay.ps.codescanner.module.QRCodeLoginRaw;
import com.netease.mpay.ps.codescanner.module.QRCodePayRaw;
import com.netease.mpay.ps.codescanner.module.QRCodeRaw;
import com.netease.mpay.ps.codescanner.module.QRCodeScannerData;
import com.netease.mpay.ps.codescanner.server.api.ConfirmLoginResp;
import com.netease.mpay.ps.codescanner.server.api.GetQRCodeInfoResp;
import com.netease.mpay.ps.codescanner.task.GetLoginInfoListener;
import com.netease.mpay.ps.codescanner.task.GetLoginInfoTask;
import com.netease.mpay.ps.codescanner.task.LoginWebListener;
import com.netease.mpay.ps.codescanner.task.LoginWebTask;
import com.netease.mpay.ps.codescanner.task.ScanExternalListener;
import com.netease.mpay.ps.codescanner.task.ScanExternalTask;
import com.netease.mpay.ps.codescanner.task.UploadPayStatusTask;
import com.netease.mpay.ps.codescanner.utils.Logging;
import com.netease.mpay.ps.codescanner.widget.AlerterDialog;
import com.netease.mpay.ps.codescanner.widget.Alerters;
import com.netease.mpay.ps.codescanner.widget.RIdentifier;
import com.netease.mpay.ps.codescanner.widget2.ConfirmLoginAlerter;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;

/* loaded from: classes3.dex */
public class CodeScannerApiImpl {
    private Activity mActivity;
    private ConfirmLoginAlerter mAlerter;
    private String mAppChannel;
    private String mChannel;
    private String mGameId;
    private String mSdkVersion;
    private String mUuid;

    protected CodeScannerApiImpl(Activity activity, String str, String str2, String str3, String str4, String str5) {
        try {
            RIdentifier.init(activity);
        } catch (NoClassDefFoundError e) {
            Logging.logStackTrace(e);
        }
        this.mActivity = activity;
        this.mGameId = str;
        this.mChannel = str2;
        this.mUuid = str3;
        this.mAppChannel = str4;
        this.mSdkVersion = str5;
    }

    protected void presentQRCodeScanner(String str, QRCodeScannerData qRCodeScannerData) {
        if (qRCodeScannerData == null || qRCodeScannerData.callback == null) {
            return;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(qRCodeScannerData.uid) || TextUtils.isEmpty(qRCodeScannerData.token) || TextUtils.isEmpty(this.mGameId) || TextUtils.isEmpty(this.mChannel)) {
            retFinish(qRCodeScannerData, CodeScannerRetCode.PARAM_INVALID, "", null);
            return;
        }
        QRCodeRaw qRCodeRawDecode = QRCodeRaw.decode(str);
        if (qRCodeRawDecode instanceof QRCodeLoginRaw) {
            handlerQRCode(qRCodeScannerData, (QRCodeLoginRaw) qRCodeRawDecode);
            return;
        }
        if (qRCodeRawDecode instanceof QRCodePayRaw) {
            handleQRCode(qRCodeScannerData, (QRCodePayRaw) qRCodeRawDecode);
        } else if (qRCodeScannerData.extCallback != null) {
            qRCodeScannerData.extCallback.onFetchQrCode(str);
        } else {
            retFinish(qRCodeScannerData, CodeScannerRetCode.QR_CODE_INVALID, "", this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__scan_err_qrcode));
        }
    }

    protected void notifyOrderFinish(String str, String str2, String str3, int i) {
        new UploadPayStatusTask(this.mActivity, this.mGameId, str, str2, str3, i).doExecute();
    }

    protected void setDebugMode(boolean z) {
        Configs.DEBUG_MODE = z;
    }

    private void handlerQRCode(final QRCodeScannerData qRCodeScannerData, final QRCodeLoginRaw qRCodeLoginRaw) {
        new GetLoginInfoTask(this.mActivity, this.mGameId, qRCodeLoginRaw.uuid, this.mChannel, this.mAppChannel, qRCodeScannerData.sdkJsonData, new GetLoginInfoListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.1
            @Override // com.netease.mpay.ps.codescanner.task.GetLoginInfoListener
            public void onSuccess(GetQRCodeInfoResp getQRCodeInfoResp) {
                if (isH5Auth()) {
                    showConfirmH5Auth(getQRCodeInfoResp);
                } else {
                    showConfirmQrLogin(getQRCodeInfoResp);
                }
            }

            private boolean isH5Auth() {
                return "game_auth".equals(qRCodeLoginRaw.scene);
            }

            private void showConfirmH5Auth(final GetQRCodeInfoResp getQRCodeInfoResp) {
                new Alerters(CodeScannerApiImpl.this.mActivity).alert(String.format(CodeScannerApiImpl.this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__login_confirms), getQRCodeInfoResp.webGameName), CodeScannerApiImpl.this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__confirmed), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CodeScannerApiImpl.this.onConfirmLogin(getQRCodeInfoResp.uuid, false, qRCodeLoginRaw, qRCodeScannerData);
                    }
                }, CodeScannerApiImpl.this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__cancel), new DialogInterface.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.1.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CodeScannerApiImpl.this.retFinish(qRCodeScannerData, CodeScannerRetCode.RETURN_GAME, "", null);
                    }
                }, false);
            }

            private void showConfirmQrLogin(final GetQRCodeInfoResp getQRCodeInfoResp) {
                String str = String.format(CodeScannerApiImpl.this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__scancode_login_message), getQRCodeInfoResp.webGameName);
                CodeScannerApiImpl codeScannerApiImpl = CodeScannerApiImpl.this;
                codeScannerApiImpl.mAlerter = ConfirmLoginAlerter.showDialog(codeScannerApiImpl.mActivity, str, new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.1.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CodeScannerApiImpl.this.onConfirmLogin(getQRCodeInfoResp.uuid, CodeScannerApiImpl.this.mAlerter != null ? CodeScannerApiImpl.this.mAlerter.isRemember() : false, qRCodeLoginRaw, qRCodeScannerData);
                    }
                }, new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.1.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CodeScannerApiImpl.this.retFinish(qRCodeScannerData, CodeScannerRetCode.RETURN_GAME, "", null);
                    }
                }, getQRCodeInfoResp.webTokenPersist);
            }

            @Override // com.netease.mpay.ps.codescanner.task.GetLoginInfoListener
            public void onFailure(String str) {
                CodeScannerApiImpl.this.retFinish(qRCodeScannerData, CodeScannerRetCode.RETURN_GAME, "", str);
            }
        }).doExecute();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfirmLogin(String str, boolean z, QRCodeLoginRaw qRCodeLoginRaw, final QRCodeScannerData qRCodeScannerData) {
        new LoginWebTask(this.mActivity, this.mGameId, str, qRCodeLoginRaw.scene, qRCodeScannerData.uid, qRCodeScannerData.token, this.mChannel, new AppExtra(this.mUuid, this.mAppChannel, this.mSdkVersion, qRCodeScannerData.sdkJsonData, qRCodeScannerData.extra, qRCodeScannerData.extraUniData), z, new LoginWebListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.2
            @Override // com.netease.mpay.ps.codescanner.task.LoginWebListener
            public void onLoginSuccess(ConfirmLoginResp confirmLoginResp) {
                if (confirmLoginResp != null && !TextUtils.isEmpty(confirmLoginResp.mRedirectUrl)) {
                    final String str2 = confirmLoginResp.mRedirectUrl;
                    CodeScannerApiImpl.this.mActivity.runOnUiThread(new Runnable() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str2.trim()));
                                intent.putExtra("com.android.browser.application_id", CodeScannerApiImpl.this.mActivity.getPackageName());
                                HookManager.startActivity(CodeScannerApiImpl.this.mActivity, intent);
                            } catch (Exception unused) {
                            }
                        }
                    });
                }
                CodeScannerApiImpl.this.retFinish(qRCodeScannerData, CodeScannerRetCode.SUCCESS, "", null);
            }

            @Override // com.netease.mpay.ps.codescanner.task.LoginWebListener
            public void onLoginFailure(String str2) {
                CodeScannerApiImpl.this.retFinish(qRCodeScannerData, CodeScannerRetCode.RETURN_GAME, "", str2);
            }
        }).doExecute();
    }

    private void handleQRCode(final QRCodeScannerData qRCodeScannerData, final QRCodePayRaw qRCodePayRaw) {
        if (qRCodeScannerData == null) {
            retFinish(null, CodeScannerRetCode.QR_CODE_INVALID, qRCodePayRaw.uid, "");
            return;
        }
        if (!("0".equals(qRCodePayRaw.uid) || "0".equals(qRCodeScannerData.uid) || TextUtils.equals(qRCodePayRaw.uid, qRCodeScannerData.uid))) {
            retFinish(qRCodeScannerData, CodeScannerRetCode.UID_MISMATCH, qRCodePayRaw.uid, this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__scan_err_payment_uid));
        } else {
            new ScanExternalTask(this.mActivity, this.mGameId, qRCodePayRaw.uid, qRCodePayRaw.dataId, new ScanExternalListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.3
                @Override // com.netease.mpay.ps.codescanner.task.ScanExternalListener
                public void onSuccess() {
                    qRCodeScannerData.callback.onScanPaymentSuccess(qRCodePayRaw.uid, qRCodePayRaw.dataId);
                }

                @Override // com.netease.mpay.ps.codescanner.task.ScanExternalListener
                public void onFailure(String str) {
                    CodeScannerApiImpl.this.retFinish(qRCodeScannerData, CodeScannerRetCode.RETURN_GAME, "", str);
                }
            }).doExecute();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retFinish(final QRCodeScannerData qRCodeScannerData, final CodeScannerRetCode codeScannerRetCode, final String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            Activity activity = this.mActivity;
            AlerterDialog alerterDialog = new AlerterDialog(activity, activity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__attention), str2, this.mActivity.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__confirmed), new AlerterDialog.OnConfirmListener() { // from class: com.netease.mpay.ps.codescanner.CodeScannerApiImpl.4
                @Override // com.netease.mpay.ps.codescanner.widget.AlerterDialog.OnConfirmListener
                public void onConfirmListener() {
                    QRCodeScannerData qRCodeScannerData2 = qRCodeScannerData;
                    if (qRCodeScannerData2 == null || qRCodeScannerData2.callback == null) {
                        return;
                    }
                    qRCodeScannerData.callback.onFinish(codeScannerRetCode, str);
                }
            }, false);
            alerterDialog.changeLayoutParams(this.mActivity.getResources().getDimensionPixelSize(com.netease.mc.mi.R.dimen.netease_mpay_ps_codescanner__login_scancode_err_dialog_width), 0);
            alerterDialog.show();
            return;
        }
        if (qRCodeScannerData == null || qRCodeScannerData.callback == null) {
            return;
        }
        qRCodeScannerData.callback.onFinish(codeScannerRetCode, str);
    }
}