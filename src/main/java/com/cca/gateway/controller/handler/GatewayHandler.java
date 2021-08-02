package com.cca.gateway.controller.handler;

import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;
import com.cca.gateway.service.GatewayService;
import com.cca.gateway.utils.ResponseUtil;
import io.boncray.bean.mode.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;


/**
 * webFlux 函数式编程，web处理器
 *
 * @author cca
 * @version 1.0
 * @date 2021/8/2 14:47
 */
@Slf4j
@Component
public class GatewayHandler {

    @Autowired
    private GatewayService gatewayService;

    /**
     * 读取body参数，进行update
     */
    public Mono<ServerResponse> update(ServerRequest request) {
        // 读取body参数
        return request.bodyToMono(Gateway.class)
                .flatMap(gateway -> {
                    log.info("update :{}", gateway);
                    int update = gatewayService.update(gateway);
                    return ResponseUtil.convertToJson(Result.success(update));
                });
    }

    /**
     * 读取body参数，query
     */
    public Mono<ServerResponse> list(ServerRequest request) {
        // bodyToMono.flatMap 方式读取 body
        return request.bodyToMono(GatewayQuery.class)
                .flatMap(query -> ResponseUtil.convertToJson(Result.success(gatewayService.listExample(query))));
    }

    /**
     * 获取url参数
     */
    public Mono<ServerResponse> get(ServerRequest request) {
        String id = request.pathVariable("id");
        return ResponseUtil.convertToJson(Result.success(gatewayService.getByKey(Long.valueOf(id))));
    }

    /**
     * 启用
     */
    public Mono<ServerResponse> enable(ServerRequest request) {
        String id = request.pathVariable("id");
        return ResponseUtil.convertToJson(Result.success(gatewayService.enable(Long.valueOf(id))));
    }

    /**
     * 禁用
     */
    public Mono<ServerResponse> disable(ServerRequest request) {
        String id = request.pathVariable("id");
        return ResponseUtil.convertToJson(Result.success(gatewayService.disable(Long.valueOf(id))));
    }

    /**
     * 每隔1秒，返回一次数据
     * <p>
     * data:2021-08-02T16:17:30.429812500
     * <p>
     * data:2021-08-02T16:17:31.429869700
     * <p>
     * data:2021-08-02T16:17:32.429926900
     */
    public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM).body(
                        Flux.interval(Duration.ofSeconds(1)).map(l -> LocalDateTime.now().toString()), String.class);
    }


}
