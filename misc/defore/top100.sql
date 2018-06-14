-- 下单付款完成，交易完成top 100 用户
SELECT * FROM t_mem_member WHERE id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100);

SELECT * FROM t_mem_personal_data WHERE id in(SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100);

SELECT * FROM t_mem_contact WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100);

SELECT * FROM t_crm_email_subscribe_log WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100);

SELECT * FROM T_SYS_COUPON_LOG WHERE memberid in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100)

SELECT * FROM t_mem_conduct WHERE id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100)
------------------------------------------------

---- top 100 用户的订单 ------------------------
SELECT * from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15';

SELECT * FROM t_so_orderline WHERE order_id in (SELECT id from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15');

SELECT * FROM t_so_consignee WHERE order_id in (SELECT id from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15');

SELECT * FROM t_so_payinfo WHERE order_id in (SELECT id from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15');

SELECT * FROM t_so_payinfo_log WHERE order_id in (SELECT id from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15');

SELECT * FROM t_so_logistics WHERE order_id in (SELECT id from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15');

SELECT * FROM t_so_salesorder_request WHERE order_id in (SELECT id from t_so_salesorder WHERE member_id in (SELECT finsh_order.member_id FROM (SELECT * FROM t_so_salesorder WHERE financial_status = '3' AND logistics_status = '15') as finsh_order GROUP BY finsh_order.member_id ORDER BY count(finsh_order.member_id) desc LIMIT 100) AND  financial_status = '3' AND logistics_status = '15');