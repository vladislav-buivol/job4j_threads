package ru.job4j;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CASCountTest {

    @Test
    public void testIncrementZero() throws InterruptedException {
        CASCount casCount = new CASCount(0);
        int nrOfIncrement = 100;
        ThreadIncrement thread = new ThreadIncrement(casCount, nrOfIncrement);
        ThreadIncrement thread2 = new ThreadIncrement(casCount, nrOfIncrement);
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        assertThat(casCount.get(), is(nrOfIncrement * 2));
    }

    @Test
    public void testIncrementStartFromNull() throws InterruptedException {
        CASCount casCount = new CASCount(null);
        int nrOfIncrement = 100;
        ThreadIncrement thread = new ThreadIncrement(casCount, nrOfIncrement);
        ThreadIncrement thread2 = new ThreadIncrement(casCount, nrOfIncrement);
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        assertThat(casCount.get(), is(nrOfIncrement * 2));
    }

    @Test
    public void testIncrementStartFromTen() throws InterruptedException {
        CASCount casCount = new CASCount(10);
        int nrOfIncrement = 100;
        ThreadIncrement thread = new ThreadIncrement(casCount, nrOfIncrement);
        ThreadIncrement thread2 = new ThreadIncrement(casCount, nrOfIncrement);
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        assertThat(casCount.get(), is(nrOfIncrement * 2 + 10));
    }

    private class ThreadIncrement extends Thread {
        private CASCount casCount;
        private final int nrOfIncrement;

        private ThreadIncrement(CASCount casCount, int nrOfIncrement) {
            this.casCount = casCount;
            this.nrOfIncrement = nrOfIncrement;
        }

        @Override
        public void run() {
            for (int i = 0; i < nrOfIncrement; i++) {
                casCount.increment();
            }
        }
    }

}