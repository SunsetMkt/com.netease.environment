package com.netease.ntunisdk.external.protocol.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.utils.CommonUtils;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes.dex */
public class AlerterEx {
    public static final String TAG = "AE";
    private TextView mContentTv;
    private final Context mContext;
    private Dialog mDialog;
    private Button mNegativeBtn;
    private final OnBackPressedListener mOnBackPressedListener;
    private Button mPostiveBtn;

    public AlerterEx(Context context) {
        this(context, null);
    }

    public AlerterEx(Context context, OnBackPressedListener onBackPressedListener) {
        this.mContext = context;
        this.mOnBackPressedListener = onBackPressedListener;
        initDialog();
    }

    public static void showToast(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            View viewInflate = LayoutInflater.from(context).inflate(ResUtils.getResId(context, "unisdk_protocol__toast", ResIdReader.RES_TYPE_LAYOUT), (ViewGroup) null);
            ((TextView) viewInflate.findViewById(ResUtils.getResId(context, "unisdk_protocol_toast_message", ResIdReader.RES_TYPE_ID))).setText(str);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(17, 0, 0);
            toast.setDuration(0);
            toast.setView(viewInflate);
            toast.show();
        } catch (Throwable unused) {
        }
    }

    private void initDialog() {
        int resId = ResUtils.getResId(this.mContext, "UniAlertDialog_AlertDialog", ResIdReader.RES_TYPE_STYLE);
        if (resId <= 0) {
            resId = ResUtils.getResId(this.mContext, "NeteaseUniSDK_AlertDialog", ResIdReader.RES_TYPE_STYLE);
        }
        Dialog dialog = new Dialog(this.mContext, resId);
        this.mDialog = dialog;
        SysHelper.hideSystemUI(dialog.getWindow());
        this.mDialog.setContentView(ResUtils.getResId(this.mContext, "unisdk_protocol__alert_dialog", ResIdReader.RES_TYPE_LAYOUT));
        this.mDialog.setCancelable(false);
        View viewFindViewById = this.mDialog.findViewById(ResUtils.getResId(this.mContext, "unisdk__alert_content", ResIdReader.RES_TYPE_ID));
        if (viewFindViewById != null && SDKRuntime.getInstance().isRTLLayout()) {
            CommonUtils.setViewRtlLayout(viewFindViewById);
        }
        this.mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.netease.ntunisdk.external.protocol.view.AlerterEx.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4 || keyEvent.getAction() != 1) {
                    return false;
                }
                AlerterEx.this.mDialog.dismiss();
                if (AlerterEx.this.mOnBackPressedListener != null) {
                    AlerterEx.this.mOnBackPressedListener.onBackPressed();
                }
                return true;
            }
        });
        this.mContentTv = (TextView) this.mDialog.findViewById(ResUtils.getResId(this.mContext, "unisdk__alert_message", ResIdReader.RES_TYPE_ID));
        this.mPostiveBtn = (Button) this.mDialog.findViewById(ResUtils.getResId(this.mContext, "unisdk__alert_positive", ResIdReader.RES_TYPE_ID));
        this.mNegativeBtn = (Button) this.mDialog.findViewById(ResUtils.getResId(this.mContext, "unisdk__alert_negative", ResIdReader.RES_TYPE_ID));
    }

    public void onDismiss() {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.cancel();
        }
        this.mDialog = null;
    }

    public void alert(String str, String str2, String str3, final DialogInterface.OnClickListener onClickListener, String str4, final DialogInterface.OnClickListener onClickListener2) {
        if (TextUtils.isEmpty(str2)) {
            L.d(TAG, "warning, return alert");
            return;
        }
        try {
            if (this.mDialog != null) {
                this.mContentTv.setText(str2);
                this.mContentTv.setMovementMethod(new ScrollingMovementMethod());
                if (!TextUtils.isEmpty(str3)) {
                    this.mPostiveBtn.setText(str3);
                    this.mPostiveBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.AlerterEx.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            DialogInterface.OnClickListener onClickListener3 = onClickListener;
                            if (onClickListener3 != null) {
                                onClickListener3.onClick(AlerterEx.this.mDialog, -1);
                            }
                            AlerterEx.this.mDialog.dismiss();
                        }
                    });
                    this.mPostiveBtn.setVisibility(0);
                } else {
                    this.mPostiveBtn.setVisibility(8);
                }
                if (!TextUtils.isEmpty(str4)) {
                    this.mNegativeBtn.setText(str4);
                    this.mNegativeBtn.requestFocus();
                    this.mNegativeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.AlerterEx.3
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            DialogInterface.OnClickListener onClickListener3 = onClickListener2;
                            if (onClickListener3 != null) {
                                onClickListener3.onClick(AlerterEx.this.mDialog, -2);
                            }
                            AlerterEx.this.mDialog.dismiss();
                        }
                    });
                    this.mNegativeBtn.setVisibility(0);
                } else {
                    this.mNegativeBtn.setVisibility(8);
                }
                this.mDialog.show();
                return;
            }
            L.d(TAG, "mDialog null");
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }
}