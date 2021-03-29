package ru.job4j.storage;

public interface Storage<T> {
    boolean add(T t);

    boolean update(T t);

    boolean delete(T t);

    void transfer(int fromId, int toId, int amount);

}
