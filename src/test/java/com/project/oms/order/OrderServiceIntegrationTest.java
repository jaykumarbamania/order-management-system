//package com.project.oms.order;
//
//import com.project.oms.order.domain.Order;
//import com.project.oms.order.domain.OrderStatus;
//import com.project.oms.order.events.OrderCreatedEvent;
//import com.project.oms.order.service.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.event.EventListener;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.transaction.TestTransaction;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class OrderServiceIntegrationTest {
//
//    @Autowired
//    private OrderService orderService;
//
//    private static final List<OrderCreatedEvent> EVENTS = new ArrayList<>();
//
//    @EventListener
//    public void handle(OrderCreatedEvent event) {
//        EVENTS.add(event);
//    }
//
//    @BeforeEach
//    void setup() {
//        EVENTS.clear();
//    }
//
//    @Test
//    void shouldCreateOrderAndPublishEvent() {
//        UUID userId = UUID.randomUUID();
//
//        Order order = orderService.createOrder(userId, BigDecimal.valueOf(500));
//
//        // 🔥 Force transaction commit
//        TestTransaction.flagForCommit();
//        TestTransaction.end();
//
//        assertThat(order.getId()).isNotNull();
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
//
//        assertThat(EVENTS).hasSize(1);
//        assertThat(EVENTS.get(0).getOrderId()).isEqualTo(order.getId());
//    }
//
//}
