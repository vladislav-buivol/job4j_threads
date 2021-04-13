package ru.job4j.producer;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class SimpleBlockingQueueTest {

    @Test
    public void offerWhenFree() throws InterruptedException {
        AtomicInteger i = new AtomicInteger();
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                i.set(queue.poll());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        assertThat(i.get(), is(0));
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(i.get(), is(1));
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