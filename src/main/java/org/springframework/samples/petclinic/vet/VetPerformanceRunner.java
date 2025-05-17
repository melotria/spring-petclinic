package org.springframework.samples.petclinic.vet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Command line runner that executes performance tests on vet queries
 * and generates a performance report.
 *
 * This runner is only active when the "performance-test" profile is active.
 * To run the performance test, start the application with:
 * java -jar your-app.jar --spring.profiles.active=performance-test
 */
@Component
@Profile("performance-test")
public class VetPerformanceRunner implements CommandLineRunner {

    private final VetPerformanceService performanceService;

    public VetPerformanceRunner(VetPerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Running vet query performance tests...");

        // Measure original performance
        System.out.println("Measuring original query performance...");
        long originalTime = performanceService.runPerformanceTest(false);

        // Measure optimized performance
        System.out.println("Measuring optimized query performance...");
        long optimizedTime = performanceService.runPerformanceTest(true);

        // Calculate improvement
        double improvementPercent = ((double) (originalTime - optimizedTime) / originalTime) * 100;

        // Generate report
        String optimizationDetails =
            "1. Changed the fetch type for specialties from EAGER to LAZY to avoid unnecessary loading\n" +
            "2. Added specific query methods with join fetch to load vets with their specialties efficiently\n" +
            "3. Updated the VetController to use the optimized query methods\n\n" +
            "The original implementation used EAGER fetching for the specialties relationship, which could lead to N+1 query problems " +
            "or complex join queries. By changing to LAZY fetching and using specific join fetch queries when needed, " +
            "we can load the data more efficiently.";

        PerformanceReport.generateReport(originalTime, optimizedTime, optimizationDetails);

        System.out.println("Performance test completed.");
        System.out.println("Original execution time: " + originalTime + " ms");
        System.out.println("Optimized execution time: " + optimizedTime + " ms");
        System.out.println("Performance improvement: " + String.format("%.2f", improvementPercent) + "%");
        System.out.println("Report generated: vet_query_performance_report.txt");
    }
}
