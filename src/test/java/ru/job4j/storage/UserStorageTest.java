package ru.job4j.storage;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UserStorageTest {

    private class ThreadTransfer extends Thread {
        private final UserStorage storage;
        private final int fromId;
        private final int toId;
        private final int amount;

        private ThreadTransfer(UserStorage storage, int fromId, int toId, int amount) {
            this.storage = storage;
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
        }

        @Override
        public void run() {
            storage.transfer(fromId, toId, amount);
        }
    }

    private class ThreadUpdate extends Thread {
        private final UserStorage storage;
        private final User newUser;

        private ThreadUpdate(UserStorage storage, User newUser) {
            this.storage = storage;
            this.newUser = newUser;
        }


        @Override
        public void run() {
            storage.update(newUser);
        }
    }

    @Test
    public void testTransfer() {
        UserStorage storage = new UserStorage();
        storage.add(new User(1, 100));
        storage.add(new User(2, 100));
        ExecutorService es = Executors.newFixedThreadPool(5);
        es.execute(() -> {
            new ThreadTransfer(storage, 1, 2, 10).start();
            new ThreadTransfer(storage, 1, 2, 10).start();
            new ThreadTransfer(storage, 2, 1, 10).start();
            new ThreadTransfer(storage, 1, 2, 10).start();
            new ThreadTransfer(storage, 1, 2, 10).start();
        });
        try {
            es.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(storage.findById(1).getAmount() + storage.findById(2).getAmount(), is(200));
        assertThat(storage.findById(1).getAmount(), is(130));
        assertThat(storage.findById(2).getAmount(), is(70));
    }

    @Test
    public void testUpdate() {
        UserStorage storage = new UserStorage();
        storage.add(new User(1, 100));
        ExecutorService es = Executors.newFixedThreadPool(4);
        User newUser = new User(1, 10);
        es.execute(() -> {
            new ThreadUpdate(storage, new User(1, 200)).start();
            new ThreadUpdate(storage, new User(1, 0)).start();
            new ThreadUpdate(storage, new User(1, -100)).start();
            new ThreadUpdate(storage, new User(1, 10)).start();
            new ThreadUpdate(storage, newUser).start();
        });
        try {
            es.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(storage.findById(1), is(newUser));
        assertThat(storage.findById(1).getAmount(), is(newUser.getAmount()));
        assertThat(storage.findById(2), is(nullValue()));
    }
}