package com.netease.androidcrashhandler.other;

/* loaded from: classes5.dex */
public class NTAssociatedFile {
    public String mDesFileName;
    public String mFileFeature;
    public String mSrcContent;
    public String mSrcFilePath;

    public NTAssociatedFile(String str, String str2, String str3) {
        this.mFileFeature = null;
        this.mSrcFilePath = str;
        this.mSrcContent = str2;
        this.mDesFileName = str3;
    }

    public NTAssociatedFile(String str, String str2, String str3, String str4) {
        this.mSrcFilePath = str;
        this.mSrcContent = str2;
        this.mDesFileName = str3;
        this.mFileFeature = str4;
    }

    public String getSrcFilePath() {
        return this.mSrcFilePath;
    }

    public String getSrcContent() {
        return this.mSrcContent;
    }

    public String getDesFileName() {
        return this.mDesFileName;
    }

    public String getFileFeature() {
        return this.mFileFeature;
    }

    public String toString() {
        return "mSrcFilePath=" + this.mSrcFilePath + ", mSrcContent=" + this.mSrcContent + ", mDesFileName=" + this.mDesFileName + ", mFileFeature=" + this.mFileFeature;
    }
}