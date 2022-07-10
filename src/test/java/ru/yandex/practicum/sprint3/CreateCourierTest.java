package ru.yandex.practicum.sprint3;

import org.junit.*;


public class CreateCourierTest {
    private CourierApiClient client;
    private LoginCourier loginCourier;
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        client = new CourierApiClient();
        loginCourier = new LoginCourier();
        login = client.createLogin();
        password = client.createPassword();
        firstName = client.createFirstName();
    }

    @Test
    public void validCreateRequestShouldReturnStatusOkAndCorrectMessage() {
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

        final Courier courier = new Courier(login, null, firstName);

        String actual = client.createCourier(courier)
                .then()
                .statusCode(400)
                .extract().body().path("message");

        String expected = "Недостаточно данных для создания учетной записи";
        Assert.assertEquals(actual, expected);
    }
}
