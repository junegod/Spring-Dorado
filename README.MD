# 使用spring纯注解集成dorado

使用注解，可以摆脱繁琐的xml配置，大大提升开发效率。希望 **dorado** 也能更多的使用注解来简化配置。
本以为使用纯注解来集成 **dorado** 很简单，想不到好多问题，目前还没有解决，希望大家一起来。
遇到的问题如下：

   
## 无法找到注解定义的bean，只能找到xml定义的
    
  
  启动后，访问[http://localhost:8080/Spring-Dorado/dorado/console](http://localhost:8080/Spring-Dorado/dorado/console)是正常的，访问[http://localhost:8080/Spring-Dorado/com.springapp.mvc.DataGrid.d](http://localhost:8080/Spring-Dorado/com.springapp.mvc.DataGrid.d)出错，找不到bean。

   
    
 我这几个类说明下，`AppConfig`是spring的配置类， `DoradoWebApplicationInitializer`替换web.xml的。
 `Test` 和 `DataGrid`是测试 **dorado** 的。
 