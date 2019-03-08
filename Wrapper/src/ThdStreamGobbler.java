/**
 * The purpose of this class is to injest all data across a given output stream and provide the responses back to the parent
 * 
 * This is requried to allow us to read both stdout and stderror simultaneously
 * 
 * @author Solomon Sonya
 *
 */

import java.io.*;
import java.net.*;

public class ThdStreamGobbler extends Thread implements Runnable
{
	ThdServerSocket parent = null;
	public static final String myClassName = "ThdStreamGobbler";
	
	
	
	public volatile BufferedReader brIn = null;
	public volatile PrintWriter pwOut = null;
	public volatile Process process = null;
	
	public ThdStreamGobbler(ThdServerSocket par, Process proc, BufferedReader br, PrintWriter pw)
	{
		try
		{
			parent = par;
			process = proc;
			brIn = br;
			pwOut = pw;
			
			this.start();
		}
		catch(Exception e)
		{
			this.eop(myClassName, "Constructor - 1", e);
		}
	}
	
	public void run()
	{
		try
		{
			String line = "";
			while((line = brIn.readLine()) != null)
			{
				parent.broadcast(line, true);
			}
			
		}
		catch(Exception e)
		{
			this.eop(myClassName, "run", e);
		}
	}	
	

	public void sop(String out){System.out.println(out);	}
	public void eop(String myClassName, String mtdName, Exception e) {		sop("Exception caught in class " +  myClassName + " in mtd: " + mtdName + ". " + e.getLocalizedMessage());	}








}
