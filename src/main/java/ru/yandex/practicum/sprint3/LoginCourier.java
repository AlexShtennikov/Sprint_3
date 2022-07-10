package ru.yandex.practicum.sprint3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class LoginCourier {
    private String login;
    private String password;

    public LoginCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginCourier() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void deleteCourierWithId(String login, String password) {

        LoginCourierApiClient loginClient = new LoginCourierApiClient();
        CourierApiClient client = new CourierApiClient();
        final LoginCourier loginCourier = new LoginCourier(login, password);

        int id = loginClient.loginCourier(loginCourier)
                .then()
                .statusCode(200)
                .extract().body().path("id");

        RestAssured.with()
                .baseUri(client.BASE_URL)
                .contentType(ContentType.JSON)
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200);
    }

}
