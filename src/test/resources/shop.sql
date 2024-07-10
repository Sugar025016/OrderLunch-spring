-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: orderLunch
-- ------------------------------------------------------
-- Server version	8.0.36-0ubuntu0.20.04.1

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
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `description` varchar(512) NOT NULL,
  `is_delete` varchar(11) NOT NULL DEFAULT '0',
  `is_disable` varchar(11) NOT NULL DEFAULT '0',
  `is_orderable` varchar(11) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `file_data` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `shop_address_id` int DEFAULT NULL,
  `delivery_km` double DEFAULT NULL,
  `delivery_price` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique_constraint` (`name`),
  KEY `FK9btfrf82rdxqf1mo926wfdf5m` (`file_data`),
  KEY `FKj97brjwss3mlgdt7t213tkchl` (`user_id`),
  KEY `FKnlnk36gfxcdw1e8tydlje6pu9` (`shop_address_id`),
  CONSTRAINT `FK9btfrf82rdxqf1mo926wfdf5m` FOREIGN KEY (`file_data`) REFERENCES `file_data` (`id`),
  CONSTRAINT `FKj97brjwss3mlgdt7t213tkchl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnlnk36gfxcdw1e8tydlje6pu9` FOREIGN KEY (`shop_address_id`) REFERENCES `shop_address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` VALUES (1,'2023-08-21 17:08:22.000000','2024-06-10 11:34:56.751484','description55555555555666','0','1','1','valueA','1234567890',47,1,44,10.1,300),(2,'2023-08-21 17:08:22.000000','2024-02-02 09:48:45.555869','description商店介紹','0','1','1','valueB','1234561230',2,1,2,8,0),(3,'2023-08-21 17:08:22.000000','2024-02-02 18:31:24.354599','description','0','0','0','valueC','1234567890',15,2,3,10,0),(4,'2023-08-21 17:08:22.000000','2024-02-02 18:31:33.354041','description','0','0','1','valueD','1234567891',9,2,4,5,0),(5,'2023-08-21 17:08:22.000000','2024-02-02 18:31:41.154197','description','0','0','0','newNAME','1234567891',3,2,5,10,0),(6,'2023-08-27 23:44:23.420175','2024-02-02 18:31:48.874443','AA','0','0','0','AA','0123456789',14,2,6,10,0),(7,'2023-09-02 14:41:17.050733','2023-10-05 01:04:13.658866','RRR','1','1','1','RRR','0123456789',16,1,7,NULL,0),(22,'2024-01-19 10:08:05.520255','2024-02-02 18:29:07.430529','NNNN','0','0','0','NN小店','1234567890',NULL,1,22,5,0),(23,'2024-01-19 10:11:03.238639','2024-02-02 09:42:11.534287','NNNN','0','1','1','NN小店A','1234567890',57,1,23,100,0),(24,'2024-01-19 10:11:56.000614','2024-02-02 18:29:28.129114','NNNN','0','0','0','NN小店GG','1234567890',NULL,1,24,11,0),(25,'2024-01-19 10:20:51.119786','2024-02-02 18:26:44.017847','NNNN','0','0','0','NN小店YY','1234567890',56,1,25,20,0),(27,'2024-01-25 01:05:56.336765','2024-02-02 18:29:37.141195','NNNN','0','0','0','NN小店-東東店','1234567890',NULL,1,27,9,0);
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;



