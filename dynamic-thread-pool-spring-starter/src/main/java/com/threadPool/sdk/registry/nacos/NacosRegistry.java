package com.threadPool.sdk.registry.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.threadPool.sdk.domain.model.entity.NacosConfigEntity;
import com.threadPool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.threadPool.sdk.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maple
 * @Description
 * @createTime:2025-05-24 16:59
 */
public class NacosRegistry implements IRegistry {
    private final Logger logger = LoggerFactory.getLogger(NacosRegistry.class);
    private NacosConfigEntity nacosConfigEntity;

    private ConfigService configService;

    public NacosRegistry(NacosConfigEntity nacosConfigEntity,ConfigService configService) {
        this.nacosConfigEntity = nacosConfigEntity;
        this.configService = configService;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities) {
        try {
            // 创建YAML格式的配置内容
            Map<String, Object> configMap = new HashMap<>();
            Map<String, Object> threadPoolsMap = new HashMap<>();

            for (ThreadPoolConfigEntity entity : threadPoolEntities) {
                Map<String, Object> poolConfig = new HashMap<>();
                poolConfig.put("appName", entity.getAppName());
                poolConfig.put("threadPoolName", entity.getThreadPoolName());
                poolConfig.put("corePoolSize", entity.getCorePoolSize());
                poolConfig.put("maximumPoolSize", entity.getMaximumPoolSize());
                poolConfig.put("poolSize", entity.getPoolSize());
                poolConfig.put("activeCount", entity.getActiveCount());
                poolConfig.put("queueType", entity.getQueueType());
                poolConfig.put("queueSize", entity.getQueueSize());
                poolConfig.put("remainingCapacity", entity.getRemainingCapacity());

                threadPoolsMap.put(entity.getThreadPoolName(), poolConfig);
            }

            configMap.put("threadPools", threadPoolsMap);

            // 转换为YAML
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            String content = yaml.dump(configMap);

            // 发布到Nacos
            boolean result = configService.publishConfig(
                    nacosConfigEntity.getNacosDataId() + "-pools",
                    nacosConfigEntity.getNacosGroup(),
                    content
            );

            logger.info("动态线程池，上报线程池列表信息到Nacos结果：{}, 数据量：{}", result, threadPoolEntities.size());
        } catch (NacosException e) {
            logger.error("动态线程池，上报线程池列表信息到Nacos异常", e);
        }
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        try {
            // 创建YAML格式的配置内容
            Map<String, Object> configMap = new HashMap<>();
            Map<String, Object> poolConfig = new HashMap<>();

            poolConfig.put("appName", threadPoolConfigEntity.getAppName());
            poolConfig.put("threadPoolName", threadPoolConfigEntity.getThreadPoolName());
            poolConfig.put("corePoolSize", threadPoolConfigEntity.getCorePoolSize());
            poolConfig.put("maximumPoolSize", threadPoolConfigEntity.getMaximumPoolSize());
            poolConfig.put("poolSize", threadPoolConfigEntity.getPoolSize());
            poolConfig.put("activeCount", threadPoolConfigEntity.getActiveCount());
            poolConfig.put("queueType", threadPoolConfigEntity.getQueueType());
            poolConfig.put("queueSize", threadPoolConfigEntity.getQueueSize());
            poolConfig.put("remainingCapacity", threadPoolConfigEntity.getRemainingCapacity());

            configMap.put(threadPoolConfigEntity.getThreadPoolName(), poolConfig);

            // 转换为YAML
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            String content = yaml.dump(configMap);

            // 发布到Nacos
            boolean result = configService.publishConfig(
                    nacosConfigEntity.getNacosDataId() + "-" + threadPoolConfigEntity.getThreadPoolName(),
                    nacosConfigEntity.getNacosGroup(),
                    content
            );

            logger.info("动态线程池，上报线程池配置参数到Nacos结果：{}, 线程池名称：{}", result, threadPoolConfigEntity.getThreadPoolName());
        } catch (NacosException e) {
            logger.error("动态线程池，上报线程池配置参数到Nacos异常，线程池名称：{}", threadPoolConfigEntity.getThreadPoolName(), e);
        }
    }
}
