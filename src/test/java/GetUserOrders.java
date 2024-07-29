import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Ingredient;
import ru.yandex.praktikum.User;
import steps.StepsForIngredient;
import steps.StepsForOrder;
import steps.StepsForUser;

import static org.apache.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class GetUserOrders {

    StepsForOrder stepsForOrder = new StepsForOrder();
    User user = new User("aa3@yandex.ru", "50313864", "Саша");
    StepsForUser stepsForUser = new StepsForUser();
    private Ingredient ingredientList;
    StepsForIngredient stepsForIngredient = new StepsForIngredient();
    private String accessToken;
    private List<String> ingredients;
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        ingredientList = stepsForIngredient.getIngredient();
        ingredients = new ArrayList<>();
        ingredients.add(ingredientList.getData().get(1).get_id());
        ingredients.add(ingredientList.getData().get(2).get_id());
        ingredients.add(ingredientList.getData().get(3).get_id());
        stepsForUser.setUser(user);
        stepsForUser.createUser();
        accessToken = stepsForUser.loginUser(user).then().extract().path("accessToken");

    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    @Description("Проверка статус кода и тела ответа при получении списка заказов авторизованным пользователем")
    public void getOrdersAuthUserCheck() {
        stepsForOrder.getOrder(accessToken)
                .then().assertThat().body("success", is(true))
                .and()
                .statusCode(SC_OK);

    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    @Description("Проверка статус кода и тела ответа при получении списка заказов неавторизованным пользователем")
    public void getOrdersNoAuthUserCheck() {
        stepsForOrder.getOrder("")
                .then().assertThat().body("success", is(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);

    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            stepsForUser.deleteUser(accessToken);
        }
    }
}
