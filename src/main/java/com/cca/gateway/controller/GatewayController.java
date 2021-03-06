
package com.cca.gateway.controller;


import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;
import com.cca.gateway.service.GatewayService;
import io.boncray.bean.mode.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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


    @PostMapping("/update")
    public Mono<Result<Boolean>> update(@RequestBody Mono<Gateway> mono) {
        return mono.flatMap(gateway -> Mono.just(Result.success(gatewayService.update(gateway))));
    }

    @PostMapping("/list")
    public Mono<Result<List<Gateway>>> list(@RequestBody Mono<GatewayQuery> mono) {
        return mono.flatMap(query -> {
            List<Gateway> list = gatewayService.listExample(query);
            return Mono.just(Result.success(list));
        });
    }

    @GetMapping("/get/{id}")
    public Mono<Result<Gateway>> get(@PathVariable Long id) {
        Gateway gateway = gatewayService.getByKey(id);
        return Mono.just(Result.success(gateway));
    }

    @GetMapping("/enable/{id}")
    public Mono<Result<Boolean>> enable(@PathVariable Long id) {
        return Mono.just(Result.success(gatewayService.enable(id)));
    }

    @GetMapping("/disable/{id}")
    public Mono<Result<Boolean>> disable(@PathVariable Long id) {
        return Mono.just(Result.success(gatewayService.disable(id)));
    }

}