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
package io.github.nobuglady.mockserver.service.dto.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.nobuglady.mockserver.persistance.db.entity.RoleEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomRolePermissionEntity;

/**
 * Role load list response dto class
 * 
 * @author NoBugLady
 *
 */
public class RoleLoadListResponseDto {

	public List<RoleEntity> roleList = new ArrayList<>();
	public Map<String, String> roleDetailMap = new HashMap<>();
	public Map<String, List<CustomRolePermissionEntity>> roleDetailListMap = new HashMap<>();

}
