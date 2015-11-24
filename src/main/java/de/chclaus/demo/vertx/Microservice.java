package de.chclaus.demo.vertx;

import de.chclaus.demo.vertx.scraper.ChefkochScraper;
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
    vertx.deployVerticle(new ChefkochScraper());

    Router mainRouter = Router.router(vertx);
    mainRouter.route().handler(BodyHandler.create());

    Router recipe = Router.router(vertx);
    recipe.route().handler(routingContext -> {
      routingContext.response().putHeader("content-type", "application/json; charset=utf-8");
      routingContext.next();
    });
    recipe.get("/decode").handler(this::getRecipeByUrl);

    mainRouter.mountSubRouter("/receipe", recipe);

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

  private void getRecipeByUrl(RoutingContext routingContext) {
    String url = routingContext.request().getParam("url");

    // atm static urls...
    url = "/rezepte/1108021216887354/Flammkuchen-elsaesser-Art-suesser-Flammkuchen.html";
    vertx.eventBus().send("chefkoch", url, reply -> {
      if (reply.succeeded()) {
        routingContext.response().end(reply.result().body().toString());
      }
    });
  }
}
