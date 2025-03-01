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
 * @since 2025-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Dish对象", description="")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String dishimg;

    private String category;

    private String name;

    private BigDecimal piece;

    @ApiModelProperty(value = "0在售，1售罄，2下架")
    private Integer dishcondition;

    private String composition;


}
