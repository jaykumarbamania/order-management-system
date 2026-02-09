CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    order_id UUID NOT NULL,
    user_id VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    retry_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_notifications_status
    ON notifications(status);

CREATE INDEX idx_notifications_order_id
    ON notifications(order_id);
