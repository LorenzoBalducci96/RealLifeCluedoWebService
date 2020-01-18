var jsonUserData;
var meKilledDeclaration;
var selectedSession = "";
var selectedEndedSession = "";

var killer_to_confirm = "";
var place_to_confirm = "";
var object_to_confirm = "";

var user_target = "";
var object_target = "";
var place_target = "";

window.onload = function() {
	$("#loginModal").modal();
};

function sendRegistrationRequest(){
	var profile_image = document.getElementById("new_user_profile_image").files[0];
	var formdata = new FormData();
	var password = document.getElementById("new_user_password").value
	var confirmedPassword =  document.getElementById("new_user_confirm_password").value
	if(password == confirmedPassword){
		formdata.append("profile_image", profile_image);
		formdata.append("username", document.getElementById("new_user_username").value);
		formdata.append("password", document.getElementById("new_user_password").value);
		formdata.append("email", document.getElementById("new_user_email").value);
		
		var ajax = new XMLHttpRequest();
		ajax.open("POST", "registrationRequest");
		
		ajax.onreadystatechange = function() {
			if (ajax.readyState === 4 && ajax.status === 200) {
				serverResponse = JSON.parse(ajax.responseText);
				if (serverResponse["status_code"] == 200){
					alert("inserted registration request...check your email for verify the account")
				}
				else{
					alert("error: already sent registration request?");
				}
			}
		}
		ajax.send(formdata);
	}else{
		alert("not same password on confirm password label")
	}
}

function login() {
	var xhr = new XMLHttpRequest();
	var url = "loginAction";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			jsonUserData = JSON.parse(xhr.responseText);
			if (jsonUserData["status_code"] == 200){
				$("#loginModal").modal('hide');
				var table = document.getElementById("allSessionsTable");
				var endedSessionTable = document.getElementById("endedSessionsTable");
				table.innerHTML = ""
				var i = 0
				while(jsonUserData["sessions"][i] != null){
					var sessionEnded = jsonUserData["sessions"][i]["session_ended"];
					if(sessionEnded == "no"){
						var date_start = new Date(jsonUserData["sessions"][i]["start"]);
						var date_end = new Date(jsonUserData["sessions"][i]["end"]);
						var date = new Date();
						var now = date.getTime();

						var session_duration = date_end.getTime() - date_start.getTime();
						var past = now - date_start.getTime();
						var percentual = Math.trunc((past / session_duration) * 100);

						var row = table.insertRow(0);
						row.setAttribute("session_name", jsonUserData["sessions"][i]["session_name"]);
						row.setAttribute("start", jsonUserData["sessions"][i]["start"]);
						row.setAttribute("end", jsonUserData["sessions"][i]["end"]);
						row.setAttribute("description", jsonUserData["sessions"][i]["description"]);
						var createClickHandler = function(row) {
							return function() {
								$("#session_modal").modal();
								selectedSession = row.getAttribute("session_name");
								document.getElementById("opened_session_name").innerHTML = row.getAttribute("session_name");
								document.getElementById("opened_session_start_date").innerHTML = row.getAttribute("start");
								document.getElementById("opened_session_end_date").innerHTML = row.getAttribute("end");
								document.getElementById("opened_session_description").innerHTML = row.getAttribute("description");
							};
						}
						row.onclick = createClickHandler(row);
						var session_name = row.insertCell(0);
						var passed = row.insertCell(1)
						passed.className = "floatRight";
						session_name.innerHTML = "<div style=\"height: 60px; line-height: 60px;\"> <label>" + 
							jsonUserData["sessions"][i]["session_name"] + "</label> </div>";
						passed.innerHTML = "<div style=\"display: inline; width: 60px; height: 60px;\">\
							<canvas width=\"60\" height=\"60\"\
							style=\"width: 30px; height: 30px;\"></canvas>\
							<input type=\"text\" class=\"knob\" data-readonly=\"true\"\
							value=\"" + percentual + "\" data-width=\"60\" data-height=\"60\"\
							data-fgcolor=\"#ffffffff\" data-bgcolor=\"#39CCCCaa\" readonly=\"readonly\">\</div>";
						$('.knob').knob();
					}else{
						var row = endedSessionTable.insertRow(0);
						row.setAttribute("session_name", jsonUserData["sessions"][i]["session_name"]);
						row.setAttribute("start", jsonUserData["sessions"][i]["start"]);
						row.setAttribute("end", jsonUserData["sessions"][i]["end"]);
						row.setAttribute("description", jsonUserData["sessions"][i]["description"]);
						var endedSession_name = row.insertCell(0);
						endedSession_name.innerHTML = "<div style=\"height: 30px; line-height: 30px;\"> <label>" + 
							jsonUserData["sessions"][i]["session_name"] + "</label> </div>";
						var createClickHandler = function(row) {
							return function() {
								$("#ended_session_modal").modal();
								selectedEndedSession = row.getAttribute("session_name");
								document.getElementById("ended_session_name").innerHTML = row.getAttribute("session_name");
								document.getElementById("ended_session_start_date").innerHTML = row.getAttribute("start");
								document.getElementById("ended_session_end_date").innerHTML = row.getAttribute("end");
								document.getElementById("ended_session_description").innerHTML = row.getAttribute("description");
								document.getElementById("endedSessionHighlightsTable").innerHTML = "";
								requesthighlightsOfClosedSession();
							};
						}
						row.onclick = createClickHandler(row);
					}
					i++;
				}
				selectedSession = jsonUserData["sessions"][0]["session_name"];
				loginToThisSession();
			}
			else{
				alert("username or password invalid")
			}
		}
	};
	var data = JSON
	.stringify({
		"username" : document
		.getElementById("login_username").value,
		"password" : document
		.getElementById("login_password").value
	});
	xhr.send(data);
}

function loginToThisSession(){
	var i = 0;
	while(jsonUserData["sessions"][i]["session_name"] != selectedSession){
		alert("not session " + jsonUserData["sessions"][i]["session_name"])
		i++;
	}
	//$('#sessions_card').CardWidget('remove')
	document.getElementById("user_target_image").src = jsonUserData["sessions"][i]["user_target_image"];
	document.getElementById("user_target_name").innerHTML = jsonUserData["sessions"][i]["actual_user_target"];
	document.getElementById("actual_place_target").innerHTML = jsonUserData["sessions"][i]["actual_place_target"];
	document.getElementById("actual_object_target").innerHTML = jsonUserData["sessions"][i]["actual_object_target"];
	
	
	
	document.getElementById("place_image").src = jsonUserData["sessions"][i]["place_image"];
	document.getElementById("object_image").src = jsonUserData["sessions"][i]["object_image"];
	document.getElementById("user_actual_points").innerHTML = jsonUserData["sessions"][i]["points"];
	document.getElementById("actual_target_points").innerHTML = 100 * jsonUserData["sessions"][i]["object_multiplicator"] * jsonUserData["sessions"][i]["place_multiplicator"]
	document.getElementById("mission").style.display = "block";
	user_target = jsonUserData["sessions"][i]["actual_user_target"];
	place_target = jsonUserData["sessions"][i]["actual_place_target"];
	object_target = jsonUserData["sessions"][i]["actual_object_target"];
	document.getElementById("user_target_declaration").innerHTML = user_target;
	document.getElementById("object_target_declaration").innerHTML = object_target;
	document.getElementById("place_target_declaration").innerHTML = place_target;
	document.getElementById("selected_session_text").innerHTML = selectedSession
	getMeKillList();
	requesthighlights();
	/*document.getElementById("mission").innerHTML = "<div> <img class=\"image_bhestia\" src=\"" +
	 jsonUserData["sessions"][i]["user_target_image"] + "\"></img> <label>" + 
	 jsonUserData["sessions"][i]["actual_user_target"] + "</label> </div>";*/
}

function requesthighlightsOfClosedSession(){
	var xhr = new XMLHttpRequest();
	var url = "requestHighlights";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			jsonResponse = JSON.parse(xhr.responseText);
			if (jsonResponse["status_code"] == 200){
				var table = document.getElementById("endedSessionHighlightsTable");
				table.innerHTML = "";
				var highlights = jsonResponse["highlights"]
				var i = 0
				if(highlights[0] == null){
					table.insertRow(0).insertCell(0).innerHTML = "you don't have any kill declaration";
				}
				while(highlights[i] != null){
					//for images: cell1.innerHTML = '<img src="folder.png" class="icon">'; //width="' + (window.screen.height * window.devicePixelRatio) / 20 + '" height="' + (window.screen.height * window.devicePixelRatio) / 20 + '" />';
					var row = table.insertRow(0);
					var username = row.insertCell(0);
					var points = row.insertCell(1);
					
					username.innerHTML = highlights[i]["username"]
					points.innerHTML = highlights[i]["points"]

					row.setAttribute("username", highlights[i]["username"]);

					/*
					var createClickHandler = function(row) {
						return function() {
							$("#confirm_kill_modal").modal();
							killer_to_confirm = row.getAttribute("killer");
							place_to_confirm = row.getAttribute("place");
							object_to_confirm = row.getAttribute("object");
							killer_image = row.getAttribute("killer_image");
							place_image = row.getAttribute("place_image");
							object_image = row.getAttribute("object_image");
							time = row.getAttribute("time");
							document.getElementById("killer_pending_declaration").innerHTML = killer_to_confirm;
							document.getElementById("object_pending_declaration").innerHTML = object_to_confirm;
							document.getElementById("place_pending_declaration").innerHTML = place_to_confirm;
							document.getElementById("time_pending_declaration").innerHTML = time;
							
							document.getElementById("killer_pending_image").src = killer_image;
							document.getElementById("place_pending_image").src = place_image;
							document.getElementById("object_pending_image").src = object_image;
						};
					};
					row.onclick = createClickHandler(row);
					*/

					//time.innerHTML = killList[i]["time"]
					i++;
				}
				if(highlights[0] != null){
					var row = table.insertRow(0);
					var username = row.insertCell(0);
					var points = row.insertCell(1);
					username.innerHTML = "username"
					points.innerHTML = "points"
				}
			}else if(killListResponse["status_code"] == 401){
				var table = document.getElementById("highlightsTable");
				table.insertRow(0).insertCell(0).innerHTML = "seems an error occured in retriving highlights";
				//alert("you don't have kill declarations till now")
			}else{
				alert("error on server response retriving highlights")
			}
		}
	}
	var data = JSON
	.stringify({
		"session" : selectedEndedSession
	});
	xhr.send(data);
}

function requesthighlights(){
	var xhr = new XMLHttpRequest();
	var url = "requestHighlights";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			jsonResponse = JSON.parse(xhr.responseText);
			if (jsonResponse["status_code"] == 200){
				var table = document.getElementById("highlightsTable");
				table.innerHTML = "";
				var highlights = jsonResponse["highlights"]
				var i = 0
				if(highlights[0] == null){
					table.insertRow(0).insertCell(0).innerHTML = "you don't have any kill declaration";
				}
				while(highlights[i] != null){
					//for images: cell1.innerHTML = '<img src="folder.png" class="icon">'; //width="' + (window.screen.height * window.devicePixelRatio) / 20 + '" height="' + (window.screen.height * window.devicePixelRatio) / 20 + '" />';
					var row = table.insertRow(0);
					var username = row.insertCell(0);
					var points = row.insertCell(1);
					
					username.innerHTML = highlights[i]["username"]
					points.innerHTML = highlights[i]["points"]

					row.setAttribute("username", highlights[i]["username"]);

					/*
					var createClickHandler = function(row) {
						return function() {
							$("#confirm_kill_modal").modal();
							killer_to_confirm = row.getAttribute("killer");
							place_to_confirm = row.getAttribute("place");
							object_to_confirm = row.getAttribute("object");
							killer_image = row.getAttribute("killer_image");
							place_image = row.getAttribute("place_image");
							object_image = row.getAttribute("object_image");
							time = row.getAttribute("time");
							document.getElementById("killer_pending_declaration").innerHTML = killer_to_confirm;
							document.getElementById("object_pending_declaration").innerHTML = object_to_confirm;
							document.getElementById("place_pending_declaration").innerHTML = place_to_confirm;
							document.getElementById("time_pending_declaration").innerHTML = time;
							
							document.getElementById("killer_pending_image").src = killer_image;
							document.getElementById("place_pending_image").src = place_image;
							document.getElementById("object_pending_image").src = object_image;
						};
					};
					row.onclick = createClickHandler(row);
					*/

					//time.innerHTML = killList[i]["time"]
					i++;
				}
				if(highlights[0] != null){
					var row = table.insertRow(0);
					var username = row.insertCell(0);
					var points = row.insertCell(1);
					username.innerHTML = "username"
					points.innerHTML = "points"
				}
			}else if(killListResponse["status_code"] == 401){
				var table = document.getElementById("highlightsTable");
				table.insertRow(0).insertCell(0).innerHTML = "seems an error occured in retriving highlights";
				//alert("you don't have kill declarations till now")
			}else{
				alert("error on server response retriving highlights")
			}
		}
	}
	var data = JSON
	.stringify({
		"session" : selectedSession
	});
	xhr.send(data);
}

function getMeKillList(){
	var xhr = new XMLHttpRequest();
	var url = "getPendingKill";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			killListResponse = JSON.parse(xhr.responseText);
			if (killListResponse["status_code"] == 200){
				var table = document.getElementById("pendingKillDeclarations");
				table.innerHTML = "";
				var killList = killListResponse["kill_list"]
				var i = 0
				if(killList[0] == null){
					table.insertRow(0).insertCell(0).innerHTML = "you don't have any kill declaration";
				}
				while(killList[i] != null){
					//for images: cell1.innerHTML = '<img src="folder.png" class="icon">'; //width="' + (window.screen.height * window.devicePixelRatio) / 20 + '" height="' + (window.screen.height * window.devicePixelRatio) / 20 + '" />';
					var row = table.insertRow(0);
					var killer = row.insertCell(0);
					var killed = row.insertCell(1);
					var place = row.insertCell(2);
					var object = row.insertCell(3);
					//var time = row.insertCell(4);
					killer.innerHTML = killList[i]["killer"]
					killed.innerHTML = killList[i]["killed"]
					place.innerHTML = killList[i]["place"]
					object.innerHTML = killList[i]["object"]

					row.setAttribute("killer", killList[i]["killer"]);
					row.setAttribute("killer_image", killList[i]["killer_image"]);
					row.setAttribute("place", killList[i]["place"]);
					row.setAttribute("place_image", killList[i]["place_image"]);
					row.setAttribute("object", killList[i]["object"]);
					row.setAttribute("object_image", killList[i]["object_image"]);
					row.setAttribute("time", killList[i]["time"]);


					var createClickHandler = function(row) {
						return function() {
							$("#confirm_kill_modal").modal();
							killer_to_confirm = row.getAttribute("killer");
							place_to_confirm = row.getAttribute("place");
							object_to_confirm = row.getAttribute("object");
							killer_image = row.getAttribute("killer_image");
							place_image = row.getAttribute("place_image");
							object_image = row.getAttribute("object_image");
							time = row.getAttribute("time");
							document.getElementById("killer_pending_declaration").innerHTML = killer_to_confirm;
							document.getElementById("object_pending_declaration").innerHTML = object_to_confirm;
							document.getElementById("place_pending_declaration").innerHTML = place_to_confirm;
							document.getElementById("time_pending_declaration").innerHTML = time;
							
							document.getElementById("killer_pending_image").src = killer_image;
							document.getElementById("place_pending_image").src = place_image;
							document.getElementById("object_pending_image").src = object_image;
						};
					};
					row.onclick = createClickHandler(row);

					//time.innerHTML = killList[i]["time"]
					i++;
				}
			}else if(killListResponse["status_code"] == 401){
				var table = document.getElementById("pendingKillDeclarations");
				table.insertRow(0).insertCell(0).innerHTML = "you don't have kill declarations till now";
				//alert("you don't have kill declarations till now")
			}else{
				alert("error on server response")
			}
		}
	}
	var data = JSON
	.stringify({
		"session" : selectedSession
	});
	xhr.send(data);
}

function confirmBeingKilled(){
	var xhr = new XMLHttpRequest();
	var url = "confirmDead";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			getMeKillList()
		}
	}
	var data = JSON
	.stringify({
		"killer" : killer_to_confirm,
		"session" : selectedSession
	});
	xhr.send(data);
}

function confirmKill(){
var xhr = new XMLHttpRequest();
	var url = "killAction";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var data = JSON
	.stringify({
		"killed" : user_target,
		"session" :  selectedSession,
		"place" : place_target,
		"object" : object_target
	});
	xhr.send(data);
	alert("send kill notification, wait for the oponent to confirm your murder")
}

function denyBeingKilled(){
	var xhr = new XMLHttpRequest();
	var url = "denyDead";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			getMeKillList()
		}
	}
	var data = JSON
	.stringify({
		"killer" : killer_to_confirm,
		"session" : selectedSession
	});
	xhr.send(data);
}