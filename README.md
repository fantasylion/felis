## 数据迁移
**felis** 数据迁移使用的手脚架，方便快速搭建迁移脚本 --by serio.shi


## require
* Jdk 1.7 以上
* Pgsql 9.0.12


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
java -Dfile.encoding=UTF-8 -DfileOutputDir=C:/data-migration/sql_script/member_pro -jar felis-1.0.0.jar member C:/data-migration/customer/customer-list.xml 1
```