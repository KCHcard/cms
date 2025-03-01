package com.cms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zhang MX
 * @since 2025-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Members对象", description="")
public class Members implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号")
    private String id;

    private BigDecimal consumption;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latest;

    private Integer point;

    @ApiModelProperty(value = "存储菜品偏好数据")
    private Integer preference;


}
