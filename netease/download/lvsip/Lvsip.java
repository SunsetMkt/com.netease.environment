package com.netease.download.lvsip;

import com.netease.download.Const;
import java.util.ArrayList;

/* loaded from: classes6.dex */
public class Lvsip {
    private static Lvsip lvsip;
    private static ArrayList<String> sLvsip = new ArrayList<>();
    private String[] mLvsips = null;
    private int index = 0;

    public static Lvsip getInstance() {
        if (lvsip == null) {
            lvsip = new Lvsip();
        }
        return lvsip;
    }

    public void init(String[] strArr) {
        if (this.mLvsips == null) {
            this.mLvsips = strArr;
        }
    }

    public boolean isCteateIp() {
        return sLvsip.size() != 0;
    }

    public void createLvsip() {
        String[] strArr = this.mLvsips;
        if (strArr == null) {
            strArr = Const.REQ_IPS_WS;
        }
        for (String str : strArr) {
            sLvsip.add(str);
        }
    }

    public boolean hasNext() {
        return this.index < sLvsip.size();
    }

    public String getNewIpFromArray() {
        if (this.index >= sLvsip.size()) {
            return null;
        }
        String str = sLvsip.get(this.index);
        this.index++;
        return str;
    }

    public void clean() {
        this.index = 0;
        String[] strArr = this.mLvsips;
        if (strArr != null && strArr.length > 0) {
            this.mLvsips = null;
        }
        ArrayList<String> arrayList = sLvsip;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        sLvsip.clear();
    }
}