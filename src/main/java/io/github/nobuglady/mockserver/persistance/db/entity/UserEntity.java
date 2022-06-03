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
package io.github.nobuglady.mockserver.persistance.db.entity;

import java.util.Date;

/**
 * User entity class
 * 
 * @author NoBugLady
 *
 */
public class UserEntity {

	public Integer user_id;
	public String user_name;
	public String email;
	public String passwd;
	public String remarks;
	public Integer admin_flag;
	public Date create_time;
	public Date update_time;
}
