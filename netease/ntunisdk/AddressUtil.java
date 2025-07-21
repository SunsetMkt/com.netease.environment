package com.netease.ntunisdk;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
class AddressUtil {
    private static final String TAG = "AddressUtil";
    private static volatile ExecutorService executor;

    AddressUtil() {
    }

    static String getAddressWithTimeLimit(final Context context, final double d, final double d2, long j) {
        if (executor == null) {
            synchronized (AddressUtil.class) {
                if (executor == null) {
                    executor = Executors.newSingleThreadExecutor();
                }
            }
        }
        try {
            return (String) executor.submit(new Callable<String>() { // from class: com.netease.ntunisdk.AddressUtil.1
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return AddressUtil.getAddress(context, d, d2);
                }
            }).get(j, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getAddress(Context context, double d, double d2) throws IOException {
        try {
            List<Address> fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(d, d2, 1);
            if (fromLocation == null || fromLocation.isEmpty()) {
                return "";
            }
            Address address = fromLocation.get(0);
            if (address == null) {
                return "";
            }
            ArrayList arrayList = new ArrayList(address.getMaxAddressLineIndex());
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                arrayList.add(address.getAddressLine(i));
            }
            return TextUtils.join("##", arrayList);
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "" + e.getMessage());
            return "";
        }
    }
}