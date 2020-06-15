package com.validakhundov.medium.kafka.metric.collector;

import com.sun.management.OperatingSystemMXBean;
import com.validakhundov.medium.kafka.metric.collector.model.Metric;
import com.validakhundov.medium.kafka.metric.collector.producer.MetricProducer;
import java.lang.management.ManagementFactory;
import com.fasterxml.uuid.Generators;
import java.util.Random;
import java.io.File;

//To monitor the computer's CPU, memory, and disk usage in Java:
//https://stackoverflow.com/questions/47177/how-do-i-monitor-the-computers-cpu-memory-and-disk-usage-in-java


public class MetricCollector {

    public static void main(String[] args) throws InterruptedException {
        String token = Generators.timeBasedGenerator().generate().toString();
        System.out.println("Token: " + token);
        int mb = 1024 * 1024;
        int gb = 1024 * 1024 * 1024;
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long memorySize = os.getTotalPhysicalMemorySize() / mb;
        File disk = new File("/");
        long totalDiskSize = disk.getTotalSpace() / gb;
        int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        while (true) {
            long physicalFreeMemorySize = os.getFreePhysicalMemorySize() / mb;
            long freeDiskSize = disk.getFreeSpace() / gb;
            long startTime = System.nanoTime();
            Random random = new Random(startTime);
            int seed = Math.abs(random.nextInt());
            int primes = 10000;
            long startCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            startTime = System.nanoTime();
            while (primes != 0) {
                if (isPrime(seed)) {
                    primes--;
                }
                seed++;
            }
            float cpuUsagePercent = calculateCpuUsagePercent(startCpuTime, startTime, cpuCount);
            Metric metric = Metric.builder()
                    .freeMemorySize(physicalFreeMemorySize)
                    .totalMemorySize(memorySize)
                    .memoryUsagePercent((float) ((memorySize - physicalFreeMemorySize) * 100 / memorySize))
                    .cpuUsagePercent(cpuUsagePercent)
                    .freeDiskSize(freeDiskSize)
                    .totalDiskSize(totalDiskSize)
                    .diskUsagePercent((float) ((totalDiskSize - freeDiskSize) * 100 / totalDiskSize))
                    .build();
            MetricProducer.produceMetric(metric, token);
            Thread.sleep(1000);
        }

    }

    private static int calculateCpuUsagePercent(long cpuStartTime, long startTime, int cpuCount) {
        long end = System.nanoTime();
        long totalAvailCpuTime = cpuCount * (end - startTime);
        long totalUsedCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - cpuStartTime;
        float per = ((float) totalUsedCpuTime * 100) / (float) totalAvailCpuTime;
        return (int) per;
    }

    private static boolean isPrime(int n) {
        if (n <= 2) {
            return n == 2;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}
