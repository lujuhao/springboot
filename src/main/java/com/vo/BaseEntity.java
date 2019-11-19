package com.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("rawtypes")
public abstract class BaseEntity<T extends Model> extends Model<T>{

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@TableField(exist=false)
	private Page<T> page;

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}
	
	
}
