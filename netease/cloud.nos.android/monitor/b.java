package com.netease.cloud.nos.android.monitor;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes5.dex */
final class b implements Parcelable.Creator<MonitorConfig> {
    b() {
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public MonitorConfig createFromParcel(Parcel parcel) {
        return new MonitorConfig(parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readLong());
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public MonitorConfig[] newArray(int i) {
        return new MonitorConfig[i];
    }
}