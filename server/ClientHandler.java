import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket socket = null;
    private static final String SERVER_FILE_DIR = "serverFiles";

    public ClientHandler(Socket socket) {
		super("ClientHandler");
		this.socket = socket;
    }

    public void run() {

		try {
           


			// Input and output streams to/from the client.
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String CommandType = in.readLine();
            // System.out.println(CommandType);
            List<String> filenames = DirectoryListing.FileNamesInDirectory(SERVER_FILE_DIR);
            // System.out.println(filenames);
            // System.out.println(filenames);
            switch(CommandType){
                case "list":
                    for (String fileName : filenames) {
                        // System.out.println(fileName);
                        out.println(fileName);
                    }
                    out.println("END_OF_LIST");
                    break;

                case "put":
                    out.println("copy file");
                    break;



            }

			// Free up resources for this connection.
			// out.close();
			// in.close();
			// socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}