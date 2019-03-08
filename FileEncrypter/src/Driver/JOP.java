package Driver;
import javax.swing.*;


public class JOP 
{
	
	
	public JOP(){}
	
	
	
	
	public  void jop(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Unable to complete selected action...", JOptionPane.INFORMATION_MESSAGE);
	}
	public  void jop_Message(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public  String jop_Query(String strMsg, String strTitle)
	{
		Object o = strMsg;
		
		return JOptionPane.showInputDialog(null, o, strTitle, JOptionPane.QUESTION_MESSAGE);
	}
		
	public  Object jop_queryJComboBox(String strMessage, String title, String[] arrElements)
	{
		return JOptionPane.showInputDialog(null, strMessage, title, JOptionPane.QUESTION_MESSAGE, null, arrElements, arrElements[0]);
		
	}
	
	public int jop_Query_Custom_Buttons(String msg, String title, Object [] buttons)
	{
		return jop_custom_buttons(msg, title, buttons);
	}
	
	public int jop_custom_buttons(String msg, String title, Object [] buttons)
	{
		
		return JOptionPane.showOptionDialog(null, msg, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,	null, buttons, buttons[0]);
	}
	
	
	public  int jop_Confirm(String strText, String strTitle)
	{
		try
		{
			//try{Main.playSound(sound_Note);}catch(Exception e){}
			
			return JOptionPane.showConfirmDialog(null, strText, strTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		catch(Exception e)
		{
			//Drivers.eop("queryDialog", strMyClassName, e, e.getMessage(), true);
		}
		
		return -1;
	}
	
	public  int jop_Confirm_YES_NO_CANCEL(String strText, String strTitle)
	{
		try
		{
			return JOptionPane.showConfirmDialog(null, strText, strTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		catch(Exception e)
		{
			//Drivers.eop("queryDialog", strMyClassName, e, e.getMessage(), true);
		}
		
		return -1;
	}	
	
	public  String jop_Password(String strMsg)
	{
		JPasswordField passwordField = new JPasswordField();

		if(JOptionPane.showConfirmDialog(null, passwordField, strMsg, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
		{
			//Drivers.sop("Entered PIN: " + new String(passwordField.getPassword()));
			return new String(passwordField.getPassword());
		}
		
		//otw
		return null;

	}
	
	public  boolean jop_Error(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.ERROR_MESSAGE);
		
		//since we're displaying an error, we'll assume the default return type is false;
		return false;
	}
	
	public  boolean jop_Error(String strMsg)
	{
		JOptionPane.showMessageDialog(null, strMsg, "Unable to Continue", JOptionPane.ERROR_MESSAGE);
		
		//since we're displaying an error, we'll assume the default return type is false;
		return false;
	}
	
	public  boolean jop_Error(String strMsg, boolean playErrorSound)
	{
		try
		{
			if(playErrorSound)
			{
				//try{	Main.playSound(Drivers.sound_Error);	}	catch(Exception ee){}
			}
			
			JOptionPane.showMessageDialog(null, strMsg, "* * Unable to Complete Selected Action... * *", JOptionPane.ERROR_MESSAGE);
		}catch(Exception e){}
		
		//since we're displaying an error, we'll assume the default return type is false;
		return false;
	}
	
	public  void jop_Warning(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.WARNING_MESSAGE);
	}
	
	public  void jop_Message(String strMsg, String strTitle)
	{
		JOptionPane.showMessageDialog(null, strMsg, strTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public  void pause()
	{
		try
		{
			jop("Pause...");
		}
		catch(Exception e){}
	}

}
