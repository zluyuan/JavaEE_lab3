CREATE USER 'demouser'@'localhost' IDENTIFIED BY '123456';
CREATE USER 'demouser'@'%' IDENTIFIED BY '123456';
GRANT PROCESS ON *.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS payment;
GRANT ALL ON payment.* TO 'demouser'@'localhost';
GRANT ALL ON payment.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS goods;
GRANT ALL ON goods.* TO 'demouser'@'localhost';
GRANT ALL ON goods.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS shop;
GRANT ALL ON shop.* TO 'demouser'@'localhost';
GRANT ALL ON shop.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS freight;
GRANT ALL ON freight.* TO 'demouser'@'localhost';
GRANT ALL ON freight.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS aftersale;
GRANT ALL ON aftersale.* TO 'demouser'@'localhost';
GRANT ALL ON aftersale.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS privilege;
GRANT ALL ON privilege.* TO 'demouser'@'localhost';
GRANT ALL ON privilege.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS coupon;
GRANT ALL ON coupon.* TO 'demouser'@'localhost';
GRANT ALL ON coupon.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS groupon;
GRANT ALL ON groupon.* TO 'demouser'@'localhost';
GRANT ALL ON groupon.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS advsale;
GRANT ALL ON advsale.* TO 'demouser'@'localhost';
GRANT ALL ON advsale.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS share;
GRANT ALL ON share.* TO 'demouser'@'localhost';
GRANT ALL ON share.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS service;
GRANT ALL ON service.* TO 'demouser'@'localhost';
GRANT ALL ON service.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS prodorder;
GRANT ALL ON prodorder.* TO 'demouser'@'localhost';
GRANT ALL ON prodorder.* TO 'demouser'@'%';

CREATE DATABASE IF NOT EXISTS customer;
GRANT ALL ON customer.* TO 'demouser'@'localhost';
GRANT ALL ON customer.* TO 'demouser'@'%';

FLUSH PRIVILEGES;