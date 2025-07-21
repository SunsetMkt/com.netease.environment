package com.netease.rnwebview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/* loaded from: classes4.dex */
public class RNWebViewModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    public static final String REACT_CLASS = "RNWebViewAndroidModule";
    private static final int REQUEST_SELECT_FILE = 1001;
    private static final int REQUEST_SELECT_FILE_LEGACY = 1002;
    private RNWebViewPackage aPackage;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageArr;

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.bridge.ActivityEventListener
    public void onNewIntent(Intent intent) {
    }

    public RNWebViewModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.mUploadMessage = null;
        this.mUploadMessageArr = null;
        reactApplicationContext.addActivityEventListener(this);
    }

    public void setPackage(RNWebViewPackage rNWebViewPackage) {
        this.aPackage = rNWebViewPackage;
    }

    public RNWebViewPackage getPackage() {
        return this.aPackage;
    }

    public Activity getActivity() {
        return getCurrentActivity();
    }

    public void showAlert(String str, String str2, JsResult jsResult) {
        new AlertDialog.Builder(getCurrentActivity()).setMessage(str2).setPositiveButton("Ok", new DialogInterface.OnClickListener() { // from class: com.netease.rnwebview.RNWebViewModule.1
            final /* synthetic */ JsResult val$result;

            AnonymousClass1(JsResult jsResult2) {
                jsResult = jsResult2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                jsResult.confirm();
            }
        }).create().show();
    }

    /* renamed from: com.netease.rnwebview.RNWebViewModule$1 */
    class AnonymousClass1 implements DialogInterface.OnClickListener {
        final /* synthetic */ JsResult val$result;

        AnonymousClass1(JsResult jsResult2) {
            jsResult = jsResult2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            jsResult.confirm();
        }
    }

    public boolean startFileChooserIntent(ValueCallback<Uri> valueCallback, String str) {
        Log.d(REACT_CLASS, "Open old file dialog");
        ValueCallback<Uri> valueCallback2 = this.mUploadMessage;
        if (valueCallback2 != null) {
            valueCallback2.onReceiveValue(null);
            this.mUploadMessage = null;
        }
        this.mUploadMessage = valueCallback;
        if (str == null || str.isEmpty()) {
            str = "*/*";
        }
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType(str);
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            Log.w(REACT_CLASS, "No context available");
            return false;
        }
        try {
            currentActivity.startActivityForResult(intent, 1002, new Bundle());
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e(REACT_CLASS, "No context available");
            e.printStackTrace();
            ValueCallback<Uri> valueCallback3 = this.mUploadMessage;
            if (valueCallback3 != null) {
                valueCallback3.onReceiveValue(null);
                this.mUploadMessage = null;
            }
            return false;
        }
    }

    public boolean startFileChooserIntent(ValueCallback<Uri[]> valueCallback, Intent intent) {
        Log.d(REACT_CLASS, "Open new file dialog");
        ValueCallback<Uri[]> valueCallback2 = this.mUploadMessageArr;
        if (valueCallback2 != null) {
            valueCallback2.onReceiveValue(null);
            this.mUploadMessageArr = null;
        }
        this.mUploadMessageArr = valueCallback;
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            Log.w(REACT_CLASS, "No context available");
            return false;
        }
        try {
            Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
            intent2.addCategory("android.intent.category.OPENABLE");
            intent2.setType("image/*");
            currentActivity.startActivityForResult(Intent.createChooser(intent2, "Browser Image..."), 1001, new Bundle());
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e(REACT_CLASS, "No context available");
            e.printStackTrace();
            ValueCallback<Uri[]> valueCallback3 = this.mUploadMessageArr;
            if (valueCallback3 != null) {
                valueCallback3.onReceiveValue(null);
                this.mUploadMessageArr = null;
            }
            return false;
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        ValueCallback<Uri[]> valueCallback;
        if (i == 1002) {
            if (this.mUploadMessage == null) {
                return;
            }
            this.mUploadMessage.onReceiveValue((intent == null || i2 != -1) ? null : intent.getData());
            this.mUploadMessage = null;
            return;
        }
        if (i != 1001 || (valueCallback = this.mUploadMessageArr) == null) {
            return;
        }
        valueCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
        this.mUploadMessageArr = null;
    }

    @Override // com.facebook.react.bridge.ActivityEventListener
    public void onActivityResult(Activity activity, int i, int i2, Intent intent) {
        onActivityResult(i, i2, intent);
    }
}