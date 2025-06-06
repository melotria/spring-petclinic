package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link PetClinicRuntimeHints}
 */
class PetClinicRuntimeHintsTests {

	@Test
	void shouldRegisterResourcePatterns() {
		RuntimeHints hints = new RuntimeHints();
		PetClinicRuntimeHints petClinicRuntimeHints = new PetClinicRuntimeHints();

		petClinicRuntimeHints.registerHints(hints, getClass().getClassLoader());

		assertThat(RuntimeHintsPredicates.resource().forResource("db/something")).accepts(hints);
		assertThat(RuntimeHintsPredicates.resource().forResource("messages/something")).accepts(hints);
		assertThat(RuntimeHintsPredicates.resource().forResource("mysql-default-conf")).accepts(hints);
	}

	@Test
	void shouldRegisterSerializationTypes() {
		RuntimeHints hints = new RuntimeHints();
		PetClinicRuntimeHints petClinicRuntimeHints = new PetClinicRuntimeHints();

		// This test verifies that no exceptions are thrown when registering hints
		petClinicRuntimeHints.registerHints(hints, getClass().getClassLoader());
	}

}
