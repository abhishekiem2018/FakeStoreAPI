package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class CartCRUDTest extends BaseTest {

// Using hardcoded cartId because FakeStoreAPI is a mock API
// and does not persist dynamically created cart resources.
// Runtime-created IDs from POST response return null in subsequent GET requests.
    static int cartId = 2  ;

    /*
     CREATE CART
     */

    @Test(priority = 1)
    public void createCartTest() {

        String payload = """
                {
                  "userId": 456,
                  "products": [
                    {
                      "id": 2,
                      "title": "Bag",
                      "price": 100
                    }
                  ]
                }
                """;
        System.out.println(AuthTest.token);
        Response response = given()
                .header("Authorization", "Bearer " + AuthTest.token)
                .contentType("application/json")
                .body(payload)

                .when()
                .post("/carts");

        // Print formatted response
        System.out.println("Post Request Status Code: " + response.statusCode());
        System.out.println("Post Request Response Body:");
        System.out.println(response.asPrettyString());

        response.then()
                .statusCode(anyOf(is(200), is(201)));

        int cartId = response.jsonPath().getInt("id");
        System.out.println("cartId:" + cartId);
        Assert.assertTrue(cartId > 0);
    }

    /*
     GET CREATED CART
     */

    @Test(priority = 2)
    public void getCartTest() {
        Response response = given()
                .when()
                .get("/carts/" + cartId);

        System.out.println("Get Request Status Code: " + response.statusCode());
        System.out.println("Get Request Response:");
        response.prettyPrint();

        response.then()
                .statusCode(200);

        int actualId = response.jsonPath().getInt("id");

        Assert.assertEquals(actualId, cartId);
    }

    /*
     UPDATE CART
     */

    @Test(priority = 3)
    public void updateCartTest() {

        String updatedPayload = """
                {
                  "userId": 2,
                  "products": [
                    {
                      "id": 2,
                      "title": "Phone",
                      "price": 20000
                    }
                  ]
                }
                """;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(updatedPayload)
                .when()
                .put("/carts/" + cartId);

        response.then()
                .log().all()
                .statusCode(200)
                .body("userId", equalTo(2));
    }

    /*
     DELETE CART
     */

    @Test(priority = 4)
    public void deleteCartTest() {
        Response response = given()
                .log().all()
                .when()
                .delete("/carts/" + cartId);

        response.then()
                .log().all()
                .statusCode(200);
    }

    /*
     NEGATIVE TEST
     */

    @Test(priority = 5)
    public void invalidCartTest() {

        given()

                .when()
                .get("/carts/99999")

                .then()
                .statusCode(404);
    }

    /*
     DATA DRIVEN TEST
     */

    @DataProvider(name = "productData")
    public Object[][] getData() {

        return new Object[][]{
                {1},
                {2},
                {3}
        };
    }

    @Test(dataProvider = "productData", priority = 6)
    public void getProductsDataDriven(int productId) {

        given()

                .when()
                .get("/products/" + productId)

                .then()
                .statusCode(200)
                .body("id", equalTo(productId));
    }

    /*
     SCHEMA VALIDATION
     */

    @Test(priority = 7)
    public void schemaValidationTest() {

        given()

                .when()
                .get("/carts/1")

                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/cart-schema.json"));
    }

}