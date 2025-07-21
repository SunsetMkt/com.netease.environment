package com.netease.ntunisdk.external.protocol.utils;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class Response {
    private ArrayList<String> mContent;
    private Map<String, List<String>> mHeader;
    private int mStatus = 503;

    public ArrayList<String> getContent() {
        return this.mContent;
    }

    public void setContent(ArrayList<String> arrayList) {
        this.mContent = arrayList;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int i) {
        this.mStatus = i;
    }

    public String getContentStr() {
        ArrayList<String> arrayList = this.mContent;
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.mContent.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }

    public Map<String, List<String>> getHeader() {
        return this.mHeader;
    }

    public Response setHeader(Map<String, List<String>> map) {
        this.mHeader = map;
        return this;
    }

    public String getHeaderProperty(String str) {
        Map<String, List<String>> map = this.mHeader;
        if (map == null || map.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, List<String>> entry : this.mHeader.entrySet()) {
            if (entry != null) {
                String key = entry.getKey();
                if (!TextUtils.isEmpty(key) && key.equalsIgnoreCase(str)) {
                    List<String> value = entry.getValue();
                    StringBuilder sb = new StringBuilder();
                    if (value != null) {
                        for (String str2 : value) {
                            if (sb.length() > 0) {
                                sb.append(" ");
                            }
                            sb.append(str2);
                        }
                    }
                    return sb.toString().trim();
                }
            }
        }
        return null;
    }
}