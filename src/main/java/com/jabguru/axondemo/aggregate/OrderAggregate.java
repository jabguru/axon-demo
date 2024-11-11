package com.jabguru.axondemo.aggregate;

import com.jabguru.axondemo.commands.ConfirmOrderCommand;
import com.jabguru.axondemo.commands.CreateOrderCommand;
import com.jabguru.axondemo.commands.ShipOrderCommand;
import com.jabguru.axondemo.events.OrderConfirmedEvent;
import com.jabguru.axondemo.events.OrderCreatedEvent;
import com.jabguru.axondemo.events.OrderShippedEvent;
import com.jabguru.axondemo.exceptions.UnconfirmedOrderException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    protected OrderAggregate() { }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command.getOrderId(), command.getProductId()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        orderConfirmed = false;
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        if (orderConfirmed) {
            return;
        }
        apply(new OrderConfirmedEvent(orderId));
    }

    @CommandHandler
    public void handle(ShipOrderCommand command)  {
        if (!orderConfirmed) {
            throw new UnconfirmedOrderException("Cannot ship an unconfirmed order.", new Throwable());
        }
        apply(new OrderShippedEvent(orderId));
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        orderConfirmed = true;
    }

}