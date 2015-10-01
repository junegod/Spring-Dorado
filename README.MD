# 使用spring纯注解集成dorado

使用注解，可以摆脱繁琐的xml配置，大大提升开发效率。希望 **dorado** 也能更多的使用注解来简化配置。
本以为使用纯注解来集成 **dorado** 很简单，想不到好多问题，目前还没有解决，希望大家一起来。
遇到的问题如下：

## 问题一 doradoHome问题（不是问题）

 **dorado** 默认的`doradoHome`是`/WEB-INF/dorado-home`，一般配置信息，我们习惯了放在`resources`目录。
 如何修改呢？看`com.bstek.dorado.web.loader.DoradoLoader`源码，发现 **dorado** 获取doradoHome的顺序是：
 
 1.从系统环境变量获取，变量名：DORADO_HOME
 
    doradoHome = System.getenv("DORADO_HOME");
    
 2.从`servlet`上下文获取
    
    doradoHome = servletContext.getInitParameter("doradoHome");
 
 3.都取不到则使用默认路径，`/WEB-INF/dorado-home`

 我这里使用的是设置servletContext的初始化参数
    
    servletContext.setInitParameter("doradoHome", "classpath:dorado-home");
       
## 问题二 自定义Spring的ContextLoaderListener问题

   参见[自定义Spring的ContextLoaderListener 问题](http://bsdn.org/projects/dorado7/issue/dorado7-177069),我这里的解决办法是重写了
   `com.bstek.dorado.web.servlet.DefaultDoradoAppContextImporter`这个类，实现的有问题，但是能用哈。这个是收集 **dorado** 的所有`contextLocation`
   期待官方解决。
   
## 问题三 到底使用 SpringContextLoaderListener还是DoradoPreloadListener
    
   现在用两种都有问题没解决，其实是个spring的问题，功力太浅啊，没办法了。。。
   使用`SpringContextLoaderListener`可以启动，dorado-console也能正常使用，就是我自己定义的`Test`的bean找不到。
   测试方法：
        
   1.删除我写的`DefaultDoradoAppContextImporter`
   
   2.删除`AppConfig`上的`@ImportResource("classpath:dorado-home/dorado-context.xml")`
  
   3.`DoradoWebApplicationInitializer`中使用`SpringContextLoaderListener`，把`DoradoPreloadListener`注释
   
   启动后，访问[http://localhost:8080/Spring-Dorado/dorado/console](http://localhost:8080/Spring-Dorado/dorado/console)是正常的，访问[http://localhost:8080/Spring-Dorado/com.springapp.mvc.DataGrid.d](http://localhost:8080/Spring-Dorado/com.springapp.mvc.DataGrid.d)出错，找不到bean。

    
    
 我这几个类说明下，`AppConfig`是spring的配置类，`BeanKit` 和`BeanListener` 我测试用的，不用管。`DoradoWebApplicationInitializer`替换web.xml的。
 `Test` 和 `DataGrid`是测试 **dorado** 的。`DefaultDoradoAppContextImporter` 是解决问题二的。
 