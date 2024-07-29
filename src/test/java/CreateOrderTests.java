import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Data;
import ru.yandex.praktikum.Ingredient;
import ru.yandex.praktikum.Order;
import ru.yandex.praktikum.User;
import steps.StepsForIngredient;
import steps.StepsForOrder;
import steps.StepsForUser;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTests {
    StepsForOrder stepsForOrder = new StepsForOrder();
    User user = new User("aa3@yandex.ru", "50313864", "Саша");
    StepsForUser stepsForUser = new StepsForUser();
    private Order order;
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
        order = new Order(ingredients);

    }

    @Test
    @DisplayName("Создание заказ авторизованным пользователем")
    @Description("Позитивная проверка статус кода и тела ответа при создании заказа авторизованным пользователем")
    public void createOrderAuthUserCheck() {

        stepsForOrder.createOrder(order, accessToken)
                .then().assertThat().body("success", is(true))
                .and()
                .statusCode(SC_OK);

    }

    @Test
    @DisplayName("Создание заказ неавторизованным пользователем")
    @Description("Позитивная проверка статус кода и тела ответа при создании заказа неавторизованным пользователем")
    public void createOrderNoAuthUserCheck() {

        stepsForOrder.createOrder(order, "")
                .then().assertThat().body("success", is(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказ авторизованным пользователем без ингредиентов")
    @Description("Негативная проверка статус кода и тела ответа при создании заказа авторизованным пользователем без ингредиентов")
    public void createOrderAuthUserWithoutIngredientsCheck() {
        ingredients.clear();
        stepsForOrder.createOrder(order, accessToken)
                .then().assertThat().body("message", is("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказ авторизованным пользователем с неправильным хеш-кодом ингредиента")
    @Description("Негативная проверка статус кода и тела ответа при создании заказа авторизованным пользователем с неправильным хеш-кодом ингредиента")
    public void createOrderAuthUserWithWrongHashIngredientsCheck() {
        ingredients.add("abcde");
        stepsForOrder.createOrder(order, accessToken)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            stepsForUser.deleteUser(accessToken);
        }
    }


}
