package com.cca.gateway.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import com.cca.gateway.dao.GatewayMapper;
import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;
import com.cca.gateway.service.GatewayService;
import io.boncray.core.sequence.IdGenerator;
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
import java.net.URISyntaxException;
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
        allGateway.stream().filter(x -> BooleanUtil.isTrue(x.getEnabled())).forEach(this::loadRouterDefinition);
    }

    @Override
    public int insert(Gateway entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.next());
        }
        int result = gatewayMapper.insert(entity);
        this.loadRouterDefinition(entity);
        return result;
    }

    @Override
    public boolean update(Gateway entity) {
        int update = this.updateByPk(entity);
        this.loadRouterDefinition(entity);
        return update > 0;
    }


    @Override
    public int deleteByPk(Long pk) {
        int delete = gatewayMapper.deleteByPk(pk);
        this.deleteRouterDefinition(pk);
        return delete;
    }

    @Override
    public boolean enable(Long pk) {
        Gateway gateway = getByKey(pk);
        if (BooleanUtil.isTrue(gateway.getEnabled())) {
            return true;
        }
        gateway.setEnabled(true);
        int update = updateByPk(gateway);
        this.loadRouterDefinition(gateway);
        return update > 0;
    }

    @Override
    public boolean disable(Long pk) {
        Gateway gateway = getByKey(pk);
        if (BooleanUtil.isFalse(gateway.getEnabled())) {
            return true;
        }
        gateway.setEnabled(false);
        int update = updateByPk(gateway);
        this.deleteRouterDefinition(pk);
        return update > 0;
    }

    @Override
    public Gateway getByKey(Long id) {
        return gatewayMapper.getByKey(id, "");
    }

    @Override
    public List<Gateway> listExample(GatewayQuery query) {
        return gatewayMapper.list(query);
    }

    private int updateByPk(Gateway entity) {
        return gatewayMapper.updateByPk(entity, entity.getId());
    }

    /**
     * 加载网关路由
     */
    private void loadRouterDefinition(Gateway gateway) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gateway.getId().toString());
        Map<String, Object> map = new HashMap<>(4);
        map.put("serviceName", gateway.getText());
        map.put("serviceNameEn", gateway.getRegId());
        definition.setMetadata(map);
        try {
            definition.setUri(new URI(gateway.getUri()));
        } catch (URISyntaxException e) {
            log.error("加载路由规则失败：", e);
            return;
        }
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


    /**
     * 删除gateway路由
     */
    private void deleteRouterDefinition(Long gatewayId) {
        if (gatewayId == null) {
            return;
        }
        routeDefinitionWriter.delete(Mono.just(gatewayId.toString())).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
