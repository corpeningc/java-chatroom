/* Created by Caleb Corpening
 * CSCE 416 conf client
 */

import java.net.*;
import java.io.*;

public class ConfClient implements Runnable {

	private BufferedReader fromUserReader;
	private PrintWriter toSocketWriter;
	private String name;

	public ConfClient(BufferedReader reader, PrintWriter writer, String n) {
		fromUserReader = reader;
		toSocketWriter = writer;
		name = n;
	}
	// want this run to be the thread responsible for writing to socket
	public void run() {
	  try{
		while (true) {
			String line = fromUserReader.readLine();

			if (line == null)
				break;
			toSocketWriter.println(name + ": " +line);
		}
	  } catch(Exception e) {
		  System.out.println(e);
		  System.exit(1);
	  }
	  System.exit(0);
	}


	// thread responsible for recieving messages and printing to the clients window
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.println("Incorrect usage: ConfClient <host> <port> <name>");
			System.exit(1);
		}

		Socket sock = null;

	  try {
		sock = new Socket(args[0], Integer.parseInt(args[1]));
		System.out.println("Connected to server at " + args[0]);
	  } catch (Exception e) {
	  	System.out.println(e);
		System.exit(1);
	  }

	  try {
		PrintWriter toSocketWriter = new PrintWriter(sock.getOutputStream(), true);

		BufferedReader fromUserReader = new BufferedReader(new InputStreamReader(System.in));

		Thread child = new Thread(new ConfClient(fromUserReader, toSocketWriter, args[2]));
		
		child.start();


	  } catch (Exception e) {
		  System.out.println(e);
		  System.exit(1);
	  }

	try {
		BufferedReader fromSocketReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		while (true) {
			String line = fromSocketReader.readLine();
			if (line == null) {
				System.out.println("Quitting");
				break;
			}
			
			System.out.println(line);
			
		}
	} catch (Exception e) {
		System.out.println(e);
		System.exit(1);
	}
		System.exit(0);
	}


}
