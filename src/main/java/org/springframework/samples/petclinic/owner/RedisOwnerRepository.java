package org.springframework.samples.petclinic.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Redis implementation of the {@link OwnerRepository} interface.
 */
@Repository
public class RedisOwnerRepository implements OwnerRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String OWNER_KEY = "owners";
    private static final String PET_TYPE_KEY = "pet_types";

    public RedisOwnerRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<PetType> findPetTypes() {
        Set<Object> petTypes = redisTemplate.opsForSet().members(PET_TYPE_KEY);
        if (petTypes == null) {
            return Collections.emptyList();
        }
        return petTypes.stream()
            .filter(obj -> obj instanceof PetType)
            .map(obj -> (PetType) obj)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Owner> findByLastNameStartingWith(String lastName, Pageable pageable) {
        List<Owner> owners = findAll();
        List<Owner> filtered = owners.stream()
            .filter(owner -> owner.getLastName().toLowerCase().startsWith(lastName.toLowerCase()))
            .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());

        return new PageImpl<>(
            start >= filtered.size() ? Collections.emptyList() : filtered.subList(start, end),
            pageable,
            filtered.size()
        );
    }

    @Override
    public Optional<Owner> findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        List<Owner> owners = findAll();
        return owners.stream()
            .filter(owner -> id.equals(owner.getId()))
            .findFirst();
    }

    @Override
    public Page<Owner> findAll(Pageable pageable) {
        List<Owner> owners = findAll();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), owners.size());

        return new PageImpl<>(
            start >= owners.size() ? Collections.emptyList() : owners.subList(start, end),
            pageable,
            owners.size()
        );
    }

    @Override
    public <S extends Owner> S save(S owner) {
        if (owner.isNew()) {
            owner.setId(getNextId());
        }

        redisTemplate.opsForHash().put(OWNER_KEY, owner.getId().toString(), owner);
        return owner;
    }

    @Override
    public <S extends Owner> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        redisTemplate.opsForHash().delete(OWNER_KEY, id.toString());
    }

    @Override
    public void delete(Owner entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Owner> entities) {
        for (Owner entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        redisTemplate.delete(OWNER_KEY);
    }

    @Override
    public long count() {
        return redisTemplate.opsForHash().size(OWNER_KEY);
    }

    @Override
    public boolean existsById(Integer id) {
        return redisTemplate.opsForHash().hasKey(OWNER_KEY, id.toString());
    }

    @Override
    public List<Owner> findAll() {
        return redisTemplate.opsForHash().values(OWNER_KEY).stream()
            .filter(obj -> obj instanceof Owner)
            .map(obj -> (Owner) obj)
            .collect(Collectors.toList());
    }

    @Override
    public List<Owner> findAllById(Iterable<Integer> ids) {
        List<Owner> result = new ArrayList<>();
        for (Integer id : ids) {
            findById(id).ifPresent(result::add);
        }
        return result;
    }

    private Integer getNextId() {
        Long nextId = redisTemplate.opsForValue().increment("owner_id_sequence");
        return nextId != null ? nextId.intValue() : 1;
    }
}
