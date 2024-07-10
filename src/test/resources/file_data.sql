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
-- Table structure for table `file_data`
--

DROP TABLE IF EXISTS `file_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content_type` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `original_file_name` varchar(255) NOT NULL,
  `suffix` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_data`
--

LOCK TABLES `file_data` WRITE;
/*!40000 ALTER TABLE `file_data` DISABLE KEYS */;
INSERT INTO `file_data` VALUES (1,'image/jpeg','1691994708919221.jpg','images.jpeg','.jpg'),(2,'image/png','1691994708919221.jpg','pngtree-halloween-ghost-jack-olantern-png-image_6127297.png','.jpg'),(3,'image/png','1691922420438552.jpg','pngtree-halloween-ghost-jack-olantern-png-image_6127298.png','.jpg'),(4,'image/jpeg','16927208415324378.jpg','150元便當.jpg','.jpg'),(5,'image/jpeg','16927208506911241.jpg','豬腳麵線.jpg','.jpg'),(6,'image/jpeg','16927208600983447.jpg','香烤圓鱈+柳葉魚.jpg','.jpg'),(7,'image/png','16927208415324378.jpg','pngtree-halloween-ghost-jack-olantern-png-image_6127298.png','.jpg'),(8,'image/jpeg','16931471371861382.jpg','9999999.jpg','.jpg'),(9,'image/jpeg','16931471959794482.jpg','20230627233234.jpg','.jpg'),(10,'image/jpeg','16931472134942111.jpg','豬腳麵線.jpg','.jpg'),(11,'image/jpeg','16931472267843470.jpg','200元便當.jpg','.jpg'),(12,'image/jpeg','16931473729955148.jpg','7862809.jpg','.jpg'),(13,'image/jpeg','16931496809474747.JPG','1542296 (1).JPG','.JPG'),(14,'image/jpeg','16931510622787524.JPG','1542296.JPG','.JPG'),(15,'image/jpeg','16932027106902359.jpg','7862841.jpg','.jpg'),(16,'image/jpeg','16936368755369222.jpg','20230627233234.jpg','.jpg'),(17,'image/jpeg','16937478489864516.JPG','1542296.JPG','.JPG'),(18,'image/jpeg','16937490913911053.jpg','1542304.jpg','.jpg'),(19,'image/jpeg','16937491499963563.jpg','1542304.jpg','.jpg'),(20,'image/jpeg','16937493330698504.jpg','1542304.jpg','.jpg'),(21,'image/jpeg','16937496051274089.JPG','1542296.JPG','.JPG'),(22,'image/jpeg','16937496725718676.jpg','1542304.jpg','.jpg'),(23,'image/jpeg','16937508048951735.JPG','1542296.JPG','.JPG'),(24,'image/jpeg','16937530392244768.jpg','1542304.jpg','.jpg'),(25,'image/jpeg','16937534051121005.jpg','7862804.jpg','.jpg'),(26,'image/jpeg','16937534818651466.jpg','200元便當.jpg','.jpg'),(27,'image/jpeg','16937928747765036.jpg','120元便當.jpg','.jpg'),(28,'image/jpeg','16939174106862309.JPG','1542296 (1).JPG','.JPG'),(29,'image/jpeg','16939174771997428.jpg','7862841.jpg','.jpg'),(30,'image/jpeg','16939174834635530.jpg','7862805.jpg','.jpg'),(31,'image/jpeg','16961402023922460.jpg','16922736932692872.jpg','.jpg'),(32,'image/jpeg','16961402279163407.jpg','16919947487523906.jpg','.jpg'),(33,'image/jpeg','1697535442327396.jpg','16937534051121005.jpg','.jpg'),(34,'image/jpeg','16975355135036248.jpg','16937490913911053.jpg','.jpg'),(35,'image/jpeg','16975357231398642.JPG','16939174106862309.JPG','.JPG'),(36,'image/jpeg','16975358280463132.jpg','16937530392244768.jpg','.jpg'),(37,'image/jpeg','16975358537908712.JPG','16939174106862309.JPG','.JPG'),(38,'image/jpeg','16998996373984526.jpg','fotor-ai-20231113212727.jpg','.jpg'),(39,'image/jpeg','16998996649719305.jpg','fotor-ai-20231113212738.jpg','.jpg'),(40,'image/jpeg','17011707929039408.jpg','fotor-ai-2023111423344.jpg','.jpg'),(41,'image/jpeg','17011709849918422.jpg','d3150a76e7f13eb5027dbcbc08e3a30e.jpg','.jpg'),(42,'image/jpeg','17011709894898376.jpg','82b7a67994bc19b0c742eb50270382eb.jpg','.jpg'),(43,'image/jpeg','17011710022555156.jpg','82b7a67994bc19b0c742eb50270382eb (2).jpg','.jpg'),(44,'image/jpeg','17011710319562291.jpg','food-home.jpg','.jpg'),(45,'image/jpeg','17011710414371117.jpg','82b7a67994bc19b0c742eb50270382eb (2).jpg','.jpg'),(46,'image/jpeg','1701592289330194.jpg','food-home.jpg','.jpg'),(47,'image/jpeg','17015922948981374.jpg','fotor-ai-2023111423344.jpg','.jpg'),(48,'image/jpeg','1701593018418979.jpg','7862843.jpg','.jpg'),(49,'image/jpeg','1701610930812453.jpg','7862805.jpg','.jpg'),(50,'image/jpeg','1701610936793498.jpg','7862802.jpg','.jpg'),(51,'image/jpeg','17016266749194599.jpg','fotor-ai-20231113212727.jpg','.jpg'),(52,'image/jpeg','17022352341575325.jpg','7862802.jpg','.jpg'),(53,'image/jpeg','17022352466716077.jpg','fotor-ai-2023111423344.jpg','.jpg'),(54,'image/jpeg','17030872440322021.jpg','7862808.jpg','.jpg'),(55,'image/jpeg','1703087267729605.jpg','7862802.jpg','.jpg'),(56,'image/jpeg','17056308714787091.jpeg','780.jpeg','.jpeg'),(57,'image/png','17068381300029626.png','780.png','.png'),(58,'image/png','17112932190367038.png','780.png','.png'),(59,'image/jpeg','17112932447839280.jpg','豬腳麵線.jpg','.jpg'),(60,'image/jpeg','171129331244410000.jpg','7862831.jpg','.jpg'),(61,'image/jpeg','17112935660281598.jpg','7862833.jpg','.jpg'),(62,'image/jpeg','17112938526551883.jpeg','imagesTest.jpeg','.jpeg'),(63,'image/jpeg','17201410888876328.jpeg','780.jpeg','.jpeg');
/*!40000 ALTER TABLE `file_data` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-10 17:15:32
