import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket socket = null;
    private static final String SERVER_FILE_DIR = "serverFiles";
    private PrintWriter output;

    public ClientHandler(Socket socket) {
        super("ClientHandler");
        this.socket = socket;
    }

    public void run() {

        try {

            // Input and output streams to/from the client.
            output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String CommandType = in.readLine();
            List<String> filenames = DirectoryListing.FileNamesInDirectory(SERVER_FILE_DIR);

            // cases for list and put commands
            switch (CommandType) {
                case "list":
                    // loop through all filenames and send them to client
                    for (String fileName : filenames) {
                        output.println(fileName);
                    }
                    output.flush();
                    ServerLogger.logRequest(socket.getInetAddress().getHostAddress(), "LIST");
                    break;

                case "put":
                    String fileName = in.readLine();
                    // check if file already in server
                    if (filenames.contains(fileName)) {
                        output.println("ERROR: File already exists.");

                    } else {
                        // creating new file in fileServer
                        File file = new File(SERVER_FILE_DIR + "/" + fileName);
                        file.createNewFile();
                        output.println("File created.");

                        //writing data to the new file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            int character;
                            while ((character = in.read()) != -1) {
                                char c = (char) character;
                                // if end of file reached stop reading characters
                                if (c == '\0') {
                                    break;
                                }
                                writer.write(c);
                            }
                            writer.flush();
                            output.println("File uploaded successfully.");
                            writer.close();
                        } catch (IOException e) {

                            e.printStackTrace();
                            output.println("ERROR: Failed to write data to file.");
                        }
                        ServerLogger.logRequest(socket.getInetAddress().getHostAddress(), "PUT " + fileName);
                    }
                    break;

                default:
                    output.println("Unknown command received by server.");
                    break;
            }

            // Free up resources for this connection.
            output.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}