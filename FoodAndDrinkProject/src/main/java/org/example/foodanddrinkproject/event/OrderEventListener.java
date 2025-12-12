package org.example.foodanddrinkproject.event;

import org.example.foodanddrinkproject.dto.OrderDto;
import org.example.foodanddrinkproject.service.ChatworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    private final ChatworkService chatworkService;

    public OrderEventListener(ChatworkService chatworkService) {
        this.chatworkService = chatworkService;
    }

    @Async
    @EventListener
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        logger.info("Event received. Sending Chatwork notification asynchronously...");

        OrderDto order = event.getOrderDto();
        chatworkService.sendOrderNotification(order);
    }
}
