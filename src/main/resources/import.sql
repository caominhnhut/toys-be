insert into authority (id, "name", status, created_date) values (1, 'ROLE_CUSTOMER', 'ACTIVE', NOW());
insert into authority (id, "name", status, created_date ) values (2, 'ROLE_EMPLOYEE','ACTIVE', NOW());
insert into authority (id, "name", status, created_date) values (3, 'ROLE_ADMIN', 'ACTIVE', NOW());

INSERT into navigation (id,  "name", status,created_date) VALUES(2, 'Đồ Chơi Gỗ', 'ACTIVE', NOW());

INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(1, 'Đồ Chơi Bảng Tính', 'ACTIVE', NOW(),2);
INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(2, 'Đồ Chơi Hình Học', 'ACTIVE', NOW(),2);
INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(3, 'Đồ Chơi Con Vật', 'ACTIVE', NOW(),2);
INSERT into category (id,  "name", status,created_date, navigation_id ) VALUES(4, 'Đồ Chơi Học Tập', 'ACTIVE', NOW(),2);