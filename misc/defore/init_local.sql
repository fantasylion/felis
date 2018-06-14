CREATE TABLE "public"."t_sf_delivery_area" (
  "id" int8 NOT NULL,
  "area" varchar(255),
  "code" varchar(255),
  "parent_id" int8,
  CONSTRAINT "t_sf_delivery_area_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "public"."order_line_temp" (
  "item_id" int8,
  "properties" varchar(255),
  "pic_url" varchar(255),
  "sku_id" int8,
  "title" varchar(255),
  "out_id" varchar(255)
);

CREATE TABLE "public"."customermapping" (
  "customer_no" varchar(255) NOT NULL,
  "member_id" int8,
  CONSTRAINT "cutomer_no_un" UNIQUE ("customer_no")
);

CREATE TABLE "public"."coupon_temp" (
  "promotioncouponcode" varchar(255) NOT NULL,
  "promotioncouponcodeid" int8 NOT NULL,
  "promotioncouponname" varchar(255) NOT NULL,
  "promotioncouponid" int8 NOT NULL
);

CREATE TABLE "public"."t_so_coupon_record" (
  "order_code" varchar(200),
  "status" varchar(200),
  "pid" varchar(200),
  "coupon" varchar(200),
  "customer_no" varchar(200)
);