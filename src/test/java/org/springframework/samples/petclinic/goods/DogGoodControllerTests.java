package org.springframework.samples.petclinic.goods;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for {@link DogGoodController}
 */
@WebMvcTest(DogGoodController.class)
class DogGoodControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogGoodRepository dogGoodRepository;

    @Test
    void testGetAllDogGoods() throws Exception {
        List<DogGood> dogGoods = new ArrayList<>();
        DogGood dogGood = new DogGood();
        dogGood.setId(1);
        dogGood.setName("Premium Dog Food");
        dogGood.setDescription("High-quality dog food for all breeds");
        dogGood.setPrice(29.99);
        dogGood.setCategory("Food");
        dogGood.setStockQuantity(100);
        dogGoods.add(dogGood);

        Page<DogGood> dogGoodsPage = new PageImpl<>(dogGoods, PageRequest.of(0, 10), 1);
        given(this.dogGoodRepository.findAll(PageRequest.of(0, 10))).willReturn(dogGoodsPage);

        mockMvc.perform(get("/api/dog-goods")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Premium Dog Food"))
                .andExpect(jsonPath("$.content[0].description").value("High-quality dog food for all breeds"))
                .andExpect(jsonPath("$.content[0].price").value(29.99))
                .andExpect(jsonPath("$.content[0].category").value("Food"))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(100));
    }

    @Test
    void testGetDogGood() throws Exception {
        DogGood dogGood = new DogGood();
        dogGood.setId(1);
        dogGood.setName("Premium Dog Food");
        dogGood.setDescription("High-quality dog food for all breeds");
        dogGood.setPrice(29.99);
        dogGood.setCategory("Food");
        dogGood.setStockQuantity(100);

        given(this.dogGoodRepository.findById(1)).willReturn(Optional.of(dogGood));

        mockMvc.perform(get("/api/dog-goods/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Premium Dog Food"))
                .andExpect(jsonPath("$.description").value("High-quality dog food for all breeds"))
                .andExpect(jsonPath("$.price").value(29.99))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.stockQuantity").value(100));
    }

    @Test
    void testGetDogGoodsByCategory() throws Exception {
        List<DogGood> dogGoods = new ArrayList<>();
        DogGood dogGood = new DogGood();
        dogGood.setId(1);
        dogGood.setName("Premium Dog Food");
        dogGood.setDescription("High-quality dog food for all breeds");
        dogGood.setPrice(29.99);
        dogGood.setCategory("Food");
        dogGood.setStockQuantity(100);
        dogGoods.add(dogGood);

        given(this.dogGoodRepository.findByCategory("Food")).willReturn(dogGoods);

        mockMvc.perform(get("/api/dog-goods/category/Food")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Premium Dog Food"))
                .andExpect(jsonPath("$[0].description").value("High-quality dog food for all breeds"))
                .andExpect(jsonPath("$[0].price").value(29.99))
                .andExpect(jsonPath("$[0].category").value("Food"))
                .andExpect(jsonPath("$[0].stockQuantity").value(100));
    }

    @Test
    void testSearchDogGoods() throws Exception {
        List<DogGood> dogGoods = new ArrayList<>();
        DogGood dogGood = new DogGood();
        dogGood.setId(1);
        dogGood.setName("Premium Dog Food");
        dogGood.setDescription("High-quality dog food for all breeds");
        dogGood.setPrice(29.99);
        dogGood.setCategory("Food");
        dogGood.setStockQuantity(100);
        dogGoods.add(dogGood);

        given(this.dogGoodRepository.findByNameContaining("Food")).willReturn(dogGoods);

        mockMvc.perform(get("/api/dog-goods/search?name=Food")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Premium Dog Food"))
                .andExpect(jsonPath("$[0].description").value("High-quality dog food for all breeds"))
                .andExpect(jsonPath("$[0].price").value(29.99))
                .andExpect(jsonPath("$[0].category").value("Food"))
                .andExpect(jsonPath("$[0].stockQuantity").value(100));
    }
}
