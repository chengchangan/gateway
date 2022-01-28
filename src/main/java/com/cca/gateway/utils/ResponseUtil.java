package com.cca.gateway.utils;

import cn.hutool.json.JSONUtil;
import io.boncray.bean.mode.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author cca
 * @version 1.0
 * @date 2021/3/9 16:40
 */
public class ResponseUtil {

    /**
     * 转换webFlux response响应结果
     *
     * @param response 响应对象
     * @param result   响应结果
     * @return 转换后 webFlux 响应的response
     */
    public static Mono<Void> convertToResponse(ServerHttpResponse response, Result<?> result) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(
                Mono.just(response.bufferFactory()
                        .allocateBuffer()
                        .write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8))));
    }


    /**
     * 转换webFlux response响应结果
     *
     * @param data 响应结果
     * @return 转换后 webFlux 响应的response
     */
    public static Mono<ServerResponse> convertToResponse(Result<?> data) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), data.getClass());
    }

}
