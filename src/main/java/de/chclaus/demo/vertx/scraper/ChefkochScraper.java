package de.chclaus.demo.vertx.scraper;

import de.chclaus.demo.vertx.Recipe;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author chclaus (ch.claus@me.com)
 */
public class ChefkochScraper extends AbstractVerticle implements Scraper {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.eventBus().consumer("chefkoch", message -> {
      String url = (String) message.body();

      HttpClient httpClient = vertx.createHttpClient();
      httpClient.getNow("www.chefkoch.de", url, httpClientResponse -> {
        httpClientResponse.bodyHandler(buffer -> {
          String html = buffer.toString();
          Document document = Jsoup.parse(html);
          Recipe recipe = new Recipe();

          document.select(".incredients .ingredient .name").stream().forEach(ingredient -> {
            recipe.getIngredients().put(ingredient.text(), ingredient.previousElementSibling().text());
          });

          recipe.setPrepTime(document.select(".prepTime").text())
              .setText(document.select("#rezept-zubereitung").text());

          message.reply(recipe.toString());
        });
      });
    });










  }

  @Override
  public String scrape() {
    return null;
  }
}
