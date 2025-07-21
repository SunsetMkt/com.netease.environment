package com.netease.mpay.ps.codescanner.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

/* loaded from: classes5.dex */
public class TextProgressDialog extends Dialog {
    public static TextProgressDialog newInstance(Context context, int i, int i2, String str, boolean z) {
        return newInstance(context, i, i2, str, z, null);
    }

    public static TextProgressDialog newInstance(Context context, int i, int i2, String str, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        TextProgressDialog textProgressDialog = new TextProgressDialog(context, 2131821296);
        textProgressDialog.setContentView(i);
        textProgressDialog.getWindow().getAttributes().gravity = 17;
        TextView textView = (TextView) textProgressDialog.findViewById(i2);
        if (textView != null) {
            textView.setText(str);
        }
        textProgressDialog.setCancelable(z);
        if (onCancelListener != null && z) {
            textProgressDialog.setOnCancelListener(onCancelListener);
        }
        return textProgressDialog;
    }

    public TextProgressDialog(Context context, int i) {
        super(context, i);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}