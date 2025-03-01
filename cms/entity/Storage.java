package com.cms.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2025-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Storage对象", description="")
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "原料名")
    private String name;

    @ApiModelProperty(value = "当前库存")
    private BigDecimal current;

    @ApiModelProperty(value = "预警阈值")
    private BigDecimal warning;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "备注")
    private String remark;


}
