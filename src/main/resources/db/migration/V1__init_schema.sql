
create sequence idempotency_records_seq start with 1 increment by 50;

    create table idempotency_records (
        status_code integer not null,
        created_at timestamp(6) with time zone not null,
        id bigint not null,
        idempotency_key varchar(255) not null,
        response_body TEXT not null,
        primary key (id),
        unique (idempotency_key)
    );

    create table inventory (
        available_quantity integer not null,
        version bigint,
        id uuid not null,
        product_id uuid not null unique,
        primary key (id)
    );

    create table orders (
        total_amount numeric(38,2) not null,
        version bigint,
        id uuid not null,
        user_id uuid not null,
        status varchar(255) not null check (status in ('CREATED','INVENTORY_RESERVED','PAYMENT_SUCCESS','CONFIRMED','CANCELLED')),
        primary key (id)
    );

    create table outbox_events (
        published boolean not null,
        occurred_at timestamp(6) with time zone,
        aggregate_id uuid,
        event_id uuid not null,
        aggregate_type varchar(255),
        event_type varchar(255),
        payload oid,
        primary key (event_id)
    );

    create table payments (
        amount numeric(38,2) not null,
        status smallint not null check (status between 0 and 1),
        id uuid not null,
        order_id uuid not null,
        primary key (id)
    );