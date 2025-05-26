package com.threadPool.sdk.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.threadPool.sdk.domain.IDynamicThreadPoolService;
import com.threadPool.sdk.domain.model.entity.NacosConfigEntity;
import com.threadPool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.threadPool.sdk.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author maple
 * @Description nacos配置监听器，用于监听线程池配置变更
 * @createTime:2025-05-26 20:34
 */
public class NacosPoolConfigAdjustListener {
    private Logger logger = LoggerFactory.getLogger(NacosPoolConfigAdjustListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    private final ConfigService configService;

    private NacosConfigEntity nacosConfigEntity;

    public NacosPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry, ConfigService configService, NacosConfigEntity nacosConfigEntity) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
        this.configService = configService;
        this.nacosConfigEntity = nacosConfigEntity;
    }

    /**
     * 初始化时为每个线程池添加单独的配置监听
     */
    @PostConstruct
    public void init() {
        try {
            // 获取当前应用中的所有线程池
            List<ThreadPoolConfigEntity> threadPoolList = dynamicThreadPoolService.queryThreadPoolList();

            logger.info("动态线程池，初始化线程池配置监听，线程池数量: {}", threadPoolList.size());
            // 上报线程池列表
            registry.reportThreadPool(threadPoolList);
            // 上报每个线程池的配置并为每个线程池添加监听器
            for (ThreadPoolConfigEntity entity : threadPoolList) {
                // 上报每个线程池的配置
                registry.reportThreadPoolConfigParameter(entity);
                logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(entity));
                // 为每个线程池动态添加配置监听
                addConfigListener(entity.getThreadPoolName());
            }
        } catch (Exception e) {
            logger.error("动态线程池，初始化线程池配置监听异常", e);
        }
    }

    /**
     * 为指定线程池添加配置监听
     * @param threadPoolName 线程池名称
     */
    private void addConfigListener(String threadPoolName) {
        try {
            String dataId = nacosConfigEntity.getNacosDataId() + "-" + threadPoolName;
            String group = nacosConfigEntity.getNacosGroup();

            configService.addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null; // 使用默认执行器
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    processConfigChange(configInfo, threadPoolName);
                }
            });

            logger.info("动态线程池，为线程池 {} 添加配置监听，dataId: {}, group: {}", threadPoolName, dataId, group);
        } catch (Exception e) {
            logger.error("动态线程池，为线程池 {} 添加配置监听异常", threadPoolName, e);
        }
    }

    /**
     * 处理配置变更
     * @param configInfo 配置信息
     * @param threadPoolName 线程池名称
     */
    private void processConfigChange(String configInfo, String threadPoolName) {
        try {
            logger.info("动态线程池，接收到线程池 {} 配置变更: {}", threadPoolName, configInfo);

            Yaml yaml = new Yaml();
            Map<String, Object> configMap = yaml.load(configInfo);

            if (configMap != null) {
                for (Map.Entry<String, Object> entry : configMap.entrySet()) {
                    String poolName = entry.getKey();
                    Map<String, Object> poolConfig = (Map<String, Object>) entry.getValue();

                    // 创建线程池配置实体
                    ThreadPoolConfigEntity entity = new ThreadPoolConfigEntity();
                    entity.setThreadPoolName(poolName);
                    entity.setAppName((String) poolConfig.get("appName"));
                    entity.setCorePoolSize(((Number) poolConfig.get("corePoolSize")).intValue());
                    entity.setMaximumPoolSize(((Number) poolConfig.get("maximumPoolSize")).intValue());

                    // 更新线程池配置
                    dynamicThreadPoolService.updateThreadPoolConfig(entity);
                    logger.info("动态线程池，更新线程池配置: {}", JSON.toJSONString(entity));

                    // 更新后上报最新数据
                    registry.reportThreadPoolConfigParameter(entity);
                }
            }
        } catch (Exception e) {
            logger.error("动态线程池，处理线程池 {} 配置变更异常", threadPoolName, e);
        }
    }

}
