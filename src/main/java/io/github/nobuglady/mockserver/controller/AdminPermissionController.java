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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.nobuglady.mockserver.persistance.db.entity.CategoryEntity;
import io.github.nobuglady.mockserver.service.AdminPermissionBusiness;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionDeleteRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionDeleteResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadCategoryRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadCategoryResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadListRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionLoadListResponseDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionSaveRequestDto;
import io.github.nobuglady.mockserver.service.dto.admin.PermissionSaveResponseDto;

/**
 * Admin controller
 * 
 * @author NoBugLady
 *
 */
@Controller
public class AdminPermissionController {

	@Autowired
	private AdminPermissionBusiness adminPermissionBusiness;

	/**
	 * delete permission
	 * 
	 * @param id id
	 * @return response dto
	 */
	@RequestMapping(value = "/admin/request_api_permission_delete", method = RequestMethod.POST)
	@ResponseBody
	public PermissionDeleteResponseDto requestApiPermissionDelete(@RequestParam(value = "id") String id) {

		PermissionDeleteRequestDto requestDto = new PermissionDeleteRequestDto();
		PermissionDeleteResponseDto responseDto = new PermissionDeleteResponseDto();

		requestDto.id = id;

		adminPermissionBusiness.requestPermissionDelete(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * select all permission
	 * 
	 * @return permissions
	 */
	@RequestMapping(value = "/admin/request_permission_load_list", method = RequestMethod.POST)
	@ResponseBody
	public PermissionLoadListResponseDto requestPermissionLoadList() {

		PermissionLoadListRequestDto requestDto = new PermissionLoadListRequestDto();
		PermissionLoadListResponseDto responseDto = new PermissionLoadListResponseDto();

		adminPermissionBusiness.requestPermissionList(requestDto, responseDto);
		return responseDto;

	}

	/**
	 * select all category
	 * 
	 * @return categories
	 */
	@RequestMapping(value = "/admin/request_permission_load_category", method = RequestMethod.POST)
	@ResponseBody
	public List<CategoryEntity> requestPermissionLoadCategory() {

		PermissionLoadCategoryRequestDto requestDto = new PermissionLoadCategoryRequestDto();
		PermissionLoadCategoryResponseDto responseDto = new PermissionLoadCategoryResponseDto();

		adminPermissionBusiness.requestCategoryList(requestDto, responseDto);
		return responseDto.categoryList;

	}

	/**
	 * save permission
	 * 
	 * @param requestDto request dto
	 * @return response dto
	 */
	@RequestMapping(value = "/admin/request_permission_save", method = RequestMethod.POST)
	@ResponseBody
	public PermissionSaveResponseDto requestPermissionSave(@RequestBody PermissionSaveRequestDto requestDto) {

//		PermissionSaveRequestDto requestDto = new PermissionSaveRequestDto();
		PermissionSaveResponseDto responseDto = new PermissionSaveResponseDto();

		adminPermissionBusiness.savePermission(requestDto, responseDto);
		return responseDto;

	}

}
