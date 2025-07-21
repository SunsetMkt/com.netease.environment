package com.netease.ntunisdk.external.protocol.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.Situation;
import com.netease.ntunisdk.external.protocol.Tracker;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.external.protocol.plugins.Callback;
import com.netease.ntunisdk.external.protocol.plugins.PluginManager;
import com.netease.ntunisdk.external.protocol.plugins.Result;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ContentDialog extends Dialog {
    private static final String TAG = "D";
    private Activity mActivity;
    private ArrayList<ProtocolInfo.ConcreteSubProtocol> mConcreteSubProtocols;
    private String mContentText;
    private final HashMap<String, NewContentView> mContentViews;
    private Context mContext;
    private NewContentView mCurrentContentView;
    private User mCurrentUser;
    private final DialogInfo mDialogInfo;
    private boolean mIsSubProtocol;
    private ProtocolInfo mProtocolInfo;
    private final ProtocolManager mProtocolManager;
    private String mScene;
    private Situation mSituation;
    private int mViewStyle;

    public ContentDialog(Context context) {
        this(context, ResUtils.getResId(context, "unisdk_protocol_dialog", ResIdReader.RES_TYPE_STYLE));
        this.mContext = context;
    }

    public ContentDialog(Context context, int i) {
        super(context, i);
        this.mContentViews = new HashMap<>();
        this.mProtocolManager = ProtocolManager.getInstance();
        DialogInfo dialogInfo = new DialogInfo();
        this.mDialogInfo = dialogInfo;
        dialogInfo.getScreenInfo((Activity) context);
    }

    private void focusNotAle(Window window) {
        window.setFlags(8, 8);
    }

    private void clearFocusNotAle(Window window) {
        window.clearFlags(8);
    }

    private NewContentView getContentView(Situation situation, String str, User user, int i, ProtocolInfo protocolInfo, boolean z, String str2, int i2, String str3) {
        if (protocolInfo != null && protocolInfo.isHtml) {
            return new UrlContentView(situation, str, user, this.mProtocolManager.getProp(), i, protocolInfo, str2, this.mConcreteSubProtocols);
        }
        return new NewContentView(situation, str, user, this.mProtocolManager.getProp(), i, protocolInfo, z, str2, i2, str3, this.mConcreteSubProtocols);
    }

    public void init(Activity activity, Situation situation, String str, User user, int i, String str2, ProtocolInfo protocolInfo, boolean z, ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList) {
        this.mViewStyle = i;
        this.mActivity = activity;
        this.mIsSubProtocol = z;
        this.mProtocolInfo = protocolInfo;
        this.mSituation = situation;
        this.mScene = str;
        this.mCurrentUser = user;
        this.mContentText = str2;
        this.mConcreteSubProtocols = arrayList;
        NewContentView contentView = getContentView(situation, str, user, this.mProtocolManager.getProtocolType(), this.mProtocolInfo, this.mIsSubProtocol, null, this.mViewStyle, this.mContentText);
        this.mCurrentContentView = contentView;
        this.mContentViews.put(contentView.getKey(), this.mCurrentContentView);
    }

    @Override // android.app.Dialog
    public void show() {
        onViewCreate();
        Window window = getWindow();
        this.mDialogInfo.computeDialogLayoutParams(window);
        focusNotAle(window);
        super.show();
        SysHelper.hideSystemUI(window);
        clearFocusNotAle(window);
        this.mProtocolManager.getCallback().onOpen();
    }

    public void onConfigurationChanged(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        try {
            try {
                this.mDialogInfo.getScreenInfo(activity);
                NewContentView contentView = getContentView(this.mSituation, this.mScene, this.mCurrentUser, this.mProtocolManager.getProtocolType(), this.mProtocolInfo, this.mIsSubProtocol, null, this.mViewStyle, this.mContentText);
                this.mCurrentContentView = contentView;
                this.mContentViews.put(contentView.getKey(), this.mCurrentContentView);
                this.mDialogInfo.computeDialogLayoutParams(getWindow());
                onViewCreate();
            } catch (Throwable unused) {
            }
        } catch (Throwable unused2) {
            dismiss();
            this.mProtocolManager.acceptProtocol(this.mCurrentUser.getUid(), false);
            this.mProtocolManager.getCallback().onFinish(3, null);
            this.mProtocolManager.onDestroy(getContext());
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        NewContentView newContentView = this.mCurrentContentView;
        if (newContentView != null) {
            newContentView.requestFocus();
        }
    }

    private void onViewCreate() {
        setCancelable(false);
        setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.1
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                ContentDialog.this.mContentViews.clear();
            }
        });
        setDialogOnKeyListener();
        View view = getView();
        if (view == null) {
            try {
                dismiss();
                this.mProtocolManager.acceptProtocol(this.mCurrentUser.getUid(), false);
                this.mProtocolManager.getCallback().onFinish(3, null);
                this.mProtocolManager.onDestroy(getContext());
                return;
            } catch (Throwable unused) {
                return;
            }
        }
        setContentView(view);
    }

    private void setDialogOnKeyListener() {
        setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) throws Resources.NotFoundException {
                if (i == 4 && keyEvent.getAction() == 1) {
                    L.d("D", "protocol dialog onKey back1");
                    if (ContentDialog.this.mCurrentContentView.isSubProtocol()) {
                        String parent = ContentDialog.this.mCurrentContentView.getParent();
                        if (!TextUtils.isEmpty(parent)) {
                            NewContentView newContentView = (NewContentView) ContentDialog.this.mContentViews.get(parent);
                            if (newContentView != null) {
                                ContentDialog.this.mCurrentContentView = newContentView;
                                ContentDialog contentDialog = ContentDialog.this;
                                contentDialog.mViewStyle = contentDialog.mCurrentContentView.getViewStyle();
                                ContentDialog contentDialog2 = ContentDialog.this;
                                contentDialog2.setContentView(contentDialog2.mCurrentContentView.getView(), ContentDialog.this.mDialogInfo.mDialogLayoutParams);
                                return true;
                            }
                        } else if (ContentDialog.this.mCurrentContentView.isHtmlProtocol()) {
                            ContentDialog.this.dismiss();
                            ContentDialog.this.mProtocolManager.onDestroy(ContentDialog.this.getContext());
                            ContentDialog.this.mProtocolManager.getCallback().onFinish(0);
                        } else {
                            L.d("D", "protocol dialog onKey back2");
                            ContentDialog contentDialog3 = ContentDialog.this;
                            contentDialog3.rejectProtocolConfirm(contentDialog3.mActivity);
                            return true;
                        }
                    } else {
                        L.d("D", "protocol dialog onKey back2");
                        if (1 == ContentDialog.this.mViewStyle) {
                            ContentDialog.this.dismiss();
                            ContentDialog.this.mProtocolManager.onDestroy(ContentDialog.this.getContext());
                            if (!SDKRuntime.getInstance().isSilentMode() || ContentDialog.this.mSituation == Situation.REVIEW) {
                                ContentDialog.this.mProtocolManager.getCallback().onFinish(0);
                            } else {
                                ContentDialog.this.mProtocolManager.getCallback().onFinish(1);
                            }
                        } else {
                            ContentDialog contentDialog4 = ContentDialog.this;
                            contentDialog4.rejectProtocolConfirm(contentDialog4.mActivity);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private View getView() {
        return this.mCurrentContentView.onCreateView(this.mActivity, this.mDialogInfo, new EventCallback() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.3
            @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
            public void next(Situation situation, ProtocolInfo protocolInfo, int i) {
                ContentDialog.this.showSubProtocol(situation, protocolInfo);
            }

            @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
            public void openBrowser(String str) {
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                if (SDKRuntime.getInstance().isSupportOpenBrowser()) {
                    SysHelper.openBrowser(ContentDialog.this.mActivity, str);
                    return;
                }
                ProtocolInfo protocolInfo = new ProtocolInfo(Const.PROTOCOL_TYPE_HTML, TextCompat.wrapperUrl(str));
                if (ContentDialog.this.mProtocolManager != null) {
                    protocolInfo.globalInfo = ContentDialog.this.mProtocolManager.getCurrentBaseProtocol() != null ? ContentDialog.this.mProtocolManager.getCurrentBaseProtocol().globalInfo : null;
                }
                ContentDialog contentDialog = ContentDialog.this;
                contentDialog.showSubProtocol(contentDialog.mSituation, protocolInfo);
            }

            @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
            public void back(String str) {
                NewContentView newContentView;
                if (TextUtils.isEmpty(str) || (newContentView = (NewContentView) ContentDialog.this.mContentViews.get(str)) == null) {
                    return;
                }
                ContentDialog.this.mCurrentContentView = newContentView;
                ContentDialog contentDialog = ContentDialog.this;
                contentDialog.mViewStyle = contentDialog.mCurrentContentView.getViewStyle();
                ContentDialog contentDialog2 = ContentDialog.this;
                contentDialog2.setContentView(contentDialog2.mCurrentContentView.getView());
                ContentDialog.this.mCurrentContentView.updateView();
            }

            @Override // com.netease.ntunisdk.external.protocol.view.EventCallback
            public void finish(int i, JSONObject jSONObject) throws Resources.NotFoundException {
                if (i != 1) {
                    if (i == 2) {
                        ContentDialog contentDialog = ContentDialog.this;
                        contentDialog.rejectProtocolConfirm(contentDialog.mActivity);
                        return;
                    } else if (i != 4) {
                        ContentDialog.this.dismiss();
                        ContentDialog.this.mProtocolManager.getCallback().onFinish(0, jSONObject);
                        ContentDialog.this.mProtocolManager.onDestroy(ContentDialog.this.getContext());
                        return;
                    }
                }
                ContentDialog.this.dismiss();
                ContentDialog.this.mProtocolManager.acceptProtocol(ContentDialog.this.mCurrentUser.getUid(), false);
                ContentDialog.this.mProtocolManager.getCallback().onFinish(i, jSONObject);
                ContentDialog.this.mProtocolManager.onDestroy(ContentDialog.this.getContext());
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.ContentDialog$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$netease$ntunisdk$external$protocol$Situation;

        static {
            int[] iArr = new int[Situation.values().length];
            $SwitchMap$com$netease$ntunisdk$external$protocol$Situation = iArr;
            try {
                iArr[Situation.SWITCH_ACCOUNT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$netease$ntunisdk$external$protocol$Situation[Situation.LOGIN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$netease$ntunisdk$external$protocol$Situation[Situation.LAUNCHER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$netease$ntunisdk$external$protocol$Situation[Situation.REVIEW.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSubProtocol(Situation situation, ProtocolInfo protocolInfo) {
        String str;
        try {
            int i = AnonymousClass6.$SwitchMap$com$netease$ntunisdk$external$protocol$Situation[situation.ordinal()];
            if (i == 1 || i == 2) {
                if (protocolInfo.acceptStatus == 3) {
                    str = protocolInfo.updateText;
                    if (TextUtils.isEmpty(str)) {
                        str = protocolInfo.text;
                    }
                } else {
                    str = protocolInfo.text;
                }
            } else if (i == 3) {
                str = protocolInfo.text;
            } else {
                str = i != 4 ? null : protocolInfo.reviewText;
            }
            String str2 = str;
            if (TextUtils.isEmpty(str2) && !protocolInfo.isHtml) {
                L.d("D", "empty  ProtocolText");
                return;
            }
            ProtocolInfo currentBaseProtocol = this.mProtocolManager.getCurrentBaseProtocol();
            boolean z = currentBaseProtocol != null && TextUtils.equals(currentBaseProtocol.url, protocolInfo.url);
            NewContentView contentView = getContentView(situation, this.mScene, this.mCurrentUser, this.mProtocolManager.getProtocolType(), protocolInfo, !z, this.mCurrentContentView.getKey(), z ? this.mViewStyle : 1, str2);
            this.mCurrentContentView = contentView;
            this.mContentViews.put(contentView.getKey(), contentView);
            setContentView(getView());
        } catch (Exception e) {
            L.e("D", "preDialog Exception : " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rejectProtocolConfirm(Context context) throws Resources.NotFoundException {
        String string;
        String str;
        if (SDKRuntime.getInstance().isHiddenClose() && Situation.LAUNCHER == this.mSituation) {
            return;
        }
        AlerterEx alerterEx = new AlerterEx(context);
        Resources resources = context.getResources();
        Locale protocolLocale = TextCompat.getProtocolLocale(this.mProtocolManager.getProp());
        if (protocolLocale != null) {
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            configuration.locale = protocolLocale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        String str2 = null;
        try {
            string = this.mProtocolInfo.globalInfo.alertMsg;
            try {
                str = this.mProtocolInfo.globalInfo.alertConfirm;
                try {
                    str2 = this.mProtocolInfo.globalInfo.alertCancel;
                } catch (Throwable unused) {
                }
            } catch (Throwable unused2) {
                str = null;
            }
        } catch (Throwable unused3) {
            string = null;
            str = null;
        }
        if (TextUtils.isEmpty(string)) {
            string = resources.getString(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_REJECT_CONFIRM_MSG, ResIdReader.RES_TYPE_STRING));
        }
        alerterEx.alert("  ", string, TextUtils.isEmpty(str) ? resources.getString(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_REJECT_CONFIRM_OK, ResIdReader.RES_TYPE_STRING)) : str, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ContentDialog.this.dismiss();
                ContentDialog.this.mProtocolManager.onDestroy(ContentDialog.this.getContext());
                ContentDialog.this.mProtocolManager.getCallback().onFinish(2);
                if (Situation.LAUNCHER == ContentDialog.this.mSituation) {
                    PluginManager.getInstance().exit(ContentDialog.this.mActivity, new Callback() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.4.1
                        @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                        public void onFinish(Result result) {
                            SysHelper.exitProcessInLaunch((Activity) ContentDialog.this.mContext);
                        }
                    });
                } else {
                    PluginManager.getInstance().exit(ContentDialog.this.mActivity, new Callback() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.4.2
                        @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                        public void onFinish(Result result) {
                            SysHelper.killProcess(ContentDialog.this.mContext);
                        }
                    });
                }
            }
        }, TextUtils.isEmpty(str2) ? resources.getString(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_REJECT_CONFIRM_BACK, ResIdReader.RES_TYPE_STRING)) : str2, new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.ContentDialog.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) throws JSONException {
                Tracker.getInstance().onEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE_RECONFIRM, String.valueOf(ContentDialog.this.mProtocolInfo.id), String.valueOf(ContentDialog.this.mProtocolInfo.version));
                dialogInterface.dismiss();
            }
        });
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        L.d("keycode:" + i + ", event:" + keyEvent.getKeyCode() + ",action:" + keyEvent.getAction());
        if (i == 22) {
            this.mCurrentContentView.requestFocus();
        }
        return super.onKeyDown(i, keyEvent);
    }

    public static class DialogInfo {
        public float mDensity;
        private WindowManager.LayoutParams mDialogLayoutParams;
        private int mScreenHeight;
        private int mScreenWidth;
        public int mViewPortWidth = 350;

        /* JADX INFO: Access modifiers changed from: private */
        public void computeDialogLayoutParams(Window window) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            this.mDialogLayoutParams = attributes;
            int i = this.mScreenWidth;
            if (i >= this.mScreenHeight) {
                if (isWideScreen()) {
                    WindowManager.LayoutParams layoutParams = this.mDialogLayoutParams;
                    double d = this.mScreenWidth;
                    Double.isNaN(d);
                    layoutParams.width = (int) (d * 0.77d);
                } else {
                    WindowManager.LayoutParams layoutParams2 = this.mDialogLayoutParams;
                    double d2 = this.mScreenWidth;
                    Double.isNaN(d2);
                    layoutParams2.width = (int) (d2 * 0.8d);
                }
                WindowManager.LayoutParams layoutParams3 = this.mDialogLayoutParams;
                double d3 = this.mScreenHeight;
                Double.isNaN(d3);
                layoutParams3.height = (int) (d3 * 0.84d);
            } else {
                double d4 = i;
                Double.isNaN(d4);
                attributes.width = (int) (d4 * 0.92d);
                WindowManager.LayoutParams layoutParams4 = this.mDialogLayoutParams;
                double d5 = this.mScreenHeight;
                Double.isNaN(d5);
                layoutParams4.height = (int) (d5 * 0.84d);
            }
            L.d("D", String.format(Locale.ROOT, "mDialogLayoutParams.width = %d,mDialogLayoutParams.height = %d, viewPortWidth=%d", Integer.valueOf(this.mDialogLayoutParams.width), Integer.valueOf(this.mDialogLayoutParams.height), Integer.valueOf(this.mViewPortWidth)));
            window.setAttributes(this.mDialogLayoutParams);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getScreenInfo(Activity activity) {
            int i;
            int i2;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            this.mDensity = displayMetrics.density == 0.0f ? 3.0f : displayMetrics.density;
            this.mScreenWidth = displayMetrics.widthPixels;
            this.mScreenHeight = displayMetrics.heightPixels;
            int i3 = displayMetrics.densityDpi;
            if ("vivo X21UD A".equals(Build.MODEL)) {
                if (i3 == 320 && this.mScreenWidth == 2201 && this.mScreenHeight == 1080) {
                    this.mScreenWidth = 1467;
                    this.mScreenHeight = 720;
                } else if (i3 == 320 && this.mScreenWidth == 1080 && this.mScreenHeight == 2201) {
                    this.mScreenWidth = 720;
                    this.mScreenHeight = 1467;
                }
            }
            L.d("D", "-- width:" + this.mScreenWidth + ",height:" + this.mScreenHeight + ",density:" + this.mDensity + ",densityDpi:" + i3);
            int gameActivityScreenOrientation = getGameActivityScreenOrientation(activity);
            if (gameActivityScreenOrientation == 1 || gameActivityScreenOrientation == 7) {
                int i4 = this.mScreenWidth;
                int i5 = this.mScreenHeight;
                if (i4 > i5) {
                    this.mScreenWidth = i5;
                    this.mScreenHeight = i4;
                    L.d("D", "correct-- width:" + this.mScreenWidth + ",height:" + this.mScreenHeight + ",density:" + this.mDensity + ",densityDpi:" + i3);
                }
            } else if ((gameActivityScreenOrientation == 0 || gameActivityScreenOrientation == 6) && (i = this.mScreenWidth) < (i2 = this.mScreenHeight)) {
                this.mScreenWidth = i2;
                this.mScreenHeight = i;
                L.d("D", "correct-- width:" + this.mScreenWidth + ",height:" + this.mScreenHeight + ",density:" + this.mDensity + ",densityDpi:" + i3);
            }
            if (isWideScreen()) {
                double d = this.mScreenWidth;
                double d2 = this.mDensity;
                Double.isNaN(d2);
                Double.isNaN(d);
                this.mViewPortWidth = (int) (d / (d2 + 0.5d));
                return;
            }
            this.mViewPortWidth = (int) (this.mScreenWidth / this.mDensity);
        }

        private int getGameActivityScreenOrientation(Activity activity) {
            return activity.getRequestedOrientation();
        }

        private boolean isWideScreen() {
            return (((float) this.mScreenWidth) * 1.0f) / ((float) this.mScreenHeight) > 1.7777778f;
        }
    }
}