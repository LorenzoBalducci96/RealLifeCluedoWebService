package administratorPage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.mysql.cj.xdevapi.JsonArray;

import utils.DBConnect;


public class CreateSessionServlet extends HttpServlet {

	public void init( ){

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		System.out.println("received create session request");
		if((int) request.getSession().getAttribute("status_code") != 250) {
			System.out.println("WARNING: UNLOGGED CREATE SESSION REQUEST");
		}else {

			StringBuilder sb = new StringBuilder();
			InputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream , StandardCharsets.UTF_8));
			String str;
			while( (str = br.readLine()) != null ){
				sb.append(str);
			}    
			JSONObject jobj = new JSONObject(sb.toString());
			String session_name = jobj.getString("session_name");
			String description = jobj.getString("description");
			String starting_date = jobj.getString("starting_date");
			String ending_date = jobj.getString("ending_date");

			JSONObject jsonObject = DBConnect.getInstance().createSession(session_name,
					description, starting_date, ending_date);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			writer.write(jsonObject.toString());
			writer.flush();
			writer.close();
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		System.out.println("received get");

		throw new ServletException("GET method used with " +
				getClass( ).getName( )+": POST method required.");

	}


}