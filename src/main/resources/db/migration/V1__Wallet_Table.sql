create table wallet
(
    id      bigint generated by default as identity,
    balance integer not null,
    primary key (id))
