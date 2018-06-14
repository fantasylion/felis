-- 用户数据迁移相关表序列更新
SELECT setval('s_t_mem_identify', (SELECT "max"(id) FROM t_mem_member));
SELECT setval('S_T_SAL_CONTACT', (SELECT "max"(id) FROM t_mem_contact));
SELECT setval('s_t_prm_promotioncouponcode', (SELECT "max"(id) FROM t_prm_promotioncouponcode));
SELECT setval('S_T_SYS_COUPON_LOG', (SELECT "max"(id) FROM T_SYS_COUPON_LOG));
SELECT setval('s_t_crm_email_subscribe_log', (SELECT "max"(id) FROM t_crm_email_subscribe_log));

-- 订单数据迁移相关表序列更新
SELECT setval('S_T_SAL_SALESORDER', (SELECT Max(id) FROM t_so_salesorder));
SELECT setval('S_T_SAL_CONSIGNEE', (SELECT Max(id) FROM t_so_consignee));
SELECT setval('S_T_SAL_ORDERDETAIL', (SELECT Max(id) FROM t_so_orderline));
SELECT setval('s_t_so_payinfo', (SELECT Max(id) FROM t_so_payinfo));
SELECT setval('s_t_so_payinfo_log', (SELECT Max(id) FROM t_so_payinfo_log));
SELECT setval('S_T_SAL_LOGISTICS', (SELECT Max(id) FROM t_so_logistics));
SELECT setval('S_T_SO_SALESORDER_REQUEST', (SELECT Max(id) FROM t_so_salesorder_request));

-- 评论
SELECT setval('S_T_PD_ITEM_RATE', (SELECT Max(id) FROM T_PD_ITEM_RATE));
SELECT setval('S_T_STORE_PD_ITEM_RATE_IMAGE',(SELECT Max(id) FROM T_STORE_PD_ITEM_RATE_IMAGE));