package ru.yandex.practicum.sprint3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
        private String[] colors = new String[1];
        public CreateOrderTest(String[] colors) {
            this.colors = colors;
        }
        @Parameterized.Parameters
        public static Object[][] sendOrderData() {

            return new Object[][] {
                    {new String[]{"BLACK"}},
                    {new String[]{"GRAY"}},
                    {new String[]{}},
                    {new String[]{"BLACK", "GRAY"}},
            };
        }

        @Test
        public void validOrderRequestShouldReturnOrderTrack(){
            OrderApiClient apiOrder = new OrderApiClient();
            Order orderTest = new Order(colors);
            int actualResult = apiOrder.createOrder(orderTest)
                    .then()
                    .statusCode(201)
                    .extract().body().path("track");
            //Проверим, что у нас число и оно больше нуля
            assertTrue(actualResult > 0);

            orderTest.cancelOrderWithTrack(actualResult);
        }
}
