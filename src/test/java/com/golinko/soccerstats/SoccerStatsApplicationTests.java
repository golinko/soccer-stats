package com.golinko.soccerstats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golinko.soccerstats.exceptions.SoccerStatsException;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("apitest")
@SpringBootTest(
        classes = SoccerStatsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = SoccerStatsApplicationTestConfig.class, loader = SpringBootContextLoader.class)
@RequiredArgsConstructor
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SoccerStatsApplicationTests {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setUp() throws SoccerStatsException {
        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/soccer-stats";
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> mapper));
        RestAssured.registerParser(ContentType.JSON.toString(), Parser.JSON);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.given().contentType(ContentType.JSON);
    }

    @Test
    public void getMatches_returnsMatchDtoList() {
        given()
                .log().all()
                .when()
                .get("/match")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .and()
                .body("matchId", hasItems(2174498, 2174497))
                .and()
                .body("matchDate", hasItems("19-Aug-2017 18:30", "13-Aug-2017 16:45"));
    }

    @Test
    public void getMatchTables_returnsMatchTablesList() {
        given()
                .log().all()
                .when()
                .get("/match/table")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("match.matchId", hasItems(2174498, 2174497))
                .body("home.team.teamId", hasItems(6, 15))
                .body("home.score", hasItems(3, 1))
                .body("away.team.teamId", hasItems(7, 74))
                .body("away.score", hasItems(3, 2));
    }

    @Test
    public void getMatchTable_returnsMatchTableById() {
        given()
                .log().all()
                .when()
                .get("/match/table/2174497")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("match.matchId", equalTo(2174497))
                .body("home.team.teamId", equalTo(15))
                .body("home.score", equalTo(1))
                .body("away.team.teamId", equalTo(74))
                .body("away.score", equalTo(2));
    }

    @Test
    public void getMatchTable_returnsNotFoundByNotExistingMatchId() {
        given()
                .log().all()
                .when()
                .get("/match/table/11111")
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getMatchInfo_returnsInfoAboutMatchPersonsByMatchId() {
        given()
                .log().all()
                .when()
                .get("/match/2174498")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("match.matchId", equalTo(2174498))
                .body("away", hasSize(21))
                .body("home", hasSize(22))
                .body("other", hasSize(4))
                .body("away.findAll {it.personFunction == 'Coach'}.person.personName", allOf(iterableWithSize(1), hasItem("Jurgen Streppel")))
                .body("away.findAll {it.personFunction.contains('Goalkeeper')}.squadNumber", allOf(iterableWithSize(2), hasItems(1, 23)))
                .body("home.findAll {it.personFunction == 'Coach'}.person.personName", allOf(iterableWithSize(1), hasItem("Ernest Faber")))
                .body("home.findAll {it.personFunction.contains('Goalkeeper')}.squadNumber", allOf(iterableWithSize(1), hasItem(1)))
                .body("other.findAll {it.personFunction == 'Referee'}.person.personName", allOf(iterableWithSize(1), hasItem("Pol van Boekel")));
    }

    @Test
    public void getMatchInfo_returnsNotFoundByNotExistingMatchId() {
        given()
                .log().all()
                .when()
                .get("/match/555555")
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getMatchInfo_methodArgumentsMismatch() {
        given()
                .log().all()
                .when()
                .get("/match/horse")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteMatch_methodNotAllowed() {
        given()
                .log().all()
                .when()
                .delete("/match")
                .then()
                .log().body()
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @Test
    public void postMatch_methodNotAllowed() {
        given()
                .log().all()
                .when()
                .post("/match")
                .then()
                .log().body()
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @Test
    public void putMatch_methodNotAllowed() {
        given()
                .log().all()
                .when()
                .put("/match")
                .then()
                .log().body()
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
    }
}
