CREATE TABLE `shop_hib`.`roles`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `shop_hib`.`user_roles`
(
    `id`     INT AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_userId` FOREIGN KEY (`user_id`) REFERENCES `shop_hib`.`users` (`id`),
    CONSTRAINT `fk_roleId` FOREIGN KEY (`role_id`) REFERENCES `shop_hib`.`roles` (`id`)
);

INSERT INTO `shop_hib`.`roles` (`name`) VALUES ('USER');
INSERT INTO `shop_hib`.`roles` (`name`) VALUES ('ADMIN');

INSERT INTO `shop_hib`.`user_roles` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `shop_hib`.`user_roles` (`user_id`, `role_id`) VALUES ('2', '2');