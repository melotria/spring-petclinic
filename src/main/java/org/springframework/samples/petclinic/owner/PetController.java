/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Wick Dynex
 */
@Path("/owners/{ownerId}")
@Produces(MediaType.TEXT_HTML)
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	@Inject
	OwnerRepository owners;

	@Inject
	Template createOrUpdatePetForm;

	private Collection<PetType> populatePetTypes() {
		return this.owners.findPetTypes();
	}

	private Owner findOwner(@PathParam("ownerId") int ownerId) {
		return this.owners.findByIdOptional(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));
	}

	private Pet findPet(@PathParam("ownerId") int ownerId, @PathParam("petId") Integer petId) {
		if (petId == null) {
			return new Pet();
		}

		Owner owner = findOwner(ownerId);
		return owner.getPet(petId);
	}

	@GET
	@Path("/pets/new")
	public TemplateInstance initCreationForm(@PathParam("ownerId") int ownerId) {
		Owner owner = findOwner(ownerId);
		Pet pet = new Pet();
		owner.addPet(pet);
		return createOrUpdatePetForm
			.data("pet", pet)
			.data("owner", owner)
			.data("types", populatePetTypes());
	}

	@POST
	@Path("/pets/new")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response processCreationForm(@PathParam("ownerId") int ownerId, @BeanParam @Valid Pet pet) {
		Owner owner = findOwner(ownerId);

		// Validation
		boolean hasErrors = false;
		String errorMessage = null;

		if (pet.getName() != null && !pet.getName().isEmpty() && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
			hasErrors = true;
			errorMessage = "Pet name already exists";
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			hasErrors = true;
			errorMessage = "Birth date cannot be in the future";
		}

		if (hasErrors) {
			return Response.ok(createOrUpdatePetForm
				.data("pet", pet)
				.data("owner", owner)
				.data("types", populatePetTypes())
				.data("error", errorMessage))
				.build();
		}

		owner.addPet(pet);
		this.owners.persist(owner);

		return Response.seeOther(
			UriBuilder.fromPath("/owners/{id}")
				.build(owner.getId()))
			.build();
	}

	@GET
	@Path("/pets/{petId}/edit")
	public TemplateInstance initUpdateForm(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId) {
		Owner owner = findOwner(ownerId);
		Pet pet = owner.getPet(petId);
		return createOrUpdatePetForm
			.data("pet", pet)
			.data("owner", owner)
			.data("types", populatePetTypes());
	}

	@POST
	@Path("/pets/{petId}/edit")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response processUpdateForm(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId,
			@BeanParam @Valid Pet pet) {
		Owner owner = findOwner(ownerId);

		// Validation
		boolean hasErrors = false;
		String errorMessage = null;

		String petName = pet.getName();

		// checking if the pet name already exists for the owner
		if (petName != null && !petName.isEmpty()) {
			Pet existingPet = owner.getPet(petName, false);
			if (existingPet != null && !existingPet.getId().equals(pet.getId())) {
				hasErrors = true;
				errorMessage = "Pet name already exists";
			}
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			hasErrors = true;
			errorMessage = "Birth date cannot be in the future";
		}

		if (hasErrors) {
			return Response.ok(createOrUpdatePetForm
				.data("pet", pet)
				.data("owner", owner)
				.data("types", populatePetTypes())
				.data("error", errorMessage))
				.build();
		}

		updatePetDetails(owner, pet);

		return Response.seeOther(
			UriBuilder.fromPath("/owners/{id}")
				.build(owner.getId()))
			.build();
	}

	/**
	 * Updates the pet details if it exists or adds a new pet to the owner.
	 * @param owner The owner of the pet
	 * @param pet The pet with updated details
	 */
	private void updatePetDetails(Owner owner, Pet pet) {
		Pet existingPet = owner.getPet(pet.getId());
		if (existingPet != null) {
			// Update existing pet's properties
			existingPet.setName(pet.getName());
			existingPet.setBirthDate(pet.getBirthDate());
			existingPet.setType(pet.getType());
		}
		else {
			owner.addPet(pet);
		}
		this.owners.persist(owner);
	}

}
