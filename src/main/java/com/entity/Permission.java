package com.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.vo.BaseEntity;

/**
 * 权限实体类
 * @author ljh
 */
public class Permission extends BaseEntity<Permission> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id")
	private Long id;
    /**
     * 上级ID
     */
	private Long pid;
    /**
     * 权限名
     */
	private String name;
    /**
     * 类型 0、目录 1、菜单 2、按钮
     */
	private Integer type;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 地址
     */
	private String url;
    /**
     * 权限编码
     */
	private String permCode;
    /**
     * 图标
     */
	private String icon;
    /**
     * 描述
     */
	private String description;
    /**
     * 状态 0、禁用 1、正常
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField(fill = FieldFill.INSERT)
	private Date gmtCreate;
    /**
     * 修改时间
     */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date gmtModified;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermCode() {
		return permCode;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Permission{" +
			"id=" + id +
			", pid=" + pid +
			", name=" + name +
			", type=" + type +
			", sort=" + sort +
			", url=" + url +
			", permCode=" + permCode +
			", icon=" + icon +
			", description=" + description +
			", status=" + status +
			", gmtCreate=" + gmtCreate +
			", gmtModified=" + gmtModified +
			"}";
	}
}
