import java.io.*;
import java.net.*;
import java.util.LinkedList;

import javax.swing.Timer;

import java.awt.event.*;

public class ThdServerSocket extends Thread implements Runnable, ActionListener
{
	public static final String myClassName = "ThdServerSocket";
	
	public volatile static int port = 9999;
	
	ServerSocket svr_skt = null;
	
	public static volatile boolean continue_run = true;
	
	public static volatile LinkedList<ThreadSocket> list_sockets = new LinkedList<ThreadSocket>();
	
	public static final int interrupt_millis = 100;
	public volatile Timer tmr = null;
	public volatile boolean handle_interrupt = true;
	public volatile static LinkedList<String> list_command_to_execute = new LinkedList<String>();
	public volatile static LinkedList<String> list_history = new LinkedList<String>();
	public static int history_cache_size = 30;
	
	public static int command_cache_size = 400;
	public static LinkedList<String> list_cache_command_output = new LinkedList<String>();
	
	public volatile String cmd = "";
	
	public static volatile boolean include_command_header = true;
	public static final String COMMAND_HEADER = "COMMAND-->";
	
	public static final String no_cache = "No commands have been executed yet...";
	
	/**states to interact with the same process, or to have a new process for each command line received from the input*/
	public static volatile boolean single_command_mode = true;
	
	public volatile Process process = null;
	
	//injest the streams
	BufferedReader brStdOut = null;
	BufferedReader brStdErr = null;
	PrintWriter pwOut = null;
	
	ThdStreamGobbler gobbler_stdout = null;
	ThdStreamGobbler gobbler_stderr = null;
	
	public ThdServerSocket(int port_num)
	{
		try
		{
			port = port_num;
			this.start();
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void run()
	{
		try
		{
			sop("WELCOME to " + Main.NAME + " verision " + Main.VERSION + " by Solomon Sonya @Carpenter1010");
			
			//attempt to bind to port
			sop("Bound to port:" + establish_server_socket(port));
			
			Socket skt = null;
			ThreadSocket thd = null;
			
			this.tmr = new Timer(this.interrupt_millis, this);
			this.tmr.start();
			
			while(continue_run)
			{
				try
				{
					skt = this.svr_skt.accept();
					thd = new ThreadSocket(skt);
				}
				catch(Exception e)
				{
					continue;
				}
			}
			
		}
		catch(Exception e)
		{
			this.eop(myClassName, "run", e);
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == tmr && handle_interrupt && list_command_to_execute.size() > 0)
				process_interrupt();
			
		}
		catch(Exception e)
		{
			this.eop(myClassName, "ae", e);
		}
	}
	
	public boolean process_interrupt()
	{
		try
		{
			if(list_command_to_execute.size() < 1)
				return true;
			
			if(!handle_interrupt)
				return false;
			
			handle_interrupt = false;
			
			//
			//extract command
			//
			cmd = ThdServerSocket.list_command_to_execute.removeFirst();
			
			//
			//execute command
			//
			if(cmd != null && !cmd.trim().equals(""))
			{
				execute(cmd);
			}				
			
			
			handle_interrupt = true;
			return true;
		}
		catch(Exception e)
		{
			this.eop(myClassName, "proocess_interrupt", e);
		}
		
		handle_interrupt = true;
		return false;
	}
	
	public boolean execute(String command)
	{
		try
		{
			//
			//Add process to history
			//
			list_history.addLast(command);
			if(list_history.size() > history_cache_size)
				list_history.removeFirst();
			
			//
			//Include a blank line in the output to demark new commands
			//
			this.broadcast("", true);
			
			//notify of the command
			if(include_command_header)
				this.broadcast(COMMAND_HEADER + command, false);
			
			
			//
			//Execute process
			//
			
			//
			//SINGLE COMMAND MODE
			//
			if(single_command_mode)
			{
				if(process == null)
				{
					//start a new process first
					if(Main.thisIsWindowsSystem)
						process = Runtime.getRuntime().exec("cmd.exe /C " + command);
					else
					{
						String [] cmd = new String [] {"/bin/bash", "-c", command};
						process = Runtime.getRuntime().exec(cmd);						
					}
					
					//injest the streams
					brStdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
					brStdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					pwOut = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
					
					gobbler_stdout = new ThdStreamGobbler(this, process, brStdOut, pwOut);
					gobbler_stderr = new ThdStreamGobbler(this, process, brStdErr, pwOut);
				}
				else//process already exists, so send this command to the process
				{
					try
					{
						pwOut.println(command);
						pwOut.flush();
					}
					catch(Exception e)
					{
						System.out.println("Process Appears to have closed streams for interaction...");
					}
				}
					
			}
			
			//
			//NOT SINGLE COMMAND MODE
			//
			else//then start a new process for each line received
			{
				//start a new process for line received 
				if(Main.thisIsWindowsSystem)
					process = Runtime.getRuntime().exec("cmd.exe /C " + command);
				else
				{
					String [] cmd = new String [] {"/bin/bash", "-c", command};
					process = Runtime.getRuntime().exec(cmd);						
				}
				
				//open gobblers
				brStdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
				brStdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				pwOut = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
				
				gobbler_stdout = new ThdStreamGobbler(this, process, brStdOut, pwOut);
				gobbler_stderr = new ThdStreamGobbler(this, process, brStdErr, pwOut);
			}
			
			
			
			
			
			
			
			System.gc();
			
			return true;
		}
		catch(Exception e)
		{
			this.eop(myClassName, "execute", e);
			//e.printStackTrace(System.out);
		}
		
		return false;
	}
	
	public static boolean broadcast(String line, boolean force_output)
	{
		try
		{
			if((line == null || line.trim().equals("")) && !force_output)
				return false;
			
			//
			//sop
			//
			sop(line);
			
			
				
			
			//
			//store the output cache			
			//
			if(!line.trim().equals("") && !line.equals(ThdServerSocket.no_cache))
				list_cache_command_output.addLast(line);
			
			if(list_cache_command_output.size() > command_cache_size)
			{
				try
				{
					while(list_cache_command_output.size() > command_cache_size)
						list_cache_command_output.removeFirst();
				}
				catch(Exception e)
				{
					sop("check while loop in " + myClassName);
				}
				
			}
			
			if(ThdServerSocket.list_sockets.size() < 1)
				return false;
						
			//
			//connected sockets
			//
			ThreadSocket socket = null;
			for(int i = 0; i < ThdServerSocket.list_sockets.size(); i++)
			{
				try
				{
					socket = list_sockets.get(i);
					socket.send(line);
				}
				catch(Exception e)
				{
					sop("check loop in broadcast mtd");
					continue;
				}
			}
			
			return true;
		}
		catch(Exception e)
		{
			eop(myClassName, "broadcast", e);
		}
		
		return false;
	}
	
	public int establish_server_socket(int prt) 
	{
		try
		{
			svr_skt = new ServerSocket(prt);
			
			sop("ServerSocket Established on port " + svr_skt.getLocalPort());
			
			return svr_skt.getLocalPort();
		}
		catch(BindException be)
		{
			//bind to next available port
			try 
			{
				sop("ERROR! I was unable to bind to expected port [" + prt + "]");
				
				svr_skt = new ServerSocket(0, 0, null);
				
				sop("Instead, I am bound to next available port [" + svr_skt.getLocalPort() + "]");
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			this.eop(myClassName, "establish_server_socket", e);
			e.printStackTrace(System.out);
		}
		
		return svr_skt.getLocalPort();
	}
	
	/**
	 * History only displays the actual commands entered for execution
	 * Cache shows the responses of commands executed
	 * @return
	 */
	public static boolean broadcastHistory()
	{
		try
		{
			if(ThdServerSocket.list_history.isEmpty())
			{
				ThdServerSocket.broadcast(no_cache, false);
				return true;
			}
			
			for(int i = 0; i < ThdServerSocket.list_history.size(); i++)
			{
				ThdServerSocket.broadcast(ThdServerSocket.list_history.get(i), false);
			}
			
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception caught in thdServerSocket broadcastHistory");
		}
		
		return false;
	}
	
	public static void sop(String out)	{	System.out.println(out);	}
	public static void eop(String myClassName, String mtdName, Exception e)
	{
		sop("Exception caught in " + myClassName + " class " + "in mtd: " + mtdName +   "  " + e.getLocalizedMessage());
	}
	
	/**
	 * History only displays the actual commands entered for execution
	 * Cache shows the responses of commands executed
	 * @return
	 */
	 public static boolean broadcastCache()
		{
			try
			{
				if(ThdServerSocket.list_cache_command_output.isEmpty())
				{
					ThdServerSocket.broadcast(ThdServerSocket.no_cache, false);
					return true;
				}
				
				for(int i = 0; i < ThdServerSocket.list_cache_command_output.size(); i++)
				{
					ThdServerSocket.broadcast(ThdServerSocket.list_cache_command_output.get(i), false);
				}
				
				return true;
			}
			catch(Exception e)
			{
				System.out.println("Exception caught in thdServerSocket broadcastCache");
			}
			
			return false;
		}
	 
	 
	 public static boolean toggleAcceptanceOfinputFromSockets()
	 {
		 try
		 {
			 ThreadSocket.accept_input = !ThreadSocket.accept_input;
			 
			 ThdServerSocket.broadcast(Main.NAME + " accepts input commands from connected sockets: " + ThreadSocket.accept_input, false);
			 
			 return true;
		 }
		 
		 catch(Exception e)
		 {
			 eop(myClassName, "toggleAcceptanceOfinputFromSockets", e);
		 }
		 
		 return false;
	 }

	 public static boolean toggleInputCommandHeader()
	 {
		 try
		 {
			 ThdServerSocket.include_command_header = !ThdServerSocket.include_command_header;
			 
			 ThdServerSocket.broadcast(Main.NAME + " will prepend the command header before providing results of executing each command: " + ThdServerSocket.include_command_header, false);
			 
			 return true;
		 }
		 
		 catch(Exception e)
		 {
			 eop(myClassName, "toggleInputCommandHeader", e);
		 }
		 
		 return false;
	 }
	 
	 public static boolean toggleSingleCommandMode()
	 {
		 try
		 {
			 ThdServerSocket.single_command_mode = !ThdServerSocket.single_command_mode;
			 
			 ThdServerSocket.broadcast("Single command mode enabled: " + ThdServerSocket.single_command_mode, false);
			 
			 
			 
			 return true;
		 }
		 
		 catch(Exception e)
		 {
			 eop(myClassName, "toggleSingleCommandMode", e);
		 }
		 
		 return false;
	 }
	 
}
