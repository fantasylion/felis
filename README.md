## felis
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



#### Demo

```
java -Dfile.encoding=UTF-8 -DfileOutputDir=C:/data-migration/sql_script/member_pro -jar felis-1.0.0.jar member C:/data-migration/customer/customer-list.xml 1
```

