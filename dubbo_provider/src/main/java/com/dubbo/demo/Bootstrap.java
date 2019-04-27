package com.dubbo.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Bootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private static AbstractXmlApplicationContext context = null;

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition STOP = LOCK.newCondition();

    static {
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (context != null) {
                    context.close();
                    context = null;
                    LOGGER.info("关闭context,释放资源");
                }
            }
        });
    }

    public static void main(String[] args) {
        if (args.length == 0 || !"start".equals(args[0])) {
            LOGGER.info("参数不正确,程序退出");
            System.exit(0);
        }

        if (context != null) {
            LOGGER.info("不允许重复启动ApplicationContext");
            System.exit(0);
        }

//        PropertiesConfiguration configuration = new PropertiesConfiguration();
//        configuration.filterConfig("cycore.properties","djlogback.xml",
//                "redis-setting.xml","config/common.xml",
//                "config/mongodb.xml","config/dubbo-precision-consumer.xml",
//                "config/mysql.xml","config/dubbo-precision-provider.xml",
//                "config/sentinel-redis.xml","config/zt_applicationContext_epasdubbo.xml",
//                "config/zt_applicationContext_service.xml");
//        PropertiesConfiguration.loadLogback("djlogback.xml");
        context = new ClassPathXmlApplicationContext("classpath:config/*.xml");
        context.start();
        LOGGER.info("启动成功");
//        addHook(context);
        //主线程阻塞等待，守护线程释放锁后退出
        try {
            LOCK.lock();
            STOP.await();
        } catch (InterruptedException e) {
            LOGGER.warn(" service   stopped, interrupted by other thread!", e);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Created on 2017年12月12日
     * <p>
     * Discription:[添加一个守护线程]
     *
     * @param applicationContext
     *
     */
    private static void addHook(AbstractApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            try {

                applicationContext.stop();
            } catch (Exception e) {
                LOGGER.error("StartMain stop exception ", e);
            }

            LOGGER.info("jvm exit, all service stopped.");
            try {
                LOCK.lock();
                STOP.signal();
            } finally {
                LOCK.unlock();
            }
        }, "StartMain-shutdown-hook"));
    }
}
