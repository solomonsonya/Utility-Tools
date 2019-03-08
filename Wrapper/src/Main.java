import java.io.*;
import java.net.*;

public class Main 
{
	
	public static final String NAME = "Wrapper";
	public static final String VERSION = "1.002";
	
	public static volatile boolean thisIsWindowsSystem = false;
	public static volatile boolean thisisUnixSystem = false;
	

	public static final String HISTORY = "history";
	public static final String CACHE = "cache";
	/**toggle accepting input fromt he connected sockets*/
	public static final String INPUT = "input";
	public static final String INCLUDE_COMMAND_HEADER = "header";
	public static final String SINGLE_COMMAND_MODE = "command";
	
	public static final String [] arrHelp = new String[] 
			{ 
					HISTORY + "\t\t - print history of commands executed", 
					//CACHE + "\t\t - print cache of responses of previously executed commands",
					INPUT + "\t\t - toggle accepting or ignoring commands as input from any connected socket into " + NAME,
					INCLUDE_COMMAND_HEADER + "\t\t - At the execution of every command, " + NAME + " includes a command header to the terminal to let you know what command was just executed. This toggles between appending the command header when each command is executed",
					SINGLE_COMMAND_MODE + "\t\t - Toggles if we are in single command mode or not. Single command mode means the same process will be used for all command received across the socket.  Not single command mode means a new process will be used each time a line is received from the terminal"
			};
	
	public static ThdServerSocket thd = null;
	
	public static void main(String[] args) 
	{
		try
		{
			// TODO Auto-generated method stub
			
			try
			{
				thisIsWindowsSystem = (System.getProperty("os.name")).startsWith("Windows");
				thisisUnixSystem = !thisIsWindowsSystem;
			}
			catch(Exception e)
			{
				System.out.println("I COULD NOT DETERMINE IF THIS IS A WINDOWS OR UNIX SYSTEM");
			}
			
			//
			//Establish ServerSocket Thread
			//			
			
			//determine if args were passed from cmd line
			if(args == null || args.length < 1)
			{
				thd = new ThdServerSocket(9999);
			}
			
			else//take the params
			{
				int port = 9999;
				try
				{
					port = Integer.parseInt(args[0].trim());
				}
				catch(Exception e)
				{
					port = 9999;
				}
				
				thd = new ThdServerSocket(port);
			}
			
			//
			//listen to standard in
			//
			BufferedReader brIn = new BufferedReader(new InputStreamReader(System.in));
			String line = "";
			
			while((line = brIn.readLine()) != null)
			{
				if(line.trim().equals(""))
					continue;
				
				//System.out.println("Ready to execute: " + line);
				//any command to execute, add it to the queue
				if(!determineCommand(line))
					ThdServerSocket.list_command_to_execute.addLast(line);
			}
		
		}
		catch(Exception e)
		{
			System.out.println("ERROR IN MAIN!");
		}
		
		
		
		
	}
	
	
	/**
	 * Determine if we know the command directive, if not, return false to try to execute the command
	 * @param line
	 * @return
	 */
	public static boolean determineCommand(String line)
	{
		try
		{
			if(line == null || line.trim().equals(""))
				return true;
			
			line = line.trim();
			
			//HISTORY, CACHE, INPUT, INCLUDE_COMMAND_HEADER
			
			if(line.toLowerCase().startsWith(HISTORY))
			{
				return ThdServerSocket.broadcastHistory();
			}
						
			else if(line.toLowerCase().startsWith(INPUT))
			{
				ThdServerSocket.toggleAcceptanceOfinputFromSockets();
				return true;
			}
			
			else if(line.toLowerCase().startsWith(INCLUDE_COMMAND_HEADER))
			{
				ThdServerSocket.toggleInputCommandHeader();
				return true;
			}
			
			else if(line.toLowerCase().startsWith("-h") || line.toLowerCase().equals("h") || line.toLowerCase().equals("help") || line.toLowerCase().equals("?") || line.toLowerCase().startsWith("display"))
			{
				return displayHelp();
			}
			
			else if(line.toLowerCase().startsWith(SINGLE_COMMAND_MODE))
			{
				thd.process = null;//reset the process instance
				ThdServerSocket.toggleSingleCommandMode();
				return true;
			}
			
			
			
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in Main determineCommand mtd");
		}
		
		return false;
	}
	
	
	
	public static void sop(String out){System.out.println(out);	}
	public static void eop(String myClassName, String mtdName, Exception e) {		sop("Exception caught in class " +  myClassName + " in mtd: " + mtdName + ". " + e.getLocalizedMessage());	}

	
	public  static boolean displayHelp()
	{
		try
		{
			for(int i = 0; i < arrHelp.length; i++)
			{
				System.out.println(arrHelp[i]);
			}
			
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in Main - displayHelp");
		}
		
		return false;
	}

}
