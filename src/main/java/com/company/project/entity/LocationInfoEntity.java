package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-02-25 16:56:48
 */
@Data
@TableName("location_info")
public class LocationInfoEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId("id")
	private String id;

	/**
	 * 机器人编号
	 */
	@TableField("robot_code")
	private String robotCode;

	/**
	 * 位置名称
	 */
	@TableField("pos_name")
	private String posName;

	/**
	 * 位置x坐标
	 */
	@TableField("pos_x")
	private BigDecimal posX;

	/**
	 * 位置y坐标
	 */
	@TableField("pos_y")
	private BigDecimal posY;

	/**
	 * 位置z坐标
	 */
	@TableField("pos_z")
	private BigDecimal posZ;

	/**
	 * 方向x坐标
	 */
	@TableField("ori_x")
	private BigDecimal oriX;

	/**
	 * 方向y坐标
	 */
	@TableField("ori_y")
	private BigDecimal oriY;

	/**
	 * 方向z坐标
	 */
	@TableField("ori_z")
	private BigDecimal oriZ;

	/**
	 * 方向w坐标
	 */
	@TableField("ori_w")
	private BigDecimal oriW;

	/**
	 * 创建人
	 */
	@TableField("creator")
	private String creator;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 修改人
	 */
	@TableField("updater")
	private String updater;

	/**
	 * 修改时间
	 */
	@TableField("update_time")
	private Date updateTime;


}
