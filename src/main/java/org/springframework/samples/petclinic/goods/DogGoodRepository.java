package org.springframework.samples.petclinic.goods;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository class for <code>DogGood</code> domain objects
 */
public interface DogGoodRepository extends JpaRepository<DogGood, Integer> {

    /**
     * Retrieve all {@link DogGood}s from the data store.
     * @return a Collection of {@link DogGood}s.
     */
    List<DogGood> findAll();

    /**
     * Retrieve all {@link DogGood}s from the data store with pagination.
     * @param pageable pagination information
     * @return a Page of {@link DogGood}s.
     */
    Page<DogGood> findAll(Pageable pageable);

    /**
     * Retrieve {@link DogGood}s from the data store by category.
     * @param category to search for
     * @return a Collection of matching {@link DogGood}s
     */
    List<DogGood> findByCategory(String category);

    /**
     * Retrieve {@link DogGood}s from the data store by name containing the given string.
     * @param name Value to search for
     * @return a Collection of matching {@link DogGood}s
     */
    List<DogGood> findByNameContaining(String name);

    /**
     * Retrieve a {@link DogGood} from the data store by id.
     * @param id the id to search for
     * @return the {@link DogGood} if found
     */
    Optional<DogGood> findById(Integer id);
}
