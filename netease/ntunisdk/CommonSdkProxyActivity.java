package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.xiaomi.onetrack.OneTrack;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CommonSdkProxyActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {
    private static final int TYPE_APIS = 5;
    private static final int TYPE_EXIT = 4;
    private static final int TYPE_LOGIN = 0;
    private static final int TYPE_MANAGER = 2;
    private static final int TYPE_ORDERINFO = 7;
    private static final int TYPE_PAY = 1;
    private static final int TYPE_PROTOCOL = 3;
    private static final int TYPE_USRINFO = 6;
    private EditText etAccount;
    private EditText etPwd;
    private String extras;
    private int type;

    public static void login(Activity activity, int i, String str) {
        open(activity, i, 0, str);
    }

    public static void pay(Activity activity, int i, String str) {
        open(activity, i, 1, str);
    }

    public static void manager(Activity activity, int i, String str) {
        open(activity, i, 2, str);
    }

    public static void protocol(Activity activity, int i, boolean z) {
        open(activity, i, 3, z ? "1" : "0");
    }

    public static void exit(Activity activity, int i) {
        open(activity, i, 4, null);
    }

    public static void apis(Activity activity, int i, String str) {
        open(activity, i, 5, str);
    }

    public static void userinfo(Activity activity, int i, String str) {
        open(activity, i, 6, str);
    }

    public static void orderinfo(Activity activity, int i, String str) {
        open(activity, i, 7, str);
    }

    private static void open(Activity activity, int i, int i2, String str) {
        Intent intent = new Intent(activity, (Class<?>) CommonSdkProxyActivity.class);
        intent.putExtra("type", i2);
        intent.putExtra("extras", str);
        HookManager.startActivityForResult(activity, intent, i);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        int resId;
        super.onCreate(bundle);
        this.type = getIntent().getIntExtra("type", 5);
        this.extras = getIntent().getStringExtra("extras");
        int i = this.type;
        if (i == 0) {
            resId = ResUtils.getResId(this, "uni_comm_login", ResIdReader.RES_TYPE_LAYOUT);
        } else if (i == 1) {
            resId = ResUtils.getResId(this, "uni_comm_pay", ResIdReader.RES_TYPE_LAYOUT);
        } else if (i == 2) {
            resId = ResUtils.getResId(this, "uni_comm_manager", ResIdReader.RES_TYPE_LAYOUT);
        } else if (i == 3) {
            resId = ResUtils.getResId(this, "uni_comm_protocol", ResIdReader.RES_TYPE_LAYOUT);
        } else if (i == 4) {
            resId = ResUtils.getResId(this, "uni_comm_exit", ResIdReader.RES_TYPE_LAYOUT);
        } else if (i == 6 || i == 7) {
            resId = ResUtils.getResId(this, "uni_comm_uerinfo", ResIdReader.RES_TYPE_LAYOUT);
        } else {
            resId = ResUtils.getResId(this, "uni_comm_apis", ResIdReader.RES_TYPE_LAYOUT);
        }
        setContentView(resId);
        init();
    }

    private void init() {
        String str;
        int i = this.type;
        if (i == 0) {
            getWindow().setSoftInputMode(20);
            View viewFindViewById = findViewById(ResUtils.getResId(this, "uni_comm_ic_cancel_login", ResIdReader.RES_TYPE_ID));
            viewFindViewById.setTag("login_cancel");
            viewFindViewById.setOnClickListener(this);
            View viewFindViewById2 = findViewById(ResUtils.getResId(this, "uni_comm_btn_login", ResIdReader.RES_TYPE_ID));
            viewFindViewById2.setTag("do_login");
            viewFindViewById2.setOnClickListener(this);
            this.etAccount = (EditText) findViewById(ResUtils.getResId(this, "uni_comm_edit_account", ResIdReader.RES_TYPE_ID));
            if (!TextUtils.isEmpty(this.extras)) {
                this.etAccount.setText(this.extras);
            }
            EditText editText = (EditText) findViewById(ResUtils.getResId(this, "uni_comm_edit_psw", ResIdReader.RES_TYPE_ID));
            this.etPwd = editText;
            editText.setOnEditorActionListener(this);
            View viewFindViewById3 = findViewById(ResUtils.getResId(this, "uni_comm_ic_account_right", ResIdReader.RES_TYPE_ID));
            viewFindViewById3.setTag("clear_account");
            viewFindViewById3.setOnClickListener(this);
            ((CheckBox) findViewById(ResUtils.getResId(this, "uni_comm_ic_psw_right", ResIdReader.RES_TYPE_ID))).setOnCheckedChangeListener(this);
            return;
        }
        if (i == 1) {
            View viewFindViewById4 = findViewById(ResUtils.getResId(this, "uni_comm_ic_cancel_pay", ResIdReader.RES_TYPE_ID));
            viewFindViewById4.setTag("pay_cancel");
            viewFindViewById4.setOnClickListener(this);
            OrderInfo orderInfoJsonStr2Obj = OrderInfo.jsonStr2Obj(this.extras);
            ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_price", ResIdReader.RES_TYPE_ID))).setText(String.valueOf(orderInfoJsonStr2Obj.getProductCurrentPrice() * orderInfoJsonStr2Obj.getCount()));
            ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_product_info", ResIdReader.RES_TYPE_ID))).setText(orderInfoJsonStr2Obj.getProductName());
            ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_pid", ResIdReader.RES_TYPE_ID))).setText(orderInfoJsonStr2Obj.getProductId());
            View viewFindViewById5 = findViewById(ResUtils.getResId(this, "uni_comm_btn_order_suc", ResIdReader.RES_TYPE_ID));
            viewFindViewById5.setTag("order_suc");
            viewFindViewById5.setOnClickListener(this);
            View viewFindViewById6 = findViewById(ResUtils.getResId(this, "uni_comm_btn_order_fail", ResIdReader.RES_TYPE_ID));
            viewFindViewById6.setTag("order_fail");
            viewFindViewById6.setOnClickListener(this);
            View viewFindViewById7 = findViewById(ResUtils.getResId(this, "uni_comm_btn_order_unknown", ResIdReader.RES_TYPE_ID));
            viewFindViewById7.setTag("order_unknown");
            viewFindViewById7.setOnClickListener(this);
            return;
        }
        if (i == 2) {
            View viewFindViewById8 = findViewById(ResUtils.getResId(this, "uni_comm_id_logout", ResIdReader.RES_TYPE_ID));
            viewFindViewById8.setTag(User.USER_NAME_LOGOUT);
            viewFindViewById8.setOnClickListener(this);
            View viewFindViewById9 = findViewById(ResUtils.getResId(this, "uni_comm_id_apis", ResIdReader.RES_TYPE_ID));
            viewFindViewById9.setTag("apis");
            viewFindViewById9.setOnClickListener(this);
            View viewFindViewById10 = findViewById(ResUtils.getResId(this, "uni_comm_id_orders", ResIdReader.RES_TYPE_ID));
            viewFindViewById10.setTag("orders");
            viewFindViewById10.setOnClickListener(this);
            View viewFindViewById11 = findViewById(ResUtils.getResId(this, "uni_comm_ibtn_cancel_manager", ResIdReader.RES_TYPE_ID));
            viewFindViewById11.setTag("manager_close");
            viewFindViewById11.setOnClickListener(this);
            return;
        }
        if (i == 3) {
            boolean zEquals = TextUtils.equals(this.extras, "1");
            View viewFindViewById12 = findViewById(ResUtils.getResId(this, "uni_comm_btn_protocol_agree", ResIdReader.RES_TYPE_ID));
            viewFindViewById12.setTag("protocol_agree");
            viewFindViewById12.setVisibility(!zEquals ? 0 : 8);
            viewFindViewById12.setOnClickListener(this);
            View viewFindViewById13 = findViewById(ResUtils.getResId(this, "uni_comm_btn_protocol_reject", ResIdReader.RES_TYPE_ID));
            viewFindViewById13.setTag("protocol_reject");
            viewFindViewById13.setVisibility(!zEquals ? 0 : 8);
            viewFindViewById13.setOnClickListener(this);
            View viewFindViewById14 = findViewById(ResUtils.getResId(this, "uni_comm_btn_protocol_confirm", ResIdReader.RES_TYPE_ID));
            viewFindViewById14.setTag("protocol_close");
            viewFindViewById14.setVisibility(zEquals ? 0 : 8);
            viewFindViewById14.setOnClickListener(this);
            View viewFindViewById15 = findViewById(ResUtils.getResId(this, "uni_comm_ibtn_cancel_protocol", ResIdReader.RES_TYPE_ID));
            viewFindViewById15.setTag("protocol_close");
            viewFindViewById15.setVisibility(zEquals ? 0 : 4);
            viewFindViewById15.setOnClickListener(this);
            return;
        }
        if (i == 4) {
            View viewFindViewById16 = findViewById(ResUtils.getResId(this, "uni_comm_ibtn_cancel_exit", ResIdReader.RES_TYPE_ID));
            viewFindViewById16.setTag("exit_close");
            viewFindViewById16.setOnClickListener(this);
            View viewFindViewById17 = findViewById(ResUtils.getResId(this, "uni_comm_btn_exit", ResIdReader.RES_TYPE_ID));
            viewFindViewById17.setTag("exit_done");
            viewFindViewById17.setOnClickListener(this);
            return;
        }
        if (i != 6) {
            if (i == 7) {
                ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_usrinfo_title", ResIdReader.RES_TYPE_ID))).setText("\u8ba2\u5355\u4fe1\u606f");
            } else {
                View viewFindViewById18 = findViewById(ResUtils.getResId(this, "uni_comm_ibtn_cancel_apis", ResIdReader.RES_TYPE_ID));
                viewFindViewById18.setTag("apis_close");
                viewFindViewById18.setOnClickListener(this);
                try {
                    JSONObject jSONObject = new JSONObject(this.extras);
                    JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("apis");
                    if (jSONObjectOptJSONObject != null) {
                        ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_apis1", ResIdReader.RES_TYPE_ID))).setText(jSONObjectOptJSONObject.optString("ed"));
                        ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_apis2", ResIdReader.RES_TYPE_ID))).setText(jSONObjectOptJSONObject.optString("un"));
                    }
                    String strOptString = jSONObject.optString("todo");
                    View viewFindViewById19 = findViewById(ResUtils.getResId(this, "uni_comm_btn_todo", ResIdReader.RES_TYPE_ID));
                    if (TextUtils.isEmpty(strOptString)) {
                        viewFindViewById19.setVisibility(8);
                        return;
                    }
                    viewFindViewById19.setTag(strOptString);
                    viewFindViewById19.setOnClickListener(this);
                    if ("on_new_intent".equals(strOptString)) {
                        str = "\u6d4b\u8bd5onNewIntent";
                    } else {
                        str = "on_req_perm_result".equals(strOptString) ? "\u6d4b\u8bd5onRequestPermissionsResult" : null;
                    }
                    if (str != null) {
                        ((Button) viewFindViewById19).setText(str);
                        return;
                    } else {
                        viewFindViewById19.setVisibility(8);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        ((TextView) findViewById(ResUtils.getResId(this, "uni_comm_tv_usrinfo", ResIdReader.RES_TYPE_ID))).setText(this.extras);
        View viewFindViewById20 = findViewById(ResUtils.getResId(this, "uni_comm_ibtn_cancel_usrinfo", ResIdReader.RES_TYPE_ID));
        viewFindViewById20.setTag("usrinfo_close");
        viewFindViewById20.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String str = (String) view.getTag();
        if ("apis".equals(str)) {
            apis(this, 110, this.extras);
            return;
        }
        if (User.USER_NAME_LOGOUT.equals(str) || "exit_done".equals(str) || "protocol_reject".equals(str) || "on_new_intent".equals(str) || "on_req_perm_result".equals(str) || "login_cancel".equals(str) || "orders".equals(str)) {
            finishWithResult(-1, str);
            return;
        }
        if ("apis_close".equals(str) || "manager_close".equals(str) || "usrinfo_close".equals(str) || "exit_close".equals(str) || "protocol_close".equals(str) || "protocol_agree".equals(str)) {
            finish();
            return;
        }
        if ("do_login".equals(str)) {
            login();
            return;
        }
        if ("clear_account".equals(str)) {
            EditText editText = this.etAccount;
            if (editText != null) {
                editText.setText("");
                return;
            }
            return;
        }
        if ("pay_cancel".equals(str) || "order_suc".equals(str) || "order_fail".equals(str) || "order_unknown".equals(str)) {
            pay(str);
        }
    }

    private void finishWithResult(int i, String str) {
        Intent intent = new Intent();
        intent.putExtra("extras", str);
        setResult(i, intent);
        finish();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (110 == i && -1 == i2) {
            setResult(i2, intent);
            finish();
        }
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (6 != i) {
            return true;
        }
        login();
        return true;
    }

    private void login() {
        String strTrim = this.etAccount.getText().toString().trim();
        String strTrim2 = this.etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(strTrim) || TextUtils.isEmpty(strTrim2)) {
            Toast.makeText(this, "\u8bf7\u8f93\u5165\u8d26\u53f7\u548c\u5bc6\u7801", 0).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("account", strTrim);
        intent.putExtra("psw", strTrim2);
        intent.putExtra("extras", "do_login");
        setResult(-1, intent);
        finish();
    }

    private void pay(String str) {
        Intent intent = new Intent();
        intent.putExtra(OneTrack.Event.ORDER, this.extras);
        intent.putExtra("extras", str);
        setResult(-1, intent);
        finish();
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        EditText editText = this.etPwd;
        if (editText != null) {
            if (z) {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            EditText editText2 = this.etPwd;
            editText2.setSelection(editText2.getText().toString().length());
        }
    }
}