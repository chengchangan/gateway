package com.cca.gateway.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.cca.core.sequence.IdGenerator;
import com.cca.gateway.dao.GatewayMapper;
import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;
import com.cca.gateway.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * service实现
 *
 * @author code-generator
 * @version 1.0
 * @date 2021-03-11
 */
@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {


    @Autowired
    private IdGenerator idGenerator;
    @Resource
    private GatewayMapper gatewayMapper;
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private ApplicationEventPublisher publisher;


    @Override
    public void loadRouterDefinition() {
        List<Gateway> allGateway = this.listExample(new GatewayQuery());
        if (CollectionUtil.isEmpty(allGateway)) {
            return;
        }
        try {
            for (Gateway gateway : allGateway) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gateway.getId().toString());
                Map<String, Object> map = new HashMap<>(4);
                map.put("serviceName", gateway.getText());
                map.put("serviceNameEn", gateway.getRegId());
                definition.setMetadata(map);
                definition.setUri(new URI(gateway.getUri()));
                List<PredicateDefinition> predicateDefinitions = gateway.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = gateway.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }

                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            }
        } catch (Exception e) {
            log.error("加载路由规则失败：", e);
        }
    }


    @Override
    public int insert(Gateway entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.next());
        }
        return gatewayMapper.insert(entity);
    }

    @Override
    public int updateByPk(Gateway entity) {
        return gatewayMapper.updateByPk(entity, entity.getId());
    }

    @Override
    public int deleteByPk(Long pk) {
        return gatewayMapper.deleteByPk(pk);
    }

    @Override
    public Gateway getByKey(Long id) {
        return gatewayMapper.getByKey(id, "");
    }

    @Override
    public List<Gateway> listExample(GatewayQuery query) {
        return gatewayMapper.list(query);
    }


}
