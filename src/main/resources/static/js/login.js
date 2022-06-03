$(document).ready(function() {
    $("#show_hide_password a").on('click', function(event) {
        event.preventDefault();
        if($('#show_hide_password input').attr("type") == "text"){
            $('#show_hide_password input').attr('type', 'password');
            $('#show_hide_password i').addClass( "fa-eye-slash" );
            $('#show_hide_password i').removeClass( "fa-eye" );
        }else if($('#show_hide_password input').attr("type") == "password"){
            $('#show_hide_password input').attr('type', 'text');
            $('#show_hide_password i').removeClass( "fa-eye-slash" );
            $('#show_hide_password i').addClass( "fa-eye" );
        }
    });
});

function login(){
	$("#loginErrorDiv").hide();
	$("#systemErrorDiv").hide();
	
	var validateResult = true;
	if($("#login_form")[0].checkValidity() == false){
		validateResult = false;
	}
	
	$("#login_form")[0].classList.add('was-validated');
	
	if(validateResult){
		
		$("#loginDiv").show();
		
		var username = $("#mailaddress").val();
		var password = $("#password").val();
		var jsonData = {
			"username": username,
			"password": password
		};
		$.ajax({
		    type: 'POST',
		    url: '/login',
		    data: JSON.stringify(jsonData),
		    contentType: 'application/json',
		    dataType: 'json'
		}).done(function (data) {
		    var result = data.result;
		    if (result == 0) {
		        console.log("login success.");
		        window.location.href="/home"
		    } else {
		        console.log("login failed");
		        $("#loginErrorDiv").show();
		    }
		}).fail(function (data) {
		    console.log("error");
		    $("#systemErrorDiv").show();
		}).always(function (data) { 
			$("#loginDiv").hide();
		    console.log("request sended.");
		});
	}
}

