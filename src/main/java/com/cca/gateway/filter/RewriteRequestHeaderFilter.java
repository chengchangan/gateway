package com.cca.gateway.filter;

import com.cca.gateway.utils.RequestUtil;
import io.boncray.core.sequence.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 改写请求头信息 filter
 *
 * @author cca
 * @date 2020/9/12 16:01
 */
@Component
@Slf4j
public class RewriteRequestHeaderFilter implements GlobalFilter, Ordered {

    @Resource
    private IdGenerator normalIdGenerator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // todo 测试代码  此处获取了body
//        String bodyStr = RequestUtil.resolveBodyFromRequest(serverHttpRequest);
//        log.info("request body：{}", bodyStr);
//
//         将body重新包装到request
//        ServerHttpRequest httpRequest = RequestUtil.wrapperNewRequest(bodyStr, serverHttpRequest);
//        return chain.filter(exchange.mutate().request(httpRequest).build());
        return chain.filter(exchange);
    }

    /**
     * 增加请求头参数
     */
    private ServerWebExchange rewriteRequest(ServerWebExchange exchange) {
//        Consumer<HttpHeaders> httpHeaders = httpHeader -> httpHeader.set(LogConstant.TRACK_ID, String.valueOf(idGenerator.next()));
//        ServerHttpRequest host = exchange.getRequest().mutate().headers(httpHeaders).build();
//        return exchange.mutate().request(host).build();
        return exchange;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
