-- Permission
INSERT INTO permissions(permission_code, description)
values ('read:staff', 'Read staff'),
       ('write:staff', 'Write staff');

-- Role
INSERT INTO roles(code_name, name)
values ('MANAGER', 'Manager'),
       ('STAFF', 'Staff'),
       ('ACCOUNTANT', 'Accountant');

-- Add permission to role
-- Manager
INSERT INTO role_permission(role_id, permission_id)
values (1, 1),
       (1, 2);

-- Staff
INSERT INTO role_permission(role_id, permission_id)
values (2, 1);

-- Accountant
INSERT INTO role_permission(role_id, permission_id)
values (3, 1);

-- Room type
INSERT INTO room_types(code_name, name)
values ('NORMAL', 'Normal room type'),
       ('VIP', 'VIP room type');

-- Room sample
INSERT INTO rooms(name, type_id)
values ('Room 1', 1),
       ('Room 2', 1),
       ('Room 3', 1),
       ('Room 4', 1),
       ('Room VIP 5', 2),
       ('Room VIP 6', 2);

-- Booking status
INSERT INTO booking_status(status_code, description)
values ('BOOKED', 'The room is already booked'),
       ('DONE', 'Booking done'),
       ('CANCEL', 'Booking has been canceled');
