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
 * @date 2020-12-30 15:25:30
 */
@Data
@TableName("blacklist")
public class BlacklistEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 黑名单编号
	 */
	@TableId("blackid")
	private String blackid;

	/**
	 * 姓名
	 */
	@TableField("xingming")
	private String xingming;

	/**
	 * 性别
	 */
	@TableField("xingbie")
	private Integer xingbie;

//	private String strSex;

	/**
	 * 身份证
	 */
	@TableField("shenfenzheng")
	private String shenfenzheng;

	/**
	 * 出生日期
	 */
	@TableField("chushengriqi")
	private Date chushengriqi;

	/**
	 * 查控类型
	 */
	@TableField("chakonglx")
	private Integer chakonglx;

	/**
	 * 交控单位
	 */
	@TableField("jiaokongdw")
	private String jiaokongdw;

	/**
	 * 交控日期
	 */
	@TableField("jiaokongriqi")
	private Date jiaokongriqi;

	/**
	 * 单位电话
	 */
	@TableField("danweidh")
	private String danweidh;

	/**
	 * 处置类型
	 */
	@TableField("chuzhi")
	private Integer chuzhi;

	/**
	 * 备注
	 */
	@TableField("beizhu")
	private String beizhu;

	/**
	 * 人脸识别唯一ID
	 */
	@TableField("person_id")
	private String personId;


}
