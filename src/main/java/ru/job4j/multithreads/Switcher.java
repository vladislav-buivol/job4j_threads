package ru.job4j.multithreads;

public class Switcher {
    public static void main(String[] args) throws InterruptedException {
        MasterSlaveBarrier barrier = new MasterSlaveBarrier();
        Thread first = new Thread(
                barrier::tryMaster
        );
        Thread second = new Thread(
                barrier::trySlave
        );
        first.start();
        second.start();
        first.join();
        second.join();
    }
}