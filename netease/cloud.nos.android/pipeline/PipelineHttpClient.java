package com.netease.cloud.nos.android.pipeline;

import a.a.a.c;
import a.a.c.a.k;
import a.a.c.aj;
import a.a.c.ao;
import a.a.c.az;
import a.a.c.b.a.a;
import a.a.c.bb;
import a.a.d.a.a.am;
import a.a.e.b;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.utils.LogUtil;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Vector;

/* loaded from: classes5.dex */
public class PipelineHttpClient {
    protected static final int retryLimit = 1;
    private c cbs;
    private aj connectChannel;
    private List<aj> connectedChannelList;
    protected String ip = null;
    protected int port;
    private PipelineHttpSession session;
    private static final String LOGTAG = LogUtil.makeLogTag(PipelineHttpClient.class);
    public static final b<PipelineHttpSession> SESSION_KEY = b.a("PipelineHttpSession");
    private static List<aj> httpChannelList = new Vector();
    private static c httpCbs = getBootstrap(new HttpChannelInitializer());
    private static List<aj> httpsChannelList = new Vector();
    private static c httpsCbs = getBootstrap(new HttpsChannelInitializer());

    public PipelineHttpClient(int i, boolean z, PipelineHttpSession pipelineHttpSession) {
        c cVar;
        this.connectedChannelList = null;
        this.cbs = null;
        this.port = i;
        this.session = pipelineHttpSession;
        if (z) {
            this.connectedChannelList = httpsChannelList;
            cVar = httpsCbs;
        } else {
            this.connectedChannelList = httpChannelList;
            cVar = httpCbs;
        }
        this.cbs = cVar;
    }

    private aj doConnect() {
        synchronized (this.connectedChannelList) {
            int i = 0;
            while (i < this.connectedChannelList.size()) {
                aj ajVar = this.connectedChannelList.get(i);
                if (ajVar.z()) {
                    String hostAddress = ((InetSocketAddress) ajVar.f()).getAddress().getHostAddress();
                    int port = ((InetSocketAddress) ajVar.f()).getPort();
                    b<PipelineHttpSession> bVar = SESSION_KEY;
                    if (ajVar.a((b) bVar).get() == null && hostAddress.equals(this.ip) && port == this.port) {
                        LogUtil.d(LOGTAG, "reuse active connection to uploadServer ip: " + this.ip);
                        ajVar.a((b) bVar).set(this.session);
                        return ajVar;
                    }
                } else {
                    LogUtil.d(LOGTAG, "doConnect close inactive channel");
                    int i2 = i - 1;
                    this.connectedChannelList.remove(i);
                    if (ajVar.y()) {
                        ajVar.h();
                    }
                    i = i2;
                }
                i++;
            }
            String str = LOGTAG;
            LogUtil.d(str, "doConnect new connect start: " + System.currentTimeMillis());
            ao aoVarA = this.cbs.a(new InetSocketAddress(this.ip, this.port));
            aoVarA.l();
            LogUtil.d(str, "doConnect to uploadServer ip: " + this.ip + ", end:" + System.currentTimeMillis());
            synchronized (this.connectedChannelList) {
                if (!aoVarA.c_()) {
                    aoVarA.d().h();
                    return null;
                }
                aj ajVarD = aoVarA.d();
                ajVarD.a((b) SESSION_KEY).set(this.session);
                this.connectedChannelList.add(ajVarD);
                return ajVarD;
            }
        }
    }

    private static c getBootstrap(az<a.a.c.b.c> azVar) {
        c cVar = new c();
        cVar.a(new k()).a(a.class).a((bb<bb<Boolean>>) bb.y, (bb<Boolean>) true).a((bb<bb<Integer>>) bb.n, (bb<Integer>) 1048576).a((bb<bb<Integer>>) bb.g, (bb<Integer>) 1048576).a((bb<bb<Integer>>) bb.h, (bb<Integer>) 1048576).a((bb<bb<Integer>>) bb.d, (bb<Integer>) Integer.valueOf(WanAccelerator.getConf().getConnectionTimeout())).a(azVar);
        return cVar;
    }

    public void channelClose() {
        synchronized (this.connectedChannelList) {
            aj ajVar = this.connectChannel;
            if (ajVar != null) {
                ajVar.a((b) SESSION_KEY).set(null);
                this.connectedChannelList.remove(this.connectChannel);
                this.connectChannel.h();
                this.connectChannel = null;
            }
        }
    }

    public aj connect(String str) {
        this.connectChannel = null;
        this.ip = str;
        aj ajVarDoConnect = doConnect();
        if (ajVarDoConnect == null) {
            return null;
        }
        this.connectChannel = ajVarDoConnect;
        return ajVarDoConnect;
    }

    public void get(am amVar) {
        if (this.connectChannel != null) {
            synchronized (this) {
                aj ajVar = this.connectChannel;
                if (ajVar != null) {
                    ajVar.a(amVar);
                }
            }
        }
    }

    public ao post(a.a.d.a.a.a aVar) {
        if (aVar == null) {
            return null;
        }
        if (this.connectChannel != null) {
            synchronized (this) {
                aj ajVar = this.connectChannel;
                aoVarA = ajVar != null ? ajVar.a(aVar) : null;
            }
        }
        return aoVarA;
    }

    public void reset() {
        synchronized (this.connectedChannelList) {
            aj ajVar = this.connectChannel;
            if (ajVar != null) {
                ajVar.a((b) SESSION_KEY).set(null);
            }
        }
    }
}