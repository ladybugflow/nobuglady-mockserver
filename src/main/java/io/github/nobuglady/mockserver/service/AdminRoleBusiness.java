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
 * the License for the specific language governing roles and limitations under the License.
 */
package io.github.nobuglady.mockserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nobuglady.mockserver.persistance.db.dao.PermissionDao;
import io.github.nobuglady.mockserver.persistance.db.dao.RoleDao;
import io.github.nobuglady.mockserver.persistance.db.dao.RolePermissionDao;
import io.github.nobuglady.mockserver.persistance.db.entity.PermissionEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.RoleEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.RolePermissionEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomRolePermissionEntity;
import io.github.nobuglady.mockserver.service.dto.admin.RoleDeleteRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleDeleteResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleLoadListRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleLoadListResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleLoadPermissionRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleLoadPermissionResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleSaveRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.RoleSaveRequestDtoPermission;
import io.github.nobuglady.mockserver.service.dto.admin.RoleSaveResponseDto;

/**
 * Home business class
 * 
 * @author NoBugLady
 *
 */
@Service
@Transactional
public class AdminRoleBusiness {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RolePermissionDao rolePermissionDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	/**
	 * request role delete
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestRoleDelete(RoleDeleteRequestDto requestDto,
			RoleDeleteResponseDto responseDto) {
		roleDao.delete(requestDto.id);
		rolePermissionDao.deleteByRole(Integer.parseInt(requestDto.id));
	}

	/**
	 * request role list
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestRoleList(RoleLoadListRequestDto requestDto,
			RoleLoadListResponseDto responseDto) {

		List<RoleEntity> roleList = roleDao.selectAll();
		List<CustomRolePermissionEntity> rolePermissionList = rolePermissionDao.selectAll();
		
		Map<String, String> roleDetailMap = new HashMap<>();
		Map<String, List<CustomRolePermissionEntity>> roleDetailListMap = new HashMap<>();
		for(CustomRolePermissionEntity entity:rolePermissionList) {
			String detail = roleDetailMap.get(entity.role_id.toString());
			if(detail == null) {
				roleDetailMap.put(entity.role_id.toString(),entity.permission_name);
			}else {
				roleDetailMap.put(entity.role_id.toString(),detail+"/"+entity.permission_name);
			}
			
			List<CustomRolePermissionEntity> detailList = roleDetailListMap.get(entity.role_id.toString());
			if(detailList == null) {
				detailList = new ArrayList<CustomRolePermissionEntity>();
			}
			
			detailList.add(entity);
			roleDetailListMap.put(entity.role_id.toString(), detailList);
			
		}
		
		responseDto.roleList.addAll(roleList);
		responseDto.roleDetailMap = roleDetailMap;
		responseDto.roleDetailListMap = roleDetailListMap;
	}

	/**
	 * request category list
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestPermissionList(RoleLoadPermissionRequestDto requestDto,
			RoleLoadPermissionResponseDto responseDto) {
		
		List<PermissionEntity> rootPermissionList = permissionDao.selectAll();
		responseDto.permissionList.addAll(rootPermissionList);
		
	}

	/**
	 * save role
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void saveRole(RoleSaveRequestDto requestDto, RoleSaveResponseDto responseDto) {
		
		RoleEntity roleEntity = null;
		
		if(requestDto.roleId != null && !requestDto.roleId.equals("") && !requestDto.roleId.equals("null")) {

			roleEntity = roleDao.get(requestDto.roleId);
			roleEntity.role_name = requestDto.roleName;
			roleEntity.remarks = requestDto.roleRemarks;
			roleDao.update(roleEntity);
			
		}else {

			roleEntity = new RoleEntity();
			roleEntity.role_name = requestDto.roleName;
			roleEntity.remarks = requestDto.roleRemarks;
			roleDao.save(roleEntity);
			
		}
		
		rolePermissionDao.deleteByRole(roleEntity.role_id);
		
		if(requestDto.rolePermissionList != null) {
			
			List<RolePermissionEntity> entityList = new ArrayList<>();
			
			for(RoleSaveRequestDtoPermission requestInfo:requestDto.rolePermissionList) {
				RolePermissionEntity entity = new RolePermissionEntity();
				entity.role_id = roleEntity.role_id;
				entity.permission_id = Integer.valueOf(requestInfo.permissionId);
				
				entityList.add(entity);
			}
			
			if(entityList.size() > 0) {
				rolePermissionDao.saveList(entityList);
			}
			
		}
	}

}
