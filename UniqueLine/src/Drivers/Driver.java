/**
 * 
 * @author Solomon Sonya
 */
package Drivers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Driver 
{
	public static final String NAME = "UniqueLine";
	public static final String VERSION = "0.001";
	public static final String myClassName = "Driver";
	
	public static final String DRIVER_BUILD_VERSION = "2016-07-30";

	
	public Driver(){this.setLookAndFeel();}//null constructor
	
	public static final boolean isOutputEnabled = true;
	
	public static void setLookAndFeel()
	{
		try 
		{	
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");	
		} 
		catch (Exception e) 
		{
		   // handle exception
		}
	}
	
	public File makeDirectory(File outputDirectory, String name)
	{
		String path = "";
		
		try
		{
			File fleSaveDirectory = null;
			
			if(outputDirectory.getCanonicalPath().endsWith(File.separator))
			{
				path = outputDirectory.getCanonicalPath();
				fleSaveDirectory = new File(outputDirectory.getCanonicalPath() + name);
			}
			else
			{
				path = outputDirectory.getCanonicalPath() + File.separator; 
				fleSaveDirectory = new File(outputDirectory.getCanonicalPath() + File.separator + name);
			}
			
			fleSaveDirectory.mkdirs();
			
			return fleSaveDirectory;
		}
		catch(Exception e)
		{
			this.eop(myClassName, "makeDirectory", e);
		}
		
		try
		{			
			File out = new File("./" + name);
			out.mkdirs();
			this.sop("ORIGINAL LOCATION: " + path + " was not successful. New folder creation is " + out.getCanonicalPath());			
			return out;
		}catch(Exception e){}
		
		return null;
	}
	
	/**
	 * This method queries the user via JChooser to select a file
	 * 
	 * Examples: INPUT  FILE TO LOAD --> querySelectFile(false, "Please specify data set to import", JFileChooser.FILES_ONLY);
	 * Examples: OUTPUT FILE TO SAVE --> querySelectFile(true, "Please specify outfile location for " + x, JFileChooser.DIRECTORIES_ONLY)
	 */
	public  File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode)
	{
		
		
		
		try
		{
			JFileChooser jfc = new JFileChooser(new File("."));
			jfc.setFileSelectionMode(fileChooserSelectionMode);
			jfc.setDialogTitle(dialogueTitle);
			//jfc.setMultiSelectionEnabled(enableMultipleFileSelection);
			
			try
			{
				jfc.setCurrentDirectory(new File(".\\"));
			}catch(Exception e){}
			
			int selection = 0;
			
			if(openDialog)					
			{
				selection = jfc.showOpenDialog(null);
			}
			
			else
			{
				//selection = jfc.showDialog(null, "Save Now!"); <-- this code works too
				selection = jfc.showSaveDialog(null);
			}
					
			if(selection == JFileChooser.APPROVE_OPTION)//selected yes!
			{
				if(openDialog || (!openDialog))
					return jfc.getSelectedFile();				
			}
			
			//else fall through and return null;
		}
		
		catch(Exception e)
		{
			eop("querySelectFile", "Drivers", e);
			
		}
		
		return null;
	}
	
	
	
	/**
	 * OK to pass in a file and not directory. I will get the parent directory from here...
	 * @param fle
	 * @param name
	 * @return
	 */
	public File createFile(File fle, String name, String extension_with_dot, boolean ensure_unique_file_name)
	{
		try
		{
			if(fle == null)
			{
				this.sop("null file received in " + myClassName  + "!");
				return null;
			}
			
			File parentDirectory = null;
			
			if(fle.isDirectory())
				parentDirectory = fle;
			else
				parentDirectory = fle.getParentFile();
			
			String path = parentDirectory.getCanonicalPath();
			
			File fleToReturn = null;
			if(path.endsWith(File.separator))
				fleToReturn =  new File(path + name + "_UNIQUE" + extension_with_dot);			
			else//otw
				fleToReturn =  new File(path + File.separator + name + "_UNIQUE" + extension_with_dot);		
						
			//check if we're done...
			if(!ensure_unique_file_name)
				return fleToReturn;
			
			
			//
			//oth, create files until a unique one exists
			//
			int i = 1;
			while(fleToReturn.exists())
			{
				try
				{
					path = parentDirectory.getCanonicalPath();
					
					if(path.endsWith(File.separator))
						fleToReturn =  new File(path + name + "_UNIQUE" + "_" + i + extension_with_dot);			
					else//otw
						fleToReturn =  new File(path + File.separator + name + "_UNIQUE" + "_" + i  + extension_with_dot);	
					
					++i;										
				}
				catch(Exception e)
				{
					this.sop("PUNTING OUT OF createFile in " + myClassName);
					break;
				}
			}
			
			//unique file at this point
			return fleToReturn;
				
		}
		catch(Exception e)
		{
			this.eop(myClassName, "createFile", e);
		}
		
		return null;
	}
	
	public void sp(String out)
	{
		System.out.print(out);
		
	}
	
	public static boolean welcome()
	{
		try
		{
			
			System.out.println("//////////////////////////////////////////////////////////////////////");
			System.out.println("// WELCOME to " + NAME + " vrs " + VERSION + " by Solomon Sonya @Carpenter1010 //");
			System.out.println("////////////////////////////////////////////////////////////////////");
			
			return true;
		}
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	public void eop(String myClassName, String mtdName, Exception e, boolean printStackTrace)
	{
		if(printStackTrace)
		{
			sop("Exception caught in " + mtdName + " in class: " + myClassName + " message: " + e.getLocalizedMessage());
			e.printStackTrace(System.out);
		}
		else
			sop("Exception caught in " + mtdName + " in class: " + myClassName + " message: " + e.getLocalizedMessage());
	}
	
	public void eop(String myClassName, String mtdName, Exception e)
	{
		sop("Exception caught in " + mtdName + " in class: " + myClassName + " message: " + e.getLocalizedMessage());
	}
	
	public void eop_loop(String myClassName, String mtdName, int i)
	{
		sop("Check mtd: " + mtdName + " in class: " + myClassName + " at index: " + i);
	}
	
	public void sop(String out)
	{
		if(isOutputEnabled)
		{
			System.out.println(out); 
			System.out.flush();
		}	
		
		//transmit_to_consoles(out);
	}
	
	public static boolean jop_Error(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Unable to Continue", JOptionPane.ERROR_MESSAGE);
		
		//since we're displaying an error, we'll assume the default return type is false;
		return false;
	}
	
	public int jop_Confirm(String strText, String strTitle)
	{
		try
		{
			return JOptionPane.showConfirmDialog(null, strText, strTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		catch(Exception e)
		{
			//Drivers.eop("queryDialog", strMyClassName, e, e.getMessage(), true);
		}
		
		return -1;
	}
	
}
