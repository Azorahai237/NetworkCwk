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
			// accept client connection and create a new thread to handle it. 
			// The thread is added to the thread pool. 
			// The thread pool is a fixed size, so if the thread pool is full, the client connection is rejected. 
			// The client connection is rejected if the server is shut down. 

			Socket client = server.accept();
			System.out.println("Client connected.");
		  	service.submit( new ClientHandler(client) );
		}

      
    } catch (IOException e) {
      System.err.println("Could not listen on port.");
      System.exit(-1);
    } finally {
		// Shutdown the executor service when the server terminates
		if (service != null) {
			service.shutdown();
		}
	}
  }
}