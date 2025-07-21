package com.netease.cloud.nos.android.pipeline;

import a.a.c.az;
import a.a.c.b.c;
import a.a.c.bj;
import a.a.d.a.a.ab;
import a.a.d.a.a.an;
import a.a.d.a.a.ap;

/* loaded from: classes5.dex */
public class HttpChannelInitializer extends az<c> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // a.a.c.az
    public void initChannel(c cVar) throws Exception {
        bj bjVarB = cVar.b();
        bjVarB.a("decoder", new ap());
        bjVarB.a("encoder", new an());
        bjVarB.a("aggregator", new ab(1048576));
        bjVarB.a("handler", new PipelineHttpClientHandler());
    }
}