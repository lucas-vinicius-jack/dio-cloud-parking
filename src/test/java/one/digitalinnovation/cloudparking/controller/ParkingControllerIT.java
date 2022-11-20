package one.digitalinnovation.cloudparking.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import io.restassured.RestAssured;
import one.digitalinnovation.cloudparking.controller.dto.ParkingCreateDTO;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingControllerIT extends AbstractContainerBase {

    @LocalServerPort
    private int randomPort;

    @BeforeEach
    public void setUpTest(){
        RestAssured.port = randomPort;
    }

    @Test
    void whenFindAllThenCheckResult() {
        RestAssured.given()
                .header("Authorization:", "Basic dXNlcjp1c2VyQDEyMw==")
                .when()
                .get("/parking")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void whenCreateThenCheckIsCreated() {

        var createDTO   = new ParkingCreateDTO();
        createDTO.setColor("ROXO");
        createDTO.setLicense("WRT-5555");
        createDTO.setModel("FIAT");
        createDTO.setState("DF");

        RestAssured.given()
                .auth()
                .basic("user", "user@123")
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createDTO)
                .post("/parking")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("license", Matchers.equalTo("WRT-5555"))
                .body("color", Matchers.equalTo("ROXO"));
    }
}