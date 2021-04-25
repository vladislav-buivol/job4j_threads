package ru.job4j.mail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    public final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        pool.submit(() -> {
            Notification notification = new Notification(user);
            send(notification.getSubject(), notification.getBody(), notification.getEmail());
        });
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String subject, String body, String email) {
    }

    public static void main(String[] args) {
        EmailNotification emailNotification = new EmailNotification();
        for (int i = 0; i < 1001; i++) {
            emailNotification.emailTo(new User(String.format("User%s", i), String.format("u%s@mail.com", i)));
        }
        emailNotification.close();
    }
}
