package com.netease.ntunisdk.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ResUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes5.dex */
public class Alerter {

    /* renamed from: a, reason: collision with root package name */
    private Context f1842a;
    private Dialog b;
    private TextView c;
    private TextView d;
    private Button e;
    private Button f;
    private View g;

    public Alerter(Context context) {
        this.f1842a = context;
        Context context2 = this.f1842a;
        BaseDialog baseDialog = new BaseDialog(context2, ResUtils.getResId(context2, "UniAlertDialog_AlertDialog", ResIdReader.RES_TYPE_STYLE));
        this.b = baseDialog;
        baseDialog.setContentView(ResUtils.getResId(this.f1842a, "unisdk_alert_dialog_view", ResIdReader.RES_TYPE_LAYOUT));
        this.b.setCancelable(false);
        this.c = (TextView) this.b.findViewById(ResUtils.getResId(this.f1842a, "unisdk__alert_title", ResIdReader.RES_TYPE_ID));
        this.d = (TextView) this.b.findViewById(ResUtils.getResId(this.f1842a, "unisdk__alert_message", ResIdReader.RES_TYPE_ID));
        this.e = (Button) this.b.findViewById(ResUtils.getResId(this.f1842a, "unisdk__alert_positive", ResIdReader.RES_TYPE_ID));
        this.f = (Button) this.b.findViewById(ResUtils.getResId(this.f1842a, "unisdk__alert_negative", ResIdReader.RES_TYPE_ID));
        this.g = this.b.findViewById(ResUtils.getResId(this.f1842a, "unisdk__alert_btn_divider", ResIdReader.RES_TYPE_ID));
    }

    public void dismiss() {
        Dialog dialog = this.b;
        if (dialog != null) {
            dialog.cancel();
            this.b = null;
        }
    }

    private void a(String str, String str2, String str3, final DialogInterface.OnClickListener onClickListener, String str4, final DialogInterface.OnClickListener onClickListener2, boolean z, boolean z2, NtSdkStringClickableSpan ntSdkStringClickableSpan) {
        Context context;
        if (TextUtils.isEmpty(str2) || (context = this.f1842a) == null || ((context instanceof Activity) && ViewUtils.isFinishing((Activity) context))) {
            UniSdkUtils.d("UniSDK Alerter", "warning, return alert");
            return;
        }
        try {
            Dialog dialog = this.b;
            if (dialog != null) {
                dialog.setCancelable(z);
                if (TextUtils.isEmpty(str)) {
                    this.c.setVisibility(8);
                } else {
                    this.c.setText(str);
                    this.c.setVisibility(0);
                }
                if (z2) {
                    c.setNtSdkStrings2TextView(c.parseNtSdkTags(str2), new NtSdkTagParser$RichTextView(this.d, ntSdkStringClickableSpan));
                } else {
                    this.d.setText(str2);
                    this.d.setMovementMethod(new ScrollingMovementMethod());
                }
                if (!TextUtils.isEmpty(str3)) {
                    this.e.setText(str3);
                    this.e.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.base.view.Alerter.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            DialogInterface.OnClickListener onClickListener3 = onClickListener;
                            if (onClickListener3 != null) {
                                onClickListener3.onClick(Alerter.this.b, -1);
                            }
                            Alerter.this.dismiss();
                        }
                    });
                    this.e.setVisibility(0);
                    this.g.setVisibility(0);
                } else {
                    this.e.setVisibility(8);
                    this.g.setVisibility(8);
                }
                if (!TextUtils.isEmpty(str4)) {
                    this.f.setText(str4);
                    this.f.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.base.view.Alerter.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            DialogInterface.OnClickListener onClickListener3 = onClickListener2;
                            if (onClickListener3 != null) {
                                onClickListener3.onClick(Alerter.this.b, -2);
                            }
                            Alerter.this.dismiss();
                        }
                    });
                    this.f.setVisibility(0);
                } else {
                    this.f.setVisibility(8);
                    this.g.setVisibility(8);
                }
                this.b.show();
                return;
            }
            UniSdkUtils.d("UniSDK Alerter", "mDialog null");
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public void showDialog(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, NtSdkStringClickableSpan ntSdkStringClickableSpan) {
        a(str, str2, str3, onClickListener, null, null, false, true, ntSdkStringClickableSpan);
    }

    public void showDialog(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, NtSdkStringClickableSpan ntSdkStringClickableSpan) {
        a(str, str2, str3, onClickListener, str4, onClickListener2, false, true, ntSdkStringClickableSpan);
    }

    public void showDialog(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, boolean z, boolean z2, NtSdkStringClickableSpan ntSdkStringClickableSpan) {
        a(str, str2, str3, onClickListener, str4, onClickListener2, z, z2, ntSdkStringClickableSpan);
    }
}