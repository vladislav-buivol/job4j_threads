package ru.job4j.storage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;

@ThreadSafe
public class UserStorage implements Storage<User> {
    @GuardedBy("this")
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public synchronized boolean add(User user) {
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean update(User user) {
        if (delete(user)) {
            return add(user);
        }
        return false;
    }

    @Override
    public synchronized boolean delete(User user) {
        return users.remove(user.getId(), user);
    }

    @Override
    public synchronized void transfer(int fromId, int toId, int amount) {
        User userFrom = findById(fromId);
        User userTo = findById(toId);
        userFrom.setAmount(userFrom.getAmount() + amount);
        userTo.setAmount(userTo.getAmount() - amount);
    }

    public synchronized User findById(int id) {
        return users.get(id);
    }
}
