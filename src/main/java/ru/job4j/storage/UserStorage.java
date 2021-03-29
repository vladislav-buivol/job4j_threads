package ru.job4j.storage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
public class UserStorage implements Storage<User> {
    @GuardedBy("this")
    private final List<User> users = new ArrayList<>();

    public UserStorage() {
    }

    @Override
    public synchronized boolean add(User user) {
        if (!users.contains(user)) {
            users.add(user);
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
        return users.remove(user);
    }

    @Override
    public synchronized void transfer(int fromId, int toId, int amount) {
        User userFrom = findById(fromId);
        User userTo = findById(toId);
        userFrom.setAmount(userFrom.getAmount() + amount);
        userTo.setAmount(userTo.getAmount() - amount);
    }

    public synchronized User findById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
}
