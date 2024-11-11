package com.jabguru.axondemo;

import com.jabguru.axondemo.aggregate.OrderAggregate;
import com.jabguru.axondemo.commands.CreateOrderCommand;
import com.jabguru.axondemo.commands.ShipOrderCommand;
import com.jabguru.axondemo.events.OrderConfirmedEvent;
import com.jabguru.axondemo.events.OrderCreatedEvent;
import com.jabguru.axondemo.events.OrderShippedEvent;
import com.jabguru.axondemo.exceptions.UnconfirmedOrderException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AxonDemoApplicationTests {
    private FixtureConfiguration fixture;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    public void testCreateOrder() {
        String orderId = UUID.randomUUID().toString();
        String productId = "Deluxe Chair";
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(orderId, productId))
                .expectEvents(new OrderCreatedEvent(orderId, productId));
    }

    @Test
    public void testShipOrderWithUnconfirmedOrder() {
        String orderId = UUID.randomUUID().toString();
        String productId = "Deluxe Chair";
        fixture.given(new OrderCreatedEvent(orderId, productId))
                .when(new ShipOrderCommand(orderId))
                .expectException(UnconfirmedOrderException.class);
    }

    @Test
    public void testShipOrder() {
        String orderId = UUID.randomUUID().toString();
        String productId = "Deluxe Chair";
        fixture.given(new OrderCreatedEvent(orderId, productId), new OrderConfirmedEvent(orderId))
                .when(new ShipOrderCommand(orderId))
                .expectEvents(new OrderShippedEvent(orderId));
    }



}
