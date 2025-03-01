package com.cms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2025-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Lists对象", description="")
public class Lists implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "20231108001格式")
    private Long id;

    private String memberid;

    @TableField("`table`")
    private Integer table;

    @TableField("`sum`")
    private BigDecimal sum;

    @ApiModelProperty(value = "0未支付，1已支付，2退款")
    @TableField("`listcondition`")
    private Integer listcondition;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;


}
