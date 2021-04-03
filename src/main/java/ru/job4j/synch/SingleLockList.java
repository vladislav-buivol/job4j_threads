package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = (List<T>) ((ArrayList<T>) list).clone();
    }

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return (T) copy(list.get(0));
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return ((List<T>) copy(this.list)).iterator();
    }


    private Object copy(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            oos.flush();
            byte[] byteData = bos.toByteArray();
            return readObject(byteData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object readObject(byte[] byteData) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteData)) {
            return new ObjectInputStream(bais).readObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}