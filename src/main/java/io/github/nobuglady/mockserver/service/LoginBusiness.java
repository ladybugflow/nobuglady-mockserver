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
package io.github.nobuglady.mockserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nobuglady.mockserver.persistance.db.dao.UserDao;
import io.github.nobuglady.mockserver.persistance.db.entity.UserEntity;
import io.github.nobuglady.mockserver.security.AuthHolder;

/**
 * Login business class
 * 
 * @author NoBugLady
 *
 */
@Service
@Transactional
public class LoginBusiness {

	@Autowired
	private UserDao userDao;
	
	/**
	 * login
	 * 
	 * @param sessionId session id
	 * @param email email
	 * @param password password
	 * @return 0:success, 1:failed
	 */
	public int login(String sessionId, String email, String password) {
		
		UserEntity userEntity = userDao.getByEmailPassword(email, password);
		
		if(userEntity == null) {
			return 1;
		}else {
			AuthHolder.saveUser(sessionId, userEntity);
			return 0;
		}
		
	}

}
