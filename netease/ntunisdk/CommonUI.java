package com.netease.ntunisdk;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes2.dex */
public class CommonUI {
    private static final String NAMESPACE_PREFIX = "ntunisdk_common_";
    private Button failBtn;
    private TextView headTV;
    private Dialog mDialog;
    private Context myCtx;
    private Button successBtn;
    private TextView valuesCheckTV;

    interface UICallback {
        void fail(int i, String str);

        void success(int i, String str);
    }

    public CommonUI(Context context) {
        this.myCtx = context;
        this.mDialog = new Dialog(context, android.R.style.Theme.Black.NoTitleBar.Fullscreen);
        this.mDialog.setContentView(initUI());
        this.mDialog.setCanceledOnTouchOutside(false);
    }

    private View initUI() {
        ScrollView scrollView = new ScrollView(this.myCtx);
        scrollView.setPadding(10, 10, 10, 10);
        LinearLayout linearLayout = new LinearLayout(this.myCtx);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        scrollView.addView(linearLayout, new ViewGroup.LayoutParams(-1, -1));
        TextView textView = new TextView(this.myCtx);
        this.headTV = textView;
        textView.setGravity(17);
        this.headTV.setTextSize(20.0f);
        this.headTV.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout.addView(this.headTV, new ViewGroup.LayoutParams(-1, -2));
        LinearLayout linearLayout2 = new LinearLayout(this.myCtx);
        linearLayout2.setOrientation(0);
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout2.setPadding(0, 10, 0, 0);
        Button button = new Button(this.myCtx);
        this.successBtn = button;
        button.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1.0f));
        this.successBtn.setText(CommonTips.SUCCESS_BTN);
        linearLayout2.addView(this.successBtn);
        Button button2 = new Button(this.myCtx);
        this.failBtn = button2;
        button2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1.0f));
        this.failBtn.setText(CommonTips.FAIL_BTN);
        linearLayout2.addView(this.failBtn);
        linearLayout.addView(linearLayout2);
        LinearLayout linearLayout3 = new LinearLayout(this.myCtx);
        linearLayout3.setOrientation(1);
        linearLayout3.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout3.setPadding(0, 10, 0, 0);
        TextView textView2 = new TextView(this.myCtx);
        this.valuesCheckTV = textView2;
        textView2.setText("");
        this.valuesCheckTV.setTextSize(20.0f);
        this.valuesCheckTV.setTextColor(-1);
        this.valuesCheckTV.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.valuesCheckTV.setVisibility(8);
        linearLayout3.addView(this.valuesCheckTV);
        linearLayout.addView(linearLayout3);
        return scrollView;
    }

    public void show(String str, String str2, String str3, UICallback uICallback) {
        showDialog(str, str2, "", "", str3, uICallback);
    }

    public void show(String str, String str2, String str3, String str4, String str5, UICallback uICallback) {
        showDialog(str, str2, str3, str4, str5, uICallback);
    }

    private void showDialog(String str, String str2, String str3, String str4, String str5, final UICallback uICallback) {
        if (!TextUtils.isEmpty(str)) {
            this.headTV.setText(str);
        }
        if (!TextUtils.isEmpty(str2)) {
            this.successBtn.setVisibility(0);
            this.successBtn.setText(str2);
        } else {
            this.successBtn.setVisibility(8);
        }
        if (!TextUtils.isEmpty(str3)) {
            this.failBtn.setVisibility(0);
            this.failBtn.setText(str3);
        } else {
            this.failBtn.setVisibility(8);
        }
        if (!TextUtils.isEmpty(str4)) {
            this.valuesCheckTV.setVisibility(0);
            this.valuesCheckTV.setText(str4);
        } else {
            this.valuesCheckTV.setVisibility(8);
        }
        this.successBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.CommonUI.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CommonUI.this.mDialog.dismiss();
                UICallback uICallback2 = uICallback;
                if (uICallback2 != null) {
                    uICallback2.success(0, "");
                }
            }
        });
        this.failBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.CommonUI.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CommonUI.this.mDialog.dismiss();
                UICallback uICallback2 = uICallback;
                if (uICallback2 != null) {
                    uICallback2.fail(1, "");
                }
            }
        });
        this.mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.netease.ntunisdk.CommonUI.3
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                CommonUI.this.mDialog.cancel();
                if (uICallback != null) {
                    if (CommonUI.this.failBtn.getVisibility() != 0) {
                        if (CommonUI.this.successBtn.getVisibility() == 0) {
                            uICallback.success(0, "");
                            return;
                        }
                        return;
                    }
                    uICallback.fail(1, "");
                }
            }
        });
        this.mDialog.show();
    }

    public void show(String str, String str2, UICallback uICallback) {
        show(str, CommonTips.SUCCESS_BTN, CommonTips.FAIL_BTN, "", str2, uICallback);
    }

    public void show(String str, UICallback uICallback) {
        show(str, CommonTips.SUCCESS_BTN, CommonTips.FAIL_BTN, "", "", uICallback);
    }

    public static int getStrResourceId(Context context, String str) {
        return getResourceId(context, str, ResIdReader.RES_TYPE_STRING);
    }

    public static int getIdResourceId(Context context, String str) {
        return getResourceId(context, str, ResIdReader.RES_TYPE_ID);
    }

    public static int getLayoutResourceId(Context context, String str) {
        return getResourceId(context, str, ResIdReader.RES_TYPE_LAYOUT);
    }

    public static int getResourceId(Context context, String str, String str2) {
        return context.getResources().getIdentifier(NAMESPACE_PREFIX + str, str2, context.getPackageName());
    }
}