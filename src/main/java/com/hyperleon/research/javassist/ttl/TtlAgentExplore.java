package com.hyperleon.research.javassist.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * VM Option: -javaagent:path/to/transmittable-thread-local-2.x.y.jar
 *
 * @author leon
 * @date 2021-12-08 22:24
 **/
public class TtlAgentExplore {

    private static final TransmittableThreadLocal<String> TTL = new TransmittableThreadLocal<>();

    public static void main(String[] args) {

        TTL.set("ttl_old_value");

        ThreadPoolExecutor ttlTestExecutor = new ThreadPoolExecutor(3,
                3,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new TtlTestThreadFactory<>(TTL, "ttl_child_value"));

        Runnable runnable = () -> {
            System.out.println("thread[" + Thread.currentThread().getId() + "]" + ":[" + TTL.get() + "]");
        };

        TTL.set(Thread.currentThread().getId() + ":ttl_new_value");

        ttlTestExecutor.execute(runnable);
    }

}
