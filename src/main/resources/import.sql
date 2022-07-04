insert into authority (id, "name", status, created_date) values (1, 'ROLE_CUSTOMER', 'ACTIVATED', NOW());
insert into authority (id, "name", status, created_date ) values (2, 'ROLE_EMPLOYEE','ACTIVATED', NOW());
insert into authority (id, "name", status, created_date) values (3, 'ROLE_ADMIN', 'ACTIVATED', NOW());

INSERT into navigation (id,  "name", status,created_date) VALUES(2, 'Đồ Chơi Gỗ', 'ACTIVATED', NOW());

INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(1, 'Đồ Chơi Bảng Tính', 'ACTIVATED', NOW(),2);
INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(2, 'Đồ Chơi Hình Học', 'ACTIVATED', NOW(),2);
INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(3, 'Đồ Chơi Con Vật', 'ACTIVATED', NOW(),2);
INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(4, 'Đồ Chơi Học Tập', 'ACTIVATED', NOW(),2);