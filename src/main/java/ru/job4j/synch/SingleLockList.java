package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.collection.SimpleArray;

import java.util.Iterator;
import java.util.List;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private final SimpleArray<T> list;

    public SingleLockList(List<T> list) {
        this.list = (SimpleArray<T>) copy(list);
    }

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return copy(this.list).iterator();
    }

    private Iterable<T> copy(Iterable<T> originalList) {
        SimpleArray<T> copy = new SimpleArray<>();
        originalList.forEach(copy::add);
        return copy;
    }
}