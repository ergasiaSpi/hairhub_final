-- Εισαγωγή χρηστών (Users)
INSERT INTO Users (username, user_password, email, phone, zipcode, role)
VALUES
('admin1', 'SecurePassword@123', 'admin1@example.com', '2101234567', 12345, 'admin'),
('admin2', 'SecurePassword@456', 'admin2@example.com', '2107654321', 54321, 'admin'),
('customer1', 'StrongPassword#111', 'customer1@example.com', '2101112233', 12345, 'customer'),
('customer2', 'StrongPassword#222', 'customer2@example.com', '2103334455', 54321, 'customer'),
('customer3', 'StrongPassword#333', 'customer3@example.com', '2105556677', 67890, 'customer');

-- Εισαγωγή κομμωτηρίων (Salons)
INSERT INTO Salons (admin_id, name, address, zipcode, phone_number, email)
VALUES
(1, 'Elegant Styles', '123 Main Street', 12345, '2108889990', 'elegantstyles@example.com'),
(2, 'Chic Hair Salon', '456 Oak Avenue', 54321, '2107778889', 'chichair@example.com');

-- Εισαγωγή κομμωτών (Stylists)
INSERT INTO Stylists (stylist_name, salon_id, specializations, shift_start, shift_end)
VALUES
('Maria Papadopoulou', 1, 'Haircut, Coloring', '09:00:00', '17:00:00'),
('Giannis Karalis', 1, 'Styling, Extensions', '10:00:00', '18:00:00'),
('Eleni Georgiou', 2, 'Haircut, Perm', '08:30:00', '16:30:00'),
('Dimitris Nikas', 2, 'Coloring, Highlights', '11:00:00', '19:00:00'),
('Sophia Laskari', 2, 'Braids, Updos', '12:00:00', '20:00:00');

-- Εισαγωγή υπηρεσιών (Services)
INSERT INTO Services (service, service_type, price, duration)
VALUES
('Basic Haircut', 'Standard', 15.00, '00:30:00'),
('Hair Coloring', 'Premium', 40.00, '01:30:00'),
('Hair Styling', 'Standard', 25.00, '01:00:00'),
('Hair Extensions', 'Premium', 60.00, '02:00:00'),
('Perm Treatment', 'Special', 50.00, '01:45:00');

-- Εισαγωγή ραντεβού (Appointments)
--INSERT INTO Appointments (user_id, salon_id, stylist_id, service_id, date, time_start)
--VALUES
--(3, 1, 1, 1, '2025-01-25', '10:00:00'),
--(4, 1, 2, 2, '2025-02-01', '11:00:00'),
--(5, 2, 3, 3, '2025-02-05', '09:30:00'),
--(3, 2, 4, 4, '2025-02-10', '14:00:00'),
--(4, 2, 5, 5, '2025-02-15', '13:00:00');









