package org.springframework.samples.petclinic.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service to measure and optimize the performance of vet queries.
 */
@Service
public class VetPerformanceService {

    private final VetRepository vetRepository;

    public VetPerformanceService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    /**
     * Measures the execution time of loading all vets using the original method.
     *
     * @return Execution time in milliseconds
     */
    public long measureOriginalFindAllExecutionTime() {
        long startTime = System.currentTimeMillis();

        // Execute the query using the original method
        Collection<Vet> vets = vetRepository.findAll();

        // Force loading of specialties to make a fair comparison
        for (Vet vet : vets) {
            vet.getSpecialties().size();
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * Measures the execution time of loading all vets using the optimized method.
     *
     * @return Execution time in milliseconds
     */
    public long measureOptimizedFindAllExecutionTime() {
        long startTime = System.currentTimeMillis();

        // Execute the query using the optimized method
        Collection<Vet> vets = vetRepository.findAllWithSpecialties();

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * Measures the execution time of loading vets with pagination using the original method.
     *
     * @param pageable Pagination information
     * @return Execution time in milliseconds
     */
    public long measureOriginalFindAllPaginatedExecutionTime(Pageable pageable) {
        long startTime = System.currentTimeMillis();

        // Execute the query using the original method
        Page<Vet> vets = vetRepository.findAll(pageable);

        // Force loading of specialties to make a fair comparison
        for (Vet vet : vets.getContent()) {
            vet.getSpecialties().size();
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * Measures the execution time of loading vets with pagination using the optimized method.
     *
     * @param pageable Pagination information
     * @return Execution time in milliseconds
     */
    public long measureOptimizedFindAllPaginatedExecutionTime(Pageable pageable) {
        long startTime = System.currentTimeMillis();

        // Execute the query using the optimized method
        Page<Vet> vets = vetRepository.findAllWithSpecialties(pageable);

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * Runs performance tests and generates a report.
     * This should be called before and after optimization to compare results.
     *
     * @param isOptimized Whether this test is running on the optimized version
     * @return Average execution time in milliseconds
     */
    public long runPerformanceTest(boolean isOptimized) {
        // Warm up the JVM and database connection
        if (isOptimized) {
            vetRepository.findAllWithSpecialties();
        } else {
            Collection<Vet> vets = vetRepository.findAll();
            for (Vet vet : vets) {
                vet.getSpecialties().size();
            }
        }

        // Measure execution time (average of multiple runs for more accurate results)
        long totalTime = 0;
        int runs = 10;

        for (int i = 0; i < runs; i++) {
            if (isOptimized) {
                totalTime += measureOptimizedFindAllExecutionTime();
            } else {
                totalTime += measureOriginalFindAllExecutionTime();
            }
        }

        long averageTime = totalTime / runs;

        if (isOptimized) {
            System.out.println("Optimized query execution time: " + averageTime + " ms");
        } else {
            System.out.println("Original query execution time: " + averageTime + " ms");
        }

        return averageTime;
    }
}
