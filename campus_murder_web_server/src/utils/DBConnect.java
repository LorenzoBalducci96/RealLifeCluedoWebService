package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.result.LocalDateTimeValueFactory;

import campusMurder.Encripter;

public class DBConnect {

	private static DBConnect instance;

	private Connection con;
	private static String DBuser = "campus_murder_logic";
	private static String DBPassword = null; //WARNING, used dist of mysql doen't have a password for user campus_murder_logic
	//PLEASE take into consideration
	private static String DatabaseTableName = "campus_murder";

	private PreparedStatement loginStatement;
	private PreparedStatement addPendingRegistration;
	private PreparedStatement completeRegistration;
	private PreparedStatement removeFromPendingRegistrations;
	private PreparedStatement retriveUserSessionData;

	private PreparedStatement getUserEmail;
	private PreparedStatement getKillerKilledSessionFromCode;

	//kill logic
	private PreparedStatement insertKillRequest;
	private PreparedStatement getPendingKill;
	private PreparedStatement confirmBeingKilled;
	private PreparedStatement denyBeingKilled;
	private PreparedStatement removePendingKill;
	private PreparedStatement increaseKillerPoints;
	private PreparedStatement decreaseKilledPoints;


	private PreparedStatement getReport;
	private PreparedStatement getKillList;
	private PreparedStatement getHighlights;
	private PreparedStatement getObjectList;
	private PreparedStatement getSessionObjectList;
	private PreparedStatement getPlaceList;
	private PreparedStatement getSessionPlaceList;
	private PreparedStatement addObject;
	private PreparedStatement addObjectToSession;
	private PreparedStatement addPlace;
	private PreparedStatement addPlaceToSession;
	private PreparedStatement deleteObject;
	private PreparedStatement deleteObjectFromSession;
	private PreparedStatement deletePlace;
	private PreparedStatement deletePlaceFromSession;
	private PreparedStatement getSessions;
	private PreparedStatement createSession;
	private PreparedStatement registerUser;
	private PreparedStatement deleteSession;
	private PreparedStatement deleteWaitingConfirmationKills;
	private PreparedStatement deleteKills;
	private PreparedStatement deleteRejectedKills;
	private PreparedStatement getAllUsers;
	private PreparedStatement getUserMission;
	private PreparedStatement addUserToSession;
	private PreparedStatement removeUserFromSession;
	private PreparedStatement getSessionUsersList;
	private PreparedStatement deleteUser;
	private PreparedStatement forceStartSession;
	private PreparedStatement startSession;
	private PreparedStatement updateUserMission;
	private Statement st;

	//private constructor to avoid client applications to use constructor
	private DBConnect(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DatabaseTableName, DBuser, DBPassword);

			getUserEmail = con.prepareStatement("select email "
					+ "from users "
					+ "where username=?");

			getKillerKilledSessionFromCode = con.prepareStatement("select killer, killed, session "
					+ "from waiting_confirmation_kills "
					+ "where confirmation_code=?");

			loginStatement = con.prepareStatement("select * from users where username=? and password_hash=?");
			//retriveSessionsStatement = con.prepareStatement("select * from inscriptions where username=?");
			
			retriveUserSessionData = con.prepareStatement("select i.session_name session_name, i.points points, "
					+ "s.start start, s.end end, s.description session_description, "
					+ "i.actual_object_target actual_object_target, o.image object_image, o.description object_description, o.multiplicator object_multiplicator, "
					+ "i.actual_place_target, p.image place_image, p.description place_description, p.multiplicator place_multiplicator,"
					+ "i.actual_user_target, u.profile_image user_target_image "
					+ "from inscriptions i "
					+ "inner join sessions s on i.session_name = s.session_name "
					+ "inner join objects o on i.actual_object_target = o.object_name "
					+ "inner join places p on i.actual_place_target = p.place_name "
					+ "inner join users u on i.actual_user_target = u.username "
					+ "where i.username=?;");
			insertKillRequest = con.prepareStatement("insert into waiting_confirmation_kills set killer=?,killed=?,session=?,place=?,object=?,confirmation_code=?,time=now()");
			getPendingKill = con.prepareStatement("select w.time time, w.killer killer, u.profile_image killer_image, w.place place, p.image place_image, w.object object, o.image object_image "
					+ "from waiting_confirmation_kills w "
					+ "inner join objects o on w.object = o.object_name "
					+ "inner join places p on w.place = p.place_name "
					+ "inner join users u on w.killer = u.username "
					+ "where killed=? and session=?");
			confirmBeingKilled = con.prepareStatement("insert into kills(killer,killed,session,place,object,time) " + 
					"select killer,killed,session,place,object,time from waiting_confirmation_kills " + 
					"where killer=? and killed=? and session=? " +
					"order by time " + 
					"limit 1;");
			denyBeingKilled = con.prepareStatement("insert into deniedKills "
					+ "select killer,killed,session,place,object,time "
					+ "from waiting_confirmation_kills "
					+ "where killer=? and killed=? and session=?;");
			removePendingKill = con.prepareStatement("delete from waiting_confirmation_kills "
					+ "where killer=? and killed=? and session=?;");
			increaseKillerPoints = con.prepareStatement("update inscriptions set points = points + 100 * "
					+ "(select o.multiplicator from objects o where o.object_name = actual_object_target) * "
					+ "(select p.multiplicator from places p where p.place_name = actual_place_target) "
					+ "where username=? and session_name=?");
			decreaseKilledPoints = con.prepareStatement("update inscriptions set points = points - 50 "
					+ "where username=? and session_name=?");
			getUserMission = con.prepareStatement("select i.session_name session_name, i.points points, " + 
					"s.start start, s.end end, s.description session_description, " + 
					"i.actual_object_target actual_object_target, o.image object_image, o.description object_description, o.multiplicator object_multiplicator, " + 
					"i.actual_place_target, p.image place_image, p.description place_description, p.multiplicator place_multiplicator, " + 
					"i.actual_user_target, u.profile_image user_target_image " + 
					"from inscriptions i " + 
					"inner join sessions s on i.session_name = s.session_name " + 
					"inner join objects o on i.actual_object_target = o.object_name " + 
					"inner join places p on i.actual_place_target = p.place_name " + 
					"inner join users u on i.actual_user_target = u.username " + 
					"where i.username=? and i.session_name=?;");
			getKillList = con.prepareStatement("select k.killer as killer, killer.profile_image as killer_profile_image, "
					+ "k.killed as killed, killed.profile_image as killed_profile_image, "
					+ "k.object as object, o.object_name as object_name, "
					+ "k.place as place, p.place_name as place_name, "
					+ "k.time as time from kills k " +
					"inner join objects o on k.object = o.object_name " + 
					"inner join places p on k.place = p.place_name " + 
					"inner join users killer on k.killer = killer.username " + 
					"inner join users killed on k.killed = killed.username " +
					"where k.session=? order by k.time");
			//session_name, session_name, session_name
			getReport = con.prepareStatement("(select 'confirmed_kill' as type,killer,killed,place,object,time "
					+ "from kills where session=?) union "
					+ "(select 'denied_kill' as type,killer,killed,place,object,time "
					+ "from deniedKills where session=?) union "
					+ "(select 'pending_kill' as type,killer,killed,place,object,time "
					+ "from waiting_confirmation_kills where session=?) order by time;");
			getHighlights = con.prepareStatement("select * from inscriptions where session_name=? order by points;");
			getObjectList = con.prepareStatement("select * from objects");
			getSessionObjectList = con.prepareStatement("select * from objects o inner join objects_on_session s on o.object_name=s.object_name and s.session_name=?;");
			addObject = con.prepareStatement("insert into objects set object_name=?, image=?, multiplicator=?, description=?");
			getSessions = con.prepareStatement("select * from sessions");
			createSession = con.prepareStatement("insert into sessions set session_name=?, description=?, start=?, end=?");
			
			deleteWaitingConfirmationKills = con.prepareStatement("delete from waiting_confirmation_kills where session=?;");
			deleteKills = con.prepareStatement("delete from kills where session=?;");
			deleteRejectedKills = con.prepareStatement("delete from deniedKills where session=?;");
			deleteSession = con.prepareStatement("delete from sessions where session_name=?;");
			
			deleteObject = con.prepareStatement("delete from objects where object_name=?");
			getPlaceList = con.prepareStatement("select * from places");
			getSessionPlaceList = con.prepareStatement("select * from places p inner join places_on_session s on p.place_name=s.place_name and s.session_name=?;");
			addPlace = con.prepareStatement("insert into places set place_name=?, image=?, multiplicator=?, description=?");
			deletePlace = con.prepareStatement("delete from places where place_name=?");
			addObjectToSession = con.prepareStatement("insert into objects_on_session set object_name=?, session_name=?");
			addPlaceToSession = con.prepareStatement("insert into places_on_session set place_name=?, session_name=?");
			deleteObjectFromSession = con.prepareStatement("delete from objects_on_session where session_name=? and object_name=?");
			deletePlaceFromSession = con.prepareStatement("delete from places_on_session where session_name=? and place_name=?");
			getAllUsers = con.prepareStatement("select * from users");
			addUserToSession = con.prepareStatement("insert into inscriptions set username=?,session_name=?,points=?");
			removeUserFromSession = con.prepareStatement("delete from inscriptions where username=? and session_name=?");
			getSessionUsersList = con.prepareStatement("select i.session_name,i.username,i.points,i.actual_object_target,i.actual_user_target,i.actual_place_target,u.profile_image from inscriptions i inner join users u on i.username=u.username where i.session_name=?;");
			registerUser = con.prepareStatement("insert into users set username=?,profile_image=?,password_hash=?");
			//addPendingRegistration = con.prepareStatement("insert into pending_registrations set username=?,profile_image=?,password_hash=?,email=?");
			//username, profile_image, email, password_hash, username, confirmation_code
			addPendingRegistration = con.prepareStatement("INSERT INTO pending_registrations (username, profile_image, email, password_hash, confirmation_code) " + 
					"SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS tmp " + 
					"WHERE NOT EXISTS (SELECT username FROM users WHERE username = ?) LIMIT 1;");
			completeRegistration = con.prepareStatement("insert into users(username, profile_image, password_hash, email) " + 
					"select username, profile_image, password_hash, email " + 
					"from pending_registrations pen where pen.confirmation_code=?");
			removeFromPendingRegistrations = con.prepareStatement("delete from pending_registrations " +
					"where confirmation_code=?");


			deleteUser = con.prepareStatement("delete from users where username=?");
			forceStartSession = con.prepareStatement("update inscriptions set " + 
					"actual_object_target = (select o.object_name from objects_on_session o where o.session_name=? order by rand() limit 1), " + 
					"actual_place_target = (select p.place_name from places_on_session p where p.session_name=? order by rand() limit 1), " + 
					"actual_user_target = (select i.username from (select * from inscriptions) i where i.session_name=? and i.username<>username order by rand() limit 1) " + 
					"where session_name=?;");

			startSession = con.prepareStatement("update inscriptions old set " + 
					"old.actual_object_target = (select o.object_name from objects_on_session o where o.session_name=? order by rand() limit 1), " + 
					"old.actual_place_target = (select p.place_name from places_on_session p where p.session_name=? order by rand() limit 1), " + 
					"old.actual_user_target = (select i.username from (select * from inscriptions) i where i.session_name=? and i.username<>old.username order by rand() limit 1) " + 
					"where old.session_name=? and " +
					"(old.actual_user_target is null or actual_object_target is null or actual_place_target is null);");
			
			
			/*
			 * select i.username as username, COALESCE((select count(actual_user_target)
				from inscriptions
				where actual_user_target=i.username
				group by actual_user_target), 0) as count
				from inscriptions i
				order by count
				limit 1
			 * */
			
			/*
			updateUserMission = con.prepareStatement("update inscriptions old set " + 
					"old.actual_object_target = (select o.object_name from objects_on_session o where o.session_name=? order by rand() limit 1), " + 
					"old.actual_place_target = (select p.place_name from places_on_session p where p.session_name=? order by rand() limit 1), " +
					"old.actual_user_target = (select i.username from (select * from inscriptions) i where i.session_name=? and i.username<>old.username order by rand() limit 1) " +
					"where old.session_name=? and old.username=?;");
			*/
			//session_name session_name username(killer) session_name username=(killer)
			updateUserMission = con.prepareStatement("update inscriptions old set " + 
					"old.actual_object_target = (select o.object_name from objects_on_session o where o.session_name=? order by rand() limit 1), " + 
					"old.actual_place_target = (select p.place_name from places_on_session p where p.session_name=? order by rand() limit 1), " + 
					"old.actual_user_target = (select subquery.username from( " + 
					"select i.username as username, COALESCE((select count(actual_user_target) " + 
					"	from inscriptions " + 
					"	where actual_user_target=i.username " + 
					"	group by actual_user_target), 0) as count " + 
					"from inscriptions i " + 
					"where username<>? " + 
					"order by count " + 
					"limit 1 " + 
					") subquery) " + 
					"where old.session_name=? and old.username=?;");
			st = con.createStatement();

		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("not handled exception");
		}
	}

	public static DBConnect getInstance(){
		if (instance == null)
			instance = new DBConnect();
		try {
			if(instance.con.isClosed())
				instance = new DBConnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return instance;
	}

	public JSONObject registerNewUser(String username, String profile_image, String password_hash) {
		try {
			registerUser.setString(1, username);
			registerUser.setString(2, profile_image);
			registerUser.setString(3, password_hash);
			if(registerUser.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}
	
	public JSONObject getUserData(String username) {
		try {
			ResultSet rs_sessions;
			retriveUserSessionData.setString(1, username);
			rs_sessions = retriveUserSessionData.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			jsonObject.accumulate("username", username);
			
			JSONArray jsonArray = new JSONArray();
			while(rs_sessions.next()) {
				JSONObject jsonObjectSession = new JSONObject();
				jsonObjectSession.accumulate("session_name", rs_sessions.getString("session_name"));
				jsonObjectSession.accumulate("session_description", rs_sessions.getString("session_description"));
				jsonObjectSession.accumulate("points", rs_sessions.getString("points"));
				jsonObjectSession.accumulate("start", rs_sessions.getString("start"));
				jsonObjectSession.accumulate("end", rs_sessions.getString("end"));
				if(LocalDateTime.parse(rs_sessions.getString("end"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isAfter(LocalDateTime.now())) {
					jsonObjectSession.accumulate("session_ended", "no");
					jsonObjectSession.accumulate("actual_object_target", rs_sessions.getString("actual_object_target"));
					jsonObjectSession.accumulate("object_image", rs_sessions.getString("object_image"));		
					jsonObjectSession.accumulate("object_description", rs_sessions.getString("object_description"));
					jsonObjectSession.accumulate("object_multiplicator", rs_sessions.getFloat("object_multiplicator"));

					jsonObjectSession.accumulate("actual_place_target", rs_sessions.getString("actual_place_target"));
					jsonObjectSession.accumulate("place_image", rs_sessions.getString("place_image"));
					jsonObjectSession.accumulate("place_description", rs_sessions.getString("place_description"));
					jsonObjectSession.accumulate("place_multiplicator", rs_sessions.getFloat("place_multiplicator"));


					jsonObjectSession.accumulate("actual_user_target", rs_sessions.getString("actual_user_target"));
					jsonObjectSession.accumulate("user_target_image", rs_sessions.getString("user_target_image"));
					jsonArray.put(jsonObjectSession);
				}else {
					jsonObjectSession.accumulate("session_ended", "yes");
					jsonArray.put(jsonObjectSession);
				}
			}
			jsonObject.put("sessions", jsonArray);
			return jsonObject;
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
		
	}

	public JSONObject autenticate(String username, String password) {
		try {

			ResultSet rs_login;
			ResultSet rs_sessions;
			password = Encripter.get_SHA_512_SecurePassword(password);

			loginStatement.setString(1, username);
			loginStatement.setString(2, password);
			retriveUserSessionData.setString(1, username);
			rs_login = loginStatement.executeQuery();
			rs_sessions = retriveUserSessionData.executeQuery();


			if(rs_login.next()) {//just for check password
				String profileImage = rs_login.getString("profile_image");
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("status_code", 200);
				jsonObject.accumulate("username", username);
				jsonObject.accumulate("profile_image", profileImage);

				JSONArray jsonArray = new JSONArray();
				while(rs_sessions.next()) {
					JSONObject jsonObjectSession = new JSONObject();
					jsonObjectSession.accumulate("session_name", rs_sessions.getString("session_name"));
					jsonObjectSession.accumulate("session_description", rs_sessions.getString("session_description"));
					jsonObjectSession.accumulate("points", rs_sessions.getString("points"));
					jsonObjectSession.accumulate("start", rs_sessions.getString("start"));
					jsonObjectSession.accumulate("end", rs_sessions.getString("end"));
					if(LocalDateTime.parse(rs_sessions.getString("end"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isAfter(LocalDateTime.now())) {
						jsonObjectSession.accumulate("session_ended", "no");
						jsonObjectSession.accumulate("actual_object_target", rs_sessions.getString("actual_object_target"));
						jsonObjectSession.accumulate("object_image", rs_sessions.getString("object_image"));		
						jsonObjectSession.accumulate("object_description", rs_sessions.getString("object_description"));
						jsonObjectSession.accumulate("object_multiplicator", rs_sessions.getFloat("object_multiplicator"));
	
						jsonObjectSession.accumulate("actual_place_target", rs_sessions.getString("actual_place_target"));
						jsonObjectSession.accumulate("place_image", rs_sessions.getString("place_image"));
						jsonObjectSession.accumulate("place_description", rs_sessions.getString("place_description"));
						jsonObjectSession.accumulate("place_multiplicator", rs_sessions.getFloat("place_multiplicator"));
	
	
						jsonObjectSession.accumulate("actual_user_target", rs_sessions.getString("actual_user_target"));
						jsonObjectSession.accumulate("user_target_image", rs_sessions.getString("user_target_image"));
						jsonArray.put(jsonObjectSession);
					}else {
						jsonObjectSession.accumulate("session_ended", "yes");
						jsonArray.put(jsonObjectSession);
					}
				}
				jsonObject.put("sessions", jsonArray);

				return jsonObject;
			}
			else
				return new JSONObject().accumulate("status_code", 401);
		} catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject confirmBeingKilled(String killer, String killed, String session) {
		try {
			confirmBeingKilled.setString(1, killer);
			confirmBeingKilled.setString(2, killed);
			confirmBeingKilled.setString(3, session);
			removePendingKill.setString(1, killer);
			removePendingKill.setString(2, killed);
			removePendingKill.setString(3, session);
			increaseKillerPoints.setString(1, killer);
			increaseKillerPoints.setString(2,session);
			decreaseKilledPoints.setString(1, killed);
			decreaseKilledPoints.setString(2, session);

			st.execute("start transaction");
			if(confirmBeingKilled.executeUpdate() > 0) { //insert into kills...
				if(removePendingKill.executeUpdate() > 0) { //remove from waiting_confirmation_kills...
					if(increaseKillerPoints.executeUpdate() > 0) { //update inscriptions set points...
						updateUserMission.setString(1, session);
						updateUserMission.setString(2, session);
						updateUserMission.setString(3, killer);
						updateUserMission.setString(4, session);
						updateUserMission.setString(5, killer);
						if(updateUserMission.executeUpdate() > 0) {
							
							//TODO cambio missione per lutente ucciso?
							updateUserMission.setString(1, session);
							updateUserMission.setString(2, session);
							updateUserMission.setString(3, killed);
							updateUserMission.setString(4, session);
							updateUserMission.setString(5, killed);
							if(updateUserMission.executeUpdate() > 0) {
								st.execute("commit");
								st.execute("set autocommit = true");
								decreaseKilledPoints.executeUpdate();//not important if not succed
								return new JSONObject().accumulate("status_code", 200 );
							}else
								st.execute("rollback");
						}else
							st.execute("rollback");
					}
					else
						st.execute("rollback");
				}else
					st.execute("rollback");
			}else
				st.execute("rollback");
			return new JSONObject().accumulate("status_code", 401 );
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getPendingKill(String username, String session) {
		try {
			ResultSet rs_getPendingKill;
			getPendingKill.setString(1, username);
			getPendingKill.setString(2, session);
			rs_getPendingKill = getPendingKill.executeQuery();

			JSONObject kills = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			while(rs_getPendingKill.next()) {
				JSONObject kill = new JSONObject();

				String killer = rs_getPendingKill.getString("killer");
				String killer_image = rs_getPendingKill.getString("killer_image");
				String place = rs_getPendingKill.getString("place");
				String place_image = rs_getPendingKill.getString("place_image");
				String object = rs_getPendingKill.getString("object");
				String object_image = rs_getPendingKill.getString("object_image");
				String time = rs_getPendingKill.getString("time");

				kill.accumulate("status_code", 200);
				kill.accumulate("session", session);
				kill.accumulate("killed", username);
				kill.accumulate("killer", killer);
				kill.accumulate("killer_image", killer_image);
				kill.accumulate("place", place);
				kill.accumulate("place_image", place_image);
				kill.accumulate("object", object);
				kill.accumulate("object_image", object_image);
				kill.accumulate("time", time);

				jsonArray.put(kill);
			}
			kills.put("kill_list", jsonArray);
			return kills.accumulate("status_code", 200);
		} catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public void killRequest(String killer, String killed, String session, String place, String object, String confirmationCode) {
		try {

			insertKillRequest.setString(1, killer);
			insertKillRequest.setString(2, killed);
			insertKillRequest.setString(3, session);
			insertKillRequest.setString(4, place);
			insertKillRequest.setString(5, object);
			insertKillRequest.setString(6, confirmationCode);

			insertKillRequest.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();

		}
	}

	public String getUserEmail(String username) {
		try {
			getUserEmail.setString(1, username);
			ResultSet rs = getUserEmail.executeQuery();
			if(rs.next()) {
				return rs.getString("email");
			}else {
				return null;
			}
		}catch (SQLException e) {
			return null;
		}
	}

	public JSONObject getKillList(String session) {
		try {
			getKillList.setString(1, session);
			ResultSet rs = getKillList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			jsonObject.accumulate("session", session);
			JSONArray killList = new JSONArray();

			while(rs.next()) {
				JSONObject kill = new JSONObject();
				kill.accumulate("killer", rs.getString("killer"));
				kill.accumulate("killed", rs.getString("killed"));
				kill.accumulate("place", rs.getString("place"));
				kill.accumulate("object", rs.getString("object"));
				kill.accumulate("time", rs.getString("time"));
				killList.put(kill);
			}
			jsonObject.put("kill_list", killList);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getHighlights(String session) {
		try {
			getHighlights.setString(1, session);
			ResultSet rs = getHighlights.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			jsonObject.accumulate("session", session);
			JSONArray highlights = new JSONArray();

			while(rs.next()) {
				JSONObject userData = new JSONObject();
				userData.accumulate("username", rs.getString("username"));
				userData.accumulate("points", Integer.toString((rs.getInt("points"))));
				highlights.put(userData);
			}
			jsonObject.put("highlights", highlights);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getObjectList() {
		try {

			ResultSet rs = getObjectList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray objects = new JSONArray();

			while(rs.next()) {
				JSONObject object = new JSONObject();
				object.accumulate("object_name", rs.getString("object_name"));
				object.accumulate("object_description", rs.getString("description"));
				object.accumulate("object_multiplicator", rs.getString("multiplicator"));
				object.accumulate("object_image", rs.getString("image"));
				objects.put(object);
			}
			jsonObject.put("objects", objects);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject addObject(String objectName, String objectImagePath, float multiplicator, String description) {
		try {
			addObject.setString(1, objectName);
			addObject.setString(2, objectImagePath);
			addObject.setFloat(3, multiplicator);
			addObject.setString(4, description);
			if(addObject.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getSessionList() {
		try {

			ResultSet rs = getSessions.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray objects = new JSONArray();

			while(rs.next()) {
				JSONObject object = new JSONObject();
				object.accumulate("session_name", rs.getString("session_name"));
				object.accumulate("description", rs.getString("description"));
				object.accumulate("start", rs.getString("start"));
				object.accumulate("end", rs.getString("end"));
				objects.put(object);
			}
			jsonObject.put("sessions", objects);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}

	}

	public JSONObject createSession(String session_name, String description, String starting_date, String ending_date) {
		try {
			createSession.setString(1, session_name);
			createSession.setString(2, description);
			createSession.setString(3, starting_date);
			createSession.setString(4, ending_date);
			if(createSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject deleteSession(String session_name) {
		try {
			deleteKills.setString(1, session_name);
			deleteWaitingConfirmationKills.setString(1, session_name);
			deleteRejectedKills.setString(1, session_name);
			deleteSession.setString(1, session_name);
			//st.execute("start transaction");
			
			deleteKills.executeUpdate();
			deleteWaitingConfirmationKills.executeUpdate();
			deleteRejectedKills.executeUpdate();
			if(deleteSession.executeUpdate() > 0) {
				//st.execute("commit");
				//st.execute("set autocommit = true");
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				//st.execute("rollback");
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getUsersList() {
		try {
			ResultSet rs = getAllUsers.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray users = new JSONArray();

			while(rs.next()) {
				JSONObject user = new JSONObject();
				user.accumulate("username", rs.getString("username"));
				user.accumulate("profile_image", rs.getString("profile_image"));
				users.put(user);
			}
			jsonObject.put("users", users);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject deleteObject(String object_name) {
		try {
			deleteObject.setString(1, object_name);
			if(deleteObject.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getPlaceList() {
		try {
			ResultSet rs = getPlaceList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray places = new JSONArray();

			while(rs.next()) {
				JSONObject object = new JSONObject();
				object.accumulate("place_name", rs.getString("place_name"));
				object.accumulate("place_image", rs.getString("image"));
				object.accumulate("place_multiplicator", rs.getString("multiplicator"));
				object.accumulate("place_description", rs.getString("description"));
				places.put(object);
			}
			jsonObject.put("places", places);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject addPlace(String placeName, String placeImagePath, float multiplicator, String description) {
		try {
			addPlace.setString(1, placeName);
			addPlace.setString(2, placeImagePath);
			addPlace.setFloat(3, multiplicator);
			addPlace.setString(4, description);
			if(addPlace.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject deletePlace(String place_name) {
		try {
			deletePlace.setString(1, place_name);
			if(deletePlace.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getSessionObjectList(String string) {
		try {
			getSessionObjectList.setString(1, string);
			ResultSet rs = getSessionObjectList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray objects = new JSONArray();

			while(rs.next()) {
				JSONObject object = new JSONObject();
				object.accumulate("object_name", rs.getString("object_name"));
				object.accumulate("object_image", rs.getString("image"));
				object.accumulate("object_multiplicator", rs.getString("multiplicator"));
				object.accumulate("object_description", rs.getString("description"));
				objects.put(object);
			}
			jsonObject.put("objects", objects);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getSessionPlaceList(String string) {
		try {
			getSessionPlaceList.setString(1, string);
			ResultSet rs = getSessionPlaceList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray places = new JSONArray();

			while(rs.next()) {
				JSONObject place = new JSONObject();
				place.accumulate("place_name", rs.getString("place_name"));
				place.accumulate("place_image", rs.getString("image"));
				place.accumulate("place_multiplicator", rs.getString("multiplicator"));
				place.accumulate("place_description", rs.getString("description"));
				places.put(place);
			}
			jsonObject.put("places", places);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject addObjectToSession(String object_name, String session_name) {
		try {
			addObjectToSession.setString(1, object_name);
			addObjectToSession.setString(2, session_name);
			if(addObjectToSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject addPlaceToSession(String place_name, String session_name) {
		try {
			addPlaceToSession.setString(1, place_name);
			addPlaceToSession.setString(2, session_name);
			if(addPlaceToSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject deleteObjectFromSession(String session_name, String object_name) {
		try {
			deleteObjectFromSession.setString(1, session_name);
			deleteObjectFromSession.setString(2, object_name);
			if(deleteObjectFromSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject deletePlaceFromSession(String session_name, String object_name) {
		try {
			deletePlaceFromSession.setString(1, session_name);
			deletePlaceFromSession.setString(2, object_name);
			if(deletePlaceFromSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject addUserToSession(String username, int points, String session_name) {
		try {
			addUserToSession.setString(1, username);
			addUserToSession.setString(2, session_name);
			addUserToSession.setInt(3, points);
			if(addUserToSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject removeUserFromSession(String username, String session_name) {
		try {
			removeUserFromSession.setString(1, username);
			removeUserFromSession.setString(2, session_name);

			if(removeUserFromSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getSessionUsersList(String session_name) {
		try {
			getSessionUsersList.setString(1, session_name);
			ResultSet rs = getSessionUsersList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			JSONArray users = new JSONArray();

			while(rs.next()) {
				JSONObject user = new JSONObject();
				user.accumulate("username", rs.getString("username"));
				user.accumulate("profile_image", rs.getString("profile_image"));
				user.accumulate("points", rs.getInt("points"));
				user.accumulate("actual_object_target", rs.getString("actual_object_target"));
				user.accumulate("actual_user_target", rs.getString("actual_user_target"));
				user.accumulate("actual_place_target", rs.getString("actual_place_target"));
				users.put(user);
			}
			jsonObject.put("users", users);
			return jsonObject;
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject deleteUser(String username) {
		try {
			deleteUser.setString(1, username);
			if(deleteUser.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject startSession(String session_name) {
		try {
			startSession.setString(1, session_name);
			startSession.setString(2, session_name);
			startSession.setString(3, session_name);
			startSession.setString(4, session_name);
			if(startSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject forceStartSession(String session_name) {
		try {
			forceStartSession.setString(1, session_name);
			forceStartSession.setString(2, session_name);
			forceStartSession.setString(3, session_name);
			forceStartSession.setString(4, session_name);
			if(forceStartSession.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject addPendingRegistrationUser(String username, String profile_image,
			String password_hash, String email, String confirmation_code) {
		try {
			//username, profile_image, email, password_hash, username
			addPendingRegistration.setString(1, username);
			addPendingRegistration.setString(2, profile_image);
			addPendingRegistration.setString(3, email);
			addPendingRegistration.setString(4, password_hash);
			addPendingRegistration.setString(5, confirmation_code);
			addPendingRegistration.setString(6, username);

			if(addPendingRegistration.executeUpdate() > 0) {
				return new JSONObject().accumulate("status_code", 200 );
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject completeRegistration(String codice) {
		try {
			st.execute("start transaction");
			completeRegistration.setString(1, codice);
			removeFromPendingRegistrations.setString(1, codice);
			if(completeRegistration.executeUpdate() > 0) {
				if(removeFromPendingRegistrations.executeUpdate() > 0) {
					st.execute("commit");
					st.execute("set autocommit = true");
					return new JSONObject().accumulate("status_code", 200 );
				}else {
					st.execute("rollback");
				}
			}else {
				st.execute("rollback");
			}
			return new JSONObject().accumulate("status_code", 405 );
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject confirmKillWithCode(String confirmation_code) {
		try {
			getKillerKilledSessionFromCode.setString(1, confirmation_code);
			ResultSet rs = getKillerKilledSessionFromCode.executeQuery();
			if(rs.next()) {
				String killer = rs.getString("killer");
				String killed = rs.getString("killed");
				String session = rs.getString("session");
				return confirmBeingKilled(killer, killed, session);
			}else {
				return new JSONObject().accumulate("status_code", 405 );
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}

	}

	public JSONObject denyBeingKilled(String killer, String killed, String session) {
		try {

			removePendingKill.setString(1, killer);
			removePendingKill.setString(2, killed);
			removePendingKill.setString(3, session);
			//insert into deniedKills select killer,killed,session,place,object,time from waiting_confirmation_kills where killer="io" and killed="io" and session="session";
			denyBeingKilled.setString(1, killer);
			denyBeingKilled.setString(2, killed);
			denyBeingKilled.setString(3, session);

			updateUserMission.setString(1, session);
			updateUserMission.setString(2, session);
			updateUserMission.setString(3, killer);
			updateUserMission.setString(4, session);
			updateUserMission.setString(5, killer);

			st.execute("start transaction");
			if(denyBeingKilled.executeUpdate() > 0) { //remove from waiting_confirmation_kills...
				if(updateUserMission.executeUpdate() > 0) {
					if(removePendingKill.executeUpdate() > 0) {
						st.execute("commit");
						st.execute("set autocommit = true");
						return new JSONObject().accumulate("status_code", 200 );
					}else {
						st.execute("rollback");
					}
				}
				else {
					st.execute("rollback");
				}
			}else {
				st.execute("rollback");
			}
			return new JSONObject().accumulate("status_code", 401 );
		}catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}

	public JSONObject getReport(String session) {
		try {
			getReport.setString(1, session);
			getReport.setString(2, session);
			getReport.setString(3, session);
			
			ResultSet rs = getReport.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			jsonObject.accumulate("session", session);
			JSONArray report = new JSONArray();
			
			while(rs.next()) {
				JSONObject data = new JSONObject();
				data.accumulate("type", rs.getString("type"));
				data.accumulate("killer", rs.getString("killer"));
				data.accumulate("killed", rs.getString("killed"));
				data.accumulate("place", rs.getString("place"));
				data.accumulate("object", rs.getString("object"));
				data.accumulate("time", rs.getString("time"));
				report.put(data);
			}
			jsonObject.put("report", report);
			return jsonObject;
			/*
			getKillList.setString(1, session);
			ResultSet rs = getKillList.executeQuery();
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("status_code", 200);
			jsonObject.accumulate("session", session);
			JSONArray reportList = new JSONArray();

			while(rs.next()) {
				JSONObject kill = new JSONObject();
				kill.accumulate("killer", rs.getString("killer"));
				kill.accumulate("killed", rs.getString("killed"));
				kill.accumulate("place", rs.getString("place"));
				kill.accumulate("object", rs.getString("object"));
				kill.accumulate("time", rs.getString("time"));
				killList.put(kill);
			}
			jsonObject.put("kill_list", killList);
			*/
			//return new JSONObject().accumulate("status_code", 500 );
		}catch (SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );	
		}
	}

	public JSONObject getUserMission(String username, String session) {
		try {
			ResultSet rs_session;
			getUserMission.setString(1, username);
			getUserMission.setString(2, session);
			rs_session = getUserMission.executeQuery();

			JSONObject jsonObjectSession = new JSONObject();
			
			if(rs_session.next()) {
				jsonObjectSession.accumulate("actual_object_target", rs_session.getString("actual_object_target"));
				jsonObjectSession.accumulate("object_image", rs_session.getString("object_image"));		
				jsonObjectSession.accumulate("object_description", rs_session.getString("object_description"));
				jsonObjectSession.accumulate("object_multiplicator", rs_session.getFloat("object_multiplicator"));

				jsonObjectSession.accumulate("actual_place_target", rs_session.getString("actual_place_target"));
				jsonObjectSession.accumulate("place_image", rs_session.getString("place_image"));
				jsonObjectSession.accumulate("place_description", rs_session.getString("place_description"));
				jsonObjectSession.accumulate("place_multiplicator", rs_session.getFloat("place_multiplicator"));

				jsonObjectSession.accumulate("actual_user_target", rs_session.getString("actual_user_target"));
				jsonObjectSession.accumulate("user_target_image", rs_session.getString("user_target_image"));

			}
			return jsonObjectSession.accumulate("status_code", 200);
		} catch(SQLException e) {
			e.printStackTrace();
			return new JSONObject().accumulate("status_code", 500 );
		}
	}
}


