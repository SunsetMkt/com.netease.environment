package com.netease.ntunisdk.external.protocol.data;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;

/* loaded from: classes.dex */
public class User {
    public static final String USER_NAME_LAUNCHER = "launcher";
    public static final String USER_NAME_LOGOUT = "logout";
    private boolean isFirstUser = false;
    private final ConcurrentHashMap<String, JSONArray> mAcceptProtocolAlias;
    private TreeSet<String> mAcceptProtocols;
    private final String mUid;

    public User(String str) {
        this.mUid = TextUtils.isEmpty(str) ? USER_NAME_LOGOUT : str;
        this.mAcceptProtocolAlias = new ConcurrentHashMap<>();
    }

    public static boolean isFirstUser(User user, String str) {
        return user != null && USER_NAME_LAUNCHER.equals(user.mUid) && (TextUtils.isEmpty(str) || !USER_NAME_LAUNCHER.equals(str));
    }

    public static boolean isLogoutUser(String str) {
        return USER_NAME_LOGOUT.equals(str) || TextUtils.isEmpty(str);
    }

    public static boolean isRealUser(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return !str.equals(USER_NAME_LAUNCHER);
    }

    public boolean isFirstUser() {
        return this.isFirstUser;
    }

    public User setFirstUser(boolean z) {
        this.isFirstUser = z;
        return this;
    }

    public void clear() {
        this.mAcceptProtocolAlias.clear();
    }

    public synchronized void addAcceptProtocolAlias(String str, JSONArray jSONArray) {
        this.mAcceptProtocolAlias.put(str, jSONArray);
    }

    public synchronized JSONArray getAcceptProtocolAlias(String str) {
        return this.mAcceptProtocolAlias.get(str);
    }

    public String getUid() {
        return this.mUid;
    }

    public void setAcceptProtocols(TreeSet<String> treeSet) {
        this.mAcceptProtocols = treeSet;
    }

    public boolean isLogout() {
        return TextUtils.equals(USER_NAME_LOGOUT, this.mUid);
    }

    public boolean isLauncher() {
        return TextUtils.equals(USER_NAME_LAUNCHER, this.mUid);
    }

    public boolean hasAcceptProtocol(String str) {
        TreeSet<String> treeSet = this.mAcceptProtocols;
        if (treeSet != null && !treeSet.isEmpty()) {
            Iterator<String> it = this.mAcceptProtocols.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next.startsWith(str + "-") && !next.endsWith("-0")) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getProtocolAcceptStatus(String str) {
        TreeSet<String> treeSet = this.mAcceptProtocols;
        if (treeSet == null || treeSet.isEmpty()) {
            return 0;
        }
        Iterator<String> it = this.mAcceptProtocols.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next.startsWith(str + "-")) {
                return !next.endsWith("-0") ? 1 : 0;
            }
        }
        return 4;
    }
}