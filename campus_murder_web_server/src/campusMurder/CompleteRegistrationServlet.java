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


public class CompleteRegistrationServlet extends HttpServlet {

   public void init( ){
	   
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
	   StringBuilder sb = new StringBuilder();
		InputStream inputStream = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream , StandardCharsets.UTF_8));

	   String str;
	   while( (str = br.readLine()) != null ){
	       sb.append(str);
	   }    
	   JSONObject jobj = new JSONObject(sb.toString());
	   
	   String codice = jobj.getString("register_code");
	   
	   JSONObject completeRegistration = DBConnect.getInstance().completeRegistration(codice);
	   
	   BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
       writer.write(completeRegistration.toString());
       writer.flush();
       writer.close();
   }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {
    	  String register_code = request.getParameter("register_code");  
			DBConnect.getInstance().completeRegistration(register_code);
			response.sendRedirect("confirmed_registration.html");

      }
      
      
}