package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

/**
 * Redis implementation of the {@link VetRepository} interface.
 */
@Repository
public class RedisVetRepository implements VetRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String VET_KEY = "vets";

    public RedisVetRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("vets")
    public Collection<Vet> findAll() throws DataAccessException {
        return redisTemplate.opsForHash().values(VET_KEY).stream()
            .filter(obj -> obj instanceof Vet)
            .map(obj -> (Vet) obj)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("vets")
    public Page<Vet> findAll(Pageable pageable) throws DataAccessException {
        List<Vet> vets = new ArrayList<>(findAll());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), vets.size());

        return new PageImpl<>(
            start >= vets.size() ? Collections.emptyList() : vets.subList(start, end),
            pageable,
            vets.size()
        );
    }

    /**
     * Save a vet to Redis
     * @param vet the vet to save
     * @return the saved vet
     */
    public Vet save(Vet vet) {
        if (vet.isNew()) {
            vet.setId(getNextId());
        }

        redisTemplate.opsForHash().put(VET_KEY, vet.getId().toString(), vet);
        return vet;
    }

    /**
     * Find a vet by ID
     * @param id the ID to search for
     * @return the vet if found, null otherwise
     */
    public Vet findById(Integer id) {
        Object vet = redisTemplate.opsForHash().get(VET_KEY, id.toString());
        return (vet instanceof Vet) ? (Vet) vet : null;
    }

    /**
     * Delete a vet by ID
     * @param id the ID of the vet to delete
     */
    public void deleteById(Integer id) {
        redisTemplate.opsForHash().delete(VET_KEY, id.toString());
    }

    private Integer getNextId() {
        Long nextId = redisTemplate.opsForValue().increment("vet_id_sequence");
        return nextId != null ? nextId.intValue() : 1;
    }
}
