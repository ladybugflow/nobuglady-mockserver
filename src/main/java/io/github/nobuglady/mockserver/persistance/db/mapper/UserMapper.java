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
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import io.github.nobuglady.mockserver.persistance.db.entity.UserEntity;

/**
 * User table mapper class
 * 
 * @author NoBugLady
 *
 */
@Mapper
public interface UserMapper {

	@Insert("insert into tuser"
			+ " ("
			+ "user_id,"
			+ "user_name,"
			+ "email,"
			+ "passwd,"
			+ "remarks,"
			+ "admin_flag,"
			+ "create_time,"
			+ "update_time"
			+ " )"
			+ " values"
			+ " ("
			+ "#{user_id},"
			+ "#{user_name},"
			+ "#{email},"
			+ "#{passwd},"
			+ "#{remarks},"
			+ "#{admin_flag},"
			+ "now(),"
			+ "now()"
			+ " ) ")
	@Options(useGeneratedKeys=true, keyProperty="user_id")
	public void save(UserEntity entity);
	
	@Select("select * from tuser")
	public List<UserEntity> selectAll();
	
	@Select("select * from tuser where user_id = #{param1}")
	public UserEntity get(String userId);
	
	@Update("update tuser "
			+ " set user_name=#{user_name}, "
			+ " email=#{email}, "
			+ " passwd=#{passwd}, "
			+ " remarks=#{remarks}, "
			+ " update_time=now() "
			+ " where "
			+ " user_id = #{user_id}"
			+ " ")
	public void update(UserEntity userEntity);
	
	@Delete("delete from tuser where user_id = #{param1}")
	public void delete(String id);
	
	@Select("select "
			+ " * "
			+ " from "
			+ " tuser"
			+ " where"
			+ " email = #{param1}"
			+ " and passwd = #{param2}"
			+ " ")
	public UserEntity getByEmailPassword(String email, String password);
}
