package com.cca.gateway.utils;

import io.netty.buffer.ByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cca
 * @version 1.0
 * @date 2021/3/8 17:42
 */
public class RequestUtil {

    private static final Pattern PATTERN = Pattern.compile("\\s*|\t|\r|\n");


    /**
     * 【不要使用这种】
     * <p>
     * 警告：
     * 此种方式读取body，目标服务报错！！！！！！
     */
    public static String resolveBodyFromRequest1(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }


    /**
     * 读取body内容
     *
     * @param serverHttpRequest
     * @return
     */
    public static String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        HttpMethod method = serverHttpRequest.getMethod();
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            //获取请求体
            Flux<DataBuffer> body = serverHttpRequest.getBody();
            StringBuilder sb = new StringBuilder();

            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                String bodyString = new String(bytes, StandardCharsets.UTF_8);
                sb.append(bodyString);
            });
            return formatStr(sb.toString());
        } else {
            return null;
        }

    }

    // 构建新的request对象 ----stringBuffer方法是上面的代码

    /**
     * 读取body后重新包装request
     */
    public static ServerHttpRequest wrapperNewRequest(String body, ServerHttpRequest request) {
        if (StringUtils.isEmpty(body)) {
            return request;
        }
        DataBuffer bodyDataBuffer = stringBuffer(body);
        Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

        request = new ServerHttpRequestDecorator(request) {
            @Override
            public Flux<DataBuffer> getBody() {
                return bodyFlux;
            }
        };
        return request;
    }


    //先将字符串写到字节缓冲
    private static DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }


    /**
     * 去掉空格,换行和制表符
     *
     * @param str
     * @return
     */
    private static String formatStr(String str) {
        if (str != null && str.length() > 0) {
            Matcher m = PATTERN.matcher(str);
            return m.replaceAll("");
        }
        return str;
    }
}
