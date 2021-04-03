package ru.job4j.synch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SingleLockListTest {

    @Test
    public void add() throws InterruptedException {
        SingleLockList<Integer> list = new SingleLockList<>(new ArrayList<>());
        Thread first = new Thread(() -> list.add(1));
        Thread second = new Thread(() -> list.add(2));
        first.start();
        second.start();
        first.join();
        second.join();
        Set<Integer> rsl = new TreeSet<>();
        list.iterator().forEachRemaining(rsl::add);
        assertThat(rsl, is(Set.of(1, 2)));
    }

    @Test
    public void addArr() throws InterruptedException {
        SingleLockList<Integer[]> list = new SingleLockList<>(new ArrayList<>());
        Thread first = new Thread(() -> {
            list.add(new Integer[]{1});
            list.get(0)[0] = 2;
        });
        Thread second = new Thread(() -> list.add(new Integer[]{2}));
        first.start();
        second.start();
        first.join();
        second.join();
        Iterator<Integer[]> it = list.iterator();
        Iterator<Integer[]> it2 = list.iterator();
        it2.next()[0] = 10;
        assertThat(it.next()[0],is(1));
        assertThat(it.next()[0],is(2));
    }
}