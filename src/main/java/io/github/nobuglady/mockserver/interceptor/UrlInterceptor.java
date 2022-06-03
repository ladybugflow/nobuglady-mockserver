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
package io.github.nobuglady.mockserver.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.util.WebUtils;

import io.github.nobuglady.mockserver.ext.cache.CacheManager;
import io.github.nobuglady.mockserver.ext.console.Console;
import io.github.nobuglady.mockserver.ext.db.DataBaseOperator;
import io.github.nobuglady.mockserver.logger.ConsoleLogger;
import io.github.nobuglady.mockserver.persistance.db.dao.ApiDao;
import io.github.nobuglady.mockserver.persistance.db.entity.ApiEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.UserEntity;
import io.github.nobuglady.mockserver.script.CookieDto;
import io.github.nobuglady.mockserver.script.RequestDto;
import io.github.nobuglady.mockserver.script.ResponseDto;
import io.github.nobuglady.mockserver.security.AuthHolder;
import io.github.nobuglady.mockserver.util.JsonUtil;

/**
 * Url interceptor
 * 
 * @author NoBugLady
 *
 */
@Component
public class UrlInterceptor implements AsyncHandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(UrlInterceptor.class);

	@Autowired
	private ApiDao apiDao;

	@Autowired
	private DataBaseOperator dataBaseOperator;

	/**
	 * pre handle
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, PUT, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
		
		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(200);
			return false;
		}
		
		String uri = getRequestUrl(request);
		String reqFullPath = uri.substring(request.getContextPath().length());

		if (reqFullPath.startsWith("/api/")) {

			logger.info("request:" + reqFullPath);

			String reqPath = reqFullPath.substring("/api/".length());
			String reqMethod = request.getMethod();
			Map<String, String> keyValueMap = new HashMap<>();

			// get api info from db
			ApiEntity apiEntity = getApiEntity(reqPath, reqMethod, keyValueMap);

			// not found
			if (apiEntity == null) {
				logger.error("no api found for:" + reqPath);
				response.sendError(404);
				return false;
			}

			// request info
			String requestJson = getRequestJson(request);

			// get api configure
			String apiId = apiEntity.api_id;
			String res_status = apiEntity.res_status;
			String res_content_type = apiEntity.res_content_type;
			String res_character_encoding = apiEntity.res_character_encoding;
//			String res_headers = apiEntity.res_headers;
//			String res_cookies = apiEntity.res_cookies;
			String res_body = apiEntity.res_body;

			// start log
			ConsoleLogger consoleLogger = ConsoleLogger.getInstance(apiId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			consoleLogger.info(sdf.format(new Date()) + " [REQUEST]" + reqPath);

			// set default value
			response.setStatus(200);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// set status
			if (res_status != null && !"".equals(res_status)) {
				if (!"200".equals(res_status)) {
					response.sendError(Integer.parseInt(res_status));
				} else {
					response.setStatus(Integer.valueOf(res_status));
				}
			}

			// set content type
			if (res_content_type != null && !"".equals(res_content_type)) {
				response.setContentType(res_content_type);
			}

			// set character encoding
			if (res_character_encoding != null && !"".equals(res_character_encoding)) {
				response.setCharacterEncoding(res_character_encoding);
			}

			// set body

			// script
			String script = apiEntity.res_script;

			// run script
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			ScriptContext ctx = new SimpleScriptContext();
			ctx.setAttribute("db", dataBaseOperator, ScriptContext.ENGINE_SCOPE);
			ctx.setAttribute("console", new Console(consoleLogger), ScriptContext.ENGINE_SCOPE);
			ctx.setAttribute("cache", CacheManager.getInstance(), ScriptContext.ENGINE_SCOPE);
			for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
				ctx.setAttribute(entry.getKey(), entry.getValue(), ScriptContext.ENGINE_SCOPE);
			}

//				// io.github.nobuglady.mockserver.ext.thread.ThreadOperator
			engine.eval("var thread  = Java.type(\"io.github.nobuglady.mockserver.ext.thread.ThreadOperator\")", ctx);

//				// io.github.nobuglady.mockserver.ext.file.FileOperator
			engine.eval("var file  = Java.type(\"io.github.nobuglady.mockserver.ext.file.FileOperator\")", ctx);

			// variable for script
			engine.eval("var request = " + requestJson, ctx);
			engine.eval("var status = 200", ctx);
			engine.eval("var response = {status:200,header:null,cookie:null,body:null}", ctx);

			try {
				engine.eval(script, ctx);
			} catch (ScriptException e) {
				consoleLogger.info(e.getMessage());
				response.getWriter().println(e.getMessage());
			}

			// result
			String responseJson = (String) engine.eval("JSON.stringify(response)", ctx);
			System.out.println(responseJson);
			int status = (Integer) engine.eval("response.status", ctx);
			String headerStr = (String) engine.eval("JSON.stringify(response.header)", ctx);
			String cookieStr = (String) engine.eval("JSON.stringify(response.cookie)", ctx);
			String bodyStr = (String) engine.eval("JSON.stringify(response.body)", ctx);

			ResponseDto responseDto = getResponseDto(status, headerStr, cookieStr, bodyStr);

			// status
			Integer statusVal = Integer.valueOf(responseDto.status);
			if (statusVal != 200) {
				response.sendError(statusVal);
			}

			// headers
			if (responseDto.header != null) {
				for (Map.Entry<String, String> entry : responseDto.header.entrySet()) {
					response.setHeader(entry.getKey(), entry.getValue());
				}
			}

			// cookies
			if (responseDto.cookie != null) {
				for (CookieDto cookieDto : responseDto.cookie) {

					Cookie cookie = new Cookie(cookieDto.name, cookieDto.value);

					if (cookieDto.comment != null) {
						cookie.setComment(cookieDto.comment);
					}

					if (cookieDto.domain != null) {
						cookie.setDomain(cookieDto.domain);
					}

					if (cookieDto.httpOnly != null) {
						cookie.setHttpOnly(cookieDto.httpOnly);
					}

//					if (cookieDto.maxAge != null) {
						cookie.setMaxAge(cookieDto.maxAge);
//					}

					if (cookieDto.path != null) {
						cookie.setPath(cookieDto.path);
					}

					if (cookieDto.secure != null) {
						cookie.setSecure(cookieDto.secure);
					}

					if (cookieDto.value != null) {
						cookie.setValue(cookieDto.value);
					}

//					if (cookieDto.version != null) {
						cookie.setVersion(cookieDto.version);
//					}

					response.addCookie(cookie);
				}
			}

			// body
			if (responseDto.body == null || "null".equals(responseDto.body) || "".equals(responseDto.body)) {
				responseDto.body = res_body;
			}
			response.getWriter().println(responseDto.body);

			// end log
			logger.info("response:" + responseJson);
			consoleLogger.info(sdf.format(new Date()) + " [RESPONSE]" + responseJson);

			return false;

		} else if (reqFullPath.startsWith("/home")) {
			UserEntity userEntity = AuthHolder.getUser(request.getSession().getId());
			if (userEntity == null) {
				response.sendRedirect("/login");
				return false;
			}
			return true;
		} else if (reqFullPath.startsWith("/admin")) {
			UserEntity userEntity = AuthHolder.getUser(request.getSession().getId());
			if (userEntity == null) {
				response.sendRedirect("/login");
				return false;
			}

			if (userEntity.admin_flag == null || userEntity.admin_flag != 1) {
				response.sendError(403);
				return false;
			}

			return true;
		} else {
			return true;
		}

	}

	/**
	 * get response dto
	 * 
	 * @param status    status
	 * @param headerStr headerStr
	 * @param cookieStr cookieStr
	 * @param bodyStr   bodyStr
	 * @return responseDto
	 */
	@SuppressWarnings("unchecked")
	private ResponseDto getResponseDto(int status, String headerStr, String cookieStr, String bodyStr) {

		ResponseDto responseDto = new ResponseDto();
		responseDto.status = status;
		responseDto.header = JsonUtil.fromJson(headerStr, Map.class);
		List<Map<String, Object>> cookieList = JsonUtil.fromJson(cookieStr, List.class);
		if (cookieList != null) {
			for (Map<String, Object> cookieMap : cookieList) {

				CookieDto cookieDto = new CookieDto();

				if (cookieMap.containsKey("comment")) {
					cookieDto.comment = (String) cookieMap.get("comment");
				}
				if (cookieMap.containsKey("domain")) {
					cookieDto.domain = (String) cookieMap.get("domain");
				}
				if (cookieMap.containsKey("httpOnly")) {
					cookieDto.httpOnly = (Boolean) cookieMap.get("httpOnly");
				}
				if (cookieMap.containsKey("maxAge")) {
					cookieDto.maxAge = (Integer) cookieMap.get("maxAge");
				}
				if (cookieMap.containsKey("name")) {
					cookieDto.name = (String) cookieMap.get("name");
				}
				if (cookieMap.containsKey("path")) {
					cookieDto.path = (String) cookieMap.get("path");
				}
				if (cookieMap.containsKey("secure")) {
					cookieDto.secure = (Boolean) cookieMap.get("secure");
				}
				if (cookieMap.containsKey("value")) {
					cookieDto.value = (String) cookieMap.get("value");
				}
				if (cookieMap.containsKey("version")) {
					cookieDto.version = (Integer) cookieMap.get("version");
				}

				responseDto.cookie.add(cookieDto);
			}
		}
		responseDto.body = bodyStr;

		return responseDto;

	}

	/**
	 * get ApiEntity from request and method
	 * 
	 * @param reqPath     request path
	 * @param reqMethod   request method
	 * @param keyValueMap key value map
	 * @return ApiEntity
	 */
	private ApiEntity getApiEntity(String reqPath, String reqMethod, Map<String, String> keyValueMap) {
		List<ApiEntity> apiEntityList = apiDao.selectByUrlMethod(reqPath, reqMethod);

		ApiEntity apiEntity = null;

		if (apiEntityList != null) {
			if (apiEntityList.size() > 1) {
				logger.error("mutiple api found for:" + reqPath);
				return null;
			} else if (apiEntityList.size() == 1) {
				apiEntity = apiEntityList.get(0);
			}
		}

		if (apiEntity == null) {
			// marching dynamic url
			List<ApiEntity> dynamicApiList = apiDao.selectDynamicUrlByMethod(reqMethod);
			for (ApiEntity dynamicApiEntity : dynamicApiList) {
				if (reqPath.matches(dynamicApiEntity.req_path_query)) {
					apiEntity = dynamicApiEntity;
					String requestPathTemp = dynamicApiEntity.req_path;

					int from = 0;
					while (requestPathTemp.indexOf("{", from) != -1 && requestPathTemp.indexOf("}", from) != -1) {
						int left = requestPathTemp.indexOf("{");
						int right = requestPathTemp.indexOf("}");
						if (right > left) {

							String key = requestPathTemp.substring(left + 1, right);
							int end = reqPath.indexOf("/", left);

							String value;
							if (end == -1) {
								value = reqPath.substring(left);
							} else {
								value = reqPath.substring(left, reqPath.indexOf("/", left));
							}

							keyValueMap.put(key, value);

							requestPathTemp = requestPathTemp.substring(0, left) + value
									+ requestPathTemp.substring(right + 1);
						} else {
							from = left + 1;
						}
					}

					break;
				}
			}

		}

		return apiEntity;
	}

	/**
	 * get request json string
	 * 
	 * @param request request
	 * @return json string
	 */
	private String getRequestJson(HttpServletRequest request) {
		RequestDto requestDto = new RequestDto();

		requestDto.method = request.getMethod();
		requestDto.url = request.getRequestURI();

		requestDto.parameter = request.getParameterMap();
		if (request.getSession() != null) {
			requestDto.sessionId = request.getSession().getId();
		}

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			requestDto.header.put(headerName, headerValue);
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];

				CookieDto cookieDto = new CookieDto();
				cookieDto.name = cookie.getName();
				cookieDto.value = cookie.getValue();
				cookieDto.version = cookie.getVersion();
				cookieDto.comment = cookie.getComment();
				cookieDto.domain = cookie.getDomain();
				cookieDto.maxAge = cookie.getMaxAge();
				cookieDto.path = cookie.getPath();
				cookieDto.secure = cookie.getSecure();
				cookieDto.httpOnly = cookie.isHttpOnly();
				requestDto.cookie.add(cookieDto);
			}

		}
		
		try {
			StringBuffer bodyBuffer = new StringBuffer();
			
			BufferedReader br = request.getReader();
			String line = null;
			while((line = br.readLine()) != null) {
				bodyBuffer.append(line);
				bodyBuffer.append("\n");
			}
			
			if(bodyBuffer.length() > 0) {
				requestDto.body = bodyBuffer.toString();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}

		return JsonUtil.toJson(requestDto);
	}

	/**
	 * get request url
	 * 
	 * @param request request
	 * @return request url
	 */
	private String getRequestUrl(HttpServletRequest request) {
		String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return uri;
	}
}
