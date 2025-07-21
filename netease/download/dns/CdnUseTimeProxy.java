package com.netease.download.dns;

import java.util.HashMap;

/* loaded from: classes5.dex */
public class CdnUseTimeProxy {
    private static CdnUseTimeProxy sCndUseTimeProxy;
    private HashMap<String, CndUseTimeUnit> mMap = new HashMap<>();

    public void init() {
    }

    private CdnUseTimeProxy() {
    }

    public static CdnUseTimeProxy getInstance() {
        if (sCndUseTimeProxy == null) {
            sCndUseTimeProxy = new CdnUseTimeProxy();
        }
        return sCndUseTimeProxy;
    }

    public void start(String str) {
        CndUseTimeUnit cndUseTimeUnit;
        if (this.mMap.containsKey(str)) {
            cndUseTimeUnit = this.mMap.get(str);
            if (cndUseTimeUnit.mCount == 0) {
                cndUseTimeUnit.mStartTime = System.currentTimeMillis();
            }
        } else {
            CndUseTimeUnit cndUseTimeUnit2 = new CndUseTimeUnit(0L, 0, 0L);
            this.mMap.put(str, cndUseTimeUnit2);
            cndUseTimeUnit2.mStartTime = System.currentTimeMillis();
            cndUseTimeUnit = cndUseTimeUnit2;
        }
        cndUseTimeUnit.mCount++;
    }

    public void finish(String str) {
        if (this.mMap.containsKey(str)) {
            CndUseTimeUnit cndUseTimeUnit = this.mMap.get(str);
            if (cndUseTimeUnit.mCount > 0) {
                cndUseTimeUnit.mCount--;
            }
            if (cndUseTimeUnit.mCount == 0) {
                cndUseTimeUnit.mUseTime = (System.currentTimeMillis() - cndUseTimeUnit.mStartTime) + cndUseTimeUnit.mUseTime;
            }
        }
    }

    public static class CndUseTimeUnit {
        public int mCount;
        public long mStartTime;
        public long mUseTime;

        public CndUseTimeUnit(long j, int i, long j2) {
            this.mStartTime = j;
            this.mCount = i;
            this.mUseTime = j2;
        }

        public String toString() {
            return "mStartTime=" + this.mStartTime + ", mCount=" + this.mCount + ", mUseTime=" + this.mUseTime;
        }
    }
}