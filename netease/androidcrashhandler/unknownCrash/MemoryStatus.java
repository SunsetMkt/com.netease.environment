package com.netease.androidcrashhandler.unknownCrash;

/* loaded from: classes5.dex */
public class MemoryStatus {
    public long apk_mmap;
    public long art_mmap;
    public long ashmem;
    public long dalvikHeapPss;
    public long dalvikOther;
    public long dex_mmap;
    public int memoryAdviceState;
    public long nativeHeapPss;
    public int[] native_status = new int[16];
    public long oat_mmap;
    public long otherDev;
    public long other_mmap;
    public long so_mmap;
    public long stack;
    public long systemFreePss;
    public boolean systemLowMemory;
    public long systemThreshold;
    public long systemTotalPss;
    public long totalPss;
    public long totalSwapPss;
    public long ttf_mmap;
    public long unknown;

    public void analyze() {
        int[] iArr = this.native_status;
        this.totalPss = iArr[0];
        this.totalSwapPss = iArr[1];
        this.nativeHeapPss = iArr[2];
        this.dalvikHeapPss = iArr[3];
        this.dalvikOther = iArr[4];
        this.stack = iArr[5];
        this.ashmem = iArr[6];
        this.otherDev = iArr[7];
        this.so_mmap = iArr[8];
        this.apk_mmap = iArr[9];
        this.ttf_mmap = iArr[10];
        this.dex_mmap = iArr[11];
        this.oat_mmap = iArr[12];
        this.art_mmap = iArr[13];
        this.other_mmap = iArr[14];
        this.unknown = iArr[15];
    }

    public String toString() {
        return "MemoryStatus{totalPss=" + this.totalPss + ", totalSwapPss=" + this.totalSwapPss + ", systemTotalPss=" + this.systemTotalPss + ", systemFreePss=" + this.systemFreePss + ", nativeHeapPss=" + this.nativeHeapPss + ", dalvikHeapPss=" + this.dalvikHeapPss + ", dalvikOther=" + this.dalvikOther + ", stack=" + this.stack + ", ashmem=" + this.ashmem + ", otherDev=" + this.otherDev + ", so_mmap=" + this.so_mmap + ", apk_mmap=" + this.apk_mmap + ", ttf_mmap=" + this.ttf_mmap + ", dex_mmap=" + this.dex_mmap + ", oat_mmap=" + this.oat_mmap + ", art_mmap=" + this.art_mmap + ", other_mmap=" + this.other_mmap + ", unknown=" + this.unknown + '}';
    }
}