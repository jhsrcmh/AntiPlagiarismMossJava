/*
SQLyog Community Edition- MySQL GUI v6.04
Host - 5.5.8-log : Database - anti-plagiarism
*********************************************************************
Server version : 5.5.8-log
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `anti-plagiarism`;

USE `anti-plagiarism`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `result` */

DROP TABLE IF EXISTS `result`;

CREATE TABLE `result` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `fileA` varchar(100) DEFAULT NULL,
  `fileB` varchar(100) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `sameLines` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
