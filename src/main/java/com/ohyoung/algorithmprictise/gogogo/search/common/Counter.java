package com.ohyoung.algorithmprictise.gogogo.search.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 计数器: 给每个链接分配id
 * @author ohyoung
 * @date 2023/3/9 17:56
 */
public class Counter {

    private static final Counter counter = new Counter();

    private AtomicLong count = new AtomicLong();

    public Long get() {
        return count.getAndAdd(1);
    }

    private Counter() {

    }

    public static Counter getInstance() {
        return counter;
    }

}
