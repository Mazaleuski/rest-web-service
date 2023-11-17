USE
shop_hib;
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
VALUES ('User', 'User', '1989-10-10', 'user@aa.aa', '$2a$10$UgUsw3sig7lfvEGur5qCoer9VA0VV55mbYt0KfslUjlaC04u.XU0.', '100', 'Minsk', '+375295555550');
INSERT INTO `shop_hib`.`users` (`name`, `surname`, `birthday`, `email`, `password`, `balance`, `address`,
                                `phone_number`)
VALUES ('Admin', 'Admin', '1990-10-10', 'admin@aa.aa', '$2a$10$5Vg84BwZGJ/tMSa9iQ8S0.CzvlIlgl6jDGTGgrN24/dGHwJZ/BHsS', '1000', 'Minsk', '+375295555551');








