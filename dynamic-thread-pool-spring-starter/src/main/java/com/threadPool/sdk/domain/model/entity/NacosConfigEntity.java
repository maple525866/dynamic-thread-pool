package com.threadPool.sdk.domain.model.entity;

/**
 * @author maple
 * @Description nacos配置实体
 * @createTime:2025-05-24 20:00
 */
public class NacosConfigEntity {
    /** nacos服务地址 */
    private String nacosServerAddr;

    /** nacos命名空间 */
    private String nacosNamespace;

    /** nacos分组 */
    private String nacosGroup;

    /** nacos配置文件 */
    private String nacosDataId;

    public NacosConfigEntity() {

    }
    public NacosConfigEntity(String nacosServerAddr, String nacosNamespace, String nacosGroup, String nacosDataId) {
        this.nacosServerAddr = nacosServerAddr;
        this.nacosNamespace = nacosNamespace;
        this.nacosGroup = nacosGroup;
        this.nacosDataId = nacosDataId;
    }

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
