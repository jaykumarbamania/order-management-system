package com.project.oms.infrastructure.eventbus;

import com.project.oms.common.events.DomainEvent;
import com.project.oms.common.events.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(EventEnvelope<?> envelope) {
        if(TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            applicationEventPublisher.publishEvent(envelope);
                        }
                    }
            );
        } else {
            applicationEventPublisher.publishEvent(envelope);
        }
    }
}