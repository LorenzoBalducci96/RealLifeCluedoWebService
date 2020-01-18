package campusMurder;

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


public class ConfirmKillOnCodeServlet extends HttpServlet {

	public void init( ){

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		String confirmation_code = request.getParameter("confirmation_code");  
		DBConnect.getInstance().confirmKillWithCode(confirmation_code);
		response.getWriter().write("grazie di aver confermato l'uccisione ");

	}


}