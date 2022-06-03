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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nobuglady.mockserver.persistance.db.dao.CategoryDao;
import io.github.nobuglady.mockserver.persistance.db.dao.PermissionCategoryDao;
import io.github.nobuglady.mockserver.persistance.db.dao.PermissionDao;
import io.github.nobuglady.mockserver.persistance.db.entity.CategoryEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.PermissionCategoryEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.PermissionEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomPermissionCategoryEntity;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionDeleteRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionDeleteResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadCategoryRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadCategoryResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadListRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadListResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionSaveRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionSaveRequestDtoCategory;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionSaveResponseDto;

/**
 * Home business class
 * 
 * @author NoBugLady
 *
 */
@Service
@Transactional
public class AdminPermissionBusiness {

	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private PermissionCategoryDao permissionCategoryDao;
	
	@Autowired
	private CategoryDao catagoryDao;
	
	/**
	 * request permission delete
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestPermissionDelete(PermissionDeleteRequestDto requestDto,
			PermissionDeleteResponseDto responseDto) {
		permissionDao.delete(requestDto.id);
		permissionCategoryDao.deleteByPermission(Integer.parseInt(requestDto.id));
	}

	/**
	 * request permission list
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestPermissionList(PermissionLoadListRequestDto requestDto,
			PermissionLoadListResponseDto responseDto) {

		List<PermissionEntity> permissionList = permissionDao.selectAll();
		List<CustomPermissionCategoryEntity> permissionCategoryList = permissionCategoryDao.selectAll();
		
		Map<String, String> permissionDetailMap = new HashMap<>();
		Map<String, List<CustomPermissionCategoryEntity>> permissionDetailListMap = new HashMap<>();
		for(CustomPermissionCategoryEntity entity:permissionCategoryList) {
			String detail = permissionDetailMap.get(entity.permission_id.toString());
			if(detail == null) {
				permissionDetailMap.put(entity.permission_id.toString(), getPermissionString(entity.category_name,entity.can_read,entity.can_update));
			}else {
				permissionDetailMap.put(entity.permission_id.toString(), detail + "/" + getPermissionString(entity.category_name,entity.can_read,entity.can_update));
			}
			
			List<CustomPermissionCategoryEntity> detailList = permissionDetailListMap.get(entity.permission_id.toString());
			if(detailList == null) {
				detailList = new ArrayList<CustomPermissionCategoryEntity>();
			}
			
			detailList.add(entity);
			permissionDetailListMap.put(entity.permission_id.toString(), detailList);
			
		}
		
		responseDto.permissionList.addAll(permissionList);
		responseDto.permissionDetailMap = permissionDetailMap;
		responseDto.permissionDetailListMap = permissionDetailListMap;
	}

	/**
	 * get permission string
	 * 
	 * @param categoryName category name
	 * @param canRead can read
	 * @param canUpdate can update
	 * @return
	 */
	private String getPermissionString(String categoryName, Integer canRead, Integer canUpdate) {
		 if(canRead + canUpdate == 0) {
			 return categoryName + ":" + "[-]";
		 }
		 
		 if(canRead + canUpdate == 2) {
			 return categoryName + ":" + "[R,U]";
		 }
		 
		 if(canRead == 1) {
			 return categoryName + ":" + "[R]";
		 }
		 
		 if(canUpdate == 1) {
			 return categoryName + ":" + "[U]";
		 }
		 
		 return "";
	}
	
	/**
	 * request category list
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void requestCategoryList(PermissionLoadCategoryRequestDto requestDto,
			PermissionLoadCategoryResponseDto responseDto) {
		
		List<CategoryEntity> rootCategoryList = catagoryDao.selectAllRootDir();
		responseDto.categoryList.addAll(rootCategoryList);
		
	}

	/**
	 * save permission
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void savePermission(PermissionSaveRequestDto requestDto, PermissionSaveResponseDto responseDto) {
		
		PermissionEntity permissionEntity = null;
		
		if(requestDto.permissionId != null && !requestDto.permissionId.equals("") && !requestDto.permissionId.equals("null")) {

			permissionEntity = permissionDao.get(requestDto.permissionId);
			permissionEntity.permission_name = requestDto.permissionName;
			permissionEntity.remarks = requestDto.permissionRemarks;
			permissionDao.update(permissionEntity);
			
		}else {

			permissionEntity = new PermissionEntity();
			permissionEntity.permission_name = requestDto.permissionName;
			permissionEntity.remarks = requestDto.permissionRemarks;
			permissionDao.save(permissionEntity);
			
		}
		
		permissionCategoryDao.deleteByPermission(permissionEntity.permission_id);
		
		if(requestDto.permissionCategoryList != null) {
			
			List<PermissionCategoryEntity> entityList = new ArrayList<>();
			
			for(PermissionSaveRequestDtoCategory requestInfo:requestDto.permissionCategoryList) {
				PermissionCategoryEntity entity = new PermissionCategoryEntity();
				entity.permission_id = permissionEntity.permission_id;
				entity.category_id = requestInfo.catelogId;
				entity.can_read = requestInfo.canRead;
				entity.can_update = requestInfo.canUpdate;
				
				entityList.add(entity);
			}
			
			if(entityList.size() > 0) {
				permissionCategoryDao.saveList(entityList);
			}
			
		}
	}

}
