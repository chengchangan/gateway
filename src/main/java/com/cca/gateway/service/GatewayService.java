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

    int insert(Gateway entity);

    int updateByPk(Gateway entity);

    int deleteByPk(Long pk);

    Gateway getByKey(Long id);

    List<Gateway> listExample(GatewayQuery query);

    void loadRouterDefinition();

}