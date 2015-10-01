package com.springapp.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author june
 * 2015年09月29日 23:04
 */
@Configuration
@ComponentScan(basePackages="com.springapp.mvc")
@EnableWebMvc
@ImportResource("classpath:dorado-home/dorado-context.xml")
public class AppConfig {


}
