package com.netease.vrlib.objects;

import android.content.Context;

/* loaded from: classes5.dex */
public class MDObject3DHelper {

    public interface LoadComplete {
        void onComplete(MDAbsObject3D mDAbsObject3D);
    }

    public static void loadObj(Context context, MDAbsObject3D mDAbsObject3D) {
        loadObj(context, mDAbsObject3D, null);
    }

    public static void loadObj(final Context context, final MDAbsObject3D mDAbsObject3D, final LoadComplete loadComplete) {
        new Thread(new Runnable() { // from class: com.netease.vrlib.objects.MDObject3DHelper.1
            @Override // java.lang.Runnable
            public void run() {
                mDAbsObject3D.executeLoad(context);
                LoadComplete loadComplete2 = loadComplete;
                if (loadComplete2 != null) {
                    loadComplete2.onComplete(mDAbsObject3D);
                }
            }
        }).start();
    }
}