package com.cca.gateway.dao;

import com.cca.gateway.mode.domain.Gateway;
import com.cca.gateway.mode.query.GatewayQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dao
 *
 * @author code-generator
 * @version 1.0
 * @date 2021-03-11
 */
@Mapper
public interface GatewayMapper {

    /**
     * 插入.
     */
    int insert(@Param("entity") Gateway entity);


    /**
     * 根据主键更新
     */
    int updateByPk(@Param("entity") Gateway entity, @Param("pk") Long pk);


    /**
     * 根据主键删除记录
     */
    int deleteByPk(@Param("pk") Long pk);

    /**
     * 根据主键查询
     */
    Gateway getByKey(@Param("pk") Long pk, @Param("fields") String fields);

    /**
     * 列表查询
     */
    List<Gateway> list(@Param("ew") GatewayQuery query);
}