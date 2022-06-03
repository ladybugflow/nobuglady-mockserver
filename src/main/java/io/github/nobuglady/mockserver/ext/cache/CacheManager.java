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
package io.github.nobuglady.mockserver.ext.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CacheManager
 * 
 * @author NoBugLady
 *
 */
public class CacheManager {

	private static CacheManager instance = new CacheManager();

	private Map<String, Object> cacheMap = new HashMap<>();

	/**
	 * constructor
	 */
	private CacheManager() {
	}

	/**
	 * get instance
	 * 
	 * @return instance
	 */
	public static CacheManager getInstance() {
		return instance;
	}

	/**
	 * save cache
	 * 
	 * @param key   key
	 * @param value value
	 */
	public void saveCache(String key, Object value) {
		this.cacheMap.put(key, value);
	}

	/**
	 * get cache
	 * 
	 * @param key key
	 * @return value
	 */
	public Object getCache(String key) {
		return cacheMap.get(key);
	}

	/**
	 * get all cache
	 * 
	 * @return cache list
	 */
	public List<String> getAll() {
		List<String> result = new ArrayList<>();

		for (Map.Entry<String, Object> entry : cacheMap.entrySet()) {
			result.add("key:" + entry.getKey());
		}

		return result;
	}

	/**
	 * clear cache by key
	 * 
	 * @param key key
	 */
	public void clearCache(String key) {
		this.cacheMap.remove(key);
	}

	/**
	 * clear all cache
	 */
	public void clearAll() {
		this.cacheMap = new HashMap<>();
	}

	/**
	 * get string value
	 * 
	 * @param str string
	 * @return value
	 */
	public String getString(String str) {
		return str;
	}
}
