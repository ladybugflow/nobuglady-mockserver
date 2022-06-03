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
package io.github.nobuglady.mockserver.security;

import java.util.HashMap;
import java.util.Map;

import io.github.nobuglady.mockserver.persistance.db.entity.UserEntity;

/**
 * AuthHolder
 * 
 * @author NoBugLady
 *
 */
public class AuthHolder {

	private static Map<String,UserEntity> userMap = new HashMap<>();
	
	/**
	 * save user
	 * 
	 * @param sessionId session id
	 * @param userEntity user entity
	 */
	public static void saveUser(String sessionId,UserEntity userEntity) {
		userMap.put(sessionId, userEntity);
	}
	
	/**
	 * get user
	 * 
	 * @param sessionId session id
	 * @return user entity
	 */
	public static UserEntity getUser(String sessionId) {
		return userMap.get(sessionId);
	}
	
	/**
	 * remove user
	 * 
	 * @param seesionId session id
	 */
	public static void removeUser(String seesionId) {
		userMap.remove(seesionId);
	}
	
	/**
	 * get user menu
	 * 
	 * @param sessionId session id
	 * @return user menu
	 */
	public static String getUserMenu(String sessionId) {
		UserEntity userEntity = userMap.get(sessionId);
		if(userEntity == null) {
			return "";
		}
		
		if(userEntity.admin_flag != null && userEntity.admin_flag == 1) {
			return ""+userEntity.email+" | <a href='/logout'>logout</a>";
		}else {
			return ""+userEntity.email+" | <a href='/logout'>logout</a>";
		}
	}
}
