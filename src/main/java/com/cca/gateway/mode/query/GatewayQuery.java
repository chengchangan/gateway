package com.cca.gateway.mode.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * query
 *
 * @author code-generator
 * @version 1.0
 * @date 2021-03-11
 */
@Data
@EqualsAndHashCode()
public class GatewayQuery {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "目标服务")
    private String uri;

    @ApiModelProperty(value = "断言-匹配规则")
    private String predicates;

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


}