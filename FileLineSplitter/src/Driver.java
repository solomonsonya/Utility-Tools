/**
 * @author Solomon Sonya @Carpenter1010
 */

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.swing.*;

import java.io.*;


public class Driver 
{
	public static final String myClassName = "Driver";
	public static final String NAME = "File Line-Splitter";
	public static final String VERSION = "1.01";
	
	
	/**
	 * This method queries the user via JChooser to select a file
	 * 
	 * Examples: INPUT  FILE TO LOAD --> querySelectFile(false, "Please specify data set to import", JFileChooser.FILES_ONLY, false, false);
	 * Examples: OUTPUT FILE TO SAVE --> querySelectFile(false, "Please specify outfile location for " + x, JFileChooser.DIRECTORIES_ONLY, false, false)
	 */
	public static  File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
	{
		
		/**
		 * Drivers_Thread.fleCarrier_NetworkCommand = Drivers.querySelectFile(true, "Please Select the Carrier Image to hold the Steganographic command(s) and content", JFileChooser.FILES_ONLY, false, true);
			
			if(Drivers_Thread.fleCarrier_NetworkCommand == null)
			{
				this.jtfCarrierImage_Settings.setText("No Carrier Destination File Selected");
				this.jtfCarrierImage_Settings.setToolTipText("No Carrier Destination File Selected");
			}
			
			else//a good file was selected
			{
				this.jtfCarrierImage_Settings.setText(Drivers_Thread.fleCarrier_NetworkCommand.getCanonicalPath());
				jtfCarrierImage_Settings.setToolTipText(Drivers_Thread.fleCarrier_NetworkCommand.getCanonicalPath());
			}
		 */
		
		try
		{
			JFileChooser jfc = new JFileChooser(new File("."));
			jfc.setFileSelectionMode(fileChooserSelectionMode);
			jfc.setDialogTitle(dialogueTitle);
			//jfc.setMultiSelectionEnabled(enableMultipleFileSelection);
			
			if(thisLoadsCSV)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	String strFleName = fle.getName().toLowerCase();
		                 
		                return strFleName.endsWith(".csv");
		              }
		   
		              public String getDescription() 
		              {
		                return "Comma Separated Values";
		              }
		              
		         });
				
			}
			
			/***************************************
			 * Filter for only Specified Formats
			 ***************************************/
			else if(useFileFilter)
			{
				jfc.setFileFilter(new javax.swing.filechooser.FileFilter() 
				{
		            public boolean accept(File fle) 
		            {
		            	String extension = "";
		            	
		                //accept directories
		            	if(fle.isDirectory())
		                	return true;
		            	
		            	if(fle == null)
		            		return false;
		            	
		            	if(fle != null && fle.exists() && getFileExtension(fle, false)!= null)
		            		extension = (getFileExtension(fle, false)).replace(".", "");//remove the "." if present
		            	
		            	/*if(lstAcceptableFileExtensionsForStego.contains(extension.toLowerCase()))
		            		return true;*/
		            	
		            	//else 
		            		return false;
		              }
		   
		              public String getDescription() 
		              {
		                return "Specific Formats";
		              }
		              
		         });
			}
			
			
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
				if(openDialog || (!openDialog && !thisLoadsCSV))
					return jfc.getSelectedFile();
				
				else
					return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");
			}
			
			//else fall through and return null;
		}
		
		catch(Exception e)
		{
			eop("querySelectFile", "Drivers", e, false);
			
		}
		
		return null;
	}
	
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
	
	public static String jop_Query(String strMsg, String strTitle)
	{
		Object o = strMsg;
		
		return JOptionPane.showInputDialog(null, o, strTitle, JOptionPane.QUESTION_MESSAGE);
	}
	
	public static String getFileExtension(File fle, boolean removeDot_Preceeding_Extension)
	{
		try
		{
			if(fle != null)
			{
				if(removeDot_Preceeding_Extension)
					return (fle.toString().substring(fle.toString().lastIndexOf(".") + 1));
					
				//some files do not have extensions, in such cases, SNSCat may seem to be crashing. therefore check if the file contains a "." at the end, if not, return what we have
				if(!fle.toString().contains(".") || fle.toString().lastIndexOf(".") < 0 )
				{
					try
					{
						return (fle.toString().substring(fle.toString().lastIndexOf(System.getProperty("file.separator"))));
					}
					catch(Exception e)
					{
						return " ";
					}
				}
				
				return (fle.toString().substring(fle.toString().lastIndexOf(".")));
			}
			
		}
		catch(NullPointerException npe)
		{
			sop("NullPointerException caught in getFileExtension_ByteArray mtd in Drivers.  This seems to be a sporadic error, called when user first attempts to view the files in a directory. This does not affect funtionality of program.  Dismissing error...");
		}
		catch(Exception e)
		{
			eop( "getFileExtension", "Drivers",e, false);
			
		}
		
		return null;
	}
	
	public static boolean sop(String out)
	{
		try
		{
			System.out.println(out);
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sop", out, e, false);
		}
		
		return false;
	}
	
	public static boolean sp(String out)
	{
		try
		{
			System.out.print(out);
			return true;
		}
		catch(Exception e)
		{
			Driver.eop("sp", out, e, false);
		}
		
		return false;
	}
	
	public static boolean eop(String mtdName, String myClassName, Exception e, boolean printStackTrace)
	{
		try
		{
			System.out.println("Exception caught in mtd: " + mtdName + " in Class: " + myClassName + " Error Message: " + e.getLocalizedMessage());
			
			if(printStackTrace)
			{
				e.printStackTrace(System.out);
			}
			
			return true;
		}
		catch(Exception ee)
		{
			System.out.println("Exception handled in eop mtd in " + myClassName);
		}
		
		return false;
	}

}
