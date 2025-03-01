package com.cms.entity;

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
@ApiModel(value="Dishcategory对象", description="")
public class Dishcategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类名称")
    private String name;


}
