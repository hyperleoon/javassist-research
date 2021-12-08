package com.hyperleon.research.javassist.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCopier;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author leon
 * @date 2021-12-08 22:52
 **/
public class YetAnotherTtlCopier<T> extends InheritableThreadLocal<T> implements TtlCopier<T> {

    @Override
    public Object copy(Object parentValue) {
        return parentValue;
    }

    public static void main(String[] args) {

        YetAnotherTtlCopier<String> yetAnotherTtlCopier = new YetAnotherTtlCopier<>();
        TransmittableThreadLocal.Transmitter.registerThreadLocal(yetAnotherTtlCopier,yetAnotherTtlCopier);

        TransmittableThreadLocal<String> ttl = new TransmittableThreadLocal<>();

        yetAnotherTtlCopier.set("old_yet_another_value");
        ttl.set("old_ttl_value");

        ThreadPoolExecutor ttlTestExecutor = new ThreadPoolExecutor(3,
                3,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new TtlTestThreadFactory<>(ttl,
                        "ttl_child_value",
                        yetAnotherTtlCopier,
                        "yet_another_child_value"));

        Runnable runnable = () -> {
            System.out.println("thread[" + Thread.currentThread().getId() + "]" + ":[" + ttl.get() + "]");
            System.out.println("thread[" + Thread.currentThread().getId() + "]" + ":[" + yetAnotherTtlCopier.get() + "]");
        };

        yetAnotherTtlCopier.set("new_yet_another_value");
        ttl.set("new_ttl_value");

        TtlExecutors.getTtlExecutorService(ttlTestExecutor).execute(runnable);
    }
}
