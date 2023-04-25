-- Schema that represents the database structure
-- Syntax: SQLite

-- Table: customers
CREATE TABLE customers
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL
);

-- Table: trainers
CREATE TABLE trainers
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    salary      REAL NOT NULL
);

-- Table: courses
CREATE TABLE courses
(
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    name                TEXT    NOT NULL,
    max_capacity        INTEGER NOT NULL,
    start_date          TEXT    NOT NULL,
    end_date            TEXT    NOT NULL,
    trainer_fiscal_code TEXT    NOT NULL,
    FOREIGN KEY (trainer_fiscal_code) REFERENCES trainers (fiscal_code)
);

-- Table: memberships
CREATE TABLE memberships
(
    fiscal_code TEXT PRIMARY KEY,
    uses        INTEGER NOT NULL DEFAULT 0,
    price       REAL    NOT NULL DEFAULT 0,
    valid_from  TEXT    NOT NULL,
    valid_until TEXT    NOT NULL,
    FOREIGN KEY (fiscal_code) REFERENCES customers (fiscal_code)
);

-- Table: membership_decorators
CREATE TABLE membership_decorators
(
    customer  TEXT NOT NULL,
    decorator TEXT NOT NULL, -- Type of decorator (e.g. "weekend")
    PRIMARY KEY (customer, decorator),
    FOREIGN KEY (customer) REFERENCES memberships (fiscal_code),
    FOREIGN KEY (decorator) REFERENCES membership_decorators (customer)
);

-- Table: bookings (many-to-many between courses and customers)
CREATE TABLE bookings
(
    course   INTEGER NOT NULL,
    customer TEXT    NOT NULL,
    PRIMARY KEY (course, customer),
    FOREIGN KEY (course) REFERENCES courses (id),
    FOREIGN KEY (customer) REFERENCES customers (fiscal_code)
);
