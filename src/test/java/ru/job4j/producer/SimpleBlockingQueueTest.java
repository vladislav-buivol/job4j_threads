package ru.job4j.producer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class SimpleBlockingQueueTest {

    @Test
    public void offer() throws InterruptedException {
        int nrOfElements = 5000;
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(nrOfElements);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < nrOfElements; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        ArrayList<Integer> ls = new ArrayList<>();
        for (int i = 0; i < nrOfElements; i++) {
            ls.add(i);
        }
        assertThat(buffer, is(ls));
    }

    @Test
    public void startPollWhenQueryIsEmpty() throws InterruptedException {
        int nrOfElements = 1000;
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(nrOfElements);
        CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < nrOfElements; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                    consumer.interrupt();
                });
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        ArrayList<Integer> ls = new ArrayList<>();
        for (int i = 0; i < nrOfElements; i++) {
            ls.add(i);
        }
        assertThat(buffer, is(ls));
    }

    @Test
    public void offerWhenFull() throws InterruptedException {
        int size = 5;
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            queue.offer(i);
        }
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
                queue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        producer.start();
        consumer.start();
        consumer.join();
        producer.join();
        assertThat(queue.size(), is(4));
    }

    @Test
    public void pullWhenEmpty() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                queue.poll();
                queue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        assertThat(queue.size(), is(0));
    }

}