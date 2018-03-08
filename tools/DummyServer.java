import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.PrintWriter;

public class DummyServer {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String message;

	public static void main(String[] args) {

		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			System.out.println("\nIP address: " + ip.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			serverSocket = new ServerSocket(9000);
		} catch (IOException e) {
			System.out.println("Could not listen on port!");
		}

		System.out.println("Server started. Listening to port 9000.\n");

		try {
			clientSocket = serverSocket.accept();
			System.out.println("Connected!\nWaiting for messages...\n");

			PrintWriter printwriter = new PrintWriter(clientSocket.getOutputStream(), true);
			printwriter.println("Hello back");
			printwriter.flush();
			printwriter.println("Hello back again!!!");
	        printwriter.flush();

			inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((message = bufferedReader.readLine()) != null) {
    			System.out.println(message);
    			// printwriter.println(message);
    			// printwriter.flush();
			}
			inputStreamReader.close();
			clientSocket.close();
		} catch (IOException ex) {
			System.out.println("Problem in message reading!");
		}

	}

}