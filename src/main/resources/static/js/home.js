
var response_editor = null;
var help_editor = null;
var log_editor = null;
var terminal_editor = null;
var markId = null;
var currentPath = "";

////////////////////////////////////////
// Initialize
////////////////////////////////////////
$(document).ready(function() {
	// register event
//	$("#btn_start_flow").on("click", startFlowClick);
//	$("#btn_refresh_flow").on("click", refreshFlowClick);
	$("#btn_save").on("click", apiSaveClick);
	$("#btn_refresh").on("click", logRefreshClick);
	$("#btn_clear_log").on("click", logClearClick);
	$("#btn_clear_terminal").on("click", terminalClearClick);
	$("#customSwitch1").on("click", onOffClick);
	
	loadtree();
	
	loadmenu();
	
	$('#jstree_demo').on('changed.jstree', function (e, data) {
	    var path = data.instance.get_path(data.node,'/');
		
		currentPath = path;
		
		if(path){
			
			if(data.node.type === 'file'){
				dispalyApiClick(data.node.id,data.node.text);
				terminal_editor.setValue(currentPath + ">",1);
			}else{
								
				$("#apiPanel").hide();
				$("#apiSettingPanel").hide();
				
				$("#folderMessagePanel").show();
				$("#apiId").text("");
				$("#apiName").text("");
			}
		}
		
	})
	
	log_editor = createEditor_log("log_div","text");
	help_editor = createEditor_help("help_div");
	terminal_editor = createEditor_terminal("terminal_div","text");
	response_editor = createEditor_response("response_div");

});

////////////////////////////////////////
// JsTree
////////////////////////////////////////
function demo_create() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	sel = ref.create_node(sel, {"type":"file"});
	if(sel) {
		ref.edit(sel);
	}
};
function demo_create_folder() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	sel = ref.create_node(sel, {"type":"default"});
	if(sel) {
		ref.edit(sel);
	}
};
function demo_rename() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	sel = sel[0];
	ref.edit(sel);
};
function demo_delete() {
	var ref = $('#jstree_demo').jstree(true),
		sel = ref.get_selected();
	if(!sel.length) { return false; }
	ref.delete_node(sel);
};

function loadtree(){
	$('#jstree_demo').jstree({
	  "core" : {
	    "animation" : 0,
	    "check_callback" : true,
	    "themes" : { "stripes" : true },
	    'data' : {
	      'url' : function (node) {
				if(node.id === '#'){
					return '/home/request_catagory_select';
				}else{
					return 'ajax_demo_children.json';
				}
	      },
	      'data' : function (node) {
	        return { 'id' : node.id };
	      }
	    }
	  },
	  "types" : {
	    "#" : {
	      "max_children" : 1,
	      "max_depth" : 4,
	      "valid_children" : ["root"]
	    },
	    "root" : {
	      "valid_children" : ["default"]
	    },
	    "default" : {
	      "valid_children" : ["default","file"]
	    },
	    "file" : {
	      "icon" : "fab fa-mixcloud",
	      "valid_children" : []
	    },
	  },
	  "plugins" : [
	    "contextmenu", "dnd", "search",
	    "state", "types", "wholerow"
	  ]
	});
	  var to = false;
	  $('#plugins4_q').keyup(function () {
	    if(to) { clearTimeout(to); }
	    to = setTimeout(function () {
	      var v = $('#plugins4_q').val();
	      $('#jstree_demo').jstree(true).search(v,false,true);
	    }, 250);
	  });
	  $('#plugins4_q_1').keyup(function () {
	    if(to) { clearTimeout(to); }
	    to = setTimeout(function () {
	      var v = $('#plugins4_q_1').val();
	      $('#jstree_demo').jstree(true).search(v,false,true);
	    }, 250);
	  });


}

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
 * click apiId
 */
function dispalyApiClick(apiId,apiName){
	$("#apiId").text(apiId);
	$("#apiName").text(apiName);
	$("#apiPanel").show();
	$("#apiSettingPanel").show();
	
	$("#folderMessagePanel").hide();
	
	request_api_info(apiId);
}


/**
 * on-off click
 */
function onOffClick(){
	
	var onOff = "off";
	
	if($("#customSwitch1").is(":checked")){
		onOff = "on";
	}
	
	request_api_save_on_off(
		$("#apiId").text(),
		$("#req_method").val(),
		$("#req_path").val(),
		onOff
	);
}

/**
 * api info save
 */
function apiSaveClick(){
	
	var onOff = "off";
	
	if($("#customSwitch1").is(":checked")){
		onOff = "on";
	}
	
	request_api_save(
		$("#apiId").text(),
		$("#req_method").val(),
		$("#req_path").val(),
		onOff,
		response_editor.getValue(),
		$("#res_status").val(),
		$("#res_content_type").val(),
		$("#res_character_encoding").val(),
		$("#res_body").val()
	);
}

/**
 * log refresh
 */
function logRefreshClick(){
	
	request_log_refresh(
		$("#apiId").text()
	);
}

/**
 * log clear
 */
function logClearClick(){
	
	request_log_clear(
		$("#apiId").text()
	);
}

/**
 * terminal clear
 */
function terminalClearClick(){
	
	terminal_editor.setValue(currentPath + ">",1);
}

////////////////////////////////////////
// Send request
////////////////////////////////////////
/*
function request_catagory_update(catagoryList){
	
	postJson(
		'/home/request_catagory_update',
		{
			catagoryReqList:catagoryList
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			
		}
	);
	
}
*/
function request_catagory_delete(deleteIdList){
	
	console.log("delete:"+deleteIdList);
	
	postJson(
		'/home/request_catagory_delete',
		{
			deleteIdList:deleteIdList
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			
		}
	);
	
	
}
function request_catagory_rename(obj){
	
	console.log("rename:"+JSON.stringify(obj));
	
	postJson(
		'/home/request_catagory_rename',
		{
			id:obj.id,
			text:obj.text
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			
		}
	);
	
}

var tempId = null;
function request_catagory_create(obj){
	
	console.log("create:"+JSON.stringify(obj));
	
	//obj.id = getUniqueStr();
	
	postJson(
		'/home/request_catagory_create',
		{
			id:obj.id,
			text:obj.text,
			type:obj.type,
			parent:obj.parent
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			
		}
	);
	
	return obj;
}
function request_catagory_move(obj){
	
	console.log("move:"+JSON.stringify(obj));
	
	postJson(
		'/home/request_catagory_move',
		{
			id:obj.id,
			parent:obj.parent
		},
		function (data) {
	        var responseObj = data;
			console.log(responseObj);
			
		}
	);
}
function request_catagory_copy(obj){
	
	console.log("copy:"+JSON.stringify(obj));

}

function request_api_save_on_off(apiId,reqMethod,reqPath,onOff){
	post(
		'/home/request_api_save_on_off',
		{
			'apiId':apiId,
			'reqMethod':reqMethod,
			'reqPath':reqPath,
			'apiStatus':onOff,
		},
		function (data) {
			var res = data;
			$("#update_time").text(res.updateTime);
			console.log(res);
			
		}
	);
}

function request_api_save(apiId,reqMethod,reqPath,onOff,resScript,resStatus,resContentType,resCharacterEncoding,resBody){
	post(
		'/home/request_api_save',
		{
			'apiId':apiId,
			'reqMethod':reqMethod,
			'reqPath':reqPath,
			'apiStatus':onOff,
			'resScript':resScript,
			'resStatus':resStatus,
			'resContentType':resContentType,
			'resCharacterEncoding':resCharacterEncoding,
			'resBody':resBody,
		},
		function (data) {
			var res = data;
			$("#update_time").text(res.updateTime);
			console.log(res);
			
			if(res.errorMsg){
				response_editor.getSession().setAnnotations([{
				  row: res.toRow,
				  column: 0,
				  text: res.errorMsg, // Or the Json reply from the parser 
				  type: "error" // also "warning" and "information"
				}]);
				
				/*
				var Range = ace.require("ace/range").Range;
				markId = response_editor.getSession().addMarker(new Range(res.fromRow, res.fromCol-1, res.toRow, res.toCol), "ace-related-code-highlight", "line");
				*/
			}else{
				response_editor.getSession().clearAnnotations();
				if(markId){
					response_editor.getSession().removeMarker(markId);
				}
			}
		}
	);
}


function request_log_refresh(apiId){
	post(
		'/home/request_log_refresh',
		{
			'apiId':apiId
		},
		function (data) {
			var res = data;
			log_editor.setValue(res.logs,1);
		}
	);
}

function request_log_clear(apiId){
	post(
		'/home/request_log_clear',
		{
			'apiId':apiId
		},
		function (data) {
			var res = data;
			log_editor.setValue(res.logs,1);
		}
	);
}

function request_api_info(apiId){
	post(
		'/home/request_api_info',
		{
			'apiId':apiId
		},
		function (data) {
			var res = data;
			$("#req_method").val(res.req_method);
			$("#req_path").val(res.req_path);
			$("#update_time").text(res.update_time);
			if(res.res_script){
				response_editor.setValue(res.res_script,1);
			}else{
				response_editor.setValue("",1);
			}
			$("#res_status").val(res.res_status);
			$("#res_content_type").val(res.res_content_type);
			$("#res_character_encoding").val(res.res_character_encoding);
			$("#res_body").val(res.res_body);
			if(res.api_status == "on"){
				$('#customSwitch1').prop('checked', true);
			}else{
				$('#customSwitch1').prop('checked', false);
			}
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
function getFormatTime(date) {
	var res = "" + date.getFullYear() + "/" + padZero(date.getMonth() + 1) + 
		"/" + padZero(date.getDate()) + " " + padZero(date.getHours()) + ":" + 
		padZero(date.getMinutes()) + ":" + padZero(date.getSeconds());
	return res;
}

function padZero(num) {
	var result;
	if (num < 10) {
		result = "0" + num;
	} else {
		result = "" + num;
	}
	return result;
}

function getUniqueStr(myStrong){
 var strong = 1000;
 if (myStrong) strong = myStrong;
 return new Date().getTime().toString(16)  + Math.floor(strong*Math.random()).toString(16)
}