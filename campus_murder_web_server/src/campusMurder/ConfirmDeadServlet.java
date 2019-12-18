package campusMurder;

import java.io.*;
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


public class ConfirmDeadServlet extends HttpServlet {

	public void init( ){

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		System.out.println("received post confirmation dead");
		if((int) request.getSession().getAttribute("status_code") != 200) {
			System.out.println("CRITICAL: UNLOGGED CONFIRM DEAD REQUEST");
		}else {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str;
			while( (str = br.readLine()) != null ){
				sb.append(str);
			}    
			JSONObject jobj = new JSONObject(sb.toString());

			String killer = jobj.getString("killer");
			String killed = (String) request.getSession().getAttribute("username");
			String session = jobj.getString("session");

			JSONObject jsonObject = DBConnect.getInstance().confirmBeingKilled(killer, killed, session);

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