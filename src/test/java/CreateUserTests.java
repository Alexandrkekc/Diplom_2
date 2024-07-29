import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.User;
import steps.StepsForUser;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateUserTests {

    User user = new User("aa5@yandex.ru", "50313864", "Саша");
    StepsForUser stepsForUser = new StepsForUser();
    private String accessToken;

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Позитивная проверка статус кода и тела ответа при создании пользователя")
    public void createUserCheck() {
        stepsForUser.setUser(user);
        stepsForUser.createUser()
                .then().assertThat().body("success", is(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка статус кода и тела ответа при создании дубликата пользователя")
    public void createDuplicateUserCheck() {
        stepsForUser.setUser(user);
        stepsForUser.createUser();
        stepsForUser.createUser()
                .then().assertThat().body("success", is(false))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без указания эмейла")
    @Description("Проверка статус кода и тела ответа при создании пользователя без указания эмейла")
    public void createUserWithoutEmailCheck() {
        stepsForUser.setUser(new User("", "50313864", "Саша"));
        stepsForUser.createUser();
        stepsForUser.createUser()
                .then().assertThat().body("message", is("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без указания пароля")
    @Description("Проверка статус кода и тела ответа при создании пользователя без указания пароля")
    public void createUserWithoutPasswordCheck() {
        stepsForUser.setUser(new User("aa@yandex.ru", "", "Саша"));
        stepsForUser.createUser();
        stepsForUser.createUser()
                .then().assertThat().body("message", is("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    @Description("Проверка статус кода и тела ответа при создании пользователя без указания имени")
    public void createUserWithoutNameCheck() {
        stepsForUser.setUser(new User("aa@yandex.ru", "50313864", ""));
        stepsForUser.createUser();
        stepsForUser.createUser()
                .then().assertThat().body("message", is("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @After
    public void deleteUser(){
        accessToken = stepsForUser.loginUser(user).then().extract().path("accessToken");
        if (accessToken!=null) {
            stepsForUser.deleteUser(accessToken);
        }
    }
}