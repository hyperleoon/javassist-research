package com.hyperleon.research.javassist.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCopier;
import com.sun.istack.internal.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;

/**
 * @author leon
 * @date 2021-12-08 22:25
 **/
public class TtlTestThreadFactory<T> implements ThreadFactory {

    private final TransmittableThreadLocal<T> ttl;

    private InheritableThreadLocal<T> inheritableThreadLocal;

    private final T childTtlValue;

    private T childInheritableThreadLocalValue;

    public TtlTestThreadFactory(TransmittableThreadLocal<T> ttl,T childTtlValue) {
        this.ttl = ttl;
        this.childTtlValue = childTtlValue;
    }

    public TtlTestThreadFactory(TransmittableThreadLocal<T> ttl,
                                T childTtlValue,
                                InheritableThreadLocal<T> inheritableThreadLocal,
                                T childInheritableThreadLocalValue) {
        this.ttl = ttl;
        this.childTtlValue = childTtlValue;
        this.inheritableThreadLocal = inheritableThreadLocal;
        this.childInheritableThreadLocalValue = childInheritableThreadLocalValue;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        T backupTllValue = ttl.get();
        T backupInheritableThreadLocalValue = null;

        if (Objects.nonNull(inheritableThreadLocal)) {
            backupInheritableThreadLocalValue = inheritableThreadLocal.get();
        }
        setValueForChildThreadCopy();
        Thread t = new Thread(r);

        ttl.set(backupTllValue);

        if (Objects.nonNull(inheritableThreadLocal)) {
            inheritableThreadLocal.set(backupInheritableThreadLocalValue);
        }

        return t;
    }

    private void setValueForChildThreadCopy() {
        ttl.set(childTtlValue);
        if (Objects.nonNull(inheritableThreadLocal)) {
            inheritableThreadLocal.set(childInheritableThreadLocalValue);
        }
    }

}
