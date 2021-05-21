package ru.job4j.multithreads;

public class MasterSlaveBarrier {
    private boolean isMasterDone = false;

    public synchronized void tryMaster() {
        while (!Thread.currentThread().isInterrupted()) {
            while (isMasterDone){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Thread A");
            doneMaster();
        }
    }

    public synchronized void trySlave() {
        while (!Thread.currentThread().isInterrupted()) {
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
    }

    public synchronized void doneSlave() {
        isMasterDone = false;
        this.notify();
    }
}
