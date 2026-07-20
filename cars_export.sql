-- MySQL dump 10.13  Distrib 9.7.1, for macos15 (arm64)
--
-- Host: localhost    Database: CarDatabase
-- ------------------------------------------------------
-- Server version	9.7.1

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'eada248e-7f33-11f1-9e6c-98bff7981394:1-47';

--
-- Table structure for table `Cars`
--

DROP TABLE IF EXISTS `Cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Cars` (
  `id` int NOT NULL AUTO_INCREMENT,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `topspeed` int DEFAULT NULL,
  `horsepower` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_make_model` (`make`,`model`)
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Cars`
--

LOCK TABLES `Cars` WRITE;
/*!40000 ALTER TABLE `Cars` DISABLE KEYS */;
INSERT INTO `Cars` VALUES (1,'Nissan','Altima','Japan',188,332),(2,'Audi','R8','Germany',174,521),(3,'Ferrari','Roma','Italy',219,600),(4,'Tesla','Model X','USA',148,161),(5,'Rolls-Royce','Cullinan','United Kingdom',205,609),(6,'Chevrolet','Impala','USA',147,435),(7,'Koenigsegg','Regera','Sweden',218,757),(8,'Genesis','G70','South Korea',155,279),(9,'Hyundai','Ioniq 5','South Korea',155,540),(10,'Kia','Telluride','South Korea',200,290),(11,'Ford','Focus RS','USA',145,498),(12,'Honda','Integra','Japan',192,285),(13,'Chevrolet','Corvette Stingray','USA',188,522),(14,'Volvo','C40','Sweden',118,136),(15,'Toyota','Corolla','Japan',182,493),(16,'Skoda','Octavia RS','Czech Republic',145,141),(17,'Dodge','Durango','USA',156,520),(18,'Bugatti','Veyron','France',217,910),(19,'Rolls-Royce','Phantom','United Kingdom',203,708),(20,'Chevrolet','Camaro ZL1','USA',199,526),(21,'Volvo','XC90','Sweden',122,211),(22,'Ford','Mustang GT','USA',159,454),(23,'Kia','EV6','South Korea',182,441),(24,'Toyota','Supra','Japan',168,514),(25,'Nissan','Leaf','Japan',168,311),(26,'BMW','M4','Germany',155,365),(27,'Porsche','Taycan','Germany',192,773),(28,'SEAT','Ateca','Spain',111,195),(29,'Mazda','MX-5 Miata','Japan',175,367),(30,'Mitsubishi','Eclipse','Japan',177,362),(31,'Lotus','Emira','United Kingdom',140,286),(32,'Mercedes-Benz','C63 AMG','Germany',185,280),(33,'Opel','Corsa','Germany',124,128),(34,'Lamborghini','Revuelto','Italy',218,616),(35,'Hyundai','Tucson','South Korea',195,419),(36,'Porsche','Cayman GT4','Germany',192,863),(37,'Toyota','Camry','Japan',155,392),(38,'Maserati','Ghibli','Italy',211,848),(39,'Chrysler','300 SRT','USA',123,189),(40,'Lexus','LFA','Japan',148,542),(41,'Tesla','Model Y','USA',146,180),(42,'Nissan','370Z','Japan',155,492),(43,'Hyundai','Veloster N','South Korea',191,458),(44,'Audi','RS3','Germany',152,298),(45,'Ferrari','SF90 Stradale','Italy',193,937),(46,'BMW','M5','Germany',167,431),(47,'Alfa Romeo','Giulia Quadrifoglio','Italy',167,460),(48,'BMW','320i','Germany',169,277),(49,'Tesla','Cybertruck','USA',116,127),(50,'BMW','X5 M','Germany',165,423),(51,'Ferrari','Portofino','Italy',215,655),(52,'McLaren','GT','United Kingdom',197,698),(53,'Jaguar','XE','United Kingdom',152,524),(54,'BMW','i8','Germany',168,321),(55,'Nissan','Skyline','Japan',167,343),(56,'Bentley','Bentayga','United Kingdom',198,836),(57,'Aston Martin','DB11','United Kingdom',197,638),(58,'Chrysler','Pacifica','USA',138,190),(59,'Koenigsegg','Jesko','Sweden',193,625),(60,'Subaru','BRZ','Japan',181,526),(61,'McLaren','Artura','United Kingdom',216,607),(62,'Mercedes-Benz','E63 AMG','Germany',145,371),(63,'Nissan','GT-R','Japan',150,458),(64,'McLaren','P1','United Kingdom',205,846),(65,'Subaru','Legacy','Japan',153,455),(66,'Maserati','Quattroporte','Italy',218,630),(67,'Porsche','Panamera','Germany',195,794),(68,'Tesla','Model 3','USA',110,169),(69,'Volvo','V60','Sweden',126,220),(70,'Toyota','Land Cruiser','Japan',190,482),(71,'Alfa Romeo','Stelvio','Italy',158,466),(72,'Peugeot','208','France',145,204),(73,'Ford','GT','USA',185,499),(74,'Holden','Commodore','Australia',119,144),(75,'Kia','Stinger GT','South Korea',158,361),(76,'Hyundai','Sonata','South Korea',143,546),(77,'Lamborghini','Huracan','Italy',213,877),(78,'Renault','Megane RS','France',113,215),(79,'Lotus','Exige','United Kingdom',160,279),(80,'Mitsubishi','Outlander','Japan',143,549),(81,'Jaguar','XF','United Kingdom',170,507),(82,'Mazda','RX-7','Japan',198,521),(83,'Bentley','Continental GT','United Kingdom',195,629),(84,'Rolls-Royce','Ghost','United Kingdom',220,860),(85,'Honda','Accord','Japan',145,345),(86,'Audi','Q7','Germany',144,284),(87,'Lexus','RC F','Japan',183,370),(88,'Volkswagen','GTI','Germany',165,311),(89,'Hyundai','Elantra N','South Korea',200,541),(90,'Chevrolet','Silverado','USA',155,546),(91,'Dodge','Charger SRT','USA',178,270),(92,'Mazda','CX-5','Japan',179,291),(93,'McLaren','720S','United Kingdom',203,936),(94,'Audi','A4','Germany',177,539),(95,'Jaguar','I-Pace','United Kingdom',173,411),(96,'SEAT','Ibiza','Spain',126,146),(97,'Aston Martin','Valkyrie','United Kingdom',211,966),(98,'Honda','CR-V','Japan',160,372),(99,'Alfa Romeo','4C','Italy',156,452),(100,'Aston Martin','DBS Superleggera','United Kingdom',194,943),(101,'Genesis','GV80','South Korea',181,403),(102,'Lamborghini','Urus','Italy',204,761),(103,'Mitsubishi','Lancer Evolution','Japan',199,287),(104,'Volkswagen','Golf R','Germany',140,484),(105,'Bentley','Flying Spur','United Kingdom',209,888),(106,'Peugeot','308','France',116,129),(107,'Opel','Astra','Germany',144,147),(108,'Koenigsegg','Agera','Sweden',206,735),(109,'Porsche','911 Turbo S','Germany',194,778),(110,'Dodge','Challenger SRT Hellcat','USA',196,285),(111,'McLaren','570S','United Kingdom',218,725),(112,'Audi','RS6 Avant','Germany',163,395),(113,'Audi','TT RS','Germany',150,474),(114,'Maserati','Levante','Italy',216,878),(115,'Ford','GT500','USA',185,404),(116,'Holden','Monaro','Australia',149,203),(117,'Honda','S2000','Japan',173,254),(118,'Dodge','Viper','USA',182,533),(119,'Jaguar','F-Type R','United Kingdom',159,303),(120,'Nissan','Silvia','Japan',200,318),(121,'Mercedes-Benz','AMG GT','Germany',156,309),(122,'Jeep','Wrangler','USA',116,215),(123,'Ferrari','296 GTB','Italy',207,679),(124,'Toyota','GR Yaris','Japan',157,394),(125,'Maserati','MC20','Italy',209,707),(126,'Lexus','IS500','Japan',185,425),(127,'Porsche','Macan','Germany',196,951),(128,'Volkswagen','Tiguan','Germany',180,385),(129,'Ferrari','F8 Tributo','Italy',206,850),(130,'Subaru','WRX STI','Japan',156,276),(131,'Lamborghini','Aventador','Italy',192,924),(132,'Renault','Alpine A110','France',137,155),(133,'SEAT','Leon','Spain',112,120),(134,'Volkswagen','Beetle','Germany',161,316),(135,'Bugatti','Chiron','France',210,734),(136,'Ford','F-150 Raptor','USA',150,476),(137,'Genesis','G80','South Korea',175,468),(138,'Volkswagen','Passat','Germany',175,254),(139,'Peugeot','3008','France',117,129),(140,'Ford','Bronco','USA',200,326),(141,'Toyota','GR86','Japan',174,268),(142,'Mercedes-Benz','G-Wagon','Germany',193,439),(143,'Aston Martin','Vantage','United Kingdom',208,882),(144,'Cadillac','Escalade-V','USA',119,175),(145,'Fiat','500','Italy',118,125),(146,'Bugatti','Divo','France',199,786),(147,'Tesla','Model S Plaid','USA',112,165),(148,'Volvo','S60','Sweden',123,207),(149,'Skoda','Kodiaq','Czech Republic',125,205),(150,'Lotus','Evora','United Kingdom',146,431),(151,'Jeep','Grand Cherokee Trackhawk','USA',145,172),(152,'Cadillac','CT5-V Blackwing','USA',149,215),(153,'Mazda','CX-9','Japan',149,371),(154,'Honda','Civic Type R','Japan',195,333),(155,'Honda','NSX','Japan',191,340),(156,'Fiat','Panda','Italy',136,123),(157,'Renault','Clio','France',121,214),(158,'Mazda','Mazda3','Japan',199,420),(159,'Kia','Forte','South Korea',190,460),(160,'Subaru','Outback','Japan',191,377),(161,'Lexus','LC500','Japan',157,331),(162,'Cadillac','CT4-V','USA',116,168),(163,'BMW','M3','Germany',195,269),(164,'Mercedes-Benz','S-Class','Germany',194,490),(165,'Toyota','Prius','Japan',154,352),(166,'Subaru','Forester','Japan',192,485),(167,'Skoda','Superb','Czech Republic',132,159),(168,'Nissan','Altima Sport','Japan',192,366),(169,'Audi','R8 Sport','Germany',154,262),(170,'Ferrari','Roma Sport','Italy',211,698),(171,'Tesla','Model X Sport','USA',135,162),(172,'Rolls-Royce','Cullinan Sport','United Kingdom',198,635),(173,'Chevrolet','Impala Sport','USA',189,392),(174,'Koenigsegg','Regera Sport','Sweden',201,928),(175,'Genesis','G70 Sport','South Korea',172,454),(176,'Hyundai','Ioniq 5 Sport','South Korea',183,524),(177,'Kia','Telluride Sport','South Korea',161,264),(178,'Ford','Focus RS Sport','USA',147,383),(179,'Honda','Integra Sport','Japan',151,547),(180,'Chevrolet','Corvette Stingray Sport','USA',156,269),(181,'Volvo','C40 Sport','Sweden',116,196),(182,'Toyota','Corolla Sport','Japan',167,426),(183,'Skoda','Octavia RS Sport','Czech Republic',130,175),(184,'Dodge','Durango Sport','USA',178,511),(185,'Bugatti','Veyron Sport','France',193,797),(186,'Rolls-Royce','Phantom Sport','United Kingdom',218,895),(187,'Chevrolet','Camaro ZL1 Sport','USA',152,380),(188,'Volvo','XC90 Sport','Sweden',112,210),(189,'Ford','Mustang GT Sport','USA',167,250),(190,'Kia','EV6 Sport','South Korea',173,525),(191,'Toyota','Supra Sport','Japan',183,350),(192,'Nissan','Leaf Sport','Japan',163,470),(193,'BMW','M4 Sport','Germany',144,419),(194,'Porsche','Taycan Sport','Germany',209,760),(195,'SEAT','Ateca Sport','Spain',117,212),(196,'Mazda','MX-5 Miata Sport','Japan',197,403),(197,'Mitsubishi','Eclipse Sport','Japan',172,408),(198,'Lotus','Emira Sport','United Kingdom',182,459),(199,'Mercedes-Benz','C63 AMG Sport','Germany',160,456),(200,'Opel','Corsa Sport','Germany',128,190);
/*!40000 ALTER TABLE `Cars` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-19 18:52:01
