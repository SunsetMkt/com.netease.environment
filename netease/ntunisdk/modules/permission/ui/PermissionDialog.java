package com.netease.ntunisdk.modules.permission.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.netease.ntunisdk.modules.permission.R;

/* loaded from: classes.dex */
public class PermissionDialog extends Dialog {
    private View divideLine;
    private PermissionDialogListener listener;
    private TextView message;
    private TextView negativeButton;
    private TextView positiveButton;

    public PermissionDialog(Context context) {
        super(context);
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        super.onBackPressed();
        PermissionDialogListener permissionDialogListener = this.listener;
        if (permissionDialogListener != null) {
            permissionDialogListener.refuse();
        }
        dismiss();
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        requestWindowFeature(1);
        setContentView(R.layout.netease_permissionkit_sdk__dialog_ui);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        initView();
        initEvent();
    }

    private void initView() {
        this.message = (TextView) findViewById(R.id.netease_permissionkit_sdk__content);
        this.divideLine = findViewById(R.id.netease_permissionkit_sdk__divide_line);
        this.negativeButton = (TextView) findViewById(R.id.netease_permissionkit_sdk__dialog_ui_btn_negative);
        this.positiveButton = (TextView) findViewById(R.id.netease_permissionkit_sdk__dialog_ui_btn_positive);
        ((ImageView) findViewById(R.id.netease_permissionkit_sdk__dialog_ui_button_bg)).setBackgroundResource(R.drawable.netease_permissionkit_sdk__permission_popup_bg);
    }

    private void initEvent() {
        TextView textView = this.negativeButton;
        if (textView == null || this.positiveButton == null) {
            return;
        }
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.permission.ui.PermissionDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PermissionDialog.this.listener != null) {
                    PermissionDialog.this.listener.refuse();
                }
                PermissionDialog.this.dismiss();
            }
        });
        this.positiveButton.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.modules.permission.ui.PermissionDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PermissionDialog.this.listener != null) {
                    PermissionDialog.this.listener.allow();
                }
                PermissionDialog.this.dismiss();
            }
        });
    }

    public void setCallback(PermissionDialogListener permissionDialogListener) {
        this.listener = permissionDialogListener;
    }

    public void setMessage(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.message.setText(str);
    }

    public void setNegativeButtonText(String str) {
        this.negativeButton.setText(str);
        this.negativeButton.setVisibility(0);
        this.divideLine.setVisibility(0);
    }

    public void setPositiveButtonText(String str) {
        this.positiveButton.setText(str);
    }
}