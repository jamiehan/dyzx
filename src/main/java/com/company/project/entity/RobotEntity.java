package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.entity.BaseEntity;
import java.math.BigDecimal;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author Jamie
 * @email *****@mail.com
 * @date 2021-01-12 10:42:11
 */
@Data
@TableName("robot")
public class RobotEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 机器人信息编号
	 */
	@TableId("id")
	private String id;

	/**
	 * 机器人型号
	 */
	@TableField("xinghao")
	private String xinghao;

	/**
	 * 机器人编号
	 */
	@TableField("bianhao")
	private String bianhao;

	/**
	 * 机器人IP地址
	 */
	@TableField("ipaddress")
	private String ipaddress;

	/**
	 * 出厂日期
	 */
	@TableField("chuchangriqi")
	private String chuchangriqi;

	/**
	 * 硬件版本号
	 */
	@TableField("yingjianbanb")
	private String yingjianbanb;

	/**
	 * 软件版本号
	 */
	@TableField("ruanjianbanb")
	private String ruanjianbanb;

	/**
	 * 所属单位
	 */
	@TableField("suosudw")
	private String suosudw;

	/**
	 * 工作地点
	 */
	@TableField("gongzuodidian")
	private String gongzuodidian;

	/**
	 * 联系人
	 */
	@TableField("contact")
	private String contact;

	/**
	 * 电话
	 */
	@TableField("telephone")
	private String telephone;

	/**
	 * 在线状态
	 */
	@TableField("zaixianzhuangtai")
	private Integer zaixianzhuangtai;

	/**
	 * 运行里程
	 */
	@TableField("yunxinglicheng")
	private BigDecimal yunxinglicheng;

	/**
	 * 备注
	 */
	@TableField("beizhu")
	private String beizhu;


}
