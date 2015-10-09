package com.springapp.mvc;

import com.bstek.dorado.web.listener.DelegatingServletContextListenersManager;
import com.bstek.dorado.web.listener.DelegatingSessionListenersManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 重写了dorado的SpringContextLoaderListener
 * @see com.bstek.dorado.web.servlet.SpringContextLoaderListener
 * @author june
 * 2015年10月09日 13:17
 */
public class DoradoContextLoaderListener extends ContextLoaderListener
        implements HttpSessionListener {

    private static final Log logger = LogFactory
            .getLog(DoradoContextLoaderListener.class);

    public DoradoContextLoaderListener(WebApplicationContext context) {
        super(context);
    }

    public DoradoContextLoaderListener() {}

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            DelegatingServletContextListenersManager
                    .fireContextInitialized(event);
        } catch (Exception e) {
            logger.error(e, e);
        }
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);

        try {
            DelegatingServletContextListenersManager
                    .fireContextDestroyed(event);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void sessionCreated(HttpSessionEvent event) {
        try {
            DelegatingSessionListenersManager.fireSessionCreated(event);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        try {
            DelegatingSessionListenersManager.fireSessionDestroyed(event);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}

