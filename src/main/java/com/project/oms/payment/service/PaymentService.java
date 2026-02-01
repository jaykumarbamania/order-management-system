package com.project.oms.payment.service;

import com.project.oms.common.events.EventEnvelope;
import com.project.oms.common.vo.AggregateType;
import com.project.oms.infrastructure.eventbus.DomainEventPublisher;
import com.project.oms.inventory.events.InventoryReservedEvent;
import com.project.oms.payment.domain.Payment;
import com.project.oms.payment.domain.PaymentStatus;
import com.project.oms.payment.events.PaymentFailedEvent;
import com.project.oms.payment.events.PaymentSuccessEvent;
import com.project.oms.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DomainEventPublisher eventPublisher;

    private final Random random = new Random();

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleInventoryReserved(InventoryReservedEvent event) {
        UUID orderId = event.getOrderId();
        log.info("Payment received InventoryReservedEvent for orderId={}", orderId);

        // MOCK payment decision (80% success)
        boolean success = random.nextInt(10) < 8;

        if(success) {
            paymentRepository.save(new Payment(orderId, BigDecimal.valueOf(500), PaymentStatus.SUCCESS));

            log.info("Payment successful for orderId={}", orderId);

            eventPublisher.publish(
                    EventEnvelope.of(AggregateType.PAYMENT,orderId,new PaymentSuccessEvent(orderId))
            );

        } else {
            paymentRepository.save(
                    new Payment(orderId, BigDecimal.valueOf(500), PaymentStatus.FAILED)
            );

            log.warn("Payment failed for orderId={}", orderId);

            eventPublisher.publish(
                    EventEnvelope.of(AggregateType.PAYMENT,orderId,new PaymentFailedEvent(orderId,"Payment gateway failure"))
            );
        }
    }

}