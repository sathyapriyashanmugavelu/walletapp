
INSERT INTO usertable (id,user_name,password)
VALUES (1,
        'seed-user-1',
        '$2a$10$KTrsvlBoTQSmmYz9KVrcjek9hvke1wQllQzTZW8./AUP2.g6HM8u6') ON CONFLICT DO NOTHING;


INSERT INTO usertable (id,user_name,password)
VALUES (2,
        'seed-user-2',
        '$2a$10$hJA1PPHIv5atOlcqYNatcuox09UX8ZSnb/MGGWHbmuFAJCY53jTii') ON CONFLICT DO NOTHING;