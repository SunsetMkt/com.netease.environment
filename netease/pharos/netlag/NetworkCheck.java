package com.netease.pharos.netlag;

import com.netease.pharos.netlag.NetworkCheckConfig;
import com.netease.pharos.util.LogUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetworkCheck {
    private static final String TAG = "NetworkCheck";
    private AtomicInteger mIpInfoCount;
    private NetworkCheckConfig mNetworkCheckConfig;
    private NetworkCheckListener mNetworkCheckListener;
    private Thread mReceiveThread;
    private final AtomicBoolean mStopCheck = new AtomicBoolean(false);

    NetworkCheckConfig getNetworkCheckConfig() {
        return this.mNetworkCheckConfig;
    }

    void init(JSONObject jSONObject, NetworkCheckListener networkCheckListener) throws Exception {
        this.mNetworkCheckListener = networkCheckListener;
        NetworkCheckConfig networkCheckConfig = new NetworkCheckConfig();
        this.mNetworkCheckConfig = networkCheckConfig;
        networkCheckConfig.init(jSONObject);
        this.mNetworkCheckConfig.mIpInfos.size();
    }

    void exec() {
        new Thread(new Runnable() { // from class: com.netease.pharos.netlag.NetworkCheck.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                NetworkCheck.this.start();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void start() throws IOException {
        Iterator<NetworkCheckConfig.IpInfo> it;
        final HashMap map = new HashMap();
        boolean z = false;
        this.mIpInfoCount = new AtomicInteger(0);
        final long jCurrentTimeMillis = System.currentTimeMillis();
        int i = 1;
        try {
            final Selector selectorOpen = Selector.open();
            Iterator<NetworkCheckConfig.IpInfo> it2 = this.mNetworkCheckConfig.mIpInfos.iterator();
            final int length = 0;
            while (it2.hasNext()) {
                NetworkCheckConfig.IpInfo next = it2.next();
                try {
                    DatagramChannel datagramChannelOpen = DatagramChannel.open();
                    datagramChannelOpen.configureBlocking(z);
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(next.ip, Integer.parseInt(next.port));
                    datagramChannelOpen.connect(inetSocketAddress);
                    datagramChannelOpen.register(selectorOpen, i);
                    ArrayList arrayList = new ArrayList();
                    int lagPks = this.mNetworkCheckConfig.getLagPks();
                    int i2 = z;
                    while (i2 < lagPks) {
                        if (map.containsKey(next.ip)) {
                            it = it2;
                        } else {
                            it = it2;
                            try {
                                long jCurrentTimeMillis2 = System.currentTimeMillis();
                                byte[] bytes = (this.mNetworkCheckConfig.getLagString() + jCurrentTimeMillis2).getBytes(StandardCharsets.UTF_8);
                                length = bytes.length;
                                datagramChannelOpen.send(ByteBuffer.wrap(bytes), inetSocketAddress);
                                arrayList.add(Long.valueOf(jCurrentTimeMillis2));
                            } catch (Exception e) {
                                e = e;
                                e.printStackTrace();
                                it2 = it;
                                z = false;
                                i = 1;
                            }
                        }
                        i2++;
                        it2 = it;
                    }
                    it = it2;
                    if (arrayList.size() == lagPks) {
                        map.put(next.ip, arrayList);
                    }
                } catch (Exception e2) {
                    e = e2;
                    it = it2;
                }
                it2 = it;
                z = false;
                i = 1;
            }
            LogUtil.i("NetLag ", "send cost:" + (System.currentTimeMillis() - jCurrentTimeMillis) + ", timeOut:" + this.mNetworkCheckConfig.getLagTimeout());
            final int lagPks2 = this.mNetworkCheckConfig.getLagPks() * 2;
            Thread thread = new Thread(new Runnable() { // from class: com.netease.pharos.netlag.NetworkCheck.2
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    SelectionKey next2;
                    Exception e3;
                    while (selectorOpen.select(700L) > 0 && !NetworkCheck.this.mStopCheck.get()) {
                        try {
                            try {
                                try {
                                    Iterator<SelectionKey> it3 = selectorOpen.selectedKeys().iterator();
                                    while (it3.hasNext() && !NetworkCheck.this.mStopCheck.get()) {
                                        try {
                                            next2 = it3.next();
                                            try {
                                                it3.remove();
                                                if (next2.isReadable()) {
                                                    DatagramChannel datagramChannel = (DatagramChannel) next2.channel();
                                                    ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length);
                                                    InetSocketAddress inetSocketAddress2 = (InetSocketAddress) datagramChannel.receive(byteBufferAllocate);
                                                    byteBufferAllocate.flip();
                                                    String str = "" + inetSocketAddress2.toString();
                                                    while (byteBufferAllocate.hasRemaining()) {
                                                        byteBufferAllocate.get(new byte[byteBufferAllocate.limit()]);
                                                        str = str + new String(byteBufferAllocate.array());
                                                    }
                                                    byteBufferAllocate.clear();
                                                    ArrayList arrayList2 = (ArrayList) map.get(inetSocketAddress2.getAddress().getHostAddress());
                                                    if (arrayList2 != null && arrayList2.size() < lagPks2) {
                                                        arrayList2.add(Long.valueOf(System.currentTimeMillis()));
                                                    } else {
                                                        LogUtil.i("NetLag ", "data is null:" + inetSocketAddress2.getAddress().getHostAddress());
                                                        datagramChannel.disconnect();
                                                    }
                                                    LogUtil.i("NetLag ", "receive:" + str);
                                                }
                                            } catch (Exception e4) {
                                                e3 = e4;
                                                e3.printStackTrace();
                                                if (next2 != null) {
                                                    next2.cancel();
                                                    next2.channel().close();
                                                }
                                            }
                                        } catch (Exception e5) {
                                            next2 = null;
                                            e3 = e5;
                                        }
                                    }
                                } catch (Throwable th) {
                                    try {
                                        selectorOpen.close();
                                    } catch (IOException e6) {
                                        e6.printStackTrace();
                                    }
                                    throw th;
                                }
                            } catch (Exception e7) {
                                e7.printStackTrace();
                                selectorOpen.close();
                            }
                        } catch (IOException e8) {
                            e8.printStackTrace();
                        }
                    }
                    LogUtil.i("NetLag ", "UDP Test Total Cost:" + (System.currentTimeMillis() - jCurrentTimeMillis));
                    if (NetworkCheck.this.mNetworkCheckListener != null && !NetworkCheck.this.mStopCheck.get()) {
                        NetworkCheck.this.mNetworkCheckListener.callBack(NetworkResult.success(new UDPCostComputer().compute(NetworkCheck.this.mNetworkCheckConfig.mIpInfos, map, NetworkCheck.this.mNetworkCheckConfig.getLagPks())));
                    }
                    selectorOpen.close();
                    NetworkCheck.this.mStopCheck.set(false);
                }
            });
            this.mReceiveThread = thread;
            thread.start();
        } catch (IOException e3) {
            e3.printStackTrace();
            NetworkCheckListener networkCheckListener = this.mNetworkCheckListener;
            if (networkCheckListener != null) {
                networkCheckListener.callBack(NetworkResult.fail(1));
            }
        }
    }

    synchronized void cancel() {
        LogUtil.i("NetLag ", "cancel executor");
        this.mStopCheck.set(true);
        this.mIpInfoCount.set(0);
        this.mNetworkCheckListener = null;
        this.mNetworkCheckConfig = null;
    }
}