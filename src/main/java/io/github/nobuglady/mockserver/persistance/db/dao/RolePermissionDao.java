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

import io.github.nobuglady.mockserver.persistance.db.entity.RolePermissionEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomRolePermissionEntity;
import io.github.nobuglady.mockserver.persistance.db.mapper.RolePermissionMapper;

/**
 * RolePermission table operation class
 * 
 * @author NoBugLady
 *
 */
@Component
public class RolePermissionDao {

	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	/**
	 * save list
	 * 
	 * @param entityList entity list
	 */
	public void saveList(List<RolePermissionEntity> entityList) {
		if(entityList != null) {
			for(RolePermissionEntity entity:entityList) {
				rolePermissionMapper.save(entity);
			}
		}
	}
	

	/**
	 * select all permission
	 * 
	 * @return permission list
	 */
	public List<CustomRolePermissionEntity> selectAll() {

		return rolePermissionMapper.selectAll();
	}

	/**
	 * delete role_permission by role_id
	 * 
	 * @param role_id role id
	 */
	public void deleteByRole(Integer role_id) {

		rolePermissionMapper.deleteByRole(role_id);
	}
}
