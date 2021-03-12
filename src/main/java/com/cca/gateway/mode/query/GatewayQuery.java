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

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "")
    private String uri;

    @ApiModelProperty(value = "")
    private String predicates;

    @ApiModelProperty(value = "")
    private String filters;

    @ApiModelProperty(value = "")
    private String text;

    @ApiModelProperty(value = "")
    private String regId;

    @ApiModelProperty(value = "")
    private Boolean enabled;

    @ApiModelProperty(value = "")
    private Boolean oauth2;


}