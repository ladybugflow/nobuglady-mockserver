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
package io.github.nobuglady.mockserver.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nobuglady.mockserver.logger.ConsoleLogger;
import io.github.nobuglady.mockserver.persistance.db.dao.ApiDao;
import io.github.nobuglady.mockserver.persistance.db.dao.CategoryDao;
import io.github.nobuglady.mockserver.persistance.db.entity.ApiEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.CategoryEntity;
import io.github.nobuglady.mockserver.persistance.db.entity.UserEntity;
import io.github.nobuglady.mockserver.security.AuthHolder;
import io.github.nobuglady.mockserver.service.dto.home.ApiInfoRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.ApiInfoResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.ApiSaveOnOffRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.ApiSaveOnOffResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.ApiSaveRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.ApiSaveResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryCreateRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryCreateResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryDeleteRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryDeleteResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryMoveRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryMoveResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryRenameRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryRenameResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagoryRes;
import io.github.nobuglady.mockserver.service.dto.home.CatagorySelectRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.CatagorySelectResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.LogClearRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.LogClearResponseDto;
import io.github.nobuglady.mockserver.service.dto.home.LogRefreshRequestDto;
import io.github.nobuglady.mockserver.service.dto.home.LogRefreshResponseDto;

/**
 * Home business class
 * 
 * @author NoBugLady
 *
 */
@Service
@Transactional
public class HomeBusiness {

	@Autowired
	private CategoryDao catagoryDao;
	
	@Autowired
	private ApiDao apiDao;
	
	/**
	 * select all categories
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 * @param sessionId session id
	 */
	public void requestCatagorySelect(CatagorySelectRequestDto requestDto,
			CatagorySelectResponseDto responseDto, String sessionId) {

		UserEntity loginUser = AuthHolder.getUser(sessionId);
		
		List<CategoryEntity> catagoryEntityList = catagoryDao.selectAll(loginUser.user_id, loginUser.admin_flag);
		List<CatagoryRes> resList = new ArrayList<CatagoryRes>();

		Map<String, List<CatagoryRes>> childrenMap = new HashMap<String, List<CatagoryRes>>();
		for (CategoryEntity entity : catagoryEntityList) {
			CatagoryRes res = new CatagoryRes();
			res.id = entity.id;
			res.text = entity.text;
			res.type = entity.type;

			resList.add(res);

			List<CatagoryRes> childList = childrenMap.get(entity.parent);
			if (childList == null) {
				childList = new ArrayList<CatagoryRes>();
				childrenMap.put(entity.parent, childList);
			}
			childList.add(res);

			if (entity.parent.equals("#")) {
				responseDto.catagoryRes = res;
			}
		}

		for (CatagoryRes res : resList) {
			res.children = childrenMap.get(res.id);
		}

	}

	/**
	 * delete categories
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 */
	public void requestCatagoryDelete(CatagoryDeleteRequestDto requestDto,
			CatagoryDeleteResponseDto responseDto) {

		if(requestDto.deleteIdList != null && requestDto.deleteIdList.size() > 0) {
			catagoryDao.delete(requestDto.deleteIdList);
		}
		
	}

	/**
	 * rename categories
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 */
	public void requestCatagoryRename(CatagoryRenameRequestDto requestDto,
			CatagoryRenameResponseDto responseDto) {

		catagoryDao.rename(requestDto.id,requestDto.text);
	}

	/**
	 * create categories
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 */
	public void requestCatagoryCreate(CatagoryCreateRequestDto requestDto,
			CatagoryCreateResponseDto responseDto) {

		CategoryEntity entity = new CategoryEntity();
		entity.id = requestDto.id;
		entity.text = requestDto.text;
		entity.parent = requestDto.parent;
		entity.type = requestDto.type;
		entity.disabled = "false";
		
		catagoryDao.save(entity);
	}

	/**
	 * move categories
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 */
	public void requestCatagoryMove(CatagoryMoveRequestDto requestDto,
			CatagoryMoveResponseDto responseDto) {

		catagoryDao.move(requestDto.id,requestDto.parent);
	}

	/**
	 * save api on-off
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 * @param sessionId session id
	 */
	public void requestApiSaveOnOff(ApiSaveOnOffRequestDto requestDto, ApiSaveOnOffResponseDto responseDto, String sessionId) {
		
		UserEntity userEntity = AuthHolder.getUser(sessionId);
		
		if(userEntity.admin_flag == null || userEntity.admin_flag != 1) {
			List<String> userUpdateCategoryList = catagoryDao.selectUserCategoryUpdateList(userEntity.user_id);
			List<CategoryEntity> catagoryEntityList = catagoryDao.selectAll();
			Map<String,CategoryEntity> catagoryEntityMap = new HashMap<>();
			for(CategoryEntity entity:catagoryEntityList) {
				catagoryEntityMap.put(entity.id, entity);
			}
			
			CategoryEntity checkEntity = catagoryEntityMap.get(requestDto.apiId);
			while(!checkEntity.parent.equals("1")) {
				checkEntity = catagoryEntityMap.get(checkEntity.parent);
			}
			
			if(!userUpdateCategoryList.contains(checkEntity.id)) {
				responseDto.updateTime = "Access denied!";
				return;
			}
		}
		
		ApiEntity entity = apiDao.selectApi(requestDto.apiId);
		if (entity == null) {
			entity = new ApiEntity();
			entity.api_id = requestDto.apiId;
			entity.req_method = requestDto.reqMethod;
			entity.req_path = requestDto.reqPath;
			entity.api_status = requestDto.apiStatus;
			apiDao.save(entity);
		} else {
			entity.api_id = requestDto.apiId;
			entity.req_method = requestDto.reqMethod;
			entity.req_path = requestDto.reqPath;
			entity.api_status = requestDto.apiStatus;
			apiDao.update(entity);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		entity = apiDao.selectApi(requestDto.apiId);
		responseDto.updateTime = sdf.format(entity.update_time);

	}

	/**
	 * save api
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 * @param sessionId session id
	 */
	public void requestApiSave(ApiSaveRequestDto requestDto, ApiSaveResponseDto responseDto, String sessionId) {
		
		UserEntity userEntity = AuthHolder.getUser(sessionId);
		
		if(userEntity.admin_flag == null || userEntity.admin_flag != 1) {
			List<String> userUpdateCategoryList = catagoryDao.selectUserCategoryUpdateList(userEntity.user_id);
			List<CategoryEntity> catagoryEntityList = catagoryDao.selectAll();
			Map<String,CategoryEntity> catagoryEntityMap = new HashMap<>();
			for(CategoryEntity entity:catagoryEntityList) {
				catagoryEntityMap.put(entity.id, entity);
			}
			
			CategoryEntity checkEntity = catagoryEntityMap.get(requestDto.apiId);
			while(!checkEntity.parent.equals("1")) {
				checkEntity = catagoryEntityMap.get(checkEntity.parent);
			}
			
			if(!userUpdateCategoryList.contains(checkEntity.id)) {
				responseDto.updateTime = "Access denied!";
				return;
			}
			
		}

		
		ApiEntity entity = apiDao.selectApi(requestDto.apiId);
		if (entity == null) {
			entity = new ApiEntity();
			entity.api_id = requestDto.apiId;
			entity.req_method = requestDto.reqMethod;
			entity.req_path = requestDto.reqPath;
			entity.api_status = requestDto.apiStatus;
			entity.res_script = requestDto.resScript;
			entity.res_status = requestDto.resStatus;
			entity.res_content_type = requestDto.resContentType;
			entity.res_character_encoding = requestDto.resCharacterEncoding;
			entity.res_body = requestDto.resBody;
			apiDao.save(entity);
		} else {
			entity.api_id = requestDto.apiId;
			entity.req_method = requestDto.reqMethod;
			entity.req_path = requestDto.reqPath;
			entity.api_status = requestDto.apiStatus;
			entity.res_script = requestDto.resScript;
			entity.res_status = requestDto.resStatus;
			entity.res_content_type = requestDto.resContentType;
			entity.res_character_encoding = requestDto.resCharacterEncoding;
			entity.res_body = requestDto.resBody;
			apiDao.update(entity);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		entity = apiDao.selectApi(requestDto.apiId);
		responseDto.updateTime = sdf.format(entity.update_time);

	}

	/**
	 * request api informations
	 * 
	 * @param requestDto  request dto
	 * @param responseDto response dto
	 */
	public void requestApiInfo(ApiInfoRequestDto requestDto, ApiInfoResponseDto responseDto) {
		ApiEntity entity = apiDao.selectApi(requestDto.apiId);

		if (entity != null) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			responseDto.api_id = entity.api_id;
			responseDto.api_status = entity.api_status;
			responseDto.req_path = entity.req_path;
			responseDto.req_method = entity.req_method;
			responseDto.res_script = entity.res_script;
			responseDto.res_status = entity.res_status;
			responseDto.res_content_type = entity.res_content_type;
			responseDto.res_character_encoding = entity.res_character_encoding;
			responseDto.res_body = entity.res_body;
			responseDto.res_headers = entity.res_headers;
			responseDto.res_cookies = entity.res_cookies;
			responseDto.create_time = sdf.format(entity.create_time);
			responseDto.update_time = sdf.format(entity.update_time);
		}

	}

	/**
	 * refresh log
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void refreshLog(LogRefreshRequestDto requestDto, LogRefreshResponseDto responseDto) {
		
		List<String> messageList = ConsoleLogger.getInstance(requestDto.apiId).getMessages();
		StringBuffer logMsg = new StringBuffer();
		
		for(String message:messageList) {
			logMsg.append(message);
			logMsg.append("\n");
		}
		
		responseDto.logs = logMsg.toString();
		
	}


	/**
	 * clear log
	 * 
	 * @param requestDto request dto
	 * @param responseDto response dto
	 */
	public void clearLog(LogClearRequestDto requestDto, LogClearResponseDto responseDto) {
		
		ConsoleLogger.getInstance(requestDto.apiId).clear();
		
		responseDto.logs = "";
		
	}

}
