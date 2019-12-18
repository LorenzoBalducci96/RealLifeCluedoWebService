var jsonUserData;
var meKilledDeclaration;
var selectedSession = "";

var killer_to_confirm = "";
var place_to_confirm = "";
var object_to_confirm = "";

var user_target = "";
var object_target = "";
var place_target = "";

window.onload = function() {
	$("#loginModal").modal();
};

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
				table.innerHTML = ""
				var i = 0
				while(jsonUserData["sessions"][i] != null){
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
					i++;
					$('.knob').knob();
				}
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
	/*document.getElementById("mission").innerHTML = "<div> <img class=\"image_bhestia\" src=\"" +
	 jsonUserData["sessions"][i]["user_target_image"] + "\"></img> <label>" + 
	 jsonUserData["sessions"][i]["actual_user_target"] + "</label> </div>";*/
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
					row.setAttribute("place", killList[i]["place"]);
					row.setAttribute("object", killList[i]["object"]);

					var createClickHandler = function(row) {
						return function() {
							$("#confirm_kill_modal").modal();
							killer_to_confirm = row.getAttribute("killer");
							place_to_confirm = row.getAttribute("place");
							object_to_confirm = row.getAttribute("object");
							document.getElementById("killer_pending_declaration").innerHTML = killer_to_confirm;
							document.getElementById("object_pending_declaration").innerHTML = object_to_confirm;
							document.getElementById("place_pending_declaration").innerHTML = place_to_confirm;
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

function openAllMurders(){
	document.getElementById("navbar_block").style.display = "block";
	document.getElementById("your_mission_block").style.display = "none";
	document.getElementById("highlights_block").style.display = "none";
	document.getElementById("all_murders_block").style.display = "block";

					var xhr = new XMLHttpRequest();
	var url = "requestKillList";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			killListResponse = JSON.parse(xhr.responseText);
			if (killListResponse["status_code"] == 200){
				var table = document.getElementById("allMurdersTable");
				table.innerHTML = "";
				var killList = killListResponse["kill_list"]
				var i = 0
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
					//time.innerHTML = killList[i]["time"]
					i++;
				}
			}else{
				alert("server error, not 200 status_code on json response")
			}
		}
	}
	var data = JSON
	.stringify({
		"session" : jsonUserData["sessions"][0]["session_name"]
	});
	xhr.send(data);
}

function openHighlights(){
	document.getElementById("navbar_block").style.display = "block";
	document.getElementById("your_mission_block").style.display = "none";
	document.getElementById("all_murders_block").style.display = "none";
	document.getElementById("highlights_block").style.display = "block";

					var xhr = new XMLHttpRequest();
	var url = "requestHighlights";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			killListResponse = JSON.parse(xhr.responseText);
			if (killListResponse["status_code"] == 200){
				var table = document.getElementById("highlightsTable");
				table.innerHTML = "";
				var highlights = killListResponse["highlights"]
				var i = 0
				while(highlights[i] != null){
					//for images: cell1.innerHTML = '<img src="folder.png" class="icon">'; //width="' + (window.screen.height * window.devicePixelRatio) / 20 + '" height="' + (window.screen.height * window.devicePixelRatio) / 20 + '" />';
					var row = table.insertRow(0);
					var user = row.insertCell(0);
					var points = row.insertCell(1);

					//var time = row.insertCell(4);
					user.innerHTML = highlights[i]["username"]
					points.innerHTML = highlights[i]["points"]
					i++;
				}
			}else{
				alert("server error, not 200 status_code on json response")
			}
		}
	}
	var data = JSON
	.stringify({
		"session" : jsonUserData["sessions"][0]["session_name"]
	});
	xhr.send(data);
}