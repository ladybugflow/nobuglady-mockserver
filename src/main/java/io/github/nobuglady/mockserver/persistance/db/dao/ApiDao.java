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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.nobuglady.mockserver.persistance.db.entity.ApiEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomCatagoryApiEntity;
import io.github.nobuglady.mockserver.persistance.db.mapper.ApiMapper;

/**
 * Api table operation class
 * 
 * @author NoBugLady
 *
 */
@Component
public class ApiDao {

	@Autowired
	private ApiMapper apiMapper;
	
	/**
	 * select api
	 * 
	 * @param apiId api id
	 * @return api entity
	 */
	public ApiEntity selectApi(String apiId) {
        return apiMapper.selectApi(apiId);
	}

	/**
	 * select api by url and method
	 * 
	 * @param url    url
	 * @param method method
	 * @return api entity
	 */
	public List<ApiEntity> selectByUrlMethod(String url, String method) {
        return apiMapper.selectByUrlMethod(url, method);
	}


	/**
	 * select dynamic api by method
	 * 
	 * @param url    url
	 * @param method method
	 * @return api entity
	 */
	public List<ApiEntity> selectDynamicUrlByMethod(String method) {
        return apiMapper.selectDynamicUrlByMethod(method);
	}

	/**
	 * save api
	 * 
	 * @param entity entity
	 */
	public void save(ApiEntity entity) {
		setReqPathQuery(entity);
		apiMapper.save(entity);
	}

	/**
	 * update api
	 * 
	 * @param entity entity
	 */
	public void update(ApiEntity entity) {
		setReqPathQuery(entity);
		apiMapper.update(entity);
	}

	/**
	 * search api
	 * 
	 * @param apiPath api path
	 * @return search result
	 */
	public List<CustomCatagoryApiEntity> searchApi(String apiPath) {
		return apiMapper.searchAPi(apiPath);
	}
	
	/**
	 * set req_path_query
	 * 
	 * @param ApiEntity ApiEntity
	 */
	private void setReqPathQuery(ApiEntity apiEntity) {
		
		String reqPath = apiEntity.req_path;
		
		if(reqPath == null) {
			return;
		}
		
		int from = 0;
		int dynamic_url_flag = 0;
		while(reqPath.indexOf("{",from) != -1 && reqPath.indexOf("}",from) != -1) {
			int left = reqPath.indexOf("{");
			int right = reqPath.indexOf("}");
			if(right > left) {
				
				reqPath = reqPath.substring(0, left) + ".*" + reqPath.substring(right+1);
				dynamic_url_flag = 1;
			}else {
				from = left + 1;
			}
		}
		
		apiEntity.req_path_query = reqPath;
		apiEntity.dynamic_url_flag = dynamic_url_flag;
	}

}
