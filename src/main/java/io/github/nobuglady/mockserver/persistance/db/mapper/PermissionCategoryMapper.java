/*
 * Copyright (c) 2021-present, NoBugLady-mockserver Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package io.github.nobuglady.mockserver.persistance.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import io.github.nobuglady.mockserver.persistance.db.entity.PermissionCategoryEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomPermissionCategoryEntity;

/**
 * PermissionCategory table mapper class
 * 
 * @author NoBugLady
 *
 */
@Mapper
public interface PermissionCategoryMapper {
	
	@Insert("insert into permission_category"
			+ " ("
			+ "permission_id,"
			+ "category_id,"
			+ "can_read,"
			+ "can_update,"
			+ "create_time,"
			+ "update_time"
			+ " )"
			+ " values"
			+ " ("
			+ "#{permission_id},"
			+ "#{category_id},"
			+ "#{can_read},"
			+ "#{can_update},"
			+ "now(),"
			+ "now()"
			+ " ) ")
	public void save(PermissionCategoryEntity entity);
	
	@Select("select "
			+ " a.*,"
			+ " b.`text` as category_name"
			+ " from"
			+ " permission_category a,"
			+ " category b"
			+ " where"
			+ " a.category_id = b.id")
	public List<CustomPermissionCategoryEntity> selectAll();
	
	@Delete("delete "
			+ " from "
			+ " permission_category"
			+ " where"
			+ " permission_id = #{param1}")
	public void deleteByPermission(Integer permission_id);
	
}
