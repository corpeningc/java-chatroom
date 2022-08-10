/* Created by Caleb Corpening
 * Conf Server for CSCE 416
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class ConfServer implements Runnable {

	private BufferedReader fromClientReader;

	private Socket clientSock;

	public List<Socket> sockList;

	public ConfServer(BufferedReader reader, Socket client, List<Socket> socks) {
		fromClientReader = reader;
		clientSock = client;
		sockList = socks;
	}

	public void run() {
	
		try {
			while (true) {

				String read = fromClientReader.readLine();
				System.out.println(read);

				if (read == null){
					clientSock.close();
					return;
				}
				for (Socket sock : sockList) {
					PrintWriter toSock = new PrintWriter(sock.getOutputStream(), true);
					toSock.println(read);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		System.exit(0);
	}
	
	public static void main(String args[]) {
		List<Socket> sockList = Collections.synchronizedList(new ArrayList<Socket>());
		if (args.length != 1) {
			System.out.println("Correct usage: java ConfServer <port>");
			System.exit(1);
		}

		ServerSocket servSocket = null;
		Socket clientSocket = null;

		try {
			servSocket = new ServerSocket(Integer.parseInt(args[0]));
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		while (true) {
		try {
			clientSocket = servSocket.accept();
			sockList.add(clientSocket);
			System.out.println("New connection established");
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		try {

			BufferedReader fromClientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			Thread child = new Thread(new ConfServer(fromClientReader, clientSocket, sockList));
			child.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	  }
	}

}
