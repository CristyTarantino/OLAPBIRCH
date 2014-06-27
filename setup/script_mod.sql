CREATE TABLE  `tpch`.`outliers` (
  `id_outlier` int(11) NOT NULL AUTO_INCREMENT,
  `num_pts` int(11) NOT NULL,
  `ls` text NOT NULL,
  `ss` text NOT NULL,
  PRIMARY KEY (`id_outlier`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `tpch`.`orders` 
ADD COLUMN `clusterID` VARCHAR(255) NOT NULL AFTER `o_comment`;

UPDATE tpch.orders
SET o_orderpriority = "1"
WHERE o_orderpriority = "1-URGENT";

UPDATE tpch.orders
SET o_orderpriority = "2"
WHERE o_orderpriority = "2-HIGH";

UPDATE tpch.orders
SET o_orderpriority = "3"
WHERE o_orderpriority = "3-MEDIUM";

UPDATE tpch.orders
SET o_orderpriority = "4"
WHERE o_orderpriority = "4-NOT SPECIFIED";

UPDATE tpch.orders
SET o_orderpriority = "5"
WHERE o_orderpriority = "5-LOW";

ALTER TABLE `tpch`.`orders` 
MODIFY COLUMN `o_orderpriority` INTEGER UNSIGNED NOT NULL;