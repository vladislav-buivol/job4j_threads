package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    public CASCount(Integer initValue) {
        if (initValue == null) {
            count.set(0);
        } else {
            count.set(initValue);
        }
    }

    public void increment() {
        Integer newValue;
        Integer refValue;
        do {
            refValue = count.get();
            newValue = refValue + 1;
        }
        while (!count.compareAndSet(refValue, newValue));
    }

    public int get() {
        return count.get();
    }
}