package ru.job4j.storage;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
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
    public void testTransferInMultiplyThreads() {
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
        assertThat(storage.findById(1).getAmount(), is(70));
        assertThat(storage.findById(2).getAmount(), is(130));
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeBalanceErrorTransfer() {
        UserStorage storage = new UserStorage();
        storage.add(new User(1, 100));
        storage.add(new User(2, 100));
        String expectedErrorMessage = "Negative amount";
        try {
            storage.transfer(1, 2, -1000);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(expectedErrorMessage));
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testInsufficientFundsErrorTransfer() {
        UserStorage storage = new UserStorage();
        storage.add(new User(1, 100));
        storage.add(new User(2, 100));
        String expectedErrorMessage = "Insufficient funds";
        try {
            storage.transfer(1, 2, 1000);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(expectedErrorMessage));
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testUserNotExistErrorTransfer() {
        UserStorage storage = new UserStorage();
        storage.add(new User(1, 100));
        String expectedErrorMessage = "User 2 does not exist";
        try {
            storage.transfer(1, 2, 10);
        } catch (Exception e) {
            assertThat(e.getMessage(), is(expectedErrorMessage));
            throw e;
        }
    }

    @Test
    public void testUpdateInMultiplyThreads() {
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

    @Test
    public void testUpdateAddDelete() {
        UserStorage storage = new UserStorage();
        User user = new User(1, 100);
        User newUser = new User(1, 10);
        assertThat(storage.update(newUser), is(false));
        assertThat(storage.delete(newUser), is(false));
        assertThat(storage.add(user), is(true));
        assertThat(storage.add(newUser), is(false));
        assertThat(storage.update(newUser), is(true));
        assertThat(storage.findById(newUser.getId()), is(newUser));
        assertThat(storage.delete(newUser), is(true));
    }

    @Test
    public void testAddInMultiplyThreads() {
        UserStorage storage = new UserStorage();
        User user1 = new User(1, 10);
        User user2 = new User(1, 20);
        User user3 = new User(1, 30);
        new Thread(() -> storage.add(user1)).start();
        new Thread(() -> storage.add(user2)).start();
        new Thread(() -> storage.add(user3)).start();
        assertThat(storage.findById(1), is(user1));
    }
}