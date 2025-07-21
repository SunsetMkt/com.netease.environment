package com.netease.mpay.ps.codescanner.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/* loaded from: classes5.dex */
public class AlerterDialog {
    private Dialog mAlertDialog;

    public interface OnConfirmListener {
        void onConfirmListener();
    }

    public AlerterDialog(Activity activity, String str, String str2, String str3, final OnConfirmListener onConfirmListener, boolean z) {
        this.mAlertDialog = new Dialog(activity, 2131821294);
        this.mAlertDialog.setContentView(com.netease.mc.mi.R.layout.netease_mpay_ps_codescanner__login_alert_dialog);
        this.mAlertDialog.setCancelable(z);
        this.mAlertDialog.setCanceledOnTouchOutside(z);
        TextView textView = (TextView) this.mAlertDialog.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_title);
        TextView textView2 = (TextView) this.mAlertDialog.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_message);
        Button button = (Button) this.mAlertDialog.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_negative);
        this.mAlertDialog.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_positive).setVisibility(8);
        this.mAlertDialog.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__button_dividing_line).setVisibility(8);
        textView.setText(str);
        textView2.setText(str2);
        button.setText(str3);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.widget.AlerterDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AlerterDialog.this.mAlertDialog.dismiss();
                onConfirmListener.onConfirmListener();
            }
        });
    }

    public void changeLayoutParams(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = ((LinearLayout) this.mAlertDialog.getWindow().getDecorView().findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__login_alert_dialog)).getLayoutParams();
        if (i > 0) {
            layoutParams.width = i;
        }
        if (i2 > 0) {
            layoutParams.height = i2;
        }
        this.mAlertDialog.getWindow().getDecorView().setLayoutParams(layoutParams);
    }

    public void show() {
        this.mAlertDialog.show();
    }
}