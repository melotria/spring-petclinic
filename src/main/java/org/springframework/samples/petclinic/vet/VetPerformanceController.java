package org.springframework.samples.petclinic.vet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for running vet query performance tests and generating reports.
 */
@Controller
public class VetPerformanceController {

    private final VetPerformanceService performanceService;
    private long originalExecutionTime = -1;

    public VetPerformanceController(VetPerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * Runs performance tests on the original (unoptimized) query.
     *
     * @return A message with the execution time
     */
    @GetMapping("/vets/performance/baseline")
    @ResponseBody
    public String measureBaselinePerformance() {
        originalExecutionTime = performanceService.runPerformanceTest(false);
        return "Baseline performance measured: " + originalExecutionTime + " ms";
    }

    /**
     * Runs performance tests on the optimized query and generates a report.
     *
     * @return A message with the execution time and report status
     */
    @GetMapping("/vets/performance/optimized")
    @ResponseBody
    public String measureOptimizedPerformance() {
        if (originalExecutionTime == -1) {
            return "Please run baseline performance test first (/vets/performance/baseline)";
        }

        long optimizedExecutionTime = performanceService.runPerformanceTest(true);

        String optimizationDetails =
            "1. Changed the fetch type for specialties from EAGER to LAZY to avoid unnecessary loading\n" +
            "2. Added a specific query method with join fetch to load vets with their specialties efficiently\n" +
            "3. Updated the repository implementation to use the optimized query method";

        PerformanceReport.generateReport(originalExecutionTime, optimizedExecutionTime, optimizationDetails);

        return "Optimized performance measured: " + optimizedExecutionTime + " ms\n" +
               "Performance improvement: " +
               String.format("%.2f", ((double)(originalExecutionTime - optimizedExecutionTime) / originalExecutionTime) * 100) +
               "%\nReport generated.";
    }

    /**
     * Runs both baseline and optimized performance tests and generates a report.
     *
     * @return A message with the execution times and report status
     */
    @GetMapping("/vets/performance/test")
    @ResponseBody
    public String runFullPerformanceTest() {
        originalExecutionTime = performanceService.runPerformanceTest(false);
        long optimizedExecutionTime = performanceService.runPerformanceTest(true);

        String optimizationDetails =
            "1. Changed the fetch type for specialties from EAGER to LAZY to avoid unnecessary loading\n" +
            "2. Added a specific query method with join fetch to load vets with their specialties efficiently\n" +
            "3. Updated the repository implementation to use the optimized query method";

        PerformanceReport.generateReport(originalExecutionTime, optimizedExecutionTime, optimizationDetails);

        return "Performance Test Results:\n" +
               "- Original execution time: " + originalExecutionTime + " ms\n" +
               "- Optimized execution time: " + optimizedExecutionTime + " ms\n" +
               "- Performance improvement: " +
               String.format("%.2f", ((double)(originalExecutionTime - optimizedExecutionTime) / originalExecutionTime) * 100) +
               "%\nReport generated.";
    }
}
