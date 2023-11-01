DROP SCHEMA IF EXISTS shop_hib;
CREATE SCHEMA IF NOT EXISTS shop_hib;
USE shop_hib;

DROP TABLE IF EXISTS `shop_hib`.`users`;
CREATE TABLE `shop_hib`.`users`
(
    `id`           INT         NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(45) NOT NULL,
    `surname`      VARCHAR(45) NOT NULL,
    `birthday`     DATE        NULL,
    `email`        VARCHAR(45) NOT NULL,
    `password`     VARCHAR(45) NOT NULL,
    `balance`      INT         NULL,
    `address`      VARCHAR(45) NOT NULL,
    `phone_number` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
    UNIQUE INDEX `surname_UNIQUE` (`surname` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
);

DROP TABLE IF EXISTS `shop_hib`.`categories`;
CREATE TABLE `shop_hib`.`categories`
(
    `id`         INT         NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(45) NOT NULL,
    `image_path` VARCHAR(75) NOT NULL,
    `rating`     INT         NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
);

DROP TABLE IF EXISTS `shop_hib`.`orders`;
CREATE TABLE `shop_hib`.`orders`
(
    `id`      INT      NOT NULL AUTO_INCREMENT,
    `date`    DATETIME NOT NULL,
    `user_id` INT      NOT NULL,
    `price`   INT      NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id` (`id` ASC),
    CONSTRAINT fk_orders_user_id_users_id
        FOREIGN KEY (`user_id`)
            REFERENCES `shop_hib`.`users` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `shop_hib`.`products`;
CREATE TABLE `shop_hib`.`products`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45)  NOT NULL,
    `description` VARCHAR(100) NOT NULL,
    `price`       INT          NOT NULL,
    `category_id` INT          NOT NULL,
    `image_path`  VARCHAR(75)  NOT NULL,
    UNIQUE INDEX `id` (`id` ASC),
    PRIMARY KEY (`id`),
    CONSTRAINT fk_products_category_id_categories_id
        FOREIGN KEY (`category_id`)
            REFERENCES `shop_hib`.`categories` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `shop_hib`.`images`;
CREATE TABLE `shop_hib`.`images`
(
    `id`          INT         NOT NULL AUTO_INCREMENT,
    `image_path`  VARCHAR(45) NOT NULL,
    `category_id` INT         NULL,
    `product_id`  INT         NULL,
    `primary`     INT         NOT NULL,
    UNIQUE INDEX `id` (`id` ASC),
    PRIMARY KEY (`id`),
    CONSTRAINT fk_images_category_id_categories_id
        FOREIGN KEY (`category_id`)
            REFERENCES `shop_hib`.`categories` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_images_product_id_products_id
        FOREIGN KEY (`product_id`)
            REFERENCES `shop_hib`.`products` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `shop_hib`.`orders_products`;
CREATE TABLE `shop_hib`.`orders_products`
(
    `id`         INT NOT NULL AUTO_INCREMENT,
    `order_id`   INT NULL,
    `product_id` INT NULL,
    `quantity`   INT NOT NULL DEFAULT 0,
    UNIQUE INDEX `id` (`id` ASC),
    PRIMARY KEY (`id`),
    CONSTRAINT fk_orders_products_order_id_orders_id
        FOREIGN KEY (`order_id`)
            REFERENCES `shop_hib`.`orders` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_orders_products_product_id_products_id
        FOREIGN KEY (`product_id`)
            REFERENCES `shop_hib`.`products` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

INSERT INTO `shop_hib`.`categories` (`name`, `image_path`, `rating`)
VALUES ('Мобильные телефоны', 'iphone.jpg', '1');
INSERT INTO `shop_hib`.`categories` (`name`, `image_path`, `rating`)
VALUES ('Наушники', 'airpods.jpg', '2');
INSERT INTO `shop_hib`.`categories` (`name`, `image_path`, `rating`)
VALUES ('Планшеты', 'ipad.jpg', '1');
INSERT INTO `shop_hib`.`categories` (`name`, `image_path`, `rating`)
VALUES ('Моноблоки', 'mac.jpg', '3');
INSERT INTO `shop_hib`.`categories` (`name`, `image_path`, `rating`)
VALUES ('Ноутбуки', 'macbook.jpg', '1');
INSERT INTO `shop_hib`.`categories` (`name`, `image_path`, `rating`)
VALUES ('Часы', 'watch.jpg', '4');
INSERT INTO `shop_hib`.`products` (`name`, `description`, `price`, `category_id`, `image_path`)
VALUES ('Iphone 13', 'Хороший телефон', '800', '1', '13.jpg');
INSERT INTO `shop_hib`.`products` (`name`, `description`, `price`, `category_id`, `image_path`)
VALUES ('Iphone 14', 'Отличный телефон', '900', '1', '14.jpg');
INSERT INTO `shop_hib`.`products` (`name`, `description`, `price`, `category_id`, `image_path`)
VALUES ('Iphone 4', 'Создовал Джобс', '500', '1', '4.jpg');
INSERT INTO `shop_hib`.`users` (`name`, `surname`, `birthday`, `email`, `password`, `balance`, `address`,
                                `phone_number`)
VALUES ('A', 'B', '2024-10-10', 'aa@aa.aa', '12345678', '100', 'Minsk', '+375295555555');








