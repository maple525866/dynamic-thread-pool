package com.threadPool.sdk.domain;

import com.threadPool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author maple
 * @Description 动态线程池服务
 * @createTime:2025-04-14 22:50
 */
public interface IDynamicThreadPoolService {
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);
}
