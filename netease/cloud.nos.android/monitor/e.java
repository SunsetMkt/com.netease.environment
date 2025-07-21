package com.netease.cloud.nos.android.monitor;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes5.dex */
final class e implements Parcelable.Creator<StatisticItem> {
    e() {
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public StatisticItem createFromParcel(Parcel parcel) {
        return new StatisticItem(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readString(), parcel.readLong(), parcel.readLong(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readInt());
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public StatisticItem[] newArray(int i) {
        return new StatisticItem[i];
    }
}