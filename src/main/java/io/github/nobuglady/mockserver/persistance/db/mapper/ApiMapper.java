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

import io.github.nobuglady.mockserver.persistance.db.entity.ApiEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomCatagoryApiEntity;

/**
 * Api table mapper class
 * 
 * @author NoBugLady
 *
 */
@Mapper
public interface ApiMapper {

	@Select("SELECT * FROM api "
			+ " WHERE"
			+ " api_id = #{param1}")
	public ApiEntity selectApi(String apiId);
	
	@Select("select * from api "
			+ " where "
			+ " req_path_query=#{param1} "
			+ " and req_method=#{param2}"
			+ " and api_status='on'"
			+ " order by update_time desc")
	public List<ApiEntity> selectByUrlMethod(String url, String method);

	@Select("select * from api "
			+ " where "
			+ " req_method=#{param1}"
			+ " and api_status='on'"
			+ " and dynamic_url_flag=1"
			+ " order by update_time desc")
	public List<ApiEntity> selectDynamicUrlByMethod(String method);
	
	@Insert("insert into api"
			+ " ("
			+ "api_id,"
			+ "api_status,"
			+ "req_path,"
			+ "req_path_query,"
			+ "dynamic_url_flag,"
			+ "req_method,"
			+ "res_script,"
			+ "res_status,"
			+ "res_content_type,"
			+ "res_character_encoding,"
			+ "res_body,"
			+ "res_headers,"
			+ "res_cookies,"
			+ "create_time,"
			+ "update_time"
			+ " )"
			+ " values"
			+ " ("
			+ "#{api_id},"
			+ "#{api_status},"
			+ "#{req_path},"
			+ "#{req_path_query},"
			+ "#{dynamic_url_flag},"
			+ "#{req_method},"
			+ "#{res_script},"
			+ "#{res_status},"
			+ "#{res_content_type},"
			+ "#{res_character_encoding},"
			+ "#{res_body},"
			+ "#{res_headers},"
			+ "#{res_cookies},"
			+ "now(),"
			+ "now()"
			+ " ) ")
	public void save(ApiEntity entity);
	
	@Update("update api "
			+ " set"
			+ " api_status=#{api_status}"
			+ " ,req_path=#{req_path}"
			+ " ,req_path_query=#{req_path_query}"
			+ " ,dynamic_url_flag=#{dynamic_url_flag}"
			+ " ,req_method=#{req_method}"
			+ " ,res_script=#{res_script}"
			+ " ,res_status=#{res_status}"
			+ " ,res_content_type=#{res_content_type}"
			+ " ,res_character_encoding=#{res_character_encoding}"
			+ " ,res_body=#{res_body}"
			+ " ,res_headers=#{res_headers}"
			+ " ,res_cookies=#{res_cookies}"
			+ " ,update_time=now()"
			+ " where"
			+ " api_id=#{api_id}")
	public void update(ApiEntity entity);

	@Select("select "
			+ " b.id as catagory_id,"
			+ " b.text as catagory_text,"
			+ " a.*"
			+ " from "
			+ " api a, category b "
			+ " where "
			+ " a.api_id = b.id "
			+ " and a.req_path like '%${param1}%' "
			+ " order by update_time desc")
	public List<CustomCatagoryApiEntity> searchAPi(String apiPath);
}
