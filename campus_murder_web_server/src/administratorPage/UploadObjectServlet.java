package administratorPage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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


public class UploadObjectServlet extends HttpServlet {

	private boolean isMultipart;
	
	private int maxFileSize = 6400000 * 1024;
	private int maxMemSize = 128000 * 1024;
	private String objectBasePath; //will be C:\Users\Jack\other...
	//private static String internetServerPath = "/SERVER_DATA/OBJECTS_IMAGES";
	
	
	private static String internetServerPath = "/SERVER_DATA/OBJECTS_IMAGES";
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
		objectBasePath = getServletConfig().getInitParameter("object-folder");
		/*
		objectBasePath = getServletContext().getRealPath("/");
		objectBasePath = objectBasePath.substring(0, objectBasePath.lastIndexOf(File.separator));
		//objectBasePath = getServletContext().getRealPath("/").concat(".." + servletBaseObjectsPath);
		objectBasePath = objectBasePath.substring(0, objectBasePath.lastIndexOf(File.separator)) 
				+ File.separator + "SERVER_DATA" + File.separator + "OBJECTS_IMAGES";
		*/
		internetServerPath = getServletConfig().getInitParameter("internet-object-path");
		
		
		System.out.println("servlet configured for receiving objects on " + objectBasePath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		if (ServletFileUpload.isMultipartContent(request)) {// e una richiesta di upload
			try {
				
				List<FileItem> fileItems = upload.parseRequest(request);
				String objectName = "";
				String description = "";
				float multiplicator = 1;
				String objectPath = "";
				
				for(FileItem fi : fileItems) {
					if (fi.isFormField()) {
						String fieldname = fi.getFieldName();
						String fieldvalue = fi.getString();
						if (fieldname.equals("object_name")) {
							objectName += fieldvalue;
						}
						if (fieldname.equals("description")) {
							description += fieldvalue;
						}
						if (fieldname.equals("multiplicator")) {
							multiplicator = Float.parseFloat(fieldvalue);
						}
					}
				}
				String fileName = "";
				for(FileItem fi : fileItems) {
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						fileName = objectName + fi.getName().substring(fi.getName().lastIndexOf('.'));
						
						if (fieldName.equals("object_image")) {
							File file = new File(objectBasePath + fileName);
							if(!file.exists()) {
								file.createNewFile();
								fi.write(file);
							}else {
								file.delete();
								file.createNewFile();
								fi.write(file);
							}
						}
					}
				}
				JSONObject jsonObject = DBConnect.getInstance()
						.addObject(objectName, internetServerPath + fileName, multiplicator, description);
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