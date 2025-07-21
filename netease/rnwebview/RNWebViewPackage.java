package com.netease.rnwebview;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public class RNWebViewPackage implements ReactPackage {
    private RNWebViewModule module;
    private RNWebViewManager viewManager;
    public String whitePath;

    public RNWebViewPackage(String str) {
        this.whitePath = str;
    }

    @Override // com.facebook.react.ReactPackage
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        RNWebViewModule rNWebViewModule = new RNWebViewModule(reactApplicationContext);
        this.module = rNWebViewModule;
        rNWebViewModule.setPackage(this);
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.module);
        return arrayList;
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override // com.facebook.react.ReactPackage
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        RNWebViewManager rNWebViewManager = new RNWebViewManager(this.whitePath);
        this.viewManager = rNWebViewManager;
        rNWebViewManager.setPackage(this);
        return Arrays.asList(this.viewManager);
    }

    public RNWebViewModule getModule() {
        return this.module;
    }

    public RNWebViewManager getViewManager() {
        return this.viewManager;
    }
}