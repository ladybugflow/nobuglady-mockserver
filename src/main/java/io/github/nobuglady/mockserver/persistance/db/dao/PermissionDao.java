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

import io.github.nobuglady.mockserver.persistance.db.entity.PermissionEntity;
import io.github.nobuglady.mockserver.persistance.db.mapper.PermissionMapper;

/**
 * Permission table operation class
 * 
 * @author NoBugLady
 *
 */
@Component
public class PermissionDao {

	@Autowired
	private PermissionMapper permissionMapper;
	
	/**
	 * save entity
	 * 
	 * @param entity entity
	 */
	public void save(PermissionEntity entity) {
		permissionMapper.save(entity);
	}

	/**
	 * select all permission
	 * 
	 * @return permission list
	 */
	public List<PermissionEntity> selectAll() {

		return permissionMapper.selectAll();
	}

	/**
	 * select permission by id
	 * 
	 * @param permissionId permission id
	 * @return permission
	 */
	public PermissionEntity get(String permissionId) {

		return permissionMapper.get(permissionId);
	}

	/**
	 * update permission
	 * 
	 * @param permissionEntity permission
	 */
	public void update(PermissionEntity permissionEntity) {

		permissionMapper.update(permissionEntity);
	}

	/**
	 * delete permission
	 * 
	 * @param id permission id
	 */
	public void delete(String id) {

		permissionMapper.delete(id);
	}
}
