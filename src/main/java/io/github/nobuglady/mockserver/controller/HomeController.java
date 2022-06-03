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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.nobuglady.mockserver.service.HomeBusiness;
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
 * Home controller
 * 
 * @author NoBugLady
 *
 */
@Controller
public class HomeController {

	@Autowired
	private HomeBusiness homeBusiness;
	
	/**
	 * select all category
	 * 
	 * @param session session
	 * @return categories
	 */
	@RequestMapping(value = "/home/request_catagory_select", method = RequestMethod.GET)
	@ResponseBody
	public CatagoryRes requestCatagorySelect(HttpSession session) {

		CatagorySelectRequestDto requestDto = new CatagorySelectRequestDto();
		CatagorySelectResponseDto responseDto = new CatagorySelectResponseDto();

		homeBusiness.requestCatagorySelect(requestDto, responseDto, session.getId());
		return responseDto.catagoryRes;

	}

	/**
	 * delete category
	 * 
	 * @param requestDto request dto
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_catagory_delete", method = RequestMethod.POST)
	@ResponseBody
	public CatagoryDeleteResponseDto requestCatagoryDelete(@RequestBody CatagoryDeleteRequestDto requestDto) {

//		CatagoryDeleteRequestDto requestDto = new CatagoryDeleteRequestDto();
		CatagoryDeleteResponseDto responseDto = new CatagoryDeleteResponseDto();

		homeBusiness.requestCatagoryDelete(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * rename category
	 * 
	 * @param requestDto request dto
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_catagory_rename", method = RequestMethod.POST)
	@ResponseBody
	public CatagoryRenameResponseDto requestCatagoryRename(@RequestBody CatagoryRenameRequestDto requestDto) {

//		CatagoryRenameRequestDto requestDto = new CatagoryRenameRequestDto();
		CatagoryRenameResponseDto responseDto = new CatagoryRenameResponseDto();

		homeBusiness.requestCatagoryRename(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * create category
	 * 
	 * @param requestDto request dto
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_catagory_create", method = RequestMethod.POST)
	@ResponseBody
	public CatagoryCreateResponseDto requestCatagoryCreate(@RequestBody CatagoryCreateRequestDto requestDto) {

//		CatagoryCreateRequestDto requestDto = new CatagoryCreateRequestDto();
		CatagoryCreateResponseDto responseDto = new CatagoryCreateResponseDto();

		homeBusiness.requestCatagoryCreate(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * move category
	 * 
	 * @param requestDto request dto
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_catagory_move", method = RequestMethod.POST)
	@ResponseBody
	public CatagoryMoveResponseDto requestMoveCreate(@RequestBody CatagoryMoveRequestDto requestDto) {

//		CatagoryMoveRequestDto requestDto = new CatagoryMoveRequestDto();
		CatagoryMoveResponseDto responseDto = new CatagoryMoveResponseDto();

		homeBusiness.requestCatagoryMove(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * save api on-off
	 * 
	 * @param apiId                api id
	 * @param reqMethod            request method
	 * @param reqPath              request path
	 * @param apiStatus            api status
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_api_save_on_off", method = RequestMethod.POST)
	@ResponseBody
	public ApiSaveOnOffResponseDto requestApiSaveOnOff(@RequestParam(value = "apiId") String apiId,
			@RequestParam(value = "reqMethod") String reqMethod, 
			@RequestParam(value = "reqPath") String reqPath,
			@RequestParam(value = "apiStatus") String apiStatus,
			HttpSession session) {

		ApiSaveOnOffRequestDto requestDto = new ApiSaveOnOffRequestDto();
		ApiSaveOnOffResponseDto responseDto = new ApiSaveOnOffResponseDto();
		requestDto.apiId = apiId;
		requestDto.reqMethod = reqMethod;
		requestDto.reqPath = reqPath;
		requestDto.apiStatus = apiStatus;

		homeBusiness.requestApiSaveOnOff(requestDto, responseDto, session.getId());
		return responseDto;

	}

	/**
	 * save api
	 * 
	 * @param apiId                api id
	 * @param reqMethod            request method
	 * @param reqPath              request path
	 * @param apiStatus            api status
	 * @param resScript            response script
	 * @param resStatus            response status
	 * @param resContentType       response content type
	 * @param resCharacterEncoding response character encoding
	 * @param resBody              response body
	 * @param session              session
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_api_save", method = RequestMethod.POST)
	@ResponseBody
	public ApiSaveResponseDto requestApiSave(@RequestParam(value = "apiId") String apiId,
			@RequestParam(value = "reqMethod") String reqMethod, @RequestParam(value = "reqPath") String reqPath,
			@RequestParam(value = "apiStatus") String apiStatus, @RequestParam(value = "resScript") String resScript,
			@RequestParam(value = "resStatus") String resStatus,
			@RequestParam(value = "resContentType") String resContentType,
			@RequestParam(value = "resCharacterEncoding") String resCharacterEncoding,
			@RequestParam(value = "resBody") String resBody,
			HttpSession session) {

		ApiSaveRequestDto requestDto = new ApiSaveRequestDto();
		ApiSaveResponseDto responseDto = new ApiSaveResponseDto();
		requestDto.apiId = apiId;
		requestDto.reqMethod = reqMethod;
		requestDto.reqPath = reqPath;
		requestDto.apiStatus = apiStatus;
		requestDto.resScript = resScript;
		requestDto.resStatus = resStatus;
		requestDto.resContentType = resContentType;
		requestDto.resCharacterEncoding = resCharacterEncoding;
		requestDto.resBody = resBody;

		homeBusiness.requestApiSave(requestDto, responseDto, session.getId());
		return responseDto;

	}


	/**
	 * refresh log
	 * 
	 * @param reqMethod            request method
	 * @param reqPath              request path
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_log_refresh", method = RequestMethod.POST)
	@ResponseBody
	public LogRefreshResponseDto requestRefreshLog(
			@RequestParam(value = "apiId") String apiId) {

		LogRefreshRequestDto requestDto = new LogRefreshRequestDto();
		LogRefreshResponseDto responseDto = new LogRefreshResponseDto();
		requestDto.apiId = apiId;

		homeBusiness.refreshLog(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * clear log
	 * 
	 * @param reqMethod            request method
	 * @param reqPath              request path
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_log_clear", method = RequestMethod.POST)
	@ResponseBody
	public LogClearResponseDto requestClearLog(
			@RequestParam(value = "apiId") String apiId) {

		LogClearRequestDto requestDto = new LogClearRequestDto();
		LogClearResponseDto responseDto = new LogClearResponseDto();
		requestDto.apiId = apiId;

		homeBusiness.clearLog(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * get api info
	 * 
	 * @param apiId api id
	 * @return response dto
	 */
	@RequestMapping(value = "/home/request_api_info", method = RequestMethod.POST)
	@ResponseBody
	public ApiInfoResponseDto requestApiInfo(@RequestParam(value = "apiId") String apiId) {

		ApiInfoRequestDto requestDto = new ApiInfoRequestDto();
		ApiInfoResponseDto responseDto = new ApiInfoResponseDto();
		requestDto.apiId = apiId;

		homeBusiness.requestApiInfo(requestDto, responseDto);
		return responseDto;

	}

}
