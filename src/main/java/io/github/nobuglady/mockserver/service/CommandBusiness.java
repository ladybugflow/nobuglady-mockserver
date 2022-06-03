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
 * the License for the specific language governing users and limitations under the License.
 */
package io.github.nobuglady.mockserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nobuglady.mockserver.persistance.db.dao.ApiDao;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomCatagoryApiEntity;

/**
 * Command business class
 * 
 * @author NoBugLady
 *
 */
@Service
@Transactional
public class CommandBusiness {


	@Autowired
	private ApiDao apiDao;
	
	/**
	 * search api
	 * 
	 * @param apiPath api path
	 * @return search result
	 */
	public List<CustomCatagoryApiEntity> searchApi(String apiPath) {
		return apiDao.searchApi(apiPath);
	}

}
