
////////////////////////////////////////
// Initialize
////////////////////////////////////////
$(document).ready(function() {
	// register event
	$("#permission-tab").on("click",loadPermissionList());
	$("#role-tab").on("click",loadRoleList());
	$("#user-tab").on("click",loadUserList());
	
	loadmenu();
});


////////////////////////////////////////
// Click event
////////////////////////////////////////
/**
 * load menu
 */
function loadmenu(){
	request_load_menu();
}

/**
 * delete click
 */
function delete_show(type,name,id){
	$('#deleteModal').modal('show');
	$('#delete_type').val(type);
	$('#delete_name').text(name);
	$('#delete_id').val(id);
}

/**
 * delete click
 */
function delete_click(type,name,id){
	var type = $('#delete_type').val();
	var id = $('#delete_id').val();
	
	if("permission" == type){
		request_api_permission_delete(id);
	}else if("role" == type){
		request_api_role_delete(id);
	}else if("user" == type){
		request_api_user_delete(id);
	}
}

/**
 * click user create
 */
function user_create_click(){
	$("#user_id").val("");
	$('#userCreateModal').modal('show');
	request_api_user_load_role();
}

/**
 * click role create
 */
function role_create_click(){
	$("#role_id").val("");
	$('#roleCreateModal').modal('show');
	request_api_role_load_permission();
}


/**
 * click permission create
 */
function permission_create_click(){
	$("#permission_id").val("");
	$('#permissionCreateModal').modal('show');
	request_api_permission_load_category();
}

/////////////////////////////////
/**
 * permission tab click
 */
function loadPermissionList(){
	request_api_permission_load_list();
}

/**
 * click permission edit
 */
function permission_edit_click(permissionId){
	$('#permissionCreateModal').modal('show');
	request_api_permission_load_category(permissionId,
		
		function(permissionId){
			
			var permissionJson = JSON.parse($("#permission_row_"+permissionId).val());
			
			var permissionDetailListJson = [];
			if($("#permission_detail_row_"+permissionId).val() != ","){
				permissionDetailListJson = JSON.parse($("#permission_detail_row_"+permissionId).val());
			}
			
			$("#permission_id").val(permissionJson.permission_id);
			$("#permission_name").val(permissionJson.permission_name);
			$("#permission_remarks").val(permissionJson.remarks);
			
			for(detail of permissionDetailListJson){
				
				if(detail.can_read == 1){
					$("#permission_read_"+detail.category_id).prop('checked', true);
				}
				
				if(detail.can_update == 1){
					$("#permission_update_"+detail.category_id).prop('checked', true);
				}
				
			}
		}
	);
	

}


/**
 * click permission save
 */
function save_permission_click(){
	
	var validateResult = true;
	if($("#permission_create_form")[0].checkValidity() == false){
		console.log("validate false.");
		validateResult = false;
	}
	
	$("#permission_create_form")[0].classList.add('was-validated');
	
	if(validateResult){
		var permissionId = $('#permission_id').val();
		var permissionName = $('#permission_name').val();
		var permissionRemarks = $('#permission_remarks').val();
		var permissionCategoryList = [];
		$('td[name="permission_id"]').each(function(index, element){
			var catelogId = $(element).text();
			console.log(catelogId);
			var canRead = 0;
			var canUpdate = 0;
			if($("#permission_read_"+catelogId).prop('checked') == true){
				canRead = 1;
			}
			if($("#permission_update_"+catelogId).prop('checked') == true){
				canUpdate = 1;
			}
			
			permissionCategoryList.push(
				{
					catelogId:catelogId,
					canRead:canRead,
					canUpdate:canUpdate
				}
			);
			
			
			
		});
		
		request_permission_save(permissionId,permissionName, permissionRemarks, permissionCategoryList);
	}
	
}

/////////////////////////////////
/**
 * role tab click
 */
function loadRoleList(){
	request_api_role_load_list();
}

/**
 * click role edit
 */
function role_edit_click(roleId){
	$('#roleCreateModal').modal('show');
	request_api_role_load_permission(roleId,
		
		function(roleId){
			
			var roleJson = JSON.parse($("#role_row_"+roleId).val());
			
			var roleDetailListJson = [];
			if($("#role_detail_row_"+roleId).val() != ","){
				roleDetailListJson = JSON.parse($("#role_detail_row_"+roleId).val());
			}
			
			$("#role_id").val(roleJson.role_id);
			$("#role_name").val(roleJson.role_name);
			$("#role_remarks").val(roleJson.remarks);
			
			for(detail of roleDetailListJson){
				
				$("#role_permission_check_"+detail.permission_id).prop('checked', true);
				
			}
		}
	);
	

}


/**
 * click role save
 */
function save_role_click(){
	
	var validateResult = true;
	if($("#role_create_form")[0].checkValidity() == false){
		console.log("validate false.");
		validateResult = false;
	}
	
	$("#role_create_form")[0].classList.add('was-validated');
	
	if(validateResult){
		var roleId = $('#role_id').val();
		var roleName = $('#role_name').val();
		var roleRemarks = $('#role_remarks').val();
		var rolePermissionList = [];
		$('input[name="role_permission_check"]:checked').each(function(index, element){
			var permissionId = $(element).val();
			console.log(permission);
			
			rolePermissionList.push(
				{
					permissionId:permissionId
				}
			);
			
			
			
		});
		
		request_role_save(roleId,roleName, roleRemarks, rolePermissionList);
	}
	
}

/////////////////////////////////
/**
 * user tab click
 */
function loadUserList(){
	request_api_user_load_list();
}

/**
 * click user edit
 */
function user_edit_click(userId){
	$('#userCreateModal').modal('show');
	request_api_user_load_role(userId,
		
		function(userId){
			
			console.log("aaa");
			
			var userJson = JSON.parse($("#user_row_"+userId).val());
			
			var userDetailListJson = [];
			if($("#user_detail_row_"+userId).val() != ","){
				userDetailListJson = JSON.parse($("#user_detail_row_"+userId).val());
			}
			
			$("#user_id").val(userJson.user_id);
			$("#user_name").val(userJson.user_name);
			$("#user_email").val(userJson.email);
			$("#user_password").val(userJson.passwd);
			$("#user_repassword").val(userJson.passwd);
			$("#user_remarks").val(userJson.remarks);
			
			for(detail of userDetailListJson){
				
				$("#user_role_check_"+detail.role_id).prop('checked', true);
				
			}
		}
	);
	

}

/**
 * click user save
 */
function save_user_click(){
	var validateResult = true;
	if($("#user_create_form")[0].checkValidity() == false){
		console.log("validate false.");
		validateResult = false;
	}
	
	var userPasswordChk = $('#user_password').val();
	var userRePasswordChk = $('#user_repassword').val();
	if(userPasswordChk != userRePasswordChk){
		validateResult = false;
	}
	
	$("#user_create_form")[0].classList.add('was-validated');
	
	if(validateResult){
		var userId = $('#user_id').val();
		var userName = $('#user_name').val();
		var userEmail = $('#user_email').val();
		var userPassword = $('#user_password').val();
		var userRemarks = $('#user_remarks').val();
		var userRoleList = [];
		$('input[name="user_role_check"]:checked').each(function(index, element){
			var roleId = $(element).val();
			console.log(role);
			
			userRoleList.push(
				{
					roleId:roleId
				}
			);
			
			
			
		});
		
		request_user_save(userId, userName, userEmail, userPassword, userRemarks, userRoleList);
	}
	
}

////////////////////////////////////////
// Send request
////////////////////////////////////////

function request_api_permission_delete(id){
	post(
		'/admin/request_api_permission_delete',
		{
			'id':id
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			$('#deleteModal').modal('hide');
			request_api_permission_load_list();
		}
	);
}

function request_api_permission_load_list(){
	
	postJson(
		'/admin/request_permission_load_list',
		{
			
		},
		function (data) {
	        var responseObj = data;
			$("#permissionListBody").html("");
			for(permission of responseObj.permissionList){
				
				var newTrHtml = `
						    <tr>
						      <th scope="row">#id#</th>
						      <td>#name#</td>
						      <td>#detail#</td>
						      <td>#remark#</td>
						      <td>
								<a href="javascript:permission_edit_click(#id#)"><i class="fas fa-edit fa-lg"></i></a> | 
								<a href="javascript:delete_show('permission','#name#','#id#')"><i class="fas fa-trash-alt fa-lg"></i></a>
								<input type="hidden" id="permission_row_#id#" name="permission_row_#id#" value='#permission#'/>
								<input type="hidden" id="permission_detail_row_#id#" name="permission_detail_row_#id#" value='#detailList#'/>
						      </td>
						    </tr>
				`;
				
				var detail = responseObj.permissionDetailMap[permission.permission_id];
				var detailList = responseObj.permissionDetailListMap[permission.permission_id];
				
				newTrHtml = newTrHtml.split("#id#").join(permission.permission_id);
				newTrHtml = newTrHtml.split("#name#").join(permission.permission_name);
				newTrHtml = newTrHtml.split("#detail#").join(detail);
				newTrHtml = newTrHtml.split("#remark#").join(permission.remarks);
				
				newTrHtml = newTrHtml.split("#permission#").join(JSON.stringify(permission));
				newTrHtml = newTrHtml.split("#detailList#").join(JSON.stringify(detailList));
				
				var existHtml = $("#permissionListBody").html();
				
				$("#permissionListBody").html(existHtml+newTrHtml);
			}
			
		}
	);
	
}

function request_api_permission_load_category(permissionId, callback){
	
	postJson(
		'/admin/request_permission_load_category',
		{
			
		},
		function (data) {
	        var responseObj = data;
			$("#permission_create_list_id").html("");
			for(category of responseObj){
				
				var newTrHtml = `
				  	<tr>
				  		<td name="permission_id">#id#</td>
				  		<td>#text#</td>
				  		<td>
				  			<input type="checkbox" id="permission_read_#id#">
				  		</td>
				  		<td>
				  			<input type="checkbox" id="permission_update_#id#">
				  		</td>
				  	</tr>
				`;
				
				newTrHtml = newTrHtml.split("#id#").join(category.id);
				newTrHtml = newTrHtml.split("#text#").join(category.text);
				
				var existHtml = $("#permission_create_list_id").html();
				
				$("#permission_create_list_id").html(existHtml+newTrHtml);
			}
			
			if(callback){
				callback(permissionId);
			}
			
		}
	);
	
}

function request_permission_save(permissionId, permissionName, permissionRemarks, permissionCategoryList){
	
	postJson(
		'/admin/request_permission_save',
		{
			permissionId:permissionId,
			permissionName:permissionName,
			permissionRemarks:permissionRemarks,
			permissionCategoryList:permissionCategoryList
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			request_api_permission_load_list();
			$('#permissionCreateModal').modal('hide');
		}
	);
	
}

/////////////////////////////////

function request_api_role_delete(id){
	post(
		'/admin/request_api_role_delete',
		{
			'id':id
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			$('#deleteModal').modal('hide');
			request_api_role_load_list();
		}
	);
}

function request_api_role_load_list(){
	
	postJson(
		'/admin/request_role_load_list',
		{
			
		},
		function (data) {
	        var responseObj = data;
			$("#roleListBody").html("");
			for(role of responseObj.roleList){
				
				var newTrHtml = `
						    <tr>
						      <th scope="row">#id#</th>
						      <td>#name#</td>
						      <td>#detail#</td>
						      <td>#remark#</td>
						      <td>
								<a href="javascript:role_edit_click(#id#)"><i class="fas fa-edit fa-lg"></i></a> | 
								<a href="javascript:delete_show('role','#name#','#id#')"><i class="fas fa-trash-alt fa-lg"></i></a>
								<input type="hidden" id="role_row_#id#" name="role_row_#id#" value='#role#'/>
								<input type="hidden" id="role_detail_row_#id#" name="role_detail_row_#id#" value='#detailList#'/>
						      </td>
						    </tr>
				`;
				
				var detail = responseObj.roleDetailMap[role.role_id];
				if(!detail){
					detail = "";
				}
				var detailList = responseObj.roleDetailListMap[role.role_id];
				
				newTrHtml = newTrHtml.split("#id#").join(role.role_id);
				newTrHtml = newTrHtml.split("#name#").join(role.role_name);
				newTrHtml = newTrHtml.split("#detail#").join(detail);
				newTrHtml = newTrHtml.split("#remark#").join(role.remarks);
				
				newTrHtml = newTrHtml.split("#role#").join(JSON.stringify(role));
				newTrHtml = newTrHtml.split("#detailList#").join(JSON.stringify(detailList));
				
				var existHtml = $("#roleListBody").html();
				
				$("#roleListBody").html(existHtml+newTrHtml);
			}
			
		}
	);
	
}

function request_api_role_load_permission(roleId, callback){
	
	postJson(
		'/admin/request_role_load_permission',
		{
			
		},
		function (data) {
	        var responseObj = data;
			$("#role_create_list_id").html("");
			for(permission of responseObj){
				
				var newTrHtml = `
					<div class="form-check">
					  <input class="form-check-input" type="checkbox" value="#id#" name="role_permission_check" id="role_permission_check_#id#">
					  <label class="form-check-label" for="defaultCheck1">
					    #permission_name#
					  </label>
					</div>
				`;
				
				newTrHtml = newTrHtml.split("#id#").join(permission.permission_id);
				newTrHtml = newTrHtml.split("#permission_name#").join(permission.permission_name);
				
				var existHtml = $("#role_create_list_id").html();
				
				$("#role_create_list_id").html(existHtml+newTrHtml);
			}
			
			if(callback){
				callback(roleId);
			}
			
		}
	);
	
}

function request_role_save(roleId, roleName, roleRemarks, rolePermissionList){
	
	postJson(
		'/admin/request_role_save',
		{
			roleId:roleId,
			roleName:roleName,
			roleRemarks:roleRemarks,
			rolePermissionList:rolePermissionList
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			request_api_role_load_list();
			$('#roleCreateModal').modal('hide');
		}
	);
	
}

/////////////////////////////////

function request_api_user_delete(id){
	post(
		'/admin/request_api_user_delete',
		{
			'id':id
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			$('#deleteModal').modal('hide');
			request_api_user_load_list();
		}
	);
}

function request_api_user_load_list(){
	
	postJson(
		'/admin/request_user_load_list',
		{
			
		},
		function (data) {
	        var responseObj = data;
			$("#userListBody").html("");
			for(user of responseObj.userList){
				
				var newTrHtml = `
						    <tr>
						      <th scope="row">#id#</th>
						      <td>#email#</td>
						      <td>#name#</td>
						      <td>#detail#</td>
						      <td>#remark#</td>
						      <td>
								<a href="javascript:user_edit_click(#id#)"><i class="fas fa-edit fa-lg"></i></a> | 
								<a href="javascript:delete_show('user','#email#','#id#')"><i class="fas fa-trash-alt fa-lg"></i></a>
								<input type="hidden" id="user_row_#id#" name="user_row_#id#" value='#user#'/>
								<input type="hidden" id="user_detail_row_#id#" name="user_detail_row_#id#" value='#detailList#'/>
						      </td>
						    </tr>
				`;
				
				var detail = responseObj.userDetailMap[user.user_id];
				if(!detail){
					detail = "";
				}
				var detailList = responseObj.userDetailListMap[user.user_id];
				
				newTrHtml = newTrHtml.split("#id#").join(user.user_id);
				newTrHtml = newTrHtml.split("#name#").join(user.user_name);
				newTrHtml = newTrHtml.split("#email#").join(user.email);
				newTrHtml = newTrHtml.split("#detail#").join(detail);
				newTrHtml = newTrHtml.split("#remark#").join(user.remarks);
				
				newTrHtml = newTrHtml.split("#user#").join(JSON.stringify(user));
				newTrHtml = newTrHtml.split("#detailList#").join(JSON.stringify(detailList));
				
				var existHtml = $("#userListBody").html();
				
				$("#userListBody").html(existHtml+newTrHtml);
			}
			
		}
	);
	
}

function request_api_user_load_role(userId, callback){
	
	postJson(
		'/admin/request_user_load_role',
		{
			
		},
		function (data) {
	        var responseObj = data;
			$("#user_create_list_id").html("");
			for(role of responseObj){
				
				var newTrHtml = `
					<div class="form-check">
					  <input class="form-check-input" type="checkbox" value="#id#" name="user_role_check" id="user_role_check_#id#">
					  <label class="form-check-label" for="defaultCheck1">
					    #role_name#
					  </label>
					</div>
				`;
				
				newTrHtml = newTrHtml.split("#id#").join(role.role_id);
				newTrHtml = newTrHtml.split("#role_name#").join(role.role_name);
				
				var existHtml = $("#user_create_list_id").html();
				
				$("#user_create_list_id").html(existHtml+newTrHtml);
			}
			
			if(callback){
				callback(userId);
			}
			
		}
	);
	
}

function request_user_save(userId, userName, userEmail, userPassword, userRemarks, userRoleList){
	
	postJson(
		'/admin/request_user_save',
		{
			userId:userId,
			userName:userName,
			email:userEmail,
			passwd:userPassword,
			userRemarks:userRemarks,
			userRoleList:userRoleList
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			request_api_user_load_list();
			$('#userCreateModal').modal('hide');
		}
	);
	
}

function request_load_menu(){
	post(
		'/menu',
		{},
		function (data) {
			var res = data;
			$("#menu").html(res);
		}
	);
}

function post(requestUrl, requestData, callBack){
    $.ajax({
        url:requestUrl,
        type:'POST',
        data:requestData
    }).done( (data, status, xhr) => {
        console.log(data);
		var ct = xhr.getResponseHeader("content-type") || "";
		if (ct.indexOf('html') > -1) {
		    window.location.replace("/login");
		}

        callBack(data);

    }).fail( (data) => {
        console.log(data);
    }).always( (data) => {

    });
}

function postJson(requestUrl, requestData, callBack){
    $.ajax({
        url:requestUrl,
        type:'POST',
		contentType: 'application/json',
		dataType: 'json',
        data:JSON.stringify(requestData)
    }).done( (data, status, xhr) => {
        console.log(data);
		var ct = xhr.getResponseHeader("content-type") || "";
		if (ct.indexOf('html') > -1) {
		    window.location.replace("/login");
		}

        callBack(data);
    }).fail( (data) => {
        console.log(data);
    }).always( (data) => {

    });
}

////////////////////////////////////////
// Helper function
////////////////////////////////////////
