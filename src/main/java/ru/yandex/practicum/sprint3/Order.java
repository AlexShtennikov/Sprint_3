package ru.yandex.practicum.sprint3;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Order {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public Order(String[] color) {
        this.firstName = getFirstName();
        this.lastName = getLastName();
        this.address = getAddress();
        this.metroStation = getMetroStation();
        this.phone = getPhone();
        this.rentTime = getRentTime();
        this.deliveryDate = getDeliveryDate();
        this.comment = getComment();
        this.color = color;
    }

    public String getFirstName() {
        return "testName";
    }

    public String getLastName() {
        return "testLastName";
    }

    public String getAddress() {
        return "testAddress";
    }

    public String getMetroStation() {
        return "testMetro";
    }

    public String getPhone() {
        return "777-555-333";
    }

    public int getRentTime() {
        return 3;
    }

    public String getDeliveryDate() {
        return "2022-08-01";
    }

    public String getComment() {
        return "testComment";
    }

    public void cancelOrderWithTrack(int track) {

        OrderApiClient apiClient = new OrderApiClient();

        RestAssured.with()
                .baseUri(apiClient.BASE_URL)
                .contentType(ContentType.JSON)
                .put("/api/v1/orders/cancel?track=" + track)
                .then()
                .statusCode(200);
    }


}
