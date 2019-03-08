import java.io.*;
import java.net.*;


public class ThreadSocket extends Thread implements Runnable
{
	public static final String myClassName = "ThreadSocket";
	public Socket mySocket = null;
	
	public volatile BufferedReader brIn = null;
	public volatile PrintWriter pwOut = null;
	
	public static volatile boolean accept_input = false;
	
	public volatile String myRemoteSocketAddress = "";
	
	public static final String WELCOME = "Successfully connected to " + Main.NAME + " verision " + Main.VERSION + " by Solomon Sonya @Carpenter1010";
	
	public ThreadSocket(Socket skt)
	{
		try
		{
			mySocket = skt;
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
			
			
			brIn = new BufferedReader(new InputStreamReader(this.mySocket.getInputStream()));
			pwOut = new PrintWriter(new OutputStreamWriter(this.mySocket.getOutputStream()));
			
			String line = "";
			
			//
			//add self to the list
			//
			ThdServerSocket.list_sockets.addLast(this);
			
			myRemoteSocketAddress = "" + mySocket.getRemoteSocketAddress();
			
			sop("New Socket Thread started for address: " + myRemoteSocketAddress);
			sop("Num total connected sockets: " + ThdServerSocket.list_sockets.size());
			
			//
			//
			//NOTIFY
			this.send(WELCOME);
			
			//listen to socket
			while((line = brIn.readLine()) != null)
			{
				try
				{
					if(line.trim().equals(""))
						continue;
					
					//sop("received line: " + line);
					
					//only purpose is to listen to commands across socket, and add to the queue to be executed
					//upon next interrupt
					if(accept_input)
					{
						//sop("ready to process line: " + line);
						ThdServerSocket.list_command_to_execute.addLast(line);
					}
					else
					{
						this.send("Your command \"" + line + "\" is not accepted at this time...");
					}
					
					//this.send("You sent me: "+ line.toUpperCase());
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
		
		try 	{			ThdServerSocket.list_sockets.remove(this);			}			catch(Exception e){}
		
		sop("Connection closed for socket: " + myRemoteSocketAddress + ". Num connected sockets:  " + ThdServerSocket.list_sockets.size());
	}
	
	public boolean send(String line)
	{
		try
		{
			this.pwOut.println(line);
			this.pwOut.flush();
			return true;
		}
		catch(Exception e)
		{
			this.eop(myClassName, "send", e);
		}
		
		return false;
	}
	
	public void sop(String out){System.out.println(out);	}
	public void eop(String myClassName, String mtdName, Exception e) {		sop("Exception caught in class " +  myClassName + " in mtd: " + mtdName + ". " + e.getLocalizedMessage());	}

}
