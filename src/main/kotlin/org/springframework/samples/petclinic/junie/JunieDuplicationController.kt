package org.springframework.samples.petclinic.junie

import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.web.bind.annotation.*

/**
 * REST controller that demonstrates how to use the Junie Duplication Service.
 * This controller shows how to use coroutines in a Spring MVC controller.
 */
@RestController
@RequestMapping("/api/junie/duplication")
class JunieDuplicationController(
    private val junieDuplicationService: JunieDuplicationService,
    private val ownerRepository: OwnerRepository
) {

    /**
     * Duplicates an owner by ID.
     * 
     * @param ownerId The ID of the owner to duplicate
     * @return The duplicated owner
     */
    @GetMapping("/owner/{ownerId}")
    suspend fun duplicateOwner(@PathVariable ownerId: Int): Owner {
        val owner = ownerRepository.findById(ownerId).orElseThrow { NoSuchElementException("Owner not found") }
        return junieDuplicationService.duplicateOwner(owner)
    }

    /**
     * Duplicates all owners.
     * 
     * @return The list of duplicated owners
     */
    @GetMapping("/owners")
    suspend fun duplicateAllOwners(): List<Owner> {
        val owners = ownerRepository.findAll()
        return junieDuplicationService.duplicateOwners(owners.toList())
    }

    /**
     * Duplicates all owners and returns them as a server-sent events stream.
     * This demonstrates how to use Kotlin Flow with server-sent events.
     * 
     * @return A Flow of duplicated owners as server-sent events
     */
    @GetMapping("/owners/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun duplicateAllOwnersStream(): Flow<Owner> {
        val owners = ownerRepository.findAll()
        return junieDuplicationService.duplicateOwnersAsFlow(owners.toList())
    }
}
