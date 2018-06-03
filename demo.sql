-- MySQL dump 10.13  Distrib 5.7.22, for Linux (x86_64)
--
-- Host: localhost    Database: demo
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer_questions`
--

DROP TABLE IF EXISTS `answer_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer_questions` (
  `question_id` int(11) NOT NULL,
  `answer_id` int(11) NOT NULL,
  KEY `fk_answer_questions_1_idx` (`answer_id`),
  KEY `fk_answer_questions_2_idx` (`question_id`),
  CONSTRAINT `fk_answer_questions_1` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_answer_questions_2` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_questions`
--

LOCK TABLES `answer_questions` WRITE;
/*!40000 ALTER TABLE `answer_questions` DISABLE KEYS */;
INSERT INTO `answer_questions` VALUES (1,1),(1,2),(1,3),(2,4),(2,5),(2,6),(2,7),(2,8),(3,10),(3,11),(3,12),(3,13),(3,14),(5,15),(5,16),(5,17),(5,18),(8,19),(8,20),(8,21),(8,22),(8,23),(8,24);
/*!40000 ALTER TABLE `answer_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answers`
--

DROP TABLE IF EXISTS `answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `answer_id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answers`
--

LOCK TABLES `answers` WRITE;
/*!40000 ALTER TABLE `answers` DISABLE KEYS */;
INSERT INTO `answers` VALUES (1,'Supervisor'),(2,'Coworker'),(3,'Subordinate'),(4,'Always'),(5,'Most of the time'),(6,'About half the time'),(7,'Never'),(8,'Once in a while'),(9,'4'),(10,'Extremly available'),(11,'Very available'),(12,'Moderately available'),(13,'Slightly available'),(14,'Not at all available'),(15,'0-4'),(16,'5-9'),(17,'10-19'),(18,'20 or more'),(19,' Productivity apps (calendar, to do list, price checker, etc.)'),(20,' Utility apps (calculate, convert, translate, etc.)'),(21,' Entertainment apps (movie trailers, celebrity gossip, radio station guides, etc.)'),(22,'   News apps (local news, national headlines, technology announcements, etc.)'),(23,'   News apps (local news, national headlines, technology announcements, etc.)'),(24,' Travel apps (airplane tickets, tourist guides, public transportation info, etc.)'),(25,'200'),(26,'444'),(27,'5'),(28,'3'),(29,'55'),(30,'66');
/*!40000 ALTER TABLE `answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant_answers`
--

DROP TABLE IF EXISTS `participant_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant_answers` (
  `participant_id` int(11) NOT NULL,
  `answer_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_participant_answers_1_idx` (`answer_id`),
  KEY `fk_participant_answers_2_idx` (`question_id`),
  KEY `fk_participant_answers_3_idx` (`participant_id`),
  CONSTRAINT `fk_participant_answers_1` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_participant_answers_2` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_participant_answers_3` FOREIGN KEY (`participant_id`) REFERENCES `participants` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant_answers`
--

LOCK TABLES `participant_answers` WRITE;
/*!40000 ALTER TABLE `participant_answers` DISABLE KEYS */;
INSERT INTO `participant_answers` VALUES (56,15,5,33),(56,25,6,34),(56,26,7,35),(56,19,8,36),(56,20,8,37),(56,21,8,38),(56,22,8,39),(57,17,5,40),(57,27,6,41),(57,28,7,42),(57,19,8,43),(57,20,8,44),(60,16,5,45),(61,15,5,46),(61,29,6,47),(61,30,7,48),(61,19,8,49),(61,20,8,50),(61,22,8,51),(61,23,8,52);
/*!40000 ALTER TABLE `participant_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participants`
--

DROP TABLE IF EXISTS `participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `participant_id_UNIQUE` (`id`),
  KEY `fk_participants_1_idx` (`user_id`),
  KEY `fk_participants_2_idx` (`survey_id`),
  CONSTRAINT `fk_participants_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_participants_2` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participants`
--

LOCK TABLES `participants` WRITE;
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` VALUES (56,5,3),(57,5,3),(58,5,3),(59,5,3),(60,5,3),(61,5,3);
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` tinytext NOT NULL,
  `description` tinytext,
  `type` enum('single','multiple','number') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `question_id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'Whom would you like to evaluate?','You can select more options','multiple'),(2,'How often is your coworker late to work?',NULL,'single'),(3,'How likely is it that you would recommend your coworker to a colleague?','Rate from 1 to 10','number'),(4,'How available to employees is your supervisor?',NULL,'single'),(5,'How many apps do you currently have on your Facebook account?',NULL,'single'),(6,'In a typical week, about how many songs or other music files do you download from the internet?',NULL,'number'),(7,'In a typical week, about how much money, in U.S. dollars, do you spend on songs or other music files you download from the internet?',NULL,'number'),(8,'Which type of apps do you currently have on your digital devices (computer, tablets, phones, etc.)?','Check all that apply','multiple');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey_questions`
--

DROP TABLE IF EXISTS `survey_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `survey_questions` (
  `survey_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  KEY `fk_survey_questions_1_idx` (`question_id`),
  KEY `fk_survey_questions_2_idx` (`survey_id`),
  CONSTRAINT `fk_survey_questions_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_survey_questions_2` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey_questions`
--

LOCK TABLES `survey_questions` WRITE;
/*!40000 ALTER TABLE `survey_questions` DISABLE KEYS */;
INSERT INTO `survey_questions` VALUES (3,1),(3,2),(3,3),(5,5),(5,6),(5,7),(5,8);
/*!40000 ALTER TABLE `survey_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveys`
--

DROP TABLE IF EXISTS `surveys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surveys` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `survey_id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveys`
--

LOCK TABLES `surveys` WRITE;
/*!40000 ALTER TABLE `surveys` DISABLE KEYS */;
INSERT INTO `surveys` VALUES (3,'360-degree Employee Evaluation Template',NULL),(5,'Mobile Apps Survey','If you develop apps or advertise on them, it’s important to get to know how people use apps. Our apps template was created by experts to get you the answers you need to inform your app creation or advertising efforts. Ask people questions from the types of apps they use most, to how frequently they use smartphone or tablet apps.');
/*!40000 ALTER TABLE `surveys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (3,'Almin','Karic','almin@fet.ba'),(4,'mike','kane','mikekane@gmail.com'),(5,'aaa','bbb','ccc'),(6,'aa','bb','cc');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-04  1:25:07
