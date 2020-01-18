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


public class GetPendingKillServlet extends HttpServlet {

   public void init( ){
	   
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
	   System.out.println("received get pending kill kill");
	   if((int) request.getSession().getAttribute("status_code") != 200) {
			System.out.println("WARNING: UNLOGGED GET PENDING KILL REQUEST");
		}else {
			StringBuilder sb = new StringBuilder();
			InputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream , StandardCharsets.UTF_8));

		   String str;
		   while( (str = br.readLine()) != null ){
		       sb.append(str);
		   }    
		   JSONObject jobj = new JSONObject(sb.toString());
		   
		   String username = (String) request.getSession().getAttribute("username");
		   String session = jobj.getString("session");
		   
		   JSONObject jsonObject = DBConnect.getInstance().getPendingKill(username, session);
		   
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