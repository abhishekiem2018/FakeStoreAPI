package tests;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AuthTest extends BaseTest {

    public static String token;

    @Test(priority = 1)
    public void loginTest() {

        String payload = """
                {
                  "username": "mor_2314",
                  "password": "83r5^_"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(payload)

                .when()
                .post("/auth/login");

        response.then().statusCode(201);

        token = response.jsonPath().getString("token");
        System.out.println(token);
        Assert.assertNotNull(token);
    }

    @Test(priority = 2)
    public void invalidLoginTest() {

        String payload = """
                {
                  "username": "wrong",
                  "password": "wrong"
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)

                .when()
                .post("/auth/login")

                .then()
                .statusCode(401);
    }
}