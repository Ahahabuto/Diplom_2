package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserApi {
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String USER_ENDPOINT = "/api/auth/user";
    private static final String LOGOUT_ENDPOINT = "/api/auth/logout";
    private static final String TOKEN_ENDPOINT = "/api/auth/token";

    @Step("Регистрация пользователя")
    public static Response register(UserData user) {
        return given()
                .spec(Specifications.requestSpec())
                .body(user)
                .when()
                .post(REGISTER_ENDPOINT);
    }

   @Step("Авторизация пользователя")
    public static Response login(UserData user) {
        return given()
                .spec(Specifications.requestSpec())
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT);
   }

   @Step("Удаление пользователя")
    public static Response delete(String accessToken) {
        return given()
                .spec(Specifications.requestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_ENDPOINT);
   }

   @Step("Выход из системы")
    public static Response logout(String refreshToken) {
        return given()
                .spec(Specifications.requestSpec())
                .body(new TokenRequest(refreshToken))
                .when()
                .post(LOGOUT_ENDPOINT);
   }

   @Step("Обновление токена")
    public static Response refreshToken(String accessToken) {
        return given()
                .spec(Specifications.requestSpec())
                .header("Authorization", "Bearer ", accessToken)
                .when()
                .get(TOKEN_ENDPOINT);
   }


   @Step("Изменение данных пользователя")
   public static Response updateUser(String accessToken, UserData updatedUser) {
       RequestSpecification request = given()
               .spec(Specifications.requestSpec())
               .body(updatedUser);

       if (accessToken != null) {
           request.header("Authorization", accessToken);
       }
       return request
               .when()
               .patch(USER_ENDPOINT);
   }

   private static class TokenRequest {
        private final String token;

        public TokenRequest(String token) {
            this.token=token;
        }

        public String getToken() {
            return token;
        }
   }
}