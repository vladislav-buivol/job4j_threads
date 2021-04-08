package ru.job4j.producer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class SimpleBlockingQueueTest {

    @Test
    public void offerWhenFree() throws InterruptedException {
        AtomicInteger i = new AtomicInteger();
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        Thread producer = new Thread(() -> {
            queue.offer(1);
        });
        Thread consumer = new Thread(() -> {
            i.set(queue.poll());
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
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        for (int i = 0; i < 5; i++) {
            queue.offer(i);
        }
        Thread producer = new Thread(() -> {
            queue.offer(1);
        });

        Thread consumer = new Thread(() -> {
            queue.poll();
            queue.poll();
        });

        producer.start();
        assertThat(queue.size(),is(5));
        consumer.start();
        consumer.join();
        assertThat(queue.size(),is(4));
        producer.join();
    }

    @Test
    public void pullWhenEmpty() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        ArrayList<Integer> ls = new ArrayList<>();
        Thread producer = new Thread(() -> {
            queue.offer(1);
            });

        Thread consumer = new Thread(() -> {
            ls.add(queue.poll());
            ls.add(queue.poll());
        });

        consumer.start();
        producer.start();
        producer.join();
        assertThat(queue.size(), is(0));
        queue.offer(1);
        consumer.join();
    }

}