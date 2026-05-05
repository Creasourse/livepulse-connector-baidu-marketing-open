package com.cs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 百度营销连接器配置
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "baidu.marketing")
@Data
public class BaiduMarketingConfig {

    /**
     * API 配置
     */
    private ApiConfig api = new ApiConfig();

    /**
     * 同步配置
     */
    private SyncConfig sync = new SyncConfig();

    /**
     * 加密配置
     */
    private EncryptionConfig encryption = new EncryptionConfig();

    /**
     * Webhook 配置
     */
    private WebhookConfig webhook = new WebhookConfig();

    /**
     * API 配置
     */
    @Data
    public static class ApiConfig {
        /**
         * API 版本
         */
        private String version = "1.0";

        /**
         * API 基础 URL
         */
        private String baseUrl = "https://api.baidu.com";

        /**
         * 超时时间（秒）
         */
        private Integer timeout = 30;

        /**
         * 连接池大小
         */
        private Integer maxConnections = 20;

        /**
         * 每个路由的最大连接数
         */
        private Integer maxConnectionsPerRoute = 10;
    }

    /**
     * 同步配置
     */
    @Data
    public static class SyncConfig {
        /**
         * 批量大小
         */
        private Integer batchSize = 100;

        /**
         * 最大重试次数
         */
        private Integer maxRetries = 3;

        /**
         * 重试间隔（毫秒）
         */
        private Long retryInterval = 1000L;

        /**
         * 并发线程数
         */
        private Integer threadPoolSize = 5;
    }

    /**
     * 加密配置
     */
    @Data
    public static class EncryptionConfig {
        /**
         * 默认加密类型
         */
        private String defaultType = "MD5";

        /**
         * 是否自动加密
         */
        private Boolean autoEncrypt = true;

        /**
         * 是否验证格式
         */
        private Boolean validateFormat = true;
    }

    /**
     * Webhook 配置
     */
    @Data
    public static class WebhookConfig {
        /**
         * Webhook 路径
         */
        private String path = "/baidu-marketing/webhook";

        /**
         * 是否启用验证
         */
        private Boolean enableVerification = true;

        /**
         * 签名密钥
         */
        private String signatureKey;

        /**
         * 重试次数
         */
        private Integer maxRetries = 3;
    }
}
