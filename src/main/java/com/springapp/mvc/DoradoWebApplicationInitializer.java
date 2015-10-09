package com.springapp.mvc;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.loader.DoradoLoader;
import com.bstek.dorado.web.servlet.DoradoPreloadListener;
import com.bstek.dorado.web.servlet.DoradoServlet;
import com.bstek.dorado.web.servlet.SpringContextLoaderListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.List;

/**
 * web.xml
 * @author june
 * 2015年09月30日 13:30
 */
public class DoradoWebApplicationInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext servletContext) throws ServletException {

        //指定dorado-home
        servletContext.setInitParameter("doradoHome", "classpath:dorado-home");

        //加载dorado的bean xml
        DoradoLoader doradoLoader = DoradoLoader.getInstance();
        try {
            doradoLoader.preload(servletContext, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);
        servletContext.addListener(new DoradoContextLoaderListener(ctx));

        //注册DoradoServlet
        DoradoServlet doradoServlet = new DoradoServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("doradoServlet", doradoServlet);
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping(new String[]{"*.d","*.dpkg","/dorado/*"});


    }

}
