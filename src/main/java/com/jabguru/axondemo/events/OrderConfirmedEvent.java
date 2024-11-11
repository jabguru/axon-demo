package com.jabguru.axondemo.events;

import lombok.Data;

@Data
public class OrderConfirmedEvent {

    private final String orderId;
}
