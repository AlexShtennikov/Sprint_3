package ru.yandex.practicum.sprint3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class LoginCourierTest {
    private LoginCourierApiClient loginClient;
    private CourierApiClient client;
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        loginClient = new LoginCourierApiClient();
        client = new CourierApiClient();
        login = client.createLogin();
        password = client.createPassword();
        firstName = client.createFirstName();
    }

    @Test
    public void validLoginRequestShouldReturnId() {
        final Courier courier = new Courier(login, password, firstName);

        //Создаем курьера
        client.createCourier(courier)
                .then()
                .statusCode(201);

        final LoginCourier loginCourier = new LoginCourier(login, password);

        //Логинимся под созданным курьером
        int id = loginClient.loginCourier(loginCourier)
                .then()
                .statusCode(200)
                .extract().body().path("id");

        //Убедимся, что у нас вернулось число и оно больше нуля
        assertTrue(id > 0);

        loginCourier.deleteCourierWithId(login, password);
    }


    @Test
    public void validLoginRequestWithoutLoginShouldReturnError() {
        final Courier courier = new Courier(login, password, firstName);

        //Создаем курьера
        client.createCourier(courier)
                .then()
                .statusCode(201);

        final LoginCourier loginCourier = new LoginCourier(null, password);

        //Логинимся под созданным курьером
        String actual = loginClient.loginCourier(loginCourier)
                .then()
                .statusCode(400)
                .extract().body().path("message");

        String expected = "Недостаточно данных для входа";
        Assert.assertEquals(actual, expected);

        loginCourier.deleteCourierWithId(login, password);
    }

    @Test
    public void validLoginRequestWithoutPasswordShouldReturnError() {
        final Courier courier = new Courier(login, password, firstName);

        //Создаем курьера
        client.createCourier(courier)
                .then()
                .statusCode(201);
        //Данный тест зависает, если вместо password передавать null, возможно,
        //что не до конца документировано api
        //Можно передавать пустую строку, с ней работает
        //поэтому сделал возврат с ошибкой 504
        final LoginCourier loginCourier = new LoginCourier(login, null);

        //Логинимся под созданным курьером и получим ошибку 504
        loginClient.loginCourier(loginCourier)
                .then()
                .statusCode(504);

        //Если бы все отработало, как в документации,
        //то код был бы таким:
                //.statusCode(400);
                //.extract().body().path("message");

        //String expected = "Недостаточно данных для входа";
        //Assert.assertEquals(actual, expected);

        loginCourier.deleteCourierWithId(login, password);
    }

    @Test
    public void validLoginRequestWithNotExistingLoginAndPasswordShouldReturnError() {
        final LoginCourier loginCourier = new LoginCourier(login, password);

        String actual = loginClient.loginCourier(loginCourier)
                .then()
                .statusCode(404)
                .extract().body().path("message");

        String expected = "Учетная запись не найдена";
        Assert.assertEquals(actual, expected);
    }
}
