package com.okuylu_back.security.service;

import com.okuylu_back.model.Order;
import com.okuylu_back.model.enums.OrderStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class OrderNotificationService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${store.address}")
    private String storeAddress;

    @Value("${store.phone}")
    private String storePhone;

    private final JavaMailSender mailSender;

    public OrderNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmationEmail(Order order) {
        String subject = "№" + order.getOrderId() + " буюртманы тастыктоо";
        String content = buildStyledTemplate(buildOrderConfirmationBody(order));
        sendEmail(order.getUser().getEmail(), subject, content);
    }

    public void sendOrderStatusUpdateEmail(Order order) {
        String subject = "№" + order.getOrderId() + " буюртманын статусун жаңылоо";
        String content = buildStyledTemplate(buildOrderStatusUpdateBody(order));
        sendEmail(order.getUser().getEmail(), subject, content);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(from);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка отправки email", e);
        }
    }

    private String buildStyledTemplate(String bodyContent) {
        return """
                <html>
                <head>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Inter:ital@1&display=swap');
                        body {
                            font-family: 'Inter', sans-serif;
                            font-style: italic;
                            background-color: #f9f9f9;
                            color: #333;
                            padding: 0;
                            margin: 0;
                        }
                        .header, .footer {
                            background-color: #0d1b2a;
                            color: white;
                            padding: 20px;
                            text-align: center;
                        }
                        .header img {
                            height: 40px;
                            float: left;
                            margin-right: 15px;
                        }
                        .container {
                            padding: 30px;
                        }
                        h3 {
                            color: #1e2a38;
                        }
                        ul {
                            padding-left: 20px;
                        }
                        hr {
                            margin: 40px 0;
                            border: none;
                            border-top: 1px solid #ccc;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2 style="margin: 0; padding-top: 5px;"> «OKU.KG» китеп дүкөнү</h2>
                    </div>
                    <div class="container">
                """ + bodyContent + """
                    </div>
                    <div class="footer">
                        <p>Бизди тандаганыңыз үчүн рахмат! — «OKU.KG»</p>
                    </div>
                </body>
                </html>
                """;
    }

    private String buildOrderConfirmationBody(Order order) {
        StringBuilder message = new StringBuilder();
        String formattedDate = order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // ——— Кыргызча ———
        message.append("<h3>Урматтуу кардар,</h3>");
        message.append("<p>Сиздин №").append(order.getOrderId()).append(" заказыңыз ийгиликтүү кабыл алынды.</p>");
        message.append("<p><b>Буюртманын чоо-жайы:</b></p><ul>");
        message.append("<li>Буюртма күнү: ").append(formattedDate).append("</li>");
        message.append("<li>Жалпы сумма: ").append(order.getDiscountedPrice()).append(" сом.</li>");
        message.append("<li>Алуу ыкмасы: ").append(getDeliveryMethodTextKg(order.isSelfPickup())).append("</li>");
        if (!order.isSelfPickup()) {
            message.append("<li>Жеткирүү дареги: ").append(order.getDeliveryAddress()).append("</li>");
            BigDecimal deliveryCost = order.getDeliveryCost();
            if (deliveryCost != null) {
                message.append("<li>Жеткирүү баасы: ").append(deliveryCost).append(" сом. (жеткирилгенде төлөнөт)</li>");
            } else {
                message.append("<li>Жеткирүү баасы менеджер тарабынан такталат.</li>");
            }
        } else {
            message.append("<li>Дүкөндүн дареги: ").append(storeAddress).append("</li>");
            message.append("<li>Дүкөндүн номери: ").append(storePhone).append("</li>");
        }
        message.append("</ul>");

        message.append("<p><b>Буюртмадагы китептер:</b></p><ul>");
        order.getOrderItems().forEach(item -> message.append("<li>")
                .append(item.getBook().getTitle())
                .append(" (").append(item.getQuantity()).append(" даана = ").append(item.getPrice()).append(" сом)</li>"));
        message.append("</ul>");

        message.append("<p>Сатып алганыңыз үчүн рахмат! Жакын арада сиз менен байланышабыз.</p>");
        message.append("<hr>");

        // ——— Русский ———
        message.append("<h3>Уважаемый клиент,</h3>");
        message.append("<p>Ваш заказ №").append(order.getOrderId()).append(" успешно оформлен.</p>");
        message.append("<p><b>Детали заказа:</b></p><ul>");
        message.append("<li>Дата заказа: ").append(formattedDate).append("</li>");
        message.append("<li>Общая сумма: ").append(order.getDiscountedPrice()).append(" сом.</li>");
        message.append("<li>Способ получения: ").append(getDeliveryMethodText(order.isSelfPickup())).append("</li>");
        if (!order.isSelfPickup()) {
            message.append("<li>Адрес доставки: ").append(order.getDeliveryAddress()).append("</li>");
            BigDecimal deliveryCost = order.getDeliveryCost();
            if (deliveryCost != null) {
                message.append("<li>Стоимость доставки: ").append(deliveryCost).append(" сом. (оплата при получении)</li>");
            } else {
                message.append("<li>Стоимость доставки будет уточнена менеджером.</li>");
            }
        } else {
            message.append("<li>Адрес самовывоза: ").append(storeAddress).append("</li>");
            message.append("<li>Телефон магазина: ").append(storePhone).append("</li>");
        }
        message.append("</ul>");

        message.append("<p><b>Книги в заказе:</b></p><ul>");
        order.getOrderItems().forEach(item -> message.append("<li>")
                .append(item.getBook().getTitle())
                .append(" (").append(item.getQuantity()).append(" шт. = ").append(item.getPrice()).append(" сом)</li>"));
        message.append("</ul>");

        message.append("<p>Спасибо за покупку! Мы свяжемся с вами в ближайшее время.</p>");


        return message.toString();
    }

    private String buildOrderStatusUpdateBody(Order order) {
        StringBuilder message = new StringBuilder();
        String formattedDate = order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


        // ——— Кыргызча ———
        message.append("<h3>Урматтуу кардар,</h3>");
        message.append("<p>Сиздин №").append(order.getOrderId()).append(" буюртмаңыздын статусу өзгөрдү.</p>");
        message.append("<p><b>Учурдагы статус:</b> ").append(getStatusTextKg(order.getStatus())).append("</p>");
        message.append("<p><b>Буюртма күнү: </b> ").append(formattedDate).append("</p>");

        if (!order.isSelfPickup() && (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED)) {
            message.append("<p><b>Жеткирүү дареги:</b> ").append(order.getDeliveryAddress()).append("</p>");
            BigDecimal deliveryCost = order.getDeliveryCost();
            if (deliveryCost != null) {
                message.append("<p><b>Жеткирүү баасы:</b> ").append(deliveryCost).append(" сом. (жеткирилгенде төлөнөт)</p>");
            } else {
                message.append("<p><b>Жеткирүү баасы:</b> менеджер кабарлайт.</p>");
            }
        }
        if (order.getAssignedManager() != null) {
            message.append("<p><b>Менеджердин байланыш маалыматтары:</b></p><ul>");
            message.append("<li><b>Аты:</b> ").append(order.getAssignedManager().getUsername()).append("</li>");
            message.append("<li><b>Телефон:</b> ").append(order.getAssignedManager().getPhone()).append("</li>");
            message.append("<li><b>Email:</b> ").append(order.getAssignedManager().getEmail()).append("</li>");
            message.append("</ul>");
        }

        message.append("<hr>");

        // ——— Русский ———
        message.append("<h3>Уважаемый клиент,</h3>");
        message.append("<p>Информация о вашем заказе №").append(order.getOrderId()).append(" обновлена.</p>");
        message.append("<p><b>Текущий статус:</b> ").append(getStatusText(order.getStatus())).append("</p>");
        message.append("<p><b>Дата заказа: </b> ").append(formattedDate).append("</p>");
        if (!order.isSelfPickup() && (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED)) {
            message.append("<p><b>Адрес доставки:</b> ").append(order.getDeliveryAddress()).append("</p>");
            BigDecimal deliveryCost = order.getDeliveryCost();
            if (deliveryCost != null) {
                message.append("<p><b>Стоимость доставки:</b> ").append(deliveryCost).append(" сом. (оплата при получении)</p>");
            } else {
                message.append("<p><b>Стоимость доставки:</b> будет уточнена менеджером.</p>");
            }
        }
        if (order.getAssignedManager() != null) {
            message.append("<p><b>Контакт менеджера:</b></p><ul>");
            message.append("<li><b>Имя:</b> ").append(order.getAssignedManager().getUsername()).append("</li>");
            message.append("<li><b>Телефон:</b> ").append(order.getAssignedManager().getPhone()).append("</li>");
            message.append("<li><b>Email:</b> ").append(order.getAssignedManager().getEmail()).append("</li>");
            message.append("</ul>");
        }


        return message.toString();
    }

    public String sendPaymentConfirmationEmail(Order order){
    StringBuilder message = new StringBuilder();
    String formattedDate = order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    // ——— Кыргызча ———
        message.append("<h3>Урматтуу кардар,</h3>");
        message.append("<p>Сиздин №").

    append(order.getOrderId()).

    append(" заказыңыз төлөмү ийгиликтүү ишке ашты.</p>");
        message.append("<p><b>Буюртманын чоо-жайы:</b></p><ul>");
        message.append("<li>Буюртма күнү: ").

    append(formattedDate).

    append("</li>");
        message.append("<li>Жалпы сумма: ").

    append(order.getDiscountedPrice()).

    append(" сом.</li>");
        message.append("<li>Алуу ыкмасы: ").

    append(getDeliveryMethodTextKg(order.isSelfPickup())).

    append("</li>");
        if(!order.isSelfPickup())

    {
        message.append("<li>Жеткирүү дареги: ").append(order.getDeliveryAddress()).append("</li>");
        BigDecimal deliveryCost = order.getDeliveryCost();
        if (deliveryCost != null) {
            message.append("<li>Жеткирүү баасы: ").append(deliveryCost).append(" сом. (жеткирилгенде төлөнөт)</li>");
        } else {
            message.append("<li>Жеткирүү баасы менеджер тарабынан такталат.</li>");
        }
    } else

    {
        message.append("<li>Дүкөндүн дареги: ").append(storeAddress).append("</li>");
        message.append("<li>Дүкөндүн номери: ").append(storePhone).append("</li>");
    }
        message.append("</ul>");

        message.append("<p><b>Буюртмадагы китептер:</b></p><ul>");
        order.getOrderItems().

    forEach(item ->message.append("<li>")
            .

    append(item.getBook().

    getTitle())
            .

    append(" (").

    append(item.getQuantity()).

    append(" даана = ").

    append(item.getPrice()).

    append(" сом)</li>"));
        message.append("</ul>");

        message.append("<p>Сатып алганыңыз үчүн рахмат! Жакын арада сиз менен байланышабыз.</p>");
        message.append("<hr>");
        return message.toString();

    }

    private String getDeliveryMethodText(boolean isSelfPickup) {
        return !isSelfPickup ? "Доставка" : "Самовывоз";
    }

    private String getStatusText(OrderStatus status) {
        return switch (status) {
            case PROCESSING -> "В обработке";
            case SHIPPED -> "Отправлен";
            case DELIVERED -> "Доставлен";
            case CANCELLED -> "Отменен";
            case PICKED_UP -> "Забрали заказ";
            case PENDING -> "В ожидании";
            case PAYMENT_FAILED -> "Платеж не выполнен";
            default -> "Неизвестный статус";
        };
    }

    private String getDeliveryMethodTextKg(boolean isSelfPickup) {
        return isSelfPickup ? "Өзү барып алуу" : "Жеткирүү";
    }

    private String getStatusTextKg(OrderStatus status) {
        return switch (status) {
            case PROCESSING -> "Иштелүүдө";
            case SHIPPED -> "Жөнөтүлдү";
            case DELIVERED -> "Жеткирилди";
            case CANCELLED -> "Жокко чыгарылды";
            case PICKED_UP -> "Алынды";
            case PENDING -> "Күтүү режиминде";
            case PAYMENT_FAILED -> "Төлөм ишке ашкан жок";
            default -> "Белгисиз статус";
        };
    }
}
