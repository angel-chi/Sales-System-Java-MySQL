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
`state` enum('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',}


-- Para calcular m�s facil
ALTER TABLE sales_details ADD COLUMN subtotal DOUBLE DEFAULT NULL AFTER priceSale;
UPDATE sales_details SET subtotal = priceSale * quantity;


---------- Esto ser� para las pruebas de seguridad

ALTER TABLE seller ADD COLUMN nivel enum('ADMIN','CONTADOR', 'VENDEDOR', 'JEFE') DEFAULT 'VENDEDOR' AFTER state;
UPDATE seller SET nivel = 'ADMIN' where dni = '12341234';
UPDATE seller SET nivel = 'CONTADOR' where dni = '23452345';
UPDATE seller SET nivel = 'JEFE' where dni = '34563456';

CREATE TABLE proveedor (
  idProveedor INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(255),
  state ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


CREATE TABLE compras (
  idCompra INT NOT NULL AUTO_INCREMENT,
  idProveedor INT NOT NULL,
  idSeller INT NOT NULL,
  subtotal DOUBLE NOT NULL,
  stateCompra ENUM('CANCELADO','COMPLETADO') DEFAULT 'COMPLETADO',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (idCompra),
  CONSTRAINT compra_fk_proveedor FOREIGN KEY (idProveedor) REFERENCES proveedor(idProveedor),
  CONSTRAINT compra_fk_seller FOREIGN KEY (idSeller) REFERENCES seller(idSeller)
) ENGINE=InnoDB;

CREATE TABLE compra_detalle (
  idCompraDetalle INT NOT NULL AUTO_INCREMENT,
  idCompra INT NOT NULL,
  idProduct INT NOT NULL,
  cantidad INT NOT NULL CHECK (cantidad > 0),
  precioCompra DOUBLE NOT NULL,
  subtotal DOUBLE NOT NULL,
  PRIMARY KEY (idCompraDetalle),
  CONSTRAINT cd_fk_compra FOREIGN KEY (idCompra) REFERENCES compras(idCompra) ON DELETE CASCADE,
  CONSTRAINT cd_fk_product FOREIGN KEY (idProduct) REFERENCES product(idProduct)
) ENGINE=InnoDB;
