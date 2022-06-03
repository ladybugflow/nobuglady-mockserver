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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Expression auto complete controller
 * 
 * @author NoBugLady
 *
 */
@Controller
public class ExpController {

	Map<String,List<String>> wordSuggestionMap = new HashMap<>();
	
	/**
	 * constructor
	 */
	public ExpController() {
		List<String> requestList = new ArrayList<>();
		requestList.add("sessionId");
		requestList.add("method");
		requestList.add("url");
		requestList.add("header");
		requestList.add("cookie");
		requestList.add("parameter");
		requestList.add("body");
		wordSuggestionMap.put("request", requestList);
		
		List<String> responseList = new ArrayList<>();
		responseList.add("status");
		responseList.add("header");
		responseList.add("cookie");
		responseList.add("body");
		wordSuggestionMap.put("response", responseList);
		
		List<String> consoleList = new ArrayList<>();
		consoleList.add("log(");
		wordSuggestionMap.put("console", consoleList);
		
		List<String> cacheList = new ArrayList<>();
		cacheList.add("saveCache(");
		cacheList.add("getCache(");
		cacheList.add("clearCache(");
		cacheList.add("clearAll(");
		wordSuggestionMap.put("cache", cacheList);
		
		List<String> dbList = new ArrayList<>();
		dbList.add("query(");
		dbList.add("update(");
		wordSuggestionMap.put("db", dbList);
		
		List<String> fileList = new ArrayList<>();
		fileList.add("write(");
		fileList.add("read(");
		wordSuggestionMap.put("file", fileList);
		
		List<String> threadList = new ArrayList<>();
		threadList.add("sleep(");
		wordSuggestionMap.put("thread", threadList);
		
	}
	
	/**
	 * auto complete method
	 * 
	 * @param flowId      flow id
	 * @param instanceId  instance id
	 * @param nodeId      node id
	 * @param nodeRefName node refname
	 * @param word        word
	 * @param cursor      current position
	 * @return code suggestion
	 */
	@RequestMapping(path = "/complete", method = RequestMethod.GET)
	@ResponseBody
	public String complete(@RequestParam("word") String word) {

		System.out.println("word:"+word);
		
		word = decode(word);

		List<String> suggestions = wordSuggestionMap.get(word);

		if(suggestions == null) {
			suggestions = new ArrayList<>();
		}
		
		String result = "[";
		for (int i = 0; i < suggestions.size(); i++) {
			if (i > 0) {
				result += ",";
			}

			String suggestion = suggestions.get(i);
			String wordData = "{\"word\":\"" + suggestion
					+ "\",\"freq\":24,\"score\":300,\"flags\":\"bc\",\"syllables\":\"1\"}";

			result += wordData;
		}
		result += "]";
		return result;
//        return "[{\"word\":\"flow\",\"freq\":24,\"score\":300,\"flags\":\"bc\",\"syllables\":\"1\"}]";
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
