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
package io.github.nobuglady.mockserver.persistance.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import io.github.nobuglady.mockserver.persistance.db.entity.ApiEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.CategoryEntity;
import io.github.nobuglady.mockserver.persistance.db.mapper.CategoryMapper;

/**
 * Category table operation class
 * 
 * @author NoBugLady
 *
 */
@Component
public class CategoryDao {

	@Autowired
	private ApiDao apiDao;
	
	@Autowired
	private CategoryMapper catagoryMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * select all root categories
	 * 
	 * @return categories
	 */
	public List<CategoryEntity> selectAllRootDir() {
		return catagoryMapper.selectAllRootDir();
	}

	/**
	 * select all categories
	 * 
	 * @param userId user id
	 * @param adminFlag admin flag
	 * @return categories
	 */
	public List<CategoryEntity> selectAll(int userId, Integer adminFlag) {

		List<CategoryEntity> catagoryEntityList = catagoryMapper.selectAll();
		
		List<String> userCatagoryIdList = selectUserCategoryList(userId);
		
		if(adminFlag == null || adminFlag != 1) {
			for(int i = catagoryEntityList.size()-1; i >= 0; i --) {
				CategoryEntity entity = catagoryEntityList.get(i);
				if(entity.parent.equals("1") && !userCatagoryIdList.contains(entity.id)) {
					catagoryEntityList.remove(i);
				}
			}
		}
		
		return catagoryEntityList;
	}
	
	/**
	 * select all categories
	 * 
	 * @param userId user id
	 * @return categories
	 */
	public List<CategoryEntity> selectAll() {

		return catagoryMapper.selectAll();
	}
	
	/**
	 * select user category list
	 * 
	 * @param userId user id
	 * @return user id list
	 */
	public List<String> selectUserCategoryList(int userId){

		return catagoryMapper.selectUserCategoryList(userId);
		
	}
	

	/**
	 * select user category update list
	 * 
	 * @param userId user id
	 * @return user id list
	 */
	public List<String> selectUserCategoryUpdateList(int userId){

		return catagoryMapper.selectUserCategoryUpdateList(userId);
		
	}

	/**
	 * delete categories
	 * 
	 * @param deleteIdList delete id list
	 */
	public void delete(List<String> deleteIdList) {
		
		List<String> strList = new ArrayList<>();
		for(String str:deleteIdList) {
			strList.add("'"+str+"'");
		}
		
		jdbcTemplate.execute("delete from category where id in ("+String.join(",", strList)+")");
		jdbcTemplate.execute("delete from api where api_id in ("+String.join(",", strList)+")");
		
	}

	/**
	 * rename categories
	 * 
	 * @param id id
	 * @param text text
	 */
	public void rename(String id, String text) {
		catagoryMapper.rename(id, text);
		
	}

	/**
	 * save categories
	 * 
	 * @param entity entity
	 */
	public void save(CategoryEntity entity) {
		
		catagoryMapper.save(entity);
		
		if ("file".equals(entity.type)) {

			// check api
			ApiEntity apiEntity = apiDao.selectApi(entity.id);
			if(apiEntity == null) {
				apiEntity = new ApiEntity();
				apiEntity.api_id = entity.id;
				apiEntity.req_method = "GET";
				apiEntity.req_path = "";
				apiEntity.api_status = "on";
				apiEntity.res_script = "response.body = {};";
				apiEntity.res_status = "200";
				apiEntity.res_content_type = "application/json";
				apiEntity.res_character_encoding = "UTF-8";
				apiEntity.res_body = "";
				apiDao.save(apiEntity);
			}
		}
	}

	/**
	 * move categories
	 * 
	 * @param id id
	 * @param parent new parent
	 */
	public void move(String id, String parent) {
		catagoryMapper.move(id, parent);
	}

}
