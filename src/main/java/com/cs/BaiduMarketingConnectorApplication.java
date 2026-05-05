package com.cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 百度营销连接器应用入口
 *
 * @author Livepulse
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class BaiduMarketingConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaiduMarketingConnectorApplication.class, args);
    }
}
