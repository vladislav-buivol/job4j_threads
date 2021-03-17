package ru.job4j.concurrent;

import java.util.List;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        LoadingAnimation animation = new LoadingAnimation();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.print(String.format("\r Loading: %s", animation.nextSymbol()));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("\r Loading complete");
                Thread.currentThread().interrupt();
            }
        }
    }

    private class LoadingAnimation {
        private final List<String> symbols = List.of("-", "\\", "|", "/");
        private int currentSymbolIndex = 0;

        public String nextSymbol() {
            if (currentSymbolIndex == symbols.size() - 1) {
                currentSymbolIndex = 0;
            }
            return symbols.get(currentSymbolIndex++);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(1000); /* симулируем выполнение параллельной задачи в течение 1 секунды. */
        progress.interrupt();
    }
}
