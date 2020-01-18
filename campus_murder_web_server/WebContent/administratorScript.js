
var jsonUserData;
var meKilledDeclaration;
var onDeletingSessionName = "";
var selectedUser = "";
var onDeletingObjectName = "";
var onDeletingPlaceName = "";
var selectedSession = "";
var sessionObjectName = "";
var sessionPlaceName = "";
var sessionUserName = "";


window.onload = function() {
	$("#loginModal").modal();
};

function login() {
	var xhr = new XMLHttpRequest();
	var url = "administratorLoginAction";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			jsonUserData = JSON.parse(xhr.responseText);
			if (jsonUserData["status_code"] == 200){
				$("#loginModal").modal('hide');
				retriveSession();
				retriveObjects();
				retrivePlaces();
				retriveUsers();
			}
			else{
				alert("username or password invalid")
			}
		}
	};
	var data = JSON
	.stringify({
		"administrator_password" : document
		.getElementById("administrator_password").value
	});
	xhr.send(data);
}

function retriveSession() {
	var xhr = new XMLHttpRequest();
	var url = "requestAdministratorSessionList";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			sessionsData = JSON.parse(xhr.responseText);
			if (sessionsData["status_code"] == 200){
				var table = document.getElementById("allSessionsTable");
				table.innerHTML = ""
					var i = 0
					while(sessionsData["sessions"][i] != null){

						//var description = sessionsData[i]["description"]

						var date_start = new Date(sessionsData["sessions"][i]["start"]);
						var date_end = new Date(sessionsData["sessions"][i]["end"]);
						var date = new Date();
						var now = date.getTime();

						var session_duration = date_end.getTime() - date_start.getTime();
						var past = now - date_start.getTime();
						var percentual = Math.trunc((past / session_duration) * 100);					
						var row = table.insertRow(0);
						row.setAttribute("session_name", sessionsData["sessions"][i]["session_name"]);
						row.setAttribute("start", sessionsData["sessions"][i]["start"]);
						row.setAttribute("end", sessionsData["sessions"][i]["end"]);
						row.setAttribute("description", sessionsData["sessions"][i]["description"]);
						var createClickHandler = function(row) {
							return function() {
								$("#session_modal").modal();
								onDeletingSessionName = row.getAttribute("session_name");
								document.getElementById("opened_session_name").innerHTML = row.getAttribute("session_name");
								document.getElementById("opened_session_start_date").innerHTML = row.getAttribute("start");
								document.getElementById("opened_session_end_date").innerHTML = row.getAttribute("end");
								document.getElementById("opened_session_description").innerHTML = row.getAttribute("description");

							};
						};
						row.onclick = createClickHandler(row);

						var session_name = row.insertCell(0);
						var passed = row.insertCell(1)
						passed.className = "floatRight";


						session_name.innerHTML = "<div style=\"height: 60px; line-height: 60px;\"> <label>" + 
						sessionsData["sessions"][i]["session_name"] + "</label> </div>";

						passed.innerHTML = "<div style=\"display: inline; width: 60px; height: 60px;\">\
							<canvas width=\"60\" height=\"60\"\
							style=\"width: 30px; height: 30px;\"></canvas>\
							<input type=\"text\" class=\"knob\" data-readonly=\"true\"\
							value=\"" + percentual + "\" data-width=\"60\" data-height=\"60\"\
							data-fgcolor=\"#ffffffff\" data-bgcolor=\"#39CCCCaa\" readonly=\"readonly\">\</div>";
						i++;
					}
				$('.knob').knob();
			}
			else{
				alert("server-side error, contact administrator and good luck for debugging :)")
			}
		}
	};
	xhr.send();
}

function retriveObjects() {
	var xhr = new XMLHttpRequest();
	var url = "requestObjects";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			objectsData = JSON.parse(xhr.responseText);
			if (objectsData["status_code"] == 200){
				var objects = objectsData["objects"]
				var node = document.getElementById("object_images");
				node.innerHTML = '';
				var i = 0
				while(objects[i] != null){
					var img = document.createElement("img");
					img.className = "image_bhestia";
					img.src = objects[i]["object_image"];

					img.setAttribute("object_name", objects[i]["object_name"]);
					img.setAttribute("object_description", objects[i]["object_description"]);
					img.setAttribute("object_multiplicator", objects[i]["object_multiplicator"]);
					img.setAttribute("object_image", objects[i]["object_image"]);

					var createClickHandler = function(img) {
						return function() {
							$("#object_modal").modal();
							onDeletingObjectName = img.getAttribute("object_name");
							document.getElementById("opened_object_name").innerHTML = img.getAttribute("object_name");
							document.getElementById("opened_object_description").innerHTML = img.getAttribute("object_description");
							document.getElementById("opened_object_multiplicator").innerHTML = img.getAttribute("object_multiplicator");
						};
					};
					img.onclick = createClickHandler(img);

				    node.appendChild(img);
					i++;
				}
				
			}else{
				alert("something went wrong")
			}
		}
	}
	xhr.send(null);
}

function retrivePlaces() {
	var xhr = new XMLHttpRequest();
	var url = "requestPlaces";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			objectsData = JSON.parse(xhr.responseText);
			if (objectsData["status_code"] == 200){
				var objects = objectsData["places"]
				var node = document.getElementById("place_images");
				node.innerHTML = '';
				var i = 0
				while(objects[i] != null){
					var img = document.createElement("img");
					img.className = "image_bhestia";
					img.src = objects[i]["place_image"];

					img.setAttribute("place_name", objects[i]["place_name"]);
					img.setAttribute("place_description", objects[i]["place_description"]);
					img.setAttribute("place_multiplicator", objects[i]["place_multiplicator"]);
					img.setAttribute("place_image", objects[i]["place_image"]);

					var createClickHandler = function(img) {
						return function() {
							$("#place_modal").modal();
							onDeletingPlaceName = img.getAttribute("place_name");
							document.getElementById("opened_place_name").innerHTML = img.getAttribute("place_name");
							document.getElementById("opened_place_description").innerHTML = img.getAttribute("place_description");
							document.getElementById("opened_place_multiplicator").innerHTML = img.getAttribute("place_multiplicator");
						};
					};
					img.onclick = createClickHandler(img);

				    node.appendChild(img);
					i++;
				}
				
			}else{
				alert("something went wrong, contact administrator and good luck for debugging :)")
			}
		}
	}
	xhr.send(null);
}


function createSession() {
	var xhr = new XMLHttpRequest();
	var url = "createSession";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			sessionsData = JSON.parse(xhr.responseText);
			if (sessionsData["status_code"] == 200){
				retriveSession()
				
			}
			else{
				alert("serverside error, a session with that name already exists?")
			}
		}
	};
	var data = JSON.stringify({
		"session_name" : document.getElementById("new_session_name").value,
		"description" : document.getElementById("new_session_description").value,
		"starting_date" : document.getElementById("new_session_start_date").value,
		"ending_date" : document.getElementById("new_session_end_date").value
	});
	xhr.send(data);
}

function deleteSession() {
	var xhr = new XMLHttpRequest();
	var url = "deleteSession";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			serverResponse = JSON.parse(xhr.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSession()
			}
			else{
				alert("serverside error, some users still registed on that session?");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : onDeletingSessionName
	});
	xhr.send(data);
}


function retriveUsers(){
	var xhr = new XMLHttpRequest();
	var url = "getAllUsers";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			usersData = JSON.parse(xhr.responseText);
			if (usersData["status_code"] == 200){
				var users = usersData["users"]
				var node = document.getElementById("profile_images");
				node.innerHTML = '';
				var i = 0
				while(users[i] != null){
					var img = document.createElement("img");
					img.className = "image_bhestia";
					img.src = users[i]["profile_image"];

					img.setAttribute("username", users[i]["username"]);
					img.setAttribute("profile_image", users[i]["profile_image"]);

					var createClickHandler = function(img) {
						return function() {
							$("#user_modal").modal();
							selectedUser = img.getAttribute("username");
							document.getElementById("opened_user_name").innerHTML = selectedUser;
						};
					};
					img.onclick = createClickHandler(img);

				    node.appendChild(img);
					i++;
				}
				
			}else{
				alert("something went wrong, contact administrator and good luck for debugging :)")
			}
		}
	}
	xhr.send(null);
}


function createUser(){
	var profile_image = document.getElementById("new_user_image").files[0];
	var formdata = new FormData();
	formdata.append("profile_image", profile_image);
	formdata.append("username", document.getElementById("new_user_name").value);
	formdata.append("password", document.getElementById("new_user_password").value);
	
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "uploadNewUser");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveUsers()
			}
			else{
				alert("serverside error, an object with same name already exist?");
			}
		}
	}
	ajax.send(formdata);
}

function createObject(){
	var object_image = document.getElementById("object_image").files[0];
	var formdata = new FormData();
	formdata.append("object_image", object_image);
	formdata.append("object_name", document.getElementById("new_object_name").value);
	formdata.append("description", document.getElementById("new_object_description").value);
	formdata.append("multiplicator", document.getElementById("new_object_multiplicator").value);
	
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "uploadObject");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveObjects()
			}
			else{
				alert("serverside error, an object with same name already exist?");
			}
		}
	}
	ajax.send(formdata);
}

function createPlace(){
	var object_image = document.getElementById("place_image").files[0];
	var formdata = new FormData();
	formdata.append("place_image", object_image);
	formdata.append("place_name", document.getElementById("new_place_name").value);
	formdata.append("description", document.getElementById("new_place_description").value);
	formdata.append("multiplicator", document.getElementById("new_place_multiplicator").value);
	
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "uploadPlace"); 
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retrivePlaces()
			}
			else{
				alert("serverside error, a place with same name already exist?");
			}
		}
	}
	ajax.send(formdata);
}

function deleteObject() {
	var xhr = new XMLHttpRequest();
	var url = "deleteObject";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			serverResponse = JSON.parse(xhr.responseText);
			if (serverResponse["status_code"] == 200){
				retriveObjects()
			}
			else{
				alert("serverside error, some users has to kill with that object? you cannot delete in case...");
			}
		}
	}
	var data = JSON.stringify({
		"object_name" : onDeletingObjectName
	});
	xhr.send(data);
}

function deletePlace() {
	var xhr = new XMLHttpRequest();
	var url = "deletePlace";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			serverResponse = JSON.parse(xhr.responseText);
			if (serverResponse["status_code"] == 200){
				retrivePlaces()
			}
			else{
				alert("serverside error, some users has to kill with that object? you cannot delete in case...");
			}
		}
	}
	var data = JSON.stringify({
		"place_name" : onDeletingPlaceName
	});
	xhr.send(data);
}

function useThisSession(){
	selectedSession = onDeletingSessionName;
	document.getElementById("selected_session_text").innerHTML = "session : " + selectedSession;
	document.getElementById("start_session_button").style.display = "block"
	retriveSessionObjects();
	retriveSessionPlaces();
	retriveSessionUsers();
	retriveReport();
}

function retriveReport(){
	var xhr = new XMLHttpRequest();
	var url = "requestReport";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			killListResponse = JSON.parse(xhr.responseText);
			if (killListResponse["status_code"] == 200){
				var table = document.getElementById("reportTable");
				table.innerHTML = "";
				var killList = killListResponse["report"]
				var i = 0
				while(killList[i] != null){
					//for images: cell1.innerHTML = '<img src="folder.png" class="icon">'; //width="' + (window.screen.height * window.devicePixelRatio) / 20 + '" height="' + (window.screen.height * window.devicePixelRatio) / 20 + '" />';
					var row = table.insertRow(0);
					var type = row.insertCell(0)
					var killer = row.insertCell(1);
					var killed = row.insertCell(2);
					var time = row.insertCell(3);
					
					//var time = row.insertCell(4);
					type.innerHTML = killList[i]["type"]
					killer.innerHTML = killList[i]["killer"]
					killed.innerHTML = killList[i]["killed"]
					time.innerHTML = killList[i]["time"]
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
		"session" : selectedSession
	});
	xhr.send(data);
}

function startSession(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "startSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				alert("every user of this session has now a mission, good game to everyone!");
			}
			else if(serverResponse["status_code"] == 405){
				alert("serverside error, every user has already a mission, if you want you can force restart the session changing mission to everybody");
			}else {
				alert("serverside error, this should not have happened");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession,
		"force_start" : "no"
	});
	ajax.send(data);
}

function retriveSessionObjects(){
	var xhr = new XMLHttpRequest();
	var url = "getSelectedSessionObjects";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			serverResponse = JSON.parse(xhr.responseText);
			if (serverResponse["status_code"] == 200){
				var objects = serverResponse["objects"]
				var node = document.getElementById("session_object_images");
				node.innerHTML = '';
				var i = 0
				while(objects[i] != null){
					var img = document.createElement("img");
					img.className = "image_bhestia";
					img.src = objects[i]["object_image"];

					img.setAttribute("object_name", objects[i]["object_name"]);
					img.setAttribute("object_description", objects[i]["object_description"]);
					img.setAttribute("object_multiplicator", objects[i]["object_multiplicator"]);
					img.setAttribute("object_image", objects[i]["object_image"]);

					
					var createClickHandler = function(img) {
						return function() {
							$("#remove_object_from_session_modal").modal();
							sessionObjectName = img.getAttribute("object_name");
							document.getElementById("removing_object_from_session_name").innerHTML = img.getAttribute("object_name");
						};
					};
					img.onclick = createClickHandler(img);
					

				    node.appendChild(img);
					i++;
				}
				
			}else{
				alert("something went wrong")
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession
	});
	xhr.send(data);
}

function retriveSessionPlaces(){
	var xhr = new XMLHttpRequest();
	var url = "getSelectedSessionPlaces";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			serverResponse = JSON.parse(xhr.responseText);
			if (serverResponse["status_code"] == 200){
				var places = serverResponse["places"]
				var node = document.getElementById("session_places_images");
				node.innerHTML = '';
				var i = 0
				while(places[i] != null){
					var img = document.createElement("img");
					img.className = "image_bhestia";
					img.src = places[i]["place_image"];

					img.setAttribute("place_name", places[i]["place_name"]);
					img.setAttribute("place_description", places[i]["place_description"]);
					img.setAttribute("place_multiplicator", places[i]["place_multiplicator"]);
					img.setAttribute("place_image", places[i]["place_image"]);

					
					var createClickHandler = function(img) {
						return function() {
							$("#remove_place_from_session_modal").modal();
							sessionPlaceName = img.getAttribute("place_name");
							document.getElementById("removing_place_from_session_name").innerHTML = img.getAttribute("place_name");
						};
					};
					img.onclick = createClickHandler(img);
					

				    node.appendChild(img);
					i++;
				}
				
			}else{
				alert("something went wrong")
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession
	});
	xhr.send(data);
}

function addObjectOnSession(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "addObjectToSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSessionObjects()
			}
			else{
				alert("serverside error, did you select a session?");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession,
		"object_name" : onDeletingObjectName
	});
	ajax.send(data);
}

function addPlaceOnSession(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "addPlaceToSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSessionPlaces()
			}
			else{
				alert("serverside error, did you select a session?");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession,
		"place_name" : onDeletingPlaceName
	});
	ajax.send(data);
}

function removeObjectFromSession(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "removeObjectFromSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSessionObjects();
			}
			else{
				alert("serverside error, this should not have happened...prey in god for a good debugging");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession,
		"object_name" : sessionObjectName
	});
	ajax.send(data);
}

function removePlaceFromSession(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "removePlaceFromSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSessionPlaces();
			}
			else{
				alert("serverside error, this should not have happened...prey in god for a good debugging?");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession,
		"place_name" : sessionPlaceName
	});
	ajax.send(data);
}

function addUserOnSession(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "addUserToSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSessionUsers()
			}
			else{
				alert("serverside error, have you select a session?");
			}
		}
	}
	var data = JSON.stringify({
		"username" : selectedUser,
		"points" : document.getElementById("starting_user_points").value,
		"session_name" : selectedSession
	});
	ajax.send(data);
}

function deleteUser(){
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "deleteUser");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveUsers()
			}
			else{
				alert("serverside error, this user is still registered to some sessions?");
			}
		}
	}
	var data = JSON.stringify({
		"username" : selectedUser,
		"session_name" : selectedSession
	});
	ajax.send(data);
} 

function retriveSessionUsers(){
	var xhr = new XMLHttpRequest();
	var url = "getSelectedSessionUsers";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 && xhr.status === 200) {
			serverResponse = JSON.parse(xhr.responseText);
			if (serverResponse["status_code"] == 200){
				var users = serverResponse["users"]
				var node = document.getElementById("session_profile_images");
				node.innerHTML = '';
				var i = 0
				while(users[i] != null){
					var img = document.createElement("img");
					img.className = "image_bhestia";
					img.src = users[i]["profile_image"];

					img.setAttribute("profile_image", users[i]["profile_image"]);
					img.setAttribute("username", users[i]["username"]);
					img.setAttribute("points", users[i]["points"]);
					
					
					var createClickHandler = function(img) {
						return function() {
							$("#remove_user_from_session_modal").modal();
							sessionUserName = img.getAttribute("username");
							document.getElementById("removing_user_from_session_name").innerHTML = img.getAttribute("username");
						};
					};
					img.onclick = createClickHandler(img);

				    node.appendChild(img);
					i++;
				}
				
			}else{
				alert("something went wrong")
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession
	});
	xhr.send(data);
}

function removeUserFromSession(){
	//removeUserFromSession
	var ajax = new XMLHttpRequest();
	ajax.open("POST", "removeUserFromSession");
	
	ajax.onreadystatechange = function() {
		if (ajax.readyState === 4 && ajax.status === 200) {
			serverResponse = JSON.parse(ajax.responseText);
			if (serverResponse["status_code"] == 200){
				retriveSessionUsers();
			}
			else{
				alert("serverside error, this should not have happened...prey in god for a good debugging?");
			}
		}
	}
	var data = JSON.stringify({
		"session_name" : selectedSession,
		"username" : sessionUserName
	});
	ajax.send(data);
}