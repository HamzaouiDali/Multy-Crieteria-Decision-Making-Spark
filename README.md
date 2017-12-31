# Multy-Crieteria-Decision-Making-Spark

before executing this code you have to import this sql script to MySqlWorkbench

CREATE DATABASE `mcdms` /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE TABLE `Users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(80) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` char(41) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

