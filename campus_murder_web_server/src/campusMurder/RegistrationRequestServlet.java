package campusMurder;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import utils.MailSender;


public class RegistrationRequestServlet extends HttpServlet {

private boolean isMultipart;
	
	private int maxFileSize = 6400000 * 1024;
	private int maxMemSize = 128000 * 1024;
	private String profileBasePath; //will be C:\Users\Jack\other...
	private static String internetServerPath = "/campus_murder_web_server/SERVER_DATA/PROFILE_IMAGES";
	private static String servletBaseProfilePath = "/SERVER_DATA/PROFILE_IMAGES";
	private String serverAddress;
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
		serverAddress = getServletConfig().getInitParameter("server-address");
		
		System.out.println("servlet configured for receiving users on " + profileBasePath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		if (ServletFileUpload.isMultipartContent(request)) {// e una richiesta di upload
			try {
				
				List<FileItem> fileItems = upload.parseRequest(request);
				String username = "";
				String password = "";
				String email = "";
				
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
						if (fieldname.equals("email")) {
							email += fieldvalue;
						}
					}
				}
				String fileName = "";
				for(FileItem fi : fileItems) {
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						
						fileName = username + fi.getName().substring(fi.getName().lastIndexOf('.'));
						
						if (fieldName.equals("profile_image")) {
							File file = new File(profileBasePath + "/" + fileName);
							if(!file.exists()) {
								file.createNewFile();
								fi.write(file);
							}
						}
					}
				}
				String password_hash = HashUtility.getInstance().encodeToSha512(password);
				String confirmationCode = generateCode();
				JSONObject jsonObject = DBConnect.getInstance()
						.addPendingRegistrationUser(username, internetServerPath + "/" + fileName, password_hash, email, confirmationCode);
				if(jsonObject.getInt("status_code") == 200) {
					MailSender.getInstance().registrationMail(email, confirmationCode);
				}
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
	
	/*
	private boolean mail(String emailNewUser, String confirmationCode) {
		  try {
			  String User = "delittiInAlma@hotmail.com";
			  String Pwd = "delittiDaIncubo333";
			  
			  Properties props = null;
			    if (props == null) {
			        props = new Properties();
			        props.put("mail.smtp.auth", true);
			        props.put("mail.smtp.starttls.enable", true);
			        props.put("mail.smtp.host", "smtp.live.com");
			        props.put("mail.smtp.port", "587");
			        props.put("mail.smtp.user", User);
			        props.put("mail.smtp.pwd", Pwd);
			    }
			    Session session = Session.getInstance(props, null);
			    session.setDebug(true);
			    Message msg = new MimeMessage(session);
			    msg.setFrom(new InternetAddress(User));
			    msg.setSubject("completa la registrazione su delitti in alma");
			    
			    msg.setText("benvenuto nel gioco delitti in alma\n"
			    		+ "un gioco in cui astuzia, furbizia e intuito ti porteranno alla vittoria.\n"
			    		+ "per completare la registrazione clicca sul seguente link...\n"
			    		+ "link:  " + serverAddress + confirmationCode);				
			    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailNewUser));
			    Transport transport = session.getTransport("smtp");
			    transport.connect("smtp.live.com", 587, User, Pwd);
			    
			    transport.sendMessage(msg, msg.getAllRecipients());    
			    
			    System.out.println("Mail sent successfully at " + emailNewUser);
			    transport.close();
			    return false;
		  }catch(Exception e ) {
			  return false;
		  }
	   }
	   */

		
		private String generateCode() {
			int length = 7;
			String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			StringBuilder builder = new StringBuilder();
			while (length-- != 0) {
				int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
				builder.append(ALPHA_NUMERIC_STRING.charAt(character));
			}
			return builder.toString();
			
		}

      
}