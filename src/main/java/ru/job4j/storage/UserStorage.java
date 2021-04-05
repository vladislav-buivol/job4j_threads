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
        return users.putIfAbsent(user.getId(), user) == null;
    }

    @Override
    public synchronized boolean update(User user) {
        return users.replace(user.getId(), user) != null;
    }

    @Override
    public synchronized boolean delete(User user) {
        return users.remove(user.getId(), user);
    }

    @Override
    public synchronized void transfer(int fromId, int toId, int amount) {
        if (!checkIfUserExist(fromId) || !checkIfUserExist(toId) || amount < 0 || findById(fromId).getAmount() < amount) {
            return;
        }
        User userFrom = findById(fromId);
        User userTo = findById(toId);
        userFrom.setAmount(userFrom.getAmount() - amount);
        userTo.setAmount(userTo.getAmount() + amount);
    }

    /**
     * @param id - user id
     * @return true, if exist else false
     */
    private boolean checkIfUserExist(int id) {
        return findById(id) != null;
    }

    public synchronized User findById(int id) {
        return users.get(id);
    }
}
