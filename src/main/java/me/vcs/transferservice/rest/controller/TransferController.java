package me.vcs.transferservice.rest.controller;

import static spark.Spark.exception;
import static spark.Spark.post;

import com.google.gson.Gson;
import java.math.BigDecimal;
import me.vcs.transferservice.exception.TransferOperationException;
import me.vcs.transferservice.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.RouteGroup;

public class TransferController {

  private static final Logger log = LoggerFactory.getLogger(TransferController.class);

  private TransferService transferService;
  private Gson gson;

  private TransferController() {
    transferService = TransferService.getInstance();
    gson = new Gson();
  }

  public static TransferController getInstance() {
    return new TransferController();
  }

  public RouteGroup configEndpoints() {
    return () -> {
      exception(
          TransferOperationException.class,
          (e, request, response) -> {
            log.error("Error during operation!", e);
            response.status(400);
            response.body(gson.toJson(e.getMessage()));
          });
      post(
          "/:amount/from/:origin/to/:destination",
          "application/json",
          (request, response) -> {
            transferService.transfer(
                Integer.valueOf(request.params(":origin")),
                new BigDecimal(request.params(":amount")),
                Integer.valueOf(request.params(":destination")));
            response.status(200);
            return response.body();
          },
          gson::toJson);
    };
  }
}
