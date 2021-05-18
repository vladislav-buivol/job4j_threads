package ru.job4j.multithreads;

public class MasterSlaveBarrier {
    private boolean isMasterDone = false;

    public synchronized void tryMaster() {
        while (true) {
            System.out.println("Thread A");
            doneMaster();
        }
    }

    public synchronized void trySlave() {
        while (true) {
            if (!isMasterDone) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Thread B");
            doneSlave();
        }
    }

    public synchronized void doneMaster() {
        isMasterDone = true;
        this.notify();
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void doneSlave() {
        isMasterDone = false;
        this.notify();
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
