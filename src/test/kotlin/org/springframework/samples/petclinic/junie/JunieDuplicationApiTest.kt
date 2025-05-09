package org.springframework.samples.petclinic.junie

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.samples.petclinic.owner.Owner

/**
 * Test for the Junie Duplication API using Kotlin coroutines.
 */
class JunieDuplicationApiTest {

    private val junieDuplicationApi = JunieDuplicationApi()

    /**
     * Test duplicating a single entity asynchronously.
     */
    @Test
    fun testDuplicateAsync() = runBlocking {
        // Create a test entity
        val owner = Owner()
        owner.firstName = "John"
        owner.lastName = "Doe"
        owner.address = "123 Main St"
        owner.city = "Anytown"
        owner.telephone = "555-1234"

        // Duplicate the entity asynchronously
        val duplicatedOwner = junieDuplicationApi.duplicateAsync(owner)

        // Verify the duplicated entity
        assertEquals(owner.firstName, duplicatedOwner.firstName)
        assertEquals(owner.lastName, duplicatedOwner.lastName)
        assertEquals(owner.address, duplicatedOwner.address)
        assertEquals(owner.city, duplicatedOwner.city)
        assertEquals(owner.telephone, duplicatedOwner.telephone)
    }

    /**
     * Test duplicating multiple entities asynchronously.
     */
    @Test
    fun testDuplicateAllAsync() = runBlocking {
        // Create test entities
        val owner1 = Owner()
        owner1.firstName = "John"
        owner1.lastName = "Doe"

        val owner2 = Owner()
        owner2.firstName = "Jane"
        owner2.lastName = "Smith"

        val owners = listOf(owner1, owner2)

        // Duplicate the entities asynchronously
        val duplicatedOwners = junieDuplicationApi.duplicateAllAsync(owners)

        // Verify the duplicated entities
        assertEquals(2, duplicatedOwners.size)
        assertEquals(owner1.firstName, duplicatedOwners[0].firstName)
        assertEquals(owner1.lastName, duplicatedOwners[0].lastName)
        assertEquals(owner2.firstName, duplicatedOwners[1].firstName)
        assertEquals(owner2.lastName, duplicatedOwners[1].lastName)
    }
}
