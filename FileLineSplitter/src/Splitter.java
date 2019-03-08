/**
 * @author Solomon Sonya @Carpenter1010
 */

import java.io.*;

public class Splitter extends Thread implements Runnable
{
	public static final String myClassName = "Splitter";
	File input_file = null;
	File output_directory = null;
	long number_lines = 0;
	
	public volatile boolean terminate_program_upon_completion = true;
	public volatile boolean remove_empty_lines = false;

	public Splitter(File inputFile, long numLines, File outputDirectory, boolean removeEmptyLines)
	{
		try
		{
			input_file = inputFile;
			output_directory = outputDirectory;
			number_lines = numLines;
			this.remove_empty_lines = removeEmptyLines;
			
			//analyze
			if(output_directory == null || !output_directory.exists() || !output_directory.isDirectory())
			{				
				output_directory = input_file.getParentFile();
			}
			
			this.start();
		}
		catch(Exception e)
		{
			Driver.eop("Constructor - 1", myClassName, e, false);
		}
		
	}
	
	public void run()
	{
		try
		{
			//Driver.sop(myClassName + " worker thread started!!!");
			this.output_directory = this.split_file(input_file, number_lines, output_directory);
			
			if(terminate_program_upon_completion)
			{
				Driver.sop("All tasks completed. If successful, split files were written to " + output_directory.getCanonicalPath());
				Driver.sop("Program terminated");
				System.exit(0);
			}
		}
		catch(Exception e)
		{
			Driver.eop("run", myClassName, e, false);
		}
	}
	
	public File split_file(File in, long num_lines, File out)
	{
		try
		{
			if(in == null || !in.exists() || !in.isFile())
			{
				Driver.sop("ERROR! INVALID INPUT FILE SPECIFIED. PROGRAM TERMINATED");
				System.exit(0);
			}
			
			if(num_lines < 1)
			{
				num_lines = 1000;
			}
			
			String fileName_without_extension = in.getCanonicalPath().substring(0, in.getCanonicalPath().lastIndexOf("."));
			fileName_without_extension = fileName_without_extension.substring(fileName_without_extension.lastIndexOf(File.separator)+1);
			
			String extension_with_dot = in.getCanonicalPath().substring(in.getCanonicalPath().lastIndexOf("."));
			
			Driver.sop("Commencing split on " + in.getCanonicalPath());
			
			Driver.sop("file name without extension = " + fileName_without_extension);
			Driver.sop("Extension: " + extension_with_dot);
			
			//
			//Create new Output directory
			//
			try
			{
				String path = in.getParent();
				if(!path.endsWith(File.separator))
					path = path + File.separator;
				
				out = new File(path + "SPLIT_" + fileName_without_extension);
				
				if(out.exists() && out.isDirectory())
				{
					out = new File(path + "SPLIT_" + System.currentTimeMillis() + "_" + fileName_without_extension);
				}
				
				if(!out.exists() || !out.isDirectory())
					out.mkdirs();
			}
			catch(Exception e)
			{
				Driver.sop("Could not create parent directory for file: " + in.getCanonicalPath());
				out = null;
			}
			
			//
			//create output directory
			//
			if(out == null || !out.exists() || !out.isDirectory())
			{
				out = new File("./");
			}
			
			//
			//split
			//
			String out_path = out.getCanonicalPath();
			
			if(!out_path.endsWith(File.separator))
				out_path = out_path + File.separator;
			
			long file_counter = 0;
			BufferedReader brin = new BufferedReader(new FileReader(in));
			String line = "";
			File outFile = null;
			PrintWriter pwOut = null;
			long index = 0;
			
			
			while((line = brin.readLine()) != null)
			{
				if(this.remove_empty_lines && line.trim().equals(""))
					continue;
				
				if(file_counter++ % num_lines == 0)
				{
					try
					{
						if(pwOut != null)
						{							
							pwOut.flush();
							pwOut.close();
							Driver.sop("Closing File: " + outFile.getCanonicalPath());
						}
					}catch(Exception e){}
					
					//create new file
					outFile = new File(out_path + fileName_without_extension + "_" + (index++) + extension_with_dot);
					pwOut = new PrintWriter(new FileWriter(outFile));
				}
				
				//write line
				pwOut.println(line);
				pwOut.flush();
				
				if(file_counter %100 == 0)
				{
					System.out.print(".");
				}
					
			}
			
			
			
			return out;
		}
		catch(Exception e)
		{
			Driver.eop("split_file", myClassName, e, false);
		}
		
		return null;
	}
}
