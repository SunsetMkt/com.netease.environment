package com.netease.mpay.httpdns;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes5.dex */
public abstract class b<Params, Progress, Result> {

    /* renamed from: a, reason: collision with root package name */
    public static final Executor f1586a;
    public static final Executor b = new ExecutorC0065b(null);
    public static final int c;
    public static final int d;
    public static final int e;
    public static final ThreadFactory f;
    public static final BlockingQueue<Runnable> g;

    public class a implements ThreadFactory {

        /* renamed from: a, reason: collision with root package name */
        public final AtomicInteger f1587a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "AsyncTask #" + this.f1587a.getAndIncrement());
        }
    }

    /* renamed from: com.netease.mpay.httpdns.b$b, reason: collision with other inner class name */
    public static class ExecutorC0065b implements Executor {

        /* renamed from: a, reason: collision with root package name */
        public final ArrayDeque<Runnable> f1588a = new ArrayDeque<>();
        public Runnable b;

        /* renamed from: com.netease.mpay.httpdns.b$b$a */
        public class a implements Runnable {

            /* renamed from: a, reason: collision with root package name */
            public final /* synthetic */ Runnable f1589a;

            public a(Runnable runnable) {
                this.f1589a = runnable;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    this.f1589a.run();
                } finally {
                    ExecutorC0065b.this.a();
                }
            }
        }

        public /* synthetic */ ExecutorC0065b(a aVar) {
        }

        public synchronized void a() {
            Runnable runnablePoll = this.f1588a.poll();
            this.b = runnablePoll;
            if (runnablePoll != null) {
                b.f1586a.execute(this.b);
            }
        }

        @Override // java.util.concurrent.Executor
        public synchronized void execute(Runnable runnable) {
            this.f1588a.offer(new a(runnable));
            if (this.b == null) {
                a();
            }
        }
    }

    static {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        c = iAvailableProcessors;
        int iMax = Math.max(2, Math.min(iAvailableProcessors - 1, 4));
        d = iMax;
        int i = (iAvailableProcessors * 2) + 1;
        e = i;
        a aVar = new a();
        f = aVar;
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(256);
        g = linkedBlockingQueue;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(iMax, i, 30L, TimeUnit.SECONDS, linkedBlockingQueue, aVar);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        f1586a = threadPoolExecutor;
    }
}