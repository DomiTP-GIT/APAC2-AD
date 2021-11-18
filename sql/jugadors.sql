CREATE DATABASE  IF NOT EXISTS `BDJocs2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `BDJocs2`;

--
-- Table structure for table `jugador2`
--

DROP TABLE IF EXISTS `jugador2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jugador2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick` varchar(45) DEFAULT NULL,
  `dataRegistre` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugador2`
--

LOCK TABLES `jugador2` WRITE;
/*!40000 ALTER TABLE `jugador2` DISABLE KEYS */;
INSERT INTO `jugador2` VALUES (1,'Billy Mitchell','2018-11-13 00:00:00'),(2,'Steve Wiebe','2018-11-13 00:00:00'),(3,'Hank Chien','2018-11-13 00:00:00');
/*!40000 ALTER TABLE `jugador2` ENABLE KEYS */;
UNLOCK TABLES;
