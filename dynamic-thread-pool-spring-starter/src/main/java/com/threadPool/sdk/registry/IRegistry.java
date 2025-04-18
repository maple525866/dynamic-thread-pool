package com.threadPool.sdk.registry;

import com.threadPool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author maple
 * @Description 注册中心接口
 * @createTime:2025-04-18 20:47
 */
public interface IRegistry {

    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
