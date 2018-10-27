package rest.controller;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.post;

import exception.TransferOperationException;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.TransferService;
import spark.RouteGroup;

public class TransferController {

  private static final Logger log = LoggerFactory.getLogger(TransferController.class);

  private TransferService transferService;

  private TransferController() {
    transferService = TransferService.getInstance();
  }

  public static TransferController getInstance() {
    return new TransferController();
  }

  public RouteGroup configEndpoints() {
    return () -> {
      before((request, response) -> request.body());
      exception(
          TransferOperationException.class,
          (e, request, response) -> {
            log.error("Error during operation!", e);
            response.status(400);
            response.body(e.getMessage());
          });
      post(
          "/",
          "application/json",
          (request, response) -> {
            transferService.transfer(1, BigDecimal.TEN, 2);
            response.status(200);
            return response;
          });
    };
  }
}
