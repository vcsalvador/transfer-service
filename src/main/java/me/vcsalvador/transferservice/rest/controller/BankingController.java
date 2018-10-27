package me.vcsalvador.transferservice.rest.controller;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import me.vcsalvador.transferservice.exception.AccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.vcsalvador.transferservice.service.BankingService;
import spark.RouteGroup;

public class BankingController {

  private static final Logger log = LoggerFactory.getLogger(BankingController.class);

  private static BankingController instance;

  private BankingService bankingService;

  private BankingController() {
    bankingService = BankingService.getInstance();
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
      before((request, response) -> request.body());
      exception(
          AccountNotFoundException.class,
          (e, request, response) -> {
            log.error("Error finding account!", e);
            response.status(404);
            response.body(e.getMessage());
          });
      get(
          "/:account",
          (request, response) -> bankingService.getAccountInfo(request.attribute("account")));
      get("/", ((request, response) -> bankingService.getAllAccountsInfo()));
      post(
          "/",
          ((request, response) ->
              bankingService.createAccount(request.attribute("id"), request.attribute("balance"))));
      delete(
          "/",
          ((request, response) -> {
            bankingService.deleteAll();
            response.status(204);
            return response;
          }));
    };
  }
}
