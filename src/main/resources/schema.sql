DROP TABLE IF EXISTS bookings,users,items,requests,comments;

CREATE TABLE IF NOT EXISTS users
(
    id    bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  varchar(255)                            NOT NULL,
    email varchar(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         varchar(255)                            NOT NULL,
    description  varchar(1023),
    is_available boolean,
    owner_id     bigint,
    request_id   bigint,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date timestamp without time zone,
    end_date   timestamp without time zone,
    item_id    bigint,
    booker_id  bigint,
    status     varchar(50),
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment_text varchar(255),
    item_id      bigint,
    author_id    bigint,
    created      timestamp without time zone,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  varchar(255)                            NOT NULL,
    requester_id bigint                                  NOT NULL,
    created      timestamp without time zone,
    item_id      bigint,
    CONSTRAINT pk_request PRIMARY KEY (id)
);


ALTER TABLE items
    ADD CONSTRAINT fk_item FOREIGN KEY (owner_id)
        REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE items
    ADD CONSTRAINT fk_item_request FOREIGN KEY (request_id)
        REFERENCES requests (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE requests
    ADD CONSTRAINT fk_request_user FOREIGN KEY (requester_id)
        REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE requests
    ADD CONSTRAINT fk_request_item FOREIGN KEY (item_id)
        REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE bookings
    ADD CONSTRAINT fk_booking FOREIGN KEY (booker_id)
        REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE bookings
    ADD CONSTRAINT fk_booking_item FOREIGN KEY (item_id)
        REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comment FOREIGN KEY (author_id)
        REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comment_item FOREIGN KEY (item_id)
        REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE;