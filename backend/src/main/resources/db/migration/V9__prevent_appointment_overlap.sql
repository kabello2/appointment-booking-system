CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE appointments
    ADD CONSTRAINT ex_consultant_schedule_overlap
    EXCLUDE USING gist
(
    consultant_id WITH =,

    tsrange(
        (appointment_date + start_time)::timestamp,
        (appointment_date + end_time)::timestamp,
        '[)'
    ) WITH &&
)
WHERE (
    status <> 'CANCELLED'
);

ALTER TABLE appointments
    ADD CONSTRAINT ex_customer_schedule_overlap
    EXCLUDE USING gist
(
    customer_id WITH =,

    tsrange(
        (appointment_date + start_time)::timestamp,
        (appointment_date + end_time)::timestamp,
        '[)'
    ) WITH &&
)
WHERE (
    status <> 'CANCELLED'
);