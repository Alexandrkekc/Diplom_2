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

public class UpdateUserTests {

    User user = new User("abcdef@yandex.ru", "50313864", "Алекс");
    StepsForUser stepsForUser = new StepsForUser();
    private String accessToken;

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        stepsForUser.setUser(user);
        stepsForUser.createUser();
        accessToken = stepsForUser.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("Позитивная проверка статус кода и тела ответа при изменении имени пользователя")
    public void changeUserNameCheck() {

        stepsForUser.changeUser(new User("abcdef@yandex.ru", "50313864", "Алексо"), accessToken)
                .then().assertThat().body("success", is(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение email пользователя")
    @Description("Позитивная проверка статус кода и тела ответа при изменении email пользователя")
    public void changeUserEmailCheck() {

        stepsForUser.changeUser(new User("abcdef123@yandex.ru", "50313864", "Алекс"), accessToken)
                .then().assertThat().body("success", is(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    @Description("Негативная проверка статус кода и тела ответа при изменении имени пользователя без авторизации")
    public void changeUserNameWithoutTokenCheck() {

        stepsForUser.changeUser(new User("abcdef@yandex.ru", "50313864345", "Алекс"), "")
                .then().assertThat().body("message", is("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение email пользователя")
    @Description("Негативная проверка статус кода и тела ответа при изменении email пользователя без авторизации")
    public void changeUserEmailWithoutTokenCheck() {

        stepsForUser.changeUser(new User("cc@yandex.ru", "50313864", "Алекс"), "")
                .then().assertThat().body("message", is("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    @Description("Негативная проверка статус кода и тела ответа при изменении пароля пользователя без авторизации")
    public void changeUserPasswordWithoutTokenCheck() {

        stepsForUser.changeUser(new User("abcdef@yandex.ru", "50313864", "Сашка"), "")
                .then().assertThat().body("message", is("You should be authorised"))
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
