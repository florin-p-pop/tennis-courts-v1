insert into guest(id, name) values(null, 'Roger Federer');
insert into guest(id, name) values(null, 'Rafael Nadal');

insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Chatrier');

insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values(null, '2020-12-20T20:00:00.0', '2020-02-20T21:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values(null, '2030-12-01T10:00:00.0', '2030-12-01T11:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values(null, '2030-12-01T12:00:00.0', '2030-12-01T13:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values(null, '2030-12-01T18:00:00.0', '2030-12-01T19:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values(null, '2030-12-01T21:00:00.0', '2030-12-01T22:00:00.0', 1);


insert
    into reservation(id, refund_value, reservation_status, value, guest_id, schedule_id)
    values(null, 0, 0, 10, 1, 1);
insert
    into reservation(id, refund_value, reservation_status, value, guest_id, schedule_id)
    values(null, 10, 0, 10, 1, 2);
insert
    into reservation(id, refund_value, reservation_status, value, guest_id, schedule_id)
    values(null, 10, 0, 10, 1, 3);
