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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nobuglady.mockserver.persistance.db.dao.RoleDao;
import io.github.nobuglady.mockserver.persistance.db.dao.UserDao;
import io.github.nobuglady.mockserver.persistance.db.dao.UserRoleDao;
import io.github.nobuglady.mockserver.persistance.db.entity.RoleEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.UserEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.UserRoleEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomUserRoleEntity;
import io.github.nobuglady.mockserver.service.dto.admin.UserDeleteRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserDeleteResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserLoadListRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserLoadListResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserLoadRoleRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserLoadRoleResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserSaveRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.UserSaveRequestDtoRole;
import io.github.nobuglady.mockserver.service.dto.admin.UserSaveResponseDto;

/**
 * Home business class
 * 
 * @author NoBugLady
 *
 */
@Service
@Transactional
public class AdminUserBusiness {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * request user delete
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestUserDelete(UserDeleteRequestDto requestDto,
			UserDeleteResponseDto responseDto) {
		userDao.delete(requestDto.id);
		userRoleDao.deleteByUser(Integer.parseInt(requestDto.id));
	}

	/**
	 * request user list
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestUserList(UserLoadListRequestDto requestDto,
			UserLoadListResponseDto responseDto) {

		List<UserEntity> userList = userDao.selectAll();
		List<CustomUserRoleEntity> userRoleList = userRoleDao.selectAll();
		
		Map<String, String> userDetailMap = new HashMap<>();
		Map<String, List<CustomUserRoleEntity>> userDetailListMap = new HashMap<>();
		for(CustomUserRoleEntity entity:userRoleList) {
			String detail = userDetailMap.get(entity.user_id.toString());
			if(detail == null) {
				userDetailMap.put(entity.user_id.toString(),entity.role_name);
			}else {
				userDetailMap.put(entity.user_id.toString(),detail+"/"+entity.role_name);
			}
			
			List<CustomUserRoleEntity> detailList = userDetailListMap.get(entity.user_id.toString());
			if(detailList == null) {
				detailList = new ArrayList<CustomUserRoleEntity>();
			}
			
			detailList.add(entity);
			userDetailListMap.put(entity.user_id.toString(), detailList);
			
		}
		
		responseDto.userList.addAll(userList);
		responseDto.userDetailMap = userDetailMap;
		responseDto.userDetailListMap = userDetailListMap;
	}

	/**
	 * request category list
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestRoleList(UserLoadRoleRequestDto requestDto,
			UserLoadRoleResponseDto responseDto) {
		
		List<RoleEntity> rootRoleList = roleDao.selectAll();
		responseDto.roleList.addAll(rootRoleList);
		
	}

	/**
	 * save user
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void saveUser(UserSaveRequestDto requestDto, UserSaveResponseDto responseDto) {
		
		UserEntity userEntity = null;
		
		if(requestDto.userId != null && !requestDto.userId.equals("") && !requestDto.userId.equals("null")) {

			userEntity = userDao.get(requestDto.userId);
			userEntity.user_name = requestDto.userName;
			userEntity.email = requestDto.email;
			userEntity.passwd = requestDto.passwd;
			userEntity.remarks = requestDto.userRemarks;
			userDao.update(userEntity);
			
		}else {

			userEntity = new UserEntity();
			userEntity.user_name = requestDto.userName;
			userEntity.email = requestDto.email;
			userEntity.passwd = requestDto.passwd;
			userEntity.remarks = requestDto.userRemarks;
			userDao.save(userEntity);
			
		}
		
		userRoleDao.deleteByUser(userEntity.user_id);
		
		if(requestDto.userRoleList != null) {
			
			List<UserRoleEntity> entityList = new ArrayList<>();
			
			for(UserSaveRequestDtoRole requestInfo:requestDto.userRoleList) {
				UserRoleEntity entity = new UserRoleEntity();
				entity.user_id = userEntity.user_id;
				entity.role_id = Integer.valueOf(requestInfo.roleId);
				
				entityList.add(entity);
			}
			
			if(entityList.size() > 0) {
				userRoleDao.saveList(entityList);
			}
			
		}
	}

}
