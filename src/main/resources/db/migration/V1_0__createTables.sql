USE shop_hib;

CREATE TABLE `shop_hib`.`users`
(
    `id`           INT         NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(45) NOT NULL,
    `surname`      VARCHAR(45) NOT NULL,
    `birthday`     DATE        NULL,
    `email`        VARCHAR(45) NOT NULL,
    `password`     VARCHAR(65) NOT NULL,
    `balance`      INT         NULL,
    `address`      VARCHAR(45) NOT NULL,
    `phone_number` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
    UNIQUE INDEX `surname_UNIQUE` (`surname` ASC) VISIBLE,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
);

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

CREATE TABLE `shop_hib`.`orders`
(
    `id`      INT  NOT NULL AUTO_INCREMENT,
    `date`    DATE NOT NULL,
    `user_id` INT  NOT NULL,
    `price`   INT  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id` (`id` ASC),
    CONSTRAINT fk_orders_user_id_users_id
        FOREIGN KEY (`user_id`)
            REFERENCES `shop_hib`.`users` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

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









