package com.netease.download.dns;

import java.util.ArrayList;

/* loaded from: classes5.dex */
public class DnsParams {
    private ArrayList<Unit> mDnsIpNodeUnitList = new ArrayList<>();

    public void add(String str, ArrayList<String> arrayList, String str2) {
        this.mDnsIpNodeUnitList.add(new Unit(str, arrayList, str2));
    }

    public ArrayList<Unit> getDnsIpNodeUnitList() {
        return this.mDnsIpNodeUnitList;
    }

    public static class Unit {
        public String domain;
        public ArrayList<String> ipArrayList;
        public String port;

        public Unit(String str, ArrayList<String> arrayList, String str2) {
            this.domain = str;
            this.ipArrayList = arrayList;
            this.port = str2;
        }

        public String toString() {
            return "domain=" + this.domain + ", ipArrayList=" + this.ipArrayList.toString() + ", port=" + this.port;
        }
    }
}