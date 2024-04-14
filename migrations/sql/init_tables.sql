CREATE TABLE link
(
    id             SERIAL PRIMARY KEY,
    url            TEXT                     NOT NULL UNIQUE,
    updated_at     TIMESTAMP WITH TIME ZONE,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    commit_message TEXT,
    commit_sha     TEXT,
    answer_id      BIGINT,
    answer_owner   TEXT
);
CREATE TABLE chat
(
    id BIGINT PRIMARY KEY
);
CREATE TABLE chat_link
(
    link_id INTEGER REFERENCES link (id),
    chat_id BIGINT REFERENCES chat (id),
    PRIMARY KEY (link_id, chat_id)
)