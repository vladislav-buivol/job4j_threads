package ru.job4j.storage;

import java.util.Objects;

public class User {
    private final int id;
    private int amount;

    public User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
