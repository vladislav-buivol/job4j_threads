package ru.job4j.net;

import java.io.*;
import java.net.URL;
import java.util.Date;

public class FileDownload {

    public void download(String file) throws InterruptedException {
        download(file, -1);
    }

    public void download(String file, double speedLimit) throws InterruptedException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(file).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            int bufferSize = 1024;
            byte[] dataBuffer = new byte[bufferSize];
            int bytesRead;
            DownloadSpeedLimiter limiter = new DownloadSpeedLimiter(bufferSize, speedLimit);
            limiter.start();
            while ((bytesRead = in.read(dataBuffer, 0, bufferSize)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                limiter.end();
                limiter.waitIfNeed();
                limiter.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DownloadSpeedLimiter {
        private Date startTime = new Date();
        private Date endTime = new Date();
        private final long bufferSize;
        private double maxWaitingTimeInSec;
        private final double maxAllowedSpeedInSec;

        public DownloadSpeedLimiter(long bufferSize, double maxSpeed) {
            this.bufferSize = bufferSize;
            this.maxAllowedSpeedInSec = maxSpeed;
            this.maxWaitingTimeInSec = (bufferSize / maxSpeed);
        }

        public void start() {
            this.startTime = new Date();
        }

        public void end() {
            this.endTime = new Date();
        }

        public double spendTimeInSec() {
            return (double) (endTime.getTime() - startTime.getTime()) / 1000;
        }

        public double currentSpeed() {
            double spendTimeInSec = spendTimeInSec();
            if (spendTimeInSec == 0) {
                return bufferSize;
            } else {
                return bufferSize / spendTimeInSec;
            }
        }

        public long timeToWaitInMs() {
            return (long) ((maxWaitingTimeInSec - spendTimeInSec()) * 1000);
        }

        public void waitIfNeed() throws InterruptedException {
            if(maxAllowedSpeedInSec < 0){
                return;
            }
            if (currentSpeed() > maxAllowedSpeedInSec) {
                System.out.println(String.format("Will wait %s ms", timeToWaitInMs()));
                Thread.sleep(timeToWaitInMs());
            }
        }

    }
}