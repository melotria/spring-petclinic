package org.springframework.samples.petclinic.goods;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST Controller for managing dog goods.
 */
@RestController
@RequestMapping("/api/dog-goods")
public class DogGoodController {

    private final DogGoodRepository dogGoodRepository;

    public DogGoodController(DogGoodRepository dogGoodRepository) {
        this.dogGoodRepository = dogGoodRepository;
    }

    /**
     * GET /api/dog-goods : Get all dog goods.
     *
     * @param page the pagination information
     * @param size the pagination size
     * @return the ResponseEntity with status 200 (OK) and the list of dog goods in body
     */
    @GetMapping
    public ResponseEntity<Page<DogGood>> getAllDogGoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DogGood> result = dogGoodRepository.findAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET /api/dog-goods/:id : Get the "id" dog good.
     *
     * @param id the id of the dog good to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dog good, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<DogGood> getDogGood(@PathVariable Integer id) {
        Optional<DogGood> dogGood = dogGoodRepository.findById(id);
        return dogGood.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /api/dog-goods/category/:category : Get dog goods by category.
     *
     * @param category the category of the dog goods to retrieve
     * @return the ResponseEntity with status 200 (OK) and the list of dog goods in body
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<DogGood>> getDogGoodsByCategory(@PathVariable String category) {
        List<DogGood> dogGoods = dogGoodRepository.findByCategory(category);
        return new ResponseEntity<>(dogGoods, HttpStatus.OK);
    }

    /**
     * GET /api/dog-goods/search : Search dog goods by name.
     *
     * @param name the name to search for
     * @return the ResponseEntity with status 200 (OK) and the list of dog goods in body
     */
    @GetMapping("/search")
    public ResponseEntity<List<DogGood>> searchDogGoods(@RequestParam String name) {
        List<DogGood> dogGoods = dogGoodRepository.findByNameContaining(name);
        return new ResponseEntity<>(dogGoods, HttpStatus.OK);
    }

    /**
     * POST /api/dog-goods : Create a new dog good.
     *
     * @param dogGood the dog good to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dog good
     */
    @PostMapping
    public ResponseEntity<DogGood> createDogGood(@Valid @RequestBody DogGood dogGood) {
        if (dogGood.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DogGood result = dogGoodRepository.save(dogGood);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * PUT /api/dog-goods/:id : Updates an existing dog good.
     *
     * @param id the id of the dog good to update
     * @param dogGood the dog good to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dog good,
     * or with status 400 (Bad Request) if the dog good is not valid,
     * or with status 500 (Internal Server Error) if the dog good couldn't be updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<DogGood> updateDogGood(
            @PathVariable Integer id,
            @Valid @RequestBody DogGood dogGood) {
        if (dogGood.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!id.equals(dogGood.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!dogGoodRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DogGood result = dogGoodRepository.save(dogGood);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * DELETE /api/dog-goods/:id : Delete the "id" dog good.
     *
     * @param id the id of the dog good to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDogGood(@PathVariable Integer id) {
        if (!dogGoodRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dogGoodRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
