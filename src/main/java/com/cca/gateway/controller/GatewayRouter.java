package com.cca.gateway.controller;

import com.cca.gateway.controller.handler.GatewayHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


/**
 * @author cca
 * @version 1.0
 * @date 2021/8/2 14:50
 */
@Configuration
public class GatewayRouter {


    @Autowired
    private GatewayHandler gatewayHandler;


    /**
     * webFlux 函数式编程，路由处理
     */
    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunction() {

        return RouterFunctions.route(RequestPredicates.POST("/list"), req -> gatewayHandler.list(req))
                .andRoute(RequestPredicates.GET("/get/{id}"), req -> gatewayHandler.get(req))
                .andRoute(RequestPredicates.POST("/update"), req -> gatewayHandler.update(req))
                .andRoute(RequestPredicates.GET("/enable/{id}"), req -> gatewayHandler.enable(req))
                .andRoute(RequestPredicates.GET("/disable/{id}"), req -> gatewayHandler.disable(req))
                // 下面测试示例
                .andRoute(RequestPredicates.GET("/interval"), req -> gatewayHandler.sendTimePerSec(req));
    }


}
