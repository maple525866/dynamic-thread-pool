package com.threadPool.sdk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author maple
 * @Description nacos配置
 * @createTime:2025-05-24 17:28
 */
@ConfigurationProperties(prefix = "spring.cloud.nacos.config", ignoreInvalidFields = true)
public class NacosDynamicThreadPoolAutoProperties {
    /** nacos服务地址 */
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosServerAddr;

    /** nacos命名空间 */
    @Value("${spring.cloud.nacos.config.namespace}")
    private String nacosNamespace;

    /** nacos分组 */
    @Value("${spring.cloud.nacos.config.group}")
    private String nacosGroup;

    /** nacos配置文件 */
    @Value("${spring.cloud.nacos.config.data-id}")
    private String nacosDataId;

    public String getNacosServerAddr() {
        return nacosServerAddr;
    }

    public void setNacosServerAddr(String nacosServerAddr) {
        this.nacosServerAddr = nacosServerAddr;
    }

    public String getNacosNamespace() {
        return nacosNamespace;
    }

    public void setNacosNamespace(String nacosNamespace) {
        this.nacosNamespace = nacosNamespace;
    }

    public String getNacosGroup() {
        return nacosGroup;
    }

    public void setNacosGroup(String nacosGroup) {
        this.nacosGroup = nacosGroup;
    }

    public String getNacosDataId() {
        return nacosDataId;
    }

    public void setNacosDataId(String nacosDataId) {
        this.nacosDataId = nacosDataId;
    }
}
