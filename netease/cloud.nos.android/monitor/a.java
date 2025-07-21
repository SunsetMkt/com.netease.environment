package com.netease.cloud.nos.android.monitor;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes5.dex */
class a implements ISendStat {

    /* renamed from: a, reason: collision with root package name */
    private IBinder f1545a;

    a(IBinder iBinder) {
        this.f1545a = iBinder;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.f1545a;
    }

    @Override // com.netease.cloud.nos.android.monitor.ISendStat
    public void sendConfig(MonitorConfig monitorConfig) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        Parcel parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("com.netease.cloud.nos.android.monitor.ISendStat");
            if (monitorConfig != null) {
                parcelObtain.writeInt(1);
                monitorConfig.writeToParcel(parcelObtain, 0);
            } else {
                parcelObtain.writeInt(0);
            }
            this.f1545a.transact(2, parcelObtain, parcelObtain2, 0);
            parcelObtain2.readException();
        } finally {
            parcelObtain2.recycle();
            parcelObtain.recycle();
        }
    }

    @Override // com.netease.cloud.nos.android.monitor.ISendStat
    public boolean sendStat(StatisticItem statisticItem) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        Parcel parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("com.netease.cloud.nos.android.monitor.ISendStat");
            if (statisticItem != null) {
                parcelObtain.writeInt(1);
                statisticItem.writeToParcel(parcelObtain, 0);
            } else {
                parcelObtain.writeInt(0);
            }
            this.f1545a.transact(1, parcelObtain, parcelObtain2, 0);
            parcelObtain2.readException();
            return parcelObtain2.readInt() != 0;
        } finally {
            parcelObtain2.recycle();
            parcelObtain.recycle();
        }
    }
}