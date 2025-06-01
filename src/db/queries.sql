CREATE TABLE IF NOT EXISTS users (
     nume VARCHAR PRIMARY KEY,
     pass VARCHAR,
     role VARCHAR,
     varsta INT
    -- bilete, notificari, eventuri_sugerate vor fi in alte tabele cu FK la user
);

CREATE TABLE IF NOT EXISTS events (
      nume VARCHAR PRIMARY KEY,
      data DATE,
      descriere TEXT,
      locatie VARCHAR,
      numarBileteDisponibile INT,
      capacitateTotala INT,
      organizator VARCHAR,
      price NUMERIC
    -- lineup va fi intr-o tabela separata cu FK la event
);

CREATE TABLE IF NOT EXISTS platas (
      cod_unic VARCHAR PRIMARY KEY,
      suma NUMERIC,
      ultimelecifrecard VARCHAR,
      status VARCHAR
);

CREATE TABLE IF NOT EXISTS bilets (
      codUnic VARCHAR PRIMARY KEY,
      eventName VARCHAR,
      cumparator VARCHAR,
      valid BOOLEAN,
      tip VARCHAR,
      plata VARCHAR,
      FOREIGN KEY (cumparator) REFERENCES users(nume),
      FOREIGN KEY (plata) REFERENCES platas(cod_unic)
);

CREATE TABLE IF NOT EXISTS artists(

);

CREATE TABLE IF NOT EXISTS notificares(
      text VARCHAR,
      emitator VARCHAR,
      receptor VARCHAR,
      data DATE,
      FOREIGN KEY (receptor) REFERENCES users(nume) /*fac doar pentru receptor ca sa am acea lista de notificari primite*/
);

CREATE TABLE IF NOT EXISTS recenzies(
    autor VARCHAR,
    text VARCHAR,
    rating INT,
    FOREIGN KEY (autor) REFERENCES users(nume)
);

CREATE TABLE IF NOT EXISTS onedaypasss(
    codUnic VARCHAR PRIMARY KEY,
    eventName VARCHAR,
    cumparator VARCHAR,
    valid BOOLEAN,
    tip VARCHAR,
    plata VARCHAR,
    ziAcces DATE,
    includeCamping BOOLEAN,
    FOREIGN KEY (cumparator) REFERENCES users(nume),
    FOREIGN KEY (plata) REFERENCES platas(cod_unic)
);

INSERT INTO users (nume, varsta, pass, role)
VALUES ('robertanechita', 21, 'roberta1', 'admin')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('biancap', 21, 'b11nc', 'user')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('iriiina', 21, '12345', 'user')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('ioanafl', 21, 'florea12', 'user')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('alex1', 21, 'alx12', 'user')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('amalia01', 21, 'ama1', 'user')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('ionuttte', 21, 'f12ionut', 'user')
ON CONFLICT (nume) DO NOTHING;

INSERT INTO users (nume, varsta, pass, role)
VALUES ('mariaioana', 21, 'maria1', 'user')
ON CONFLICT (nume) DO NOTHING;


INSERT INTO events (nume, data, descriere, locatie, numarBileteDisponibile, capacitateTotala, organizator, price)
VALUES ('Untold', '2025-07-15', 'Va asteptam la cel mai tare festival din acest an!', 'Cluj', 10000, 10000, 'untoldfestivals', 539.99)
ON CONFLICT (nume) DO NOTHING;

INSERT INTO events (nume, data, descriere, locatie, numarBileteDisponibile, capacitateTotala, organizator, price)
VALUES ('Neversea', '2025-06-15', 'Va asteptam la cel mai tare festival din acest an!', 'Constanta', 10000, 10000, 'untoldfestivals', 339.99)
ON CONFLICT (nume) DO NOTHING;

INSERT INTO events (nume, data, descriere, locatie, numarBileteDisponibile, capacitateTotala, organizator, price)
VALUES ('Beach-Please', '2025-07-02', 'Va asteptam la cel mai tare festival din acest an!', 'Costinesti', 10000, 10000, 'SMN', 639.99)
ON CONFLICT (nume) DO NOTHING;

INSERT INTO events (nume, data, descriere, locatie, numarBileteDisponibile, capacitateTotala, organizator, price)
VALUES ('Nunta De Proba', '2025-05-30', 'Va asteptam la nunta de proba din acest an!', 'Bucuresti', 100, 100, 'nuntiBucuresti', 139.99)
ON CONFLICT (nume) DO NOTHING;
