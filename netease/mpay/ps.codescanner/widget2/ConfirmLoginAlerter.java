package com.netease.mpay.ps.codescanner.widget2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.netease.mc.mi.R;
import com.netease.mpay.ps.codescanner.server.api.ApiConsts;
import com.netease.mpay.ps.codescanner.widget2.AgreeCheckBox;
import com.netease.ntunisdk.base.view.ViewUtils;

/* loaded from: classes5.dex */
public class ConfirmLoginAlerter {
    private final Activity activity;
    private final String cancelText;
    private final String confirmText;
    private final String content;
    private AgreeCheckBox mAgreeCheckBox;
    private String mCancelBtnName;
    private Dialog mCurrentShowingDialog;
    private String mOkBtnName;
    private String mPageName;
    private LinearLayout mWebTokenPersistLayout;
    private final View.OnClickListener onCancelListener;
    private final View.OnClickListener onConfirmListener;
    private final int webTokenPersist;

    /* JADX INFO: Access modifiers changed from: private */
    public void onAgreeWebTokenPersistChanged(boolean z) {
    }

    public ConfirmLoginAlerter(Activity activity, String str, String str2, View.OnClickListener onClickListener, String str3, View.OnClickListener onClickListener2, int i) {
        this.activity = activity;
        this.content = str;
        this.confirmText = str2;
        this.onConfirmListener = onClickListener;
        this.cancelText = str3;
        this.onCancelListener = onClickListener2;
        this.webTokenPersist = i;
    }

    private boolean isShowing() {
        return this.mCurrentShowingDialog != null;
    }

    public static ConfirmLoginAlerter showDialog(Activity activity, String str, View.OnClickListener onClickListener, View.OnClickListener onClickListener2, int i) {
        return showDialog(activity, str, activity.getString(R.string.netease_mpay_ps_codescanner__confirmed), onClickListener, activity.getString(R.string.netease_mpay_ps_codescanner__cancel), onClickListener2, i);
    }

    public static ConfirmLoginAlerter showDialog(Activity activity, String str, String str2, View.OnClickListener onClickListener, String str3, View.OnClickListener onClickListener2, int i) {
        ConfirmLoginAlerter confirmLoginAlerter = new ConfirmLoginAlerter(activity, str, str2, onClickListener, str3, onClickListener2, i);
        confirmLoginAlerter.show();
        return confirmLoginAlerter;
    }

    public ConfirmLoginAlerter setDialogTrackInfo(String str, String str2, String str3) {
        this.mPageName = str;
        this.mOkBtnName = str2;
        this.mCancelBtnName = str3;
        return this;
    }

    private void show() {
        if (ViewUtils.isFinishing(this.activity) || TextUtils.isEmpty(this.content) || (TextUtils.isEmpty(this.confirmText) && TextUtils.isEmpty(this.cancelText))) {
            View.OnClickListener onClickListener = this.onConfirmListener;
            if (onClickListener != null) {
                onClickListener.onClick(null);
                return;
            }
            View.OnClickListener onClickListener2 = this.onCancelListener;
            if (onClickListener2 != null) {
                onClickListener2.onClick(null);
                return;
            }
            return;
        }
        final BaseDialog baseDialog = new BaseDialog(this.activity, 2131821295);
        baseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        baseDialog.getWindow().setGravity(17);
        baseDialog.getWindow().requestFeature(1);
        baseDialog.setContentView(R.layout.netease_mpay_ps_codescanner__alerter);
        baseDialog.show();
        this.mCurrentShowingDialog = baseDialog;
        baseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.netease.mpay.ps.codescanner.widget2.ConfirmLoginAlerter.1
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                ConfirmLoginAlerter.this.mCurrentShowingDialog = null;
            }
        });
        baseDialog.setCancelable(false);
        TextView textView = (TextView) baseDialog.findViewById(R.id.netease_mpay_ps_codescanner__alert_message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(this.content);
        customInitViews(baseDialog);
        Button button = (Button) baseDialog.findViewById(R.id.netease_mpay_ps_codescanner__confirm);
        Button button2 = (Button) baseDialog.findViewById(R.id.netease_mpay_ps_codescanner__cancel);
        View viewFindViewById = baseDialog.findViewById(R.id.netease_mpay_ps_codescanner__btn_divider);
        if (Build.VERSION.SDK_INT >= 21) {
            button.setBackgroundTintMode(PorterDuff.Mode.DST_IN);
            button2.setBackgroundTintMode(PorterDuff.Mode.DST_IN);
        }
        if (TextUtils.isEmpty(this.confirmText)) {
            button.setVisibility(8);
            viewFindViewById.setVisibility(8);
        } else {
            button.setText(this.confirmText);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.widget2.ConfirmLoginAlerter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    baseDialog.dismiss();
                    if (ConfirmLoginAlerter.this.onConfirmListener != null) {
                        ConfirmLoginAlerter.this.onConfirmListener.onClick(view);
                    }
                }
            });
        }
        if (TextUtils.isEmpty(this.cancelText)) {
            button2.setVisibility(8);
            viewFindViewById.setVisibility(8);
        } else {
            button2.setText(this.cancelText);
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.widget2.ConfirmLoginAlerter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    baseDialog.dismiss();
                    if (ConfirmLoginAlerter.this.onCancelListener != null) {
                        ConfirmLoginAlerter.this.onCancelListener.onClick(view);
                    }
                }
            });
        }
    }

    private void customInitViews(Dialog dialog) {
        if (ApiConsts.WebTokenPersistMode.shouldShow(this.webTokenPersist)) {
            renderWebTokenPersistLayout(dialog);
        }
    }

    private void renderWebTokenPersistLayout(Dialog dialog) {
        this.mWebTokenPersistLayout = (LinearLayout) dialog.findViewById(R.id.netease_mpay_ps_codescanner__qrcode_login_checkbox_content);
        this.mWebTokenPersistLayout.setVisibility(0);
        ((TextView) this.mWebTokenPersistLayout.findViewById(R.id.netease_mpay_ps_codescanner__remember_login_info)).setOnClickListener(new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.widget2.ConfirmLoginAlerter.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ConfirmLoginAlerter.this.mAgreeCheckBox.setCheckState(!ConfirmLoginAlerter.this.mAgreeCheckBox.isChecked());
                ConfirmLoginAlerter confirmLoginAlerter = ConfirmLoginAlerter.this;
                confirmLoginAlerter.onAgreeWebTokenPersistChanged(confirmLoginAlerter.mAgreeCheckBox.isChecked());
            }
        });
        this.mAgreeCheckBox = new AgreeCheckBox(this.mWebTokenPersistLayout.findViewById(R.id.netease_mpay_ps_codescanner__remember_selected), this.mWebTokenPersistLayout.findViewById(R.id.netease_mpay_ps_codescanner__remember_unselected), ApiConsts.WebTokenPersistMode.shouldAgree(this.webTokenPersist)).setOnAgreeChangeListener(new AgreeCheckBox.OnAgreeChangeListener() { // from class: com.netease.mpay.ps.codescanner.widget2.ConfirmLoginAlerter.5
            @Override // com.netease.mpay.ps.codescanner.widget2.AgreeCheckBox.OnAgreeChangeListener
            public void onAgreeChanged(boolean z) {
                ConfirmLoginAlerter.this.onAgreeWebTokenPersistChanged(z);
            }
        });
    }

    public boolean isRemember() {
        AgreeCheckBox agreeCheckBox = this.mAgreeCheckBox;
        return agreeCheckBox != null && agreeCheckBox.isChecked();
    }
}