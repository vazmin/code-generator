# code-generator
代码生成器－自动读取表结构信息，并根据模版生成项目代码。

一般项目里的基本CURD的前后端代码几乎都是相同的有规律的，变的只是对象名称和属性。
完全可以根据表结构和项目模板自动生成代码，减少琐碎的工作。唯一的工作就是项目初始时写一遍模板。

当前的模板适用于 [Vazmin Manage Simple](https://github.com/vazmin/vazmin-manage/tree/master/manage-simples)

### How to use?

1. 根据已有的项目代码编写自己的`FreeMark`模板
2. 复制一份yml, 修改配置，指定模板输出信息
3. 直接运行项目或者`mvn package`得到可执行的jar并执行

建议一个项目一个yml配置。

### Data Model

Commonly used data
```
+- genDate = "2020/1/20"

+- table
    |
    +- tableName = "menu_info"
    |
    +- lowerCamelCaseName = "menuInfo"
    |
    +- upperCamelCaseName = "MenuInfo"
    |
    +- lowerCaseSubName = "menu-info"    
    |
    +- clearRemark = "菜单信息"
    |
    +- remarks = "菜单信息表"
    |
    +- ...

+- columnList
    |
    +- column
        |
        +- actualColumnName = "parent_id"
        |
        +- nullable = false
        |
        +- lowerCamelCaseName = "parentId"
        |
        +- upperCamelCaseName = "ParentId"
        |
        +- clearRemark = "上级id"
        |
        +- enRemark = "Parent Id"
        |
        +- javaType = "String"
        |
        +- ...

+- app = AppProperties
    |
    +- author = "wangzhiming"
    |
    +- ...
```

如果将java设置为true,将获得对应的package数据 
```
folders:
  - path: /src/main/java/${application.pkg}/system 
    java: true 
    tpl-i-o-list:
      - tpl-path: TableName.java.ftl 
        target: /model 
      - tpl-path: TableNameController.java.ftl
        target: /controller
```
data model 
```
+- TableNamePackage = ${application.pkg}.system.model
+- TableNameControllerPackage = ${application.pkg}.system.controller
```


### Properties

```yaml
application:
  table-names: menu_info # table names
  search-column-names: name # search column names
  pkg:  com.github.vazmin.manage.simple # the base package name
  modulePkg: system # the module name
  url:
    value: /system/menu-info # url value
    frontend-prefix: /pages # frontend route = frontend-prefix + value
    backend-prefix: /api # backend api = backend-prefix + value
  output-root-dir: /output/path # output dir
  folders:
    # source-folder
    - path: /src/main/java/${application.pkg} # output path
      java: true # java code, default false
      tpl-i-o-list:
        - tpl-path: TableName.java.ftl # template name pattern
          target: /model # output target dir
  author: wangzhiming # author name
  override: false # output override
  date-pattern: yyyy/M/d # date pattern
```

### FAQ

```
Q: 为什么没有取到MYSQL的表注释？
A: mysql jdbc连接需要设置useInformationSchema=true

```