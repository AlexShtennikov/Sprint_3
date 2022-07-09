package ru.yandex.practicum.sprint3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;


public class CreateСourierTest {
    
    private CourierApiClient client;
    private LoginCourierApiClient loginClient;

    private LoginCourier loginCourier;


    @Before
    public void setUp() {
        client = new CourierApiClient();
        loginClient = new LoginCourierApiClient();
        loginCourier = new LoginCourier();
    }

    @Test
    public void validCreateRequestShouldReturnStatusOkAndCorrectMessage() {

        String login = client.createLogin();
        String password = client.createPassword();
        String firstName = client.createFirstName();

        final Courier courier = new Courier(login, password, firstName);

        boolean result = client.createCourier(courier)
                .then()
                .statusCode(201)
                .extract().body().path("ok");

        Assert.assertTrue(result);

        loginCourier.deleteCourierWithId(login, password);

    }

    @Test
    public void validCreateWithExistingCourierShouldReturnError() {

        String login = client.createLogin();
        String password = client.createPassword();
        String firstName = client.createFirstName();

        final Courier courier = new Courier(login, password, firstName);

        //Создаем курьера
        client.createCourier(courier)
                .then()
                .statusCode(201)
                .extract().body().path("ok");

        //Пробуем создать его еще раз
        String actual = client.createCourier(courier)
                .then()
                .statusCode(409)
                .extract().body().path("message");

        String expected = "Этот логин уже используется. Попробуйте другой.";
        Assert.assertEquals(actual, expected);

        loginCourier.deleteCourierWithId(login, password);

    }


    @Test
    public void createCourierWithoutRequiredFieldLoginShouldReturnError() {

        String password = client.createPassword();
        String firstName = client.createFirstName();

        final Courier courier = new Courier(null, password, firstName);

        String actual = client.createCourier(courier)
                .then()
                .statusCode(400)
                .extract().body().path("message");

        String expected = "Недостаточно данных для создания учетной записи";
        Assert.assertEquals(actual, expected);

    }

    @Test
    public void createCourierWithoutRequiredFieldPasswordShouldReturnError() {

        String login = client.createLogin();
        String firstName = client.createFirstName();

        final Courier courier = new Courier(login, null, firstName);

        String actual = client.createCourier(courier)
                .then()
                .statusCode(400)
                .extract().body().path("message");

        String expected = "Недостаточно данных для создания учетной записи";
        Assert.assertEquals(actual, expected);

    }
}
