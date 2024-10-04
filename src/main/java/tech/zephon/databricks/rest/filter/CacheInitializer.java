/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tech.zephon.databricks.rest.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 *
 * @author ForesterF
 */
public class CacheInitializer implements ServletContextListener {
    
    private static final Logger logger = LogManager.getLogger(CacheInitializer.class.getName());
    private static final String cacheName = "APPLICATIONCACHE";
    
    private static CacheManager cacheManager;


    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static String getCacheName() {
        return cacheName;
    }
    
    
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.debug("--> Servlet listener invoked");
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(100)))
                .build();

        cacheManager.init();
        
    }
 
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        
        
    }
    
    
}
