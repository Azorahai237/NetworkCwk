import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class Server {

  // As a demonstration, put everything into main(); obviously you would probably want
  // to use instance variables and break this up into separate methods for a real application.
  public static void main(String[] args) throws IOException {

    ServerSocket server = null;
    ExecutorService service = Executors.newCachedThreadPool();

    // Try to open up the listening port
    try {
		server = new ServerSocket(8080);
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