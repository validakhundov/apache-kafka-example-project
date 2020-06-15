package com.validakhundov.medium.kafka.metric.collector.model;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metric {

    private Long totalMemorySize;
    private Long freeMemorySize;
    private Float memoryUsagePercent;

    private Long totalDiskSize;
    private Long freeDiskSize;
    private Float diskUsagePercent;

    private Float cpuUsagePercent;

}
