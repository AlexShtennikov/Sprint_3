package ru.yandex.practicum.sprint3;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrdersTest {
    OrderApiClient apiOrder = new OrderApiClient();
    private final String[] order1 = new String[]{"BLACK"};
    private final String[] order2 = new String[]{"GRAY"};
    private final Filter requestFilter = new RequestLoggingFilter();
    private final Filter responseFilter = new ResponseLoggingFilter();

    @Test
    public void validRequestShouldReturnSuccessCodeAndList(){
    //Создадим два заказа
    Order orderTest1 = new Order(order1);
    Order orderTest2 = new Order(order2);

    int track1 = apiOrder.createOrder(orderTest1)
            .then()
            .statusCode(201)
            .extract().body().path("track");

    int track2 = apiOrder.createOrder(orderTest1)
            .then()
            .statusCode(201)
            .extract().body().path("track");

    MainOrdersClass orderList = RestAssured.with()
                    .filters(requestFilter, responseFilter)
                    .baseUri(apiOrder.BASE_URL)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .get("/api/v1/orders")
                    .body().as(MainOrdersClass.class);

    //Проверим, что у нас список
    assertTrue(orderList.getOrders().getClass() == ArrayList.class);
    //Проверим, что список не пустой
    assertTrue(orderList.getOrders().size() > 0);

    //Отменим созданные заказы
    orderTest1.cancelOrderWithTrack(track1);
    orderTest2.cancelOrderWithTrack(track2);
    }
}
