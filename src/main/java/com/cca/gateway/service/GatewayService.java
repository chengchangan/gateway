package com.cca.gateway.service;

import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;

import java.util.List;

/**
 * service
 *
 * @author code-generator
 * @version 1.0
 * @date 2021-03-11
 */
public interface GatewayService {

    /**
     * 加载全部路由
     */
    void loadRouterDefinition();

    /**
     * 新增网关路由
     */
    int insert(Gateway entity);

    /**
     * 更新网关路由
     */
    boolean update(Gateway entity);

    /**
     * 删除网关路由
     */
    int deleteByPk(Long pk);

    /**
     * 启用
     */
    boolean enable(Long pk);

    /**
     * 禁用
     */
    boolean disable(Long pk);

    /**
     * 网关单个查询
     */
    Gateway getByKey(Long id);

    /**
     * 网关列表查询
     */
    List<Gateway> listExample(GatewayQuery query);


}