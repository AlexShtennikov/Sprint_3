package ru.yandex.practicum.sprint3;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginCourierApiClient {

    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    public Response loginCourier(LoginCourier loginCourier){

        return RestAssured.with()
                .filters(requestFilter, responseFilter)
                .baseUri(BASE_URL)
                .body(loginCourier)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/api/v1/courier/login");
    }

}
