package com.project.oms.common.idempotency;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
        name = "idempotency_records",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idempotencyKey"})
)
@Getter
@NoArgsConstructor
public class IdempotencyRecord {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private String idempotencyKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String responseBody;

    @Column(nullable = false)
    private int statusCode;

    @Column(nullable = false)
    private Instant createdAt;

    public IdempotencyRecord(String key, String body, int statusCode) {
        this.idempotencyKey = key;
        this.responseBody = body;
        this.statusCode = statusCode;
        this.createdAt = Instant.now();
    }
}
