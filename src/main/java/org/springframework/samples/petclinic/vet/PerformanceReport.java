package org.springframework.samples.petclinic.vet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating performance reports.
 */
public class PerformanceReport {

    private static final String REPORT_FILE = "vet_query_performance_report.txt";

    /**
     * Writes a performance report to a file.
     *
     * @param beforeOptimization Execution time before optimization in milliseconds
     * @param afterOptimization Execution time after optimization in milliseconds
     * @param optimizationDetails Description of the optimization changes made
     */
    public static void generateReport(long beforeOptimization, long afterOptimization, String optimizationDetails) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
            writer.println("VET QUERY PERFORMANCE OPTIMIZATION REPORT");
            writer.println("=========================================");
            writer.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println();

            writer.println("PERFORMANCE MEASUREMENTS");
            writer.println("------------------------");
            writer.println("Execution time before optimization: " + beforeOptimization + " ms");
            writer.println("Execution time after optimization: " + afterOptimization + " ms");

            // Calculate improvement
            double improvementPercent = ((double) (beforeOptimization - afterOptimization) / beforeOptimization) * 100;
            writer.println("Performance improvement: " + String.format("%.2f", improvementPercent) + "%");
            writer.println("Absolute time saved: " + (beforeOptimization - afterOptimization) + " ms");
            writer.println();

            writer.println("OPTIMIZATION DETAILS");
            writer.println("-------------------");
            writer.println(optimizationDetails);

            System.out.println("Performance report generated: " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("Error writing performance report: " + e.getMessage());
        }
    }
}
