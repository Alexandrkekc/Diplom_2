package steps;

import io.qameta.allure.Step;
import ru.yandex.praktikum.Ingredient;

import static io.restassured.RestAssured.given;

public class StepsForIngredient {

    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";

    @Step("Получение данных об ингредиентах")
    public Ingredient getIngredient() {
        return given()
                .header("Content-type", "application/json")
                .get(INGREDIENTS_ENDPOINT)
                .as(Ingredient.class);
    }
}
