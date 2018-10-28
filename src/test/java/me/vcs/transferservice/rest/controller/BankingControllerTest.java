package me.vcs.transferservice.rest.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import me.vcs.transferservice.Application;
import me.vcs.transferservice.model.AccountInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import spark.Spark;

@DisplayName("Banking Controller Tests")
class BankingControllerTest {

  @BeforeAll
  static void initServer() {
    Application.main(null);
    RestAssured.port = 4567;
  }

  @AfterAll
  static void tearDownServer() {
    Spark.awaitStop();
  }

  @Nested
  @DisplayName("When banking request")
  class WhenBankingRequested {
    @Test
    @DisplayName("Should retrieve empty set")
    void shouldRetrieveEmptySet() {
      when().
          get("/banking/").
      then().
          log().ifValidationFails().
          statusCode(200).
          body(is("[]"));
    }

    @Test
    @DisplayName("Should create account info")
    void shouldRetrieveAccounts() {
      AccountInfo accountInfo = new AccountInfo(1, BigDecimal.valueOf(1000.00));

      given().
        body(accountInfo).
      when().
          post("/banking/").
      then().
          log().ifValidationFails().
          statusCode(200);

      when().
          get("/banking/").
      then().
          log().ifValidationFails().
          statusCode(200).
          body("accountId", contains(1));
    }

    @Test
    @DisplayName("Should clean up banking accounts")
    void shouldDeleteAllAccounts() {
      when().
          delete("/banking/").
      then().
          log().ifValidationFails().
          statusCode(204);

      when().
          get("/banking/").
      then().
          log().ifValidationFails().
          statusCode(200).
          body(is("[]"));
    }
  }

}