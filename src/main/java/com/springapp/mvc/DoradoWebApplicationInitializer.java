package com.springapp.mvc;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.servlet.DoradoPreloadListener;
import com.bstek.dorado.web.servlet.DoradoServlet;
import com.bstek.dorado.web.servlet.SpringContextLoaderListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * web.xml
 * @author june
 * 2015年09月30日 13:30
 */
public class DoradoWebApplicationInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext servletContext) throws ServletException {

        //指定dorado-home
        servletContext.setInitParameter("doradoHome", "classpath:dorado-home");

        //使用SpringContextLoaderListener，AnnotationConfigWebApplicationContext和XmlWebApplicationContext没法合并，各自初始化了
        servletContext.addListener(new SpringContextLoaderListener());

        //使用DoradoPreloadListener，手动导入dorado的spring xml
//        servletContext.addListener(new DoradoPreloadListener());

        //注册DoradoServlet
        DoradoServlet doradoServlet = new DoradoServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("doradoServlet", doradoServlet);
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping(new String[]{"*.d","*.dpkg","/dorado/*"});

        // 初始化WebContext
//        DoradoContext context = DoradoContext.init(servletContext, false);
//        Context.setFailSafeContext(context);

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);
        ctx.setServletContext(servletContext);
        ctx.refresh();


        System.out.println("=========完成");

    }

//原本的web.xml的配置，是没有任何问题的
//    <?xml version="1.0" encoding="UTF-8"?>
//        <web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//        xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
//        <listener>
//        <listener-class>com.bstek.dorado.web.servlet.SpringContextLoaderListener</listener-class>
//        </listener>
//
//        <servlet>
//        <servlet-name>doradoServlet</servlet-name>
//        <servlet-class>com.bstek.dorado.web.servlet.DoradoServlet</servlet-class>
//        <load-on-startup>1</load-on-startup>
//        </servlet>
//        <servlet-mapping>
//        <servlet-name>doradoServlet</servlet-name>
//        <url-pattern>*.d</url-pattern>
//        </servlet-mapping>
//        <servlet-mapping>
//        <servlet-name>doradoServlet</servlet-name>
//        <url-pattern>*.dpkg</url-pattern>
//        </servlet-mapping>
//        <servlet-mapping>
//        <servlet-name>doradoServlet</servlet-name>
//        <url-pattern>/dorado/*</url-pattern>
//        </servlet-mapping>
//
//    </web-app>

}
