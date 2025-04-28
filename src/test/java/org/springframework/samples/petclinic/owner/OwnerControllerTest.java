package org.springframework.samples.petclinic.owner;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class OwnerControllerTest {

    @Test
    public void testFindOwnersPage() {
        given()
            .when().get("/owners/find")
            .then()
            .statusCode(200)
            .contentType(ContentType.HTML)
            .body(containsString("Find Owners"));
    }

    @Test
    public void testGetOwnerById() {
        // Owner with ID 1 is "George Franklin" in sample data
        given()
            .when().get("/owners/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.HTML)
            .body(
                containsString("George Franklin"),
                containsString("110 W. Liberty St.")
            );
    }

    @Test
    public void testFindOwnersByLastName() {
        given()
            .when().get("/owners?lastName=Davis")
            .then()
            .statusCode(200)
            .contentType(ContentType.HTML)
            .body(containsString("Betty Davis"));
    }
}
