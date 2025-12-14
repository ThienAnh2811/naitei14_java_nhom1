package org.example.foodanddrinkproject.service.impl;

import org.example.foodanddrinkproject.dto.OrderDto;
import org.example.foodanddrinkproject.dto.OrderItemDto;
import org.example.foodanddrinkproject.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Async("asyncExecutor")
    @Override
    public void sendOrderConfirmationEmail(OrderDto order) {
        try {
            logger.info("Sending order confirmation email asynchronously to user: {}", order.getUserEmail());
            
            // Simulate email sending delay
            Thread.sleep(2000);
            
            String emailContent = buildOrderConfirmationEmail(order);
            
            // TODO: Integrate with real email service (JavaMailSender)
            // For now, just log the email content
            logger.info("Email sent successfully to {}", order.getUserEmail());
            logger.debug("Email content:\n{}", emailContent);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Order confirmation email was interrupted for Order ID: {}", order.getId());
        } catch (Exception e) {
            logger.error("Failed to send order confirmation email for Order ID: {}", order.getId(), e);
        }
    }

    @Async("asyncExecutor")
    @Override
    public void sendOrderStatusUpdateEmail(OrderDto order, String oldStatus, String newStatus) {
        try {
            logger.info("Sending order status update email asynchronously to user: {}", order.getUserEmail());
            
            // Simulate email sending delay
            Thread.sleep(1500);
            
            String emailContent = buildOrderStatusUpdateEmail(order, oldStatus, newStatus);
            
            // TODO: Integrate with real email service
            logger.info("Status update email sent successfully to {}", order.getUserEmail());
            logger.debug("Email content:\n{}", emailContent);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Order status update email was interrupted for Order ID: {}", order.getId());
        } catch (Exception e) {
            logger.error("Failed to send status update email for Order ID: {}", order.getId(), e);
        }
    }

    @Async("asyncExecutor")
    @Override
    public void sendAdminNotification(OrderDto order) {
        try {
            logger.info("Sending admin notification asynchronously for Order ID: {}", order.getId());
            
            // Simulate notification delay
            Thread.sleep(1000);
            
            String notificationContent = buildAdminNotification(order);
            
            // TODO: Integrate with admin notification system
            logger.info("Admin notification sent successfully for Order ID: {}", order.getId());
            logger.debug("Notification content:\n{}", notificationContent);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Admin notification was interrupted for Order ID: {}", order.getId());
        } catch (Exception e) {
            logger.error("Failed to send admin notification for Order ID: {}", order.getId(), e);
        }
    }

    private String buildOrderConfirmationEmail(OrderDto order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear ").append(order.getUserEmail()).append(",\n\n");
        sb.append("Thank you for your order!\n\n");
        sb.append("Order Details:\n");
        sb.append("Order ID: #").append(order.getId()).append("\n");
        sb.append("Order Date: ").append(order.getCreatedAt().format(DATE_FORMATTER)).append("\n");
        sb.append("Status: ").append(order.getOrderStatus()).append("\n");
        sb.append("Payment Method: ").append(order.getPaymentMethod()).append("\n");
        sb.append("Shipping Address: ").append(order.getShippingAddress()).append("\n\n");
        
        sb.append("Items:\n");
        for (OrderItemDto item : order.getItems()) {
            sb.append("- ").append(item.getProductName())
              .append(" x").append(item.getQuantity())
              .append(" - $").append(item.getPriceAtPurchase())
              .append("\n");
        }
        
        sb.append("\nSubtotal: $").append(order.getSubtotal()).append("\n");
        sb.append("Shipping: $").append(order.getShippingCost()).append("\n");
        sb.append("Discount: -$").append(order.getDiscountAmount()).append("\n");
        sb.append("Total: $").append(order.getTotalAmount()).append("\n\n");
        
        sb.append("We will send you another email when your order is shipped.\n\n");
        sb.append("Best regards,\n");
        sb.append("Food & Drink Project Team");
        
        return sb.toString();
    }

    private String buildOrderStatusUpdateEmail(OrderDto order, String oldStatus, String newStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear Customer,\n\n");
        sb.append("Your order status has been updated!\n\n");
        sb.append("Order ID: #").append(order.getId()).append("\n");
        sb.append("Previous Status: ").append(oldStatus).append("\n");
        sb.append("New Status: ").append(newStatus).append("\n");
        sb.append("Updated At: ").append(order.getUpdatedAt().format(DATE_FORMATTER)).append("\n\n");
        
        if ("SHIPPED".equals(newStatus)) {
            sb.append("Your order is on its way! You should receive it soon.\n");
        } else if ("DELIVERED".equals(newStatus)) {
            sb.append("Your order has been delivered! Enjoy your meal!\n");
        } else if ("CANCELLED".equals(newStatus)) {
            sb.append("Your order has been cancelled. If you did not request this, please contact us.\n");
        }
        
        sb.append("\nBest regards,\n");
        sb.append("Food & Drink Project Team");
        
        return sb.toString();
    }

    private String buildAdminNotification(OrderDto order) {
        StringBuilder sb = new StringBuilder();
        sb.append("🛒 New Order Alert!\n\n");
        sb.append("Order ID: #").append(order.getId()).append("\n");
        sb.append("Customer: ").append(order.getUserEmail()).append("\n");
        sb.append("Total Amount: $").append(order.getTotalAmount()).append("\n");
        sb.append("Payment Method: ").append(order.getPaymentMethod()).append("\n");
        sb.append("Payment Status: ").append(order.getPaymentStatus()).append("\n");
        sb.append("Items Count: ").append(order.getItems().size()).append("\n");
        sb.append("Created At: ").append(order.getCreatedAt().format(DATE_FORMATTER)).append("\n");
        
        return sb.toString();
    }
}
