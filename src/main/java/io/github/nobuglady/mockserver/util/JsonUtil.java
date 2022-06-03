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
package io.github.nobuglady.mockserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json utils
 * 
 * @author NoBugLady
 *
 */
public class JsonUtil {

	/**
	 * convert json to object
	 * 
	 * @param <T>   object type
	 * @param json  json
	 * @param clazz object type
	 * @return object
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * convert object to json
	 * 
	 * @param obj object
	 * @return json
	 */
	public static String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
