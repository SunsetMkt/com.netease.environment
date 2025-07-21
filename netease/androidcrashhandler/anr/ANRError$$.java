package com.netease.androidcrashhandler.anr;

import java.io.Serializable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes4.dex */
public class ANRError$$ implements Serializable {
    private final String _name;
    private final StackTraceElement[] _stackTrace;

    /* JADX INFO: Access modifiers changed from: private */
    class _Thread extends Throwable {
        private _Thread(_Thread _thread) {
            super(ANRError$$.this._name, _thread);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            setStackTrace(ANRError$$.this._stackTrace);
            return this;
        }
    }

    private ANRError$$(String str, StackTraceElement[] stackTraceElementArr) {
        this._name = str;
        this._stackTrace = stackTraceElementArr;
    }
}