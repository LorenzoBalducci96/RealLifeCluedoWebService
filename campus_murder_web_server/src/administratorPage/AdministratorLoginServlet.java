package administratorPage;

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


public class AdministratorLoginServlet extends HttpServlet {

   public void init( ){
	   
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
	   System.out.println("received post");
	   StringBuilder sb = new StringBuilder();
	   BufferedReader br = request.getReader();
	   String str;
	   while( (str = br.readLine()) != null ){
	       sb.append(str);
	   }    
	   JSONObject jobj = new JSONObject(sb.toString());
	   String password = jobj.getString("administrator_password");
	   System.out.println(password);
	   JSONObject jsonResponse = new JSONObject();
	   if(password.equals(getServletConfig().getInitParameter("administrator_password"))) {
		   HttpSession session = request.getSession();
		   session.setAttribute("status_code", 250);
		   jsonResponse.accumulate("status_code", 200);
	   }
	   else {
		   jsonResponse.accumulate("status_code", 401);
	   }
	   
	   BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
       writer.write(jsonResponse.toString());
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