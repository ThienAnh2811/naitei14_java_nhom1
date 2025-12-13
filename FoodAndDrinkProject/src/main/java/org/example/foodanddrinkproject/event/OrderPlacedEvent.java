package org.example.foodanddrinkproject.event;

import org.example.foodanddrinkproject.dto.OrderDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {

    private final OrderDto orderDto;

    public OrderPlacedEvent(Object source, OrderDto orderDto) {
        super(source);
        this.orderDto = orderDto;
    }
}