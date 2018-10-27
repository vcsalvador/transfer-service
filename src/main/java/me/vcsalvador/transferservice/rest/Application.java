package me.vcsalvador.transferservice.rest;

import static spark.Spark.path;

import me.vcsalvador.transferservice.rest.controller.BankingController;
import me.vcsalvador.transferservice.rest.controller.TransferController;

public class Application {

  public static void main(String[] args) {
    routes();
  }

  private static void routes() {
    BankingController bankingController = BankingController.getInstance();
    TransferController transferController = TransferController.getInstance();

    path("/banking", bankingController.configEndpoints());

    path("/transfer", transferController.configEndpoints());
  }
}
