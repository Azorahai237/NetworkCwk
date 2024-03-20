import java.io.*;
import java.net.*;

public class Client {

	private Socket socket = null;
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;

	public void client(String command, String filename) {

		try {

			// Try and create the socket. This assumes the server is running on the same
			// machine, "locaslhost".
			socket = new Socket("localhost", 9237);

			// Chain a writing stream
			socketOutput = new PrintWriter(socket.getOutputStream(), true);

			// Chain a reading stream
			socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host.\n");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to host.\n");
			System.exit(1);
		}

		// handling list command
		if (command.equals("list")) {
			// Check if filename is null. If not, the list command is invalid and the client
			// should
			// not be able to send the command to thhe server
			if (filename != null) {
				System.err.println("Usage: java Client list");
				System.exit(1);
			}
			// send command to server
			// only need to send command to server
			socketOutput.println(command);
			socketOutput.flush();

			try {
				String line;
				// reading server response
				while ((line = socketInput.readLine()) != null) {
					System.out.println("Received from server: " + line);
				}
			} catch (IOException e) {
				System.err.println("Error reading response from the server: " + e.getMessage());

			}
		}

		// handling put command
		else if (command.equals("put")) {
			try {

				// Open the file
				File file = new File(filename);
				BufferedReader fileReader = new BufferedReader(new FileReader(file));

				// Create a buffer to read the file data in chunks
				char[] buffer = new char[100000];
				// send command and filename to server
				socketOutput.println(command);
				socketOutput.println(filename);
				// Read and send the file data in characaters
				int charsRead;
				while ((charsRead = fileReader.read(buffer)) != -1) {
					socketOutput.write(buffer, 0, charsRead);
				}
				fileReader.close();
				socketOutput.println('\0');

			} catch (FileNotFoundException e) {
				System.err.println("File not found: " + filename);
				System.exit(1);
				// Handle the case where the specified file is not found
			} catch (IOException e) {
				System.err.println("Error reading file: " + filename);
				System.exit(1);
				// Handle other I/O exceptions, such as problems reading from the file
			}
			// Close the input stream
			// Flush the output stream to ensure that all data is sent
			socketOutput.flush();
			// reading response from server
			try {
				String line;
				while ((line = socketInput.readLine()) != null) {
					System.out.println("Received from server: " + line);

				}
			} catch (IOException e) {
				System.err.println("Error reading response from the server: " + e.getMessage());

			}

		} else {
			System.err.println("Command not supported. Usage: java Client list|put <file.txt>");
		}

		// Close the socket
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Error closing socket.");
			System.exit(1);
		}
	}

	public static void main(String[] args) {

		// command line arguments parsing
		String command = null;
		String filename = null;
		// arg length should be either 1 or 2, rest are invalid commands
		if (args.length <= 0 || args.length >= 3) {
			System.err.println("Usage: java Client list|put <file.txt>");
			System.exit(0);
		}
		// saving cmd line args
		command = args[0];
		if (args.length == 2) {
			filename = args[1];
		}
		// open client
		Client client = new Client();
		client.client(command, filename);
	}
}
