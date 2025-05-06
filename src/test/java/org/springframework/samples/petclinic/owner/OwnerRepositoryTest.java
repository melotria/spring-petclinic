package org.springframework.samples.petclinic.owner;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class OwnerRepositoryTest {

    @Inject
    OwnerRepository ownerRepository;

    @Test
    public void testFindByLastNameStartingWith() {
        io.quarkus.panache.common.Page<Owner> owners = ownerRepository.findByLastNameStartingWith("D", 1, 10);
        assertNotNull(owners);
        assertNotNull(owners.list());
        assertEquals(2, owners.list().size()); // Davis and Douglas in sample data
    }

    @Test
    public void testFindPetTypes() {
        List<PetType> petTypes = ownerRepository.findPetTypes();
        assertNotNull(petTypes);
        assertEquals(6, petTypes.size()); // 6 pet types in sample data
    }
}
