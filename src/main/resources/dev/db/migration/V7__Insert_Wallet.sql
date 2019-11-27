INSERT INTO wallet (id,balance,user_id)
VALUES (1,
        100,
        1) ON CONFLICT DO NOTHING;


INSERT INTO wallet (id,balance,user_id)
VALUES (2,
        100,
        1) ON CONFLICT DO NOTHING;

INSERT INTO wallet (id,balance,user_id)
VALUES (3,
        100,
        2) ON CONFLICT DO NOTHING;


INSERT INTO wallet (id,balance,user_id)
VALUES (4,
        100,
        2) ON CONFLICT DO NOTHING;