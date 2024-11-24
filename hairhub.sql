CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    user_password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'customer') NOT NULL
);

CREATE TABLE Salons (
    salon_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    location VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    rating DECIMAL(2, 1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Stylists (
    stylist_id INT AUTO_INCREMENT PRIMARY KEY,
    stylist_name VARCHAR(100) NOT NULL,
    salon_id INT NOT NULL,
    specializations TEXT,
    shift_start TIME NOT NULL,
    shift_end TIME NOT NULL,
    FOREIGN KEY (salon_id) REFERENCES Salons(salon_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    service VARCHAR(100) NOT NULL,
    service_type TEXT,
    price DECIMAL(10, 2) NOT NULL,
    duration INT NOT NULL -- διάρκεια σε λεπτά
);

CREATE TABLE AvailabilitybyStylist (
    stylist_id INT PRIMARY KEY NOT NULL,
    availability_id INT AUTO_INCREMENT ,
    appoint_date DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
    FOREIGN KEY (stylist_id) REFERENCES Stylists(stylist_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    salon_id INT NOT NULL,
    stylist_id INT NOT NULL,
    service_id INT NOT NULL,
    date DATE NOT NULL,
    time_start TIME NOT NULL,
    status ENUM('pending', 'confirmed', 'canceled') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    FOREIGN KEY (salon_id) REFERENCES Salons(salon_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    FOREIGN KEY (stylist_id) REFERENCES Stylists(stylist_id)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Services(service_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    salon_id INT NOT NULL,
    stylist_id INT NOT NULL,
    rating TINYINT CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    FOREIGN KEY (salon_id) REFERENCES Salons(salon_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    FOREIGN KEY (stylist_id) REFERENCES Stylists(stylist_id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);


