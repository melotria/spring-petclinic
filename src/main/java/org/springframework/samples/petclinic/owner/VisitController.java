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

import java.util.Optional;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
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
 * @author Michael Isvy
 * @author Dave Syer
 * @author Wick Dynex
 */
@Path("/owners/{ownerId}/pets/{petId}/visits")
@Produces(MediaType.TEXT_HTML)
class VisitController {

	@Inject
	OwnerRepository owners;

	@Inject
	Template createOrUpdateVisitForm;

	/**
	 * Helper method to load pet and owner data
	 * @param ownerId
	 * @param petId
	 * @return Visit
	 */
	private Visit loadPetWithVisit(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId) {
		Owner owner = owners.findByIdOptional(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Pet not found with id: " + petId);
		}

		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	@GET
	@Path("/new")
	public TemplateInstance initNewVisitForm(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId) {
		Owner owner = owners.findByIdOptional(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Pet not found with id: " + petId);
		}

		Visit visit = loadPetWithVisit(ownerId, petId);

		return createOrUpdateVisitForm
			.data("visit", visit)
			.data("pet", pet)
			.data("owner", owner);
	}

	@POST
	@Path("/new")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response processNewVisitForm(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId,
			@BeanParam @Valid Visit visit) {
		Owner owner = owners.findByIdOptional(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Pet not found with id: " + petId);
		}

		// Validation
		if (visit.getDescription() == null || visit.getDescription().trim().isEmpty()) {
			return Response.ok(createOrUpdateVisitForm
				.data("visit", visit)
				.data("pet", pet)
				.data("owner", owner)
				.data("error", "Description must not be empty"))
				.build();
		}

		owner.addVisit(petId, visit);
		this.owners.persist(owner);

		return Response.seeOther(
			UriBuilder.fromPath("/owners/{id}")
				.build(owner.getId()))
			.build();
	}

}
