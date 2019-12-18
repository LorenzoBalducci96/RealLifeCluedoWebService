package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.result.LocalDateTimeValueFactory;

import campusMurder.Encripter;

public class HashUtility {

	private static HashUtility instance;



	//private constructor to avoid client applications to use constructor
	private HashUtility(){
	}

	public static HashUtility getInstance(){
		if (instance == null)
			instance = new HashUtility();
		return instance;
	}

	public String encodeToSha512(String input) {
		{ 
			try { 
				// getInstance() method is called with algorithm SHA-512 
				MessageDigest md = MessageDigest.getInstance("SHA-512"); 

				// digest() method is called 
				// to calculate message digest of the input string 
				// returned as array of byte 
				byte[] messageDigest = md.digest(input.getBytes()); 

				// Convert byte array into signum representation 
				BigInteger no = new BigInteger(1, messageDigest); 

				// Convert message digest into hex value 
				String hashtext = no.toString(16); 

				// Add preceding 0s to make it 32 bit 
				while (hashtext.length() < 32) { 
					hashtext = "0" + hashtext; 
				} 

				// return the HashText 
				return hashtext; 
			} 

			// For specifying wrong message digest algorithms 
			catch (NoSuchAlgorithmException e) { 
				throw new RuntimeException(e); 
			} 
		}
	}
}


