package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.User;

import static io.restassured.RestAssured.given;

public class StepsForUser {

    private User user;

    private static final String CREATE_USER_ENDPOINT = "api/auth/register";
    private static final String LOGIN_USER_ENDPOINT = "api/auth/login";
    private static final String INFO_USER_ENDPOINT = "api/auth/user";

    public void setUser(User user) {
        this.user = user;
    }

    @Step("Создать пользователя")
    public Response createUser() {
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post(CREATE_USER_ENDPOINT);
        return response;
    }

    @Step("Авторизоваться пользователем")
    public Response loginUser(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_ENDPOINT);
    }

    @Step("Изментиь данные пользователя")
    public Response changeUser(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch(INFO_USER_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        return given()
                .header("Authorization",accessToken)
                .when()
                .delete(INFO_USER_ENDPOINT);
    }
}