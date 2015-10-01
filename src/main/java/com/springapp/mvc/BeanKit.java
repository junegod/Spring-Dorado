package com.springapp.mvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author june
 *         2015年09月30日 14:11
 */
@Service
public class BeanKit implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    /**
     * 注入context
     * @param context
     */
    public void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    /**
     * 得到上下文
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据名称获取
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据名称和类型
     * @param beanName
     * @param clz
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> clz) {
        return applicationContext.getBean(beanName, clz);
    }

    /**
     * 根据类型获取bean
     * @param clz
     * @return
     */
    public static <T> T getBean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }


    /**
     * 根据类型获取beanName
     * @param clz
     * @return
     */
    public static String[] getBeanNamesForType(Class<?> clz){
        return applicationContext.getBeanNamesForType(clz);
    }
    /**
     * 根据bean上的注解获取bean
     * @param clz
     * @return
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> clz){
        return applicationContext.getBeansWithAnnotation(clz);
    }

    /**
     * 获取beanFactory
     * @return
     */
    public static DefaultListableBeanFactory getBeanFactory(){
        return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }

}
