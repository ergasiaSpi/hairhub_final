-- Εισαγωγή δεδομένων στον πίνακα Users (ισχυρός κωδικός πρόσβασης και σωστή μορφή τηλεφώνου)
INSERT INTO Users (username, user_password, email, phone, zipcode, role)
VALUES 
('john_doe', 'Str0ng#Pass123', 'john.doe@example.com', '+690-123-4567', '10001', 'customer'),
('admin_user', 'Adm1n$ecureP@ss', 'admin@example.com', '+690-987-6543', '10001', 'admin');

-- Εισαγωγή δεδομένων στον πίνακα Salons
INSERT INTO Salons (admin_id, name, address, zipcode, phone_number, email)
VALUES 
(2, 'Hairhub Downtown', '123 Main Street', '10001', '+690-111-2222', 'downtown@hairhub.com'),
(2, 'Hairhub Uptown', '456 Elm Street', '10002', '+690-333-4444', 'uptown@hairhub.com');

-- Εισαγωγή δεδομένων στον πίνακα Stylists
INSERT INTO Stylists (stylist_name, salon_id, specializations, shift_start, shift_end)
VALUES 
('Alice Johnson', 1, 'Haircut, Coloring', '09:00:00', '17:00:00'),
('Bob Smith', 2, 'Shaving, Beard Styling', '10:00:00', '18:00:00'),
('Catherine Lee', 1, 'Perm, Straightening', '12:00:00', '20:00:00');

-- Εισαγωγή δεδομένων στον πίνακα Services με το stylist_id
INSERT INTO Services (stylist_id, service, service_type, price, duration)
VALUES 
(1, 'Basic Haircut', 'Haircut', 20.00, '00:30:00'),    -- Στην υπηρεσία "Basic Haircut", ο stylist είναι η Alice Johnson (stylist_id 1)
(1, 'Hair Coloring', 'Coloring', 50.00, '01:00:00'),     -- Στην υπηρεσία "Hair Coloring", ο stylist είναι η Alice Johnson (stylist_id 1)
(2, 'Beard Styling', 'Shaving', 15.00, '00:20:00'),       -- Στην υπηρεσία "Beard Styling", ο stylist είναι ο Bob Smith (stylist_id 2)
(3, 'Hair Straightening', 'Treatment', 80.00, '02:00:00'); -- Στην υπηρεσία "Hair Straightening", ο stylist είναι η Catherine Lee (stylist_id 3)
