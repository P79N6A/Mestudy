package com.fly.demo;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fly.common.DataBaseInit;

/**
 * 
 * Spring在启动完成后执行方法
 * 
 * @author 00fly
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class Startup implements ApplicationListener<ContextRefreshedEvent>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);
    
    /**
     * 事件回调
     * 
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        ApplicationContext applicationContext = event.getApplicationContext();
        LOGGER.info("★★★★★★★★ {}", applicationContext);
        
        // servlet context初始化完成
        if (applicationContext.getParent() != null && SystemUtils.IS_OS_WINDOWS)
        {
            try
            {
                DataBaseInit.initUseSQL("/sql/init.sql");
                
                LOGGER.info("now open Browser...");
                ServletContext servletContext = applicationContext.getBean(ServletContext.class);
                Runtime.getRuntime().exec("cmd.exe /c start /min http://127.0.0.1:8080" + servletContext.getContextPath() + "/druid");
                Runtime.getRuntime().exec("cmd.exe /c start /min http://127.0.0.1:8080" + servletContext.getContextPath() + "/");
            }
            catch (Exception e)
            {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
