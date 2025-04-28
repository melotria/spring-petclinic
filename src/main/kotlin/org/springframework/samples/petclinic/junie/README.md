# Junie Duplication API with Kotlin Coroutines

This package contains an implementation of the Junie Duplication API using Kotlin coroutines. The API provides functionality to duplicate entities asynchronously.

## Overview

The implementation consists of the following components:

1. **JunieDuplicationApi**: The core API that provides methods to duplicate entities asynchronously.
2. **JunieDuplicationService**: A service that uses the API to duplicate entities.
3. **JunieDuplicationController**: A REST controller that exposes the service functionality.

## Using the API

### Duplicating a Single Entity

```kotlin
// Inject the API
@Autowired
private lateinit var junieDuplicationApi: JunieDuplicationApi

// Use the API in a suspend function
suspend fun duplicateEntity(entity: MyEntity): MyEntity {
    return junieDuplicationApi.duplicateAsync(entity)
}
```

### Duplicating Multiple Entities

```kotlin
// Inject the API
@Autowired
private lateinit var junieDuplicationApi: JunieDuplicationApi

// Use the API in a suspend function
suspend fun duplicateEntities(entities: List<MyEntity>): List<MyEntity> {
    return junieDuplicationApi.duplicateAllAsync(entities)
}
```

### Using Kotlin Flow

```kotlin
// Inject the service
@Autowired
private lateinit var junieDuplicationService: JunieDuplicationService

// Use the service to get a Flow of duplicated entities
fun duplicateEntitiesAsFlow(entities: List<MyEntity>): Flow<MyEntity> {
    return junieDuplicationService.duplicateEntitiesAsFlow(entities)
}
```

## REST API Endpoints

The following REST API endpoints are available:

- `GET /api/junie/duplication/owner/{ownerId}`: Duplicates an owner by ID.
- `GET /api/junie/duplication/owners`: Duplicates all owners.
- `GET /api/junie/duplication/owners/stream`: Duplicates all owners and returns them as a server-sent events stream.

## Testing

The implementation includes tests for the API, service, and controller. The tests demonstrate how to use the API in a test environment.

### Testing the API

```kotlin
@Test
fun testDuplicateAsync() = runBlocking {
    val entity = MyEntity()
    val duplicatedEntity = junieDuplicationApi.duplicateAsync(entity)
    assertEquals(entity, duplicatedEntity)
}
```

### Testing the Controller

```kotlin
@Test
fun testDuplicateOwner() = runBlocking<Unit> {
    `when`(junieDuplicationService.duplicateOwner(owner)).thenReturn(owner)

    mockMvc.perform(get("/api/junie/duplication/owner/1"))
        .andExpect(status().isOk)

    verify(junieDuplicationService).duplicateOwner(owner)
}
```

## Benefits of Using Kotlin Coroutines

- **Asynchronous Programming**: Coroutines make it easy to write asynchronous code that looks like synchronous code.
- **Non-Blocking**: Coroutines allow you to write non-blocking code without callbacks.
- **Structured Concurrency**: Coroutines provide a structured way to handle concurrency.
- **Integration with Spring**: Spring WebFlux supports Kotlin coroutines out of the box.
- **Flow API**: Kotlin Flow provides a way to handle streams of data asynchronously.
