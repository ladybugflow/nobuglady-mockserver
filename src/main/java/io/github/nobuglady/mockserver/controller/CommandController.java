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
package io.github.nobuglady.mockserver.controller;

import java.util.List;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.nobuglady.mockserver.ext.cache.CacheManager;
import io.github.nobuglady.mockserver.persistance.db.entity.custom.CustomCatagoryApiEntity;
import io.github.nobuglady.mockserver.service.CommandBusiness;

/**
 * Command controller
 * 
 * @author NoBugLady
 *
 */
@Controller
public class CommandController {

	@Autowired
	private CommandBusiness commandBusiness;
	
	/**
	 * execute command
	 * 
	 * @param command        command
	 * @param apiId          apiId
	 * @return execute result
	 */
	@RequestMapping(path = "/command", method = RequestMethod.GET)
	@ResponseBody
	public String complete(@RequestParam("word") String command,
			@RequestParam("apiId") String apiId) {

		System.out.println("word:"+command);
		System.out.println("apiId:"+apiId);
		
		command = decode(command);

		String inputCommand = command;
		
		if(command == null || "".equals(command.trim())) {
			return "";
		}
		
		command = command.trim();
		
		if("help".equals(command)) {
			String result = "-------------";
			result +="\nhelp";
			result +="\nsearch <apiPath>";
			result +="\ncache *";
			result +="\ncache <key>";
			result +="\ncache clear *";
			result +="\ncache clear <key>";
			result +="\ncache save <key> <value>";
			result += "\n-------------";
			return result;
		}
		
		if(command.startsWith("cache")) {
			
			String temp = command.substring("cache".length()).trim();
			
			String param1 = temp.split(" ")[0];
			String param2 = temp.substring(param1.length()).trim();
			
			if("clear".equals(param1)) {
				if("*".equals(param2)) {
					CacheManager.getInstance().clearAll();
					return "";
				}else {
					CacheManager.getInstance().clearCache(param2);
					return "";
				}
			}else if("save".equals(param1)){
				String[] keyValues = param2.split(" ");
				String key = keyValues[0];
				
				String value = null;
				if(key.equals(param2.trim())) {
					value = null;
				}else {
					value = keyValues[keyValues.length-1];
				}
				CacheManager.getInstance().saveCache(key, value);
				return "";
			}else {
				if("*".equals(param1)) {
					List<String> resultList = CacheManager.getInstance().getAll();
					return String.join("\n", resultList);
				}else {
					Object cacheValueObj = CacheManager.getInstance().getCache(param1);
					if(cacheValueObj == null) {
						return "";
					}else {
				        ScriptEngineManager manager = new ScriptEngineManager();
				        ScriptEngine engine = manager.getEngineByName("javascript");
				        ScriptContext ctx = new SimpleScriptContext();
				        ctx.setAttribute("cache", CacheManager.getInstance(), ScriptContext.ENGINE_SCOPE);
				        try {
							engine.eval("var temp = cache.getCache('"+param1+"')",ctx);
							return (String)engine.eval("cache.getString(JSON.stringify(temp))",ctx);
						} catch (ScriptException e) {
							e.printStackTrace();
							return e.getMessage();
						}
						
					}
				}
			}
			
		}
		
		if(command.startsWith("search")) {
			String apiPath = command.substring("search".length()).trim();
			
			List<CustomCatagoryApiEntity> apiEntityList = commandBusiness.searchApi(apiPath);
			if(apiEntityList == null || apiEntityList.size() == 0) {
				return "no result found for api:"+apiPath;
			}
			
			StringBuffer result = new StringBuffer();
			
			for(CustomCatagoryApiEntity apiEntity:apiEntityList) {
				result.append(apiEntity.catagory_text + "," + apiEntity.api_status + "," + apiEntity.req_method + "," + apiEntity.req_path);
				result.append("\n");
			}
			
			return "find " + apiEntityList.size() + " result.\n" + result.toString();
		}
		
		return "Unknow command:"+inputCommand;
	}

	/**
	 * encode a string
	 * 
	 * @param str string
	 * @return encoded string
	 */
	@SuppressWarnings("deprecation")
	public String encode(String str) {
		return java.net.URLEncoder.encode(str).replaceAll("\\+", "%20").replaceAll("\\%21", "!")
				.replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
	}

	/**
	 * decode a string
	 * 
	 * @param str string
	 * @return decoded string
	 */
	@SuppressWarnings("deprecation")
	public String decode(String str) {
		return java.net.URLDecoder.decode(str);
	}

}
