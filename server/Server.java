import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class Server {

  
  public static void main(String[] args) throws IOException {

    ServerSocket server = null;
	// manage fixed thread pool
    ExecutorService service = Executors.newFixedThreadPool(20);

    // Try to open up the listening port
    try {
		server = new ServerSocket(9237);
		System.out.println("Waiting for client connection...");
		
		while( true )
		{
			Socket client = server.accept();
			System.out.println("Client connected.");
		  	service.submit( new ClientHandler(client) );
		}

      
    } catch (IOException e) {
      System.err.println("Could not listen on port: 8080.");
      System.exit(-1);
    } finally {
		// Shutdown the executor service when the server terminates
		if (service != null) {
			service.shutdown();
		}
	}
  }
}