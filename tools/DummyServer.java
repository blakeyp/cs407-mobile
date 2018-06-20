import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.io.IOException;

public class DummyServer {

	private static ServerSocket serverSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;

	public static void main(String[] args) {

		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			System.out.println("My IP address: " + ip.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			serverSocket = new ServerSocket(9000);
			System.out.println("Server started - listening to port 9000");
		} catch (IOException e) {
			System.out.println("Could not listen on port!");
		}

		Thread acceptThread = new Thread(new AcceptThread(serverSocket));
		acceptThread.start();


		// try {
			
		// 	connection = serverSocket.accept();
		// 	System.out.println("Connected!\nWaiting for messages...\n");



		// } catch (IOException ex) {
		// 	System.out.println("Problem in message reading!");
		// }


	}


}

class AcceptThread implements Runnable {

	private Socket connection;
	private ServerSocket serverSocket;

	public AcceptThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void run() {
		while (true) {
			try {
				connection = serverSocket.accept();
				System.out.println("connected to a new client wooooo");

				Thread connectionThread = new Thread(new ConnectionThread(connection));
				connectionThread.start();


			} catch (IOException e) {
				System.out.println("caught IOException from accept thread");
				e.printStackTrace();
			}
		}
	}

}

class ConnectionThread implements Runnable {

	private Socket connection;

	public ConnectionThread(Socket connection) {
		this.connection = connection;
	}

	public void run() {

		try (PrintWriter printWriter = new PrintWriter(connection.getOutputStream(),true);
			InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputReader)) {
		
			printWriter.println("Hello back");
			printWriter.flush();
			printWriter.println("Hello back again!!!");
	        printWriter.flush();		
			
			String message;
			while ((message = bufferedReader.readLine()) != null)
				System.out.println(message);

			try {
				connection.close();
			} catch (IOException e) {
				System.out.println("caught IOException from connection thread");
			}

		} catch (IOException e) {
			System.out.println("caught IOException from connection thread");
		}

	}

}