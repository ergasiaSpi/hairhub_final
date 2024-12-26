PRAGMA foreign_keys = ON;

CREATE TABLE Users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    user_password TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    phone TEXT NOT NULL,
    Postal_Code INTEGER NOT NULL,
    role TEXT 
);

CREATE TABLE Salons (
    salon_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    location TEXT NOT NULL,
    phone_number TEXT NOT NULL,
    email TEXT,
    rating REAL CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Stylists (
    stylist_id INTEGER PRIMARY KEY AUTOINCREMENT,
    stylist_name TEXT NOT NULL,
    salon_id INTEGER NOT NULL,
    specializations TEXT,
    shift_start TIME NOT NULL,
    shift_end TIME NOT NULL,
    FOREIGN KEY (salon_id) REFERENCES Salons(salon_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Services (
    service_id INTEGER PRIMARY KEY AUTOINCREMENT,
    service TEXT NOT NULL,
    service_type TEXT,
    price REAL NOT NULL,
    duration INTEGER NOT NULL -- σε λεπτά
);

CREATE TABLE AvailabilitybyStylist (
    availability_id INTEGER PRIMARY KEY AUTOINCREMENT,
    stylist_id INTEGER NOT NULL,
    appoint_date DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
    FOREIGN KEY (stylist_id) REFERENCES Stylists(stylist_id) ON DELETE CASCADE,
    UNIQUE (stylist_id, appoint_date, time_start)
);

CREATE TABLE Appointments (
    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    salon_id INTEGER NOT NULL,
    stylist_id INTEGER,
    service_id INTEGER NOT NULL,
    date DATE NOT NULL,
    time_start TIME NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('pending', 'confirmed', 'canceled')) DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (salon_id) REFERENCES Salons(salon_id) ON DELETE CASCADE,
    FOREIGN KEY (stylist_id) REFERENCES Stylists(stylist_id) ON DELETE SET NULL,
    FOREIGN KEY (service_id) REFERENCES Services(service_id) ON DELETE CASCADE
);

CREATE TABLE Reviews (
    review_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    salon_id INTEGER NOT NULL,
    stylist_id INTEGER,
    rating INTEGER CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (salon_id) REFERENCES Salons(salon_id) ON DELETE CASCADE,
    FOREIGN KEY (stylist_id) REFERENCES Stylists(stylist_id) ON DELETE SET NULL
);



