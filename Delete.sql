-- Διαγραφή δεδομένων από όλους τους πίνακες
DELETE FROM Appointments;
DELETE FROM AvailabilitybyStylist;
DELETE FROM Stylists;
DELETE FROM Services;
DELETE FROM Salons;
DELETE FROM Users;
DELETE FROM Location;

-- Επαναφορά του AUTOINCREMENT για όλους τους πίνακες
DELETE FROM sqlite_sequence WHERE name IN ('Appointments', 'AvailabilitybyStylist', 'Stylists', 'Services', 'Salons', 'Users', 'Location');

