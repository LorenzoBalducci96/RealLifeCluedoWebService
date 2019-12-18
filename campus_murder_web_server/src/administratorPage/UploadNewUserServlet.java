package administratorPage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.mysql.cj.xdevapi.JsonArray;

import utils.DBConnect;
import utils.HashUtility;


public class UploadNewUserServlet extends HttpServlet {

	private boolean isMultipart;
	
	private int maxFileSize = 6400000 * 1024;
	private int maxMemSize = 128000 * 1024;
	private String profileBasePath; //will be C:\Users\Jack\other...
	private static String internetServerPath = "/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES";
	private static String servletBaseProfilePath = "/SERVER_DATA/PROFILE_IMAGES";
	DiskFileItemFactory factory = null;
	ServletFileUpload upload = null;

	public void init() {
		factory = new DiskFileItemFactory();
		// max dimension stored in memory instead in repository
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		// factory.setRepository(new File("c:\temp"));
		factory.setRepository(new File(getServletConfig().getInitParameter("temp-folder")));
		upload = new ServletFileUpload(factory);
		// max size upload
		upload.setSizeMax(maxFileSize);

		// Get the file location where it would be stored.
		profileBasePath = getServletContext().getRealPath(servletBaseProfilePath);
		
		System.out.println("servlet configured for receiving users on " + profileBasePath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		if (ServletFileUpload.isMultipartContent(request)) {// e una richiesta di upload
			try {
				
				List<FileItem> fileItems = upload.parseRequest(request);
				String username = "";
				String password = "";
				
				String iamgePath = "";
				
				for(FileItem fi : fileItems) {
					if (fi.isFormField()) {
						String fieldname = fi.getFieldName();
						String fieldvalue = fi.getString();
						if (fieldname.equals("username")) {
							username += fieldvalue;
						}
						if (fieldname.equals("password")) {
							password += fieldvalue;
						}
					}
				}
				String fileName = "";
				for(FileItem fi : fileItems) {
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						fileName = fi.getName();
						
						if (fieldName.equals("profile_image")) {
							File file = new File(profileBasePath + "/" + fileName);
							iamgePath = profileBasePath + "/" + fileName;
							if(!file.exists())
								file.createNewFile();
							fi.write(file);
						}
					}
				}
				String password_hash = HashUtility.getInstance().encodeToSha512(password);
				JSONObject jsonObject = DBConnect.getInstance()
						.registerNewUser(username, internetServerPath + "/" + fileName, password_hash);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
				writer.write(jsonObject.toString());
				writer.flush();
				writer.close();
				
					
			} catch (Exception ex) {
				System.out.println(ex);
				response.sendRedirect("errore.html");
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		System.out.println("received get");

		throw new ServletException("GET method used with " +
				getClass( ).getName( )+": POST method required.");
	}


}