package com.cca.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.MD5;
import com.cca.gateway.mode.CommonConstants;
import com.cca.gateway.utils.ResponseUtil;
import io.boncray.bean.mode.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 过滤非法请求的filter
 *
 * @author cca
 * @date 2020/9/12 16:01
 */
@Component
@Slf4j
public class IllegalRequestFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        if (!this.legalRequest(serverHttpRequest.getHeaders())) {
            return ResponseUtil.convertToResponse(response, Result.failure(302, "非法请求"));
        }
        return chain.filter(exchange);
    }

    /**
     * 校验是否是合法请求
     */
    private boolean legalRequest(HttpHeaders headers) {
        List<String> timestampList = headers.get(CommonConstants.timestamp);
        List<String> signMarkList = headers.get(CommonConstants.signMark);

        if (CollectionUtil.isEmpty(timestampList) || timestampList.size() != 1) {
            return false;
        }
        if (CollectionUtil.isEmpty(signMarkList) || signMarkList.size() != 1) {
            return false;
        }
        // 原始请求时间戳
        String timestamp = timestampList.get(0);
        // 前端加密后的时间戳
        String signMark = signMarkList.get(0);

        timestamp = "sm" + timestamp;
        String digestHex = MD5.create().digestHex(timestamp);

        return digestHex.equals(signMark);
    }


    public static void main(String[] args) {
        String timestamp = "sm" + "1651890117341";
        System.out.println(MD5.create().digestHex(timestamp));
        System.out.println(MD5.create().digestHex(timestamp));
    }


    @Override
    public int getOrder() {
        return 2;
    }
}
