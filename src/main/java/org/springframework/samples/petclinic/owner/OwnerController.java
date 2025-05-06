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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.quarkus.panache.common.Page;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Wick Dynex
 */
@Path("/owners")
@Produces(MediaType.TEXT_HTML)
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	@Inject
	OwnerRepository owners;

	@Inject
	Template createOrUpdateOwnerForm;

	@Inject
	Template findOwners;

	@Inject
	Template ownersList;

	@Inject
	Template ownerDetails;

	private Owner getOwner(Integer ownerId) {
		if (ownerId == null) {
			return new Owner();
		}
		return this.owners.findByIdOptional(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
				+ ". Please ensure the ID is correct and the owner exists in the database."));
	}

	@GET
	@Path("/new")
	public TemplateInstance initCreationForm() {
		return createOrUpdateOwnerForm.data("owner", new Owner());
	}

	@POST
	@Path("/new")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response processCreationForm(@BeanParam @Valid Owner owner) {
		// In a real app, you'd use Bean Validation and exception mappers
		// to handle validation errors
		this.owners.persist(owner);
		return Response.seeOther(
			UriBuilder.fromPath("/owners/{id}")
				.build(owner.getId()))
			.build();
	}

	@GET
	@Path("/find")
	public TemplateInstance initFindForm() {
		return findOwners.data("owner", new Owner());
	}

	@GET
	public Response processFindForm(@QueryParam("page") @DefaultValue("1") int page,
			@BeanParam Owner owner) {
		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		int pageSize = 5;
		io.quarkus.panache.common.Page<Owner> ownersResults =
			owners.findByLastNameStartingWith(owner.getLastName(), page, pageSize);

		if (ownersResults.list().isEmpty()) {
			// no owners found
			return Response.ok(findOwners
				.data("owner", owner)
				.data("error", "No owners found with last name: " + owner.getLastName()))
				.build();
		}

		if (ownersResults.list().size() == 1 && ownersResults.pageCount() == 1) {
			// 1 owner found
			owner = ownersResults.list().get(0);
			return Response.seeOther(
				UriBuilder.fromPath("/owners/{id}")
					.build(owner.getId()))
				.build();
		}

		// multiple owners found
		return Response.ok(
			ownersList
				.data("listOwners", ownersResults.list())
				.data("currentPage", page)
				.data("totalPages", ownersResults.pageCount())
				.data("totalItems", owners.count("lastName LIKE ?1", owner.getLastName() + "%")))
			.build();
	}

	@GET
	@Path("/{ownerId}/edit")
	public TemplateInstance initUpdateOwnerForm(@PathParam("ownerId") int ownerId) {
		return createOrUpdateOwnerForm.data("owner", getOwner(ownerId));
	}

	@POST
	@Path("/{ownerId}/edit")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response processUpdateOwnerForm(@BeanParam @Valid Owner owner, @PathParam("ownerId") int ownerId) {
		// In a real app, you'd use Bean Validation and exception mappers
		// to handle validation errors

		if (owner.getId() != ownerId) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(createOrUpdateOwnerForm
					.data("owner", owner)
					.data("error", "Owner ID mismatch. Please try again."))
				.build();
		}

		owner.setId(ownerId);
		this.owners.persist(owner);
		return Response.seeOther(
			UriBuilder.fromPath("/owners/{id}")
				.build(owner.getId()))
			.build();
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a Response with the owner details
	 */
	@GET
	@Path("/{ownerId}")
	public TemplateInstance showOwner(@PathParam("ownerId") int ownerId) {
		Owner owner = getOwner(ownerId);
		return ownerDetails.data("owner", owner);
	}

}
