package me.vcs.transferservice.rest.controller;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import me.vcs.transferservice.Application;
import me.vcs.transferservice.model.AccountInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import spark.Spark;

@DisplayName("Transfer Controller Test")
class TransferControllerTest {

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
  @DisplayName("When transfer is requested")
  class WhenTransferRequest {

    @BeforeEach
    void initializeBankingAccounts() {
      createAccounts(
          Set.of(
              new AccountInfo(1, BigDecimal.valueOf(100.00)),
              new AccountInfo(2, BigDecimal.valueOf(200.00)),
              new AccountInfo(3, BigDecimal.valueOf(300.00)),
              new AccountInfo(4, BigDecimal.valueOf(400.00)),
              new AccountInfo(5, BigDecimal.valueOf(500.00))));
    }

    @AfterEach
    void cleanUpBankingAccounts() {
      delete("/banking/");
    }

    @Test
    @DisplayName("Should execute transfer")
    void shouldExecuteTransfer() {
      when().
          post("/transfer/{amount}/from/{origin}/to/{destination}", 100.00, 2, 1).
      then().
          log().ifValidationFails().
          statusCode(200);

      when().
          get("/banking/").
      then().
          log().ifValidationFails(LogDetail.BODY, true).
          statusCode(200).
          body("find { it.accountId == 1 }.balance", is(200.00f));
    }

    @Test
    @DisplayName("Should return 400 for invalid transfer")
    void shouldThrowTransferException() {
      when().
          post("/transfer/{amount}/from/{origin}/to/{destination}", 200.00, 1, 2).
      then().
          log().ifValidationFails(LogDetail.BODY, true).
          statusCode(400);
    }

    @Test
    @DisplayName("Should work with multiple requests")
    void shouldGuaranteeStateWithMultipleParallelRequests() throws InterruptedException {
      ExecutorService executorService = Executors.newFixedThreadPool(8);

      List.of(
          createTransferThread(100.00, 2, 1, 200),
          createTransferThread(100.00, 2, 3, 200),
          createTransferThread(100.00, 2, 4, 400),
          createTransferThread(100.00, 3, 5, 200),
          createTransferThread(500.00, 1, 5, 400),
          createTransferThread(100.00, 1, 6, 400))
      .forEach(executorService::execute);

      executorService.awaitTermination(10, TimeUnit.SECONDS);

      when().
          get("/banking/").
      then().
          log().body(true).
          statusCode(200).
          body("find { it.accountId == 1 }.balance", is(200.00f)).
          body("find { it.accountId == 2 }.balance", is(0.00f)).
          body("find { it.accountId == 3 }.balance", is(300.00f)).
          body("find { it.accountId == 4 }.balance", is(400.00f)).
          body("find { it.accountId == 5 }.balance", is(600.00f));
    }

    private Runnable createTransferThread(
        double amount, int origin, int destination, int expected) {
      return () ->
          when()
              .post(
                  "/transfer/{amount}/from/{origin}/to/{destination}", amount, origin, destination)
              .then()
              .statusCode(expected);
    }

    private void createAccounts(Set<AccountInfo> accounts) {
      accounts.forEach(accountInfo -> given().body(accountInfo).post("/banking/"));
    }
  }
}
