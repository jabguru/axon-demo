package com.jabguru.axondemo.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String productId;
}