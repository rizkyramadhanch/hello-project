package id.co.alamisharia.hello_project;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MainVerticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/").handler(this::index);

    int serverPort = Integer.parseInt(Optional.ofNullable(System.getenv("SERVER_PORT")).orElse("8888"));
    server.requestHandler(router).listen(serverPort, ar -> {
      if (ar.succeeded()) {
        log.info("HTTP server running on port " + serverPort);
        startPromise.complete();
      } else {
        log.error("Could not start a HTTP server", ar.cause());
        startPromise.fail(ar.cause());
      }
    });
  }

  private void index(RoutingContext context) {
    log.info("index request: " + context.request().headers().toString().replace("\n", " "));

    JsonObject response = new JsonObject()
      .put("status", 200)
      .put("messages", "Hello from ALAMI (v1.0)")

    context.response().putHeader("Content-Type", "application/json");
    context.response().end(response.encodePrettily());
  }
}
