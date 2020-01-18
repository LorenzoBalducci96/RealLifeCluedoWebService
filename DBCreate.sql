CREATE DATABASE  IF NOT EXISTS `campus_murder` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `campus_murder`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: campus_murder
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Table structure for table `deniedkills`
--

DROP TABLE IF EXISTS `deniedkills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `deniedkills` (
  `killer` varchar(255) DEFAULT NULL,
  `killed` varchar(255) DEFAULT NULL,
  `session` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `object` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `pending_registrations`
--

DROP TABLE IF EXISTS `pending_registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `pending_registrations` (
  `username` varchar(255) NOT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `confirmation_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `confirmation_code` varchar(255) DEFAULT NULL,
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-18 17:28:42
