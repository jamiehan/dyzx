package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-01-18 15:24:22
 */
@Data
@TableName("alarminfo")
public class AlarminfoEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 黑名单表
	 */
	@TableId("alarmid")
	private Integer alarmid;

	/**
	 * 黑名单id
	 */
	@TableField("blackid")
	private Integer blackid;

	/**
	 * 黑名单姓名
	 */
	@TableField("blackname")
	private String blackname;

	/**
	 * 机器人编号
	 */
	@TableField("bianhao")
	private String bianhao;

	/**
	 * 报警时间
	 */
	@TableField("baojingshijian")
	private Date baojingshijian;

	/**
	 * 报警地点
	 */
	@TableField("baojingdidian")
	private String baojingdidian;

	/**
	 * 报警坐标
	 */
	@TableField("baojingzuobiao")
	private String baojingzuobiao;

	/**
	 * 备注
	 */
	@TableField("beizhu")
	private String beizhu;

	/**
	 * 图片地址
	 */
	@TableField("baojingPIC")
	private String baojingPIC;

	/**
	 * 报警类型
	 */
	@TableField("baojingType")
	private Integer baojingType;


}
