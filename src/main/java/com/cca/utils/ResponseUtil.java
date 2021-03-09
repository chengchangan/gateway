package com.cca.utils;

import com.alibaba.fastjson.JSON;
import com.cca.mode.response.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author cca
 * @version 1.0
 * @date 2021/3/9 16:40
 */
public class ResponseUtil {

    public static DataBuffer buildFailResponse(ServerHttpResponse response, Result<?> result) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String fastResult = JSON.toJSONString(result);
        return response.bufferFactory().allocateBuffer().write(fastResult.getBytes(StandardCharsets.UTF_8));
    }


}
