package me.vcs.transferservice.rest.controller;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import me.vcs.transferservice.exception.AccountNotFoundException;
import me.vcs.transferservice.model.AccountInfo;
import me.vcs.transferservice.service.BankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.RouteGroup;

public class BankingController {

  private static final Logger log = LoggerFactory.getLogger(BankingController.class);

  private static BankingController instance;

  private BankingService bankingService;

  private Gson gson;

  private BankingController() {
    bankingService = BankingService.getInstance();
    gson = new Gson();
  }

  /**
   * Singleton synchronized instance of {@code BankingController}
   *
   * @return singleton {@code BankingController.instance}
   */
  public static synchronized BankingController getInstance() {
    if (instance == null) {
      instance = new BankingController();
    }
    return instance;
  }

  public RouteGroup configEndpoints() {
    return () -> {
      exception(
          AccountNotFoundException.class,
          (e, request, response) -> {
            log.error("Error finding account!", e);
            response.status(404);
            response.body(gson.toJson(e.getMessage()));
          });
      get("/", "*/*", ((request, response) -> bankingService.getAllAccountsInfo()), gson::toJson);
      get(
          "/:account",
          "*/*",
          (request, response) -> bankingService.getAccountInfo(request.attribute("account")),
          gson::toJson);
      post(
          "/",
          "application/json",
          (request, response) -> {
            AccountInfo accountInfo = gson.fromJson(request.body(), AccountInfo.class);
            bankingService.createAccount(accountInfo);
            response.status(200);
            return response.body();
          },
          gson::toJson);
      delete(
          "/",
          "*/*",
          (request, response) -> {
            bankingService.deleteAll();
            response.status(204);
            return response.body();
          },
          gson::toJson);
    };
  }
}
