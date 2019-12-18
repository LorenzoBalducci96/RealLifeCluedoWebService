CREATE DATABASE  IF NOT EXISTS `campus_murder` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `campus_murder`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: campus_murder
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `inscriptions`
--

DROP TABLE IF EXISTS `inscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `inscriptions` (
  `session_name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `points` int(11) DEFAULT NULL,
  `actual_object_target` varchar(255) DEFAULT NULL,
  `actual_user_target` varchar(255) DEFAULT NULL,
  `actual_place_target` varchar(255) DEFAULT NULL,
  KEY `session_name` (`session_name`),
  KEY `username` (`username`),
  KEY `actual_object_target` (`actual_object_target`),
  KEY `actual_user_target` (`actual_user_target`),
  CONSTRAINT `inscriptions_ibfk_1` FOREIGN KEY (`session_name`) REFERENCES `sessions` (`session_name`),
  CONSTRAINT `inscriptions_ibfk_2` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `inscriptions_ibfk_3` FOREIGN KEY (`actual_object_target`) REFERENCES `objects` (`object_name`),
  CONSTRAINT `inscriptions_ibfk_4` FOREIGN KEY (`actual_user_target`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscriptions`
--

LOCK TABLES `inscriptions` WRITE;
/*!40000 ALTER TABLE `inscriptions` DISABLE KEYS */;
INSERT INTO `inscriptions` VALUES ('first murder','io',790,'bicchiere','lorenzo','place7'),('first murder','lorenzo',0,'bustina di zucchero','io','place7'),('prova6','lorenzo3',0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `inscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kills`
--

DROP TABLE IF EXISTS `kills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `kills` (
  `killer` varchar(255) DEFAULT NULL,
  `killed` varchar(255) DEFAULT NULL,
  `session` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `object` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  KEY `killer` (`killer`),
  KEY `killed` (`killed`),
  KEY `session` (`session`),
  KEY `place` (`place`),
  KEY `object` (`object`),
  CONSTRAINT `kills_ibfk_1` FOREIGN KEY (`killer`) REFERENCES `users` (`username`),
  CONSTRAINT `kills_ibfk_2` FOREIGN KEY (`killed`) REFERENCES `users` (`username`),
  CONSTRAINT `kills_ibfk_3` FOREIGN KEY (`session`) REFERENCES `sessions` (`session_name`),
  CONSTRAINT `kills_ibfk_4` FOREIGN KEY (`place`) REFERENCES `places` (`place_name`),
  CONSTRAINT `kills_ibfk_5` FOREIGN KEY (`object`) REFERENCES `objects` (`object_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kills`
--

LOCK TABLES `kills` WRITE;
/*!40000 ALTER TABLE `kills` DISABLE KEYS */;
INSERT INTO `kills` VALUES ('io','lorenzo','first murder','place5','bustina di the','2019-12-17 23:19:22'),('io','io','first murder','place10','aereo di carta','2019-12-17 23:41:38'),('io','io','first murder','place10','aereo di carta','2019-12-17 23:57:29'),('io','io','first murder','place10','aereo di carta','2019-12-17 23:58:11'),('io','io','first murder','place10','aereo di carta','2019-12-18 00:01:31'),('io','lorenzo','first murder','place5','bustina di the','2019-12-18 00:02:58');
/*!40000 ALTER TABLE `kills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objects`
--

DROP TABLE IF EXISTS `objects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `objects` (
  `object_name` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `multiplicator` float DEFAULT NULL,
  PRIMARY KEY (`object_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objects`
--

LOCK TABLES `objects` WRITE;
/*!40000 ALTER TABLE `objects` DISABLE KEYS */;
INSERT INTO `objects` VALUES ('accendino','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/accendino.jpg','assolutamente non acceso ',1),('aereo di carta','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/aereo di carta.jpg','',1),('asciugacapelli ','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/asciugacapelli .jpg','(non necessariamente acceso)',1.2),('banconota da 10â¬','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/banconota da 10 euro.jpg','deve essere perforza da 10â¬',1.1),('bicchiere','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/bicchiere.jpg','',0.9),('bottiglia','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/bottiglia.jpg','',1.1),('bustina di the','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/bustina di te.jpg','',0.9),('bustina di zucchero','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/bustina di zucchero.jpg','',0.8),('fazzoletti','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/fazzoletti.jpg','un pacco di fazzoletti',0.7),('ferro da stiro','/campus_murder_web_server/SERVER_DATA/OBJECTS_IMAGES/ferro_da_stiro.jpg','un ferro da stiro (non necessariamente acceso)',1.4);
/*!40000 ALTER TABLE `objects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objects_on_session`
--

DROP TABLE IF EXISTS `objects_on_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `objects_on_session` (
  `object_name` varchar(255) DEFAULT NULL,
  `session_name` varchar(255) DEFAULT NULL,
  KEY `object_name` (`object_name`),
  KEY `session_name` (`session_name`),
  CONSTRAINT `objects_on_session_ibfk_1` FOREIGN KEY (`object_name`) REFERENCES `objects` (`object_name`),
  CONSTRAINT `objects_on_session_ibfk_2` FOREIGN KEY (`session_name`) REFERENCES `sessions` (`session_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objects_on_session`
--

LOCK TABLES `objects_on_session` WRITE;
/*!40000 ALTER TABLE `objects_on_session` DISABLE KEYS */;
INSERT INTO `objects_on_session` VALUES ('accendino','first murder'),('bicchiere','first murder'),('bustina di the','first murder'),('bustina di the','first murder'),('bustina di zucchero','first murder'),('banconota da 10â¬','first murder'),('accendino','first murder'),('banconota da 10â¬','first murder'),('bicchiere','first murder'),('bottiglia','first murder'),('banconota da 10â¬','prova6');
/*!40000 ALTER TABLE `objects_on_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `places`
--

DROP TABLE IF EXISTS `places`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `places` (
  `place_name` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `multiplicator` float DEFAULT NULL,
  PRIMARY KEY (`place_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `places`
--

LOCK TABLES `places` WRITE;
/*!40000 ALTER TABLE `places` DISABLE KEYS */;
INSERT INTO `places` VALUES ('palestra','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder.jpg','la palestra al terzo piano',1.6),('place1','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place10','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place11','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place2','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place3','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place4','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place5','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place6','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place7','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1),('place8','/campus_murder_web_server/SERVER_DATA/PLACES_IMAGES/Folder3.jpg','aaa',1);
/*!40000 ALTER TABLE `places` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `places_on_session`
--

DROP TABLE IF EXISTS `places_on_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `places_on_session` (
  `place_name` varchar(255) DEFAULT NULL,
  `session_name` varchar(255) DEFAULT NULL,
  KEY `place_name` (`place_name`),
  KEY `session_name` (`session_name`),
  CONSTRAINT `places_on_session_ibfk_1` FOREIGN KEY (`place_name`) REFERENCES `places` (`place_name`),
  CONSTRAINT `places_on_session_ibfk_2` FOREIGN KEY (`session_name`) REFERENCES `sessions` (`session_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `places_on_session`
--

LOCK TABLES `places_on_session` WRITE;
/*!40000 ALTER TABLE `places_on_session` DISABLE KEYS */;
INSERT INTO `places_on_session` VALUES ('place5','first murder'),('place7','first murder'),('place5','first murder'),('place5','first murder'),('place5','first murder'),('palestra','first murder'),('place2','prova6');
/*!40000 ALTER TABLE `places_on_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rejected_kills`
--

DROP TABLE IF EXISTS `rejected_kills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `rejected_kills` (
  `killer` varchar(255) DEFAULT NULL,
  `killed` varchar(255) DEFAULT NULL,
  `session` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `object` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  KEY `killer` (`killer`),
  KEY `killed` (`killed`),
  KEY `session` (`session`),
  KEY `place` (`place`),
  KEY `object` (`object`),
  CONSTRAINT `rejected_kills_ibfk_1` FOREIGN KEY (`killer`) REFERENCES `users` (`username`),
  CONSTRAINT `rejected_kills_ibfk_2` FOREIGN KEY (`killed`) REFERENCES `users` (`username`),
  CONSTRAINT `rejected_kills_ibfk_3` FOREIGN KEY (`session`) REFERENCES `sessions` (`session_name`),
  CONSTRAINT `rejected_kills_ibfk_4` FOREIGN KEY (`place`) REFERENCES `places` (`place_name`),
  CONSTRAINT `rejected_kills_ibfk_5` FOREIGN KEY (`object`) REFERENCES `objects` (`object_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rejected_kills`
--

LOCK TABLES `rejected_kills` WRITE;
/*!40000 ALTER TABLE `rejected_kills` DISABLE KEYS */;
/*!40000 ALTER TABLE `rejected_kills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sessions` (
  `session_name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  PRIMARY KEY (`session_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES ('first murder','nessuna descrizione','2019-11-22 12:30:30','2020-01-10 12:30:30'),('prova6','kjasb','2019-12-11 01:07:00','2019-12-25 01:08:00');
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('io','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder3.jpg','6cf92c9dbb709dbcded52d0efea7a7dd05133d6ae0d0c34d897b38cf855449258ad3a559b8ae4823f97e16470cfb60901aa8a8a0ff2cb3de8cbf2a948969a8c4'),('lorenzo','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo10','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo2','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo3','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo4','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo5','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo6','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo7','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo8','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a'),('lorenzo9','/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES/Folder.jpg','4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waiting_confirmation_kills`
--

DROP TABLE IF EXISTS `waiting_confirmation_kills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `waiting_confirmation_kills` (
  `killer` varchar(255) DEFAULT NULL,
  `killed` varchar(255) DEFAULT NULL,
  `session` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `object` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  KEY `killer` (`killer`),
  KEY `killed` (`killed`),
  KEY `session` (`session`),
  KEY `place` (`place`),
  KEY `object` (`object`),
  CONSTRAINT `waiting_confirmation_kills_ibfk_1` FOREIGN KEY (`killer`) REFERENCES `users` (`username`),
  CONSTRAINT `waiting_confirmation_kills_ibfk_2` FOREIGN KEY (`killed`) REFERENCES `users` (`username`),
  CONSTRAINT `waiting_confirmation_kills_ibfk_3` FOREIGN KEY (`session`) REFERENCES `sessions` (`session_name`),
  CONSTRAINT `waiting_confirmation_kills_ibfk_4` FOREIGN KEY (`place`) REFERENCES `places` (`place_name`),
  CONSTRAINT `waiting_confirmation_kills_ibfk_5` FOREIGN KEY (`object`) REFERENCES `objects` (`object_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waiting_confirmation_kills`
--

LOCK TABLES `waiting_confirmation_kills` WRITE;
/*!40000 ALTER TABLE `waiting_confirmation_kills` DISABLE KEYS */;
/*!40000 ALTER TABLE `waiting_confirmation_kills` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-18 13:19:22
