package de.chclaus.demo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author chclaus (ch.claus@me.com)
 */
public class Microservice extends AbstractVerticle {

  @Override
  public void start(Future<Void> fut) throws Exception {
    Router mainRouter = Router.router(vertx);
    mainRouter.route().handler(BodyHandler.create());

    Router beerRouter = Router.router(vertx);
    beerRouter.route().handler(routingContext -> {
      routingContext.response().putHeader("content-type", "application/json; charset=utf-8");
      routingContext.next();
    });
    beerRouter.get("/:id").handler(this::getBeerById);

    mainRouter.mountSubRouter("/beer", beerRouter);

    vertx
        .createHttpServer()
        .requestHandler(mainRouter::accept)
        .listen(8080, result -> {
          if (result.succeeded()) {
            fut.complete();
          } else {
            fut.fail(result.cause());
          }
        });
  }

  private void getBeerById(RoutingContext routingContext) {
    routingContext.response().end("foo");
  }
}
