-- Schema that represents the database structure
-- Syntax: SQLite

-- Drop tables if they already exist
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS trainers;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS membership_extensions;
DROP TABLE IF EXISTS bookings;

-- Table: customers
CREATE TABLE IF NOT EXISTS customers
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL
);

-- Table: trainers
CREATE TABLE IF NOT EXISTS trainers
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    salary      REAL NOT NULL
);

-- Table: courses
CREATE TABLE IF NOT EXISTS courses
(
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    name         TEXT    NOT NULL,
    max_capacity INTEGER NOT NULL,
    start_date   TEXT    NOT NULL,
    end_date     TEXT    NOT NULL,
    trainer      TEXT    NOT NULL,
    FOREIGN KEY (trainer) REFERENCES trainers (fiscal_code) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table: memberships
CREATE TABLE IF NOT EXISTS memberships
(
    customer    TEXT PRIMARY KEY,
    valid_from  TEXT NOT NULL,
    valid_until TEXT NOT NULL,
    FOREIGN KEY (customer) REFERENCES customers (fiscal_code) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table: membership_extensions
CREATE TABLE IF NOT EXISTS membership_extensions
(
    customer TEXT    NOT NULL,
    type     TEXT    NOT NULL, -- Type of extension (e.g. "weekend")
    uses     INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (customer, type),
    FOREIGN KEY (customer) REFERENCES memberships (customer) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table: bookings (many-to-many between courses and customers)
CREATE TABLE IF NOT EXISTS bookings
(
    course   INTEGER NOT NULL,
    customer TEXT    NOT NULL,
    PRIMARY KEY (course, customer),
    FOREIGN KEY (course) REFERENCES courses (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (customer) REFERENCES customers (fiscal_code) ON UPDATE CASCADE ON DELETE CASCADE
);
