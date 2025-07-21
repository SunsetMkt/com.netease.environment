package com.netease.ntunisdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/* loaded from: classes.dex */
public class UniAlertDialog {
    private final Activity mContext;

    public UniAlertDialog(Activity activity) {
        this.mContext = activity;
    }

    public void alert(String str, String str2, DialogInterface.OnClickListener onClickListener, boolean z) {
        alert(str, str2, null, onClickListener, null, z);
    }

    public void alert(final String str, final String str2, final String str3, final DialogInterface.OnClickListener onClickListener, final DialogInterface.OnClickListener onClickListener2, final boolean z) {
        this.mContext.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.UniAlertDialog.1
            @Override // java.lang.Runnable
            public void run() {
                UniAlertDialog.this.alertImpl(str, str2, str3, onClickListener, onClickListener2, z);
            }
        });
    }

    public void alertImpl(String str, String str2, String str3, final DialogInterface.OnClickListener onClickListener, final DialogInterface.OnClickListener onClickListener2, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setCancelable(z);
        final AlertDialog alertDialogCreate = builder.create();
        try {
            alertDialogCreate.show();
            alertDialogCreate.setContentView(com.netease.mc.mi.R.layout.ntunisdk_scanner_dialog);
            if (alertDialogCreate.getWindow() != null) {
                alertDialogCreate.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialogCreate.getWindow().setGravity(17);
            }
            ((TextView) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.ntunisdk_scanner_alert_message)).setText(str);
            View viewFindViewById = alertDialogCreate.findViewById(com.netease.mc.mi.R.id.ntunisdk_scanner_alert_sep);
            TextView textView = (TextView) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.ntunisdk_scanner_alert_btn_positive);
            if (str2 != null) {
                textView.setText(str2);
                textView.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.UniAlertDialog.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        DialogInterface.OnClickListener onClickListener3 = onClickListener;
                        if (onClickListener3 != null) {
                            onClickListener3.onClick(alertDialogCreate, -1);
                        }
                        alertDialogCreate.dismiss();
                    }
                });
                textView.setVisibility(0);
            } else {
                viewFindViewById.setVisibility(8);
                textView.setVisibility(8);
            }
            TextView textView2 = (TextView) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.ntunisdk_scanner_alert_btn_negative);
            if (str3 != null) {
                final Runnable runnable = new Runnable() { // from class: com.netease.ntunisdk.UniAlertDialog.3
                    @Override // java.lang.Runnable
                    public void run() {
                        DialogInterface.OnClickListener onClickListener3 = onClickListener2;
                        if (onClickListener3 != null) {
                            onClickListener3.onClick(alertDialogCreate, -2);
                        }
                        alertDialogCreate.dismiss();
                    }
                };
                textView2.setText(str3);
                textView2.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.UniAlertDialog.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        runnable.run();
                    }
                });
                textView2.setVisibility(0);
                addOnBackPressedListener(alertDialogCreate, runnable);
                return;
            }
            viewFindViewById.setVisibility(8);
            textView2.setVisibility(8);
        } catch (WindowManager.BadTokenException unused) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOnBackPressedListener(AlertDialog alertDialog, final Runnable runnable) {
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.netease.ntunisdk.UniAlertDialog.5
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                runnable.run();
                return true;
            }
        });
    }
}