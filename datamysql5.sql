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
INSERT INTO role_permission(role_id, permission_id) value (5, 5);
#- write staff
INSERT INTO role_permission(role_id, permission_id) value (5, 15);

#Staff
# - read staff
INSERT INTO role_permission(role_id, permission_id) value (15, 5);

#Accountant
# - read staff
INSERT INTO role_permission(role_id, permission_id) value (15, 5);
-- ---------------------------------------------------------------------------------------------------------------------
#Staffs
# *gender 0: male, 1:female
# *default pass: 123456
# *status 0: disable, 1: enable
INSERT INTO staffs(username, password, first_name, last_name, gender, address1, address2, phone_number, email, status,
                   role_id)
VALUES ('staff', '$2a$10$G/LBPAq6EUpbON1WY8nLOu8wjnE3QIh/2VOniCUoQmFoqPNUGJ5Je', 'staff first name', 'staff last name',
        0, 'ho chi minh', 'ha noi', '0123456781', 'staff@gmail.com', 1, 15),
       ('manager', '$2a$10$G/LBPAq6EUpbON1WY8nLOu8wjnE3QIh/2VOniCUoQmFoqPNUGJ5Je', 'manager first name',
        'manager last name', 1, 'ho chi minh', 'ha noi', '0123456782', 'manager@gmail.com', 1, 5),
       ('accountant', '$2a$10$G/LBPAq6EUpbON1WY8nLOu8wjnE3QIh/2VOniCUoQmFoqPNUGJ5Je', 'accountant first name',
        'accountant last name', 1, 'ho chi minh', 'ha noi', '0123456783', 'accountant@gmail.com', 1, 25);
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
values ('Room 1', 'ENABLE', 5),
       ('Room 2', 'ENABLE', 5),
       ('Room 3', 'ENABLE', 5),
       ('Room 4', 'ENABLE', 5),
       ('Room VIP 5', 'ENABLE', 15),
       ('Room VIP 6', 'ENABLE', 15);
-- ---------------------------------------------------------------------------------------------------------------------
#Booking status
# status success flow: pending -> booked -> done
# status fail flow: pending -> cancel
INSERT INTO booking_status(code_name, description)
values ('BOOKED', 'Ph??ng ???? ???????c ?????t, hi???n ??ang d??ng'),
       ('PENDING', 'Ph??ng ?????t tr?????c nh??ng ch??a x??c nh???n'),
       ('DONE', '???? ho??n th??nh ?????t ph??ng'),
       ('CANCEL', 'Tr???ng th??i ?????t ph??ng l?? hu???');
-- ---------------------------------------------------------------------------------------------------------------------
#Products
INSERT INTO products(name, description, price, status)
VALUES ('Kh??n ?????t', 'Kh??n ?????t', 5000, 1),
       ('Kh??n kh??', 'Kh??n kh??', 2000, 1),
       ('Tr??i c??y 1', 'Tr??i c??y combo 1', 100000, 1),
       ('Tr??i c??y 2', 'Tr??i c??y combo 2', 150000, 1),
       ('Bia Tiger th?????ng', 'Bia tiger xanh', 12000, 1),
       ('Bia Tiger b???c', 'Bia tiger b???c', 15000, 1);
-- ---------------------------------------------------------------------------------------------------------------------
#Order status
INSERT INTO order_status(code_name, description)
VALUES ('DONE', 'done'),
       ('PENDING', 'pending'),
       ('CANCEL', 'cancel');
-- ---------------------------------------------------------------------------------------------------------------------
COMMIT;
