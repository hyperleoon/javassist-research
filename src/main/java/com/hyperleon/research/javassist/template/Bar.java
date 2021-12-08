package com.hyperleon.research.javassist.template;

import java.util.function.Supplier;

/**
 * @author leon
 * @date 2021-12-08 19:28
 **/
public interface Bar {

    /**
     * obtain
     * @param supplier supply the whole world
     * @return something cool
     */
    String obtainAnything(Supplier<String> supplier);
}
