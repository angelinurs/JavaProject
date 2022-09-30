CREATE SCHEMA `project_Gallery` DEFAULT CHARACTER SET utf8 ;

DROP TABLE `project_Gallery`.`Member`;
CREATE TABLE IF NOT EXISTS `project_Gallery`.`Member` (
	`no` INT NOT NULL AUTO_INCREMENT, -- primary key
	`id` VARCHAR(256) NOT NULL,
    `pw` VARCHAR(256) NOT NULL,
    `name` VARCHAR(256) NOT NULL,
    `age` int not null,
    `phone` VARCHAR(256) NOT NULL,
    `address` VARCHAR(256) NOT NULL,
    `grade` int default 0, -- 회원 등급
    `valid` int default 0, -- 유효 회원
    
    CONSTRAINT `pk_Member` primary key( `no` )
);