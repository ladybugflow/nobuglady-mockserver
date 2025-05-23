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
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.nobuglady.mockserver.controller.dto.WebLoginRequestDto;
import io.github.nobuglady.mockserver.controller.dto.WebLoginResponseDto;
import io.github.nobuglady.mockserver.security.AuthHolder;
import io.github.nobuglady.mockserver.service.LoginBusiness;

/**
 * Login controller
 * 
 * @author NoBugLady
 *
 */
@Controller
public class LoginController {

	@Autowired
	private LoginBusiness loginBusiness;
	
	/**
	 * login
	 * 
	 * @param requestDto request dto
	 * @param session session
	 * @return login response dto
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public WebLoginResponseDto login(@RequestBody WebLoginRequestDto requestDto, HttpSession session) {
		
		WebLoginResponseDto responseDto = new WebLoginResponseDto();
		
		responseDto.result = loginBusiness.login(session.getId(), requestDto.username, requestDto.password);
		
		return responseDto;
	}
	
	/**
	 * get menu
	 * 
	 * @param session session
	 * @return menu
	 */
	@RequestMapping(value="/menu",method=RequestMethod.POST)
	@ResponseBody
	public String getMenu(HttpSession session) {
		
		return AuthHolder.getUserMenu(session.getId());
		
	}
	
}
