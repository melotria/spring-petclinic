package org.springframework.samples.petclinic.goods;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * Simple JavaBean domain object representing a dog good/product.
 */
@Entity
@Table(name = "dog_goods")
public class DogGood extends NamedEntity {

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "price")
    @NotNull
    @Positive
    private Double price;

    @Column(name = "category")
    @NotBlank
    private String category;

    @Column(name = "stock_quantity")
    @NotNull
    @Positive
    private Integer stockQuantity;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
