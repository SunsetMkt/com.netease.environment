package com.netease.mpay.ps.codescanner.module;

/* loaded from: classes6.dex */
public class DataStructure {

    public static final class StInfo<T> {
        public T data;
        public String errMsg;
        public boolean success;

        public StInfo<T> success(T t) {
            this.success = true;
            this.data = t;
            return this;
        }

        public StInfo<T> fail(String str) {
            this.success = false;
            this.errMsg = str;
            return this;
        }
    }
}