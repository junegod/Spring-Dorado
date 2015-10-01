/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.web.servlet;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.core.io.LocationTransformerHolder;
import com.bstek.dorado.core.pkgs.PackageConfigurer;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.spring.DoradoAppContextImporter;
import com.bstek.dorado.web.ConsoleUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-20
 */
public class DefaultDoradoAppContextImporter implements
        DoradoAppContextImporter {
    private static final Log logger = LogFactory
            .getLog(DefaultDoradoAppContextImporter.class);

    protected void importBeanDefinitionResource(String location,
                                                Element element, ParserContext parserContext) throws Exception {
        XmlReaderContext readerContext = parserContext.getReaderContext();
        try {
            ResourceLoader resourceLoader = readerContext.getResourceLoader();
            Resource relativeResource = resourceLoader.getResource(location);

            int importCount = readerContext.getReader().loadBeanDefinitions(
                    relativeResource);
            if (logger.isDebugEnabled()) {
                logger.debug("Imported " + importCount
                        + " bean definitions from dorado-context [" + location
                        + "]");
            }
        } catch (Exception ex) {
            readerContext.error("Invalid dorado-context [" + location
                    + "] to import bean definitions from", element, null, ex);
        }

        readerContext.fireImportProcessed(location,
                readerContext.extractSource(element));
    }

    public void importDoradoAppContext(Element element,
                                       ParserContext parserContext) throws Exception {
//        List<String> doradoContextLocations = DoradoLoader.getInstance()
//                .getContextLocations(false);
        List<String> doradoContextLocations = getDoradoContextLocations();
        Assert.notNull("Can not get [doradoContextLocations], the DoradoPreloadListener may not configured or configured in wrong order. "
                + "Please check your web.xml.");

        ConsoleUtils.outputLoadingInfo("Loading dorado context configures...");

        for (String location : doradoContextLocations) {
            importBeanDefinitionResource(location, element, parserContext);
        }
    }




    private void pushLocation(List<String> locationList, String location) {
        if (StringUtils.isNotEmpty(location)) {
            location = LocationTransformerHolder.transformLocation(location);
            locationList.add(location);
        }
    }

    private void pushLocations(List<String> locationList, String locations) {
        if (StringUtils.isNotEmpty(locations)) {
            for (String location : org.springframework.util.StringUtils
                    .tokenizeToStringArray(
                            locations,
                            ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS)) {
                pushLocation(locationList, location);
            }
        }
    }

    private List<String> getDoradoContextLocations() throws Exception {
        List<String> contextLocations = new ArrayList<String>();

        // gothrough packages
        String addonLoadMode = Configure.getString("core.addonLoadMode");
        String[] enabledAddons = StringUtils.split(
                Configure.getString("core.enabledAddons"), ",; \n\r");
        String[] disabledAddon = StringUtils.split(
                Configure.getString("core.disabledAddon"), ",; \n\r");

        Collection<PackageInfo> packageInfos = PackageManager
                .getPackageInfoMap().values();
        int addonNumber = 0;
        for (PackageInfo packageInfo : packageInfos) {
            String packageName = packageInfo.getName();
            if (packageName.equals("dorado-core")) {
                continue;
            }

            if (addonNumber > 9999) {
                packageInfo.setEnabled(false);
            } else if (StringUtils.isEmpty(addonLoadMode)
                    || "positive".equals(addonLoadMode)) {
                packageInfo.setEnabled(!ArrayUtils.contains(disabledAddon,
                        packageName));
            } else {
                // addonLoadMode == negative
                packageInfo.setEnabled(ArrayUtils.contains(enabledAddons,
                        packageName));
            }

            if (packageInfo.isEnabled()) {
                addonNumber++;
            }
        }

        ConfigureStore configureStore = Configure.getStore();
        configureStore.set("console.enabled",true);

        // load packages
        //packageInfos现在有三个包 dorado-core、dorado-vidor、dorado-console
        for (PackageInfo packageInfo : packageInfos) {
            if (!packageInfo.isEnabled()) {
                pushLocations(contextLocations,
                        packageInfo.getComponentLocations());
                continue;
            }

            PackageConfigurer packageConfigurer = packageInfo.getConfigurer();

            String[] locations;

            // 处理Spring的配置文件
            pushLocations(contextLocations, packageInfo.getContextLocations());
            if (packageConfigurer != null) {
                locations = packageConfigurer
                        .getContextConfigLocations(null);
                if (locations != null) {
                    for (String location : locations) {
                        pushLocation(contextLocations, location);
                    }
                }
            }


        }
        pushLocation(contextLocations,"classpath:com/bstek/dorado/vidorsupport/context.xml");
//        pushLocation(contextLocations,"classpath:com/bstek/dorado/console/context.xml");

//        pushLocation(contextLocations,"classpath:com/bstek/dorado/web/servlet-context.xml");
//        pushLocation(contextLocations,"classpath:com/bstek/dorado/view/servlet-context.xml");
//        pushLocation(contextLocations,"classpath:com/bstek/dorado/idesupport/servlet-context.xml");
//        pushLocation(contextLocations,"classpath:com/bstek/dorado/console/required-servlet-context.xml");
//        pushLocation(contextLocations,"classpath:com/bstek/dorado/console/servlet-context.xml");
        return contextLocations;
    }
}
