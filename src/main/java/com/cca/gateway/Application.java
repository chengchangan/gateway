package com.cca.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author cca
 * @version 1.0
 * @date 2021/3/5 9:21
 */
@EnableFeignClients(basePackages = "com.cca")
@SpringBootApplication(scanBasePackages = "com.cca")
@EnableDiscoveryClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
