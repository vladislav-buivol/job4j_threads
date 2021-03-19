package ru.job4j;

public final class Cache {
    private static Cache cache;

    public synchronized static Cache instOf() {
        if (cache == null) {
            return new Cache();
        }
        return cache;
    }
}