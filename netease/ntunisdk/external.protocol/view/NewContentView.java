package com.netease.ntunisdk.external.protocol.view;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.Situation;
import com.netease.ntunisdk.external.protocol.Tracker;
import com.netease.ntunisdk.external.protocol.data.GlobalInfo;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.external.protocol.utils.AsyncTask;
import com.netease.ntunisdk.external.protocol.utils.CommonUtils;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.external.protocol.view.ContentDialog;
import com.netease.ntunisdk.external.protocol.view.ProtocolAdapter;
import com.netease.ntunisdk.external.protocol.view.ProtocolView;
import com.netease.ntunisdk.external.protocol.view.UniWebView;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class NewContentView implements ProtocolView.OnPageListener {
    public static final int BOTTOM_ONE_BUTTON = 1;
    public static final int BOTTOM_TWO_BUTTON = 2;
    private static final String TAG = "ContentView";
    protected Button acceptAllBtn;
    protected Button confirmBtn;
    protected boolean isHTMLProtocol = false;
    protected boolean isRTL;
    private final ArrayList<ProtocolInfo.ConcreteSubProtocol> mConcreteSubProtocols;
    protected UniWebView mContentWebView;
    protected User mCurrentUser;
    protected ContentDialog.DialogInfo mDialogInfo;
    protected EventCallback mEventCallback;
    protected boolean mIsSubProtocol;
    protected String mParent;
    private ProtocolAdapter mProtocolAdapter;
    protected ProtocolInfo mProtocolInfo;
    protected ProtocolProp mProtocolProp;
    protected int mProtocolType;
    protected String mScene;
    protected Situation mSituation;
    private final String mText;
    protected View mView;
    protected int mViewStyle;

    @Override // com.netease.ntunisdk.external.protocol.view.ProtocolView.OnPageListener
    public void nextPage() {
    }

    @Override // com.netease.ntunisdk.external.protocol.view.ProtocolView.OnPageListener
    public void openLink(int i) {
    }

    @Override // com.netease.ntunisdk.external.protocol.view.ProtocolView.OnPageListener
    public void prePage() {
    }

    public void setOnShowListener(UniWebView.OnShowListener onShowListener) {
    }

    public NewContentView(Situation situation, String str, User user, ProtocolProp protocolProp, int i, ProtocolInfo protocolInfo, boolean z, String str2, int i2, String str3, ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList) {
        this.isRTL = false;
        this.mSituation = situation;
        this.mScene = str;
        this.mCurrentUser = user;
        this.mProtocolProp = protocolProp;
        this.mProtocolType = i;
        this.mIsSubProtocol = z;
        this.mProtocolInfo = protocolInfo;
        this.mParent = str2;
        this.mViewStyle = i2;
        this.mText = str3;
        this.isRTL = protocolInfo.textAlign == 2 || SDKRuntime.getInstance().isRTLLayout();
        this.mConcreteSubProtocols = arrayList;
    }

    public int getViewStyle() {
        return this.mViewStyle;
    }

    public View onCreateView(Context context, ContentDialog.DialogInfo dialogInfo, EventCallback eventCallback) throws JSONException {
        this.mDialogInfo = dialogInfo;
        this.mEventCallback = eventCallback;
        try {
            if (this.mProtocolInfo.isHomeStyle()) {
                this.mView = getHomeView(context);
            } else if (SDKRuntime.getInstance().isShowContentByTextView()) {
                this.mView = getTextContentView(context);
            } else {
                boolean z = true;
                if (this.mContentWebView == null) {
                    try {
                        this.mContentWebView = new UniWebView(context);
                    } catch (Throwable unused) {
                        z = false;
                    }
                }
                this.mView = z ? getContentView(context) : getTextContentView(context);
            }
        } catch (Throwable unused2) {
            this.mView = getTextContentView(context);
        }
        postTrackerEvent(Tracker.EVENT_PROTOCOL_SHOW, this.mProtocolInfo);
        return this.mView;
    }

    private View getHomeView(Context context) {
        TextView textView;
        View viewInflate = LayoutInflater.from(context).inflate(ResUtils.getResId(context, "unisdk_protocol__home", ResIdReader.RES_TYPE_LAYOUT), (ViewGroup) null, false);
        if (this.isRTL) {
            CommonUtils.setViewRtlLayout(viewInflate);
        }
        if ("1".equals(this.mProtocolProp.getShowAgreeLineFlag())) {
            String agreeLineText = this.mProtocolProp.getAgreeLineText();
            if (!TextUtils.isEmpty(agreeLineText) && (textView = (TextView) viewInflate.findViewById(ResUtils.getResId(context, "protocol_agree_tv", ResIdReader.RES_TYPE_ID))) != null) {
                textView.setVisibility(0);
                textView.setText(agreeLineText);
            }
        }
        Button button = (Button) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_accept_btn", ResIdReader.RES_TYPE_ID));
        this.acceptAllBtn = (Button) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_accept_all_btn", ResIdReader.RES_TYPE_ID));
        View viewFindViewById = viewInflate.findViewById(ResUtils.getResId(context, "uni_p_reject_btn", ResIdReader.RES_TYPE_ID));
        GlobalInfo globalInfo = this.mProtocolInfo.globalInfo;
        if (this.mViewStyle == 1) {
            if (button != null) {
                button.setVisibility(8);
            }
            if (this.acceptAllBtn != null) {
                if (globalInfo != null && !TextUtils.isEmpty(globalInfo.confirm)) {
                    this.acceptAllBtn.setText(globalInfo.confirm);
                }
                this.acceptAllBtn.setVisibility(0);
                this.acceptAllBtn.requestFocus();
                this.acceptAllBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.1
                    AnonymousClass1() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) throws JSONException {
                        if (NewContentView.this.checkRequiredProtocol()) {
                            return;
                        }
                        NewContentView newContentView = NewContentView.this;
                        newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CONFIRM, newContentView.mProtocolInfo);
                        if (NewContentView.this.mEventCallback != null) {
                            NewContentView.this.mEventCallback.finish(NewContentView.this.mCurrentUser.isLogout() ? 0 : 4, null);
                        }
                    }
                });
            }
            if (viewFindViewById != null) {
                viewFindViewById.setVisibility(0);
                viewFindViewById.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.2
                    AnonymousClass2() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) throws JSONException {
                        NewContentView newContentView = NewContentView.this;
                        newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CANCEL, newContentView.mProtocolInfo);
                        if (NewContentView.this.mEventCallback != null) {
                            NewContentView.this.mEventCallback.finish(0, null);
                        }
                    }
                });
            }
        } else {
            if (button != null) {
                if (globalInfo != null && !TextUtils.isEmpty(globalInfo.accept)) {
                    button.setText(globalInfo.accept);
                }
                button.setVisibility(0);
                button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.3
                    AnonymousClass3() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) throws JSONException {
                        if (NewContentView.this.checkRequiredProtocol()) {
                            return;
                        }
                        NewContentView newContentView = NewContentView.this;
                        newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE, newContentView.mProtocolInfo);
                        if (NewContentView.this.mEventCallback != null) {
                            NewContentView.this.mEventCallback.finish(4, null);
                        }
                    }
                });
            }
            if (this.acceptAllBtn != null) {
                if (globalInfo != null && !TextUtils.isEmpty(globalInfo.acceptAll)) {
                    this.acceptAllBtn.setText(globalInfo.acceptAll);
                }
                this.acceptAllBtn.setVisibility(0);
                this.acceptAllBtn.requestFocus();
                this.acceptAllBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.4
                    AnonymousClass4() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) throws JSONException {
                        NewContentView newContentView = NewContentView.this;
                        newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE_ALL, newContentView.mProtocolInfo);
                        if (NewContentView.this.mEventCallback != null) {
                            EventCallback eventCallback = NewContentView.this.mEventCallback;
                            NewContentView newContentView2 = NewContentView.this;
                            eventCallback.finish(4, newContentView2.agreedProtocols(newContentView2.mCurrentUser.getUid(), NewContentView.this.mConcreteSubProtocols, true));
                        }
                    }
                });
            }
            if (viewFindViewById != null) {
                viewFindViewById.setVisibility((SDKRuntime.getInstance().isHiddenClose() && Situation.LAUNCHER == this.mSituation) ? 4 : 0);
                viewFindViewById.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.5
                    AnonymousClass5() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) throws JSONException {
                        NewContentView newContentView = NewContentView.this;
                        newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE, newContentView.mProtocolInfo);
                        if (NewContentView.this.mEventCallback != null) {
                            if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                                ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                                ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                            }
                            NewContentView.this.mEventCallback.finish(2, null);
                        }
                    }
                });
            }
        }
        TextView textView2 = (TextView) viewInflate.findViewById(ResUtils.getResId(context, Const.UNISDK_PROTOCOL_TITLE, ResIdReader.RES_TYPE_ID));
        if (textView2 != null) {
            textView2.setText(this.mProtocolInfo.displayName);
        }
        TextView textView3 = (TextView) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_tv", ResIdReader.RES_TYPE_ID));
        if (this.isRTL && Build.VERSION.SDK_INT >= 17) {
            textView3.setTextDirection(4);
        }
        textView3.setVisibility(0);
        textView3.setText(this.mText);
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(ResUtils.getResId(context, "unisdk_protocol_list", ResIdReader.RES_TYPE_ID));
        ProtocolAdapter protocolAdapter = new ProtocolAdapter(context, this.mConcreteSubProtocols, new AnonymousClass6(textView3));
        this.mProtocolAdapter = protocolAdapter;
        recyclerView.setAdapter(protocolAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        return viewInflate;
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$1 */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            if (NewContentView.this.checkRequiredProtocol()) {
                return;
            }
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CONFIRM, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                NewContentView.this.mEventCallback.finish(NewContentView.this.mCurrentUser.isLogout() ? 0 : 4, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$2 */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CANCEL, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                NewContentView.this.mEventCallback.finish(0, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$3 */
    class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            if (NewContentView.this.checkRequiredProtocol()) {
                return;
            }
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                NewContentView.this.mEventCallback.finish(4, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$4 */
    class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE_ALL, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                EventCallback eventCallback = NewContentView.this.mEventCallback;
                NewContentView newContentView2 = NewContentView.this;
                eventCallback.finish(4, newContentView2.agreedProtocols(newContentView2.mCurrentUser.getUid(), NewContentView.this.mConcreteSubProtocols, true));
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$5 */
    class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                NewContentView.this.mEventCallback.finish(2, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$6 */
    class AnonymousClass6 implements ProtocolAdapter.OpenProtocolCallback {
        final /* synthetic */ TextView val$contentView;

        AnonymousClass6(TextView textView) {
            this.val$contentView = textView;
        }

        @Override // com.netease.ntunisdk.external.protocol.view.ProtocolAdapter.OpenProtocolCallback
        public void onOpen(ProtocolInfo.ConcreteSubProtocol concreteSubProtocol) {
            ProtocolInfo.SubProtocolInfo subProtocolInfo;
            if (concreteSubProtocol == null || NewContentView.this.mProtocolInfo == null || (subProtocolInfo = concreteSubProtocol.mSubProtocolInfo) == null || TextUtils.isEmpty(subProtocolInfo.mUrl)) {
                return;
            }
            if (NewContentView.this.mProtocolInfo.subProtocolUrls.contains(subProtocolInfo.mUrl)) {
                String strMd5 = TextCompat.md5(subProtocolInfo.mUrl);
                if (!concreteSubProtocol.isHasAsync()) {
                    concreteSubProtocol.setHasAsync(subProtocolInfo.isLocal());
                }
                AsyncTask.execute(strMd5, new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.6.1
                    final /* synthetic */ ProtocolInfo.ConcreteSubProtocol val$concreteSubProtocol;
                    final /* synthetic */ ProtocolInfo.SubProtocolInfo val$subInfo;

                    AnonymousClass1(ProtocolInfo.SubProtocolInfo subProtocolInfo2, ProtocolInfo.ConcreteSubProtocol concreteSubProtocol2) {
                        subProtocolInfo = subProtocolInfo2;
                        concreteSubProtocol = concreteSubProtocol2;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        ProtocolInfo protocolInfoDownloadAndCheckNewProtocol;
                        ProtocolInfo protocolInfoFindProtocolByUrl = NewContentView.this.mProtocolInfo.findProtocolByUrl(subProtocolInfo.mUrl);
                        if (!concreteSubProtocol.isHasAsync() || protocolInfoFindProtocolByUrl == null || TextUtils.isEmpty(protocolInfoFindProtocolByUrl.text)) {
                            protocolInfoDownloadAndCheckNewProtocol = ProtocolManager.getInstance().getProvider().downloadAndCheckNewProtocol(NewContentView.this.mProtocolInfo.getProtocolFile(), protocolInfoFindProtocolByUrl, subProtocolInfo.mUrl);
                            concreteSubProtocol.setHasAsync(true);
                        } else {
                            protocolInfoDownloadAndCheckNewProtocol = protocolInfoFindProtocolByUrl;
                        }
                        if (protocolInfoDownloadAndCheckNewProtocol != null) {
                            protocolInfoFindProtocolByUrl = protocolInfoDownloadAndCheckNewProtocol;
                        } else if (protocolInfoFindProtocolByUrl == null || TextUtils.isEmpty(protocolInfoFindProtocolByUrl.text)) {
                            return;
                        }
                        if (protocolInfoFindProtocolByUrl.globalInfo == null) {
                            protocolInfoFindProtocolByUrl.globalInfo = NewContentView.this.mProtocolInfo.globalInfo;
                        }
                        AnonymousClass6.this.val$contentView.post(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.6.1.1
                            final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                            RunnableC00781(ProtocolInfo protocolInfoFindProtocolByUrl2) {
                                protocolInfo = protocolInfoFindProtocolByUrl2;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (NewContentView.this.mEventCallback != null) {
                                    if (subProtocolInfo.mUrl.equals(NewContentView.this.mParent)) {
                                        NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                                    } else {
                                        NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                                    }
                                }
                            }
                        });
                    }

                    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$6$1$1 */
                    class RunnableC00781 implements Runnable {
                        final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                        RunnableC00781(ProtocolInfo protocolInfoFindProtocolByUrl2) {
                            protocolInfo = protocolInfoFindProtocolByUrl2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (NewContentView.this.mEventCallback != null) {
                                if (subProtocolInfo.mUrl.equals(NewContentView.this.mParent)) {
                                    NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                                } else {
                                    NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                                }
                            }
                        }
                    }
                });
                return;
            }
            if (subProtocolInfo2.mUrl.endsWith(".json")) {
                return;
            }
            NewContentView.this.mEventCallback.openBrowser(subProtocolInfo2.mUrl);
        }

        /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$6$1 */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ ProtocolInfo.ConcreteSubProtocol val$concreteSubProtocol;
            final /* synthetic */ ProtocolInfo.SubProtocolInfo val$subInfo;

            AnonymousClass1(ProtocolInfo.SubProtocolInfo subProtocolInfo2, ProtocolInfo.ConcreteSubProtocol concreteSubProtocol2) {
                subProtocolInfo = subProtocolInfo2;
                concreteSubProtocol = concreteSubProtocol2;
            }

            @Override // java.lang.Runnable
            public void run() {
                ProtocolInfo protocolInfoDownloadAndCheckNewProtocol;
                ProtocolInfo protocolInfoFindProtocolByUrl2 = NewContentView.this.mProtocolInfo.findProtocolByUrl(subProtocolInfo.mUrl);
                if (!concreteSubProtocol.isHasAsync() || protocolInfoFindProtocolByUrl2 == null || TextUtils.isEmpty(protocolInfoFindProtocolByUrl2.text)) {
                    protocolInfoDownloadAndCheckNewProtocol = ProtocolManager.getInstance().getProvider().downloadAndCheckNewProtocol(NewContentView.this.mProtocolInfo.getProtocolFile(), protocolInfoFindProtocolByUrl2, subProtocolInfo.mUrl);
                    concreteSubProtocol.setHasAsync(true);
                } else {
                    protocolInfoDownloadAndCheckNewProtocol = protocolInfoFindProtocolByUrl2;
                }
                if (protocolInfoDownloadAndCheckNewProtocol != null) {
                    protocolInfoFindProtocolByUrl2 = protocolInfoDownloadAndCheckNewProtocol;
                } else if (protocolInfoFindProtocolByUrl2 == null || TextUtils.isEmpty(protocolInfoFindProtocolByUrl2.text)) {
                    return;
                }
                if (protocolInfoFindProtocolByUrl2.globalInfo == null) {
                    protocolInfoFindProtocolByUrl2.globalInfo = NewContentView.this.mProtocolInfo.globalInfo;
                }
                AnonymousClass6.this.val$contentView.post(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.6.1.1
                    final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                    RunnableC00781(ProtocolInfo protocolInfoFindProtocolByUrl22) {
                        protocolInfo = protocolInfoFindProtocolByUrl22;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (NewContentView.this.mEventCallback != null) {
                            if (subProtocolInfo.mUrl.equals(NewContentView.this.mParent)) {
                                NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                            } else {
                                NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                            }
                        }
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$6$1$1 */
            class RunnableC00781 implements Runnable {
                final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                RunnableC00781(ProtocolInfo protocolInfoFindProtocolByUrl22) {
                    protocolInfo = protocolInfoFindProtocolByUrl22;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (NewContentView.this.mEventCallback != null) {
                        if (subProtocolInfo.mUrl.equals(NewContentView.this.mParent)) {
                            NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                        } else {
                            NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                        }
                    }
                }
            }
        }
    }

    private JSONObject agreedProtocols(String str, ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList) {
        return agreedProtocols(str, arrayList, false);
    }

    public JSONObject agreedProtocols(String str, ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList, boolean z) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (Situation.LAUNCHER == this.mSituation) {
            ProtocolManager.getInstance().setLaunchProtocolId(this.mProtocolInfo.id);
            ProtocolManager.getInstance().setLaunchProtocolVersion(this.mProtocolInfo.version);
        }
        try {
            jSONObject.put("methodId", "agreedProtocols");
            jSONObject.put("uid", str);
            JSONArray jSONArray = new JSONArray();
            Iterator<ProtocolInfo.ConcreteSubProtocol> it = arrayList.iterator();
            while (it.hasNext()) {
                ProtocolInfo.ConcreteSubProtocol next = it.next();
                if (z) {
                    next.setChecked(true);
                }
                if (next.isChecked()) {
                    jSONArray.put(next.mSubProtocolInfo.getAlias());
                }
            }
            jSONObject.put("protocols", jSONArray);
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    public final void updateView() {
        ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList;
        ProtocolAdapter protocolAdapter;
        Button button = this.confirmBtn;
        if (button != null) {
            button.requestFocus();
        }
        Button button2 = this.acceptAllBtn;
        if (button2 != null) {
            button2.requestFocus();
        }
        if (this.mCurrentUser.isLogout() || (arrayList = this.mConcreteSubProtocols) == null || arrayList.isEmpty() || !this.mProtocolInfo.isHomeStyle() || (protocolAdapter = this.mProtocolAdapter) == null) {
            return;
        }
        protocolAdapter.notifyDataSetChanged();
    }

    public boolean checkRequiredProtocol() {
        boolean z = false;
        if ((Situation.REVIEW == this.mSituation && (this.mCurrentUser.isLauncher() || this.mCurrentUser.isLogout())) || this.mCurrentUser.isLogout()) {
            return false;
        }
        ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList = this.mConcreteSubProtocols;
        if (arrayList != null && !arrayList.isEmpty()) {
            Iterator<ProtocolInfo.ConcreteSubProtocol> it = this.mConcreteSubProtocols.iterator();
            while (it.hasNext()) {
                ProtocolInfo.ConcreteSubProtocol next = it.next();
                if (!next.isChecked() && next.isRequired()) {
                    next.setWarning(next.isRequired());
                    z = true;
                }
            }
            if (z && this.mProtocolAdapter != null) {
                ProtocolInfo protocolInfo = this.mProtocolInfo;
                if (protocolInfo != null && protocolInfo.globalInfo != null) {
                    AlerterEx.showToast(this.mView.getContext(), this.mProtocolInfo.globalInfo.missingTips);
                }
                this.mProtocolAdapter.notifyDataSetChanged();
            }
        }
        return z;
    }

    private View getTextContentView(Context context) {
        String str;
        TextView textView;
        View viewInflate = LayoutInflater.from(context).inflate(ResUtils.getResId(context, "unisdk_protocol__text_content", ResIdReader.RES_TYPE_LAYOUT), (ViewGroup) null);
        if (this.isRTL) {
            CommonUtils.setViewRtlLayout(viewInflate);
        }
        ImageView imageView = (ImageView) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_logo_iv", ResIdReader.RES_TYPE_ID));
        int i = this.mProtocolType;
        if (1 == i) {
            imageView.setImageResource(ResUtils.getResId(context, "unisdk_protocol_logo_long", ResIdReader.RES_TYPE_DRAWABLE));
        } else if (2 == i) {
            imageView.setImageResource(ResUtils.getResId(context, "unisdk_protocol_logo_envoy", ResIdReader.RES_TYPE_DRAWABLE));
        }
        if (SDKRuntime.getInstance().isHideLogo()) {
            imageView.setVisibility(4);
        }
        if ("1".equals(this.mProtocolProp.getShowAgreeLineFlag())) {
            String agreeLineText = this.mProtocolProp.getAgreeLineText();
            if (!TextUtils.isEmpty(agreeLineText) && (textView = (TextView) viewInflate.findViewById(ResUtils.getResId(context, "protocol_agree_tv", ResIdReader.RES_TYPE_ID))) != null) {
                textView.setVisibility(0);
                textView.setText(agreeLineText);
            }
        }
        this.confirmBtn = (Button) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_confirm_btn", ResIdReader.RES_TYPE_ID));
        Button button = (Button) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_reject_btn", ResIdReader.RES_TYPE_ID));
        GlobalInfo globalInfo = this.mProtocolInfo.globalInfo;
        if (this.mViewStyle == 1) {
            if (globalInfo != null) {
                if (!TextUtils.isEmpty(globalInfo.confirm)) {
                    this.confirmBtn.setText(globalInfo.confirm);
                }
            } else {
                this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_CONFIRM));
            }
            button.setVisibility(8);
            this.confirmBtn.setVisibility(0);
            this.confirmBtn.setPadding(0, 0, 0, 0);
            this.confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.7
                AnonymousClass7() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws JSONException {
                    NewContentView newContentView = NewContentView.this;
                    newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CONFIRM, newContentView.mProtocolInfo);
                    if (NewContentView.this.mIsSubProtocol) {
                        L.d(NewContentView.TAG, "parent:" + NewContentView.this.mParent);
                        if (NewContentView.this.mEventCallback != null) {
                            NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                            return;
                        }
                        return;
                    }
                    if (NewContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                        }
                        NewContentView.this.mEventCallback.finish(0, null);
                    }
                }
            });
        } else {
            if (globalInfo != null) {
                if (!TextUtils.isEmpty(globalInfo.accept)) {
                    this.confirmBtn.setText(globalInfo.accept);
                }
                if (!TextUtils.isEmpty(globalInfo.reject)) {
                    button.setText(globalInfo.reject);
                }
            } else {
                this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_ACCEPT));
                button.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_REJECT));
            }
            this.confirmBtn.setVisibility(0);
            this.confirmBtn.requestFocus();
            this.confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.8
                AnonymousClass8() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws JSONException {
                    NewContentView newContentView = NewContentView.this;
                    newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE, newContentView.mProtocolInfo);
                    if (NewContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                        }
                        NewContentView.this.mEventCallback.finish(1, null);
                    }
                }
            });
            button.setVisibility(0);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.9
                AnonymousClass9() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws JSONException {
                    NewContentView newContentView = NewContentView.this;
                    newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE, newContentView.mProtocolInfo);
                    if (NewContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                        }
                        NewContentView.this.mEventCallback.finish(2, null);
                    }
                }
            });
        }
        button.setPadding(0, 0, 0, 0);
        this.confirmBtn.setPadding(0, 0, 0, 0);
        TextView textView2 = (TextView) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_content_text_view", ResIdReader.RES_TYPE_ID));
        int i2 = Build.VERSION.SDK_INT;
        String str2 = Const.HTML_RTL;
        if (i2 < 21) {
            Locale locale = Locale.ROOT;
            Object[] objArr = new Object[2];
            if (!this.isRTL) {
                str2 = "";
            }
            objArr[0] = str2;
            objArr[1] = this.mProtocolInfo.text;
            str = String.format(locale, Const.HTML_TEMPLATE_OLD, objArr);
        } else {
            L.d(TAG, "viewport:" + this.mDialogInfo.mViewPortWidth);
            Locale locale2 = Locale.ROOT;
            Object[] objArr2 = new Object[3];
            if (!this.isRTL) {
                str2 = "";
            }
            objArr2[0] = str2;
            objArr2[1] = Integer.valueOf(this.mDialogInfo.mViewPortWidth);
            objArr2[2] = this.mProtocolInfo.text;
            str = String.format(locale2, Const.HTML_TEMPLATE, objArr2);
        }
        Spanned spannedFromHtml = Html.fromHtml(str);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannedFromHtml);
        for (URLSpan uRLSpan : (URLSpan[]) spannableStringBuilder.getSpans(0, spannedFromHtml.length(), URLSpan.class)) {
            spannableStringBuilder.setSpan(new ClickableSpan() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.10
                final /* synthetic */ URLSpan val$urlSpan;

                AnonymousClass10(URLSpan uRLSpan2) {
                    uRLSpan = uRLSpan2;
                }

                @Override // android.text.style.ClickableSpan
                public void onClick(View view) {
                    String url = uRLSpan.getURL();
                    try {
                        URL url2 = new URL(url);
                        String host = url2.getHost();
                        L.d("url host:" + host);
                        String path = url2.getPath();
                        L.d("url path:" + host);
                        if (path.endsWith(".json")) {
                            url = url.replace(path, path.replace(".json", ".html"));
                        }
                        L.d(NewContentView.TAG, "onClickURL:" + url);
                        if (NewContentView.this.mProtocolInfo.addParamsHost.contains(host)) {
                            url = TextCompat.wrapperUrl(url);
                        }
                    } catch (Throwable unused) {
                    }
                    L.d("openBrowser:" + url);
                    NewContentView.this.mEventCallback.openBrowser(url);
                }
            }, spannableStringBuilder.getSpanStart(uRLSpan2), spannableStringBuilder.getSpanEnd(uRLSpan2), spannableStringBuilder.getSpanFlags(uRLSpan2));
        }
        textView2.setText(spannableStringBuilder);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        return viewInflate;
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$7 */
    class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CONFIRM, newContentView.mProtocolInfo);
            if (NewContentView.this.mIsSubProtocol) {
                L.d(NewContentView.TAG, "parent:" + NewContentView.this.mParent);
                if (NewContentView.this.mEventCallback != null) {
                    NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                    return;
                }
                return;
            }
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                NewContentView.this.mEventCallback.finish(0, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$8 */
    class AnonymousClass8 implements View.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                NewContentView.this.mEventCallback.finish(1, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$9 */
    class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                NewContentView.this.mEventCallback.finish(2, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$10 */
    class AnonymousClass10 extends ClickableSpan {
        final /* synthetic */ URLSpan val$urlSpan;

        AnonymousClass10(URLSpan uRLSpan2) {
            uRLSpan = uRLSpan2;
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            String url = uRLSpan.getURL();
            try {
                URL url2 = new URL(url);
                String host = url2.getHost();
                L.d("url host:" + host);
                String path = url2.getPath();
                L.d("url path:" + host);
                if (path.endsWith(".json")) {
                    url = url.replace(path, path.replace(".json", ".html"));
                }
                L.d(NewContentView.TAG, "onClickURL:" + url);
                if (NewContentView.this.mProtocolInfo.addParamsHost.contains(host)) {
                    url = TextCompat.wrapperUrl(url);
                }
            } catch (Throwable unused) {
            }
            L.d("openBrowser:" + url);
            NewContentView.this.mEventCallback.openBrowser(url);
        }
    }

    public void requestFocus() {
        Button button = this.confirmBtn;
        if (button != null) {
            button.requestFocus();
        }
        Button button2 = this.acceptAllBtn;
        if (button2 != null) {
            button2.requestFocus();
        }
    }

    private View getContentView(Context context) {
        String str;
        TextView textView;
        View viewInflate = LayoutInflater.from(context).inflate(ResUtils.getResId(context, "unisdk_protocol__content", ResIdReader.RES_TYPE_LAYOUT), (ViewGroup) null);
        if (this.isRTL) {
            CommonUtils.setViewRtlLayout(viewInflate);
        }
        viewInflate.requestFocus();
        ImageView imageView = (ImageView) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_logo_iv", ResIdReader.RES_TYPE_ID));
        int i = this.mProtocolType;
        if (1 == i) {
            imageView.setImageResource(ResUtils.getResId(context, "unisdk_protocol_logo_long", ResIdReader.RES_TYPE_DRAWABLE));
        } else if (2 == i) {
            imageView.setImageResource(ResUtils.getResId(context, "unisdk_protocol_logo_envoy", ResIdReader.RES_TYPE_DRAWABLE));
        }
        if (SDKRuntime.getInstance().isHideLogo()) {
            imageView.setVisibility(4);
        }
        if ("1".equals(this.mProtocolProp.getShowAgreeLineFlag())) {
            String agreeLineText = this.mProtocolProp.getAgreeLineText();
            if (!TextUtils.isEmpty(agreeLineText) && (textView = (TextView) viewInflate.findViewById(ResUtils.getResId(context, "protocol_agree_tv", ResIdReader.RES_TYPE_ID))) != null) {
                textView.setVisibility(0);
                textView.setText(agreeLineText);
            }
        }
        this.confirmBtn = (Button) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_confirm_btn", ResIdReader.RES_TYPE_ID));
        Button button = (Button) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_reject_btn", ResIdReader.RES_TYPE_ID));
        GlobalInfo globalInfo = this.mProtocolInfo.globalInfo;
        if (this.mViewStyle == 1) {
            if (globalInfo != null) {
                if (SDKRuntime.getInstance().isSilentMode() && this.mSituation != Situation.REVIEW) {
                    if (!TextUtils.isEmpty(globalInfo.accept)) {
                        this.confirmBtn.setText(globalInfo.accept);
                    } else {
                        this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_ACCEPT));
                    }
                } else if (!TextUtils.isEmpty(globalInfo.confirm)) {
                    this.confirmBtn.setText(globalInfo.confirm);
                } else {
                    this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_CONFIRM));
                }
            } else if (SDKRuntime.getInstance().isSilentMode() && this.mSituation != Situation.REVIEW) {
                this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_ACCEPT));
            } else {
                this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_CONFIRM));
            }
            button.setVisibility(8);
            this.confirmBtn.setVisibility(0);
            this.confirmBtn.requestFocus();
            this.confirmBtn.setPadding(0, 0, 0, 0);
            this.confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.11
                AnonymousClass11() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws JSONException {
                    NewContentView newContentView = NewContentView.this;
                    newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CONFIRM, newContentView.mProtocolInfo);
                    if (NewContentView.this.mIsSubProtocol) {
                        L.d(NewContentView.TAG, "parent:" + NewContentView.this.mParent);
                        if (NewContentView.this.mEventCallback != null) {
                            NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                            return;
                        }
                        return;
                    }
                    if (NewContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                        }
                        if (SDKRuntime.getInstance().isSilentMode() && NewContentView.this.mSituation != Situation.REVIEW) {
                            NewContentView.this.mEventCallback.finish(1, null);
                        } else {
                            NewContentView.this.mEventCallback.finish(0, null);
                        }
                    }
                }
            });
        } else {
            if (globalInfo != null) {
                if (!TextUtils.isEmpty(globalInfo.accept)) {
                    this.confirmBtn.setText(globalInfo.accept);
                }
                if (!TextUtils.isEmpty(globalInfo.reject)) {
                    button.setText(globalInfo.reject);
                }
            } else {
                this.confirmBtn.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_ACCEPT));
                button.setText(ResUtils.getString(context, Const.UNISDK_PROTOCOL_REJECT));
            }
            this.confirmBtn.setVisibility(0);
            this.confirmBtn.requestFocus();
            this.confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.12
                AnonymousClass12() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws JSONException {
                    NewContentView newContentView = NewContentView.this;
                    newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE, newContentView.mProtocolInfo);
                    if (NewContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                        }
                        NewContentView.this.mEventCallback.finish(1, null);
                    }
                }
            });
            button.setVisibility(0);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.13
                AnonymousClass13() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws JSONException {
                    NewContentView newContentView = NewContentView.this;
                    newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE, newContentView.mProtocolInfo);
                    if (NewContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                        }
                        NewContentView.this.mEventCallback.finish(2, null);
                    }
                }
            });
        }
        button.setPadding(0, 0, 0, 0);
        this.confirmBtn.setPadding(0, 0, 0, 0);
        UniWebView.getConfig().setLoadLocalJS(false);
        if (this.mContentWebView == null) {
            this.mContentWebView = new UniWebView(context);
        }
        this.mContentWebView.setVisibility(4);
        this.mContentWebView.initWebView();
        this.mContentWebView.setSupportClearFocus(true);
        this.mContentWebView.setHTMLProtocol(this.isHTMLProtocol);
        int i2 = Build.VERSION.SDK_INT;
        String str2 = Const.HTML_RTL;
        if (i2 < 21) {
            Locale locale = Locale.ROOT;
            Object[] objArr = new Object[2];
            if (!this.isRTL) {
                str2 = "";
            }
            objArr[0] = str2;
            objArr[1] = this.mProtocolInfo.text;
            str = String.format(locale, Const.HTML_TEMPLATE_OLD, objArr);
        } else {
            L.d(TAG, "viewport:" + this.mDialogInfo.mViewPortWidth);
            Locale locale2 = Locale.ROOT;
            Object[] objArr2 = new Object[3];
            if (!this.isRTL) {
                str2 = "";
            }
            objArr2[0] = str2;
            objArr2[1] = Integer.valueOf(this.mDialogInfo.mViewPortWidth);
            objArr2[2] = this.mProtocolInfo.text;
            str = String.format(locale2, Const.HTML_TEMPLATE, objArr2);
        }
        this.mContentWebView.loadDataWithBaseURL(Const.HTML_LOCAL_PATH, str, Const.HTML_CONTENT_TYPE, "UTF-8", null);
        this.mContentWebView.setOnUrlLoadingListener(new AnonymousClass14());
        ((FrameLayout) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_content", ResIdReader.RES_TYPE_ID))).addView(this.mContentWebView, new FrameLayout.LayoutParams(-1, -1));
        return viewInflate;
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$11 */
    class AnonymousClass11 implements View.OnClickListener {
        AnonymousClass11() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_CONFIRM, newContentView.mProtocolInfo);
            if (NewContentView.this.mIsSubProtocol) {
                L.d(NewContentView.TAG, "parent:" + NewContentView.this.mParent);
                if (NewContentView.this.mEventCallback != null) {
                    NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                    return;
                }
                return;
            }
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                if (SDKRuntime.getInstance().isSilentMode() && NewContentView.this.mSituation != Situation.REVIEW) {
                    NewContentView.this.mEventCallback.finish(1, null);
                } else {
                    NewContentView.this.mEventCallback.finish(0, null);
                }
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$12 */
    class AnonymousClass12 implements View.OnClickListener {
        AnonymousClass12() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_AGREE, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                NewContentView.this.mEventCallback.finish(1, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$13 */
    class AnonymousClass13 implements View.OnClickListener {
        AnonymousClass13() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws JSONException {
            NewContentView newContentView = NewContentView.this;
            newContentView.postTrackerEvent(Tracker.EVENT_PROTOCOL_CLICK_REFUSE, newContentView.mProtocolInfo);
            if (NewContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == NewContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(NewContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(NewContentView.this.mProtocolInfo.version);
                }
                NewContentView.this.mEventCallback.finish(2, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$14 */
    class AnonymousClass14 implements UniWebView.OnUrlLoadingListener {
        AnonymousClass14() {
        }

        @Override // com.netease.ntunisdk.external.protocol.view.UniWebView.OnUrlLoadingListener
        public boolean shouldOverrideUrlLoading(String str) {
            String host;
            if (NewContentView.this.mProtocolInfo.subProtocolUrls.contains(str)) {
                AsyncTask.execute(TextCompat.md5(str), new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.14.1
                    final /* synthetic */ String val$url;

                    AnonymousClass1(String str2) {
                        str = str2;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        ProtocolInfo protocolInfoDownloadAndCheckNewProtocol = ProtocolManager.getInstance().getProvider().downloadAndCheckNewProtocol(NewContentView.this.mProtocolInfo.getProtocolFile(), null, str);
                        if (protocolInfoDownloadAndCheckNewProtocol == null && ((protocolInfoDownloadAndCheckNewProtocol = NewContentView.this.mProtocolInfo.findProtocolByUrl(str)) == null || TextUtils.isEmpty(protocolInfoDownloadAndCheckNewProtocol.text))) {
                            return;
                        }
                        if (protocolInfoDownloadAndCheckNewProtocol.globalInfo == null) {
                            protocolInfoDownloadAndCheckNewProtocol.globalInfo = NewContentView.this.mProtocolInfo.globalInfo;
                        }
                        NewContentView.this.mContentWebView.post(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.14.1.1
                            final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                            RunnableC00771(ProtocolInfo protocolInfoDownloadAndCheckNewProtocol2) {
                                protocolInfo = protocolInfoDownloadAndCheckNewProtocol2;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (NewContentView.this.mEventCallback != null) {
                                    if (str.equals(NewContentView.this.mParent)) {
                                        NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                                    } else {
                                        NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                                    }
                                }
                            }
                        });
                    }

                    /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$14$1$1 */
                    class RunnableC00771 implements Runnable {
                        final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                        RunnableC00771(ProtocolInfo protocolInfoDownloadAndCheckNewProtocol2) {
                            protocolInfo = protocolInfoDownloadAndCheckNewProtocol2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (NewContentView.this.mEventCallback != null) {
                                if (str.equals(NewContentView.this.mParent)) {
                                    NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                                } else {
                                    NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                                }
                            }
                        }
                    }
                });
                return true;
            }
            try {
                host = new URL(str2).getHost();
                L.d("url host:" + host);
            } catch (Throwable unused) {
            }
            String strWrapperUrl = NewContentView.this.mProtocolInfo.addParamsHost.contains(host) ? TextCompat.wrapperUrl(str2) : str2;
            L.d("openBrowser:" + str2);
            NewContentView.this.mEventCallback.openBrowser(strWrapperUrl);
            return true;
        }

        /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$14$1 */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ String val$url;

            AnonymousClass1(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                ProtocolInfo protocolInfoDownloadAndCheckNewProtocol2 = ProtocolManager.getInstance().getProvider().downloadAndCheckNewProtocol(NewContentView.this.mProtocolInfo.getProtocolFile(), null, str);
                if (protocolInfoDownloadAndCheckNewProtocol2 == null && ((protocolInfoDownloadAndCheckNewProtocol2 = NewContentView.this.mProtocolInfo.findProtocolByUrl(str)) == null || TextUtils.isEmpty(protocolInfoDownloadAndCheckNewProtocol2.text))) {
                    return;
                }
                if (protocolInfoDownloadAndCheckNewProtocol2.globalInfo == null) {
                    protocolInfoDownloadAndCheckNewProtocol2.globalInfo = NewContentView.this.mProtocolInfo.globalInfo;
                }
                NewContentView.this.mContentWebView.post(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.view.NewContentView.14.1.1
                    final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                    RunnableC00771(ProtocolInfo protocolInfoDownloadAndCheckNewProtocol22) {
                        protocolInfo = protocolInfoDownloadAndCheckNewProtocol22;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (NewContentView.this.mEventCallback != null) {
                            if (str.equals(NewContentView.this.mParent)) {
                                NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                            } else {
                                NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                            }
                        }
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.external.protocol.view.NewContentView$14$1$1 */
            class RunnableC00771 implements Runnable {
                final /* synthetic */ ProtocolInfo val$finalSubProtocol;

                RunnableC00771(ProtocolInfo protocolInfoDownloadAndCheckNewProtocol22) {
                    protocolInfo = protocolInfoDownloadAndCheckNewProtocol22;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (NewContentView.this.mEventCallback != null) {
                        if (str.equals(NewContentView.this.mParent)) {
                            NewContentView.this.mEventCallback.back(NewContentView.this.mParent);
                        } else {
                            NewContentView.this.mEventCallback.next(NewContentView.this.mSituation, protocolInfo, 1);
                        }
                    }
                }
            }
        }
    }

    public View getView() {
        return this.mView;
    }

    public String getParent() {
        return this.mParent;
    }

    public boolean isSubProtocol() {
        return this.mIsSubProtocol;
    }

    public boolean isHtmlProtocol() {
        ProtocolInfo protocolInfo = this.mProtocolInfo;
        return protocolInfo != null && protocolInfo.isHtml;
    }

    public String getKey() {
        return this.mProtocolInfo.url;
    }

    public void postTrackerEvent(String str, ProtocolInfo protocolInfo) throws JSONException {
        if (protocolInfo != null) {
            Tracker.getInstance().onEvent(str, String.valueOf(this.mProtocolInfo.id), String.valueOf(this.mProtocolInfo.version));
        }
    }
}