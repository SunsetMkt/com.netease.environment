package com.netease.cloud.nos.android.monitor;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes5.dex */
public interface ISendStat extends IInterface {

    public abstract class Stub extends Binder implements ISendStat {
        private static final String DESCRIPTOR = "com.netease.cloud.nos.android.monitor.ISendStat";
        static final int TRANSACTION_sendConfig = 2;
        static final int TRANSACTION_sendStat = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISendStat asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ISendStat)) ? new a(iBinder) : (ISendStat) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                boolean zSendStat = sendStat(parcel.readInt() != 0 ? StatisticItem.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                parcel2.writeInt(zSendStat ? 1 : 0);
                return true;
            }
            if (i != 2) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            parcel.enforceInterface(DESCRIPTOR);
            sendConfig(parcel.readInt() != 0 ? MonitorConfig.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            return true;
        }
    }

    void sendConfig(MonitorConfig monitorConfig) throws RemoteException;

    boolean sendStat(StatisticItem statisticItem) throws RemoteException;
}