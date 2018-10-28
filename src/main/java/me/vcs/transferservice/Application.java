package me.vcs.transferservice;

import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.path;

import me.vcs.transferservice.rest.controller.BankingController;
import me.vcs.transferservice.rest.controller.TransferController;

public class Application {

  public static void main(String[] args) {
    routes();
  }

  private static void routes() {
    BankingController bankingController = BankingController.getInstance();
    TransferController transferController = TransferController.getInstance();

    after((request, response) -> response.type("application/json"));

    path("/banking", bankingController.configEndpoints());
    path("/transfer", transferController.configEndpoints());

    awaitInitialization();
  }
}
