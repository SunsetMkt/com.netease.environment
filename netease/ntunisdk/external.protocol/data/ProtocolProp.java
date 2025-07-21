package com.netease.ntunisdk.external.protocol.data;

import com.netease.ntunisdk.external.protocol.data.Config;
import com.netease.ntunisdk.external.protocol.utils.L;

/* loaded from: classes.dex */
public class ProtocolProp {
    private String agreeLineText;
    private String gameId;
    private String gameName;
    private String issuer;
    private String language;
    private String locale;
    private Config.ProtocolConfig mProtocolConfig;
    private String newOfflineFlag;
    private String offlineGameFlag;
    private String scene;
    private String showAgreeLineFlag;
    private String showTitleFlag;
    private String url;
    private String urlType;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        L.d("Prop", "setUrl : " + str);
        this.url = str;
    }

    public String getUrlType() {
        return this.urlType;
    }

    public void setUrlType(String str) {
        this.urlType = str;
    }

    public String getOfflineGameFlag() {
        return this.offlineGameFlag;
    }

    public void setOfflineGameFlag(String str) {
        this.offlineGameFlag = str;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String str) {
        this.gameId = str;
    }

    public String getNewOfflineFlag() {
        return this.newOfflineFlag;
    }

    public void setNewOfflineFlag(String str) {
        this.newOfflineFlag = str;
    }

    public String getShowTitleFlag() {
        return this.showTitleFlag;
    }

    public void setShowTitleFlag(String str) {
        this.showTitleFlag = str;
    }

    public String getShowAgreeLineFlag() {
        return this.showAgreeLineFlag;
    }

    public void setShowAgreeLineFlag(String str) {
        this.showAgreeLineFlag = str;
    }

    public String getAgreeLineText() {
        return this.agreeLineText;
    }

    public void setAgreeLineText(String str) {
        this.agreeLineText = str;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String str) {
        this.locale = str;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setIssuer(String str) {
        this.issuer = str;
    }

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String str) {
        this.gameName = str;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public Config.ProtocolConfig getProtocolConfig() {
        return this.mProtocolConfig;
    }

    public void setProtocolConfig(Config.ProtocolConfig protocolConfig) {
        this.mProtocolConfig = protocolConfig;
    }

    public String getScene() {
        return this.scene;
    }

    public void setScene(String str) {
        this.scene = str;
    }
}