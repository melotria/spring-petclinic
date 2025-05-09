package org.springframework.samples.petclinic.junie

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.stereotype.Service

/**
 * Service that uses the Junie Duplication API to duplicate entities.
 * This service demonstrates how to use the coroutine-based API in a Spring service.
 */
@Service
class JunieDuplicationService(private val junieDuplicationApi: JunieDuplicationApi) {

    /**
     * Duplicates an owner entity asynchronously.
     * 
     * @param owner The owner to duplicate
     * @return The duplicated owner
     */
    suspend fun duplicateOwner(owner: Owner): Owner {
        return junieDuplicationApi.duplicateAsync(owner)
    }

    /**
     * Duplicates multiple owner entities asynchronously.
     * 
     * @param owners The list of owners to duplicate
     * @return The list of duplicated owners
     */
    suspend fun duplicateOwners(owners: List<Owner>): List<Owner> {
        return junieDuplicationApi.duplicateAllAsync(owners)
    }

    /**
     * Duplicates owners and returns them as a Flow.
     * This demonstrates how to use Kotlin Flow with the coroutine-based API.
     * 
     * @param owners The list of owners to duplicate
     * @return A Flow of duplicated owners
     */
    fun duplicateOwnersAsFlow(owners: List<Owner>): Flow<Owner> = flow {
        for (owner in owners) {
            val duplicatedOwner = junieDuplicationApi.duplicateAsync(owner)
            emit(duplicatedOwner)
        }
    }
}
