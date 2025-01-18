-- Απενεργοποίηση των foreign keys για να αποφύγεις προβλήματα με τις διαγραφές
PRAGMA foreign_keys = OFF;

-- Διαγραφή του πίνακα Appointments (εξαρτάται από τους άλλους πίνακες)
DROP TABLE IF EXISTS Appointments;

-- Διαγραφή του πίνακα AvailabilitybyStylist
DROP TABLE IF EXISTS AvailabilitybyStylist;

-- Διαγραφή του πίνακα Services
DROP TABLE IF EXISTS Services;

-- Διαγραφή του πίνακα Stylists
DROP TABLE IF EXISTS Stylists;

-- Διαγραφή του πίνακα Salons
DROP TABLE IF EXISTS Salons;

-- Διαγραφή του πίνακα Users
DROP TABLE IF EXISTS Users;

-- Διαγραφή του πίνακα Location
DROP TABLE IF EXISTS Location;

-- Επαναφορά των foreign keys
PRAGMA foreign_keys = ON;




