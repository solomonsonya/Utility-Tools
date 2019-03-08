package Start;

import javax.swing.JFileChooser;

import java.io.*;
import java.util.*;

import Drivers.Driver;

public class Start extends Thread implements Runnable
{
	public static final String myClassName = "Start";
	public static Driver driver = new Driver();
	public static String [] args = null;
	
	public Start(String[] argv)
	{
		try
		{
			args = argv;
			this.start();
		}
		catch(Exception e)
		{
			driver.eop(myClassName, "Constructor - 1", e);
		}
	}
	
	public void run()
	{
		try
		{
			driver.welcome();
			
			File fle = null;
			if(args != null && args.length > 0)
			{
				fle = new File(args[0].trim());
				
				//sop("searching for-->" + fle.getCanonicalPath());
				
				if(!fle.exists())
					fle = null;
			}
			
			if(fle == null || !fle.exists())
				fle = driver.querySelectFile(true, "Please specify input file...", JFileChooser.FILES_ONLY);
				
			analyzeFile(fle);
			
			driver.sop("Process complete. Terminating now...");
			System.exit(0);
		}
		
		catch(Exception e)
		{
			driver.eop(myClassName, "run", e);
		}
		
	}
	
	public boolean analyzeFile(File fle)
	{
		try
		{
			if(fle == null || !fle.exists() || !fle.isFile() || fle.length() < 1)
			{
				driver.sop("INVALID FILE SPECIFIED! UNABLE TO CONTINUE...");
				return false;
			}
															
			//notify
			sop("Commencing analysis on input file-->" + fle.getCanonicalPath());
			
			//begin analysis
			HashMap<String, String> map = new HashMap<String, String>();
			
			String line = "", line_TRIMMED = "";
			int i = 0;
			boolean punted_early = false;
			boolean hash_trimmed_line = true;
			BufferedReader brIn = new BufferedReader(new FileReader(fle));
			
			while((line = brIn.readLine()) != null)
			{
				try
				{
					if(line.trim().equals(""))
						continue;
					
					line_TRIMMED = line.trim();
					
					//
					//ensure we do not run out of memory
					//
					if(Runtime.getRuntime().freeMemory() - 10000 <= 0)
					{
						driver.sop("ERROR! Running out of available memory. I am stopping here...");
						punted_early = true;
						break;
					}
					
					//assumption: hashmap does not handle collisions, and in fact will reject adding a line that already exists in the hashmap
					
					//
					//store the unique line
					//
					if(hash_trimmed_line)
						map.put(line_TRIMMED, line_TRIMMED);
					else
						map.put(line, line);
										
					//
					//incr
					//
					++i;
				}
				catch(Exception e)
				{
					continue;
				}
			}
			
			//done
			if(!punted_early)
			{
				sop("Done! Num lines read: [" + i + "]. Num unique elements found within input file: [" + map.size() + "]. Num Duplicate lines removed: [" + (i - map.size()) + "].");
			}
			else
			{
				sop("Done but with Errors! NOTE: I ran out of memory analyzing input file and thus could not finish full analysis. I stopped at line [" + i + "].");
				sop("The first [" + map.size() + "] line(s) will be what I could determine to be unique lines. After this, you will see a single blank line followed by the remainder of input file starting at line [" + i + "].");
			}
			
			writeOutput(fle, map, punted_early, brIn, i, line);
						
			try	{	brIn.close();}	catch(Exception e){}
			
			return true;
		}
		catch(Exception e)
		{
			driver.eop(myClassName, "analyzeFile", e);
		}
		
		return false;
	}
	
	public File writeOutput(File inputFile, HashMap<String, String> map, boolean print_remainder_of_buffered_reader, BufferedReader brIn, int line_num, String last_line_read)
	{
		try
		{
			if(map == null || map.size() < 1)
			{
				driver.sop("No data populated in unique lines map thus no unique file to write. Punting from here!");
				return null;
			}
			
			String name_and_extension = inputFile.getName();
			String name = name_and_extension.substring(0, name_and_extension.lastIndexOf("."));
			String extension_with_dot = name_and_extension.substring(name_and_extension.lastIndexOf("."));
						
			File fleOutput = driver.createFile(inputFile, name, extension_with_dot, true);
						
			//Create the file!
			PrintWriter pwOut = new PrintWriter(new FileWriter(fleOutput));
			
			//notify
			sop("Created file -->" + fleOutput.getCanonicalPath() + ". Writing contents now...");
			
			int num_lines_written = 0;
			
			//
			//Print map
			//
			LinkedList<String> list = new LinkedList<String>(map.values());
			
			for(int i = 0; i < list.size(); i++)
			{
				try
				{
					pwOut.println(list.get(i));
					++num_lines_written;
				}
				catch(Exception e)
				{
					driver.eop_loop(myClassName, "writeOutput", i);
					continue;
				}
			}
			
			//
			//print remainder of bufferedreader if necessary
			//
			if(print_remainder_of_buffered_reader)
			{				
				//notify
				sop("Partially done. Written [" + list.size() + "] line(s) to output file. Had to punt early, thus printing remainder of input file now...");
				
				//first print a blank line
				pwOut.println("");
				++num_lines_written;
				
				//print line in the buffer that was read but not written out
				pwOut.println(last_line_read);
				++num_lines_written;
				
				//print remaining of file
				try
				{
					String line = "";
					
					while((line = brIn.readLine()) != null)
					{
						pwOut.println(line);
						++num_lines_written;
					}							
				}
				catch(Exception e)
				{
					driver.sop("Could not read full buffer in writeOutput in " + myClassName);
				}
			}
			
			//
			//flush
			//
			pwOut.flush();
			
			//
			//close
			//
			try	{	pwOut.close();}	catch(Exception e){}			
			
			//notify
			driver.sop("COMPLETE! Num lines written: [" + num_lines_written + "] to output file-->" + fleOutput);
			
			//return
			return fleOutput;			
		}
		catch(Exception e)
		{
			driver.eop(myClassName, "writeOutput", e);
		}
		
		return null;
	}
	
	public void sop(String out){	try	{	driver.sop(out); 	}	catch(Exception e){}}
}

