package com.netease.ntunisdk.okhttp3.internal.connection;

import com.netease.ntunisdk.okhttp3.Address;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.ConnectionPool;
import com.netease.ntunisdk.okhttp3.EventListener;
import com.netease.ntunisdk.okhttp3.Interceptor;
import com.netease.ntunisdk.okhttp3.OkHttpClient;
import com.netease.ntunisdk.okhttp3.Route;
import com.netease.ntunisdk.okhttp3.internal.Internal;
import com.netease.ntunisdk.okhttp3.internal.Util;
import com.netease.ntunisdk.okhttp3.internal.connection.RouteSelector;
import com.netease.ntunisdk.okhttp3.internal.http.HttpCodec;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.List;

/* loaded from: classes5.dex */
public final class StreamAllocation {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public final Address address;
    public final Call call;
    private final Object callStackTrace;
    private boolean canceled;
    private HttpCodec codec;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    public final EventListener eventListener;
    private int refusedStreamCount;
    private boolean released;
    private boolean reportedAcquired;
    private Route route;
    private RouteSelector.Selection routeSelection;
    private final RouteSelector routeSelector;

    public static final class StreamAllocationReference extends WeakReference<StreamAllocation> {
        public final Object callStackTrace;

        StreamAllocationReference(StreamAllocation streamAllocation, Object obj) {
            super(streamAllocation);
            this.callStackTrace = obj;
        }
    }

    public StreamAllocation(ConnectionPool connectionPool, Address address, Call call, EventListener eventListener, Object obj) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.call = call;
        this.eventListener = eventListener;
        this.routeSelector = new RouteSelector(address, routeDatabase(), call, eventListener);
        this.callStackTrace = obj;
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.net.Socket deallocate(boolean r2, boolean r3, boolean r4) {
        /*
            r1 = this;
            r0 = 0
            if (r4 == 0) goto L5
            r1.codec = r0
        L5:
            r4 = 1
            if (r3 == 0) goto La
            r1.released = r4
        La:
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r3 = r1.connection
            if (r3 == 0) goto L4e
            if (r2 == 0) goto L12
            r3.noNewStreams = r4
        L12:
            com.netease.ntunisdk.okhttp3.internal.http.HttpCodec r2 = r1.codec
            if (r2 != 0) goto L4e
            boolean r2 = r1.released
            if (r2 != 0) goto L20
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r2 = r1.connection
            boolean r2 = r2.noNewStreams
            if (r2 == 0) goto L4e
        L20:
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r2 = r1.connection
            r1.release(r2)
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r2 = r1.connection
            java.util.List<java.lang.ref.Reference<com.netease.ntunisdk.okhttp3.internal.connection.StreamAllocation>> r2 = r2.allocations
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L4a
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r2 = r1.connection
            long r3 = java.lang.System.nanoTime()
            r2.idleAtNanos = r3
            com.netease.ntunisdk.okhttp3.internal.Internal r2 = com.netease.ntunisdk.okhttp3.internal.Internal.instance
            com.netease.ntunisdk.okhttp3.ConnectionPool r3 = r1.connectionPool
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r4 = r1.connection
            boolean r2 = r2.connectionBecameIdle(r3, r4)
            if (r2 == 0) goto L4a
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r2 = r1.connection
            java.net.Socket r2 = r2.socket()
            goto L4b
        L4a:
            r2 = r0
        L4b:
            r1.connection = r0
            r0 = r2
        L4e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.okhttp3.internal.connection.StreamAllocation.deallocate(boolean, boolean, boolean):java.net.Socket");
    }

    private RealConnection findConnection(int i, int i2, int i3, int i4, boolean z) throws Throwable {
        RealConnection realConnection;
        Socket socketReleaseIfNoNewStreams;
        RealConnection realConnection2;
        Socket socketDeduplicate;
        Route next;
        boolean z2;
        boolean z3;
        RouteSelector.Selection selection;
        synchronized (this.connectionPool) {
            if (this.released) {
                throw new IllegalStateException("released");
            }
            if (this.codec != null) {
                throw new IllegalStateException("codec != null");
            }
            if (this.canceled) {
                throw new IOException("Canceled");
            }
            realConnection = this.connection;
            socketReleaseIfNoNewStreams = releaseIfNoNewStreams();
            realConnection2 = this.connection;
            socketDeduplicate = null;
            if (realConnection2 != null) {
                realConnection = null;
            } else {
                realConnection2 = null;
            }
            if (!this.reportedAcquired) {
                realConnection = null;
            }
            if (realConnection2 == null) {
                Internal.instance.get(this.connectionPool, this.address, this, null);
                RealConnection realConnection3 = this.connection;
                if (realConnection3 != null) {
                    z2 = true;
                    realConnection2 = realConnection3;
                    next = null;
                } else {
                    next = this.route;
                }
            } else {
                next = null;
            }
            z2 = false;
        }
        Util.closeQuietly(socketReleaseIfNoNewStreams);
        if (realConnection != null) {
            this.eventListener.connectionReleased(this.call, realConnection);
        }
        if (z2) {
            this.eventListener.connectionAcquired(this.call, realConnection2);
        }
        if (realConnection2 != null) {
            this.route = this.connection.route();
            return realConnection2;
        }
        if (next != null || ((selection = this.routeSelection) != null && selection.hasNext())) {
            z3 = false;
        } else {
            this.routeSelection = this.routeSelector.next();
            z3 = true;
        }
        synchronized (this.connectionPool) {
            if (this.canceled) {
                throw new IOException("Canceled");
            }
            if (z3) {
                List<Route> all = this.routeSelection.getAll();
                int size = all.size();
                int i5 = 0;
                while (true) {
                    if (i5 >= size) {
                        break;
                    }
                    Route route = all.get(i5);
                    Internal.instance.get(this.connectionPool, this.address, this, route);
                    RealConnection realConnection4 = this.connection;
                    if (realConnection4 != null) {
                        this.route = route;
                        z2 = true;
                        realConnection2 = realConnection4;
                        break;
                    }
                    i5++;
                }
            }
            if (!z2) {
                if (next == null) {
                    next = this.routeSelection.next();
                }
                this.route = next;
                this.refusedStreamCount = 0;
                realConnection2 = new RealConnection(this.connectionPool, next);
                acquire(realConnection2, false);
            }
        }
        if (z2) {
            this.eventListener.connectionAcquired(this.call, realConnection2);
            return realConnection2;
        }
        realConnection2.connect(i, i2, i3, i4, z, this.call, this.eventListener);
        routeDatabase().connected(realConnection2.route());
        synchronized (this.connectionPool) {
            this.reportedAcquired = true;
            Internal.instance.put(this.connectionPool, realConnection2);
            if (realConnection2.isMultiplexed()) {
                socketDeduplicate = Internal.instance.deduplicate(this.connectionPool, this.address, this);
                realConnection2 = this.connection;
            }
        }
        Util.closeQuietly(socketDeduplicate);
        this.eventListener.connectionAcquired(this.call, realConnection2);
        return realConnection2;
    }

    private RealConnection findHealthyConnection(int i, int i2, int i3, int i4, boolean z, boolean z2) throws Throwable {
        while (true) {
            RealConnection realConnectionFindConnection = findConnection(i, i2, i3, i4, z);
            synchronized (this.connectionPool) {
                if (realConnectionFindConnection.successCount == 0 && !realConnectionFindConnection.isMultiplexed()) {
                    return realConnectionFindConnection;
                }
                if (realConnectionFindConnection.isHealthy(z2)) {
                    return realConnectionFindConnection;
                }
                noNewStreams();
            }
        }
    }

    private void release(RealConnection realConnection) {
        int size = realConnection.allocations.size();
        for (int i = 0; i < size; i++) {
            if (realConnection.allocations.get(i).get() == this) {
                realConnection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    private Socket releaseIfNoNewStreams() {
        RealConnection realConnection = this.connection;
        if (realConnection == null || !realConnection.noNewStreams) {
            return null;
        }
        return deallocate(false, false, true);
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public void acquire(RealConnection realConnection, boolean z) {
        if (this.connection != null) {
            throw new IllegalStateException();
        }
        this.connection = realConnection;
        this.reportedAcquired = z;
        realConnection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
    }

    public void cancel() throws IOException {
        HttpCodec httpCodec;
        RealConnection realConnection;
        synchronized (this.connectionPool) {
            this.canceled = true;
            httpCodec = this.codec;
            realConnection = this.connection;
        }
        if (httpCodec != null) {
            httpCodec.cancel();
        } else if (realConnection != null) {
            realConnection.cancel();
        }
    }

    public HttpCodec codec() {
        HttpCodec httpCodec;
        synchronized (this.connectionPool) {
            httpCodec = this.codec;
        }
        return httpCodec;
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public boolean hasMoreRoutes() {
        RouteSelector.Selection selection;
        return this.route != null || ((selection = this.routeSelection) != null && selection.hasNext()) || this.routeSelector.hasNext();
    }

    public HttpCodec newStream(OkHttpClient okHttpClient, Interceptor.Chain chain, boolean z) {
        try {
            HttpCodec httpCodecNewCodec = findHealthyConnection(chain.connectTimeoutMillis(), chain.readTimeoutMillis(), chain.writeTimeoutMillis(), okHttpClient.pingIntervalMillis(), okHttpClient.retryOnConnectionFailure(), z).newCodec(okHttpClient, chain, this);
            synchronized (this.connectionPool) {
                this.codec = httpCodecNewCodec;
            }
            return httpCodecNewCodec;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    public void noNewStreams() throws IOException {
        RealConnection realConnection;
        Socket socketDeallocate;
        synchronized (this.connectionPool) {
            realConnection = this.connection;
            socketDeallocate = deallocate(true, false, false);
            if (this.connection != null) {
                realConnection = null;
            }
        }
        Util.closeQuietly(socketDeallocate);
        if (realConnection != null) {
            this.eventListener.connectionReleased(this.call, realConnection);
        }
    }

    public void release() throws IOException {
        RealConnection realConnection;
        Socket socketDeallocate;
        synchronized (this.connectionPool) {
            realConnection = this.connection;
            socketDeallocate = deallocate(false, true, false);
            if (this.connection != null) {
                realConnection = null;
            }
        }
        Util.closeQuietly(socketDeallocate);
        if (realConnection != null) {
            Internal.instance.timeoutExit(this.call, null);
            this.eventListener.connectionReleased(this.call, realConnection);
            this.eventListener.callEnd(this.call);
        }
    }

    public Socket releaseAndAcquire(RealConnection realConnection) {
        if (this.codec != null || this.connection.allocations.size() != 1) {
            throw new IllegalStateException();
        }
        Reference<StreamAllocation> reference = this.connection.allocations.get(0);
        Socket socketDeallocate = deallocate(true, false, false);
        this.connection = realConnection;
        realConnection.allocations.add(reference);
        return socketDeallocate;
    }

    public Route route() {
        return this.route;
    }

    /* JADX WARN: Code restructure failed: missing block: B:57:0x001c, code lost:
    
        if (r7 != com.netease.ntunisdk.okhttp3.internal.http2.ErrorCode.CANCEL) goto L58;
     */
    /* JADX WARN: Removed duplicated region for block: B:75:0x004e A[Catch: all -> 0x0062, TryCatch #0 {, blocks: (B:49:0x0003, B:51:0x000a, B:53:0x0012, B:58:0x001e, B:73:0x0044, B:75:0x004e, B:79:0x0054, B:56:0x001a, B:59:0x0021, B:61:0x0025, B:63:0x002b, B:65:0x002f, B:67:0x0035, B:70:0x003b), top: B:87:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void streamFailed(java.io.IOException r7) throws java.io.IOException {
        /*
            r6 = this;
            com.netease.ntunisdk.okhttp3.ConnectionPool r0 = r6.connectionPool
            monitor-enter(r0)
            boolean r1 = r7 instanceof com.netease.ntunisdk.okhttp3.internal.http2.StreamResetException     // Catch: java.lang.Throwable -> L62
            r2 = 0
            r3 = 1
            r4 = 0
            if (r1 == 0) goto L21
            com.netease.ntunisdk.okhttp3.internal.http2.StreamResetException r7 = (com.netease.ntunisdk.okhttp3.internal.http2.StreamResetException) r7     // Catch: java.lang.Throwable -> L62
            com.netease.ntunisdk.okhttp3.internal.http2.ErrorCode r7 = r7.errorCode     // Catch: java.lang.Throwable -> L62
            com.netease.ntunisdk.okhttp3.internal.http2.ErrorCode r1 = com.netease.ntunisdk.okhttp3.internal.http2.ErrorCode.REFUSED_STREAM     // Catch: java.lang.Throwable -> L62
            if (r7 != r1) goto L1a
            int r7 = r6.refusedStreamCount     // Catch: java.lang.Throwable -> L62
            int r7 = r7 + r3
            r6.refusedStreamCount = r7     // Catch: java.lang.Throwable -> L62
            if (r7 <= r3) goto L43
            goto L1e
        L1a:
            com.netease.ntunisdk.okhttp3.internal.http2.ErrorCode r1 = com.netease.ntunisdk.okhttp3.internal.http2.ErrorCode.CANCEL     // Catch: java.lang.Throwable -> L62
            if (r7 == r1) goto L43
        L1e:
            r6.route = r2     // Catch: java.lang.Throwable -> L62
            goto L41
        L21:
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r1 = r6.connection     // Catch: java.lang.Throwable -> L62
            if (r1 == 0) goto L43
            boolean r1 = r1.isMultiplexed()     // Catch: java.lang.Throwable -> L62
            if (r1 == 0) goto L2f
            boolean r1 = r7 instanceof com.netease.ntunisdk.okhttp3.internal.http2.ConnectionShutdownException     // Catch: java.lang.Throwable -> L62
            if (r1 == 0) goto L43
        L2f:
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r1 = r6.connection     // Catch: java.lang.Throwable -> L62
            int r1 = r1.successCount     // Catch: java.lang.Throwable -> L62
            if (r1 != 0) goto L41
            com.netease.ntunisdk.okhttp3.Route r1 = r6.route     // Catch: java.lang.Throwable -> L62
            if (r1 == 0) goto L1e
            if (r7 == 0) goto L1e
            com.netease.ntunisdk.okhttp3.internal.connection.RouteSelector r5 = r6.routeSelector     // Catch: java.lang.Throwable -> L62
            r5.connectFailed(r1, r7)     // Catch: java.lang.Throwable -> L62
            goto L1e
        L41:
            r7 = r3
            goto L44
        L43:
            r7 = r4
        L44:
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r1 = r6.connection     // Catch: java.lang.Throwable -> L62
            java.net.Socket r7 = r6.deallocate(r7, r4, r3)     // Catch: java.lang.Throwable -> L62
            com.netease.ntunisdk.okhttp3.internal.connection.RealConnection r3 = r6.connection     // Catch: java.lang.Throwable -> L62
            if (r3 != 0) goto L54
            boolean r3 = r6.reportedAcquired     // Catch: java.lang.Throwable -> L62
            if (r3 != 0) goto L53
            goto L54
        L53:
            r2 = r1
        L54:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L62
            com.netease.ntunisdk.okhttp3.internal.Util.closeQuietly(r7)
            if (r2 == 0) goto L61
            com.netease.ntunisdk.okhttp3.EventListener r7 = r6.eventListener
            com.netease.ntunisdk.okhttp3.Call r0 = r6.call
            r7.connectionReleased(r0, r2)
        L61:
            return
        L62:
            r7 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L62
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.okhttp3.internal.connection.StreamAllocation.streamFailed(java.io.IOException):void");
    }

    public void streamFinished(boolean z, HttpCodec httpCodec, long j, IOException iOException) {
        RealConnection realConnection;
        Socket socketDeallocate;
        boolean z2;
        this.eventListener.responseBodyEnd(this.call, j);
        synchronized (this.connectionPool) {
            if (httpCodec != null) {
                if (httpCodec == this.codec) {
                    if (!z) {
                        this.connection.successCount++;
                    }
                    realConnection = this.connection;
                    socketDeallocate = deallocate(z, false, true);
                    if (this.connection != null) {
                        realConnection = null;
                    }
                    z2 = this.released;
                }
            }
            throw new IllegalStateException("expected " + this.codec + " but was " + httpCodec);
        }
        Util.closeQuietly(socketDeallocate);
        if (realConnection != null) {
            this.eventListener.connectionReleased(this.call, realConnection);
        }
        if (iOException != null) {
            this.eventListener.callFailed(this.call, Internal.instance.timeoutExit(this.call, iOException));
        } else if (z2) {
            Internal.instance.timeoutExit(this.call, null);
            this.eventListener.callEnd(this.call);
        }
    }

    public String toString() {
        RealConnection realConnectionConnection = connection();
        return realConnectionConnection != null ? realConnectionConnection.toString() : this.address.toString();
    }
}