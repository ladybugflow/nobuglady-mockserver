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
package io.github.nobuglady.mockserver.logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * ConsoleLogger
 * 
 * @author NoBugLady
 *
 */
public class ConsoleLogger {

	private CircularLinkedList logList;
	
	private static Map<String, ConsoleLogger> instanceMap = new HashMap<>();
	
	/**
	 * constructor
	 */
	private ConsoleLogger() {
		Resource resource = new ClassPathResource("/application.properties");
		Integer logSize = 64;
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			logSize = Integer.parseInt(props.getOrDefault("log.size", 64).toString());
			System.out.println("log size:"+logSize);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logList = new CircularLinkedList(logSize);
	}
	
	/**
	 * get instance
	 * 
	 * @param method method
	 * @param path path
	 * @return instance
	 */
	public static synchronized ConsoleLogger getInstance(String apiId) {
		String instanceId = apiId;
		ConsoleLogger instance = instanceMap.get(instanceId);
		if(instance == null) {
			instance = new ConsoleLogger();
			instanceMap.put(instanceId, instance);
		}
		return instance;
	}
	
	/**
	 * info
	 * 
	 * @param message message
	 */
	public void info(String message) {
		logList.append(message);
	}
	
	/**
	 * get message list
	 * 
	 * @return message list
	 */
	public List<String> getMessages(){
		return logList.readAll();
	}

	/**
	 * clear log
	 */
	public void clear() {
		logList.clear();
	}
	

}
