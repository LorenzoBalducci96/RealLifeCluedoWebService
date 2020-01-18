package utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MailSender{
	private static MailSender instance;
	private String User = "";
	private String Pwd = "";
	private String registrationMailAddress = "";
	private String confirmKillAddress = "";
	
	
	
	private MailSender() {
		Context ctx;
		try {
			ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
		    User = (String) env.lookup("email");
		    Pwd = (String) env.lookup("password");
		    registrationMailAddress = (String) env.lookup("server-address");
		    confirmKillAddress = (String) env.lookup("confirm-kill-address");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	    
    
	}
	
	public static MailSender getInstance() {
		if (instance == null)
			instance = new MailSender();
		return instance;
	}
	
	public boolean registrationMail(String emailNewUser, String confirmationCode) {
		  try {
			  //String User = "delittiInAlma@hotmail.com";
			  //String Pwd = "delittiDaIncubo333";
			  
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
			    		+ "link:  " + registrationMailAddress + confirmationCode);				
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
	
	public boolean killEmail(String emailKilled, String confirmationCode, String killer, String place, String object) {
		  try {
			  
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
			    msg.setSubject(killer + " ha dichiarato di averti ucciso");
			    
			    msg.setText(killer + " ha dichiarato di averti ucciso\n"
			    		+ "con " + object + "\n"
			    		+ "in " + place + "\n"
			    		+ "devi confermare di essere stato ucciso accedendo con il tuo account o direttamente cliccando sul seguente link:\n"
			    		+ "link di conferma:  " + confirmKillAddress + confirmationCode + " \n"
			    		+ "se non sei stato veramente ucciso in questa maniera il killer potrebbe aver cliccato per sbaglio sulla dichiarazione di uccisione\n"
			    		+ "in tal caso puoi rinnegare l'uccisione accedendo al sito web");
			    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailKilled));
			    Transport transport = session.getTransport("smtp");
			    transport.connect("smtp.live.com", 587, User, Pwd);
			    
			    transport.sendMessage(msg, msg.getAllRecipients());    
			    
			    System.out.println("Mail sent successfully at " + emailKilled);
			    transport.close();
			    return false;
		  }catch(Exception e ) {
			  return false;
		  }
	   }

}
