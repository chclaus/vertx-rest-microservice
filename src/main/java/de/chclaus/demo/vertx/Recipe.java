package de.chclaus.demo.vertx;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chclaus (ch.claus@me.com)
 */
public class Recipe {

  private String prepTime;
  private Map<String, String> ingredients;
  private String text;

  public Recipe() {
    ingredients = new LinkedHashMap<>();
  }

  @Override
  public String toString() {
    return "Recipe{" +
        "prepTime='" + prepTime + '\'' +
        ", ingredients=" + ingredients +
        ", text='" + text + '\'' +
        '}';
  }

  public String getPrepTime() {
    return prepTime;
  }

  public Recipe setPrepTime(String prepTime) {
    this.prepTime = prepTime;
    return this;
  }

  public Map<String, String> getIngredients() {
    return ingredients;
  }

  public Recipe setIngredients(Map<String, String> ingredients) {
    this.ingredients = ingredients;
    return this;
  }

  public String getText() {
    return text;
  }

  public Recipe setText(String text) {
    this.text = text;
    return this;
  }
}
