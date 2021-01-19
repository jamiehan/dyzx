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
 * @date 2021-01-18 13:02:06
 */
@Data
@TableName("onekeyalarm")
public class OnekeyalarmEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId("oneid")
	private Integer oneid;

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
	 * 结束时间
	 */
	@TableField("jieshushijian")
	private Date jieshushijian;

	/**
	 * 视频地址
	 */
	@TableField("shipinurl")
	private String shipinurl;

	/**
	 * 音频地址
	 */
	@TableField("yinpinurl")
	private String yinpinurl;

	/**
	 * 报警性质
	 */
	@TableField("baojingxingzhi")
	private Integer baojingxingzhi;

	/**
	 * 备注
	 */
	@TableField("beizhu")
	private String beizhu;


}
