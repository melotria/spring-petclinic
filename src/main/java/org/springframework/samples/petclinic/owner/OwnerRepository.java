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
import java.util.Optional;

import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

/**
 * Repository class for <code>Owner</code> domain objects using Quarkus Panache.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
@ApplicationScoped
public class OwnerRepository implements PanacheRepository<Owner> {

    @Inject
    EntityManager entityManager;

	/**
	 * Retrieve all {@link PetType}s from the data store.
	 * @return a Collection of {@link PetType}s.
	 */
	public List<PetType> findPetTypes() {
		return entityManager.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name", PetType.class)
			.getResultList();
	}

	/**
	 * Retrieve {@link Owner}s from the data store by last name, returning all owners
	 * whose last name <i>starts</i> with the given name.
	 * @param lastName Value to search for
	 * @param page Page number (1-based)
	 * @param pageSize Page size
	 * @return a Page of matching {@link Owner}s (or an empty Page if none found)
	 */
	public io.quarkus.panache.common.Page<Owner> findByLastNameStartingWith(String lastName, int page, int pageSize) {
		return find("lastName LIKE ?1", Sort.by("lastName"), lastName + "%").page(Page.of(page - 1, pageSize));
	}

	/**
	 * Retrieve an {@link Owner} from the data store by id.
	 * <p>
	 * This method returns an {@link Optional} containing the {@link Owner} if found. If
	 * no {@link Owner} is found with the provided id, it will return an empty
	 * {@link Optional}.
	 * </p>
	 * @param id the id to search for
	 * @return an {@link Optional} containing the {@link Owner} if found, or an empty
	 * {@link Optional} if not found.
	 * @throws IllegalArgumentException if the id is null
	 */
	@Override
	public Optional<Owner> findByIdOptional(@Nonnull Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		return Optional.ofNullable(findById(id));
	}

	/**
	 * Returns all the owners from data store with pagination
	 * @param page Page number (1-based)
	 * @param pageSize Page size
	 * @return a Page of {@link Owner}s
	 **/
	public io.quarkus.panache.common.Page<Owner> findAllPaginated(int page, int pageSize) {
		return findAll().page(Page.of(page - 1, pageSize));
	}

}
