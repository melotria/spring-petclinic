package org.springframework.samples.petclinic.junie

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(JunieDuplicationController::class)
class JunieDuplicationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var junieDuplicationService: JunieDuplicationService

    @MockBean
    private lateinit var ownerRepository: OwnerRepository

    private lateinit var owner: Owner

    @BeforeEach
    fun setup() {
        owner = Owner()
        owner.id = 1
        owner.firstName = "John"
        owner.lastName = "Doe"
        owner.address = "123 Main St"
        owner.city = "Anytown"
        owner.telephone = "555-1234"

        `when`(ownerRepository.findById(1)).thenReturn(Optional.of(owner))
        `when`(ownerRepository.findAll()).thenReturn(listOf(owner))
    }

    @Test
    fun testDuplicateOwner() = runBlocking<Unit> {
        `when`(junieDuplicationService.duplicateOwner(owner)).thenReturn(owner)

        mockMvc.perform(get("/api/junie/duplication/owner/1"))
            .andExpect(status().isOk)

        verify(junieDuplicationService).duplicateOwner(owner)
    }

    @Test
    fun testDuplicateAllOwners() = runBlocking<Unit> {
        `when`(junieDuplicationService.duplicateOwners(listOf(owner))).thenReturn(listOf(owner))

        mockMvc.perform(get("/api/junie/duplication/owners"))
            .andExpect(status().isOk)

        verify(junieDuplicationService).duplicateOwners(listOf(owner))
    }

    @Test
    fun testDuplicateAllOwnersStream() {
        `when`(junieDuplicationService.duplicateOwnersAsFlow(listOf(owner))).thenReturn(flowOf(owner))

        mockMvc.perform(get("/api/junie/duplication/owners/stream"))
            .andExpect(status().isOk)

        verify(junieDuplicationService).duplicateOwnersAsFlow(listOf(owner))
    }
}
