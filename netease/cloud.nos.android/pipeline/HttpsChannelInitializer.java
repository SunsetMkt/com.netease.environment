package com.netease.cloud.nos.android.pipeline;

import a.a.c.az;
import a.a.c.b.c;
import a.a.c.bj;
import a.a.d.a.a.ab;
import a.a.d.a.a.an;
import a.a.d.a.a.ap;
import a.a.d.b.d;
import com.netease.cloud.nos.android.ssl.SSLTrustAllSocketFactory;
import javax.net.ssl.SSLEngine;

/* loaded from: classes5.dex */
public class HttpsChannelInitializer extends az<c> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // a.a.c.az
    public void initChannel(c cVar) throws Exception {
        bj bjVarB = cVar.b();
        SSLEngine sslEngine = ((SSLTrustAllSocketFactory) SSLTrustAllSocketFactory.getSocketFactory()).getSslEngine();
        sslEngine.setUseClientMode(true);
        bjVarB.a("ssl", new d(sslEngine));
        bjVarB.a("decoder", new ap());
        bjVarB.a("encoder", new an());
        bjVarB.a("aggregator", new ab(1048576));
        bjVarB.a("handler", new PipelineHttpClientHandler());
    }
}