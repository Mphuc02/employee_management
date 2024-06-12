package com.example.employee_management.dto;

import com.example.employee_management.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends BaseObjectDTO{
	private String name;
	private String code;

	public CategoryDto() {
		super();
	}
	public CategoryDto(Category entity) {
		if(entity != null) {
			this.setId(entity.getId());
			this.code = entity.getCode();
			this.name = entity.getName();
		}
	}
}
