package com.netease.ntunisdk.modules.ngwebviewgeneral;

import java.io.Serializable;

/* loaded from: classes.dex */
public class WebviewParams implements Serializable {
    private static final long serialVersionUID = 1;
    private String YYGameID;
    private String additionalUserAgent;
    private String appVersionName;
    private String blackBorderColor;
    private String channelID;
    private int closeBtnHeight;
    private int closeBtnOriginX;
    private int closeBtnOriginY;
    private int closeBtnWidth;
    private boolean closeButtonVisible;
    private int cutoutHeight;
    private int cutoutWidth;
    private boolean hasCutout;
    private int height;
    private String intercept_schemes;
    private boolean isFullScreen;
    private boolean isFullScreenNoAdaptive;
    private boolean isHasPdfView;
    private boolean isModule;
    private boolean isSecureVerify;
    private boolean isSetSurveyXY;
    private boolean isSingleInstance;
    private boolean isSingleProcess;
    private boolean isSurvey;
    private boolean navigationBarVisible;
    private int orientation;
    private int originX;
    private int originY;

    @Deprecated
    private boolean qstnCloseBtnVisible;
    private String scriptVersion;
    private String source;
    private boolean supportBackKey;
    private String url;
    private String webviewBackgroundColor;
    private String webviewCtx;
    private int width;

    public boolean isSingleInstance() {
        return this.isSingleInstance;
    }

    public void setSingleInstance(boolean z) {
        this.isSingleInstance = z;
    }

    public boolean isSingleProcess() {
        return this.isSingleProcess;
    }

    public void setSingleProcess(boolean z) {
        this.isSingleProcess = z;
    }

    public boolean isFullScreenNoAdaptive() {
        return this.isFullScreenNoAdaptive;
    }

    public void setFullScreenNoAdaptive(boolean z) {
        this.isFullScreenNoAdaptive = z;
    }

    public String getWebviewCtx() {
        return this.webviewCtx;
    }

    public void setWebviewCtx(String str) {
        this.webviewCtx = str;
    }

    public int getCutoutWidth() {
        return this.cutoutWidth;
    }

    public void setCutoutWidth(int i) {
        this.cutoutWidth = i;
    }

    public int getCutoutHeight() {
        return this.cutoutHeight;
    }

    public void setCutoutHeight(int i) {
        this.cutoutHeight = i;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public void setAppVersionName(String str) {
        this.appVersionName = str;
    }

    public boolean isModule() {
        return this.isModule;
    }

    public void setIsModule(boolean z) {
        this.isModule = z;
    }

    public String getYYGameID() {
        return this.YYGameID;
    }

    public void setYYGameID(String str) {
        this.YYGameID = str;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public void setChannelID(String str) {
        this.channelID = str;
    }

    public String getWebviewBackgroundColor() {
        return this.webviewBackgroundColor;
    }

    public void setWebviewBackgroundColor(String str) {
        this.webviewBackgroundColor = str;
    }

    public boolean isSetSurveyXY() {
        return this.isSetSurveyXY;
    }

    public void setSetSurveyXY(boolean z) {
        this.isSetSurveyXY = z;
    }

    public int getOriginX() {
        return this.originX;
    }

    public void setOriginX(int i) {
        this.originX = i;
    }

    public int getOriginY() {
        return this.originY;
    }

    public void setOriginY(int i) {
        this.originY = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i) {
        this.orientation = i;
    }

    public String getScriptVersion() {
        return this.scriptVersion;
    }

    public void setScriptVersion(String str) {
        this.scriptVersion = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getAdditionalUserAgent() {
        return this.additionalUserAgent;
    }

    public void setAdditionalUserAgent(String str) {
        this.additionalUserAgent = str;
    }

    public boolean isNavigationBarVisible() {
        return this.navigationBarVisible;
    }

    public void setNavigationBarVisible(boolean z) {
        this.navigationBarVisible = z;
    }

    public String getIntercept_schemes() {
        return this.intercept_schemes;
    }

    public void setIntercept_schemes(String str) {
        this.intercept_schemes = str;
    }

    public boolean isFullScreen() {
        return this.isFullScreen;
    }

    public void setFullScreen(boolean z) {
        this.isFullScreen = z;
    }

    @Deprecated
    public boolean isQstnCloseBtnVisible() {
        return this.qstnCloseBtnVisible;
    }

    @Deprecated
    public void setQstnCloseBtnVisible(boolean z) {
        this.qstnCloseBtnVisible = z;
    }

    public boolean isCloseButtonVisible() {
        return this.closeButtonVisible;
    }

    public void setCloseButtonVisible(boolean z) {
        this.closeButtonVisible = z;
    }

    public int getCloseBtnOriginX() {
        return this.closeBtnOriginX;
    }

    public void setCloseBtnOriginX(int i) {
        this.closeBtnOriginX = i;
    }

    public int getCloseBtnOriginY() {
        return this.closeBtnOriginY;
    }

    public void setCloseBtnOriginY(int i) {
        this.closeBtnOriginY = i;
    }

    public boolean isSupportBackKey() {
        return this.supportBackKey;
    }

    public void setSupportBackKey(boolean z) {
        this.supportBackKey = z;
    }

    public boolean isHasCutout() {
        return this.hasCutout;
    }

    public void setHasCutout(boolean z) {
        this.hasCutout = z;
    }

    public int getCloseBtnWidth() {
        return this.closeBtnWidth;
    }

    public void setCloseBtnWidth(int i) {
        this.closeBtnWidth = i;
    }

    public int getCloseBtnHeight() {
        return this.closeBtnHeight;
    }

    public void setCloseBtnHeight(int i) {
        this.closeBtnHeight = i;
    }

    public boolean isSurvey() {
        return this.isSurvey;
    }

    public void setSurvey(boolean z) {
        this.isSurvey = z;
    }

    public boolean isSecureVerify() {
        return this.isSecureVerify;
    }

    public void setSecureVerify(boolean z) {
        this.isSecureVerify = z;
    }

    public boolean isHasPdfView() {
        return this.isHasPdfView;
    }

    public void setHasPdfView(boolean z) {
        this.isHasPdfView = z;
    }

    public String getBlackBorderColor() {
        return this.blackBorderColor;
    }

    public void setBlackBorderColor(String str) {
        this.blackBorderColor = str;
    }
}