package ru.job4j.thread;

import ru.job4j.net.FileDownload;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    /**
     * @param url to download
     * @param speed byte/s
     */
    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        FileDownload downloader = new FileDownload();
        try {
            downloader.download(url, speed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        /* Скачать файл*/
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}