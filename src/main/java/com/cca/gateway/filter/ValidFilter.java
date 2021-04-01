package com.cca.gateway.filter;

import com.cca.mode.response.Result;
import com.cca.gateway.utils.RequestUtil;
import com.cca.gateway.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 鉴权 filter
 *
 * @author cca
 * @date 2020/9/12 16:01
 */
@Component
@Slf4j
public class ValidFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String bodyStr = RequestUtil.resolveBodyFromRequest(serverHttpRequest);

        if (bodyStr == null) {
            log.info("********** valid失败 ************");
            DataBuffer dataBuffer = ResponseUtil.buildResponse(response, Result.failure(302, "valid失败"));
            return response.writeWith(Mono.just(dataBuffer));
        } else {
            log.info("********** valid成功 ************");
            log.info("body：{}", bodyStr);
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
