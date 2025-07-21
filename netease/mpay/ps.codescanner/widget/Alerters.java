package com.netease.mpay.ps.codescanner.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.netease.mpay.ps.codescanner.utils.Logging;

/* loaded from: classes5.dex */
public class Alerters {
    private Context mContext;

    public interface AlertersInterface {
        void cancel();
    }

    public Alerters(Context context) {
        this.mContext = context;
    }

    public void alert(String str, String str2, DialogInterface.OnClickListener onClickListener, String str3, DialogInterface.OnClickListener onClickListener2, boolean z) {
        alert(str, str2, onClickListener, str3, onClickListener2, z, null);
    }

    public void alert(String str, String str2, DialogInterface.OnClickListener onClickListener, String str3, DialogInterface.OnClickListener onClickListener2, boolean z, AlertersInterface alertersInterface) {
        alert(str, str2, onClickListener, str3, onClickListener2, z, null, null);
    }

    public void alert(String str, String str2, final DialogInterface.OnClickListener onClickListener, String str3, final DialogInterface.OnClickListener onClickListener2, boolean z, final AlertersInterface alertersInterface, String str4) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setCancelable(z);
        final AlertDialog alertDialogCreate = builder.create();
        try {
            alertDialogCreate.show();
            alertDialogCreate.setContentView(com.netease.mc.mi.R.layout.netease_mpay_ps_codescanner__login_alert_dialog);
            alertDialogCreate.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialogCreate.getWindow().setGravity(17);
            TextView textView = (TextView) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_title);
            if (str4 == null) {
                str4 = this.mContext.getString(com.netease.mc.mi.R.string.netease_mpay_ps_codescanner__attention);
            }
            textView.setText(str4);
            ((TextView) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_message)).setText(str);
            Button button = (Button) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_positive);
            if (str2 != null) {
                button.setText(str2);
                button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.widget.Alerters.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        DialogInterface.OnClickListener onClickListener3 = onClickListener;
                        if (onClickListener3 != null) {
                            onClickListener3.onClick(alertDialogCreate, -1);
                        }
                        alertDialogCreate.dismiss();
                    }
                });
            } else {
                button.setVisibility(8);
                alertDialogCreate.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__button_dividing_line).setVisibility(8);
            }
            Button button2 = (Button) alertDialogCreate.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__alert_negative);
            if (str3 != null) {
                button2.setText(str3);
                button2.setOnClickListener(new View.OnClickListener() { // from class: com.netease.mpay.ps.codescanner.widget.Alerters.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        DialogInterface.OnClickListener onClickListener3 = onClickListener2;
                        if (onClickListener3 != null) {
                            onClickListener3.onClick(alertDialogCreate, -2);
                        }
                        alertDialogCreate.dismiss();
                    }
                });
            } else {
                button2.setVisibility(8);
                alertDialogCreate.findViewById(com.netease.mc.mi.R.id.netease_mpay_ps_codescanner__button_dividing_line).setVisibility(8);
            }
            if (z && alertersInterface != null) {
                alertDialogCreate.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.netease.mpay.ps.codescanner.widget.Alerters.3
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        alertersInterface.cancel();
                    }
                });
            }
            if (str2 == null) {
                button2.setBackgroundResource(com.netease.mc.mi.R.drawable.netease_mpay_ps_codescanner__alert_dialog_single_button);
            } else if (str3 == null) {
                button.setBackgroundResource(com.netease.mc.mi.R.drawable.netease_mpay_ps_codescanner__alert_dialog_single_button);
            }
        } catch (WindowManager.BadTokenException unused) {
        } catch (Exception e) {
            Logging.logStackTrace(e);
        }
    }
}