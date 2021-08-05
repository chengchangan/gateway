package com.cca.gateway.exception;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cca.gateway.utils.ResponseUtil;
import io.boncray.bean.constants.LogConstant;
import io.boncray.bean.mode.log.TrackMetric;
import io.boncray.bean.mode.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Slf4j
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {

    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    private List<ViewResolver> viewResolvers = Collections.emptyList();

    private static final String responseDate = "responseDate";

    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        log.info("global exception:{} \n", exchange.getRequest().getPath(), throwable);
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(throwable);
        }
        // 封装返回结果对象,缓存再exchange中
        exchange.getAttributes().putIfAbsent(responseDate, JSONUtil.toJsonStr(parseResponseData(exchange, throwable)));

        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
                .route(newRequest)
                .switchIfEmpty(Mono.error(throwable))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));
    }


    /**
     * 创建错误的结果对象
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        String errorResult = Objects.requireNonNull(request.exchange().getAttribute(responseDate)).toString();
        return ResponseUtil.convertToResponse(JSONUtil.toBean(errorResult, Result.class));
    }


    /**
     * 创建视图,写出去
     */
    private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GlobalGatewayExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return GlobalGatewayExceptionHandler.this.viewResolvers;
        }
    }

    /**
     * 封装返回结果对象
     */
    protected Result<String> parseResponseData(ServerWebExchange exchange, Throwable throwable) {
        Long requestId = -1L;
        String trackMetric = exchange.getRequest().getHeaders().getFirst(LogConstant.TRACK_METRIC);
        if (StrUtil.isNotBlank(trackMetric)) {
            requestId = JSONUtil.toBean(trackMetric, TrackMetric.class).getCurrentTrackId();
        }

        // 根据异常,处理返回结果
        int code = 500;
        String msg;
        if (throwable instanceof NotFoundException) {
            NotFoundException notFoundException = (NotFoundException) throwable;
            code = notFoundException.getStatus().value();
            msg = notFoundException.getReason();
        } else if (throwable instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
            code = responseStatusException.getStatus().value();
            msg = responseStatusException.getReason();
        } else {
            msg = throwable.getMessage();
        }

        Result<String> result = Result.failure(code, msg);
        result.setRequestId(requestId);
        return result;
    }
}

