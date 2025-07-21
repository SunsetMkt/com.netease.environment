package com.netease.ntunisdk.external.protocol.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.ProtocolManager;
import com.netease.ntunisdk.external.protocol.Situation;
import com.netease.ntunisdk.external.protocol.data.GlobalInfo;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.view.ContentDialog;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class UrlContentView extends NewContentView {
    private static final String TAG = "UrlContentView";

    public UrlContentView(Situation situation, String str, User user, ProtocolProp protocolProp, int i, ProtocolInfo protocolInfo, String str2, ArrayList<ProtocolInfo.ConcreteSubProtocol> arrayList) {
        super(situation, str, user, protocolProp, i, protocolInfo, true, str2, 1, null, arrayList);
        this.isHTMLProtocol = true;
    }

    @Override // com.netease.ntunisdk.external.protocol.view.NewContentView
    public View onCreateView(Context context, ContentDialog.DialogInfo dialogInfo, EventCallback eventCallback) {
        this.mDialogInfo = dialogInfo;
        this.mEventCallback = eventCallback;
        this.mView = getContentView(context);
        return this.mView;
    }

    private View getContentView(Context context) {
        TextView textView;
        View viewInflate = LayoutInflater.from(context).inflate(ResUtils.getResId(context, "unisdk_protocol__content", ResIdReader.RES_TYPE_LAYOUT), (ViewGroup) null);
        ImageView imageView = (ImageView) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_logo_iv", ResIdReader.RES_TYPE_ID));
        if (1 == this.mProtocolType) {
            imageView.setImageResource(ResUtils.getResId(context, "unisdk_protocol_logo_long", ResIdReader.RES_TYPE_DRAWABLE));
        } else if (2 == this.mProtocolType) {
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
            this.confirmBtn.requestFocus();
            this.confirmBtn.setPadding(0, 0, 0, 0);
            this.confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.UrlContentView.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (UrlContentView.this.mIsSubProtocol) {
                        L.d(UrlContentView.TAG, "parent:" + UrlContentView.this.mParent);
                        if (UrlContentView.this.mEventCallback != null) {
                            if (TextUtils.isEmpty(UrlContentView.this.mParent)) {
                                if (UrlContentView.this.mContentWebView != null && UrlContentView.this.mContentWebView.canGoBack()) {
                                    UrlContentView.this.mContentWebView.goBack();
                                    return;
                                } else {
                                    UrlContentView.this.mEventCallback.finish(0, null);
                                    return;
                                }
                            }
                            UrlContentView.this.mEventCallback.back(UrlContentView.this.mParent);
                            return;
                        }
                        return;
                    }
                    if (UrlContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == UrlContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(UrlContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(UrlContentView.this.mProtocolInfo.version);
                        }
                        UrlContentView.this.mEventCallback.finish(0, null);
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
            this.confirmBtn.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.UrlContentView.2
                AnonymousClass2() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (UrlContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == UrlContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(UrlContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(UrlContentView.this.mProtocolInfo.version);
                        }
                        UrlContentView.this.mEventCallback.finish(1, null);
                    }
                }
            });
            button.setVisibility(0);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.view.UrlContentView.3
                AnonymousClass3() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (UrlContentView.this.mEventCallback != null) {
                        if (Situation.LAUNCHER == UrlContentView.this.mSituation) {
                            ProtocolManager.getInstance().setLaunchProtocolId(UrlContentView.this.mProtocolInfo.id);
                            ProtocolManager.getInstance().setLaunchProtocolVersion(UrlContentView.this.mProtocolInfo.version);
                        }
                        UrlContentView.this.mEventCallback.finish(2, null);
                    }
                }
            });
        }
        button.setPadding(0, 0, 0, 0);
        this.confirmBtn.setPadding(0, 0, 0, 0);
        UniWebView.getConfig().setLoadLocalJS(true);
        this.mContentWebView = new UniWebView(context);
        this.mContentWebView.setNeedShowButton(false);
        this.mIsSubProtocol = true;
        this.mContentWebView.setSupportClearFocus(true);
        this.mContentWebView.setHTMLProtocol(this.isHTMLProtocol);
        this.mContentWebView.initWebView();
        this.mContentWebView.loadUrl(this.mProtocolInfo.url);
        ((FrameLayout) viewInflate.findViewById(ResUtils.getResId(context, "uni_p_content", ResIdReader.RES_TYPE_ID))).addView(this.mContentWebView, new FrameLayout.LayoutParams(-1, -1));
        return viewInflate;
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.UrlContentView$1 */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (UrlContentView.this.mIsSubProtocol) {
                L.d(UrlContentView.TAG, "parent:" + UrlContentView.this.mParent);
                if (UrlContentView.this.mEventCallback != null) {
                    if (TextUtils.isEmpty(UrlContentView.this.mParent)) {
                        if (UrlContentView.this.mContentWebView != null && UrlContentView.this.mContentWebView.canGoBack()) {
                            UrlContentView.this.mContentWebView.goBack();
                            return;
                        } else {
                            UrlContentView.this.mEventCallback.finish(0, null);
                            return;
                        }
                    }
                    UrlContentView.this.mEventCallback.back(UrlContentView.this.mParent);
                    return;
                }
                return;
            }
            if (UrlContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == UrlContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(UrlContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(UrlContentView.this.mProtocolInfo.version);
                }
                UrlContentView.this.mEventCallback.finish(0, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.UrlContentView$2 */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (UrlContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == UrlContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(UrlContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(UrlContentView.this.mProtocolInfo.version);
                }
                UrlContentView.this.mEventCallback.finish(1, null);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.view.UrlContentView$3 */
    class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (UrlContentView.this.mEventCallback != null) {
                if (Situation.LAUNCHER == UrlContentView.this.mSituation) {
                    ProtocolManager.getInstance().setLaunchProtocolId(UrlContentView.this.mProtocolInfo.id);
                    ProtocolManager.getInstance().setLaunchProtocolVersion(UrlContentView.this.mProtocolInfo.version);
                }
                UrlContentView.this.mEventCallback.finish(2, null);
            }
        }
    }
}