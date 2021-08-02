
package com.cca.gateway.controller;


import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;
import com.cca.gateway.service.GatewayService;
import com.cca.gateway.utils.ResponseUtil;
import io.boncray.bean.mode.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * 控制器
 *
 * @author code-generator
 * @version 1.0
 * @date 2021-03-11
 */
@Slf4j
@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("ok");
    }

    @PostMapping("/update")
    public Mono<ServerResponse> update(@RequestBody Mono<Gateway> flux) {
        return flux.flatMap(gateway -> {
            log.info("rest update :{}", gateway);
            int i = gatewayService.updateByPk(gateway);
            return ResponseUtil.convertToJson(null);
        });
    }

    @GetMapping("/get/{id}")
    public Mono<ServerResponse> get(@PathVariable Long id) {
        Gateway gateway = gatewayService.getByKey(id);
        return ResponseUtil.convertToJson(Result.success(gateway));
    }

    @GetMapping("/list")
    public Mono<ServerResponse> list(@RequestBody Mono<GatewayQuery> mono) {
        return mono.flatMap(query -> {
            List<Gateway> list = gatewayService.listExample(query);
            return ResponseUtil.convertToJson(Result.success(list));
        });
    }

}