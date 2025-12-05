-- Esto es para modificar la base de datos que se tuvo en la versi�n anterior
-- Esto ayudara a mantener la misma base de datos si se parte del folker del maestro

-- Modificaciones en la tabla de vendedores, hace que no se pueda repetir algunos campos de identificaci�n, evitando un problema por si acaso

ALTER TABLE seller ADD COLUMN password varchar(20) AFTER user;
ALTER TABLE seller ADD COLUMN email varchar(255) AFTER name;
ALTER TABLE customer ADD COLUMN email varchar(255) AFTER name;
UPDATE seller SET email = CONCAT(user, '@gmail.com');
UPDATE customer SET email = CONCAT(user, '@gmail.com');

-- Ayuda en busquedas
ALTER TABLE seller ADD unique(dni);
ALTER TABLE seller ADD unique(user);

CREATE TABLE estados IF NOT EXISTS{
`state` enum('ACTIVE','DISACTIVE') DEFAULT 'ACTIVE',}


-- Para calcular m�s facil
ALTER TABLE sales_details ADD COLUMN subtotal DOUBLE DEFAULT NULL AFTER priceSale;
UPDATE sales_details SET subtotal = priceSale * quantity;
