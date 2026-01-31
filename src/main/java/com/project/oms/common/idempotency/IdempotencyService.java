package com.project.oms.common.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository repository;

    public Optional<IdempotencyRecord> find(String key) {
        return repository.findByIdempotencyKey(key);
    }

    @Transactional
    public void save(String key, String body, int statusCode) {
        repository.save(
                new IdempotencyRecord(key, body, statusCode)
        );
    }
}
