package com.springapp.mvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Service;

/**
 * @author june
 *         2015年10月01日 13:00
 */
@Service
public class BeanListener implements MergedBeanDefinitionPostProcessor {

    public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String beanName) {
        System.out.println("=====Bean Name:" + beanName);
    }

    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }
}
