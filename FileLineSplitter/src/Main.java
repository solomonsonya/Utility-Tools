/**
 * @author Solomon Sonya @Carpenter1010
 */

import java.io.*;

import javax.swing.JFileChooser;


public class Main 
{
	public static final String myClassName = "Main";
	
	public static void main(String[] args) 
	{
		try
		{
			System.out.println("");
			System.out.println("  ///////////////////////////////////////////////////////////////////////////");
			System.out.println(" // " + "Welcome to " + Driver.NAME + " vrs " + Driver.VERSION + " by Solomon Sonya @Carpenter1010//");
			System.out.println("///////////////////////////////////////////////////////////////////////////\n");
			
			
			try	{	Driver.setLookAndFeel();}	catch(Exception e){}
			
			File fle = null;
			long lines = 1000;
			
			//
			//Determine Command
			//
			if(args == null || args.length < 1 || args[0].trim().toLowerCase().equalsIgnoreCase("-g") || args[0].trim().toLowerCase().equalsIgnoreCase("g"))
			{
				fle = Driver.querySelectFile(false, "Please specify data set to import", JFileChooser.FILES_ONLY, false, false);
				lines = Long.parseLong(Driver.jop_Query("Please enter the number of lines for each file", "Specify Num Lines"));
				
			}
			else
			{
				//Extract File
				fle = new File(args[0].trim());
				lines = Long.parseLong(args[1].trim());
				
				if(!fle.exists() || !fle.isFile())
					throw new Exception("File does not appear to be a valid file!!!");
			}
			
			
			
			Driver.sop("File: " + fle.getCanonicalPath() + " Num Lines: " + lines);
			
			//commens split thread
			Splitter splitter = new Splitter(fle, lines, fle.getParentFile(), false);
			
			String line = "";
			BufferedReader brIn = new BufferedReader(new InputStreamReader(System.in));
			while((line = brIn.readLine()) != null)
			{
				//wait until we close the program!
			}
			
		}
		catch(Exception e)
		{
			Driver.sop("Invalid Arguments!");
			printUsage();
		}
		
	}
	
	public static boolean printUsage()
	{
		try
		{
			Driver.sop("**************************************************************************************");
			Driver.sop("* ");
			Driver.sop("* " + Driver.NAME + " By Solomon Sonya @Carpenter1010");
			Driver.sop("* ");
			Driver.sop("* USAGE:");
			Driver.sop("* ======");
			Driver.sop("* ");
			Driver.sop("* file_splitter.exe <file path> <num lines per file>");
			Driver.sop("* ");
			Driver.sop("**************************************************************************************");
			
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("printUsage", myClassName, e, false);
		}
		
		return false;
	}

}
