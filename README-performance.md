# Vet Query Performance Optimization

This document describes the performance optimization implemented for the SQL query that loads vets in the Spring PetClinic application.

## Overview

The original implementation used EAGER fetching for the specialties relationship in the Vet entity, which could lead to N+1 query problems or complex join queries. By changing to LAZY fetching and using specific join fetch queries when needed, we can load the data more efficiently.

## Optimizations Implemented

1. Changed the fetch type for specialties from EAGER to LAZY to avoid unnecessary loading
2. Added specific query methods with join fetch to load vets with their specialties efficiently
3. Updated the VetController to use the optimized query methods

## Performance Measurement

A performance measurement framework has been implemented to measure the execution time of the SQL query that loads vets. The framework includes:

- `VetPerformanceService`: Measures the execution time of the SQL query
- `VetPerformanceController`: Provides REST endpoints to run the performance tests
- `VetPerformanceRunner`: Command-line runner that executes the performance tests and generates a report
- `PerformanceReport`: Utility class for generating performance reports

## Running the Performance Tests

There are two ways to run the performance tests:

### 1. Using the Command-Line Runner

Start the application with the "performance-test" profile:

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=performance-test
```

This will run the performance tests and generate a report in the file `vet_query_performance_report.txt`.

### 2. Using the REST Endpoints

1. Start the application:

```
./mvnw spring-boot:run
```

2. Run the baseline performance test:

```
curl http://localhost:8080/vets/performance/baseline
```

3. Run the optimized performance test and generate a report:

```
curl http://localhost:8080/vets/performance/optimized
```

Alternatively, you can run both tests and generate a report in a single request:

```
curl http://localhost:8080/vets/performance/test
```

## Performance Report

The performance report includes:

- Execution time before optimization
- Execution time after optimization
- Performance improvement percentage
- Absolute time saved
- Details of the optimization changes made

The report is generated in the file `vet_query_performance_report.txt`.

## Expected Results

The optimization is expected to significantly improve the performance of the SQL query that loads vets, especially when there are many vets and specialties in the database. The actual improvement will depend on the specific database and data volume.
