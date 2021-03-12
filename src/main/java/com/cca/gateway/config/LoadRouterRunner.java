package com.cca.gateway.config;

import com.cca.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author cca
 * @version 1.0
 * @date 2021/3/9 17:13
 */
@Component
public class LoadRouterRunner implements CommandLineRunner {

    @Autowired
    private GatewayService gatewayService;


    @Override
    public void run(String... args) {
        gatewayService.loadRouterDefinition();
    }
}
