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
package org.springframework.samples.petclinic.vet;

import io.quarkus.cache.CacheResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Repository class for <code>Vet</code> domain objects using Quarkus Panache.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
@ApplicationScoped
public class VetRepository implements PanacheRepository<Vet> {

	/**
	 * Retrieve all <code>Vet</code>s from the data store.
	 * @return a <code>List</code> of <code>Vet</code>s
	 */
	@Transactional
	@CacheResult(cacheName = "vets")
	public List<Vet> findAllVets() {
		return findAll(Sort.by("lastName")).list();
	}

	/**
	 * Retrieve all <code>Vet</code>s from data store in Pages
	 * @param page Page number (1-based)
	 * @param pageSize Page size
	 * @return a Page of <code>Vet</code>s
	 */
	@Transactional
	@CacheResult(cacheName = "vets")
	public io.quarkus.panache.common.Page<Vet> findAllPaginated(int page, int pageSize) {
		return findAll(Sort.by("lastName")).page(Page.of(page - 1, pageSize));
	}

}
