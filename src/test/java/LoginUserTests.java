import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.User;
import steps.StepsForUser;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTests {

    User user = new User("aa3@yandex.ru", "50313864", "Саша");
    StepsForUser stepsForUser = new StepsForUser();

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Позитивная проверка статус кода и тела ответа при авторизации пользователя")
    public void loginUserCheck() {
        stepsForUser.setUser(user);
        stepsForUser.createUser();
        stepsForUser.loginUser(new User("aa3@yandex.ru", "50313864"))
                .then().assertThat().body("accessToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Негативная проверка статус кода и тела ответа при авторизации пользователя с неверным логином")
    public void loginUserWithWrongLoginCheck() {
        stepsForUser.setUser(user);
        stepsForUser.createUser();
        stepsForUser.loginUser(new User("aa2@yandex.ru", "50313864"))
                .then().assertThat().body("message", is("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Негативная проверка статус кода и тела ответа при авторизации пользователя с неверным паролем")
    public void loginUserWithWrongPasswordCheck() {
        stepsForUser.setUser(user);
        stepsForUser.createUser();
        stepsForUser.loginUser(new User("aa3@yandex.ru", "50313864@@@"))
                .then().assertThat().body("message", is("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void deleteUser(){
        accessToken = stepsForUser.loginUser(user).then().extract().path("accessToken");
        if (accessToken!=null) {
            stepsForUser.deleteUser(accessToken);
        }
    }
}