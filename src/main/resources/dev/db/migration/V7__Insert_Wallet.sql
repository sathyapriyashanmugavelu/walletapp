INSERT INTO wallet (id,balance,user_id)
VALUES (1,
        0,
        1) ON CONFLICT DO NOTHING;

INSERT INTO wallet (id,balance,user_id)
VALUES (3,
        0,
        2) ON CONFLICT DO NOTHING;

