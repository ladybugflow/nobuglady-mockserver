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

import io.github.nobuglady.mockserver.persistance.db.entity.UserRoleEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomUserRoleEntity;

/**
 * UserRole table mapper class
 * 
 * @author NoBugLady
 *
 */
@Mapper
public interface UserRoleMapper {

	@Insert("insert into user_role"
			+ " ("
			+ "user_id,"
			+ "role_id,"
			+ "create_time,"
			+ "update_time"
			+ " )"
			+ " values"
			+ " ("
			+ "#{user_id},"
			+ "#{role_id},"
			+ "now(),"
			+ "now()"
			+ " ) ")
	public void save(UserRoleEntity entity);
	
	@Select("select "
			+ " a.*,"
			+ " b.role_name as role_name"
			+ " from"
			+ " user_role a,"
			+ " trole b"
			+ " where"
			+ " a.role_id = b.role_id"
			+ " ")
	public List<CustomUserRoleEntity> selectAll();
	
	@Delete("delete "
			+ " from "
			+ " user_role"
			+ " where"
			+ " user_id = #{param1}")
	public void deleteByUser(Integer user_id);
}
