package org.example.foodanddrinkproject.service.impl;

import org.example.foodanddrinkproject.dto.OrderDto;
import org.example.foodanddrinkproject.service.ChatworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatworkServiceImpl implements ChatworkService {

    private static final Logger logger = LoggerFactory.getLogger(ChatworkServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.chatwork.api-token}")
    private String apiToken;

    @Value("${app.chatwork.room-id}")
    private String roomId;

    @Value("${app.chatwork.api-url}")
    private String apiUrl;

    @Override
    public void sendOrderNotification(OrderDto order) {
        try {
            String url = apiUrl + "/rooms/" + roomId + "/messages";
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-ChatWorkToken", apiToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String messageContent = String.format(
                    "[info][title]🛒 New Order Received! #%d[/title]" +
                            "Total Amount: $%.2f\n" +
                            "Payment Method: %s\n" +
                            "Status: %s[/info]",
                    order.getId(),
                    order.getTotalAmount(),
                    order.getPaymentMethod(),
                    order.getOrderStatus()
            );

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("body", messageContent);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            restTemplate.postForObject(url, request, String.class);

            logger.info("Chatwork notification sent for Order ID: {}", order.getId());

        } catch (Exception e) {
            logger.error("Failed to send Chatwork message", e);
        }
    }
}