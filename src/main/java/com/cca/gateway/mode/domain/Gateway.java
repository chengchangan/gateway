package com.cca.gateway.mode.domain;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author code-generator
 * @version 1.0
 * @date 2021-03-11
 */
@Data
@EqualsAndHashCode()
public class Gateway implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "目标服务")
    private String uri;

    /**
     * "predicates":[{
     *      "name":"Path"
     *      "args":{
     *          "pattern":"/product/**"
     *      }
     * }]
     *
     * 就是匹配所有　以/product开头的路由，转发为 /**
     *
     * 例如：http://localhost:8080/product/spu/getByid
     *
     */
    @ApiModelProperty(value = "断言-匹配规则")
    private String predicates;


    /**
     * "filters":[{
     *      "name":"StripPrefix"
     *      "args":{
     *          "_genkey_0":"1"
     *      }
     * }]
     *
     * 跳过匹配的前缀
     *
     * 例如：
     *      原：http://localhost:8080/product/spu/getByid
     *      转发后：lb://目标服务/spu/getByid
     *
     */
    @ApiModelProperty(value = "过滤-替换目标访问地址")
    private String filters;

    @ApiModelProperty(value = "服务名称")
    private String text;

    @ApiModelProperty(value = "服务id")
    private String regId;

    @ApiModelProperty(value = "启用")
    private Boolean enabled;

    @ApiModelProperty(value = "授权")
    private Boolean oauth2;

    public List<PredicateDefinition> getPredicateDefinition() {
        if (this.predicates != null) {
            return JSON.parseArray(this.predicates, PredicateDefinition.class);
        } else {
            return new ArrayList<>();
        }
    }

    public List<FilterDefinition> getFilterDefinition() {
        if (this.filters != null) {
            return JSON.parseArray(this.filters, FilterDefinition.class);
        } else {
            return new ArrayList<>();
        }
    }
}