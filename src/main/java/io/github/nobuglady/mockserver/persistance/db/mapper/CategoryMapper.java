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

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import io.github.nobuglady.mockserver.persistance.db.entity.CategoryEntity;

/**
 * Category table mapper class
 * 
 * @author NoBugLady
 *
 */
@Mapper
public interface CategoryMapper {

	@Insert("insert into category"
			+ " ("
			+ "id,"
			+ "text,"
			+ "type,"
			+ "parent,"
			+ "disabled,"
			+ "create_time,"
			+ "update_time"
			+ " )"
			+ " values"
			+ " ("
			+ "#{id},"
			+ "#{text},"
			+ "#{type},"
			+ "#{parent},"
			+ "#{disabled},"
			+ "now(),"
			+ "now()"
			+ " ) ")
	public void save(CategoryEntity entity);
	
	@Select("select * from category where parent = '1'")
	public List<CategoryEntity> selectAllRootDir();
	
	@Select("select * from category")
	public List<CategoryEntity> selectAll();
	
	@Select("select"
			+ " pc.category_id"
			+ " from "
			+ " permission_category pc,"
			+ " role_permission rp,"
			+ " user_role ur"
			+ " where"
			+ " pc.permission_id = rp.permission_id"
			+ " and rp.role_id = ur.role_id"
			+ " and (pc.can_read = 1 or pc.can_update=1)"
			+ " and ur.user_id = #{param1}"
			+ " ")
	public List<String> selectUserCategoryList(int userId);
	
	@Select("select"
			+ " pc.category_id"
			+ " from"
			+ " permission_category pc,"
			+ " role_permission rp,"
			+ " user_role ur"
			+ " where"
			+ " pc.permission_id = rp.permission_id"
			+ " and rp.role_id = ur.role_id"
			+ " and (pc.can_update=1)"
			+ " and ur.user_id = #{param1}"
			+ " ")
	public List<String> selectUserCategoryUpdateList(int userId);

	@Update("update category set text = #{param2} where id = #{param1}")
	public void rename(String id, String text);

	@Update("update category set parent = #{param2} where id = #{param1}")
	public void move(String id, String parent);
}
