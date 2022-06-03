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
package io.github.nobuglady.mockserver.persistance.db.entity.custom;

import java.util.Date;

/**
 * RolePermission entity class
 * 
 * @author NoBugLady
 *
 */
public class CustomRolePermissionEntity {

	public Integer role_id;
	public Integer permission_id;
	public String permission_name;
	public Date create_time;
	public Date update_time;

}
