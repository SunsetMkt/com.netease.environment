package com.netease.ntunisdk.okhttp3.internal.connection;

import com.netease.ntunisdk.okhttp3.Route;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: classes5.dex */
public final class RouteDatabase {
    private final Set<Route> failedRoutes = new LinkedHashSet();

    public synchronized void connected(Route route) {
        this.failedRoutes.remove(route);
    }

    public synchronized void failed(Route route) {
        this.failedRoutes.add(route);
    }

    public synchronized boolean shouldPostpone(Route route) {
        return this.failedRoutes.contains(route);
    }
}