## 数据迁移
**data-hamal** 用于 **loreal** 7 站数据迁移，`data-hamal` 工具使用文档 --by serio.shi

## 注意事项
在往数据库导入数据时，确保所有定时任务已经关闭，避免发生系统发出催付短信、邮件等


## require
* Jdk 1.7 以上
* Pgsql 9.0.12

## 目录介绍
目录 `init\defore` 下 `init_local.sql` 文件用于初始化本地表

* _t_sf_delivery_area_ 地址信息，通过这个表获取地址的code，需要通过 `init_pro.sql` 在项目数据库中查出数据后导入此表
* _order_line_temp_ 用于获取订单行商品信息，需要通过 `init_pro.sql` 在项目数据库中查出数据后导入此表
* _customermapping_ 用于记录 `memberId` 和 `customber_no` 关联，运行jar前需要清空此表
* _coupon_temp_ 用于获取优惠券类型 `id` ，需要通过 `init_pro.sql` 在项目数据库中查出数据后导入此表

目录 `init\defore` 下 `init_pro.sql` 文件用于初始化项目数据库表

目录 `init\defore` 下 `update_sequence.sql` 文件用于数据迁移到项目数据库后，更新相关序列

目录 `config` 下 `db.properties` 文件，用于配置本地数据库信息，建议每个项目使用不同的库

文件 `setup.bat` 批处理文件，点击即可运行，需要自行修改参数

## 如何使用

第一步：按照目录介绍，执行 `init\defore` 目录下的sql文件

第二步：执行 `jar`，生成 `sql` 脚本

第二步：将 `sql` 脚本放到项目数据库中执行

## options

#### parameters

|参数|类型|必填|值域|描述|
|-----|-----|-----|-----|-----|
|fileOutputDir|VM参数|是||sql脚本输出目录|
|file.encoding|VM参数|是||使用的字符集编码，默认使用操作系统默认编码，目前必须填写`UTF-8`|
|damal.maketag|VM参数|否|<p>`true`</p><p>`false`</p>|如果是 `true` 将会给手机号和邮箱字段值前后分别加上 `test-` `-test`，默认为 `false` |
|mode.process.type|VM参数|否|<p>`1`</p><p>`2`</p>|订单状态处理模式<ul><li>模式 `1`：如果付款或者是COD订单标记为已完成，如果是未付款标记为取消</li>	<li>模式 `2`：未支付改为会员取消，已付款未完成状态改为在途状态 `6`</li>	<li>默	认：不做处理</li></ul>|
|command|Main参数|是|`order`<p>`member`</p><p>`review`</p><p>`vote`</p>`review-txt`|main 第一个参数，表示解析的数据类型|
|inputFilePath|Main参数|是||main 第二个参数，需要解析的xml文件|
|index|Main参数|是||main 第三个参数，涉及到的表index的开始序列号一般从1开始|


#### Demo

```
java -Dfile.encoding=UTF-8 -DfileOutputDir=C:/data-migration/sql_script/member_pro -jar data-hamal-1.0.0.jar member C:/data-migration/customer/lancome-cn-customer-list.xml 1
```


## 订单迁移表

* t_so_salesorder
* t_so_consignee
* t_so_orderline
* t_so_payinfo
* t_so_payinfo_log
* t_so_logistics

## 会员迁移表

* t_mem_member
* t_mem_personal_data
* t_mem_contact
* t_mem_conduct
* t_mem_cryptoguard
* t_sys_coupon_log
* t_crm_email_subscribe_log

## 评论表

* t_pd_item_rate