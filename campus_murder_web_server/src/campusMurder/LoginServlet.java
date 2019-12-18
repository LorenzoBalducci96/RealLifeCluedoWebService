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


public class LoginServlet extends HttpServlet {

   public void init( ){
	   
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
	   System.out.println("received user login");
	   StringBuilder sb = new StringBuilder();
	   BufferedReader br = request.getReader();
	   String str;
	   while( (str = br.readLine()) != null ){
	       sb.append(str);
	   }    
	   JSONObject jobj = new JSONObject(sb.toString());
	   
	   String username = jobj.getString("username");
	   String password = jobj.getString("password");
	   System.out.println(username);
	   System.out.println(password);
	   
	   JSONObject userData = DBConnect.getInstance().autenticate(username, password);
	   
	   
	   HttpSession session=request.getSession();
	   session.setAttribute("status_code", userData.getInt("status_code"));
	   if(userData.getInt("status_code") == 200)
		   session.setAttribute("username", userData.getString("username"));
	   
	   BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
       writer.write(userData.toString());
       writer.flush();
       writer.close();
   }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {
    	  System.out.println("received get");

         throw new ServletException("GET method used with " +
            getClass( ).getName( )+": POST method required.");

      }
      
      
}