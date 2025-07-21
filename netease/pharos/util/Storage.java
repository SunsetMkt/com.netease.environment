package com.netease.pharos.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.netease.pharos.PharosProxy;

/* loaded from: classes5.dex */
public class Storage {
    private static final String HASH = "_hash";
    private static final String Pharos_Storage = "pharos_storage";
    private static final String TAG = "_tag";
    private static Storage sInstance;
    private boolean hasInit = false;
    private SharedPreferences mPharosSharedPreferences;

    private Storage() {
        init();
    }

    public static Storage getInstance() {
        if (sInstance == null) {
            synchronized (Storage.class) {
                if (sInstance == null) {
                    sInstance = new Storage();
                }
            }
        }
        return sInstance;
    }

    public synchronized void init() {
        if (this.hasInit) {
            return;
        }
        Context context = PharosProxy.getInstance().getContext();
        if (context != null) {
            this.mPharosSharedPreferences = context.getSharedPreferences(Pharos_Storage, 0);
            this.hasInit = true;
        } else {
            this.mPharosSharedPreferences = null;
            this.hasInit = false;
        }
    }

    public String getTag(String str) {
        init();
        if (!this.hasInit) {
            return "";
        }
        SharedPreferences sharedPreferences = this.mPharosSharedPreferences;
        if (sharedPreferences == null) {
            this.hasInit = false;
            return "";
        }
        return sharedPreferences.getString(str + TAG, "");
    }

    public boolean saveTag(String str, String str2) {
        init();
        if (!this.hasInit) {
            return false;
        }
        SharedPreferences sharedPreferences = this.mPharosSharedPreferences;
        if (sharedPreferences == null) {
            this.hasInit = false;
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString(str + TAG, str2);
        editorEdit.apply();
        return true;
    }

    public boolean clearTag(String str) {
        init();
        if (!this.hasInit) {
            return false;
        }
        SharedPreferences sharedPreferences = this.mPharosSharedPreferences;
        if (sharedPreferences == null) {
            this.hasInit = false;
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.remove(str + TAG);
        editorEdit.apply();
        return true;
    }

    public String getHash(String str) {
        init();
        if (!this.hasInit) {
            return "";
        }
        SharedPreferences sharedPreferences = this.mPharosSharedPreferences;
        if (sharedPreferences == null) {
            this.hasInit = false;
            return "";
        }
        return sharedPreferences.getString(str + HASH, "");
    }

    public boolean saveHash(String str, String str2) {
        init();
        if (!this.hasInit) {
            return false;
        }
        if (this.mPharosSharedPreferences == null) {
            this.hasInit = false;
        }
        LogUtil.i(TAG, "saveHash:" + str + ",value:" + str2);
        SharedPreferences.Editor editorEdit = this.mPharosSharedPreferences.edit();
        editorEdit.putString(str + HASH, str2);
        editorEdit.apply();
        return true;
    }

    public boolean clearHash(String str) {
        init();
        if (!this.hasInit) {
            return false;
        }
        SharedPreferences sharedPreferences = this.mPharosSharedPreferences;
        if (sharedPreferences == null) {
            this.hasInit = false;
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.remove(str + HASH);
        editorEdit.apply();
        return true;
    }
}