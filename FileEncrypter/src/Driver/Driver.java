package Driver;


import java.io.*;
import javax.swing.*;

public class Driver 
{
	public static final String myClassName = "Driver";
	
	
	public volatile boolean outputEnabled = true;
	
	public Driver(){}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method queries the user via JChooser to select a file
	 */
	public File querySelectFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
	{
		
		/**
		 * Drivers_Thread.fleCarrier_NetworkCommand = querySelectFile(true, "Please Select the Carrier Image to hold the Steganographic command(s) and content", JFileChooser.FILES_ONLY, false, true);
			
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
		            	
		            	/*if(Driver.lstAcceptableFileExtensionsForStego.contains(extension.toLowerCase()))
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
			this.eop("querySelectFile", myClassName, e, false);
						
		}
		
		return null;
	}
	
	public String getFileName_From_FullPath(File fle)
	{
		try
		{
			if(fle == null || !fle.exists() || !fle.isFile())
			{
				sop("INVALID FILE SPECIFIED!!!");
				return " ";
			}
			
			try
			{
				return fle.getCanonicalPath().substring(fle.getCanonicalPath().lastIndexOf(File.separator)+1);
			}
			catch(Exception e)
			{
				return fle.getCanonicalPath();
			}
			
			
		}
		catch(Exception e)
		{
			this.eop("getFileName_From_FullPath", myClassName, e, false);
		}
		
		return " ";
		
	}
	
	public String getFileExtension(File fle, boolean removeDot_Preceeding_Extension)
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
			sop("NullPointerException caught in getFileExtension_ByteArray mtd in   This seems to be a sporadic error, called when user first attempts to view the files in a directory. This does not affect funtionality of program.  Dismissing error...");
		}
		catch(Exception e)
		{
			this.eop("getFileExtension", myClassName, e, false);
			
		}
		
		return null;
	}
	
	/**
	 * This method queries the user via JChooser to select a file
	 */
	public File [] querySelectMultipleFile(boolean openDialog, String dialogueTitle, int fileChooserSelectionMode, boolean thisLoadsCSV, boolean useFileFilter)
	{
		try
		{
			JFileChooser jfc = new JFileChooser(new File("."));
			jfc.setFileSelectionMode(fileChooserSelectionMode);
			jfc.setDialogTitle(dialogueTitle);
			jfc.setMultiSelectionEnabled(true);
			
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
		            		return true;
		            	*/
		            	
		            	
		            	//else 
		            		return false;
		              }
		   
		              public String getDescription() 
		              {
		                return "Multiple Formats";
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
				selection = jfc.showSaveDialog(null);
			}
					
			if(selection == JFileChooser.APPROVE_OPTION)//selected yes!
			{
				if(openDialog || (!openDialog && !thisLoadsCSV))
					return jfc.getSelectedFiles();//solo, come here!
				
				/*else
					return new File(jfc.getSelectedFile().getAbsolutePath() + ".csv");*/
			}
			
			//else fall through and return null;
		}
		
		catch(Exception e)
		{
			this.eop("querySelectFile", this.myClassName, e, false);
		}
		
		return null;
	}
	
	
	
	/**
	 * This method is called from querySelectFile.  You see, if the user is able to select multiple files, 
	 * querySelectFile was written to return only one file.  Therefore, this method, if the user is allowed to return multiple files will actually handle returning the file array based on the user selecting ok
	 * 
	 * @param jfcToReturn
	 * @return
	 */
	private File [] returnSelectedFiles(JFileChooser jfcToReturn)
	{		
		try
		{
			return jfcToReturn.getSelectedFiles();			
		}
		catch(Exception e)
		{
			this.eop("returnSelectedFiles", myClassName, e, false);
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	public  void eop(String mtdName, String myClassName, Exception e, boolean printStackTrace)
	{
		try
		{
			//String errorMsg = "Exception caught in " + mtdName + " mtd in " + myClassName;
			String errorMsg = "Error Encountered in " + mtdName + " mtd in " + myClassName;
			
//			if(this.db_console_out != null)
//				db_console_out.text_pane.appendString(errorMsg);
			
			if(e != null)
			{
				try
				{
					String newErrorMsg = errorMsg + " Error Msg: " + e.getLocalizedMessage();
					
					//no errors, reset back
					errorMsg = newErrorMsg;
				}
				catch(Exception ee)
				{
					//do n/t, original error msg remains
				}
			}
			
			System.out.println(errorMsg);
			
			if(printStackTrace && e != null)
			{
				e.printStackTrace(System.out);
			}
			
		}catch(Exception eee){}
	}
	
	
	public boolean sop(String out)
	{
		try
		{
			
			if(outputEnabled)
			{
				System.out.println(out);
			}
			
//			if(this.db_console_out != null)
//				db_console_out.text_pane.appendString(out);
			
			return true;
		}
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	public boolean sp(String line)
	{
		try
		{
			if(!outputEnabled)
				return true;
			
			System.out.print(line);			
		}
		catch(Exception e)
		{
			
		}
		
		return true;
	}
	
}
