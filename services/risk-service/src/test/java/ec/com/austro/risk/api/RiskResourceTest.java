package ec.com.austro.risk.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class RiskResourceTest {

    @Test
    void shouldExposeScoreEndpoint() {
        given()
                .when().get("/v1/risks/1710034065/score")
                .then()
                .statusCode(200)
                .body("score", greaterThanOrEqualTo(0))
                .body("score", lessThanOrEqualTo(100));
    }

    @Test
    void shouldExposeDebtsEndpoint() {
        given()
                .when().get("/v1/risks/1710034065/debts")
                .then()
                .statusCode(200)
                .body("monthlyPayment", everyItem(greaterThanOrEqualTo(0.0f)));
    }
}
