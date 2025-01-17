INSERT INTO Users (username, user_password, email, phone, zipcode, role)
VALUES ('adminnn1', 'passworD1$1', 'admin1@example.com', '+932-093-0984', 11111, 'admin'),
       ('customer1', 'passworD2#2', 'customer1@example.com', '+934-032-0922', 22222, 'customer');

INSERT INTO Salons (admin_id, name, address, zipcode, phone_number, email)
VALUES (1, 'HairHub Downtown', '123 Main St', '11111', '1234567890', 'downtown@hairhub.com');

-- Προσθήκη stylists
INSERT INTO Stylists (stylist_name, salon_id, specializations, shift_start, shift_end)
VALUES ('Stylist A', 1, 'Haircut, Color', '09:00:00', '17:00:00'),
       ('Stylist B', 1, 'Styling, Highlights', '10:00:00', '18:00:00');

-- Προσθήκη υπηρεσιών
INSERT INTO Services (service, service_type, price, duration)
VALUES ('Haircut', 'Basic', 20.00, '00:30:00'),
       ('Coloring', 'Premium', 50.00, '01:00:00');
