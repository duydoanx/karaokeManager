START TRANSACTION;
#Permission
INSERT INTO permissions(permission_code, description)
values ('read:staff', 'Read staff'),
       ('write:staff', 'Write staff');
-- ---------------------------------------------------------------------------------------------------------------------
#Role
INSERT INTO roles(code_name, name)
values ('MANAGER', 'Manager'),
       ('STAFF', 'Staff'),
       ('ACCOUNTANT', 'Accountant');
-- ---------------------------------------------------------------------------------------------------------------------
#Add permission to role
# Manager
# - read staff
INSERT INTO role_permission(role_id, permission_id) value (1, 1);
#- write staff
INSERT INTO role_permission(role_id, permission_id) value (1, 2);

#Staff
# - read staff
INSERT INTO role_permission(role_id, permission_id) value (2, 1);

#Accountant
# - read staff
INSERT INTO role_permission(role_id, permission_id) value (3, 1);
-- ---------------------------------------------------------------------------------------------------------------------
#Staffs
# *gender 0: male, 1:female
# *default pass: 123456
# *status 0: disable, 1: enable
INSERT INTO staffs(username, password, first_name, last_name, gender, address1, address2, phone_number, email, status,
                   role_id)
VALUES ('staff', '$2a$10$G/LBPAq6EUpbON1WY8nLOu8wjnE3QIh/2VOniCUoQmFoqPNUGJ5Je', 'staff first name', 'staff last name',
        0, 'ho chi minh', 'ha noi', '0123456781', 'staff@gmail.com', 1, 2),
       ('manager', '$2a$10$G/LBPAq6EUpbON1WY8nLOu8wjnE3QIh/2VOniCUoQmFoqPNUGJ5Je', 'manager first name',
        'manager last name', 1, 'ho chi minh', 'ha noi', '0123456782', 'manager@gmail.com', 1, 1),
       ('accountant', '$2a$10$G/LBPAq6EUpbON1WY8nLOu8wjnE3QIh/2VOniCUoQmFoqPNUGJ5Je', 'accountant first name',
        'accountant last name', 1, 'ho chi minh', 'ha noi', '0123456783', 'accountant@gmail.com', 1, 3);
-- ---------------------------------------------------------------------------------------------------------------------
#Guests
# *gender 0: male, 1:female
# *status 0: disable, 1: enable
INSERT INTO guests(first_name, last_name, gender, address1, address2, phone_number, email, status)
VALUES ('guest first name 1', 'guest last name 1', 0, 'ha noi', 'sai gon', '0123456771', 'guest1@gmail.com', 1),
       ('guest first name 2', 'guest last name 2', 1, 'ha noi', 'sai gon', '0123456772', 'guest2@gmail.com', 1);
-- ---------------------------------------------------------------------------------------------------------------------
#Room type
INSERT
INTO room_types(code_name, name, price)
values ('NORMAL', 'Normal room type', 60000),
       ('VIP', 'VIP room type', 100000);
-- ---------------------------------------------------------------------------------------------------------------------
#Room sample
INSERT INTO rooms(name, status_code, type_id)
values ('Room 1', 'ENABLE', 1),
       ('Room 2', 'ENABLE', 1),
       ('Room 3', 'ENABLE', 1),
       ('Room 4', 'ENABLE', 1),
       ('Room VIP 5', 'ENABLE', 2),
       ('Room VIP 6', 'ENABLE', 2);
-- ---------------------------------------------------------------------------------------------------------------------
#Booking status
# status success flow: pending -> booked -> done
# status fail flow: pending -> cancel
INSERT INTO booking_status(code_name, description)
values ('BOOKED', 'Phòng đã được đặt, hiện đang dùng'),
       ('PENDING', 'Phòng đặt trước nhưng chưa xác nhận'),
       ('DONE', 'Đã hoàn thành đặt phòng'),
       ('CANCEL', 'Trạng thái đặt phòng là huỷ');
-- ---------------------------------------------------------------------------------------------------------------------
#Products
INSERT INTO products(name, description, price, status)
VALUES ('Khăn ướt', 'Khăn ướt', 5000, 1),
       ('Khăn khô', 'Khăn khô', 2000, 1),
       ('Trái cây 1', 'Trái cây combo 1', 100000, 1),
       ('Trái cây 2', 'Trái cây combo 2', 150000, 1),
       ('Bia Tiger thường', 'Bia tiger xanh', 12000, 1),
       ('Bia Tiger bạc', 'Bia tiger bạc', 15000, 1);
-- ---------------------------------------------------------------------------------------------------------------------
#Order status
INSERT INTO order_status(code_name, description)
VALUES ('DONE', 'done'),
       ('PENDING', 'pending'),
       ('CANCEL', 'cancel');
-- ---------------------------------------------------------------------------------------------------------------------
COMMIT;
