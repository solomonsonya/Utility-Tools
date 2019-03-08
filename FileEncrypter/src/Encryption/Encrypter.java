/**
 * This class handles complete retrieval of all data to encrypt a file
 */

package Encryption;

import javax.swing.JFileChooser;
import Driver.*;
import java.io.*;
import java.security.interfaces.*;
import java.io.*;
import java.security.*;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



public class Encrypter extends Thread implements Runnable
{
	public static volatile JOP jop = new JOP();
	public static final String myClassName = "Encrypter";
	public static volatile Driver driver = new Driver();
	public static final boolean ready_to_start = false;
	
	public File fleToEncrypt = null;
	
	public String base64encoded_AES_Key = null;
	public String encyrption_key_HEAD_base64encoded = null;
	public String encyrption_key_TAIL_base64encoded = null;
	public String IV_base64encoded = null;
	public byte [] AES_KEY = null;
	public byte [] AES_IV = null;
	
		
	public String digital_signature = "-";
	public String digital_signature_public_key_used_to_sign_file = "-";
	
	
	
	long startTime = 0;
	
	/**
	 * the purpose of this constructor is to query user for file attributes to encrypt
	 */
	public Encrypter(String [] args)
	{
		try
		{
			if(args == null || args.length < 1)
			{
				//query user for file to encrypt!
				this.fleToEncrypt = driver.querySelectFile(true, "Please select a file", JFileChooser.FILES_ONLY, false, false);
				
				if(fleToEncrypt == null)
				{
					driver.sop("User rejected selecting a file");					
				}
				
				//
				//Ensure file is valid
				//
				if(!fleToEncrypt.exists() || !fleToEncrypt.isFile())
				{
					jop.jop("Invalid file selected!  Please choose another...");
				}
			}
			
			if(ready_to_start)
				this.start();
			else
			{
				driver.sop("\n\n* * * NOT READY TO RUN PROGRAM.  PROGRAM TERMINATED!");
			}
		}
		catch(Exception e)
		{
			driver.eop("Constructor - 1", myClassName, e, true);
		}
	}
	
	public void run()
	{
		try
		{
			
			
			
		}
		catch(Exception e)
		{
			driver.eop("run", myClassName, e, false);
		}
		
	}
	
	
}