-- MySQL dump - Base de Datos: ventas_sistema
-- ------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Estructura de tabla para la tabla `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `idCliente` int NOT NULL AUTO_INCREMENT,
  `correo` varchar(20) NOT NULL, -- Revertido a 'correo' por solicitud
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
  PRIMARY KEY (`idCliente`),
  UNIQUE KEY `uk_correo_cliente` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Volcado de datos para la tabla `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES 
(3,'00237851','Pablo','Callejuela Estrecha','ACTIVO'),
(4,'86865956','Antonio','Calle del Abrazo','INACTIVO'),
(6,'08010307','Maria','Calle Mayor','ACTIVO'),
(7,'96451368','Silvi','Avenida de los Sueños','ACTIVO'),
(10,'44277662','Patricia','Calle Mayor','ACTIVO'),
(11,'11478371','Beatriz','Avenida Central','ACTIVO'),
(12,'52827528','Rosa','Calle Mayor','INACTIVO'),
(13,'54198473','Andrés','Calle Principal','INACTIVO'),
(14,'88706068','Mónica','Calle Mayor','INACTIVO'),
(17,'66689819','Gonzalo','Avenida de los Sueños','INACTIVO'),
(18,'80633754','Verónica','Calle Real','ACTIVO'),
(19,'07006552','Ana','Calle del Abrazo','INACTIVO'),
(20,'65032102','David','Callejuela Estrecha','ACTIVO'),
(21,'97709229','Luis','Avenida del Sol','INACTIVO'),
(24,'59946264','Lucía','Paseo Marítimo','ACTIVO'),
(25,'31897418','Ignacio','Avenida de la Paz','ACTIVO'),
(26,'21476435','Hugo','Calle Mayor','INACTIVO'),
(27,'89991515','Hugo','Avenida de la Paz','ACTIVO'),
(28,'22830461','Natalia','Avenida de los Sueños','INACTIVO'),
(31,'07266187','José','Callejón del Arco','ACTIVO'),
(32,'92465782','Cristina','Avenida de la Libertad','ACTIVO'),
(33,'93617628','Pablo','Calle del Recuerdo','ACTIVO'),
(35,'39600486','Elena','Calle Principal','ACTIVO'),
(38,'61543688','Manuel','Calle del Recuerdo','ACTIVO'),
(39,'78114308','Ángel','Calle del Abrazo','INACTIVO'),
(40,'15395113','Rosa','Calle del Recuerdo','INACTIVO'),
(41,'70221336','Laura','Avenida de la Libertad','ACTIVO'),
(42,'28588926','Diego','Avenida Central','ACTIVO'),
(45,'29983726','Victoria','Avenida de la Libertad','ACTIVO'),
(46,'19270002','Eva','Paseo de la Esperanza','ACTIVO'),
(47,'34299557','Manuel','Avenida de la Paz','INACTIVO'),
(48,'79127525','Natalia','Avenida de la Libertad','INACTIVO'),
(49,'41789389','Alberto','Calle Principal','ACTIVO'),
(52,'40905625','Luis','Avenida de la Libertad','ACTIVO'),
(53,'49612549','Patricia','Calle del Recuerdo','INACTIVO'),
(54,'68805019','Natalia','Calle Real','ACTIVO'),
(55,'17347336','Francisco','Avenida Amor','ACTIVO'),
(56,'82677547','Manuel','Avenida del Sol','ACTIVO'),
(59,'44237851','Andres','Los arroyitos','ACTIVO');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Estructura de tabla para la tabla `vendedores`
--

DROP TABLE IF EXISTS `vendedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendedores` (
  `idVendedor` int NOT NULL AUTO_INCREMENT,
  `identificacion` varchar(20) NOT NULL, -- Cambiado de 'dni' a 'identificacion'
  `nombre` varchar(50) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
  `usuario` varchar(20) DEFAULT NULL,
  `rol` enum('VENDEDOR','ADMINISTRADOR') NOT NULL DEFAULT 'VENDEDOR', -- Se agregó el campo 'rol'
  PRIMARY KEY (`idVendedor`),
  UNIQUE KEY `uk_identificacion_vendedor` (`identificacion`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Volcado de datos para la tabla `vendedores`
--

LOCK TABLES `vendedores` WRITE;
/*!40000 ALTER TABLE `vendedores` DISABLE KEYS */;
INSERT INTO `vendedores` VALUES 
(1,'12341234','Jennifer Lee','5551112222','ACTIVO','jennifer1','ADMINISTRADOR'),
(2,'23452345','Daniel Garcia','5553334443','ACTIVO','daniel2','ADMINISTRADOR'),
(3,'34563456','Sophia Rodriguez','5555556666','ACTIVO','sophia3','VENDEDOR'),
(4,'45674567','Michael Smith','5557778888','ACTIVO','michael4','VENDEDOR'),
(5,'56785678','Olivia Hernandez','5559990000','ACTIVO','olivia5','VENDEDOR'),
(6,'67896789','Ethan Martinez','5551213141','ACTIVO','ethan6','VENDEDOR'),
(7,'78907890','Isabella Gonzalez','5553434343','ACTIVO','isabella7','VENDEDOR'),
(8,'89018901','Alexander Perez','5555656565','ACTIVO','alexander8','VENDEDOR'),
(9,'90129012','Mia Sanchez','5557878787','ACTIVO','mia9','VENDEDOR'),
(10,'01230123','William Torres','5559090909','ACTIVO','william10','VENDEDOR'),
(11,'12340123','Samantha Ramirez','5552323232','ACTIVO','samantha11','VENDEDOR'),
(12,'23451234','James Cruz','5554545454','ACTIVO','james12','VENDEDOR'),
(13,'34562345','David Flores','5556767676','ACTIVO','david13','VENDEDOR'),
(14,'45673456','Charlotte Reed','5558989898','ACTIVO','charlotte14','VENDEDOR'),
(15,'56784567','Joseph Stewart','5551212121','ACTIVO','joseph15','VENDEDOR'),
(16,'67895678','Emma Morris','5553434343','ACTIVO','emma16','VENDEDOR'),
(17,'78906789','Benjamin Nguyen','5555656565','ACTIVO','benjamin17','VENDEDOR'),
(18,'89017890','Ava Hughes','5557878787','ACTIVO','ava18','VENDEDOR'),
(19,'90128901','Daniel Bell','5559090909','ACTIVO','daniel19','VENDEDOR'),
(20,'01230012','Madison Cox','5552323232','ACTIVO','madison20','VENDEDOR'),
(21,'09870987','Christopher Wright','5554321098','INACTIVO','chris21','VENDEDOR'),
(22,'98769876','Grace Parker','5556543210','INACTIVO','grace22','VENDEDOR'),
(23,'87658765','Andrew Evans','5558765432','INACTIVO','andrew23','VENDEDOR'),
(24,'76547654','Victoria Richardson','5550987654','INACTIVO','victoria24','VENDEDOR'),
(25,'65436543','Ryan Hill','5552109876','INACTIVO','ryan25','VENDEDOR'),
(26,'54325432','Chloe Bailey','5554321098','INACTIVO','chloe26','VENDEDOR'),
(27,'43214321','Samuel Mitchell','5556543210','INACTIVO','samuel27','VENDEDOR'),
(28,'32103210','Natalie Carter','5558765432','INACTIVO','natalie28','VENDEDOR'),
(29,'21092109','Christopher Perez','5550987654','INACTIVO','chris29','VENDEDOR'),
(30,'10981098','Zoe Edwards','5552109876','INACTIVO','zoe30','VENDEDOR'),
(31,'44994806','Tomas Borghi','3412674629','ACTIVO','chimu','VENDEDOR'),
(32,'34567514','Sophia Lorea','555556692','ACTIVO','sophi4','VENDEDOR');
/*!40000 ALTER TABLE `vendedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Estructura de tabla para la tabla `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `precio` double NOT NULL,
  `existencia` int NOT NULL,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
  PRIMARY KEY (`idProducto`),
  UNIQUE KEY `uk_nombre_producto` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Volcado de datos para la tabla `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES 
(1,'Portátil Dell XPS 15',1899.99,7,'ACTIVO'),
(2,'Smartphone Samsung Galaxy S22',999.99,19,'ACTIVO'),
(3,'Smart TV LG OLED C1 55"',1499.99,12,'ACTIVO'),
(4,'Tablet Apple iPad Pro 12.9"',1099.99,15,'ACTIVO'),
(5,'Auriculares Inalámbricos Sony WH-1000XM4',400,8,'ACTIVO'),
(6,'Consola Sony PlayStation 5',499.99,10,'ACTIVO'),
(7,'Reloj Inteligente Apple Watch Series 7',399.99,29,'ACTIVO'),
(8,'PC de Escritorio HP Pavilion',899.99,8,'ACTIVO'),
(9,'Dron DJI Mavic Air 2',799.99,0,'ACTIVO'),
(10,'Cámara Sony Alpha A7 III',1999.99,5,'ACTIVO'),
(11,'Altavoz Inteligente Amazon Echo',99.99,4,'INACTIVO'),
(12,'Pulsera de Actividad Fitbit Charge 5',149.99,3,'INACTIVO'),
(13,'Auriculares Inalámbricos Apple AirPods Pro',249.99,12,'INACTIVO'),
(14,'Robot Aspirador iRobot Roomba',299.99,6,'INACTIVO'),
(15,'Cámara de Acción GoPro Hero 10',449.99,0,'INACTIVO'),
(16,'Router Inalámbrico TP-Link Archer AX6000',299.99,13,'ACTIVO'),
(17,'SSD Externo Samsung T7 1TB',169.99,22,'ACTIVO'),
(18,'Monitor Dell UltraSharp U2720Q 27"',449.99,0,'ACTIVO'),
(19,'Tarjeta Gráfica NVIDIA GeForce RTX 3080',699.99,8,'ACTIVO'),
(20,'Termostato Inteligente Nest',249.99,15,'ACTIVO'),
(21,'Patinete Eléctrico Xiaomi Mi Pro 2',499.99,10,'ACTIVO'),
(22,'Ratón Inalámbrico Logitech MX Master 3',99.99,0,'ACTIVO'),
(23,'Cargador Portátil Anker PowerCore 26800mAh',59.99,25,'ACTIVO'),
(24,'Visor VR Oculus Quest 2',299.99,18,'ACTIVO'),
(25,'Cámara Compacta Canon PowerShot G7 X',699.99,10,'ACTIVO'),
(26,'Timbre Inteligente Ring Video Pro',199.99,5,'INACTIVO'),
(27,'Teclado Inalámbrico Logitech K780',79.99,8,'INACTIVO'),
(29,'Proyector Portátil Anker Nebula Capsule II',399.99,6,'INACTIVO'),
(30,'Robot Cortacésped Husqvarna Automower',1499.99,7,'INACTIVO'),
(31,'Lector E-Reader Amazon Kindle Paperwhite',129.99,10,'INACTIVO'),
(32,'Ratón Gaming Razer DeathAdder V2',69,15,'INACTIVO'),
(33,'Cámara de Seguridad Arlo Pro 4',249.99,18,'INACTIVO'),
(34,'Báscula Inteligente Withings Body+',79.99,20,'ACTIVO'),
(35,'Monopatín Eléctrico Boosted Stealth',1599.99,8,'INACTIVO');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Estructura de tabla para la tabla `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `idVenta` int NOT NULL AUTO_INCREMENT,
  `idCliente` int DEFAULT NULL,
  `idVendedor` int DEFAULT NULL,
  `numeroDeVenta` varchar(255) NOT NULL,
  `fechaDeVenta` date NOT NULL,
  `total` double NOT NULL,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',
  PRIMARY KEY (`idVenta`),
  KEY `idx_cliente` (`idCliente`),
  KEY `idx_vendedor` (`idVendedor`),
  CONSTRAINT `fk_ventas_cliente` FOREIGN KEY (`idCliente`) REFERENCES `clientes` (`idCliente`),
  CONSTRAINT `fk_ventas_vendedor` FOREIGN KEY (`idVendedor`) REFERENCES `vendedores` (`idVendedor`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Volcado de datos para la tabla `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES 
(2,17,31,'0002','2024-04-06',1049.97,'ACTIVO'),
(3,12,31,'0003','2024-04-06',2599.95,'ACTIVO'),
(4,12,31,'0004','2024-04-06',1349.97,'ACTIVO'),
(5,4,31,'0005','2024-04-07',2099.94,'ACTIVO'),
(6,4,31,'0006','2024-04-08',1749.95,'ACTIVO'),
(7,4,31,'0007','2024-04-09',1749.95,'ACTIVO'),
(8,4,31,'0008','2024-04-01',799.98,'ACTIVO'),
(9,4,31,'0009','2024-04-01',10999.9,'ACTIVO'),
(10,3,31,'0010','2024-04-06',1899.81,'ACTIVO'),
(11,3,31,'0011','2024-04-03',1999.98,'ACTIVO'),
(12,3,31,'0012','2024-04-04',999.99,'ACTIVO'),
(13,5,31,'0013','2024-03-06',1499.99,'ACTIVO'),
(14,5,31,'0014','2024-03-06',1499.99,'ACTIVO'),
(15,5,31,'0015','2024-03-06',1499.99,'ACTIVO'),
(16,5,31,'0016','2024-03-07',4399.96,'ACTIVO'),
(17,5,31,'0017','2024-03-08',4399.96,'ACTIVO'),
(18,4,31,'0018','2024-04-06',699.98,'ACTIVO'),
(19,4,31,'0019','2024-04-16',699.98,'ACTIVO'),
(20,5,31,'0020','2024-03-06',5699.97,'ACTIVO'),
(21,5,31,'0021','2024-03-25',699.98,'ACTIVO'),
(22,14,31,'0022','2023-04-10',6749.85,'ACTIVO'),
(23,25,31,'0023','2024-04-10',199.98,'ACTIVO'),
(24,35,31,'0024','2024-04-10',999.99,'ACTIVO'),
(25,34,31,'0025','2024-04-10',999.99,'ACTIVO'),
(26,4,31,'0026','2024-04-24',12549.85,'ACTIVO'),
(27,10,31,'0027','2024-04-24',36949.69,'ACTIVO');
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Estructura de tabla para la tabla `detalle_ventas`
--

DROP TABLE IF EXISTS `detalle_ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_ventas` (
  `idDetalleVenta` int NOT NULL AUTO_INCREMENT,
  `idVenta` int DEFAULT NULL,
  `idProducto` int DEFAULT NULL,
  `cantidad` int NOT NULL,
  `precioVenta` double NOT NULL,
  PRIMARY KEY (`idDetalleVenta`),
  KEY `idx_producto` (`idProducto`),
  KEY `idx_venta` (`idVenta`),
  CONSTRAINT `fk_detalle_producto` FOREIGN KEY (`idProducto`) REFERENCES `productos` (`idProducto`),
  CONSTRAINT `fk_detalle_venta` FOREIGN KEY (`idVenta`) REFERENCES `ventas` (`idVenta`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Volcado de datos para la tabla `detalle_ventas`
--

LOCK TABLES `detalle_ventas` WRITE;
/*!40000 ALTER TABLE `detalle_ventas` DISABLE KEYS */;
INSERT INTO `detalle_ventas` VALUES 
(2,2,5,3,349.99),
(3,3,7,4,399.99),
(4,3,2,1,999.99),
(5,4,15,3,449.99),
(6,5,5,6,349.99),
(7,6,5,5,349.99),
(8,7,5,5,349.99),
(9,8,7,2,399.99),
(10,9,16,5,299.99),
(11,9,1,5,1899.99),
(12,10,22,19,99.99),
(13,11,2,2,999.99),
(14,12,2,1,999.99),
(15,13,3,1,1499.99),
(16,14,3,1,1499.99),
(17,15,3,1,1499.99),
(18,16,4,4,1099.99),
(19,17,4,4,1099.99),
(20,18,5,2,349.99),
(21,19,5,2,349.99),
(22,20,1,3,1899.99),
(23,21,5,2,349.99),
(24,22,18,12,449.99),
(25,22,15,3,449.99),
(26,23,22,1,99.99),
(27,23,11,1,99.99),
(28,24,2,1,999.99),
(29,25,2,1,999.99),
(30,26,5,7,400),
(31,26,8,10,899.99),
(32,26,12,5,149.99),
(33,27,2,5,999.99),
(34,27,9,15,799.99),
(35,27,1,5,1899.99),
(36,27,10,5,1999.99),
(37,27,15,1,449.99);
/*!40000 ALTER TABLE `detalle_ventas` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;