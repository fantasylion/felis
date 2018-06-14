-- 地址字段太小
ALTER TABLE t_mem_member ADD COLUMN customer_no varchar(255);
ALTER TABLE t_mem_contact alter COLUMN address type character varying(500);

ALTER TABLE t_so_salesorder ADD COLUMN customer_no varchar(255);
ALTER TABLE t_so_consignee alter COLUMN address type character varying(500);


-- 以下数据导出存入本地coupon_temp表中
SELECT
	couponcode.coupon_code promotioncouponcode,
	couponcode.id promotioncouponcodeid,
	coupon.coupon_name promotioncouponname,
	coupon.id promotioncouponid
FROM
	t_prm_promotioncouponcode couponcode
	JOIN t_prm_promotioncoupon coupon ON couponcode.coupon_id = coupon.ID 
WHERE
	couponcode.end_time = '2020-12-31 16:00:00';


-- 以下数据导入本地order_line_temp表中
-- 以下数据导入本地order_line_temp表中
SELECT
	item.ID item_id,
	sku.properties,
	img.pic_url,
	sku.ID sku_id,
	info.title,
	sku.out_id 
FROM
	t_pd_item item
	LEFT JOIN t_pd_iteminfo info ON info.item_id = item.ID
  LEFT JOIN t_pd_sku sku ON sku.item_id = item.ID	
  LEFT JOIN (SELECT item_id, type, pic_url FROM t_pd_item_image WHERE type = 'IMG_TYPE_PDP' AND position = 1) as img ON img.item_id = item.ID
WHERE
	sku.lifecycle != 2 AND sku.lifecycle != 0 
	AND item.lifecycle != 2 AND item.lifecycle != 0

-- 以下数据导入本地t_sf_delivery_area表中
SELECT id, area, code, parent_id FROM t_sf_delivery_area;
