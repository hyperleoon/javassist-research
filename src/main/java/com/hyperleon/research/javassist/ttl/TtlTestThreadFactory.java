package com.hyperleon.research.javassist.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.sun.istack.internal.NotNull;

import java.util.concurrent.ThreadFactory;

/**
 * @author leon
 * @date 2021-12-08 22:25
 **/
public class TtlTestThreadFactory<T> implements ThreadFactory {

    private final TransmittableThreadLocal<T> ttl;

    private final T childTtlValue;

    public TtlTestThreadFactory(TransmittableThreadLocal<T> ttl,T childTtlValue) {
        this.ttl = ttl;
        this.childTtlValue = childTtlValue;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        T backup = ttl.get();
        //for chile thread to copy
        ttl.set(this.childTtlValue);
        Thread t = new Thread(r);
        //roll back
        ttl.set(backup);
        return t;
    }
}
